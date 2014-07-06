package com.cisco.claims;

import com.cisco.claims.dto.Claim;
import com.cisco.claims.excel.ClaimsExtractor;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposAssistant;
import com.cisco.prepos.services.PreposService;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static com.cisco.prepos.dto.Prepos.Status.PROCESSED;
import static com.cisco.prepos.dto.Prepos.Status.WAITING;

/**
 * Created by Alf on 05.07.2014.
 */
@Component("claimsImporter")
public class DefaultClaimsImporter implements ClaimsImporter {

	private final Logger logger = LoggerFactory.getLogger(DefaultClaimsImporter.class);

	@Autowired
	private ClaimsExtractor claimsExtractor;

	@Autowired
	private PreposService preposService;

	@Override
	public void importClaims(InputStream inputStream) {

		List<Claim> claims = claimsExtractor.extract(inputStream);

		if (CollectionUtils.isEmpty(claims)) {
			throw new CiscoException("Exported from excel claims are null or empty. Please, check file.");
		}

		List<Prepos> preposes = preposService.getPreposes(WAITING);
		Table<String, String, Prepos> preposTable = PreposAssistant.asTable(preposes);

		Set<String> posreadyIdSet = Sets.newHashSet();

		for (Claim claim : claims) {

			String partNumber = claim.getPartNumber();
			String shippedBillNumber = claim.getShippedBillNumber();

			Prepos prepos = preposTable.get(partNumber, shippedBillNumber);

			updatePreposWithDataFromClaim(posreadyIdSet, claim, partNumber, shippedBillNumber, prepos);
		}

		ListMultimap<String, Prepos> posreadyIdMultimap = PreposAssistant.asListMultimap(preposes);

		for (String posreadyId : posreadyIdSet) {
			updatePreposStatus(posreadyIdMultimap, posreadyId);
		}

		preposService.update(preposes);

		logger.info("Claims have been imported and processed");
	}

	private void updatePreposWithDataFromClaim(Set<String> posreadyIdSet, Claim claim, String partNumber, String shippedBillNumber, Prepos prepos) {
		if(prepos == null) {
			throw new CiscoException(String.format("No prepos found for claim with PN=%s and SBN=%s", partNumber, shippedBillNumber));
		}

		String preposPosreadyId = prepos.getPosreadyId();
		if(StringUtils.isBlank(preposPosreadyId)) {
			throw new CiscoException(String.format("Prepos posreadyId shouldn`t be empty! Please contact support (PN  = %s)", prepos.getPartNumber()));
		}

		posreadyIdSet.add(preposPosreadyId);

		prepos.setClaimId(claim.getClaimId());
		prepos.setBatchId(claim.getBatchId());
	}

	private void updatePreposStatus(ListMultimap<String, Prepos> posreadyIdMultimap, String posreadyId) {
		List<Prepos> preposesPerPosready = posreadyIdMultimap.get(posreadyId);
		for (Prepos prepos : preposesPerPosready) {
			if(preposStatusShouldBeChangedToProcessed(prepos)) {
				prepos.setStatus(PROCESSED);
			}
		}
	}

	private boolean preposStatusShouldBeChangedToProcessed(Prepos prepos) {
		return (StringUtils.isBlank(prepos.getFirstPromo()) && StringUtils.isBlank(prepos.getSecondPromo()) || prepos.getClaimId() != 0);
	}
}

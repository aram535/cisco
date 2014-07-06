package com.cisco.claims.excel;

import com.cisco.claims.dto.Claim;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;


public class ExcelClaimsExtractorTest {

	private ClaimsExtractor dartsExtractor = new ExcelClaimsExtractor();

	@Test
	public void thatClaimsExtractedFromCorrectFile() throws URISyntaxException {
		InputStream inputStream = this.getClass().getResourceAsStream("/templates/Claims.xls");

		List<Claim> extractedClaims = dartsExtractor.extract(inputStream);

		assertThat(extractedClaims).isNotNull().isNotEmpty().hasSize(2);
		assertThat(extractedClaims).containsAll(createExpectedClaims());
	}

	private List<Claim> createExpectedClaims() {

		Claim firstClaim = new Claim("Part number 1", "1/1", 1, 1);
		Claim secondClaim = new Claim("Part number 2", "1/2", 2, 1);

		return Lists.newArrayList(firstClaim, secondClaim);
	}
}
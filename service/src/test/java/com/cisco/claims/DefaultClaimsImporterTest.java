package com.cisco.claims;

import com.cisco.claims.dto.Claim;
import com.cisco.claims.excel.ClaimsExtractor;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.PreposService;
import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.List;

import static com.cisco.testtools.TestObjects.PART_NUMBER;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static com.cisco.testtools.TestObjects.PreposFactory.newPreposWithoutPromos;
import static com.cisco.testtools.TestObjects.SHIPPED_BILL_NUMBER;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultClaimsImporterTest {

	public static final long CLAIM_ID = 1;
	public static final long BATCH_ID = 2;
	public static final long ANOTHER_CLAIM_ID = 3;
	public static final long ANOTHER_BATCH_ID = 4;
	public static final String ANOTHER_PART_NUMBER = "another part number";
	public static final String ANOTHER_SHIPPED_BN = "another shipped bn";
	public static final String POSREADY_ID = "posreadyId";

	@InjectMocks
	private DefaultClaimsImporter claimsImporter;

	@Mock
	private ClaimsExtractor claimsExtractor;

	@Mock
	private PreposService preposService;

	@Mock
	private InputStream inputStream;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void thatImportDartsThrowsCiscoExceptionIfExportedDataIsEmptyOrNull() {

		when(claimsExtractor.extract(inputStream)).thenReturn(null);

		expectedException.expect(CiscoException.class);
		expectedException.expectMessage("Exported from excel claims are null or empty. Please, check file.");

		claimsImporter.importClaims(inputStream);
	}

	@Test
	public void thatExceptionIsThrownWhenPreposNotFoundForClaim() throws Exception {

		List<Claim> claims = getClaims();
		Prepos prepos = newPrepos();
		prepos.setPosreadyId(POSREADY_ID);

		when(claimsExtractor.extract(inputStream)).thenReturn(claims);
		when(preposService.getPreposes(Prepos.Status.WAITING)).thenReturn(Lists.newArrayList(prepos));

		expectedException.expect(CiscoException.class);
		expectedException.expectMessage(String.format("No prepos found for claim with PN=%s and SBN=%s", ANOTHER_PART_NUMBER, ANOTHER_SHIPPED_BN));

		claimsImporter.importClaims(inputStream);
	}

	@Test
	public void thatExceptionIsThrownWhenPosreadyIdIsBlank() throws Exception {

		List<Claim> claims = getClaims();

		when(claimsExtractor.extract(inputStream)).thenReturn(claims);
		when(preposService.getPreposes(Prepos.Status.WAITING)).thenReturn(Lists.newArrayList(newPrepos()));

		expectedException.expect(CiscoException.class);
		expectedException.expectMessage(String.format("Prepos posreadyId shouldn`t be empty! Please contact support (PN  = %s)", PART_NUMBER));

		claimsImporter.importClaims(inputStream);
	}

	@Test
	public void thatPreposIsUpdatedWithDataFromClaim() throws Exception {

		List<Claim> claims = getClaims();

		when(claimsExtractor.extract(inputStream)).thenReturn(claims);
		when(preposService.getPreposes(Prepos.Status.WAITING)).thenReturn(getPreposes());

		claimsImporter.importClaims(inputStream);

		verify(preposService, times(1)).getPreposes(Prepos.Status.WAITING);
		verify(preposService, times(1)).update(getUpdatedPreposes());
		verifyNoMoreInteractions(preposService);
	}

	@Test
	public void thatPreposWithEmptyPromosIsAlsoUpdatedWhenCorrecpondingClaimIsImported() throws Exception {

		List<Claim> claims = getClaims();

		when(claimsExtractor.extract(inputStream)).thenReturn(claims);
		when(preposService.getPreposes(Prepos.Status.WAITING)).thenReturn(getPreposesWithoutPromos());

		claimsImporter.importClaims(inputStream);

		verify(preposService, times(1)).getPreposes(Prepos.Status.WAITING);
		verify(preposService, times(1)).update(getUpdatedPreposesWithoutPromos());
		verifyNoMoreInteractions(preposService);
	}

	private List<Claim> getClaims() {

		Claim firstClaim = new Claim(PART_NUMBER, SHIPPED_BILL_NUMBER, CLAIM_ID, BATCH_ID);
		Claim secondClaim = new Claim(ANOTHER_PART_NUMBER, ANOTHER_SHIPPED_BN, ANOTHER_CLAIM_ID, ANOTHER_BATCH_ID);

		return Lists.newArrayList(firstClaim, secondClaim);

	}

	private List<Prepos> getPreposes() {

		Prepos firstPrepos = newPrepos();
		firstPrepos.setPosreadyId(POSREADY_ID);
		Prepos secondPrepos = newPrepos();
		secondPrepos.setPartNumber(ANOTHER_PART_NUMBER);
		secondPrepos.setShippedBillNumber(ANOTHER_SHIPPED_BN);
		secondPrepos.setPosreadyId(POSREADY_ID);

		return Lists.newArrayList(firstPrepos, secondPrepos);
	}

	private List<Prepos> getPreposesWithoutPromos() {

		Prepos firstPrepos = newPreposWithoutPromos();
		firstPrepos.setPosreadyId(POSREADY_ID);
		Prepos secondPrepos = newPrepos();
		secondPrepos.setPartNumber(ANOTHER_PART_NUMBER);
		secondPrepos.setShippedBillNumber(ANOTHER_SHIPPED_BN);
		secondPrepos.setPosreadyId(POSREADY_ID);

		return Lists.newArrayList(firstPrepos, secondPrepos);
	}


	private List<Prepos> getUpdatedPreposes() {

		Prepos firstPrepos = newPrepos();
		firstPrepos.setPosreadyId(POSREADY_ID);
		firstPrepos.setClaimId(CLAIM_ID);
		firstPrepos.setBatchId(BATCH_ID);
		firstPrepos.setStatus(Prepos.Status.PROCESSED);
		Prepos secondPrepos = newPrepos();
		secondPrepos.setPosreadyId(POSREADY_ID);
		secondPrepos.setPartNumber(ANOTHER_PART_NUMBER);
		secondPrepos.setShippedBillNumber(ANOTHER_SHIPPED_BN);
		secondPrepos.setClaimId(ANOTHER_CLAIM_ID);
		secondPrepos.setBatchId(ANOTHER_BATCH_ID);
		secondPrepos.setStatus(Prepos.Status.PROCESSED);

		return Lists.newArrayList(firstPrepos, secondPrepos);
	}

	private List<Prepos> getUpdatedPreposesWithoutPromos() {

		Prepos firstPrepos = newPreposWithoutPromos();
		firstPrepos.setPosreadyId(POSREADY_ID);
		firstPrepos.setClaimId(CLAIM_ID);
		firstPrepos.setBatchId(BATCH_ID);
		firstPrepos.setStatus(Prepos.Status.PROCESSED);
		Prepos secondPrepos = newPrepos();
		secondPrepos.setPosreadyId(POSREADY_ID);
		secondPrepos.setPartNumber(ANOTHER_PART_NUMBER);
		secondPrepos.setShippedBillNumber(ANOTHER_SHIPPED_BN);
		secondPrepos.setClaimId(ANOTHER_CLAIM_ID);
		secondPrepos.setBatchId(ANOTHER_BATCH_ID);
		secondPrepos.setStatus(Prepos.Status.PROCESSED);

		return Lists.newArrayList(firstPrepos, secondPrepos);
	}
}
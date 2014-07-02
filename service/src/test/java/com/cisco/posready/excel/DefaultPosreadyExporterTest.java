package com.cisco.posready.excel;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static com.cisco.posready.excel.PosreadyConstants.*;
import static com.cisco.posready.excel.PosreadyConstants.ColumnId.*;
import static com.cisco.posready.excel.PosreadyConstants.POS_READY_SHEET_NAME;
import static com.cisco.posready.excel.PosreadyConstants.RESELLER_COUNTRY;
import static com.cisco.testtools.TestObjects.ClientsFactory.newClient;
import static com.cisco.testtools.TestObjects.DartsFactory.getDartsTable;
import static com.cisco.testtools.TestObjects.PART_NUMBER;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static com.cisco.testtools.TestObjects.PricelistsFactory.newPricelist;
import static com.cisco.testtools.TestObjects.PromosFactory.newPromo;
import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPosreadyExporterTest {

	@InjectMocks
	private DefaultPosreadyExporter posreadyExporter = new DefaultPosreadyExporter();

	@Spy
	private DefaultPosreadyFieldsBuilder posreadyFieldsBuilder;

	private static final String SELECTED_DART_AUTHORIZATION_NUMBER = "other authorization number";
	private static final String SELECTED_DART_END_USER_NAME = "other end user name";
	private static final int OTHER_GPL = 270;

	private final Prepos prepos = newPrepos();
	private final Pricelist newPricelist = newPricelist(OTHER_GPL);
	private final Promo promo = newPromo("other promo code");
	private final Map<String, Client> clientsMap = of(prepos.getClientNumber(), newClient());
	private final Table<String, String, Dart> dartsTable = getDartsTable();
	private final Map<String, Pricelist> pricelistMap = of(prepos.getPartNumber(), newPricelist);
	private final Map<String, Promo> promosMap = of(PART_NUMBER, promo);

	@Test(expected = CiscoException.class)
	public void thatThrowsExceptionWhenInputIsEmpty() throws Exception {

		posreadyExporter.exportDarts(Lists.<Prepos>newArrayList(), clientsMap, pricelistMap, dartsTable, promosMap);
	}

	@Test
	public void thatExportedPosreadysTitleRowContainsCorrectNumberOfColunms() throws Exception {

		Prepos prepos = newPrepos();

		Workbook workbook = posreadyExporter.exportDarts(Lists.newArrayList(prepos), clientsMap, pricelistMap, dartsTable, promosMap);

		Sheet posReadySheet = workbook.getSheet(POS_READY_SHEET_NAME);

		Row row = posReadySheet.getRow(0);

		verify(posreadyFieldsBuilder, times(TOTAL_CELL_COUNT)).buildStringCell((Row) anyObject(), anyInt(), anyString());
		assertEquals(TOTAL_CELL_COUNT, row.getPhysicalNumberOfCells());
	}

	@Test
	public void thatSinglePreposResultsInCorrectDataRow() throws Exception {

		Prepos prepos = newPrepos();

		Workbook workbook = posreadyExporter.exportDarts(Lists.newArrayList(prepos), clientsMap, pricelistMap, dartsTable, promosMap);

		Sheet posReadySheet = workbook.getSheet(POS_READY_SHEET_NAME);

		Row row = posReadySheet.getRow(1);

		Client client = clientsMap.get(prepos.getClientNumber());
		Pricelist pricelist = pricelistMap.get(prepos.getPartNumber());
		Dart dart = dartsTable.get(prepos.getPartNumber(), prepos.getSecondPromo());

		assertEquals(row.getCell(PARTNER_NAME_COLUMN).getStringCellValue(), prepos.getPartnerName());
		assertEquals(row.getCell(CLIENT_NUMBER_COLUMN).getStringCellValue(), prepos.getClientNumber());
		assertEquals(row.getCell(CLIENT_ADRESS_COLUMN_1).getStringCellValue(), client.getAddress());
		assertEquals(row.getCell(CLIENT_ADRESS_COLUMN_2).getStringCellValue(), client.getAddress());
		assertEquals(row.getCell(CLIENT_CITY_COLUMN).getStringCellValue(), client.getCity());
		assertEquals(row.getCell(ZIP_CODE_COLUMN).getStringCellValue(), String.valueOf(prepos.getZip()));
		assertEquals(row.getCell(RESELLER_COUNTRY_COLUMN).getStringCellValue(), RESELLER_COUNTRY);
		assertEquals(row.getCell(SHIPPED_DATE_COLUMN).getStringCellValue(), prepos.getShippedDate().toString());
		assertEquals(row.getCell(SHIPPED_BN_COLUMN).getStringCellValue(), prepos.getShippedBillNumber());
		assertEquals(row.getCell(PART_NUMBER_COLUMN).getStringCellValue(), prepos.getPartNumber());
		assertEquals(row.getCell(PROD_DESC_COLUMN).getStringCellValue(), prepos.getPartNumber());
		assertEquals(row.getCell(QUANTITY_COLUMN_1).getStringCellValue(), String.valueOf(prepos.getQuantity()));
		assertEquals(row.getCell(WPL_COLUMN).getStringCellValue(), String.valueOf(pricelist.getWpl()));
		assertEquals(row.getCell(BUY_PRICE_COLUMN).getStringCellValue(), String.valueOf(prepos.getBuyPrice()));
		assertEquals(row.getCell(SERIALS_COLUMN).getStringCellValue(), prepos.getSerials());
		assertEquals(row.getCell(END_USER_COLUMN).getStringCellValue(), prepos.getEndUser());
		assertEquals(row.getCell(END_CUSTOMER_CITY_COLUMN).getStringCellValue(), client.getCity());
		assertEquals(row.getCell(END_CUSTOMER_COUNTRY_COLUMN).getStringCellValue(), RESELLER_COUNTRY);
		assertEquals(row.getCell(AUTH_NUMBER_COLUMN).getStringCellValue(), prepos.getSecondPromo());
		assertEquals(row.getCell(PROMO_VERSION_COLUMN).getStringCellValue(), String.valueOf(dart.getVersion()));
		assertEquals(row.getCell(QUANTITY_COLUMN_2).getStringCellValue(), String.valueOf(prepos.getQuantity()));
		assertEquals(row.getCell(CLAIM_PER_UNIT_COLUMN).getStringCellValue(), String.valueOf(dart.getClaimUnit()));
		assertEquals(row.getCell(EXTENDED_CLAIM_AMOUNT_COLUMN).getStringCellValue(), String.valueOf(dart.getClaimUnit() * prepos.getQuantity()));

	}

	//TODO END user or partner name logic
	//TODO Promo1 / Promo2 logic
}
package com.cisco.posready.excel;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static com.cisco.posready.excel.PosreadyConstants.*;
import static com.cisco.posready.excel.PosreadyConstants.ColumnId.*;
import static com.cisco.prepos.services.discount.utils.DiscountPartCounter.getRoundedDouble;

/**
 * Created by Alf on 02.07.2014.
 */
public class DefaultPosreadyExporter implements PosreadyExporter {

	@Autowired
	private PosreadyFieldsBuilder posreadyFieldsBuilder;

	@Override
	public Workbook exportDarts(List<Prepos> preposes, Map<String, Client> clientMap,
	                            Map<String, Pricelist> pricelistsMap,
	                            Table<String, String, Dart> dartsTable, Map<String, Promo> promosMap) {

		if(preposes.isEmpty()) {
			throw new CiscoException("Nothing to export. Prepos list is empty");
		}

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(POS_READY_SHEET_NAME);

		createTitleRow(sheet);

		int i = 1;
		for (Prepos prepos : preposes) {

			Row dataRow = posreadyFieldsBuilder.createEmptyDataRow(sheet, i++, TOTAL_CELL_COUNT);

			Client client = clientMap.get(prepos.getClientNumber());
			Pricelist pricelist = pricelistsMap.get(prepos.getPartNumber());

			posreadyFieldsBuilder.setStringCellValue(dataRow, PARTNER_NAME_COLUMN, prepos.getPartnerName());
			posreadyFieldsBuilder.setStringCellValue(dataRow, CLIENT_NUMBER_COLUMN, prepos.getClientNumber());
			posreadyFieldsBuilder.setStringCellValue(dataRow, CLIENT_ADRESS_COLUMN_1, client.getAddress());
			posreadyFieldsBuilder.setStringCellValue(dataRow, CLIENT_ADRESS_COLUMN_2, client.getAddress());
			posreadyFieldsBuilder.setStringCellValue(dataRow, CLIENT_CITY_COLUMN, client.getCity());
			posreadyFieldsBuilder.setStringCellValue(dataRow, ZIP_CODE_COLUMN, String.valueOf(prepos.getZip()));
			posreadyFieldsBuilder.setStringCellValue(dataRow, RESELLER_COUNTRY_COLUMN, RESELLER_COUNTRY);
			posreadyFieldsBuilder.setStringCellValue(dataRow, SHIPPED_DATE_COLUMN, prepos.getShippedDate().toString());
			posreadyFieldsBuilder.setStringCellValue(dataRow, SHIPPED_BN_COLUMN, prepos.getShippedBillNumber());
			posreadyFieldsBuilder.setStringCellValue(dataRow, PART_NUMBER_COLUMN, prepos.getPartNumber());
			posreadyFieldsBuilder.setStringCellValue(dataRow, PROD_DESC_COLUMN, prepos.getPartNumber());
			posreadyFieldsBuilder.setStringCellValue(dataRow, QUANTITY_COLUMN_1, String.valueOf(prepos.getQuantity()));
			posreadyFieldsBuilder.setStringCellValue(dataRow, WPL_COLUMN, String.valueOf(pricelist.getWpl()));
			posreadyFieldsBuilder.setStringCellValue(dataRow, BUY_PRICE_COLUMN, String.valueOf(prepos.getBuyPrice()));
			posreadyFieldsBuilder.setStringCellValue(dataRow, SERIALS_COLUMN, prepos.getSerials());

			String endUser = getEndUser(prepos);
			posreadyFieldsBuilder.setStringCellValue(dataRow, END_USER_COLUMN, endUser);

			posreadyFieldsBuilder.setStringCellValue(dataRow, END_CUSTOMER_CITY_COLUMN, client.getCity());
			posreadyFieldsBuilder.setStringCellValue(dataRow, END_CUSTOMER_COUNTRY_COLUMN, RESELLER_COUNTRY);

			String promoCode = getPromoCode(prepos);
			posreadyFieldsBuilder.setStringCellValue(dataRow, AUTH_NUMBER_COLUMN, promoCode);

			String promoVersion = getPromoVersion(prepos, dartsTable, promosMap);
			posreadyFieldsBuilder.setStringCellValue(dataRow, PROMO_VERSION_COLUMN, promoVersion);
			posreadyFieldsBuilder.setStringCellValue(dataRow, QUANTITY_COLUMN_2, String.valueOf(prepos.getQuantity()));

			String claimPerUnit = getClaimPerUnit(prepos, dartsTable, promosMap);
			posreadyFieldsBuilder.setStringCellValue(dataRow, CLAIM_PER_UNIT_COLUMN, claimPerUnit);

			String claimAmout = getClaimAmout(prepos.getQuantity(), claimPerUnit);
			posreadyFieldsBuilder.setStringCellValue(dataRow, EXTENDED_CLAIM_AMOUNT_COLUMN, claimAmout);

		}

		return workbook;
	}

	private String getClaimAmout(int quantity, String claimPerUnit) {

		if(StringUtils.isNotBlank(claimPerUnit)) {
			return String.valueOf(getRoundedDouble(Double.valueOf(claimPerUnit) * quantity));
		}

		return "";
	}

	private String getClaimPerUnit(Prepos prepos, Table<String, String, Dart> dartsTable, Map<String, Promo> promosMap) {

		String secondPromo = prepos.getSecondPromo();
		String firstPromo = prepos.getFirstPromo();

		if(StringUtils.isNotBlank(secondPromo)) {

			Dart dart = dartsTable.get(prepos.getPartNumber(), secondPromo);
			checkDartNotNull(prepos, dart);

			return String.valueOf(dart.getClaimUnit());

		} else if (StringUtils.isNotBlank(firstPromo)){

			Promo promo = promosMap.get(prepos.getPartNumber());
			checkPromoNotNull(prepos, promo);

			return String.valueOf(promo.getClaimPerUnit());

		}

		return "";
	}

	//TODO что если промо1 тоже пустое?
	private String getPromoVersion(Prepos prepos, Table<String, String, Dart> dartsTable, Map<String, Promo> promosMap) {

		String secondPromo = prepos.getSecondPromo();
		String firstPromo = prepos.getFirstPromo();

		if(StringUtils.isNotBlank(secondPromo)) {

			Dart dart = dartsTable.get(prepos.getPartNumber(), secondPromo);
			checkDartNotNull(prepos, dart);

			return String.valueOf(dart.getVersion());

		} else if (StringUtils.isNotBlank(firstPromo)){

			Promo promo = promosMap.get(prepos.getPartNumber());
			checkPromoNotNull(prepos, promo);

			return String.valueOf(promo.getVersion());

		}

		return "";
	}

	private void checkPromoNotNull(Prepos prepos, Promo promo) {
		if(promo == null) {
			throw new CiscoException("No promo found for prepos with PN: " + prepos.getPartNumber());
		}
	}

	private void checkDartNotNull(Prepos prepos, Dart dart) {
		if(dart == null) {
			throw new CiscoException("No dart found for prepos with PN: " + prepos.getPartNumber());
		}
	}

	private String getPromoCode(Prepos prepos) {
		String secondPromo = prepos.getSecondPromo();
		return StringUtils.isNotBlank(secondPromo) ? secondPromo : prepos.getFirstPromo();
	}

	private String getEndUser(Prepos prepos) {
		String endUser = prepos.getEndUser();
		return StringUtils.isNotBlank(endUser) ? endUser : prepos.getPartnerName();
	}

	private HSSFRow createTitleRow(HSSFSheet sheet) {

		if(sheet.getPhysicalNumberOfRows() != 0) {
			throw new CiscoException("Sheet should be empty for title row creation");
		}

		HSSFRow row = sheet.createRow(0);

		posreadyFieldsBuilder.buildStringCell(row, 0, DISTRIBUTOR_WAREHOUSE_NUMBER);
		posreadyFieldsBuilder.buildStringCell(row, 1, BUYER_RESELLER_NAME);
		posreadyFieldsBuilder.buildStringCell(row, 2, BUYER_RESELLER_PARTNER_IDENTIFICATION);
		posreadyFieldsBuilder.buildStringCell(row, 3, BUYER_RESELLER_ADDRESS1);
		posreadyFieldsBuilder.buildStringCell(row, 4, BUYER_RESELLER_ADDRESS2);
		posreadyFieldsBuilder.buildStringCell(row, 5, BUYER_RESELLER_CITY);
		posreadyFieldsBuilder.buildStringCell(row, 6, BUYER_RESELLER_ZIP___POSTAL_CODE);
		posreadyFieldsBuilder.buildStringCell(row, 7, BUYER_RESELLER_COUNTRY);
		posreadyFieldsBuilder.buildStringCell(row, 8, RESELLER_CONTACT_NAME);
		posreadyFieldsBuilder.buildStringCell(row, 9, RESELLER_CONTACT_TELEPHONE);
		posreadyFieldsBuilder.buildStringCell(row, 10, RESELLER_CONTACT_EMAIL);
		posreadyFieldsBuilder.buildStringCell(row, 11, DISTRIBUTOR_TO_RESELLER);
		posreadyFieldsBuilder.buildStringCell(row, 12, INVOICE_DATE_SHIPPED_DATE);
		posreadyFieldsBuilder.buildStringCell(row, 13, DISTRIBUTOR_TO_RESELLER_SALES_ORDER_DATE);
		posreadyFieldsBuilder.buildStringCell(row, 14, RESELLER_TO_DISTRIBUTOR_PO_NUMBER);
		posreadyFieldsBuilder.buildStringCell(row, 15, DISTRIBUTOR_TO_RESELLER_INVOICE_NUMBER);
		posreadyFieldsBuilder.buildStringCell(row, 16, ORIGINAL_DISTRIBUTOR_TO_RESELLER_INVOICE_NUMBER);
		posreadyFieldsBuilder.buildStringCell(row, 17, SO_LINE_NUMBER_ORDER_LINE_ITEM_NUMBER);
		posreadyFieldsBuilder.buildStringCell(row, 18, DISTRIBUTOR_SALES_ORDER_NUMBER);
		posreadyFieldsBuilder.buildStringCell(row, 19, ORIGINAL_DISTRIBUTOR_TO_RESELLER_SALES_ORDER_NUMBER);
		posreadyFieldsBuilder.buildStringCell(row, 20, DISTRIBUTOR_PART_NUMBER);
		posreadyFieldsBuilder.buildStringCell(row, 21, CISCO_STANDARD_PART_NUMBER);
		posreadyFieldsBuilder.buildStringCell(row, 22, PRODUCT_ITEM_DESCRIPTION);
		posreadyFieldsBuilder.buildStringCell(row, 23, PRODUCT_QUANTITY);
		posreadyFieldsBuilder.buildStringCell(row, 24, DROP_SHIP);
		posreadyFieldsBuilder.buildStringCell(row, 25, DISTRIBUTOR_PO_NUMBER_TO_CISCO);
		posreadyFieldsBuilder.buildStringCell(row, 26, REPORTED_PRODUCT_UNIT_PRICE_USD);
		posreadyFieldsBuilder.buildStringCell(row, 28, REPORTED_NET_POS_PRICE);
		posreadyFieldsBuilder.buildStringCell(row, 29, PRODUCT_SERIAL_NUMBER);
		posreadyFieldsBuilder.buildStringCell(row, 30, END_CUSTOMER_NAME);
		posreadyFieldsBuilder.buildStringCell(row, 31, END_CUSTOMER_ADDRESS1);
		posreadyFieldsBuilder.buildStringCell(row, 32, END_CUSTOMER_ADDRESS2);
		posreadyFieldsBuilder.buildStringCell(row, 33, END_CUSTOMER_CITY);
		posreadyFieldsBuilder.buildStringCell(row, 34, END_CUSTOMER_ZIP_POSTAL_CODE);
		posreadyFieldsBuilder.buildStringCell(row, 35, END_CUSTOMER_COUNTRY);
		posreadyFieldsBuilder.buildStringCell(row, 36, END_CUSTOMER_TO_RESELLER_PO_NUMBER);
		posreadyFieldsBuilder.buildStringCell(row, 37, END_CUSTOMER_CONTACT_NAME);
		posreadyFieldsBuilder.buildStringCell(row, 38, END_CUSTOMER_CONTACT_TELEPHONE);
		posreadyFieldsBuilder.buildStringCell(row, 39, END_CUSTOMER_CONTACT_EMAIL);
		posreadyFieldsBuilder.buildStringCell(row, 40, SP_SERVICE_SHIP_TO_NAME);
		posreadyFieldsBuilder.buildStringCell(row, 41, SHIP_TO_ADDRESS1);
		posreadyFieldsBuilder.buildStringCell(row, 42, SHIP_TO_ADDRESS2);
		posreadyFieldsBuilder.buildStringCell(row, 43, SHIP_TO_CITY);
		posreadyFieldsBuilder.buildStringCell(row, 44, SHIP_TO_ZIP_POSTAL_CODE);
		posreadyFieldsBuilder.buildStringCell(row, 45, SHIP_TO_COUNTRY_BILL_TO_NAME);
		posreadyFieldsBuilder.buildStringCell(row, 46, BILL_TO_ADDRESS1);
		posreadyFieldsBuilder.buildStringCell(row, 47, BILL_TO_ADDRESS2);
		posreadyFieldsBuilder.buildStringCell(row, 48, BILL_TO_CITY);
		posreadyFieldsBuilder.buildStringCell(row, 49, BILL_TO_ZIP_POSTAL_CODE);
		posreadyFieldsBuilder.buildStringCell(row, 50, BILL_TO_COUNTRY_REPORTED_DEAL_ID);
		posreadyFieldsBuilder.buildStringCell(row, 51, PROMOTION_AUTHORIZATION_NUMBER_1);
		posreadyFieldsBuilder.buildStringCell(row, 52, CLAIM_REFERENCE_NUMBER_1);
		posreadyFieldsBuilder.buildStringCell(row, 53, CLAIM_ELIGIBILITY_QUANTITY_1);
		posreadyFieldsBuilder.buildStringCell(row, 54, CLAIM_PER_UNIT_1);
		posreadyFieldsBuilder.buildStringCell(row, 55, EXTENDED_CLAIM_AMOUNT_1);
		posreadyFieldsBuilder.buildStringCell(row, 56, COMMENTS_1);
		posreadyFieldsBuilder.buildStringCell(row, 57, PROMOTION_AUTHORIZATION_NUMBER_2);
		posreadyFieldsBuilder.buildStringCell(row, 58, CLAIM_REFERENCE_NUMBER_2);
		posreadyFieldsBuilder.buildStringCell(row, 59, CLAIM_ELIGIBILITY_QUANTITY_2);
		posreadyFieldsBuilder.buildStringCell(row, 60, CLAIM_PER_UNIT_2);
		posreadyFieldsBuilder.buildStringCell(row, 61, EXTENDED_CLAIM_AMOUNT_2);
		posreadyFieldsBuilder.buildStringCell(row, 62, COMMENTS_2);
		posreadyFieldsBuilder.buildStringCell(row, 63, PROMOTION_AUTHORIZATION_NUMBER_3);
		posreadyFieldsBuilder.buildStringCell(row, 64, CLAIM_REFERENCE_NUMBER_3);
		posreadyFieldsBuilder.buildStringCell(row, 65, CLAIM_ELIGIBILITY_QUANTITY_3);
		posreadyFieldsBuilder.buildStringCell(row, 66, CLAIM_PER_UNIT_3);
		posreadyFieldsBuilder.buildStringCell(row, 67, EXTENDED_CLAIM_AMOUNT_3);
		posreadyFieldsBuilder.buildStringCell(row, 68, COMMENTS_3);
		posreadyFieldsBuilder.buildStringCell(row, 69, PROMOTION_AUTHORIZATION_NUMBER_4);
		posreadyFieldsBuilder.buildStringCell(row, 70, CLAIM_REFERENCE_NUMBER_4);
		posreadyFieldsBuilder.buildStringCell(row, 71, CLAIM_ELIGIBILITY_QUANTITY_4);
		posreadyFieldsBuilder.buildStringCell(row, 72, CLAIM_PER_UNIT_4);
		posreadyFieldsBuilder.buildStringCell(row, 73, EXTENDED_CLAIM_AMOUNT_4);
		posreadyFieldsBuilder.buildStringCell(row, 74, COMMENTS_4);

		return row;
	}
}

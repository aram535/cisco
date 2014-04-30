package com.cisco.darts.excel;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartBuilder;
import com.cisco.excel.DefaultFieldsExtractor;
import com.cisco.exception.CiscoException;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * User: Rost
 * Date: 28.04.2014
 * Time: 22:53
 */
public class DefaultDartsExtractor implements DartsExtractor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int AUTORIZATION_NUMBER_COLUMN = 0;
    private static final int VERSION_COLUMN = 1;
    private static final int DISTRIBUTOR_INFO_COLUMN = 2;
    private static final int START_DATE_COLUMN = 3;
    private static final int END_DATE_COLUMN = 4;
    private static final int DISTI_DISCOUNT_COLUMN = 5;
    private static final int RESELLER_NAME_COLUMN = 6;
    private static final int RESELLER_COUNTRY_COLUMN = 7;
    private static final int RESELLER_ACCT_COLUMN = 8;
    private static final int END_USER_NAME_COLUMN = 9;
    private static final int END_USER_COUNTRY_COLUMN = 10;
    private static final int QUANTITY_COLUMN = 11;
    private static final int CISKO_SKU_COLUMN = 12;
    private static final int DISTI_SKU_COLUMN = 13;
    private static final int LIST_PRICE_COLUMN = 14;
    private static final int CLAIM_UNIT_COLUMN = 15;
    private static final int EXT_CREDIT_ATM_COLUMN = 16;
    private static final int FAST_TRACK_PIE_COLUMN = 17;
    private static final int IP_NGN_PARTNER_PRICING_EM_COLUMN = 18;
    private static final int MDM_FULLFILLMENT_COLUMN = 19;

    private final DefaultFieldsExtractor fieldsExtractor = new DefaultFieldsExtractor();


    @Override
    public List<Dart> extract(File file) {
        List<Dart> darts = Lists.newArrayList();
        try {
            logger.debug("extracting darts from file {}", file.getAbsolutePath());
            FileInputStream fileInputStream = new FileInputStream(file);
            Workbook workbook = fieldsExtractor.getWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            //skipping first row with names of columns
            rowIterator.next();

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                String autorizationNumber = fieldsExtractor.extractStringValue(row, AUTORIZATION_NUMBER_COLUMN);

                int version = fieldsExtractor.extractIntValue(row, VERSION_COLUMN);

                String distributorInfo = fieldsExtractor.extractStringValue(row, DISTRIBUTOR_INFO_COLUMN);

                Timestamp startDate = fieldsExtractor.extractTimestamp(row, START_DATE_COLUMN);

                Timestamp endDate = fieldsExtractor.extractTimestamp(row, END_DATE_COLUMN);

                double distiDiscount = fieldsExtractor.extractDoubleValue(row, DISTI_DISCOUNT_COLUMN);

                String resellerName = fieldsExtractor.extractStringValue(row, RESELLER_NAME_COLUMN);

                String resellerCountry = fieldsExtractor.extractStringValue(row, RESELLER_COUNTRY_COLUMN);

                int resellerAcct = fieldsExtractor.extractIntValue(row, RESELLER_ACCT_COLUMN);

                String endUserName = fieldsExtractor.extractStringValue(row, END_USER_NAME_COLUMN);

                String endUserCountry = fieldsExtractor.extractStringValue(row, END_USER_COUNTRY_COLUMN);

                int quantity = fieldsExtractor.extractIntValue(row, QUANTITY_COLUMN);

                String ciscoSku = fieldsExtractor.extractStringValue(row, CISKO_SKU_COLUMN);

                String distiSku = fieldsExtractor.extractStringValue(row, DISTI_SKU_COLUMN);

                double listPrice = fieldsExtractor.extractDoubleValue(row, LIST_PRICE_COLUMN);

                double claimUnit = fieldsExtractor.extractDoubleValue(row, CLAIM_UNIT_COLUMN);

                double extCreditAtm = fieldsExtractor.extractDoubleValue(row, EXT_CREDIT_ATM_COLUMN);

                double fastTrackPie = fieldsExtractor.extractDoubleValue(row, FAST_TRACK_PIE_COLUMN);

                double ipNgnPartnerPricingEm = fieldsExtractor.extractDoubleValue(row, IP_NGN_PARTNER_PRICING_EM_COLUMN);

                double mdmFullFillment = fieldsExtractor.extractDoubleValue(row, MDM_FULLFILLMENT_COLUMN);

                Dart dart = DartBuilder.builder().
                        setAuthorizationNumber(autorizationNumber)
                        .setVersion(version)
                        .setCiscoSku(ciscoSku)
                        .setClaimUnit(claimUnit)
                        .setDistiDiscount(distiDiscount)
                        .setDistiSku(distiSku)
                        .setStartDate(startDate)
                        .setEndDate(endDate)
                        .setDistributorInfo(distributorInfo)
                        .setExtCreditAmt(extCreditAtm)
                        .setFastTrackPie(fastTrackPie)
                        .setIpNgnPartnerPricingEm(ipNgnPartnerPricingEm)
                        .setMdmFulfillment(mdmFullFillment)
                        .setListPrice(listPrice)
                        .setQuantity(quantity)
                        .setResellerCountry(resellerCountry)
                        .setResellerAcct(resellerAcct)
                        .setResellerName(resellerName)
                        .setEndUserName(endUserName)
                        .setEndUserCountry(endUserCountry)
                        .build();

                darts.add(dart);
            }

            fileInputStream.close();

        } catch (FileNotFoundException e) {
            throw new CiscoException(String.format("Cannot find file %s", file.getAbsolutePath()), e);
        } catch (IOException e) {
            logger.error("Error occured during Darts import", e);
            e.printStackTrace();
        }

        logger.debug("extracted darts {}", darts);
        return darts;
    }

}

package com.cisco.promos.excel;

import com.cisco.excel.DefaultFieldsExtractor;
import com.cisco.excel.FieldsExtractor;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import static com.cisco.promos.dto.PromoBuilder.newPromoBuilder;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 0:16
 */
@Component
public class DefaultPromosExtractor implements PromosExtractor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int PART_NUMBER_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private static final int DISCOUNT_COLUMN = 2;
    private static final int NAME_COLUMN = 3;
    private static final int GPL_COLUMN = 4;
    private static final int CODE_COLUMN = 5;
    private static final int CLAIM_PER_UNIT_COLUMN = 6;
    private static final int VERSION_COLUMN = 7;
    private static final int END_DATE_COLUMN = 8;


    private final FieldsExtractor fieldsExtractor = new DefaultFieldsExtractor();

    @Override
    public List<Promo> extract(InputStream inputStream) {
        logger.debug("Extracting promos list from file started...");

        List<Promo> promos = Lists.newArrayList();
        try {
            Workbook workbook = fieldsExtractor.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            //skipping first row with names of columns
            rowIterator.next();

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                String partNumber = fieldsExtractor.extractStringValue(row, PART_NUMBER_COLUMN);
                String description = fieldsExtractor.extractStringValue(row, DESCRIPTION_COLUMN);

                double discount = calculateDiscount(row);

                String name = fieldsExtractor.extractStringValue(row, NAME_COLUMN);
                int gpl = (int) fieldsExtractor.extractNumericValue(row, GPL_COLUMN);
                String code = fieldsExtractor.extractStringValue(row, CODE_COLUMN);
                double claimPerUnit = fieldsExtractor.extractDoubleValue(row, CLAIM_PER_UNIT_COLUMN);
                int version = (int) fieldsExtractor.extractNumericValue(row, VERSION_COLUMN);
	            Timestamp endDate = fieldsExtractor.extractTimestampEU(row, END_DATE_COLUMN);

                Promo promo = newPromoBuilder().
                        setPartNumber(partNumber).
                        setDescription(description).
                        setDiscount(discount).
                        setName(name).
                        setGpl(gpl).
                        setCode(code).
                        setClaimPerUnit(claimPerUnit).
                        setVersion(version).
		                setEndDate(endDate).
                        build();

                promos.add(promo);
            }

            inputStream.close();

        } catch (IOException e) {
            logger.error("Error occured during Darts import", e);
            e.printStackTrace();
        }

        logger.info("extracted promos {}", promos);

        return promos;
    }

    private double calculateDiscount(Row row) {
        double discountInPercents = fieldsExtractor.extractDoubleValue(row, DISCOUNT_COLUMN);
        return discountInPercents / 100d;
    }
}

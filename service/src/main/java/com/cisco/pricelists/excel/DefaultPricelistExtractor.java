package com.cisco.pricelists.excel;

import com.cisco.excel.DefaultFieldsExtractor;
import com.cisco.excel.FieldsExtractor;
import com.cisco.pricelists.dto.Pricelist;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

import static com.cisco.prepos.services.discount.utils.DiscountPartCounter.getRoundedDouble;
import static com.cisco.pricelists.dto.PricelistBuilder.newPricelistBuilder;

/**
 * User: Rost
 * Date: 29.04.2014
 * Time: 21:24
 */
@Component
public class DefaultPricelistExtractor implements PricelistExtractor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int PART_NUMBER_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private static final int GPL_COLUMN = 2;
    private static final int DISCOUNT_COLUMN = 3;

    private final FieldsExtractor fieldsExtractor = new DefaultFieldsExtractor();

    @Override
    public Map<String, Pricelist> extract(InputStream inputStream) {
        logger.debug("Extracting prices from file started...");

        Map<String, Pricelist> pricelistMap = Maps.newLinkedHashMap();
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
                int gpl = fieldsExtractor.extractIntValue(row, GPL_COLUMN);
                int discount = fieldsExtractor.extractIntValue(row, DISCOUNT_COLUMN);
                double wpl = calculateWpl(gpl, discount);
	            double fractionalDiscount = getRoundedDouble((double)discount / 100);
	            Pricelist price = newPricelistBuilder().setPartNumber(partNumber).setDescription(description).
                        setWpl(wpl).setGpl(gpl).setDiscount(fractionalDiscount).build();

                pricelistMap.put(price.getPartNumber(), price);
            }
            inputStream.close();

        } catch (IOException e) {
            logger.error("Error occured during Darts import", e);
            e.printStackTrace();
        }

        logger.debug("extracted prices {}", pricelistMap);

        return pricelistMap;
    }

    private double calculateWpl(int gpl, int discount) {
        double wpl = gpl * (1 - (discount / 100d));
        BigDecimal bd = new BigDecimal(wpl);
        BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return rounded.doubleValue();
    }
}

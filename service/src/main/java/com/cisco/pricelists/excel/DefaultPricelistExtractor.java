package com.cisco.pricelists.excel;

import com.cisco.excel.DefaultFieldsExtractor;
import com.cisco.excel.FieldsExtractor;
import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dto.Pricelist;
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
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import static com.cisco.pricelists.dto.PricelistBuilder.newPricelistBuilder;

/**
 * User: Rost
 * Date: 29.04.2014
 * Time: 21:24
 */
public class DefaultPricelistExtractor implements PricelistExtractor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int PART_NUMBER_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private static final int GPL_COLUMN = 2;
    private static final int DISCOUNT_COLUMN = 3;

    private final FieldsExtractor fieldsExtractor = new DefaultFieldsExtractor();

    @Override
    public List<Pricelist> extract(File file) {
        logger.debug("Extracting prices from file %s", file.getAbsolutePath());
        List<Pricelist> pricelist = Lists.newArrayList();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Workbook workbook = fieldsExtractor.getWorkbook(fileInputStream);
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
                Pricelist price = newPricelistBuilder().setPartNumber(partNumber).setDescription(description).
                        setWpl(wpl).setGpl(gpl).setDiscount(discount).build();

                pricelist.add(price);
            }
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            throw new CiscoException(String.format("Cannot find file %s", file.getAbsolutePath()), e);
        } catch (IOException e) {
            logger.error("Error occured during Darts import", e);
            e.printStackTrace();
        }

        return pricelist;
    }

    private double calculateWpl(int gpl, int discount) {
        double wpl = gpl * (1 - (discount / 100d));
        BigDecimal bd = new BigDecimal(wpl);
        BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return rounded.doubleValue();
    }
}

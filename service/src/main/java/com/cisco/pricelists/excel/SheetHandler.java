package com.cisco.pricelists.excel;

import com.cisco.pricelists.dto.Pricelist;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Map;

import static com.cisco.prepos.services.discount.utils.DiscountPartCounter.getRoundedDouble;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Maps.uniqueIndex;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * User: Rost
 * Date: 08.07.2014
 * Time: 16:50
 */
public class SheetHandler extends DefaultHandler {

    private static final String CELL_CONTENT_ATTRIBUTE_NAME = "v";
    private static final String ELEMENT_TYPE_ATTRIBUTE_CELL_VALUE = "c";
    private static final String CELL_COORDINATES_ATTRIBUTE_NAME = "r";
    private static final String CELL_TYPE_ATTRIBUTE_NAME = "t";
    private static final String PART_NUMBER_COLUMN = "A";
    private static final String DESCRIPTION_COLUMN = "B";
    private static final String GPL_COLUMN = "C";
    private static final String DISCOUNT_COLUMN = "D";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final LinkedList<Pricelist> pricelist = newLinkedList();

    private SharedStringsTable sharedStringsTable;
    private String lastContents;
    private String currentColumn;
    private boolean nextIsString;

    public SheetHandler(SharedStringsTable sharedStringsTable) {
        this.sharedStringsTable = sharedStringsTable;
    }

    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        if (name.equals(ELEMENT_TYPE_ATTRIBUTE_CELL_VALUE)) {
            String cellCoordinates = attributes.getValue(CELL_COORDINATES_ATTRIBUTE_NAME);

            String row = cellCoordinates.substring(1);
            if (row.equals("1")) {
                return;
            }

            currentColumn = cellCoordinates.substring(0, 1);
            if (currentColumn.equals(PART_NUMBER_COLUMN)) {
                pricelist.add(new Pricelist());
            }

            String cellType = attributes.getValue(CELL_TYPE_ATTRIBUTE_NAME);
            if (cellType != null && cellType.equals("s")) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }
        }
        lastContents = "";
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        // Process the last contents as required.
        // Do now, as characters() may be called more than once
        if (nextIsString) {
            String indexValue = lastContents;
            int idx = parseInt(indexValue);
            String stringContentValue = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx)).toString();
            if (!pricelist.isEmpty()) {
                appendPricelistAccordingToColumn(currentColumn, stringContentValue, pricelist.getLast());
            }
            nextIsString = false;
            return;
        }

        if (name.equals(CELL_CONTENT_ATTRIBUTE_NAME)) {
            String cellCoordinatesWithContentValue = lastContents;
            if (!pricelist.isEmpty()) {
                appendPricelistAccordingToColumn(currentColumn, cellCoordinatesWithContentValue, pricelist.getLast());
            }
        }
    }

    private Pricelist appendPricelistAccordingToColumn(String column, String value, Pricelist pricelist) {

        if (column.equals(PART_NUMBER_COLUMN)) {
            pricelist.setPartNumber(value);
            return pricelist;
        }

        if (column.equals(DESCRIPTION_COLUMN)) {
            pricelist.setDescription(value);
            return pricelist;
        }

        if (column.equals(GPL_COLUMN)) {
            double gpl = getGpl(column, value);
            pricelist.setGpl(gpl);
            return pricelist;
        }

        if (column.equals(DISCOUNT_COLUMN)) {
            int discount = getDiscount(column, value);
            double fractionalDiscount = getRoundedDouble((double) discount / 100);
            pricelist.setDiscount(fractionalDiscount);

            double gpl = pricelist.getGpl();
            pricelist.setWpl(calculateWpl(gpl, discount));

            return pricelist;
        }

        return pricelist;
    }

    private int getDiscount(String column, String value) {
        int discount;
        try {
            discount = (int) parseDouble(value);
        } catch (NumberFormatException e) {
            String message = String.format("Error during parsing column=%s, value=%s", column, value);
            logger.error(message, e);
            discount = 0;
        }
        return discount;
    }

    private double getGpl(String column, String value) {
        double gpl;
        try {
            gpl = parseDouble(value);
        } catch (NumberFormatException e) {
            String message = String.format("Error during parsing column=%s, value=%s", column, value);
            logger.error(message, e);
            gpl = 0.0d;
        }
        return gpl;
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        lastContents += new String(ch, start, length);
    }


    private double calculateWpl(Double gpl, int discount) {

        double wpl = gpl * (1 - (discount / 100d));
        BigDecimal bd = new BigDecimal(wpl);
        BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return rounded.doubleValue();

    }

    public Map<String, Pricelist> getPricelist() {

        Map<String, Pricelist> pricelistMap = Maps.newLinkedHashMap();
        for (Pricelist price : pricelist) {
            pricelistMap.put(price.getPartNumber(), price);
        }
        return pricelistMap;
    }
}

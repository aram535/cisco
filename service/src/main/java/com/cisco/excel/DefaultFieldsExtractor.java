package com.cisco.excel;

import com.cisco.exception.CiscoException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK;

public class DefaultFieldsExtractor implements FieldsExtractor {

    private static final String DATE_PATTERN = "dd-MMM-yyyy";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_PATTERN).withLocale(Locale.US);

    @Override
    public int extractIntValue(Row row, int column) {
        Cell cell = row.getCell(column, CREATE_NULL_AS_BLANK);
        return getIntValueFromCell(cell, column);
    }

    @Override
    public Timestamp extractTimestamp(Row row, int column) {
        Cell cell = row.getCell(column, CREATE_NULL_AS_BLANK);
        return getTimestampValueFromCell(cell, column);
    }

    @Override
    public double extractDoubleValue(Row row, int column) {
        Cell cell = row.getCell(column, CREATE_NULL_AS_BLANK);
        return getDoubleValueFromCell(cell, column);
    }

    @Override
    public String extractStringValue(Row row, int column) {
        Cell cell = row.getCell(column, CREATE_NULL_AS_BLANK);
        return getStringValueFromCell(cell, column);
    }

    @Override
    public Workbook getWorkbook(InputStream inputStream) {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (Exception e) {
            throw new CiscoException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return workbook;
    }

    private double getDoubleValueFromCell(Cell cell, int column) {

	    if (cell == null) {
		    throw new CiscoException(String.format("Column number %s is required", column));
	    }

	    switch(cell.getCellType()) {
		    case Cell.CELL_TYPE_NUMERIC: {
			    return cell.getNumericCellValue();
		    }
		    case Cell.CELL_TYPE_STRING: {
			    return Integer.valueOf(cell.getStringCellValue());
		    }
		    case Cell.CELL_TYPE_BLANK: {
			    return 0;
		    }
		    default: {
			    throw new CiscoException(String.format("column number %s is expected to have numeric or string type", column));
		    }
	    }
    }

    private Timestamp getTimestampValueFromCell(Cell cell, int column) {

        String stringValueFromCell = getStringValueFromCell(cell, column);

        if (isBlank(stringValueFromCell)) {
            return null;
        }

        DateTime dateTime = DateTime.parse(stringValueFromCell, DATE_TIME_FORMATTER);
        return new Timestamp(dateTime.getMillis());
    }

    private int getIntValueFromCell(Cell cell, int column) {

	    if (cell == null) {
            throw new CiscoException(String.format("Column number %s is required", column));
        }

	    switch(cell.getCellType()) {
		    case Cell.CELL_TYPE_NUMERIC: {
			    return (int) cell.getNumericCellValue();
		    }
		    case Cell.CELL_TYPE_STRING: {
			    return Integer.valueOf(cell.getStringCellValue());
		    }
		    case Cell.CELL_TYPE_BLANK: {
			    return 0;
		    }
		    default: {
			    throw new CiscoException(String.format("column number %s is expected to have numeric or string type", column));
		    }
	    }
    }

    private String getStringValueFromCell(Cell cell, int column) {

	    if (cell == null) {
		    throw new CiscoException(String.format("Column number %s is required", column));
	    }

	    switch(cell.getCellType()) {
		    case Cell.CELL_TYPE_NUMERIC: {
			    return String.valueOf(cell.getNumericCellValue());
		    }
		    case Cell.CELL_TYPE_STRING: {
			    return cell.getStringCellValue();
		    }
		    case Cell.CELL_TYPE_BLANK: {
			    return "";
		    }
		    default: {
			    throw new CiscoException(String.format("column number %s is expected to have numeric or string type", column));
		    }
	    }
    }
}
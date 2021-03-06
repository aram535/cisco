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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK;

public class DefaultFieldsExtractor implements FieldsExtractor {

    private static final String DATE_PATTERN_US = "dd-MMM-yyyy";
    private static final String DATE_PATTERN_EU = "dd-MM-yyyy";
    private static final DateTimeFormatter US_DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_PATTERN_US).withLocale(Locale.US);
    private static final DateTimeFormatter EU_DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_PATTERN_EU);

    @Override
    public long extractNumericValue(Row row, int column) {
	    try {
		    Cell cell = row.getCell(column, CREATE_NULL_AS_BLANK);
		    return getIntValueFromCell(cell, column);
	    } catch (Exception e) {
		    throw new CiscoException(String.format("Row number %d:%s", row.getRowNum(), e.getMessage()));
	    }

    }

    @Override
    public Timestamp extractTimestampUS(Row row, int column) {
	    try {
		    Cell cell = row.getCell(column, CREATE_NULL_AS_BLANK);
		    return getTimestampValueFromCell(cell, column);
	    } catch (Exception e) {
		    throw new CiscoException(String.format("Row number %d:%s", row.getRowNum(), e.getMessage()));
	    }
    }

	@Override
	public Timestamp extractTimestampEU(Row row, int column) {
		try {
			Cell cell = row.getCell(column, CREATE_NULL_AS_BLANK);
			return getTimestampValueFromCellEU(cell, column);
		} catch (Exception e) {
			throw new CiscoException(String.format("Row number %d:%s", row.getRowNum(), e.getMessage()));
		}
	}

    @Override
    public double extractDoubleValue(Row row, int column) {
	    try {
		    Cell cell = row.getCell(column, CREATE_NULL_AS_BLANK);
		    return getDoubleValueFromCell(cell, column);
	    } catch (Exception e) {
		    throw new CiscoException(String.format("Row number %d:%s", row.getRowNum(), e.getMessage()));
	    }

    }

    @Override
    public String extractStringValue(Row row, int column) {
	    try {
		    Cell cell = row.getCell(column, CREATE_NULL_AS_BLANK);
		    return getStringValueFromCell(cell, column);
	    } catch (Exception e) {
		    throw new CiscoException(String.format("Row number %d:%s", row.getRowNum(), e.getMessage()));
	    }

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
			    try {
				    NumberFormat format = NumberFormat.getInstance(Locale.US);
				    Number number = format.parse(cell.getStringCellValue());
				    return number.doubleValue();
			    } catch (ParseException e) {
				    throw new CiscoException(String.format("string representation of column number %s value should have numeric format", column));
			    }
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

        DateTime dateTime = DateTime.parse(stringValueFromCell, US_DATE_TIME_FORMATTER);
        return new Timestamp(dateTime.getMillis());
    }

	private Timestamp getTimestampValueFromCellEU(Cell cell, int column) {

		String stringValueFromCell = getStringValueFromCell(cell, column);

		if (isBlank(stringValueFromCell)) {
			return null;
		}

		DateTime dateTime = DateTime.parse(stringValueFromCell, EU_DATE_TIME_FORMATTER);
		return new Timestamp(dateTime.getMillis());
	}

    private long getIntValueFromCell(Cell cell, int column) {

	    if (cell == null) {
            throw new CiscoException(String.format("Column number %s is required", column));
        }

	    switch(cell.getCellType()) {
		    case Cell.CELL_TYPE_NUMERIC: {
			    return (long) cell.getNumericCellValue();
		    }
		    case Cell.CELL_TYPE_STRING: {
			    try {
				    return Integer.valueOf(cell.getStringCellValue());
			    }
			    catch(NumberFormatException e) {
				    throw new CiscoException(String.format("string representation of column number %s value should have numeric format", column));
			    }
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
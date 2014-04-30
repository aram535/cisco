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

import static org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL;

public class DefaultFieldsExtractor implements FieldsExtractor {

    private static final String DATE_PATTERN = "dd-MMM-yyyy";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_PATTERN).withLocale(Locale.US);

    @Override
    public int extractIntValue(Row row, int column) {
        Cell cell = row.getCell(column, RETURN_BLANK_AS_NULL);
        return getIntValueFromCell(cell, column);
    }

    @Override
    public Timestamp extractTimestamp(Row row, int column) {
        Cell cell = row.getCell(column, RETURN_BLANK_AS_NULL);
        return getTimestampValueFromCell(cell, column);
    }

    @Override
    public double extractDoubleValue(Row row, int column) {
        Cell cell = row.getCell(column, RETURN_BLANK_AS_NULL);
        return getDoubleValueFromCell(cell, column);
    }

    @Override
    public String extractStringValue(Row row, int column) {
        Cell cell = row.getCell(column, RETURN_BLANK_AS_NULL);
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

        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue();
        }
        throw new CiscoException(String.format("cell %s-%s should be numeric", column, cell.getRowIndex()));
    }

    private Timestamp getTimestampValueFromCell(Cell cell, int column) {

        String stringValueFromCell = getStringValueFromCell(cell, column);

        if (stringValueFromCell == null) {
            return null;
        }

        DateTime dateTime = DateTime.parse(stringValueFromCell, DATE_TIME_FORMATTER);
        return new Timestamp(dateTime.getMillis());
    }

    private int getIntValueFromCell(Cell cell, int column) {
        if (cell == null) {
            throw new CiscoException(String.format("Column number %s is required", column));
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else {
            throw new CiscoException(String.format("column number %s is expected to have numeric type", column));
        }
    }

    private String getStringValueFromCell(Cell cell, int column) {
        if (cell != null) {
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                return cell.getStringCellValue();
            } else {
                throw new CiscoException(String.format("column number %s is expected to have string type", column));
            }
        }
        return null;
    }
}
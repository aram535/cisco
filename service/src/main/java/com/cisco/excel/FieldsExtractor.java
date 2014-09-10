package com.cisco.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.sql.Timestamp;

/**
 * User: Rost
 * Date: 29.04.2014
 * Time: 20:23
 */
public interface FieldsExtractor {

	long extractNumericValue(Row row, int column);

    Timestamp extractTimestampUS(Row row, int column);

	Timestamp extractTimestampEU(Row row, int column);

    double extractDoubleValue(Row row, int column);

    String extractStringValue(Row row, int column);

    Workbook getWorkbook(InputStream inputStream);
}

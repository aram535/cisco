package com.cisco.posready.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Created by Alf on 02.07.2014.
 */
public interface PosreadyFieldsBuilder {

	Cell buildStringCell(Row dataRow, int pos, String value);

	Cell setStringCellValue(Row dataRow, int pos, String value);

	Row createEmptyDataRow(Sheet sheet, int pos, int cellCount);
}

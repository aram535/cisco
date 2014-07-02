package com.cisco.posready.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

/**
 * Created by Alf on 02.07.2014.
 */
@Component
public class DefaultPosreadyFieldsBuilder implements PosreadyFieldsBuilder {

	@Override
	public Cell buildStringCell(Row dataRow, int pos, String value) {

		Cell cell = dataRow.createCell(pos);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(value);

		return cell;
	}

	@Override
	public Cell setStringCellValue(Row dataRow, int pos, String value) {

		Cell cell = dataRow.getCell(pos);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(value);

		return cell;
	}

	@Override
	public Row createEmptyDataRow(Sheet sheet, int pos, int cellCount) {
		Row row = sheet.createRow(pos);

		for (int i = 0; i < cellCount; i++) {
			row.createCell(i);
		}

		return row;
	}
}

package com.cisco.posready.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultPosreadyFieldsBuilderTest {

	private DefaultPosreadyFieldsBuilder posreadyFieldsBuilder = new DefaultPosreadyFieldsBuilder();

	@Test
	public void thatBuildCellReturnsCellWithCorrectTypeIndexAndValue() throws Exception {

		String value = "test value";

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("test sheet");

		Row row = sheet.createRow(0);
		Cell cell = posreadyFieldsBuilder.buildStringCell(row, 0, value);

		assertEquals(0, cell.getColumnIndex());
		assertEquals(Cell.CELL_TYPE_STRING, cell.getCellType());
		assertEquals(value, cell.getStringCellValue());
	}

	@Test
	public void thatSetCellValueReturnsCellWithCorrectTypeIndexAndValue() throws Exception {

		String value = "test value";

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("test sheet");

		Row row = sheet.createRow(0);
		row.createCell(0);
		Cell cell = posreadyFieldsBuilder.setStringCellValue(row, 0, value);

		assertEquals(0, cell.getColumnIndex());
		assertEquals(Cell.CELL_TYPE_STRING, cell.getCellType());
		assertEquals(value, cell.getStringCellValue());
	}
}
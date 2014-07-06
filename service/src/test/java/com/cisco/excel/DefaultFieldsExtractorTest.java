package com.cisco.excel;

import com.cisco.exception.CiscoException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 29.04.2014
 * Time: 20:31
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultFieldsExtractorTest {

    @Mock
    private Row row;

    @Mock
    private Cell cell;

    private FieldsExtractor fieldsExtractor = new DefaultFieldsExtractor();

    @Before
    public void init() {
        when(row.getCell(anyInt(), eq(Row.CREATE_NULL_AS_BLANK))).thenReturn(cell);
    }

    @Test
    public void thatExtractIntValueWhenCellTypeIsString() {
        when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
		when(cell.getStringCellValue()).thenReturn("20");

		int value = (int) fieldsExtractor.extractNumericValue(row, 1);

		assertThat(value).isEqualTo(20);

	}

    @Test(expected = CiscoException.class)
    public void thatExtractIntValueThrowsCiscoExceptionIfCellIsNull() {
        when(row.getCell(anyInt(), eq(Row.CREATE_NULL_AS_BLANK))).thenReturn(null);
        fieldsExtractor.extractNumericValue(row, 1);
    }

    @Test
    public void thatExtractIntValueReturnsIntValueOfNumericCell() {
        double expectedResult = 20d;
        when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
        when(cell.getNumericCellValue()).thenReturn(expectedResult);

        int result = (int) fieldsExtractor.extractNumericValue(row, 1);

        assertThat(expectedResult).isEqualTo(result);
    }

    @Test
    public void thatExtractDoubleValueWhenCellTypeIsString() {
        when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
		when(cell.getStringCellValue()).thenReturn("20.0");

		double value = fieldsExtractor.extractDoubleValue(row, 1);

		assertThat(value).isEqualTo(20d);
	}

    @Test(expected = CiscoException.class)
    public void thatExtractDoubleValueThrowsCiscoExceptionIfCellIsNull() {
        when(row.getCell(anyInt(), eq(Row.CREATE_NULL_AS_BLANK))).thenReturn(null);
        fieldsExtractor.extractDoubleValue(row, 1);
    }

    @Test
    public void thatExtractDoubleValueReturnsIntValueOfNumericCell() {
        double expectedResult = 20.2;
        when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
        when(cell.getNumericCellValue()).thenReturn(expectedResult);

        double result = fieldsExtractor.extractDoubleValue(row, 1);

        assertThat(expectedResult).isEqualTo(result);
    }

    @Test
    public void thatExtractStringValueReturnsNullIfCellWasBlankOrNull() {
        when(row.getCell(anyInt(), eq(Row.CREATE_NULL_AS_BLANK))).thenReturn(cell);
	    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_BLANK);

        String result = fieldsExtractor.extractStringValue(row, 1);
        assertThat(result).isEmpty();
    }

    @Test
    public void thatExtractStringValueReturnsValueOfStringCell() {
        String expectedResult = "value";
        when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
        when(cell.getStringCellValue()).thenReturn(expectedResult);

        String result = fieldsExtractor.extractStringValue(row, 1);

        assertThat(expectedResult).isEqualTo(result);
    }

    @Test
    public void thatExtractTimestampReturnsNeededTimestampFromStringCell() {
        String expectedResult = "28-MAR-2014";
        when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
        when(cell.getStringCellValue()).thenReturn(expectedResult);

        Timestamp result = fieldsExtractor.extractTimestamp(row, 1);
        DateTime dateTime = new DateTime(2014, 3, 28, 0, 0, 0);
        assertThat(result).isEqualTo(new Timestamp(dateTime.getMillis()));
    }

    @Test
    public void thatExtractTimestampReturnsNullIfCellWasBlankOrNull() {
        when(row.getCell(anyInt(), eq(Row.CREATE_NULL_AS_BLANK))).thenReturn(cell);
	    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_BLANK);

        Timestamp result = fieldsExtractor.extractTimestamp(row, 1);
        assertThat(result).isNull();
    }

	@Test(expected = CiscoException.class)
	public void thatExtractIntThrowsExceptionWhenCellTypeIsStringAndValueNonNumeric() {
		when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
		when(cell.getStringCellValue()).thenReturn("20text");

		fieldsExtractor.extractNumericValue(row, 1);
	}

	@Test(expected = CiscoException.class)
	public void thatExtractDoubleThrowsExceptionWhenCellTypeIsStringAndValueNonNumeric() {
		when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
		when(cell.getStringCellValue()).thenReturn("some text");

		fieldsExtractor.extractDoubleValue(row, 1);
	}

}

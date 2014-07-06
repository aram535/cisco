package com.cisco.claims.excel;

import com.cisco.claims.dto.Claim;
import com.cisco.excel.DefaultFieldsExtractor;
import com.cisco.exception.CiscoException;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alf on 05.07.2014.
 */
@Component
public class ExcelClaimsExtractor implements ClaimsExtractor {

	public static final int CLAIM_ID_COLUMN = 0;
	public static final int BATCH_ID_COLUMN = 3;
	public static final int PART_NUMBER_COLUMN = 10;
	public static final int SHIPPED_BN_COLUMN = 16;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final DefaultFieldsExtractor fieldsExtractor = new DefaultFieldsExtractor();

	@Override
	public List<Claim> extract(InputStream inputStream) {

		List<Claim> claims = Lists.newArrayList();

		try {
			logger.debug("extracting darts started...");
			Workbook workbook = fieldsExtractor.getWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			//skipping first row with names of columns
			rowIterator.next();

			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();

				long claimId = fieldsExtractor.extractNumericValue(row, CLAIM_ID_COLUMN);

				long batchId = fieldsExtractor.extractNumericValue(row, BATCH_ID_COLUMN);

				String partNumber = fieldsExtractor.extractStringValue(row, PART_NUMBER_COLUMN);

				String shippedBillNumber = fieldsExtractor.extractStringValue(row, SHIPPED_BN_COLUMN);

				Claim claim = new Claim(partNumber, shippedBillNumber, claimId, batchId);

				claims.add(claim);
			}

			inputStream.close();

		} catch (IOException e) {
			String errorMsg = "Error occured during Darts import";
			logger.error(errorMsg, e);
			throw new CiscoException(errorMsg,e);
		}

		return claims;
	}
}

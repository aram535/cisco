package com.cisco.serials;

import com.cisco.serials.dto.Serial;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class FileSerialsImporterTest {

	SerialsImporter serialsImporter = new DefaultSerialsImporter();

	@Test
	public void thatSerialsAreSuccessfullyImportedFromTxtFile() throws Exception {

		String serialsString = "ABC123234\nCGF213123\nASDASD213";

		List<Serial> expectedSerials = createExpectedSerials();

		List<Serial> serials = serialsImporter.importSerials(serialsString);

		assertEquals(3, serials.size());
		assertEquals(expectedSerials, serials);
	}

	private List<Serial> createExpectedSerials() {
		return Lists.newArrayList(
				new Serial("ABC123234"),
				new Serial("CGF213123"),
				new Serial("ASDASD213"));
	}
}
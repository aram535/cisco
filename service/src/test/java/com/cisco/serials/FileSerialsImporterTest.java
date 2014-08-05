package com.cisco.serials;

import com.cisco.serials.dto.Serial;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.api.Assertions.assertThat;


public class FileSerialsImporterTest {

    private SerialsImporter serialsImporter = new DefaultSerialsImporter();

    @Test
    public void thatSerialsAreSuccessfullyImportedFromTxtFile() {

        String serialsString = "ABC123234\nCGF213123\nASDASD213";

        List<Serial> serials = serialsImporter.importSerials(serialsString);

        assertThat(serials).
                hasSize(3).
                isEqualTo(createExpectedSerials());
    }

    private List<Serial> createExpectedSerials() {
        return newArrayList(
                new Serial("ABC123234"),
                new Serial("CGF213123"),
                new Serial("ASDASD213"));
    }
}
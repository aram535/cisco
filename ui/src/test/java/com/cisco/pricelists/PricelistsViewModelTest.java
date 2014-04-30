package com.cisco.pricelists;

import com.cisco.exception.CiscoException;
import com.cisco.pricelists.excel.PricelistImporter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;

import java.io.InputStream;

import static org.mockito.Mockito.*;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 18:30
 */
@RunWith(MockitoJUnitRunner.class)
public class PricelistsViewModelTest {

    @Mock
    private Media media;

    private UploadEvent uploadEvent;

    private PricelistsViewModel pricelistsViewModel = new PricelistsViewModel();

    @Mock
    private PricelistImporter pricelistImporter;

    @Mock
    private InputStream inputStream;

    @Before
    public void init() {
        uploadEvent = new UploadEvent("event", null, new Media[]{media});
        pricelistsViewModel.setPricelistImporter(pricelistImporter);
    }

    @Test(expected = CiscoException.class)
    public void thatImportPricelistThrowsCiscoExceptionIfMediaTypeIsNotBinary() {
        when(media.isBinary()).thenReturn(false);

        pricelistsViewModel.importPricelist(uploadEvent);
    }

    @Test
    public void thatPricelistViewModelDelegatesImportToPricelistImporter() {
        when(media.isBinary()).thenReturn(true);
        when(media.getStreamData()).thenReturn(inputStream);

        pricelistsViewModel.importPricelist(uploadEvent);

        verify(pricelistImporter).importPricelist(inputStream);
        verifyNoMoreInteractions(pricelistImporter);
    }
}

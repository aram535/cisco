package com.cisco.promos;

import com.cisco.exception.CiscoException;
import com.cisco.promos.excel.PromosImporter;
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
 * Time: 14:24
 */
@RunWith(MockitoJUnitRunner.class)
public class PromosViewModelTest {

    @Mock
    private Media media;

    private UploadEvent uploadEvent;

    private PromosViewModel promosViewModel = new PromosViewModel();

    @Mock
    private PromosImporter promosImporter;

    @Mock
    private InputStream inputStream;

    @Before
    public void init() {
        uploadEvent = new UploadEvent("event", null, new Media[]{media});
        promosViewModel.setPromosImporter(promosImporter);
    }

    @Test(expected = CiscoException.class)
    public void thatImportPromosThrowsCiscoExceptionIfMediaTypeIsNotBinary() {
        when(media.isBinary()).thenReturn(false);

        promosViewModel.importPromos(uploadEvent);
    }

    @Test
    public void thatPreposViewModelDelegatesImportToPromosImporter() {
        when(media.isBinary()).thenReturn(true);
        when(media.getStreamData()).thenReturn(inputStream);

        promosViewModel.importPromos(uploadEvent);

        verify(promosImporter).importPromos(inputStream);
        verifyNoMoreInteractions(promosImporter);
    }

}

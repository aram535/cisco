package com.cisco.pricelists.excel;

import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dto.Pricelist;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import static org.apache.poi.openxml4j.opc.OPCPackage.open;
import static org.xml.sax.helpers.XMLReaderFactory.createXMLReader;

/**
 * User: Rost
 * Date: 07.07.2014
 * Time: 20:01
 */
@Component
public class EventDrivenPricelistExtractor implements PricelistExtractor {

    private static final String ERROR_DURING_IMPORTING_PRICELIST_MESSAGE = "error during importing pricelist";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Map<String, Pricelist> extract(InputStream file) {
        try {
            return process(file);
        } catch (Exception ex) {
            logger.error(ERROR_DURING_IMPORTING_PRICELIST_MESSAGE, ex);
            throw new CiscoException(ERROR_DURING_IMPORTING_PRICELIST_MESSAGE, ex);
        }
    }

    private Map<String, Pricelist> process(InputStream inputStream) throws Exception {
        XSSFReader xssfReader = createXssfReader(inputStream);
        SharedStringsTable sharedStringsTable = xssfReader.getSharedStringsTable();

        XMLReader parser = createXMLReader("org.apache.xerces.parsers.SAXParser");

        SheetHandler handler = new SheetHandler(sharedStringsTable);
        parser.setContentHandler(handler);

        processSheets(xssfReader, parser);

        return handler.getPricelist();
    }

    private void processSheets(XSSFReader xssfReader, XMLReader parser) throws IOException, InvalidFormatException, SAXException {
        Iterator<InputStream> sheets = xssfReader.getSheetsData();
        while (sheets.hasNext()) {
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
    }


    private XSSFReader createXssfReader(InputStream inputStream) throws IOException, OpenXML4JException {
        OPCPackage opcPackage = open(inputStream);
        return new XSSFReader(opcPackage);
    }
}

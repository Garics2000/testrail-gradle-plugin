package org.ie.testrail.report.parser.xml;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;

public class XmlParserImpl implements XmlParser {
    private final SAXBuilder saxBuilder;

    public XmlParserImpl(SAXBuilder saxBuilder) {
        this.saxBuilder = saxBuilder;
    }

    @Override
    public Document parse(File xmlFile) throws JDOMException, IOException {
        if (xmlFile == null) {
            throw new IllegalArgumentException("The test report XML file cannot be null.");
        }

        return saxBuilder.build(xmlFile);
    }

}
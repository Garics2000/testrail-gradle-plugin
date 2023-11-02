package io.github.garics2000.testrail.report.parser.xml;

import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;

public interface XmlParser {
    Document parse(File xmlFile) throws JDOMException, IOException;
}
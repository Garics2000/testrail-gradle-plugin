package org.ie.testrail.report.parser.xml;


import org.jdom2.Document;
import org.jdom2.Element;

import java.io.File;

public class XmlValidator {
    public void validateDocument(Document document, File xmlFile) throws IllegalArgumentException {
        if (document == null) {
            throw new IllegalArgumentException("Document is null for file: " + xmlFile.getName());
        }
    }

    public void validateElement(Element rootElement, File xmlFile) throws IllegalArgumentException {
        if (rootElement == null) {
            throw new IllegalArgumentException("Root element is null for file: " + xmlFile.getName());
        }
    }
}
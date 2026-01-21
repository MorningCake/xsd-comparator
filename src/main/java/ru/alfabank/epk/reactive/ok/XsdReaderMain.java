package ru.alfabank.epk.reactive.ok;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XsdReaderMain {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        SaxXsdReader saxReader = new SaxXsdReader();
        saxReader.readXsd("UWSConsumerSubjectInfoGetInOutParms28.xsd", true, true, true, true);
    }
}

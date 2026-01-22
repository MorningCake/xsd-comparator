package ru.alfabank.epk.reactive.ok;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;

public class ParserMain {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        SaxXsdReader saxReader = new SaxXsdReader();
        saxReader.readXsd("UWSConsumerSubjectInfoGetInOutParms28_CHANGES_FOR_DIFF.xsd",
                true, true, true, true, false, "28");

    }
}

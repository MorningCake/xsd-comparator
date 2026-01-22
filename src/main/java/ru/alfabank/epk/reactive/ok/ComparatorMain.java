package ru.alfabank.epk.reactive.ok;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;

public class ComparatorMain {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        SaxXsdReader saxReader = new SaxXsdReader();
        Path path1 = saxReader.readXsd("UWSConsumerSubjectInfoGetInOutParms28.xsd",
                false, true, false, false, false, "28");

        SaxXsdReader saxReader2 = new SaxXsdReader();
        Path path2 = saxReader2.readXsd("UWSConsumerSubjectInfoGetInOutParms28_CHANGES_FOR_DIFF.xsd",
                false, true, false, false, false, "28_with_diff");

        XsdSchemaCsvComparator csvComparator = new XsdSchemaCsvComparator();

        csvComparator.compare(path1, path2);
    }
}

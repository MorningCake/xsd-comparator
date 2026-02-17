package ru.alfabank.epk.reactive.ok.generator;

import ru.alfabank.epk.reactive.ok.SaxXsdReader;
import ru.alfabank.epk.reactive.ok.XsdSchemaCsvComparator;

import java.nio.file.Path;

public class XsdGeneratorMain {
    public static void main(String[] args) {
        Path csvPath = Path.of("src/main/resources/28.csv").toAbsolutePath();

        XsdFromCsvGenerator xsdFromCsvGenerator = new XsdFromCsvGenerator();
        Path path = xsdFromCsvGenerator.xsdGenerate(csvPath, "http://epk.subject.adapter.esb.alfa.ru/webservice");
    }
}

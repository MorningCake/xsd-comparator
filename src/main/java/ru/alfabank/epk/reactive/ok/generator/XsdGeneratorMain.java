package ru.alfabank.epk.reactive.ok.generator;

import java.nio.file.Path;
import java.util.Map;

// TODO запилить форму UI (одиночная с чеком дерева и выводом xsd в файл)

public class XsdGeneratorMain {
    public static void main(String[] args) {
        Path csvPath = Path.of("src/main/resources/28.csv").toAbsolutePath();

        XsdFromCsvGenerator xsdFromCsvGenerator = new XsdFromCsvGenerator();
        Path path = xsdFromCsvGenerator.xsdGenerate(
                csvPath, "xsd",  "http://epk.subject.adapter.esb.alfa.ru/webservice",
                Map.of("http://WSCommonTypes10.CS.ws.alfabank.ru","WSCommonTypes10.xsd")
        );

        Path csvOnlyXpathsPath = Path.of("src/main/resources/29get-uwsOutput-esbInput.csv").toAbsolutePath();

        XsdFromCsvOnlyXPathGenerator xsdFromCsvOnlyXPathGenerator = new XsdFromCsvOnlyXPathGenerator();
        Path path2 = xsdFromCsvOnlyXPathGenerator.xsdGenerate(
                csvOnlyXpathsPath, "xsd",  "http://epk.subject.adapter.esb.alfa.ru/webservice",
                Map.of("http://WSCommonTypes10.CS.ws.alfabank.ru","WSCommonTypes10.xsd")
        );
    }
}

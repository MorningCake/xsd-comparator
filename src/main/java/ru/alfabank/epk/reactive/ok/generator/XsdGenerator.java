package ru.alfabank.epk.reactive.ok.generator;

import java.nio.file.Path;
import java.util.Map;

public interface XsdGenerator {
    Path xsdGenerate(Path csvPath, String namespace, String xmlnsUrl, Map<String, String> importNamespacesAndUrls);
}

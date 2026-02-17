package ru.alfabank.epk.reactive.ok;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.alfabank.epk.reactive.ui.utils.UiUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class XsdSchemaCsvComparator {

    public Path compare(Path path1, Path path2, boolean onlyXPath) {
        Set<String> csvLines1 = readCsv(path1);
        Set<String> csvLines2 = readCsv(path2);

        // найти пересечения файлов, а остатки дописать в конце
        List<String> intersections = findIntersection(csvLines1, csvLines2);
        List<String> onlyInFirst = findOnlyInFirst(csvLines1, csvLines2);
        List<String> onlyInSecond = findOnlyInSecond(csvLines1, csvLines2);

        List<String> csvLines = new ArrayList<>();
        String fileName1 = UiUtils.getFileName(path1);
        String fileName2 = UiUtils.getFileName(path2);
        csvLines.add(onlyXPath ? getCsvHeaderOnlyXPath(fileName1, fileName2) : getCsvHeader(fileName1, fileName2));

        for (String intersection : intersections) {
            csvLines.add(intersection + ",+,+");
        }
        for (String first : onlyInFirst) {
            csvLines.add(first + ",+,-");
        }
        for (String first : onlyInSecond) {
            csvLines.add(first + ",-,+");
        }
        return UiUtils.exportResultToFile("compare__" + fileName1 + "__" + fileName2, "csv", csvLines);
    }


    private static String getCsvHeader(String file1, String file2) {
        return "name,type,xPath,minOccurs,maxOccurs," + file1 + "," + file2;
    }
    private static String getCsvHeaderOnlyXPath(String file1, String file2) {
        return "xPath," + file1 + "," + file2;
    }

    private static Set<String> readCsv(Path path) {
        try {
            return new LinkedHashSet<>(Files.readAllLines(path));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла " + path);
        }
    }

    /**
     * Находит пересечение двух списков.
     */
    public static <T> List<T> findIntersection(Set<T> set1, Set<T> set2) {
        return set1.stream().filter(set2::contains).collect(Collectors.toList());
    }

    /**
     * Находит элементы, которые есть во втором списке, но отсутствуют в первом.
     */
    public static <T> List<T> findOnlyInSecond(Set<T> set1, Set<T> set2) {
        return set2.stream().filter(s2 -> !set1.contains(s2)).toList();
    }

    /**
     * Находит элементы, которые есть в первом списке, но отсутствуют во втором.
     */
    public static <T> List<T> findOnlyInFirst(Set<T> set1, Set<T> set2) {
        return set1.stream().filter(s1 -> !set2.contains(s1)).toList();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    private static class CsvLine {
        private String name;
        private String type;
        private String xPath;
        private String minOccurs;
        private String maxOccurs;

//        @Override
//        public boolean equals(Object o) {
//            if (o == null || getClass() != o.getClass()) return false;
//            CsvLine csvLine = (CsvLine) o;
//            return Objects.equals(name, csvLine.name) && Objects.equals(type, csvLine.type)
//                   && Objects.equals(xPath, csvLine.xPath) && Objects.equals(minOccurs, csvLine.minOccurs)
//                   && Objects.equals(maxOccurs, csvLine.maxOccurs);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(name, type, xPath, minOccurs, maxOccurs);
//        }
//
//        public String getDiff(CsvLine other) {
//            StringBuilder diffBuilder = new StringBuilder();
//            if (!this.equals(other)) {
//                appendFieldDiff(this.name,       other.name,       diffBuilder, "name");
//                appendFieldDiff(this.type,       other.type,       diffBuilder, "type");
//                appendFieldDiff(this.xPath,      other.xPath,      diffBuilder, "xPath");
//                appendFieldDiff(this.minOccurs,  other.minOccurs,  diffBuilder, "minOccurs");
//                appendFieldDiff(this.maxOccurs,  other.maxOccurs,  diffBuilder, "maxOccurs");
//            }
//            return diffBuilder.toString();
//        }
//
//        private void appendFieldDiff(String thisParam, String otherParam, StringBuilder diffBuilder, String paramName) {
//            if (!thisParam.equals(otherParam))
//                diffBuilder.append(paramName).append(" (").append(thisParam).append(", ").append(otherParam).append("); ");
//        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    private static class CsvComparedLine extends CsvLine {
        private boolean inFirstFile;
        private boolean inSecondFile;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class CsvComparingFile {
        private List<CsvComparedLine> comparedLines;
        private String firstFileName;
        private String secondFileName;
    }
}

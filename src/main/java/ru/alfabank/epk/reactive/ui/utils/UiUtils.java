package ru.alfabank.epk.reactive.ui.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UiUtils {

    public List<String> readAllLines(Path path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла " + path);
        }
        return lines;
    }

    public Path exportResultToFile(String name, String fileFormat, List<String> lines) {
        String fileStr = lines.stream().collect(Collectors.joining(System.lineSeparator()));
        return exportResultToFile(name, fileFormat, fileStr);
    }

    public Path exportResultToFile(String name, String fileFormat, String fileStr) {
        Path resultPath = Path.of(
                "src/main/resources/generated/" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss"))
                + "__" + name + "." + fileFormat
        ).toAbsolutePath();
        try {
            Files.writeString(resultPath, fileStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultPath;
    }

    public String getFileName(Path path) {
        String fileNameWithFormat = path.getFileName().toString();
        return fileNameWithFormat.substring(0, fileNameWithFormat.length()-4);
    }
}

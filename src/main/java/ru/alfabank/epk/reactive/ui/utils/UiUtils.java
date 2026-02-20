package ru.alfabank.epk.reactive.ui.utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static void clearGeneratedFolder(String pathStr) {
        Path path = Path.of(pathStr).toAbsolutePath();
        try (Stream<Path> walk = Files.walk(path)) {
            walk.filter(Files::isRegularFile).forEach(file -> {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при очистке temp директории (src/main/resources/generated)");
        }
    }

    /** Вспомогательный метод для копирования файла */
    public static void copyFile(String sourceFilename, File destinationFile) {
        Path sourcePath = Paths.get(sourceFilename);
        Path destPath = destinationFile.toPath();
        try {
            Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при копировании файла " + sourceFilename + " из temp " +
                                       "(src/main/resources/generated) в директорию " + destinationFile.getName());
        }
    }
}

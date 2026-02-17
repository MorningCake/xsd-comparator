package ru.alfabank.epk.reactive.ui.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
}

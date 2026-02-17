package ru.alfabank.epk.reactive.ui.xpath_comparator;

import org.apache.logging.log4j.util.Strings;
import org.jdesktop.swingx.JXTreeTable;
import ru.alfabank.epk.reactive.ok.SaxXsdReader;
import ru.alfabank.epk.reactive.ok.XsdSchemaCsvComparator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class UiProcessorOnlyXPath {

    public void process(
            String parsingName1, String parsingName2, File xsdFile, File csvFile, JPanel downloadPanel,
            JPanel treePanel, TreeTableGeneratorOnlyXPath treeTableGenerator
    ) {
        if (Strings.isBlank(parsingName1) || Strings.isBlank(parsingName2) || xsdFile == null || csvFile == null)
            throw new RuntimeException("Не заполнены необходимые поля!");
        // очистка папки generated
        Path path = Path.of("src/main/resources/generated").toAbsolutePath();
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
        // логика обработки данных
        SaxXsdReader saxReader = new SaxXsdReader();
        Path path1 = saxReader.readXsdByFilePath(xsdFile, false, true, false,
                false, false, parsingName1, true);

        Path path2 = csvFile.toPath();

        XsdSchemaCsvComparator csvComparator = new XsdSchemaCsvComparator();
        Path compared = csvComparator.compare(path1, path2, true);
        // Создание ссылок для скачивания результатов
        createDownloadLinks(downloadPanel, path1.toString(), path2.toString(), compared.toString());
        createTriesWithDiff(treePanel, path1, path2, compared, treeTableGenerator);
    }

    private void createTriesWithDiff(JPanel treePanel, Path path1, Path path2, Path compared,
                                     TreeTableGeneratorOnlyXPath treeTableGenerator) {

        JXTreeTable leftTreeTable = treeTableGenerator.generate(path1, compared, TreeTableGeneratorOnlyXPath.TreeType.LEFT);
        JXTreeTable rightTreeTable = treeTableGenerator.generate(path2, compared, TreeTableGeneratorOnlyXPath.TreeType.RIGHT);

        treePanel.removeAll();

        treePanel.add(new JScrollPane(leftTreeTable));
        treePanel.add(new JScrollPane(rightTreeTable));

        treePanel.revalidate();
        treePanel.repaint();
    }


    protected void createDownloadLinks(JPanel panel, String... filenames) {
        panel.removeAll(); // Удаляем все предыдущие компоненты
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (String filename : filenames) {
            JButton link = new JButton(filename);
            link.setAlignmentX(Component.LEFT_ALIGNMENT); // Центруем кнопку по горизонтали
            link.addActionListener(e -> showSaveDialog(panel, filename));
            panel.add(link);
        }
        panel.revalidate(); // Пересчитываем размеры и положение компонентов
        panel.repaint(); // Принудительная перерисовка панели
    }

    private void showSaveDialog(JPanel parent, String originalFilename) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(originalFilename));
        int result = chooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File destinationFile = chooser.getSelectedFile();
            try {
                // Копируем файл из исходного расположения в указанное пользователем
                copyFile(originalFilename, destinationFile);
                JOptionPane.showMessageDialog(parent, "Файл успешно сохранён: " + destinationFile.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Ошибка при сохранении файла: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Вспомогательный метод для копирования файла
    private void copyFile(String sourceFilename, File destinationFile) {
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

package ru.alfabank.epk.reactive.ui;

import org.apache.logging.log4j.util.Strings;
import org.xml.sax.SAXException;
import ru.alfabank.epk.reactive.ok.SaxXsdReader;
import ru.alfabank.epk.reactive.ok.XsdSchemaCsvComparator;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class UiProcessor {

    public void process(String parsingName1, String parsingName2, File xsdFile1, File xsdFile2, JPanel downloadPanel)
            throws ParserConfigurationException, IOException, SAXException {
        if (Strings.isBlank(parsingName1) || Strings.isBlank(parsingName2) || xsdFile1 == null || xsdFile2 == null)
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
        }

        // логика обработки данных
        SaxXsdReader saxReader = new SaxXsdReader();
        Path path1 = saxReader.readXsdByFilePath(xsdFile1,
                false, true, false, false, false, parsingName1);

        SaxXsdReader saxReader2 = new SaxXsdReader();
        Path path2 = saxReader2.readXsdByFilePath(xsdFile2,
                false, true, false, false, false, parsingName2);

        XsdSchemaCsvComparator csvComparator = new XsdSchemaCsvComparator();

        Path compared = csvComparator.compare(path1, path2);
        // Пример создания ссылок для скачивания результатов
        createDownloadLinks(downloadPanel, path1.toString(), path2.toString(), compared.toString());
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
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "Ошибка при сохранении файла: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Вспомогательный метод для копирования файла
    private void copyFile(String sourceFilename, File destinationFile) throws IOException {
        Path sourcePath = Paths.get(sourceFilename);
        Path destPath = destinationFile.toPath();
        Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
    }
}

package ru.alfabank.epk.reactive.ui.xsd_generator;

import org.apache.logging.log4j.util.Strings;
import org.jdesktop.swingx.JXTreeTable;
import ru.alfabank.epk.reactive.ok.generator.XsdFromCsvGenerator;
import ru.alfabank.epk.reactive.ok.generator.XsdFromCsvOnlyXPathGenerator;
import ru.alfabank.epk.reactive.ok.generator.XsdGenerator;
import ru.alfabank.epk.reactive.ui.utils.AbstractTreeTableGenerator;
import ru.alfabank.epk.reactive.ui.utils.UiUtils;
import ru.alfabank.epk.reactive.ui.xpath_comparator.TreeTableGeneratorOnlyXPath;
import ru.alfabank.epk.reactive.ui.xsd_comparator.TreeTableGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")
public class UiGenProcessor {

    public void process(
            String parsingName, File csvFile, JPanel downloadPanel, JPanel treePanel,
            TreeTableGenerator treeTableGenerator, TreeTableGeneratorOnlyXPath treeTableGeneratorOnlyXPath
    ) {
        if (Strings.isBlank(parsingName) || csvFile == null)
            throw new RuntimeException("Не заполнены необходимые поля!");
        // очистка папки generated
        UiUtils.clearGeneratedFolder("src/main/resources/generated");

        // логика обработки данных
        // проверить формат csv, и исходя из формата создать обработчик
        boolean isFullCsv;
        try (Stream<String> lines = Files.lines(csvFile.toPath())) {
            isFullCsv = lines.anyMatch(line -> line.split(",").length == 5);
        } catch (Exception ex) {
            throw new RuntimeException("Файл csv не может быть прочитан!");
        }
        XsdGenerator xsdGenerator = isFullCsv ? new XsdFromCsvGenerator() : new XsdFromCsvOnlyXPathGenerator();
        Path path = xsdGenerator.xsdGenerate(
                csvFile.toPath(), "xsd",  "http://epk.subject.adapter.esb.alfa.ru/webservice",
                Map.of("http://WSCommonTypes10.CS.ws.alfabank.ru","WSCommonTypes10.xsd")
        );

        // Создание ссылок для скачивания результатов
        createDownloadLinks(downloadPanel, path.toString());
        AbstractTreeTableGenerator generator = isFullCsv ? treeTableGenerator : treeTableGeneratorOnlyXPath;
        createTree(treePanel, csvFile.toPath(), generator);
    }

        private <G extends AbstractTreeTableGenerator> void createTree(JPanel treePanel, Path path, G treeTableGenerator) {
        JXTreeTable leftTreeTable = treeTableGenerator.generateWithoutDiff(path);
        treePanel.removeAll();
        treePanel.add(new JScrollPane(leftTreeTable));
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
                UiUtils.copyFile(originalFilename, destinationFile);
                JOptionPane.showMessageDialog(parent, "Файл успешно сохранён: " + destinationFile.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Ошибка при сохранении файла: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}

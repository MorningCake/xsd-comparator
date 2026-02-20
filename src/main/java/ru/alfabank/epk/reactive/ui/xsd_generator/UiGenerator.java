package ru.alfabank.epk.reactive.ui.xsd_generator;

import org.jdesktop.swingx.JXTreeTable;
import ru.alfabank.epk.reactive.ui.xsd_comparator.TreeTableGenerator;
import ru.alfabank.epk.reactive.ui.xsd_comparator.UiProcessor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class UiGenerator extends JFrame {

    private final TreeTableGenerator treeTableGenerator = new TreeTableGenerator();

    private JTextField inputText;
    private JButton selectFileButton;
    private JButton selectFileButton2;

    private JLabel selectedFileLabel;
    private JLabel selectedFileLabel2;

    private JButton executeButton;
    private JPanel downloadPanel;
    private JPanel topSouthPanel;

    private File selectedFile;

    public UiGenerator() {
        super("Генерация xsd-схемы из csv-файла (либо только с xpath, либо полного (name, type, xpath, min, max))");
        initUI();
    }


    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1500, 900));
        setLayout(new BorderLayout());

        // Верхняя панель с полями ввода
        JPanel topPanel = new JPanel(new GridLayout(1, 1));

        // Поле ввода с надписью
        JPanel fieldGroup = new JPanel();
        fieldGroup.setLayout(new BoxLayout(fieldGroup, BoxLayout.Y_AXIS));
        JLabel label1 = new JLabel("Название csv-файла:");
        label1.setFont(label1.getFont().deriveFont(Font.PLAIN, 12f));
        fieldGroup.add(label1);
        inputText = new JTextField(20);
        fieldGroup.add(inputText);
        topPanel.add(fieldGroup);

        add(topPanel, BorderLayout.NORTH);

        // Средняя панель с выбором файлов
        JPanel middlePanel = new JPanel(new BorderLayout());

// Верхняя часть средней панели (выбор файлов)
        JPanel upperMiddlePanel = new JPanel();
        upperMiddlePanel.setLayout(new BoxLayout(upperMiddlePanel, BoxLayout.Y_AXIS));

        // Группа - кнопка и метка
        JPanel group = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectFileButton = new JButton("Выбрать файл csv");
        selectedFileLabel = new JLabel("Нет выбранного файла");
        group.add(selectFileButton);
        group.add(selectedFileLabel);
        upperMiddlePanel.add(group);

        // Вторая группа (кнопка и метка)
//        JPanel group2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        selectFileButton2 = new JButton("Выбрать файл второй xsd-схемы");
//        selectedFileLabel2 = new JLabel("Нет выбранного файла");
//        group2.add(selectFileButton2);
//        group2.add(selectedFileLabel2);
//        upperMiddlePanel.add(group2);

        middlePanel.add(upperMiddlePanel, BorderLayout.NORTH);

        // Установка фиксированной высоты для верхней части
        Dimension preferredSize1 = new Dimension(1000, 100);
        upperMiddlePanel.setPreferredSize(preferredSize1);

        // Нижняя часть средней панели (кнопка "Выполнить")
        JPanel lowerMiddlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Dimension preferredSize2 = new Dimension(1000, 50);
        lowerMiddlePanel.setPreferredSize(preferredSize2);

        executeButton = new JButton("Выполнить");
        executeButton.setBackground(Color.cyan);
        executeButton.setForeground(Color.RED);
        executeButton.addActionListener(this::executeProcessing);

        lowerMiddlePanel.add(executeButton);
        middlePanel.add(lowerMiddlePanel, BorderLayout.SOUTH);

        add(middlePanel, BorderLayout.CENTER);

        // Основная панель с двумя колонками для размещения JTreeTable
        JPanel southPanel = new JPanel(new BorderLayout());

        topSouthPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        topSouthPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        Dimension preferredSize3 = new Dimension(1000, 550);
        topSouthPanel.setPreferredSize(preferredSize3);

        // Левое дерево
        JXTreeTable leftTreeTable = treeTableGenerator.initGenerate("tree");
        topSouthPanel.add(new JScrollPane(leftTreeTable));

        southPanel.add(topSouthPanel, BorderLayout.NORTH);

        // Область для скачивания результатов
        downloadPanel = new JPanel();
        downloadPanel.setBorder(BorderFactory.createTitledBorder("Файлы для скачивания:"));
        downloadPanel.setPreferredSize(new Dimension(0, 130));
        southPanel.add(downloadPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Регистрация слушателей для кнопок выбора файлов
        selectFileButton.addActionListener(e -> selectFileAndUpdateLabel(1));

        pack();
        setLocationRelativeTo(null);
    }

    private void selectFileAndUpdateLabel(int buttonNumber) {
        JFileChooser chooser = new JFileChooser();
        // Ограничиваем выбор только файлами с расширениями .txt и .log
        FileNameExtensionFilter filter = new FileNameExtensionFilter("csv-файл (*.csv)", "csv");
        chooser.setFileFilter(filter);

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            if (buttonNumber == 1) {
                selectedFile = selected;
                selectedFileLabel.setText(selectedFile.getAbsolutePath());
            }
        }
    }

    private void executeProcessing(ActionEvent e) {
        String text = inputText.getText();
        UiGenProcessor processor = new UiGenProcessor();
        try {
            processor.process(text, selectedFile, downloadPanel, topSouthPanel, treeTableGenerator);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка!", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UiGenerator app = new UiGenerator();
            app.setVisible(true);
        });
    }
}



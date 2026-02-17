package ru.alfabank.epk.reactive.ui.xsd_comparator;

import org.jdesktop.swingx.JXTreeTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class UiMain extends JFrame {

    private JTextField inputText1;
    private JTextField inputText2;
    private JButton selectFileButton1;
    private JButton selectFileButton2;
    private JLabel selectedFileLabel1;
    private JLabel selectedFileLabel2;
    private JButton executeButton;
    private JPanel downloadPanel;
    private JPanel topSouthPanel;

    private File selectedFile1;
    private File selectedFile2;

    public UiMain() {
        super("Сравнение двух xsd-схем");
        initUI();
    }


    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1500, 900));
        setLayout(new BorderLayout());

        // Верхняя панель с полями ввода
        JPanel topPanel = new JPanel(new GridLayout(1, 2));

        // Первое поле ввода с надписью
        JPanel fieldGroup1 = new JPanel();
        fieldGroup1.setLayout(new BoxLayout(fieldGroup1, BoxLayout.Y_AXIS));
        JLabel label1 = new JLabel("Название первой xsd-схемы:");
        label1.setFont(label1.getFont().deriveFont(Font.PLAIN, 12f));
        fieldGroup1.add(label1);
        inputText1 = new JTextField(20);
        fieldGroup1.add(inputText1);
        topPanel.add(fieldGroup1);

        // Второе поле ввода с надписью
        JPanel fieldGroup2 = new JPanel();
        fieldGroup2.setLayout(new BoxLayout(fieldGroup2, BoxLayout.Y_AXIS));
        JLabel label2 = new JLabel("Название второй xsd-схемы:");
        label2.setFont(label2.getFont().deriveFont(Font.PLAIN, 12f));
        fieldGroup2.add(label2);
        inputText2 = new JTextField(20);
        fieldGroup2.add(inputText2);
        topPanel.add(fieldGroup2);

        add(topPanel, BorderLayout.NORTH);

        // Средняя панель с выбором файлов
        JPanel middlePanel = new JPanel(new BorderLayout());

// Верхняя часть средней панели (выбор файлов)
        JPanel upperMiddlePanel = new JPanel();
        upperMiddlePanel.setLayout(new BoxLayout(upperMiddlePanel, BoxLayout.Y_AXIS));

        // Первая группа (кнопка и метка)
        JPanel group1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectFileButton1 = new JButton("Выбрать файл первой xsd-схемы");
        selectedFileLabel1 = new JLabel("Нет выбранного файла");
        group1.add(selectFileButton1);
        group1.add(selectedFileLabel1);
        upperMiddlePanel.add(group1);

        // Вторая группа (кнопка и метка)
        JPanel group2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectFileButton2 = new JButton("Выбрать файл второй xsd-схемы");
        selectedFileLabel2 = new JLabel("Нет выбранного файла");
        group2.add(selectFileButton2);
        group2.add(selectedFileLabel2);
        upperMiddlePanel.add(group2);
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

        topSouthPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        topSouthPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        Dimension preferredSize3 = new Dimension(1000, 550);
        topSouthPanel.setPreferredSize(preferredSize3);

        // Левое дерево
        JXTreeTable leftTreeTable = TreeTableGenerator.initGenerate("xsd1");
        topSouthPanel.add(new JScrollPane(leftTreeTable));

        // Правое дерево
        JXTreeTable rightTreeTable = TreeTableGenerator.initGenerate("xsd2");
        topSouthPanel.add(new JScrollPane(rightTreeTable));
        southPanel.add(topSouthPanel, BorderLayout.NORTH);

        // Область для скачивания результатов
        downloadPanel = new JPanel();
        downloadPanel.setBorder(BorderFactory.createTitledBorder("Файлы для скачивания:"));
        downloadPanel.setPreferredSize(new Dimension(0, 130));
        southPanel.add(downloadPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Регистрация слушателей для кнопок выбора файлов
        selectFileButton1.addActionListener(e -> selectFileAndUpdateLabel(1));
        selectFileButton2.addActionListener(e -> selectFileAndUpdateLabel(2));

        pack();
        setLocationRelativeTo(null);
    }

    private void selectFileAndUpdateLabel(int buttonNumber) {
        JFileChooser chooser = new JFileChooser();
        // Ограничиваем выбор только файлами с расширениями .txt и .log
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xsd-схема (*.xsd)", "xsd");
        chooser.setFileFilter(filter);

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (buttonNumber == 1) {
                selectedFile1 = selectedFile;
                selectedFileLabel1.setText(selectedFile.getAbsolutePath());
            } else {
                selectedFile2 = selectedFile;
                selectedFileLabel2.setText(selectedFile.getAbsolutePath());
            }
        }
    }

    private void executeProcessing(ActionEvent e) {
        String text1 = inputText1.getText();
        String text2 = inputText2.getText();

        UiProcessor processor = new UiProcessor();
        try {
            processor.process(text1, text2, selectedFile1, selectedFile2, downloadPanel, topSouthPanel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка!", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UiMain app = new UiMain();
            app.setVisible(true);
        });
    }
}



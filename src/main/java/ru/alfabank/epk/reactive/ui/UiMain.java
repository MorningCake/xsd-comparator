package ru.alfabank.epk.reactive.ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UiMain extends JFrame {

    private JTextField inputText1;
    private JTextField inputText2;
    private JButton selectFileButton1;
    private JButton selectFileButton2;
    private JLabel selectedFileLabel1;
    private JLabel selectedFileLabel2;
    private JButton executeButton;
    private JXTreeTable leftTreeTable;
    private JXTreeTable rightTreeTable;
    private JPanel downloadPanel;

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

        JPanel topSouthPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        topSouthPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        Dimension preferredSize3 = new Dimension(1000, 520);
        topSouthPanel.setPreferredSize(preferredSize3);

        // Левое дерево
        JXTreeTable leftTreeTable = createCustomizedTreeTable("Левое дерево");
        topSouthPanel.add(leftTreeTable);

        // Правое дерево
        JXTreeTable rightTreeTable = createCustomizedTreeTable("Правое дерево");
        topSouthPanel.add(rightTreeTable);
        southPanel.add(topSouthPanel, BorderLayout.NORTH);

        // Область для скачивания результатов
        downloadPanel = new JPanel();
        downloadPanel.setBorder(BorderFactory.createTitledBorder("Файлы для скачивания:"));
        downloadPanel.setPreferredSize(new Dimension(0, 100));

        southPanel.add(downloadPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // Регистрация слушателей для кнопок выбора файлов
        selectFileButton1.addActionListener(e -> selectFileAndUpdateLabel(1));
        selectFileButton2.addActionListener(e -> selectFileAndUpdateLabel(2));

        pack();
        setLocationRelativeTo(null);
    }

    //public DualTreeTablesExample() {
    //        setTitle("Пример двух JTreeTable с кастомизацией");
    //        setDefaultCloseOperation(EXIT_ON_CLOSE);
    //        setLayout(new BorderLayout());
    //
    //        // Основная панель с двумя колонками для размещения JTreeTable
    //        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    //        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    //
    //        // Левое дерево
    //        JXTreeTable leftTreeTable = createCustomizedTreeTable("Левое дерево");
    //        centerPanel.add(leftTreeTable);
    //
    //        // Правое дерево
    //        JXTreeTable rightTreeTable = createCustomizedTreeTable("Правое дерево");
    //        centerPanel.add(rightTreeTable);
    //
    //        // Панель загрузки результатов (downloadPanel)
    //        JPanel downloadPanel = new JPanel();
    //        downloadPanel.setBorder(BorderFactory.createTitledBorder("Файлы для скачивания:"));
    //        downloadPanel.setPreferredSize(new Dimension(0, 100));
    //
    //        // Добавляем центрированную панель и панель загрузки на форму
    //        add(centerPanel, BorderLayout.CENTER);
    //        add(downloadPanel, BorderLayout.SOUTH);
    //
    //        pack();
    //        setLocationRelativeTo(null);
    //    }
    //
        private JXTreeTable createCustomizedTreeTable(String title) {
            // Создаем модель данных для дерева
//            MutableTreeTableNode root = new DefaultMutableTreeTableNode(new Object[]{title, "Данные 1"});
//            MutableTreeTableNode child1 = new DefaultMutableTreeTableNode(new Object[]{"Ребенок 1", "Данные ребенка 1"});
//            MutableTreeTableNode child2 = new DefaultMutableTreeTableNode(new Object[]{"Ребенок 2", "Данные ребенка 2"});
//            root.insert(child1, 0);
//            root.insert(child2, 1);

// usage
            ArrayNode root = new ArrayNode(new Object[] {"root", "0"});
            for (int i = 0; i < 5; i++) {
                ArrayNode arrayNode = new ArrayNode(new Object[]{"child", "" + i});
                root.add(arrayNode);
                for (int j = 0; j < 5; j++) {
                    arrayNode.add(new ArrayNode(new Object[]{"sub-child", "" + j}));
                }
            }

//            JXTreeTable table = new JXTreeTable(new DefaultTreeTableModel(root, new ArrayList<>(java.util.List.of("name", "index"))));
            JXTreeTable table = new JXTreeTable(new DefaultTreeTableModel(root));


//            JXTreeTable treeTable = getJxTreeTable(root);
//            JXTreeTable treeTable = JXTreeTableNode.getJXTreeTable(root);

//            // Настраиваем ширину столбцов
//            TableColumn column = treeTable.getColumnModel().getColumn(0);
//            column.setPreferredWidth(200);
//            column = treeTable.getColumnModel().getColumn(1);
//            column.setPreferredWidth(300);
//
//            // Кастомизация внешнего вида узлов
//            treeTable.setRowHeight(25);
//            treeTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
//
//            // Добавляем слушатель мыши для подсветки узлов
//            treeTable.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseEntered(MouseEvent e) {
//                    int row = treeTable.rowAtPoint(e.getPoint());
//                    if (row >= 0) {
//                        treeTable.changeSelection(row, 0, false, false);
//                    }
//                }
//            });

            // Возвращаем созданный JTreeTable
            return table;
        }

    private static JXTreeTable getJxTreeTable(MutableTreeTableNode root)  {

        //  public int getColumnCount();
        //  public Object getValueAt(Object node, int column);
        //  public Object getChild(Object parent, int index);
        //  public int getChildCount(Object parent);
        //  public int getIndexOfChild(Object parent, Object child);
        //  public boolean isLeaf(Object node);

        AbstractTreeTableModel model = new AbstractTreeTableModel(root) {

            @Override
            public Object getChild(Object parent, int index) {
                return ((DefaultMutableTreeTableNode) parent).getChildAt(index);
            }

            @Override
            public int getChildCount(Object parent) {
                return ((DefaultMutableTreeTableNode) parent).getChildCount();
            }

            @Override
            public int getIndexOfChild(Object parent, Object child) {
                return 0;
            }

            @Override
            public Object getValueAt(Object node, int column) {
                return ((DefaultMutableTreeTableNode) node).getValueAt(column);
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int column) {
                return switch (column) {
                    case 0 -> "ID";
                    case 1 -> "Данные";

                    default -> "";
                    };
            }
        };

        // Создаем JTreeTable
        JXTreeTable treeTable = new JXTreeTable(model);
        return treeTable;
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
            processor.process(text1, text2, selectedFile1, selectedFile2, downloadPanel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this, // Текущий компонент (например, JFrame)
                    ex.getMessage(),
                    "Ошибка!",
                    JOptionPane.ERROR_MESSAGE
            );
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



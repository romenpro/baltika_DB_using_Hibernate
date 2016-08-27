package baltika;

import baltika.excel.ExcelAdapter;
import baltika.listeners.*;
import baltika.table.MyTableModel;
import baltika.table.ResultModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UI extends JFrame {

    private static JSpinner spinner;
    private static double partOfSN;
    private static JTextField jTextField;
    private static String stringJComboBox;
    private static Date spinnerDate;
    private static MyTableModel factTableModel;
    private MyTableModel planTableModel;

    public static String getStringJComboBox() {
        return stringJComboBox;
    }

    public static void setStringJComboBox(String s) {
        stringJComboBox = s;
    }

    public static Date getSpinnerDate() {
        if (spinnerDate == null) {
            spinnerDate = (Date) spinner.getValue();
        }
        return spinnerDate;
    }

    public static void setSpinnerDate(Date date){
        spinnerDate = date;
    }

    public static double getPartOfSN(){
        return partOfSN;
    }

    public static void setPartOfSN(double partOfSN){
        jTextField.setText(String.valueOf(partOfSN));
    }

    public static MyTableModel getFactTableModel() {
        return factTableModel;
    }

    public static String fileNameTosave(){
        String s = stringJComboBox + "_" + new SimpleDateFormat("MMyyyy").format(getSpinnerDate())
                + ".xls";
        return s;
    }

    public UI() {
        final OracleConnection oracleConnection = new OracleConnection();
        oracleConnection.createArrayLists();
        ArrayList<BaltikaObject> baltikaObjects = oracleConnection.getBaltikaObjects();

        JLabel branchLabel = new JLabel("Выберите филиал");
//        branch.setForeground(Color.black);
//        branch.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(Color.gray),
//                BorderFactory.createEmptyBorder(15,15,15,15)
//        ));

        JLabel dateLabel = new JLabel("Выберите период расчета");

        JLabel partSNLabel = new JLabel("Доля СН");

        JComboBox<BaltikaObject> jComboBox = new JComboBox<BaltikaObject>();
        for (BaltikaObject arrayList : baltikaObjects) {
            jComboBox.addItem(arrayList);
        }
        jComboBox.setMaximumRowCount(baltikaObjects.size());
        stringJComboBox = jComboBox.getSelectedItem().toString();

        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -10);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 100);
        Date latestDate = calendar.getTime();
        SpinnerModel dateModel = new SpinnerDateModel(initDate,
                earliestDate,
                latestDate,
                Calendar.YEAR);
        spinner = new JSpinner(dateModel);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "MM/yyyy"));

        jTextField = new JTextField();
//        partOfSN = Double.parseDouble(jTextField.getText().replace(",", ".").replace(" ", ""));
        jTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                partOfSN = Double.parseDouble(jTextField.getText().replace(",", ".").replace(" ", ""));
            }
        });
        jTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
//                super.focusLost(e);
                partOfSN = Double.parseDouble(jTextField.getText().replace(",", ".").replace(" ", ""));
            }
        });

        JButton buttonCalculate = new JButton("Рассчитать");

        JLabel labelSave = new JLabel("Сохранить результат в файл: " + fileNameTosave());

        JButton buttonSave = new JButton("Сохранить в файл");

        JButton buttonSaveToDB = new JButton("Сохранить в базу данных");

        JButton buttonReadFromDB = new JButton("Прочитать из базы данных");

        factTableModel = new MyTableModel();
        JTable fact = new JTable(factTableModel);
//        fact.setRowHeight(20);
        factTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {

            }
        });
        ExcelAdapter factEA = new ExcelAdapter(fact);
//        fact.setFillsViewportHeight(true);
        fact.setCellSelectionEnabled(true);
        fact.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane factScrollPane = new JScrollPane(fact);

        planTableModel = new MyTableModel();
        JTable plan = new JTable(planTableModel);
        for (int i = 0; i < planTableModel.getRowCount(); i++) {
            for (int j = 1; j < planTableModel.getColumnCount(); j++) {
                planTableModel.setValueAt("0", i, j);
            }
        }
        ExcelAdapter planEA = new ExcelAdapter(plan);
        plan.setCellSelectionEnabled(true);
        plan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane planScrollPane = new JScrollPane(plan);

        JTable result = new JTable(new ResultModel(36, 2));
        result.setTableHeader(null);
//        result.getCellRenderer(0, 1).getTableCellRendererComponent(null, null, false, false, 0, 1).setBackground(Color.blue);
        result.setValueAt("Сумма потребленной электроэнергии, кВт*ч", 0, 0);
        result.setValueAt("Рг, кВт", 1, 0);
        result.setValueAt("Рс, кВт", 2, 0);
        result.setValueAt("Расчет стоимости электроэнергии по 3,4 ценовой категории", 4, 0);
        result.setValueAt("Со сбытовой надбавкой", 5, 0);
        result.setValueAt("C, руб", 6, 0);
        result.setValueAt("W, кВт*ч", 7, 0);
        result.setValueAt("Ц, руб/кВт*ч, ", 8, 0);
        result.setValueAt("Без сбытовой надбавки", 9, 0);
        result.setValueAt("C, руб", 10, 0);
        result.setValueAt("W, кВт*ч", 11, 0);
        result.setValueAt("Ц, руб/кВт*ч", 12, 0);
        result.setValueAt("Сбытовая надбавка по электроэнергии", 13, 0);
        result.setValueAt("Расчет стоимости электроэнергии по 5,6 ценовой категории", 15, 0);
        result.setValueAt("РСВ", 16, 0);
        result.setValueAt("Сэ1, руб", 17, 0);
        result.setValueAt("W, кВт*ч", 18, 0);
        result.setValueAt("Ц, руб/кВт*ч", 19, 0);
        result.setValueAt("БР +", 20, 0);
        result.setValueAt("Сэ2, руб", 21, 0);
        result.setValueAt("W, кВт*ч", 22, 0);
        result.setValueAt("Ц, руб/кВт*ч", 23, 0);
        result.setValueAt("БР -", 24, 0);
        result.setValueAt("Сэ3, руб", 25, 0);
        result.setValueAt("W, кВт*ч", 26, 0);
        result.setValueAt("Ц, руб/кВт*ч", 27, 0);
        result.setValueAt("Сэ4, руб", 28, 0);
        result.setValueAt("W'4, кВт*ч", 29, 0);
        result.setValueAt("Цэ4, руб/кВт*ч", 30, 0);
        result.setValueAt("Сэ5, руб", 31, 0);
        result.setValueAt("W'5, кВт*ч", 32, 0);
        result.setValueAt("Цэ5, руб/кВт*ч", 33, 0);
        result.setValueAt("Средневзвешенная нерегулируемая цена на мощность на оптовом рынке, руб/кВт*ч", 35, 0);
        result.setShowVerticalLines(false);
        result.setCellSelectionEnabled(true);
        TableColumn column;
        column = result.getColumnModel().getColumn(0);
        column.setPreferredWidth(300);

        JScrollPane resultScrollPane = new JScrollPane(result);

        JTable distribution = new JTable(new ResultModel(7, 18));
        distribution.setTableHeader(null);
        distribution.setCellSelectionEnabled(true);
        distribution.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane distributionScrollPane = new JScrollPane(distribution);

        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.setSize(800, 600);
        jTabbedPane.addTab("Потребленная электроэнергия", factScrollPane);
        jTabbedPane.addTab("План по потреблению", planScrollPane);
        jTabbedPane.addTab("Результат", resultScrollPane);
        jTabbedPane.addTab("Распределение часовых отклонений", distributionScrollPane);

        buttonCalculate.addActionListener(new CalculateActionListener(baltikaObjects, factTableModel,
                planTableModel, result, distribution));
        buttonSave.addActionListener(new SaveFileActionListener(result, distribution));
        jComboBox.addItemListener(new MyItemListener(labelSave));
        spinner.addChangeListener(new MyChangeListener(factTableModel, planTableModel,
                labelSave, spinner));
        buttonSaveToDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                try {
//                    oracleConnection.deleteTable();
//                    oracleConnection.createTable();
//                } catch (SQLException e1) {
//                    e1.printStackTrace();
//                }
                oracleConnection.insertIntoTable();
            }
        });
        buttonReadFromDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oracleConnection.readFromTable();
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Правка");
        JMenuItem menuItem = new JMenuItem("Ввод интервальных значений");
        menuBar.add(menu);
        menu.add(menuItem);
        menuItem.addActionListener(new AddHourPeakActionListener(this, baltikaObjects));

        this.setJMenuBar(menuBar);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
//        c.insets = new Insets(5, 5, 5, 5);
        c.ipady = 5;
//        c.weightx = 1;
//        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 0, 10);
        getContentPane().add(branchLabel, c);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 10, 10, 10);
        getContentPane().add(jComboBox, c);
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(10, 10, 0, 10);
        getContentPane().add(dateLabel, c);
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 10, 10, 10);
        getContentPane().add(spinner, c);
        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(10, 10, 0, 10);
        getContentPane().add(partSNLabel, c);
        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(0, 10, 10, 10);
        getContentPane().add(jTextField, c);
        c.gridx = 0;
        c.gridy = 6;
        c.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(buttonCalculate, c);
        c.gridx = 0;
        c.gridy = 7;
        c.insets = new Insets(10, 10, 0, 10);
        getContentPane().add(labelSave, c);
        c.anchor = GridBagConstraints.PAGE_START;
        c.weighty = 0;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 8;
        c.insets = new Insets(0, 10, 10, 10);
        getContentPane().add(buttonSave, c);
        c.gridx = 0;
        c.gridy = 9;
        c.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(buttonSaveToDB, c);
        c.gridx = 0;
        c.gridy = 10;
        c.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(buttonReadFromDB, c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 9;
        c.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(jTabbedPane, c);

        setSize(1000, 700);
//        setBounds(100, 100, 900, 700);
        setLocationRelativeTo(null);
    }

    private static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Couldn't use system look and feel.");
        }
        UI ui = null;
        ui = new UI();
//        ui.setTitle("Дерево обьектов");
        ui.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        ui.pack();
        ui.setVisible(true);
    }

    public static void main(String[] args) {
//        System.setProperty("file.encoding", "UTF-8");
//        System.out.println(System.getProperty("file.encoding"));
        System.setProperty("proxySet", "true");
        System.setProperty("http.proxyHost", "spbsrvprx-vwsa1.baltikacorp.ds.local");
        System.setProperty("http.proxyPort", "3128");
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("BALTIKACORP\\kokoshnikov","QAZwsx117".toCharArray());
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

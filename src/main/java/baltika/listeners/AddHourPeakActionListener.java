package baltika.listeners;

import baltika.BaltikaObject;
import baltika.table.HourPeakModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddHourPeakActionListener implements ActionListener {
    private JFrame ui;
    private ArrayList<BaltikaObject> baltikaObjects;
    private String stringJComboBox, stringDate;
    private Date dateSpinner;
    private JSpinner spinner;
    private JTable hourPeakTable;
    private HourPeakModel hourPeakModel;

    public AddHourPeakActionListener(JFrame ui, ArrayList<BaltikaObject> baltikaObjects) {
        this.ui = ui;
        this.baltikaObjects = baltikaObjects;
    }

    private void hourPeakReadFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new FileInputStream("Часовые пики.ini"), "UTF-8"))) {
            String str, str1, str2, str3;
            char[] chars;
            int ch;
            boolean isReaded = false;
            while ((str = bufferedReader.readLine()) != null) {
                if (str.equals(stringJComboBox) || str.substring(1).equals(stringJComboBox)) {
                    str1 = bufferedReader.readLine();
                    if (str1.equals(stringDate)) {
                        isReaded = true;
                        for (int i = 0; i < hourPeakTable.getRowCount(); i++) {
                            str2 = bufferedReader.readLine();
                            ch = str2.indexOf(';');
                            str3 = str2.substring(ch + 1);
                            chars = str3.toCharArray();
                            for (int j = 1; j < hourPeakTable.getColumnCount(); j++) {
                                if (chars[j - 1] == '1') {
                                    hourPeakTable.setValueAt(Boolean.TRUE, i, j);
                                } else {
                                    if (chars[j - 1] == '0') {
                                        hourPeakTable.setValueAt(Boolean.FALSE, i, j);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!isReaded) {
                setHourPeakTableToFalse();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hourPeakWriteToFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("Часовые пики.ini", true), "UTF-8"))) {
            Object obj;
            String str;
            bufferedWriter.write(stringJComboBox);
            bufferedWriter.newLine();
            bufferedWriter.write(stringDate);
            bufferedWriter.newLine();
            for (int i = 0; i < hourPeakTable.getRowCount(); i++) {
                for (int j = 0; j < hourPeakTable.getColumnCount(); j++) {
                    obj = hourPeakTable.getValueAt(i, j);
                    if (obj instanceof Boolean) {
                        if ((Boolean) obj == true) {
                            str = "1";
                        } else {
                            str = "0";
                        }
                        bufferedWriter.write(str);
                    } else {
                        bufferedWriter.write(hourPeakTable.getValueAt(i, j).toString() + ";");
                    }
                }
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setHourPeakTableToFalse() {
        for (int i = 0; i < hourPeakTable.getRowCount(); i++) {
            for (int j = 1; j < hourPeakTable.getColumnCount(); j++) {
                hourPeakTable.setValueAt(new Boolean(false), i, j);
            }
        }
    }

    private void setStringDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        stringDate = dateFormat.format(dateSpinner);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame jFrame = new JFrame();
        JLabel branchLabel = new JLabel("Выберите филиал");

        JLabel dateLabel = new JLabel("Выберите год");

        JComboBox<BaltikaObject> jComboBox = new JComboBox<BaltikaObject>();
        for (BaltikaObject arrayList : baltikaObjects) {
            jComboBox.addItem(arrayList);
        }
        jComboBox.setMaximumRowCount(baltikaObjects.size());
        stringJComboBox = jComboBox.getSelectedItem().toString();
        jComboBox.addItemListener(new ItemListener() {
            Object lastValue = null;
            Object currentValue = null;

            @Override
            public void itemStateChanged(ItemEvent e) {
                Object currentValue = e.getItem();
                if (lastValue != null && !currentValue.equals(lastValue)) {
                    stringJComboBox = currentValue.toString();
                    hourPeakReadFromFile();
                }
                lastValue = currentValue;
            }
        });

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
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy"));
        dateSpinner = (Date) spinner.getValue();
        spinner.addChangeListener(new ChangeListener() {
            Object lastValue = null;
            Object currentValue = null;

            @Override
            public void stateChanged(ChangeEvent e) {
                currentValue = spinner.getValue();
                if (lastValue != null && !currentValue.equals(lastValue)) {
                    dateSpinner = (Date) spinner.getValue();
                    setStringDate();
                    hourPeakReadFromFile();
                }
                lastValue = currentValue;
            }
        });

        final JButton buttonEdit = new JButton("Редактировать");

        final JButton buttonSave = new JButton("Сохранить");
        buttonSave.setEnabled(false);

        hourPeakModel = new HourPeakModel();
        hourPeakTable = new JTable(hourPeakModel);
        hourPeakTable.setCellSelectionEnabled(true);
        TableColumn column;
        column = hourPeakTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(200);
        hourPeakTable.setEnabled(false);
        JScrollPane hourPeakScrollPane = new JScrollPane(hourPeakTable);

        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hourPeakTable.setEnabled(true);
                buttonSave.setEnabled(true);
                buttonEdit.setEnabled(false);
            }
        });

        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hourPeakTable.setEnabled(false);
                buttonSave.setEnabled(false);
                buttonEdit.setEnabled(true);
                hourPeakWriteToFile();
            }
        });

        jFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.ipady = 5;
//        c.weightx = 1;
//        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 0, 10);
        jFrame.getContentPane().add(branchLabel, c);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 10, 10, 10);
        jFrame.getContentPane().add(jComboBox, c);
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(10, 10, 0, 10);
        jFrame.getContentPane().add(dateLabel, c);
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 10, 10, 10);
        jFrame.getContentPane().add(spinner, c);
        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(10, 10, 10, 10);
        jFrame.getContentPane().add(buttonEdit, c);
        c.anchor = GridBagConstraints.PAGE_START;
        c.weighty = 0;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(10, 10, 10, 10);
        jFrame.getContentPane().add(buttonSave, c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 6;
        c.insets = new Insets(10, 10, 10, 10);
        jFrame.getContentPane().add(hourPeakScrollPane, c);

        setStringDate();
        hourPeakReadFromFile();

        jFrame.setSize(1000, 700);
        jFrame.setLocationRelativeTo(null);
        ui.setVisible(false);
        jFrame.setVisible(true);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
                ui.setVisible(true);
            }
        });
    }
}

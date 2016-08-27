package baltika.listeners;

import baltika.BaltikaObject;
import baltika.DialogFrame;
import baltika.UI;
import baltika.excel.Calcfacthour;
import baltika.excel.Retail;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalculateActionListener implements ActionListener {
    private Map<String, String[]> pattern;
    private ArrayList<BaltikaObject> baltikaObjects;
    //    private File file;
    private Calcfacthour calcfacthour;
    private Retail retail;
    private String branch;
    //    private String currentDate;
    private String fileNameHourPeak;
    private String fileNamePrice;
    private String firsrPartOfURLHourPeak = "http://www.atsenergo.ru/dload/calcfacthour_regions/";
    private String firsrPartOfURLPrice = "http://www.atsenergo.ru/dload/retail/";
    private TableModel factTableModel;
    private TableModel planTableModel;
    private JTable result;
    private JTable distribution;
    private Map<String, String> hourPeak;
    private double totalEnergy;
    private double totalEnergyFact;
    private double totalEnergyPlan;
    private double totalEnergy4;
    private double totalEnergy5;
    private double powerGen;
    private double powerSet;
    private double[][] energyFact;
    private double[][] energyPlan;
    private double[][] priceRSV;
    private double[][] priceVf;
    private double[][] priceVpl;
    private double[][] priceRSViBR;
    private double[][] energyFactPlan;
    private double[][] energyPlanFact;
    private double partOfSN;
    double sn;
    private double costWithSN;
    private double priceWithSN;
    private double costWithoutSN;
    private double priceWithoutSN;
    private double costWithSNe1;
    private double priceWithSNe1;
    private double costWithSNe2;
    private double priceWithSNe2;
    private double costWithSNe3;
    private double priceWithSNe3;
    private double costWithSNe4;
    private double priceWithSNe4;
    private double costWithSNe5;
    private double priceWithSNe5;
    private double priceNebalRSV;
    private double priceNebalBR;
    private double priceOpt;
    private double[][] percentTable;
    private String[] percentString1 = {"<=2%", "<=3%", "<=4%", "<=5%", "<=6%", "<=7%", "<=8%", "<=9%", "<=10%",
            "<=15%", "<=20%", "<=25%", "<=30%", "<=35%", "<=40%", "<=50%", ">50%"};
    private String[] percentString2 = {"<=2%", "<=5%", "<=10%", "<=15%", "<=20%", "<=25%", "<=30%", "<=40%", "<=50%"};
    private int[] percentCount1;
    private int[] percentCount2;
    private double[] percentOfPercent1;
    private double[] percentOfPercent2;
    Integer sum;
    int sumOfPercent;
    double max;
    double averageOfPersent;
    int beginHour1 = -1;
    int endHour1 = -1;
    int beginHour2 = 0;
    int endHour2 = 0;

    public CalculateActionListener(ArrayList<BaltikaObject> baltikaObjects, TableModel factTableModel,
                                   TableModel planTableModel, JTable result, JTable distribution) {
        this.baltikaObjects = baltikaObjects;
        this.factTableModel = factTableModel;
        this.planTableModel = planTableModel;
        this.result = result;
        this.distribution = distribution;
        pattern = new HashMap<String, String[]>();
        setPattern();
        for (int i = 0; i < percentString1.length; i++) {
            distribution.setValueAt(percentString1[i], 0, i);
        }
        for (int i = 0; i < percentString2.length; i++) {
            distribution.setValueAt(percentString2[i], 4, i);
        }
    }

    private double[][] fromTableToArray(TableModel myTableModel) {
        String s;
        double array[][] = new double[myTableModel.getRowCount()][myTableModel.getColumnCount() - 1];
        for (int i = 0; i < myTableModel.getRowCount(); i++) {
            for (int j = 1; j < myTableModel.getColumnCount(); j++) {
                s = (String) myTableModel.getValueAt(i, j);
                array[i][j - 1] = Double.parseDouble(s.replaceAll(" ", "").replaceAll(",", "."));
            }
        }
        return array;
    }

    private String currentDate(String pattern) {
        Date spinnerDate = UI.getSpinnerDate();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(spinnerDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(spinnerDate);
    }

    private String currentDateTextMonth(String pattern) {
        String numberOfMonth, month = null;
        Date spinnerDate = UI.getSpinnerDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        numberOfMonth = dateFormat.format(spinnerDate);
        switch (numberOfMonth) {
            case "01": {
                month = "ЯНВАРЬ";
                break;
            }
            case "02": {
                month = "ФЕВРАЛЬ";
                break;
            }
            case "03": {
                month = "МАРТ";
                break;
            }
            case "04": {
                month = "АПРЕЛЬ";
                break;
            }
            case "05": {
                month = "МАЙ";
                break;
            }
            case "06": {
                month = "ИЮНЬ";
                break;
            }
            case "07": {
                month = "ИЮЛЬ";
                break;
            }
            case "08": {
                month = "АВГУСТ";
                break;
            }
            case "09": {
                month = "СЕНТЯБРЬ";
                break;
            }
            case "10": {
                month = "ОКТЯБРЬ";
                break;
            }
            case "11": {
                month = "НОЯБРЬ";
                break;
            }
            case "12": {
                month = "ДЕКАБРЬ";
                break;
            }
        }
        return month;
    }

    private String nextMonth(String pattern) {
        Date spinnerDate = UI.getSpinnerDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(spinnerDate);
        calendar.add(Calendar.MONTH, +1);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(calendar.getTime());
    }

    private void setPattern() {
        pattern.put(baltikaObjects.get(0).toString(), new String[]{"_DEKENERG_08_calcfacthour.xls",
                ""});
        pattern.put(baltikaObjects.get(1).toString(), new String[]{"_ROSTOVEN_60_calcfacthour.xls",
                "_ROSTOVEN_PROSTOVE_"});
        pattern.put(baltikaObjects.get(2).toString(), new String[]{"_LENENERG_41_calcfacthour.xls",
                "_LENENERG_PLENENER_"});
        pattern.put(baltikaObjects.get(3).toString(), new String[]{"_SAMARAEN_36_calcfacthour.xls",
                "_SAMARAEN_PSAMARAE_"});
        pattern.put(baltikaObjects.get(4).toString(), new String[]{"_TULAENSK_70_calcfacthour.xls",
                "_TULAENSK_PTULENER_"});
        pattern.put(baltikaObjects.get(5).toString(), new String[]{"_CHELENER_75_calcfacthour.xls",
                "_CHELENER_PCHELENE_"});
        pattern.put(baltikaObjects.get(6).toString(), new String[]{"_VORNEGEN_20_calcfacthour.xls",
                "_VORNEGEN_PVORNEGE_"});
        pattern.put(baltikaObjects.get(7).toString(), new String[]{"_YARENERG_78_calcfacthour.xls",
                "_YARENERG_PYARENER_"});
        pattern.put(baltikaObjects.get(8).toString(), new String[]{"_LENENERG_41_calcfacthour.xls",
                "_LENENERG_PLENENER_"});
        pattern.put(baltikaObjects.get(9).toString(), new String[]{"_KRASNOEN_04_calcfacthour.xls",
                "_KRASNOEN_PKRASNEN_"});
        pattern.put(baltikaObjects.get(10).toString(), new String[]{"_SBRENERG_50_calcfacthour.xls",
                "_SBRENERG_PNOVOSIB_"});
    }

    private String setFileNameHourPeak() {
        return currentDate("yyyyMM") + pattern.get(branch)[0];
    }

    private String setFileNamePrice() {
        String s;
        s = nextMonth("yyyyMM") + "10" + pattern.get(branch)[1] + currentDate("MMyyyy") +
                "_gtp_1st_stage.xls";
        return s;
    }

    private void fileDownload(String fileName, String firsrPartOfURL) throws IOException {
        File file = new File("Загруженные файлы/" + fileName);
        URL url = null;
        url = new URL(firsrPartOfURL + fileName);
        FileUtils.copyURLToFile(url, file);
    }

    private double sum(double[][] array) {
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                sum = sum + array[i][j];
            }
        }
        return sum;
    }

    private char[] readIntervalHourPeak() {
        char[] chars = new char[24];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = '0';
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new FileInputStream("Часовые пики.ini"), "UTF-8"))) {
            String str, str1;
            int ch;
            while ((str = bufferedReader.readLine()) != null) {
                if (str.equals(branch) || str.substring(1).equals(branch)) {
                    if (bufferedReader.readLine().equals(currentDate("yyyy"))) {
                        while ((str = bufferedReader.readLine()) != null) {
                            ch = str.indexOf(';');
                            str1 = str.substring(0, ch);
                            if (str1.equals(currentDateTextMonth("MM"))) {
                                str1 = str.substring(ch + 1);
                                chars = str1.toCharArray();
                                break;
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chars;
    }

    private void interval(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] == '1') && (beginHour1 == -1)) {
                beginHour1 = i;
            }
            if ((chars[i] == '0') && (beginHour1 != -1) && (chars[i - 1] == '1') && (beginHour2 == 0)) {
                endHour1 = i - 1;
            }
            if ((chars[i] == '1') && (beginHour2 == 0) && (endHour1 != -1)) {
                beginHour2 = i;
            }
            if ((chars[i] == '0') && (beginHour2 != 0) && (chars[i - 1] == '1')) {
                endHour2 = i - 1;
            }
        }
    }

    private void power() {
//        int beginHour1 = Integer.parseInt(hour(7));
//        int endHour1 = Integer.parseInt(hour(11));
//        int beginHour2 = Integer.parseInt(hour(15));
//        int endHour2 = Integer.parseInt(hour(20));
        beginHour1 = -1;
        endHour1 = -1;
        beginHour2 = 0;
        endHour2 = 0;
        char[] chars;
        chars = readIntervalHourPeak();
        interval(chars);
        ArrayList<Double> d = new ArrayList<Double>();
        ArrayList<Double> d1 = new ArrayList<Double>();
        String key, value, day, hour;
        double sum = 0, sum1 = 0;
        Set set = hourPeak.entrySet();
        for (Object element : set) {
            Map.Entry entry = (Map.Entry) element;
            key = (String) entry.getKey();
            day = key.substring(0, 2);
            value = (String) entry.getValue();
            hour = value.substring(0, 2);
            d.add(findValueAtHourPeak(day, hour));
            if (beginHour1 != -1) {
                d1.add(findMaxValue(day, beginHour1, endHour1, beginHour2, endHour2));
            }
        }
        for (Double aDouble : d) {
            sum = sum + aDouble;
        }
        powerGen = sum / d.size();
        for (Double aDouble : d1) {
            sum1 = sum1 + aDouble;
        }
        powerSet = sum1 / d1.size();
    }

    private double findValueAtHourPeak(String day, String hour) {
        int row = 0, column = 0;
        String s;
        for (int i = 0; i < factTableModel.getRowCount(); i++) {
            s = (String) factTableModel.getValueAt(i, 0);
            if (hour.equals(s.substring(0, 2))) {
                row = i;
                break;
            }
        }
        for (int i = 1; i < factTableModel.getColumnCount(); i++) {
            s = factTableModel.getColumnName(i);
            if (day.equals(s.substring(0, 2))) {
                column = i;
                break;
            }
        }
        s = (String) factTableModel.getValueAt(row, column);
        return Double.parseDouble(s.replaceAll(" ", "").replaceAll(",", "."));
    }

    private double findMaxValue(String day, int beginHour1, int endHour1,
                                int beginHour2, int endHour2) {
        double[] d = new double[endHour1 - beginHour1 + endHour2 - beginHour2 + 2];
        int column = 0;
        String s;
        for (int i = 1; i < factTableModel.getColumnCount(); i++) {
            s = factTableModel.getColumnName(i);
            if (day.equals(s.substring(0, 2))) {
                column = i;
                break;
            }
        }
        setArray(d, beginHour1, endHour1, column, 0);
        setArray(d, beginHour2, endHour2, column, endHour1 - beginHour1 + 1);
        Arrays.sort(d);
        return d[d.length - 1];
    }

//    private String hour(int i) {
//        String s1, s2;
//        s1 = (String) factTableModel.getValueAt(i, 0);
//        if (s1.charAt(1) == ':'){
//            s2 = s1.substring(0, 1);
//        } else {
//            s2 = s1.substring(0, 2);
//        }
//        return s2;
//    }

    private void setArray(double[] d, int beginHour, int endHour, int column, int count) {
        String s;
        for (int row = beginHour, i = count; row <= endHour; row++, i++) {
            s = (String) factTableModel.getValueAt(row, column);
            d[i] = Double.parseDouble(s.replaceAll(" ", "").replaceAll(",", "."));
        }
    }

    private void print(double[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + "  ");
            }
            System.out.println();
        }
    }

    private int countOfPercent(String s) {
        double doubleFromString = Double.parseDouble(s.replace("<", "").replace("=", "").replace("%", "").replace(">", "")) / 100;
        int count = 0;
        for (int i = 0; i < percentTable.length; i++) {
            for (int j = 0; j < percentTable[i].length; j++) {
                if (percentTable[i][j] <= doubleFromString) {
                    count++;
                }
            }
        }
        return count;
    }

    private void calculate() {
        double[][] priceFor3CKWithSN = new double[24][priceRSV[0].length];
        for (int i = 0; i < priceFor3CKWithSN.length; i++) {
            for (int j = 0; j < priceFor3CKWithSN[i].length; j++) {
                priceFor3CKWithSN[i][j] = priceRSViBR[i][j] * (1 + partOfSN);
//                priceFor3CKWithSN[i][j] = (double) Math.round(priceFor3CKWithSN[i][j]*1000000000)/1000000000;
            }
        }
        costWithSN = 0;
        for (int i = 0; i < priceFor3CKWithSN.length; i++) {
            for (int j = 0; j < priceFor3CKWithSN[i].length; j++) {
                costWithSN = costWithSN + (priceFor3CKWithSN[i][j] * energyFact[i][j]);
            }
        }
        priceWithSN = costWithSN / totalEnergy;

        costWithoutSN = 0;
        for (int i = 0; i < priceRSViBR.length; i++) {
            for (int j = 0; j < priceRSViBR[i].length; j++) {
                costWithoutSN = costWithoutSN + (priceRSViBR[i][j] * energyFact[i][j]);
            }
        }
        priceWithoutSN = costWithoutSN / totalEnergy;
        sn = (costWithSN - costWithoutSN) / totalEnergy;

        double[][] priceRSVWithSN = new double[24][priceRSV[0].length];
        for (int i = 0; i < priceRSVWithSN.length; i++) {
            for (int j = 0; j < priceRSVWithSN[i].length; j++) {
                priceRSVWithSN[i][j] = priceRSV[i][j] * (1 + partOfSN);
//                priceRSVWithSN[i][j] = (double) Math.round(priceRSVWithSN[i][j]*1000000000)/1000000000;
            }
        }
        costWithSNe1 = 0;
        for (int i = 0; i < priceRSVWithSN.length; i++) {
            for (int j = 0; j < priceRSVWithSN[i].length; j++) {
                costWithSNe1 = costWithSNe1 + (priceRSVWithSN[i][j] * energyFact[i][j]);
            }
        }
        priceWithSNe1 = costWithSNe1 / totalEnergy;

        energyFactPlan = new double[24][energyFact[0].length];
        for (int i = 0; i < energyFactPlan.length; i++) {
            for (int j = 0; j < energyFactPlan[i].length; j++) {
                if (energyFact[i][j] > energyPlan[i][j]) {
                    energyFactPlan[i][j] = energyFact[i][j] - energyPlan[i][j];
                } else {
                    energyFactPlan[i][j] = 0;
                }
            }
        }
        totalEnergyFact = 0;
        for (int i = 0; i < energyFactPlan.length; i++) {
            for (int j = 0; j < energyFactPlan[i].length; j++) {
                totalEnergyFact = totalEnergyFact + energyFactPlan[i][j];
            }
        }
        double[][] priceBRplusSN = new double[24][energyFact[0].length];
        for (int i = 0; i < priceBRplusSN.length; i++) {
            for (int j = 0; j < priceBRplusSN[i].length; j++) {
                priceBRplusSN[i][j] = priceVf[i][j] * (1 + partOfSN);
//                priceRSVWithSN[i][j] = (double) Math.round(priceRSVWithSN[i][j]*1000000000)/1000000000;
            }
        }
        costWithSNe2 = 0;
        for (int i = 0; i < priceBRplusSN.length; i++) {
            for (int j = 0; j < priceBRplusSN[i].length; j++) {
                costWithSNe2 = costWithSNe2 + (priceBRplusSN[i][j] * energyFactPlan[i][j]);
            }
        }
        priceWithSNe2 = costWithSNe2 / totalEnergyFact;

        energyPlanFact = new double[24][energyFact[0].length];
        for (int i = 0; i < energyPlanFact.length; i++) {
            for (int j = 0; j < energyPlanFact[i].length; j++) {
                if (energyFact[i][j] < energyPlan[i][j]) {
                    energyPlanFact[i][j] = energyPlan[i][j] - energyFact[i][j];
                } else {
                    energyPlanFact[i][j] = 0;
                }
            }
        }
        totalEnergyPlan = 0;
        for (int i = 0; i < energyPlanFact.length; i++) {
            for (int j = 0; j < energyPlanFact[i].length; j++) {
                totalEnergyPlan = totalEnergyPlan + energyPlanFact[i][j];
            }
        }
        double[][] priceBRminusSN = new double[24][energyFact[0].length];
        for (int i = 0; i < priceBRminusSN.length; i++) {
            for (int j = 0; j < priceBRminusSN[i].length; j++) {
                priceBRminusSN[i][j] = priceVpl[i][j] * (1 + partOfSN);
//                priceRSVWithSN[i][j] = (double) Math.round(priceRSVWithSN[i][j]*1000000000)/1000000000;
            }
        }
        costWithSNe3 = 0;
        for (int i = 0; i < priceBRplusSN.length; i++) {
            for (int j = 0; j < priceBRplusSN[i].length; j++) {
                costWithSNe3 = costWithSNe3 + (priceBRminusSN[i][j] * energyPlanFact[i][j]);
            }
        }
        priceWithSNe3 = costWithSNe3 / totalEnergyPlan;

        totalEnergy4 = sum(energyPlan);
        priceWithSNe4 = Math.abs(priceNebalRSV / 1000 * (1 + partOfSN));
        costWithSNe4 = priceWithSNe4 * totalEnergy4;

        totalEnergy5 = sum(energyFactPlan) + sum(energyPlanFact);
        priceWithSNe5 = Math.abs(priceNebalBR / 1000 * (1 + partOfSN));
        costWithSNe5 = priceWithSNe5 * totalEnergy5;

        percentTable = new double[24][energyFact[0].length];
        for (int i = 0; i < percentTable.length; i++) {
            for (int j = 0; j < percentTable[i].length; j++) {
                percentTable[i][j] = Math.abs(energyFact[i][j] - energyPlan[i][j]) / energyPlan[i][j];
            }
        }
//TODO print percentTable if energyPlan = 0
        percentCount1 = new int[percentString1.length];
        percentOfPercent1 = new double[percentString1.length];
        for (int i = 0; i < percentString1.length; i++) {
            if (percentString1[i].compareTo("<=2%") == 0) {
                percentCount1[i] = countOfPercent(percentString1[i]);
                continue;
            }
            if (percentString1[i].compareTo(">50%") == 0) {
                double doubleFromString = Double.parseDouble(percentString1[i].replace("<", "").replace("=", "").replace("%", "").replace(">", "")) / 100;
                int count = 0;
                for (int i1 = 0; i1 < percentTable.length; i1++) {
                    for (int j1 = 0; j1 < percentTable[i1].length; j1++) {
                        if (percentTable[i1][j1] > doubleFromString) {
                            count++;
                        }
                    }
                }
                percentCount1[i] = count;
                continue;
            }
            percentCount1[i] = countOfPercent(percentString1[i]) - countOfPercent(percentString1[i - 1]);
        }
        sum = 0;
        for (int i = 0; i < percentCount1.length; i++) {
            sum = sum + percentCount1[i];
        }
        Integer percentCount;
        for (int i = 0; i < percentOfPercent1.length; i++) {
            percentCount = percentCount1[i];
            percentOfPercent1[i] = Math.round(percentCount.doubleValue() * 100 / sum.doubleValue());
        }
        sumOfPercent = 0;
        for (int i = 0; i < percentOfPercent1.length; i++) {
            sumOfPercent = sumOfPercent + new Double(percentOfPercent1[i]).intValue();
        }

        percentCount2 = new int[percentString2.length];
        percentOfPercent2 = new double[percentString2.length];
        for (int i = 0; i < percentString2.length; i++) {
            percentCount2[i] = countOfPercent(percentString2[i]);
        }
        for (int i = 0; i < percentOfPercent2.length; i++) {
            percentCount = percentCount2[i];
            percentOfPercent2[i] = Math.round(percentCount.doubleValue() * 100 / sum.doubleValue());
        }

        double sum1 = 0;
        max = 0;
        for (int i = 0; i < percentTable.length; i++) {
            for (int j = 0; j < percentTable[i].length; j++) {
                if (percentTable[i][j] > max) {
                    max = percentTable[i][j];
                }
                sum1 = sum1 + percentTable[i][j];
            }
        }
        max = Math.round(max * 100);
        averageOfPersent = sum1 * 100 / (percentTable.length * percentTable[0].length);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            branch = UI.getStringJComboBox();
            partOfSN = UI.getPartOfSN();
            fileNameHourPeak = setFileNameHourPeak();
            fileDownload(fileNameHourPeak, firsrPartOfURLHourPeak);
            fileNamePrice = setFileNamePrice();
            fileDownload(fileNamePrice, firsrPartOfURLPrice + currentDate("yyyyMM") + "01" + "/");
            calcfacthour = new Calcfacthour(fileNameHourPeak);
            calcfacthour.read();
            energyFact = fromTableToArray(factTableModel);
            energyPlan = fromTableToArray(planTableModel);
            totalEnergy = sum(energyFact);
            retail = new Retail(fileNamePrice);
            retail.read();
            priceRSV = retail.getPriceRSV();
            priceVf = retail.getPriceVf();
            priceVpl = retail.getPriceVpl();
            priceRSViBR = retail.getPriceRSViBR();
            priceNebalRSV = retail.getPriceNebalRSV();
            priceNebalBR = retail.getPriceNebalBR();
            priceOpt = retail.getPriceOpt();
            calculate();
            hourPeak = calcfacthour.getHourPeak();
            power();
            result.setValueAt(totalEnergy, 0, 1);
            result.setValueAt(powerGen, 1, 1);
            result.setValueAt(powerSet, 2, 1);
            result.setValueAt(costWithSN, 6, 1);
            result.setValueAt(totalEnergy, 7, 1);
            result.setValueAt(priceWithSN, 8, 1);
            result.setValueAt(costWithoutSN, 10, 1);
            result.setValueAt(totalEnergy, 11, 1);
            result.setValueAt(priceWithoutSN, 12, 1);
            result.setValueAt(sn, 13, 1);
            result.setValueAt(costWithSNe1, 17, 1);
            result.setValueAt(totalEnergy, 18, 1);
            result.setValueAt(priceWithSNe1, 19, 1);
            result.setValueAt(costWithSNe2, 21, 1);
            result.setValueAt(totalEnergyFact, 22, 1);
            result.setValueAt(priceWithSNe2, 23, 1);
            result.setValueAt(costWithSNe3, 25, 1);
            result.setValueAt(totalEnergyPlan, 26, 1);
            result.setValueAt(priceWithSNe3, 27, 1);
            result.setValueAt(costWithSNe4, 28, 1);
            result.setValueAt(totalEnergy4, 29, 1);
            result.setValueAt(priceWithSNe4, 30, 1);
            result.setValueAt(costWithSNe5, 31, 1);
            result.setValueAt(totalEnergy5, 32, 1);
            result.setValueAt(priceWithSNe5, 33, 1);
            result.setValueAt(priceOpt, 35, 1);
            for (int i = 0; i < percentCount1.length; i++) {
                distribution.setValueAt(percentCount1[i], 1, i);
            }
            for (int i = 0; i < percentOfPercent1.length; i++) {
                distribution.setValueAt(new Double(percentOfPercent1[i]).intValue() + "%", 2, i);
            }
            for (int i = 0; i < percentCount2.length; i++) {
                distribution.setValueAt(percentCount2[i], 5, i);
            }
            for (int i = 0; i < percentOfPercent2.length; i++) {
                distribution.setValueAt(new Double(percentOfPercent2[i]).intValue() + "%", 6, i);
            }
            distribution.setValueAt(sum, 1, 17);
            distribution.setValueAt(sumOfPercent, 2, 17);
            distribution.setValueAt("max %", 4, 9);
            distribution.setValueAt(new Double(max).intValue() + "%", 5, 9);
            distribution.setValueAt("ср. откл. %", 4, 10);
            distribution.setValueAt(new Double(averageOfPersent).intValue() + "%", 5, 10);
            DialogFrame.showSuccessMessage("Выполнено");
        } catch (Exception ex) {
            DialogFrame.showWarningMessage(ex);
            ex.printStackTrace();
        }
    }
}

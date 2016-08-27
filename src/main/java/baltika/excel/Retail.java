package baltika.excel;

import baltika.UI;
import com.sun.org.apache.xpath.internal.functions.FuncRound;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class Retail {
    private String fileName;
    private double[][] priceRSV;
    private double[][] priceVf;
    private double[][] priceVpl;
    private double[][] priceRSViBR;
    private double priceNebalRSV;
    private double priceNebalBR;
    private double priceOpt;
    int numberOfDays;

    public Retail(String fileName) {
        this.fileName = fileName;
        numberOfDays = numberOfDays();
    }

    private int numberOfDays(){
        Date date = UI.getSpinnerDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public double[][] getPriceRSV() {
        return priceRSV;
    }

    public double[][] getPriceVf() {
        return priceVf;
    }

    public double[][] getPriceVpl() {
        return priceVpl;
    }

    public double[][] getPriceRSViBR() {
        return priceRSViBR;
    }

    public double getPriceNebalRSV() {
        return priceNebalRSV;
    }

    public double getPriceNebalBR() {
        return priceNebalBR;
    }

    public double getPriceOpt() {
        return priceOpt;
    }

    public void read(){
        File file = new File("Загруженные файлы/" + fileName);
        FileInputStream fis = null;
        String date = null, hour = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HSSFWorkbook myWorkBook = null;
        try {
            myWorkBook = new HSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        priceRSV = new double[24][numberOfDays];
        priceVf = new double[24][numberOfDays];
        priceVpl = new double[24][numberOfDays];
        priceRSViBR = new double[24][numberOfDays];
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);
        priceRSV = getArray(mySheet, numberOfDays, 2);
        priceVf = getArray(mySheet, numberOfDays, 3);
        priceVpl = getArray(mySheet, numberOfDays, 4);
        priceRSViBR = getArray(mySheet, numberOfDays, 5);
        priceNebalRSV = readCell(mySheet, 36);
        priceNebalBR = readCell(mySheet, 37);
        priceOpt = readCell(mySheet, 23) / 1000;
//        Set set = hourPeak.entrySet();
//        for (Object element : set){
//            Map.Entry entry = (Map.Entry) element;
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
    }

    private double readCell(HSSFSheet mySheet, int c){
        Row row = mySheet.getRow(c);
        Cell cell = row.getCell(1);
        return Double.parseDouble(cell.getStringCellValue().replaceAll(",","."));
    }

    private double[][] getArray(HSSFSheet mySheet, int numberOfDays, int c){
        Iterator<Row> rowIterator = mySheet.iterator();
        int i = 0, j = 0;
        double[][] array = new double[24][numberOfDays];
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() < 40) {
                continue;
            }
            Iterator<Cell> cellIterator = row.cellIterator();
            if (cellIterator.hasNext() == false) {
                break;
            }
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getColumnIndex() == c) {
                    if (i == 24) {
                        j++;
                        i = 0;
                    }
                    if (j == numberOfDays) {
                        break;
                    }
                    array[i][j] = (Double.parseDouble(cell.getStringCellValue().replaceAll(",",
                            ".")))/1000;
                    array[i][j] = (double) Math.round(array[i][j]*100000)/100000;
                    i++;
                }
                if (cell.getColumnIndex() == (c + 1)) {
                    break;
                }
            }
        }
        return array;
    }
}

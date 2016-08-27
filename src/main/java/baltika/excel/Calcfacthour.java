package baltika.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Calcfacthour {
    private String fileName;
    private Map<String, String> hourPeak = new HashMap<String, String>();

    public Calcfacthour(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, String> getHourPeak() {
        return hourPeak;
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

        HSSFSheet mySheet = myWorkBook.getSheetAt(0);
        Iterator<Row> rowIterator = mySheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() < 8){
                continue;
            }
            Iterator<Cell> cellIterator = row.cellIterator();
            if (cellIterator.hasNext() == false){
                break;
            }
            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();

                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        date = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        hour = new Double(cell.getNumericCellValue()).toString();
                        break;
                    default : ;
                }
            }
            hourPeak.put(date, hour);
        }
//        Set set = hourPeak.entrySet();
//        for (Object element : set){
//            Map.Entry entry = (Map.Entry) element;
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
    }
}

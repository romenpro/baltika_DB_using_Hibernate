package baltika.listeners;

import baltika.DialogFrame;
import baltika.UI;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;

public class SaveFileActionListener implements ActionListener {
    private JTable result, distribution;
    private Sheet sheetResult;
    private Sheet sheetDistribution;
    private Workbook book;

    public SaveFileActionListener(JTable result, JTable distribution) {
        this.result = result;
        this.distribution = distribution;
    }

    private void createRowSheetResult(int rowNum, boolean isMerged) {
        Row row = sheetResult.createRow(rowNum);
        Cell cell = row.createCell(0);
        if (isMerged) {
            CellStyle style = book.createCellStyle();
            Font font = book.createFont();
            font.setBold(true);
//                font.setFontHeightInPoints((short) 12);
            style.setFont(font);
            cell.setCellValue((String) result.getValueAt(rowNum, 0));
            cell.setCellStyle(style);
            sheetResult.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2));
        } else {
            cell.setCellValue((String) result.getValueAt(rowNum, 0));
            cell = row.createCell(1);
            cell.setCellValue((Double) result.getValueAt(rowNum, 1));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            new File("Сохраненные файлы").mkdir();
            File file = new File("Сохраненные файлы/" + UI.fileNameTosave());
            book = new HSSFWorkbook();
            sheetResult = book.createSheet("Результат");
            sheetDistribution = book.createSheet("Распределение часовых отклонений");

            createRowSheetResult(0, false);
            createRowSheetResult(1, false);
            createRowSheetResult(2, false);
            createRowSheetResult(4, true);
            createRowSheetResult(5, true);
            createRowSheetResult(6, false);
            createRowSheetResult(7, false);
            createRowSheetResult(8, false);
            createRowSheetResult(9, true);
            createRowSheetResult(10, false);
            createRowSheetResult(11, false);
            createRowSheetResult(12, false);
            createRowSheetResult(13, false);
            createRowSheetResult(15, true);
            createRowSheetResult(16, true);
            createRowSheetResult(17, false);
            createRowSheetResult(18, false);
            createRowSheetResult(19, false);
            createRowSheetResult(20, true);
            createRowSheetResult(21, false);
            createRowSheetResult(22, false);
            createRowSheetResult(23, false);
            createRowSheetResult(24, true);
            createRowSheetResult(25, false);
            createRowSheetResult(26, false);
            createRowSheetResult(27, false);
            createRowSheetResult(28, false);
            createRowSheetResult(29, false);
            createRowSheetResult(30, false);
            createRowSheetResult(31, false);
            createRowSheetResult(32, false);
            createRowSheetResult(33, false);
            createRowSheetResult(35, false);

            Row row;
            Cell cell;
            CellStyle style;
            for (int i = 0; i < distribution.getRowCount(); i++){
                row = sheetDistribution.createRow(i);
                for (int j = 0; j < distribution.getColumnCount(); j++){
                    cell = row.createCell(j);
                    style = book.createCellStyle();
                    style.setAlignment(CellStyle.ALIGN_CENTER);
                    cell.setCellStyle(style);
                    if (distribution.getValueAt(i, j) instanceof String){
                        cell.setCellValue((String) distribution.getValueAt(i, j));
                    }
                    if (distribution.getValueAt(i, j) instanceof Integer){
                        cell.setCellValue((Integer) distribution.getValueAt(i, j));
                    }
                }
            }


            sheetDistribution.autoSizeColumn(10);
            sheetResult.autoSizeColumn(0);
            sheetResult.autoSizeColumn(1);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            book.write(fileOutputStream);
            fileOutputStream.close();
            book.close();
            DialogFrame.showSuccessMessage("Файл сохранен");
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (Exception e1) {
            DialogFrame.showWarningMessage(e1);
//            e1.printStackTrace();
        }
    }
}

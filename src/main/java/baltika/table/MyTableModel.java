package baltika.table;

import baltika.UI;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MyTableModel extends AbstractTableModel {

    protected String[] columnNames;
    protected Object[][] data;
    private Date spinnerDate;
    private int daysInMonth;

    public MyTableModel() {
        this.columnNames = dateString();
        data = new Object[24][daysInMonth+1];
        String[] h = hourString();
        for(int i=0;i<24;i++){
            data[i][0] = h[i];
        }
    }

    public void changeTableModel(){
        this.columnNames = dateString();
        data = new Object[24][daysInMonth+1];
        String[] h = hourString();
        for(int i=0;i<24;i++){
            data[i][0] = h[i];
        }
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public boolean isCellEditable(int row, int col) {
        if (col < 1) {
            return false;
        } else {
            return true;
        }
    }

    private String[] hourString(){
        String [] s = new String[24];
        s[23] = "0:00";
        for(int i = 0 ; i < 23 ; i++){
            s[i]=((i+1)+":00");
        }
        return s;
    }

    private String[] dateString(){
        ArrayList<String> days = new ArrayList<String>();
        String pattern = "dd.MM.yyyy";
        spinnerDate = UI.getSpinnerDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(spinnerDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        int currentMonth = calendar.get(Calendar.MONTH);
        for (int i = 1; i <= 31; i++){
            int month = calendar.get(Calendar.MONTH);
            if(month == currentMonth){
                calendar.set(Calendar.DAY_OF_MONTH,i);
                Date date = calendar.getTime();
                days.add(dateFormat.format(date));
                daysInMonth = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_MONTH,i+1);
            }
        }
        String[] s = days.toArray(new String[daysInMonth]);
        String[] str = new String[s.length+1];
        System.arraycopy(s,0,str,1,s.length);
        return str;
    }
}

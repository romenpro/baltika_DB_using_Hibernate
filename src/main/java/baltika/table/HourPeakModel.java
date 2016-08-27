package baltika.table;

public class HourPeakModel extends MyTableModel {
    String[] month = {"ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ",
            "МАЙ", "ИЮНЬ", "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"};

    public HourPeakModel() {
        this.columnNames = hourString();
        data = new Object[month.length][this.columnNames.length + 1];
        for (int i = 0; i < month.length; i++) {
            data[i][0] = month[i];
        }
        for (int i = 0; i < data.length; i++) {
            for (int j = 1; j < data[i].length; j++) {
                data[i][j] = new Boolean(false);
            }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    private String[] hourString() {
        String[] s = new String[24];
        for (int i = 0; i < 24; i++) {
            s[i] = String.valueOf(i + 1);
        }
        String[] str = new String[s.length + 1];
        System.arraycopy(s, 0, str, 1, s.length);
        return str;
    }
}
package baltika.table;

import javax.swing.table.DefaultTableModel;

public class ResultModel extends DefaultTableModel {
    public ResultModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}

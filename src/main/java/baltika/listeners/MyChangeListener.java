package baltika.listeners;

import baltika.UI;
import baltika.table.MyTableModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Date;

public class MyChangeListener implements ChangeListener {
    private Object lastValue;
    private Object currentValue;
    private MyTableModel factModel, planModel;
    private JLabel labelSave;
    private JSpinner spinner;

    public MyChangeListener(MyTableModel factModel, MyTableModel planModel,
                            JLabel labelSave, JSpinner spinner) {
        this.factModel = factModel;
        this.planModel = planModel;
        this.labelSave = labelSave;
        this.spinner = spinner;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        currentValue = spinner.getValue();
        if (lastValue != null && !currentValue.equals(lastValue)) {
            UI.setSpinnerDate((Date) currentValue);
            factModel.changeTableModel();
            planModel.changeTableModel();
            for (int i = 0; i < planModel.getRowCount(); i++) {
                for (int j = 1; j < planModel.getColumnCount(); j++) {
                    planModel.setValueAt("0", i, j);
                }
            }
            labelSave.setText("Сохранить результат в файл: " + UI.fileNameTosave());
        }
        lastValue = currentValue;
    }
}

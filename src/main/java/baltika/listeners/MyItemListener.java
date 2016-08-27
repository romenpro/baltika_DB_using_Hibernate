package baltika.listeners;

import baltika.UI;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MyItemListener implements ItemListener {
    private Object lastValue;
    private Object currentValue;
    private JLabel labelSave;

    public MyItemListener(JLabel labelSave) {
        this.labelSave = labelSave;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        currentValue = e.getItem();
        if(lastValue != null && !currentValue.equals(lastValue)){
            UI.setStringJComboBox(currentValue.toString());
            labelSave.setText("Сохранить результат в файл: " + UI.fileNameTosave());
//            System.out.println(currentValue.toString());
        }
        lastValue = currentValue;
    }
}
package baltika;

import javax.swing.*;
import java.io.IOException;

public class DialogFrame {
    public static void showWarningMessage(Exception e) {
        JOptionPane.showMessageDialog(null,
                e.fillInStackTrace(),
                "",
                JOptionPane.WARNING_MESSAGE);
    }

    public static void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null,
                message,
                "",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

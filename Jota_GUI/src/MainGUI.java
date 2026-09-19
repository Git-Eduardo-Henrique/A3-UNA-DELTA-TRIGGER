import gui.DeltaTriggerGUI;

import javax.swing.SwingUtilities;

public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeltaTriggerGUI().iniciar());
    }
}

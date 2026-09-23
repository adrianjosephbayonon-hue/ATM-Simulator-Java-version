import javax.swing.SwingUtilities;

public class GuiApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new ATMGui();
        });
    }
}

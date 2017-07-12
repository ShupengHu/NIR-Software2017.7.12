package samples;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.Frame;


/**
 * <p>Title: TeeChart for Java Swing example application </p>
 *
 * <p>Description: Simple Swing Application using TeeChart</p>
 *
 * <p>Copyright: Copyright (c) 2006 Steema Sofware SL</p>
 *
 * <p>Company: Steema Software SL</p>
 *
 * @author db
 * @version 1.0
 */
public class Application1 {
    boolean packFrame = false;

    /**
     * Construct and show the application.
     */
    public Application1() {
        Frame frame = new Frame1();
        // Validate frames that have preset sizes
        // Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }

        // Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2,
                          (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

    /**
     * Application entry point.
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                new Application1();
            }
        });
    }
}

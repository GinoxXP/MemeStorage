import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MemeWindow {

    public MemeWindow(MainFrame mainFrame){
        buildUI(mainFrame);
    }

    private void buildUI(MainFrame mainFrame){
        mainFrame.getMainPanel().removeAll();

        fillImages(mainFrame);

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void fillImages(MainFrame mainFrame){
        JPanel contentPanel = new JPanel(new GridLayout(0,5));

        File[] files = new File("storage/images").listFiles();
        Arrays.sort(files, (f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));
        for(int i = 0; i < files.length; i++)
        {
            File image = files[i];
            try {
                contentPanel.add(new ImageLabel(image, contentPanel, mainFrame));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mainFrame.getMainPanel().add(contentPanel);
    }
}

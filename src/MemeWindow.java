import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MemeWindow {

    private int imagesStartCount = 0;
    private int imagesGroupCount = 100;
    private int currentPage = 1;
    private int pageCount;

    private MainFrame mainFrame;

    public MemeWindow(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI(){
        mainFrame.getMainPanel().removeAll();
        mainFrame.getMainPanel().setLayout(new BorderLayout());

        fillImages();

        addTurnPageButtons();
        addPageNumber();

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void fillImages(){
        JPanel contentPanel = new JPanel(new GridLayout(0,5));

        File[] files = new File("storage/images").listFiles();
        Arrays.sort(files, (f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));
        for(int i = imagesStartCount; i < files.length && i < imagesStartCount + imagesGroupCount; i++)
        {
            File image = files[i];
            try {
                contentPanel.add(new ImageLabel(image, contentPanel, mainFrame));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        pageCount = files.length / imagesGroupCount;
        if(files.length % imagesGroupCount != 0)
            pageCount++;

        mainFrame.getMainPanel().add(contentPanel, BorderLayout.CENTER);
    }

    private void addTurnPageButtons(){
        JButton back = new JButton("<");
        if(imagesStartCount <= 0)
            back.setEnabled(false);
        back.addActionListener(actionEvent -> {
            imagesStartCount -= imagesGroupCount;
            currentPage--;
            buildUI();
        });
        mainFrame.getMainPanel().add(back, BorderLayout.WEST);


        JButton next = new JButton(">");
        if(imagesStartCount >= imagesGroupCount)
            next.setEnabled(false);
        next.addActionListener(actionEvent -> {
            imagesStartCount += imagesGroupCount;
            currentPage++;
            buildUI();
        });
        mainFrame.getMainPanel().add(next, BorderLayout.EAST);
    }

    private void addPageNumber(){
        JLabel pageTextUp = new JLabel(Localization.getPage() + " " + currentPage + " / " + pageCount, SwingConstants.CENTER);
        mainFrame.getMainPanel().add(pageTextUp, BorderLayout.NORTH);

        JLabel pageTextDown = new JLabel(Localization.getPage() + " " + currentPage + " / " + pageCount, SwingConstants.CENTER);
        mainFrame.getMainPanel().add(pageTextDown, BorderLayout.SOUTH);
    }
}

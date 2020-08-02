import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TagsWindow {

    public TagsWindow(MainFrame mainFrame){
        buildUI(mainFrame);
    }

    void buildUI(MainFrame mainFrame) {
        mainFrame.mainPanel.removeAll();

        mainFrame.mainPanel.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new GridLayout(0, 5));
        File[] filesArr = new File("storage/tags/").listFiles();

        Arrays.sort(filesArr, (f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));

        String[] tagsFile = new String[filesArr.length];
        for (int i = 0; i < tagsFile.length; i++)
            tagsFile[i] = filesArr[i].getName();

        if (tagsFile.length > 0) {
            JList<String> tagsList = new JList<>();

            JScrollPane tagsScrollPane = new JScrollPane(tagsList);
            tagsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            tagsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            tagsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

            JScrollPane contentScrollPane = new JScrollPane(contentPanel);
            contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);


            ArrayList<String> allTags = new ArrayList<>();                                                              ///
            for (int i = 0; i < tagsFile.length; i++)                                                                    /// Feeling list
                allTags.add(tagsFile[i]);                                                                                   ///
            ///
            tagsList.setListData(allTags.toArray(String[]::new));                                                       ///


            JTextField searchField = new JTextField();
            searchField.addCaretListener(caretEvent -> {
                ArrayList<String> searchedTags = new ArrayList<>();
                for (int i = 0; i < tagsFile.length; i++)
                    if (tagsFile[i].contains(searchField.getText()))
                        searchedTags.add(tagsFile[i]);

                tagsList.setListData(searchedTags.toArray(String[]::new));

                mainFrame.revalidate();
                mainFrame.repaint();
            });

            mainFrame.mainPanel.add(searchField, BorderLayout.NORTH);

            mainFrame.mainPanel.add(tagsScrollPane, BorderLayout.WEST);
            mainFrame.mainPanel.add(contentScrollPane, BorderLayout.CENTER);

            tagsList.addListSelectionListener(listSelectionEvent -> {
                try {
                    contentPanel.removeAll();
                    contentPanel.revalidate();

                    BufferedReader reader = new BufferedReader(new FileReader("storage/tags/" + tagsList.getSelectedValue()));

                    ArrayList<File> tagedImage = new ArrayList<>();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        File image = new File("storage/images/" + line);
                        tagedImage.add(image);
                    }

                    tagedImage.sort((f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));

                    for (int i = 0; i < tagedImage.size(); i++) {
                        File image = tagedImage.get(i);

                        contentPanel.add(new ImageLabel(image, contentPanel, mainFrame));
                    }

                    contentPanel.revalidate();
                    contentPanel.repaint();
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();                                                                                  // It's OK
                } catch (IOException e) {
                    e.printStackTrace();
                    int confirmDelete = JOptionPane.showConfirmDialog(null,
                            Localization.getMessageTagEmptyOrDamaged(),
                            Localization.getMessageTitleTagIsEmpty(),
                            JOptionPane.YES_NO_OPTION);
                    if (confirmDelete == JOptionPane.YES_OPTION) {
                        new File("storage/tags/" + tagsList.getSelectedValue()).delete();
                    }
                }
            });
            mainFrame.revalidate();
        }
    }
}

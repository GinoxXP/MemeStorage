import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TagsWindow {
    private MainFrame mainFrame;

    public TagsWindow(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        mainFrame.getMainPanel().removeAll();
        mainFrame.getMainPanel().setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new GridLayout(0, 5));

        String[] tagsFiles = getTags();
        if (tagsFiles.length > 0) {
            JList<String> tagsList = new JList<>();
            fillTagList(tagsList, tagsFiles);
            tagsList.addListSelectionListener(listSelectionEvent ->
                setTagsListAction(contentPanel, tagsList)
            );
            tagsList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON2){
                        System.out.println(tagsList.getModel().getElementAt(tagsList.getSelectedIndex()));
                    }
                }
            });

            JScrollPane tagsScrollPane = new JScrollPane(tagsList);
            tagsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            tagsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            tagsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
            mainFrame.getMainPanel().add(tagsScrollPane, BorderLayout.WEST);

            JScrollPane contentScrollPane = new JScrollPane(contentPanel);
            contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);
            mainFrame.getMainPanel().add(contentScrollPane, BorderLayout.CENTER);

            JTextField searchField = new JTextField();
            searchField.addCaretListener(caretEvent -> {
                ArrayList<String> searchedTags = new ArrayList<>();
                searchTags(tagsFiles, searchedTags, searchField.getText());

                tagsList.setListData(searchedTags.toArray(String[]::new));

                mainFrame.revalidate();
                mainFrame.repaint();
            });
            mainFrame.getMainPanel().add(searchField, BorderLayout.NORTH);


            mainFrame.revalidate();
        }
    }

    private String[] getTags(){
        File[] filesArr = new File("storage/tags/").listFiles();

        Arrays.sort(filesArr, (f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));

        String[] tagsFiles = new String[filesArr.length];
        for (int i = 0; i < tagsFiles.length; i++)
            tagsFiles[i] = filesArr[i].getName();


        return tagsFiles;
    }

    private void fillTagList(JList<String> tagsList, String[] tagsFile){
        ArrayList<String> allTags = new ArrayList<>();
        for (int i = 0; i < tagsFile.length; i++)
            allTags.add(tagsFile[i]);
        tagsList.setListData(allTags.toArray(String[]::new));
    }

    private void setTagsListAction(JPanel contentPanel, JList<String> tagsList) {
        contentPanel.removeAll();
        contentPanel.revalidate();

        try {
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

        } catch (FileNotFoundException e) {
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
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void searchTags( String[] tagsFiles, ArrayList<String> searchedTags, String search){
        for (int i = 0; i < tagsFiles.length; i++)
            if (tagsFiles[i].contains(search))
                searchedTags.add(tagsFiles[i]);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class TagsFrame extends JFrame {
    private File image;
    private Frame frame;

    TagsFrame(File image){
        setTitle("Tags");
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.image = image;
        this.frame = this;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0,1));

        JLabel tagsLabel = new JLabel("Tags");

        JTextArea tags = new JTextArea();
        tags.setFont(new Font(" ", Font.PLAIN, 25));

        String[] tagsArr = getThisImageTags();
        String thisImageTags = "";
        for(int i = 0; i < tagsArr.length; i++)
            thisImageTags += tagsArr[i] + ((i!=tagsArr.length-1)?"\n":"");
        tags.setText(thisImageTags);

        JButton acceptButton = new JButton("Accept");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                acceptTags(tags.getText().split("\n"));
                frame.dispose();
            }
        });

        mainPanel.add(tagsLabel);
        mainPanel.add(new JScrollPane(tags));
        mainPanel.add(acceptButton);

        setContentPane(mainPanel);
    }

    private void acceptTags(String[] tags){
        String[] allTags = getAllTags();
        for(int i = 0; i < tags.length; i++) {
            boolean isNew = true;
            for (int j = 0; j < allTags.length; j++) {
                if(tags[i].equals(allTags[j]))
                    isNew = false;
            }
            if(isNew)
                addNewTag(tags[i]);
        }

        String[] thisImageTags = getThisImageTags();
        for(int i = 0; i < tags.length; i++) {
            boolean isRemove = true;
            for (int j = 0; j < thisImageTags.length; j++) {
                if(tags[i].equals(thisImageTags[j]))
                    isRemove = false;
            }
            if(isRemove)
                removeTag(tags[i]);
        }
    }

    private void removeTag(String tag){
        File file = new File("storage/tags/" + tag);
        ArrayList<String> images = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(true){
                try {
                    String line = reader.readLine();
                    if(line == null)
                        break;

                    if(!line.equals(tag))
                        images.add(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileWriter writer = new FileWriter(file, false);
                for(int i = 0; i < images.size(); i++){
                    writer.write(images.get(i));
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void addNewTag(String tag){
        File file = new File("storage/tags/" + tag);
        if(!file.exists()){
            try {
                file.createNewFile();

                FileWriter writer = new FileWriter(file, true);
                writer.write(image.getName() + "\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] getAllTags(){
        ArrayList<String> tagsArrayList = new ArrayList<>();

        File[] fileArr = new File("storage/tags").listFiles();
        for(File file : fileArr)
            tagsArrayList.add(file.getName());

        String[] tags = {};
        tags= tagsArrayList.toArray(tags);
        return tags;
    }

    private String[] getThisImageTags(){
        ArrayList<String> tagsArrayList = new ArrayList<>();

        File[] fileArr = new File("storage/tags").listFiles();
        for(File file : fileArr){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));

                while(true){
                    String line = reader.readLine();
                    if(line == null)
                        break;
                    if(line.equals(image.getName()))
                        tagsArrayList.add(file.getName());
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[] tags = {};
        tags= tagsArrayList.toArray(tags);
        return tags;
    }
}

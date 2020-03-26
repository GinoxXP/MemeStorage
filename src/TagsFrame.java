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
        String[] thisImageTags = getThisImageTags();

        for(String tag : thisImageTags){
            removeTag(tag);
        }
        for(String tag : tags){
            addTag(tag);
        }

        checkEmptyTags();
    }

    private void removeTag(String tag){
        File sourceFile = new File("storage/tags/" + tag);
        File newFile = new File("storage/tags/" + "." + tag);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(image.getName())) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            reader.close();
            writer.close();
            sourceFile.delete();
            newFile.renameTo(sourceFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addTag(String tag){
        File file = new File("storage/tags/" + tag);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(image.getName() + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkEmptyTags(){
        File[] fileArr = new File("storage/tags").listFiles();
        for(int i = 0; i < fileArr.length; i++){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileArr[i]));

                boolean isEmpty = false;
                if(reader.readLine()==null)
                    isEmpty = true;

                reader.close();

                if(isEmpty)
                    fileArr[i].delete();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
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

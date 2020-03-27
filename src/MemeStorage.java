import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class MemeStorage extends JFrame {
    final String programName = "MemeStorage";
    JPanel mainPanel = null;
    JScrollPane scrollPane = null;
    String defaultImagesFormat = "png";
    final String VERSION = "0.4";

    public MemeStorage() {
        setTitle(programName);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        try {
            setIconImage(ImageIO.read(new File("files/icon32.png")));
        } catch (IOException e) {
            System.err.println("Icon can't load");
            e.printStackTrace();
        }

        mainPanel = new JPanel();

        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);

        setSize(700,450);
        setVisible(true);

        setTrayIcon();

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu showMenu = new JMenu("Show");
        menuBar.add(showMenu);

        JMenuItem showAllItem = new JMenuItem("Show all");
        showAllItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showAllImages();
            }
        });
        showMenu.add(showAllItem);

        JMenuItem showTagImageItem = new JMenuItem("Show tag image");
        showTagImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showTagImages();
            }
        });
        showMenu.add(showTagImageItem);

        JMenuItem addImageFromClipboard = new JMenuItem("Add image from clipboard");
        addImageFromClipboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Transferable transferableImg = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

                try {
                    Image image = (Image) transferableImg.getTransferData(DataFlavor.imageFlavor);
                    ImageIO.write((BufferedImage)image, defaultImagesFormat, new File("storage/images/" + generateName()));
                    showAllImages();
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        menuBar.add(addImageFromClipboard);

        JMenu propertiesMenu = new JMenu("Properties");
        menuBar.add(propertiesMenu);

        JMenuItem settings = new JMenuItem("Settings");
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showSettings();
            }
        });
        propertiesMenu.add(settings);

        JMenuItem info = new JMenuItem("Info");
        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showInfo();
            }
        });
        propertiesMenu.add(info);

        checkFiles();

        showAllImages();


        revalidate();
        repaint();
    }

    void showSettings(){
        JFrame settingsFrame = new JFrame("Settings");
        settingsFrame.setVisible(true);
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(400,200);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        settingsFrame.add(mainPanel);

        JLabel formatLabel = new JLabel("Default save images format");
        mainPanel.add(formatLabel);

        String imageFormats[] = {"png", "jpg", "memes"};
        JComboBox changeImageFormats = new JComboBox(imageFormats);

        for(int i = 0; i < imageFormats.length; i++){
            if(imageFormats[i].equals(defaultImagesFormat))
                changeImageFormats.setSelectedItem(imageFormats[i]);
        }

        changeImageFormats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                defaultImagesFormat = (String) changeImageFormats.getSelectedItem();
            }
        });
        mainPanel.add(changeImageFormats);

        settingsFrame.revalidate();
    }

    void showInfo(){
        JFrame infoFrame = new JFrame("Info");
        infoFrame.setVisible(true);
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setSize(100,200);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0,1));
        infoFrame.add(mainPanel);

        JLabel nameLabel = new JLabel("MemeStorage");
        mainPanel.add(nameLabel);

        JLabel authorLabel = new JLabel("GinoxXP, 2020");
        mainPanel.add(authorLabel);

        JLabel linkLabel = new JLabel("<html><u><font color=BLUE>Open on GitHub");
        linkLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/GinoxXP/MemeStorage"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        mainPanel.add(linkLabel);

        JLabel versionLabel = new JLabel("V. " + VERSION);
        mainPanel.add(versionLabel);

        infoFrame.revalidate();
    }

    void showAllImages(){
        mainPanel.removeAll();

        JPanel contentPanel = new JPanel(new GridLayout(0,5));
        File[] files = new File("storage/images").listFiles();

        Arrays.sort(files, (f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));

        for(int i = 0; i < files.length; i++)
        {
            try {
                ImageLabel imageLabel = new ImageLabel(files[i], contentPanel);
                mainPanel.add(contentPanel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        revalidate();
        repaint();
    }

    void showTagImages(){
        mainPanel.removeAll();

        mainPanel.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new GridLayout(0,5));
        File[] filesArr = new File("storage/tags/").listFiles();

        Arrays.sort(filesArr, (f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));

        String[] tagsFile = new String[filesArr.length];
        for(int i = 0; i < tagsFile.length; i++)
            tagsFile[i] = filesArr[i].getName();

        if(tagsFile.length > 0){
            JList<String> tagsList = new JList<>(tagsFile);

            JScrollPane tagsScrollPane = new JScrollPane(tagsList);
            tagsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            tagsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            tagsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

            JScrollPane contentScrollPane = new JScrollPane(contentPanel);
            contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);

            mainPanel.add(tagsScrollPane, BorderLayout.WEST);
            mainPanel.add(contentScrollPane, BorderLayout.CENTER);

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

                    for(int i = 0; i < tagedImage.size(); i++) {
                        File image = tagedImage.get(i);
                        ImageLabel imageLabel = new ImageLabel(image, contentPanel);
                    }

                    contentPanel.revalidate();
                    contentPanel.repaint();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    int confirmDelete = JOptionPane.showConfirmDialog(null, "This tag is empty or damaged. Delete this?", "Tag is empty", JOptionPane.YES_NO_OPTION);
                    if(confirmDelete == JOptionPane.YES_OPTION){
                        new File("storage/tags/" + tagsList.getSelectedValue()).delete();
                    }
                }
            });
            revalidate();
        }
    }

    String generateName(){
        String name;
        do{
            name = "img-";

            String[] letters = {"Q", "q", "W", "w", "E", "e", "R", "r", "T", "t", "Y", "y", "U", "u", "I", "i", "O", "o", "P", "p",
                    "A", "a", "S", "s", "D", "d", "F", "f", "G", "g", "H", "h", "J", "j", "K", "k", "L", "l",
                    "Z", "z", "X", "x", "C", "c", "V", "v", "B", "b", "N", "n", "M", "m",
                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

            for(int i = 0 ; i < 16; i++){
                name += letters[new Random().nextInt(letters.length)];
            }

            name += "." + defaultImagesFormat;
        }while(new File("storage/images/" + name).exists());


        return name;
    }

    void checkFiles() {
        if(!new File("storage/").exists())
            new File("storage/").mkdir();

        if(!new File("storage/images/").exists())
            new File("storage/images/").mkdir();

        if(!new File("storage/tags/").exists())
            new File("storage/tags/").mkdir();
    }

    void setTrayIcon(){
        if(!SystemTray.isSupported())
            return;


        PopupMenu trayMenu = new PopupMenu();

        MenuItem menuItemClose = new MenuItem("Close");
        menuItemClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        trayMenu.add(menuItemClose);


        Image icon = null;
        try {
            icon = ImageIO.read(new File("files/icon32.png"));
        } catch (IOException e) {
            System.err.println("Icon can't load");
            e.printStackTrace();
        }
        TrayIcon trayIcon = new TrayIcon(icon, programName, trayMenu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(true);
            }
        });

        SystemTray systemTray = SystemTray.getSystemTray();
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        MemeStorage memeStorage = new MemeStorage();
    }
}

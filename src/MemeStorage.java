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
import java.util.Random;

public class MemeStorage extends JFrame {
    final String programName = "MemeStorage";
    JPanel mainPanel = null;
    JScrollPane scrollPane = null;
    String defaultImagesFormat = "png";
    final String VERSION = "0.3";

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
        mainPanel.setLayout(new GridLayout(0,5));

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

//        JMenuItem search = new JMenuItem("Search");
//        search.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                showSearchTagImages();
//            }
//        });
//        menuBar.add(search);

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

        try {
            checkFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        JLabel linkLabel = new JLabel("Open on GitHub");
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
        File[] filesArr = new File("storage/images").listFiles();

        for(int i = 0; i < filesArr.length; i++)
        {
            try {
                BufferedImage img = ImageIO.read(filesArr[i]);
                img = scale(img, 100);
                ImageIcon icon = new ImageIcon(img);

                JLabel imageLabel = new JLabel();
                int finalI = i;
                File imageFile = filesArr[i];
                imageLabel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        if(mouseEvent.getButton() == MouseEvent.BUTTON1){
                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                                    new ImageTransferable(imageFile), null);
                        }
                        if(mouseEvent.getButton() == MouseEvent.BUTTON3){
                            JPopupMenu imageSettingsMenu = new JPopupMenu();

                            JMenuItem deleteImage = new JMenuItem("Delete");
                            deleteImage.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent actionEvent) {
                                    imageFile.delete();
                                    JOptionPane.showMessageDialog(imageLabel, "Image successful delete", "Delete image", JOptionPane.INFORMATION_MESSAGE);
                                    showAllImages();
                                }
                            });
                            imageSettingsMenu.add(deleteImage);

                            JMenuItem tagsImage = new JMenuItem("Tags");
                            tagsImage.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent actionEvent) {
                                    TagsFrame tagsFrame = new TagsFrame(imageFile);
                                    tagsFrame.setBounds(imageLabel.getBounds().x, imageLabel.getBounds().y, 400, 400);
                                }
                            });
                            imageSettingsMenu.add(tagsImage);

                            JMenuItem infoImage = new JMenuItem("Info");
                            infoImage.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent actionEvent) {
                                    JOptionPane.showMessageDialog(imageLabel,  "Name " + imageFile.getName() + "\n" +
                                                                                        "Path " + imageFile.getAbsolutePath() + "\n" +
                                                                                        "Size " + (float)(imageFile.length()/1024) + " KB");
                                }
                            });
                            imageSettingsMenu.add(infoImage);

                            imageSettingsMenu.show(imageLabel,0, 50);
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
                imageLabel.setIcon(icon);
                mainPanel.add(imageLabel);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        revalidate();
        repaint();
    }

    void showTagImages(){
        mainPanel.removeAll();

        String[] tagsFile = new File("storage/tags/").list();
        if(tagsFile.length > 0){
            JList<String> tagsList = new JList<>(tagsFile);
            tagsList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent listSelectionEvent) {
                    try {
                        mainPanel.removeAll();
                        mainPanel.add(tagsList);
                        mainPanel.revalidate();

                        BufferedReader reader = new BufferedReader(new FileReader("storage/tags/" + tagsList.getSelectedValue()));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            File image = new File("storage/images/" + line);

                            BufferedImage img = ImageIO.read(image);
                            img = scale(img, 100);
                            ImageIcon icon = new ImageIcon(img);

                            JLabel imageLabel = new JLabel();
                            imageLabel.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent mouseEvent) {
                                    if(mouseEvent.getButton() == MouseEvent.BUTTON1){
                                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                                                new ImageTransferable(image), null);
                                    }
                                    if(mouseEvent.getButton() == MouseEvent.BUTTON3){
                                        JPopupMenu imageSettingsMenu = new JPopupMenu();

                                        JMenuItem deleteImage = new JMenuItem("Delete");
                                        deleteImage.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent actionEvent) {
                                                image.delete();
                                                JOptionPane.showMessageDialog(imageLabel, "Image successful delete", "Delete image", JOptionPane.INFORMATION_MESSAGE);
                                                showAllImages();
                                            }
                                        });
                                        imageSettingsMenu.add(deleteImage);

                                        JMenuItem tagsImage = new JMenuItem("Tags");
                                        tagsImage.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent actionEvent) {
                                                TagsFrame tagsFrame = new TagsFrame(image);
                                                tagsFrame.setBounds(imageLabel.getBounds().x, imageLabel.getBounds().y, 400, 400);
                                            }
                                        });
                                        imageSettingsMenu.add(tagsImage);

                                        JMenuItem infoImage = new JMenuItem("Info");
                                        infoImage.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent actionEvent) {
                                                JOptionPane.showMessageDialog(imageLabel,  "Name " + image.getName() + "\n" +
                                                        "Path " + image.getAbsolutePath() + "\n" +
                                                        "Size " + (float)(image.length()/1024) + " KB");
                                            }
                                        });
                                        imageSettingsMenu.add(infoImage);

                                        imageSettingsMenu.show(imageLabel,0, 50);
                                    }
                                }

                                @Override
                                public void mousePressed(MouseEvent mouseEvent) {}

                                @Override
                                public void mouseReleased(MouseEvent mouseEvent) {}

                                @Override
                                public void mouseEntered(MouseEvent mouseEvent) {}

                                @Override
                                public void mouseExited(MouseEvent mouseEvent) {}
                            });
                            imageLabel.setIcon(icon);
                            mainPanel.add(imageLabel);
                        }
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            mainPanel.add(new JScrollPane(tagsList));
            revalidate();
        }
    }

    void showSearchTagImages(){
        mainPanel.removeAll();

        JTextArea searchArea = new JTextArea();

        revalidate();
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

    BufferedImage scale(BufferedImage img, int minSize) {
        int targetWidth = 0;
        int targetHeight = 0;

        if(img.getHeight() <= minSize && img.getWidth() <= minSize){
            return img;
        }else{
            if(img.getWidth() > img.getHeight()){
                float scaleKoef = img.getWidth()/minSize;

                targetWidth = (int) (img.getWidth()/scaleKoef);
                targetHeight = (int) (img.getHeight()/scaleKoef);
            }else{
                float scaleKoef = img.getHeight()/minSize;

                targetWidth = (int) (img.getWidth()/scaleKoef);
                targetHeight = (int) (img.getHeight()/scaleKoef);
            }

        }


        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;

        int w = img.getWidth();
        int h = img.getHeight();

        int prevW = w;
        int prevH = h;

        do {
            if (w > targetWidth) {
                w /= 2;
                w = (w < targetWidth) ? targetWidth : w;
            }

            if (h > targetHeight) {
                h /= 2;
                h = (h < targetHeight) ? targetHeight : h;
            }

            if (scratchImage == null) {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        if (g2 != null) {
            g2.dispose();
        }

        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }


        return ret;

    }

    void checkFiles() throws IOException {
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

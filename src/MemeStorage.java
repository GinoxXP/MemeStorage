import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.desktop.AppEvent;
import java.awt.desktop.SystemEventListener;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class MemeStorage extends JFrame {
    Localization localization;
    JPanel mainPanel = null;
    JScrollPane scrollPane = null;
    Settings settings = null;
    final String VERSION = "0.9";

    public MemeStorage() {
        settings = new Settings();
        localization = new Localization(settings.localizations);
        buildUI();

        revalidate();
        repaint();
    }

    void buildUI(){
        setTitle(localization.getTitle());

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        try {
            setIconImage(ImageIO.read(new File("files/icon32.png")));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                                                        localization.getMessageIconCantLoad(),
                                                        localization.getMessageIconCantLoad(),
                                                        JOptionPane.ERROR_MESSAGE);
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_V && e.isControlDown())
                    addImageFromClipboard();

                if(e.getKeyCode() == KeyEvent.VK_X && e.isControlDown())
                    setVisible(false);

                if(e.getKeyCode() == KeyEvent.VK_Q && e.isControlDown())
                    System.exit(0);
            }
        });
        mainPanel = new JPanel();

        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);

        setSize(750,450);
        setVisible(true);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu showMenu = new JMenu(localization.getMenuShow());
        menuBar.add(showMenu);

        JMenuItem showAllItem = new JMenuItem(localization.getButtonShowAll());
        showAllItem.addActionListener(actionEvent -> showAllImages());
        showMenu.add(showAllItem);

        JMenuItem showTagImageItem = new JMenuItem(localization.getButtonShowTagImages());
        showTagImageItem.addActionListener(actionEvent -> showTagImages());
        showMenu.add(showTagImageItem);

        JMenuItem addImageFromClipboard = new JMenuItem(localization.getButtonAddImage());
        addImageFromClipboard.addActionListener(actionEvent -> addImageFromClipboard());
        menuBar.add(addImageFromClipboard);

        JMenu propertiesMenu = new JMenu(localization.getMenuProperties());
        menuBar.add(propertiesMenu);

        JMenuItem settings = new JMenuItem(localization.getButtonSettings());
        settings.addActionListener(actionEvent -> showSettings());
        propertiesMenu.add(settings);

        JMenuItem info = new JMenuItem(localization.getButtonInfo());
        info.addActionListener(actionEvent -> showInfo());
        propertiesMenu.add(info);

        for(int i = 0; i < SystemTray.getSystemTray().getTrayIcons().length; i++)
            SystemTray.getSystemTray().remove(SystemTray.getSystemTray().getTrayIcons()[i]);
        setTrayIcon();

        checkFiles();

        showAllImages();
    }

    void addImageFromClipboard(){
        Transferable transferableImg = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        try {
            Image image = (Image) transferableImg.getTransferData(DataFlavor.imageFlavor);
            ImageIO.write((BufferedImage)image,
                    settings.defaultSaveFormat,
                    new File("storage/images/" + generateName()));
            showAllImages();
        } catch (UnsupportedFlavorException e) {
            JOptionPane.showMessageDialog(null,
                    localization.getMessageClipboardEmpty(),
                    localization.getMessageTitleClipboardEmpty(),
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void showSettings(){
        JFrame settingsFrame = new JFrame(localization.getButtonSettings());
        settingsFrame.setVisible(true);
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(400,200);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0,2,100,100));
        settingsFrame.add(mainPanel);

        JLabel formatLabel = new JLabel(localization.getLabelDefaultFormat());
        mainPanel.add(formatLabel);

        String imageFormats[] = {"png", "jpg"};
        JComboBox changeImageFormats = new JComboBox(imageFormats);

        for(int i = 0; i < imageFormats.length; i++){
            if(imageFormats[i].equals(settings.defaultSaveFormat))
                changeImageFormats.setSelectedItem(imageFormats[i]);
        }

        changeImageFormats.addActionListener(actionEvent -> {
            settings.defaultSaveFormat = (String) changeImageFormats.getSelectedItem();
            settings.setSettings();
        });
        mainPanel.add(changeImageFormats);

        JLabel localizationLabel = new JLabel(localization.getLabelLocalization());
        mainPanel.add(localizationLabel);

        File[] localizationFiles = new File("localizations/").listFiles(file -> file.getName().contains(".xml"));
        String[] localizations = new String[localizationFiles.length];
        for(int i = 0; i < localizations.length; i++){
            localizations[i] = new Localization("localizations/" + localizationFiles[i].getName()).getLocalizationName();
        }
        JComboBox changeLocalization = new JComboBox(localizations);


        for(int i = 0; i < localizationFiles.length; i++){
            if(localizations[i].equals(localization.getLocalizationName())){
                changeLocalization.setSelectedItem(localizations[i]);
            }
        }
        changeLocalization.addActionListener(actionEvent -> {
            localization = new Localization(localizationFiles[changeLocalization.getSelectedIndex()].getAbsolutePath());
            settings.localizations = "localizations/" + localizationFiles[changeLocalization.getSelectedIndex()].getName();
            settings.setSettings();
            settingsFrame.dispose();
            buildUI();
        });
        mainPanel.add(changeLocalization);

        settingsFrame.revalidate();
    }

    void showInfo(){
        JFrame infoFrame = new JFrame(localization.getButtonInfo());
        infoFrame.setVisible(true);
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setSize(100,200);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0,1));
        infoFrame.add(mainPanel);

        JLabel nameLabel = new JLabel(localization.getTitle());
        mainPanel.add(nameLabel);

        JLabel authorLabel = new JLabel("GinoxXP, 2020");
        mainPanel.add(authorLabel);

        JLabel linkLabel = new JLabel("<html><u><font color=BLUE>" + localization.getLinkGitHub());
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
            for(int i = 0; i < tagsFile.length; i++)                                                                    /// Feeling list
            allTags.add(tagsFile[i]);                                                                                   ///
                                                                                                                        ///
            tagsList.setListData(allTags.toArray(String[]::new));                                                       ///


            JTextField searchField = new JTextField();
            searchField.addCaretListener(caretEvent -> {
                ArrayList<String> searchedTags = new ArrayList<>();
                for(int i = 0; i < tagsFile.length; i++)
                    if(tagsFile[i].contains(searchField.getText()))
                        searchedTags.add(tagsFile[i]);

                tagsList.setListData(searchedTags.toArray(String[]::new));
                
                revalidate();
                repaint();
            });

            mainPanel.add(searchField, BorderLayout.NORTH);

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
                //e.printStackTrace();                                                                                  // It's OK
                } catch (IOException e) {
                    e.printStackTrace();
                    int confirmDelete = JOptionPane.showConfirmDialog(null,
                                                                    localization.getMessageTagEmptyOrDamaged(),
                                                                    localization.getMessageTitleTagIsEmpty(),
                                                                    JOptionPane.YES_NO_OPTION);
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

            String[] letters = {
                    "Q", "q", "W", "w", "E", "e", "R", "r", "T", "t", "Y", "y", "U", "u", "I", "i", "O", "o", "P", "p",
                    "A", "a", "S", "s", "D", "d", "F", "f", "G", "g", "H", "h", "J", "j", "K", "k", "L", "l",
                    "Z", "z", "X", "x", "C", "c", "V", "v", "B", "b", "N", "n", "M", "m",
                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
                    };

            for(int i = 0 ; i < 16; i++){
                name += letters[new Random().nextInt(letters.length)];
            }

            name += "." + settings.defaultSaveFormat;
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

        MenuItem menuItemClose = new MenuItem(localization.getTrayMenuButtonClose());
        menuItemClose.addActionListener(actionEvent -> System.exit(0));
        trayMenu.add(menuItemClose);


        Image icon = null;
        try {
            icon = ImageIO.read(new File("files/icon32.png"));
        } catch (IOException e) {
            System.err.println(localization.getMessageIconCantLoad());
            e.printStackTrace();
        }
        TrayIcon trayIcon = new TrayIcon(icon, localization.getTitle(), trayMenu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(actionEvent -> setVisible(true));

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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class MainFrame extends JFrame {
    private JPanel mainPanel = null;
    final String VERSION = "0.9.5";

    public MainFrame(){
        setFrameSettings();
        clearSystemTray();
        buildMainFrame();

    }

    private void setFrameSettings(){
        setTitle(Localization.getTitle());

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        try {
            setIconImage(ImageIO.read(new File("files/icon32.png")));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    Localization.getMessageIconCantLoad(),
                    Localization.getMessageIconCantLoad(),
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

        setSize(750,450);
        setVisible(true);
    }

    private void clearSystemTray(){
        for(int i = 0; i < SystemTray.getSystemTray().getTrayIcons().length; i++)
            SystemTray.getSystemTray().remove(SystemTray.getSystemTray().getTrayIcons()[i]);
    }

    private void buildMainFrame(){
        mainPanel = new JPanel();

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

            JMenu showMenu = new JMenu(Localization.getMenuShow());
            menuBar.add(showMenu);

                JMenuItem showAllItem = new JMenuItem(Localization.getButtonShowAll());
                showAllItem.addActionListener(actionEvent -> new MemeWindow(this));
                showMenu.add(showAllItem);

                JMenuItem showTagImageItem = new JMenuItem(Localization.getButtonShowTagImages());
                showTagImageItem.addActionListener(actionEvent -> new TagsWindow(this));
                showMenu.add(showTagImageItem);

            JMenuItem addImageFromClipboard = new JMenuItem(Localization.getButtonAddImage());
            addImageFromClipboard.addActionListener(actionEvent -> addImageFromClipboard());
            menuBar.add(addImageFromClipboard);

            JMenu propertiesMenu = new JMenu(Localization.getMenuProperties());
            menuBar.add(propertiesMenu);

                JMenuItem settings = new JMenuItem(Localization.getButtonSettings());
                settings.addActionListener(actionEvent -> showSettings());
                propertiesMenu.add(settings);

                JMenuItem info = new JMenuItem(Localization.getButtonInfo());
                info.addActionListener(actionEvent -> showInfo());
                propertiesMenu.add(info);

        new MemeWindow(this);
    }

    private void addImageFromClipboard(){
        Transferable transferableImg = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        try {
            Image image = (Image) transferableImg.getTransferData(DataFlavor.imageFlavor);
            ImageIO.write((BufferedImage)image,
                    Settings.defaultSaveFormat,
                    new File("storage/images/" + UUID.randomUUID() + "." + Settings.defaultSaveFormat));
            new MemeWindow(this);
        } catch (UnsupportedFlavorException e) {
            JOptionPane.showMessageDialog(null,
                    Localization.getMessageClipboardEmpty(),
                    Localization.getMessageTitleClipboardEmpty(),
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSettings(){
        JFrame settingsFrame = new JFrame(Localization.getButtonSettings());
        settingsFrame.setVisible(true);
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(400,200);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0,2,100,100));
        settingsFrame.add(mainPanel);

        JLabel formatLabel = new JLabel(Localization.getLabelDefaultFormat());
        mainPanel.add(formatLabel);

        String imageFormats[] = {"png", "jpg"};
        JComboBox changeImageFormats = new JComboBox(imageFormats);

        for(int i = 0; i < imageFormats.length; i++){
            if(imageFormats[i].equals(Settings.defaultSaveFormat))
                changeImageFormats.setSelectedItem(imageFormats[i]);
        }

        changeImageFormats.addActionListener(actionEvent -> {
            Settings.defaultSaveFormat = (String) changeImageFormats.getSelectedItem();
            Settings.set();
        });
        mainPanel.add(changeImageFormats);

        JLabel localizationLabel = new JLabel(Localization.getLabelLocalization());
        mainPanel.add(localizationLabel);

        File[] localizationFiles = new File("localizations/").listFiles(file -> file.getName().contains(".xml"));
        String[] localizations = new String[localizationFiles.length];
        for(int i = 0; i < localizations.length; i++){
            Localization.load("localizations/" + localizationFiles[i].getName());
            localizations[i] = Localization.getLocalizationName();
        }
        JComboBox changeLocalization = new JComboBox(localizations);


        for(int i = 0; i < localizationFiles.length; i++){
            if(localizations[i].equals(Localization.getLocalizationName())){
                changeLocalization.setSelectedItem(localizations[i]);
            }
        }
        changeLocalization.addActionListener(actionEvent -> {
            Localization.load(localizationFiles[changeLocalization.getSelectedIndex()].getAbsolutePath());
            Settings.localizations = "localizations/" + localizationFiles[changeLocalization.getSelectedIndex()].getName();
            Settings.set();
            settingsFrame.dispose();
            new MemeWindow(this);
        });
        mainPanel.add(changeLocalization);

        settingsFrame.revalidate();
    }

    private void showInfo(){
        JFrame infoFrame = new JFrame(Localization.getButtonInfo());
        infoFrame.setVisible(true);
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setSize(100,200);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0,1));
        infoFrame.add(mainPanel);

        JLabel nameLabel = new JLabel(Localization.getTitle());
        mainPanel.add(nameLabel);

        JLabel authorLabel = new JLabel("GinoxXP, 2020");
        mainPanel.add(authorLabel);

        JLabel linkLabel = new JLabel("<html><u><font color=BLUE>" + Localization.getLinkGitHub());
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

    public JPanel getMainPanel() {
        return mainPanel;
    }
}

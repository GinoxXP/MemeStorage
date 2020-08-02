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

public class MemeStorage {
    MainFrame mainFrame;
    public MemeStorage() {
        checkFiles();

        Settings.load();
        Localization.load(Settings.localizations);

        mainFrame = new MainFrame();
        setTrayIcon();
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

        MenuItem menuItemClose = new MenuItem(Localization.getTrayMenuButtonClose());
        menuItemClose.addActionListener(actionEvent -> System.exit(0));
        trayMenu.add(menuItemClose);


        Image icon = null;
        try {
            icon = ImageIO.read(new File("files/icon32.png"));
        } catch (IOException e) {
            System.err.println(Localization.getMessageIconCantLoad());
            e.printStackTrace();
        }
        TrayIcon trayIcon = new TrayIcon(icon, Localization.getTitle(), trayMenu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(actionEvent -> mainFrame.setVisible(true));

        SystemTray systemTray = SystemTray.getSystemTray();
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(MemeStorage::new);
    }
}

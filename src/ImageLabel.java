import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLabel extends JLabel {
    public ImageLabel(File image, JPanel contentPanel) throws IOException {
        BufferedImage img = ImageIO.read(image);
        img = scale(img, 90);
        ImageIcon icon = new ImageIcon(img);

        JLabel imageLabel = this;
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseEvent.BUTTON1){
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                            new ImageTransferable(image), null);
                }
                if(mouseEvent.getButton() == MouseEvent.BUTTON3){
                    JPopupMenu imageSettingsMenu = new JPopupMenu();

                    JMenuItem deleteImage = new JMenuItem("Delete");
                    deleteImage.addActionListener(actionEvent -> {
                        image.delete();
                        JOptionPane.showMessageDialog(imageLabel, "Image successful delete", "Delete image", JOptionPane.INFORMATION_MESSAGE);
                    });
                    imageSettingsMenu.add(deleteImage);

                    JMenuItem tagsImage = new JMenuItem("Tags");
                    tagsImage.addActionListener(actionEvent -> {
                        TagsFrame tagsFrame = new TagsFrame(image);
                        tagsFrame.setBounds(imageLabel.getBounds().x, imageLabel.getBounds().y, 400, 400);
                    });
                    imageSettingsMenu.add(tagsImage);

                    JMenuItem infoImage = new JMenuItem("Info");
                    infoImage.addActionListener(actionEvent -> JOptionPane.showMessageDialog(imageLabel,  "Name " + image.getName() + "\n" +
                                                                                                            "Path " + image.getAbsolutePath() + "\n" +
                                                                                                            "Size " + (image.length()/1024) + " KB"));
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
        setIcon(icon);
        contentPanel.add(this);
    }

    private BufferedImage scale(BufferedImage img, int minSize) {
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
}

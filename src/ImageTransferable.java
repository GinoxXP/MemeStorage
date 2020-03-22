import javax.imageio.ImageIO;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

class ImageTransferable implements Transferable {
    private File img;

    public ImageTransferable(File img){
        this.img = img;
    }
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { DataFlavor.imageFlavor };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return dataFlavor == DataFlavor.imageFlavor;
    }

    @Override
    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(dataFlavor))
        {
            return ImageIO.read(img);
        }
        else
        {
            throw new UnsupportedFlavorException(dataFlavor);
        }
    }
}

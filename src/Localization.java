import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Localization {
    private static String LocalizationName;

    private static String Title;
    private static String MenuShow;
    private static String ButtonShowAll;
    private static String ButtonShowTagImages;
    private static String ButtonAddImage;
    private static String MenuProperties;
    private static String ButtonSettings;
    private static String ButtonInfo;
    private static String LabelDefaultFormat;
    private static String LabelLocalization;
    private static String LinkGitHub;
    private static String LocalizationAuthor;

    private static String MessageIconCantLoad;
    private static String MessageTagEmptyOrDamaged;
    private static String MessageTitleTagIsEmpty;
    private static String MessageClipboardEmpty;
    private static String MessageTitleClipboardEmpty;

    private static String TrayMenuButtonClose;

    public static void load(String localization){
        if(!new File(localization).exists())
            localization = "localizations/eng.xml";

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(localization);

            Node root = document.getDocumentElement();
            NodeList list = root.getChildNodes();
            for(int i = 0; i < list.getLength(); i++){
                Node element = list.item(i);
                if (element.getNodeType() == Node.ELEMENT_NODE){
                    String content = element.getTextContent();
                    switch (element.getNodeName()){
                        case "LocalizationName":LocalizationName = content;break;
                        case "Title":Title = content;break;
                        case "MenuShow":MenuShow = content;break;
                        case "ButtonShowAll":ButtonShowAll = content;break;
                        case "ButtonShowTagImages":ButtonShowTagImages = content;break;
                        case "ButtonAddImage":ButtonAddImage = content;break;
                        case "MenuProperties":MenuProperties = content;break;
                        case "ButtonSettings":ButtonSettings = content;break;
                        case "ButtonInfo":ButtonInfo = content;break;
                        case "LabelDefaultFormat":LabelDefaultFormat = content;break;
                        case "LabelLocalization":LabelLocalization = content;break;
                        case "LinkGitHub":LinkGitHub = content;break;
                        case "LocalizationAuthor":LocalizationAuthor = content;break;

                        case "MessageIconCantLoad":MessageIconCantLoad = content;break;
                        case "MessageTagEmptyOrDamaged":MessageTagEmptyOrDamaged = content;break;
                        case "MessageTitleTagIsEmpty":MessageTitleTagIsEmpty = content;break;
                        case "MessageClipboardEmpty":MessageClipboardEmpty = content;break;
                        case "MessageTitleClipboardEmpty":MessageTitleClipboardEmpty = content;break;

                        case "TrayMenuButtonClose":TrayMenuButtonClose = content;break;
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTitle() {
        return Title;
    }

    public static String getMenuShow() {
        return MenuShow;
    }

    public static String getButtonShowAll() {
        return ButtonShowAll;
    }

    public static String getButtonShowTagImages() {
        return ButtonShowTagImages;
    }

    public static String getButtonAddImage() {
        return ButtonAddImage;
    }

    public static String getMenuProperties() {
        return MenuProperties;
    }

    public static String getButtonSettings() {
        return ButtonSettings;
    }

    public static String getButtonInfo() {
        return ButtonInfo;
    }

    public static String getLabelDefaultFormat() {
        return LabelDefaultFormat;
    }

    public static String getLinkGitHub() {
        return LinkGitHub;
    }

    public static String getLocalizationAuthor() {
        return LocalizationAuthor;
    }

    public static String getMessageIconCantLoad() {
        return MessageIconCantLoad;
    }

    public static String getMessageTagEmptyOrDamaged() {
        return MessageTagEmptyOrDamaged;
    }

    public static String getMessageTitleTagIsEmpty() {
        return MessageTitleTagIsEmpty;
    }

    public static String getTrayMenuButtonClose() {
        return TrayMenuButtonClose;
    }

    public static String getLabelLocalization() {
        return LabelLocalization;
    }

    public static String getLocalizationName() {
        return LocalizationName;
    }

    public static String getMessageClipboardEmpty() {
        return MessageClipboardEmpty;
    }

    public static String getMessageTitleClipboardEmpty() {
        return MessageTitleClipboardEmpty;
    }
}

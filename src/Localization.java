import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Localization {
    private String LocalizationName;

    private String Title;
    private String MenuShow;
    private String ButtonShowAll;
    private String ButtonShowTagImages;
    private String ButtonAddImage;
    private String MenuProperties;
    private String ButtonSettings;
    private String ButtonInfo;
    private String LabelDefaultFormat;
    private String LabelLocalization;
    private String LinkGitHub;
    private String LocalizationAuthor;

    private String MessageIconCantLoad;
    private String MessageTagEmptyOrDamaged;
    private String MessageTitleTagIsEmpty;
    private String MessageClipboardEmpty;
    private String MessageTitleClipboardEmpty;

    private String TrayMenuButtonClose;

    public Localization(String localization){
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

    public String getTitle() {
        return Title;
    }

    public String getMenuShow() {
        return MenuShow;
    }

    public String getButtonShowAll() {
        return ButtonShowAll;
    }

    public String getButtonShowTagImages() {
        return ButtonShowTagImages;
    }

    public String getButtonAddImage() {
        return ButtonAddImage;
    }

    public String getMenuProperties() {
        return MenuProperties;
    }

    public String getButtonSettings() {
        return ButtonSettings;
    }

    public String getButtonInfo() {
        return ButtonInfo;
    }

    public String getLabelDefaultFormat() {
        return LabelDefaultFormat;
    }

    public String getLinkGitHub() {
        return LinkGitHub;
    }

    public String getLocalizationAuthor() {
        return LocalizationAuthor;
    }

    public String getMessageIconCantLoad() {
        return MessageIconCantLoad;
    }

    public String getMessageTagEmptyOrDamaged() {
        return MessageTagEmptyOrDamaged;
    }

    public String getMessageTitleTagIsEmpty() {
        return MessageTitleTagIsEmpty;
    }

    public String getTrayMenuButtonClose() {
        return TrayMenuButtonClose;
    }

    public String getLabelLocalization() {
        return LabelLocalization;
    }

    public String getLocalizationName() {
        return LocalizationName;
    }

    public String getMessageClipboardEmpty() {
        return MessageClipboardEmpty;
    }

    public String getMessageTitleClipboardEmpty() {
        return MessageTitleClipboardEmpty;
    }
}

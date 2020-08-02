import java.io.*;

public class Settings {
    public static String defaultSaveFormat;
    public static String localizations;
    public static File file;

    public static void load(){
        file = new File(".settings");
        try {
            if(!file.exists()){
                file.createNewFile();
                defaultSaveFormat = "png";
                localizations = "eng.xml";

                set();
            }
            else{
                BufferedReader reader = new BufferedReader(new FileReader(file));

                defaultSaveFormat = reader.readLine();
                localizations = reader.readLine();

                reader.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void set(){
        try {
            File fileNewSettings = new File("..settings");

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileNewSettings));
            writer.write(defaultSaveFormat);
            writer.newLine();
            writer.write(localizations);
            writer.newLine();

            writer.flush();
            writer.close();

            file.delete();
            fileNewSettings.renameTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import java.io.*;

public class Settings {
    public String defaultSaveFormat;
    public String localizations;
    File file;

    public Settings(){
        file = new File(".settings");
        try {
            if(!file.exists()){
                file.createNewFile();
                defaultSaveFormat = "png";
                localizations = "eng.xml";

                setSettings();
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

    public void setSettings(){
        try {
            File file = new File("..settings");

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(defaultSaveFormat);
            writer.newLine();
            writer.write(localizations);
            writer.newLine();

            writer.flush();
            writer.close();

            this.file.delete();
            file.renameTo(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

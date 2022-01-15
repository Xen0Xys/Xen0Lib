package fr.xen0xys.xen0lib.utils;

import com.google.common.hash.Hashing;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Scanner;

public abstract class Utils {

    /**
     * Get player ip
     * @param player Bukkit Player Object
     * @return
     */
    public static String getPlayerIP(Player player){
        return player.getAddress().getAddress().toString().replace("/", "");
    }

    /**
     * Get current timestamp in seconds
     * @return
     */
    public static long getCurrentTimestamp(){
        return new Timestamp(System.currentTimeMillis()).getTime() / 1000;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static String encryptString(String string, String additionalEncryptionString){
        string = string + additionalEncryptionString;
        return Hashing.sha256().hashString(string, StandardCharsets.UTF_8).toString();
    }

    /**
     * Get content of file latest.log
     * @return
     */
    public static String getLatestLogContent(){
        try {
            File myObj = new File("logs/latest.log");
            Scanner myReader = new Scanner(myObj);
            StringBuilder content = new StringBuilder();
            while (myReader.hasNextLine()) {
                content.append(myReader.nextLine());
                content.append("\n");
            }
            myReader.close();
            return content.toString();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "";
    }

}

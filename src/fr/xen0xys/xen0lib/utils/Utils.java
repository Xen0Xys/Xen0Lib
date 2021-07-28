package fr.xen0xys.xen0lib.utils;

import com.google.common.hash.Hashing;
import org.apache.logging.log4j.core.util.IOUtils;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.UUID;

public abstract class Utils {

    /**
     * Get Minecraft username from UUID
     * @param uuid Java UUID
     * @return Minecraft username
     */
    public static String getUsernameFromUUID(UUID uuid) throws IOException, ParseException {
        String strUUID = uuid.toString();
        String url = "https://api.mojang.com/user/profiles/" + strUUID.replace("-", "") + "/names";

        String nameJson = IOUtils.toString(new InputStreamReader(new URL(url).openStream()));
        JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
        String playerSlot = nameValue.get(nameValue.size()-1).toString();
        JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
        return nameObject.get("name").toString();
    }

    /**
     * Get UUID from Minecraft username
     * @param minecraftName Minecraft username
     * @return Java UUID
     */
    public static UUID getUUIDFromUsername(String minecraftName) throws ParseException, IOException {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + minecraftName;
        String jsonUUID = IOUtils.toString(new InputStreamReader(new URL(url).openStream()));
        if(jsonUUID.isEmpty()){
            return null;
        }
        JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(jsonUUID);
        String strUUID = UUIDObject.get("id").toString();
        String completeUUID =  strUUID.replaceFirst( "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5" );
        return java.util.UUID.fromString( completeUUID );
    }

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

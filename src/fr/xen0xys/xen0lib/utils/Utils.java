package fr.xen0xys.xen0lib.utils;

import org.apache.logging.log4j.core.util.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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

}

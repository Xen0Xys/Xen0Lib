package fr.xen0xys.tests;

import fr.xen0xys.xen0lib.utils.Utils;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

class UtilsTest {

    @Test
    void getUsernameFromUUID() {
        String minecraftName = "";
        try {
            minecraftName = Utils.getUsernameFromUUID(UUID.fromString("392a4f22-41c2-4cba-b4a2-897ab1dae40b"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        assert minecraftName.equals("Xen0Xys");
    }


    @Test
    void getUUIDFromUsername() {
        UUID uuid = null;
        try {
            uuid = Utils.getUUIDFromUsername("Xen0Xys");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        if(uuid != null){
            assert uuid.equals(UUID.fromString("392a4f22-41c2-4cba-b4a2-897ab1dae40b"));
        }else{
            assert false;
        }
    }

    @Test
    void getCurrentTimestamp() {
        System.out.println(Utils.getCurrentTimestamp());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Utils.getCurrentTimestamp());
    }
}
package fr.xen0xys.xen0lib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    public Gson getGsonBuilder(){
        return new GsonBuilder().setPrettyPrinting().create();
    }

}

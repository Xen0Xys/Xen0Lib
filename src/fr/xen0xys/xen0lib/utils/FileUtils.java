package fr.xen0xys.xen0lib.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public abstract class FileUtils {

    public static void saveFile(Path path, String content){
        createFile(path);
        try {
            FileWriter myWriter = new FileWriter(path.toString());
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFile(Path path){
        try {
            File file = new File(path.toString());
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Path path){
        try {
            File myObj = new File(path.toString());
            Scanner myReader = new Scanner(myObj);
            StringBuilder content = new StringBuilder();
            while (myReader.hasNextLine()) {
                content.append(myReader.nextLine());
            }
            myReader.close();
            return content.toString();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }



}

package com.runningmanstudios.caffeineGameEngine.util.resources;

import com.runningmanstudios.caffeineGameEngine.util.log.GameLogger;
import com.runningmanstudios.caffeineGameEngine.window.Game;

import java.io.*;
import java.util.Arrays;

public class ResourceCreator implements Serializable {
    Game game;
    File resourceLocation;

    public ResourceCreator(Game game) {
        this.game = game;
        this.resourceLocation = game.getResourcesFolder();
    }

    public void createResource(String fileId) {
        if (!fileId.contains(":") || fileId.split(":").length != 2) {
            throw new IllegalArgumentException("File Id  ->  \"" + fileId + "\" <-\nid does not contain game id and resource location separated by colon");
        }

        File searchLocation;
        String[] resourcePath = fileId.split(":");
        String[] givenSearchId = resourcePath[0]
                .replace(".", System.getProperty("file.separator"))
                .replace("this", this.game.getBuilder().id())
                .split(System.getProperty("file.separator").equals("\\")?"\\\\":"/");
        String[] givenSubFolders = Arrays.copyOfRange(givenSearchId, 1, givenSearchId.length);
        String subfolders = String.join(System.getProperty("file.separator"), givenSubFolders)+System.getProperty("file.separator");
        String givenFile = subfolders + resourcePath[1]
                .replace("/", "")
                .replace("\\", "");
        if (!givenSearchId[0].equals(this.game.getBuilder().id())) {
            searchLocation = this.game.createResourcesFolder("ext"+System.getProperty("file.separator")+givenSearchId[0]+System.getProperty("file.separator"));
        } else {
            searchLocation = this.resourceLocation;
        }

        File fileToCreate = new File(searchLocation.getAbsolutePath()+System.getProperty("file.separator")+givenFile);
        if (fileToCreate.exists()) GameLogger.error("That file already exists");
        else {
            try {
                fileToCreate.getParentFile().mkdirs();
                fileToCreate.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void serializeResource(String fileId, Object object) {
        if (!fileId.contains(":") || fileId.split(":").length != 2) {
            throw new IllegalArgumentException("File Id  ->  \"" + fileId + "\" <-\nid does not contain game id and resource");
        }

        File searchLocation;
        String[] resourcePath = fileId.split(":");
        String[] givenSearchId = resourcePath[0]
                .replace(".", System.getProperty("file.separator"))
                .replace("this", this.game.getBuilder().id())
                .split(System.getProperty("file.separator").equals("\\")?"\\\\":"/");
        String[] givenSubFolders = Arrays.copyOfRange(givenSearchId, 1, givenSearchId.length);
        String subfolders = String.join(System.getProperty("file.separator"), givenSubFolders)+System.getProperty("file.separator");
        String givenFile = subfolders + resourcePath[1]
                .replace("/", "")
                .replace("\\", "");
        if (!givenSearchId[0].equals(this.game.getBuilder().id())) {
            searchLocation = this.game.createResourcesFolder("ext"+System.getProperty("file.separator")+givenSearchId[0]+System.getProperty("file.separator"));
        } else {
            searchLocation = this.resourceLocation;
        }

        File fileToCreate = new File(searchLocation.getAbsolutePath()+System.getProperty("file.separator")+givenFile);
        try {
            fileToCreate.getParentFile().mkdirs();
            FileOutputStream fo = new FileOutputStream(searchLocation.getAbsolutePath()+System.getProperty("file.separator")+givenFile);
            ObjectOutputStream os = new ObjectOutputStream(fo);
            os.writeObject(object);
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

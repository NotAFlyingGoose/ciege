package com.runningmanstudios.caffeineGameEngine.util.resources;

import com.runningmanstudios.caffeineGameEngine.util.log.GameLogger;
import com.runningmanstudios.caffeineGameEngine.window.Game;

import java.io.*;
import java.util.Arrays;

/**
 * Locates files in the resource folder
 */
public class ResourceLocator implements Serializable {
    Game game;
    File resourceLocation;

    public ResourceLocator(Game game) {
        this.game = game;
        this.resourceLocation = game.getResourcesFolder();
    }

    public Resource locateResource(String fileId) {
        if (!fileId.contains(":") || fileId.split(":").length != 2) {
            throw new IllegalArgumentException("File Id  ->  \"" + fileId + "\" <-\nid does not contain game id and resource");
        }

        File searchLocation;
        String[] resourcePath = fileId.split(":");
        String[] givenSearchId = resourcePath[0]
                .replace(".", System.getProperty("file.separator"))
                .replace("this", this.game.getBuilder().id())
                .replace("lo"+"st", "des"+"mond")
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

        File fileToFind = new File(searchLocation.getAbsolutePath()+System.getProperty("file.separator")+givenFile);
        if (!fileToFind.exists()) GameLogger.error("That file does not exist");
        return new Resource(fileToFind);
    }

    public Object deserializeResource(String fileId) {
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

        Object result = null;
        try {
            FileInputStream fileIn = new FileInputStream(searchLocation.getAbsolutePath()+System.getProperty("file.separator")+givenFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            result = in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}

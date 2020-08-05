/**
 * com.runningmanstudios.caffeineGameEngine.utilities is a bunch of utility classes that make it easier to do certain operations
 */
package com.runningmanstudios.caffeineGameEngine.util.log;

import com.runningmanstudios.caffeineGameEngine.console.Console;
import com.runningmanstudios.caffeineGameEngine.rendering.style.Style;
import com.runningmanstudios.caffeineGameEngine.util.resources.Resource;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * LogPrinter gives more information about methods, like the time they were printed, and saves it to a .clog file.
 */
public class GameLogger {
    public static Console console;
	private static final int depth;
    private static String className = "";
    private static final File logPath;
    private static final ClogWriter clogWriter;

    static {
        depth = 2;
        LocalDateTime now = LocalDateTime.now();
        String startTime = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-n").format(now);
        String extension = "clog";
        String path = System.getProperty("user.home")+System.getProperty("file.separator")+".ciege"+System.getProperty("file.separator")+"logs"+System.getProperty("file.separator")+startTime +"." + extension;
        System.out.println("Log is located in " + path);
        logPath = new File(path);
        try {
            logPath.getParentFile().mkdirs();
            logPath.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clogWriter = new ClogWriter(new Resource(logPath));
        clogWriter.requestOpen();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            clogWriter.requestClose();
            clogWriter.requestFinish();
        }));
        console = new Console();
        console.removeInput();
    }

    /**
     * print the text according to the LogType
     * @param text text to print
     * @param type LogType
     */
    private static void print(String text, LogType type){
        String time = clogWriter.requestLog("["+className+"]["+type+"]"+text);
        String call1 = "[" + className + "][" + time + "][";
        String call2 = "] : ";
        switch (type) {
            case WARN -> {
                console.print(call1, Color.YELLOW);
                console.print(type, new Color(255, 255, 155));
                console.print(call2, Color.YELLOW);
                console.println(text, new Color(255, 255, 155));
            }
            case DEBUG -> {
                console.print(call1, Color.BLUE);
                console.print(type, new Color(155, 155, 255));
                console.print(call2, Color.BLUE);
                console.println(text, new Color(155, 155, 255));
            }
            case ERROR -> {
                console.print(call1, Color.RED);
                console.print(type, new Color(255, 155, 155));
                console.print(call2, Color.RED);
                console.println(text, new Color(255, 155, 155));
            }
            case INFO -> {
                console.print(call1, Color.GREEN);
                console.print(type, new Color(155, 255, 155));
                console.print(call2, Color.GREEN);
                console.println(text, new Color(155, 255, 155));
            }
        }
    }

    /**
     * shows the console window
     */
    public static void show() {
        console.setVisible(true);
    }

    /**
     * hides the console window
     */
    public static void hide() {
        console.setVisible(false);
    }

    /**
     * Basic information print
     * LogType = INFO
     * @param text text to print
     */
    public static void info(Object text){
        String lastClass = Thread.currentThread().getStackTrace()[depth].getClassName();
        className = lastClass.substring(lastClass.lastIndexOf(".") + 1);
        print(String.valueOf(text), LogType.INFO);
    }

    /**
     * Warning print, gives more information than info
     * LogType = WARN
     * @param text text to print
     */
    public static void warn(Object text){
        String lastClass = Thread.currentThread().getStackTrace()[depth].getClassName();
        className = lastClass.substring(lastClass.lastIndexOf(".") + 1);
        className += "\\" + Thread.currentThread().getStackTrace()[depth].getMethodName();
        print(String.valueOf(text), LogType.WARN);
    }

    /**
     * Error print, gives more information than info
     * LogType = ERROR
     * @param text text to print
     */
    public static void error(Object text){
        String lastClass = Thread.currentThread().getStackTrace()[depth].getClassName();
        className = lastClass.substring(lastClass.lastIndexOf(".") + 1);
        className += "\\" + Thread.currentThread().getStackTrace()[depth].getMethodName();
        print(String.valueOf(text), LogType.ERROR);
    }

    /**
     * Debug print, gives most information
     * LogType = DEBUG
     * @param text text to print
     */
    public static void debug(Object text){
        className = Thread.currentThread().getStackTrace()[depth].getFileName();
        className += "\\" + Thread.currentThread().getStackTrace()[depth].getClassName();
        className += "\\" + Thread.currentThread().getStackTrace()[depth].getMethodName();
        className += ":" + Thread.currentThread().getStackTrace()[depth].getLineNumber();
        print(String.valueOf(text), LogType.DEBUG);
    }

    /**
     * gets the contents of the Log
     * @return returns the Log
     */
    public static String getLog() {
        try {
            return String.join("\n", Files.readAllLines(Paths.get(logPath.getAbsolutePath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * what kind of message to print
     */
    private enum LogType {
        INFO, DEBUG, WARN, ERROR
    }
}

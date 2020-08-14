package com.runningmanstudios.caffeineGameEngine.util.log;

import com.runningmanstudios.caffeineGameEngine.checks.exceptions.ClogException;
import com.runningmanstudios.caffeineGameEngine.util.resources.Resource;

import java.util.*;

public class ClogReader {
    private final Resource clogFile;
    public String readVersion;

    public static int NANO = 0;
    public static int SECOND = 1;
    public static int MINUTE = 2;
    public static int HOUR = 3;
                    //hour      minute     second      nano       command
    private final Map<String, Map<String, Map<String, Map<String, String>>>> logs = new LinkedHashMap<>();
    private final Map<String, List<String>> nanoLogs = new LinkedHashMap<>();
    private final Map<String, List<String>> secondLogs = new LinkedHashMap<>();
    private final Map<String, List<String>> minuteLogs = new LinkedHashMap<>();
    private final Map<String, List<String>> hourLogs = new LinkedHashMap<>();

    public ClogReader(Resource clogFile) {
        this.clogFile = clogFile;
        if (!clogFile.getExtension().equals("clog")) {
            throw new IllegalArgumentException("Resource  ->  \"" + clogFile + "\" <-\nresource is not a .clog file");
        }
        String[] contents = clogFile.getContents().split("\n");
        boolean semicolon = false;
        boolean finished = false;
        for (String line : contents) {
            if ((line.endsWith(",") || line.endsWith(";")) && !(semicolon && finished)) {
                String[] data = this.getText(line);
                String command = data[0];
                String time = data[1];
                String text = data[2];
                String nanoTime = time;
                String secondTime = time = time.substring(0, time.contains(".") ? time.lastIndexOf('.') : time.length());
                String minuteTime = time = time.substring(0, time.contains(":") ? time.lastIndexOf(':') : time.length());
                String hourTime = time = time.substring(0, time.contains(":") ? time.lastIndexOf(':') : time.length());
                if (!this.logs.containsKey(hourTime)) {
                    this.logs.put(hourTime, new HashMap<>());
                }
                if (!this.logs.get(hourTime).containsKey(minuteTime)) {
                    this.logs.get(hourTime).put(minuteTime, new HashMap<>());
                }
                if (!this.logs.get(hourTime).get(minuteTime).containsKey(secondTime)) {
                    this.logs.get(hourTime).get(minuteTime).put(secondTime, new HashMap<>());
                }
                if (!this.logs.get(hourTime).get(minuteTime).get(secondTime).containsKey(nanoTime)) {
                    this.logs.get(hourTime).get(minuteTime).get(secondTime).put(nanoTime, command + "_" + text);
                }

                //nano
                if (!this.nanoLogs.containsKey(time)) {
                    this.nanoLogs.put(time, new LinkedList<>());
                }
                this.nanoLogs.get(time).add(command + "_" + text);

                //second
                time = time.substring(0, time.contains(".") ? time.lastIndexOf('.') : time.length());
                if (!this.secondLogs.containsKey(time)) {
                    this.secondLogs.put(time, new LinkedList<>());
                }
                this.secondLogs.get(time).add(command + "_" + text);

                //minute
                time = time.substring(0, time.contains(":") ? time.lastIndexOf(':') : time.length());
                if (!this.minuteLogs.containsKey(time)) {
                    this.minuteLogs.put(time, new LinkedList<>());
                }
                this.minuteLogs.get(time).add(command + "_" + text);

                //hour
                time = time.substring(0, time.contains(":") ? time.lastIndexOf(':') : time.length());
                if (!this.hourLogs.containsKey(time)) {
                    this.hourLogs.put(time, new LinkedList<>());
                }
                this.hourLogs.get(time).add(command + "_" + text);

                synchronized(this) {
                    //check for semicolon
                    if (line.endsWith(";")) {
                        semicolon = true;
                    }
                    //check for finish command
                    if (line.equals(ClogWriter.finish)) {
                        finished = true;
                    }
                }
            }
        }
        synchronized(this) {
            if (!semicolon) {
                new ClogException("File  ->  \"" + clogFile + "\"\nClog file does not have ending semicolon").printStackTrace();
            }
            if (!finished) {
                new ClogException("File  ->  \"" + clogFile + "\"\nClog file does not have a finish command").printStackTrace();
            }
        }
        System.out.println(this.logs);
    }

    public String[] getText(String line) {
        String[] sep = line.split(":", 2);
        String commandName = sep[0].substring(1);
        String[] data = new String[3];
        String text = sep[1].substring(sep[1].indexOf("{")+1,sep[1].indexOf("}"));
        data[0] = commandName;
        String[] time = sep[1].substring(sep[1].indexOf("[") + 1, sep[1].indexOf("]")).split("\\|");
        data[1] = time.length>1?time[1]:"IGNORED";
        data[2] = text.startsWith("\"")?text:"default";
        return data;
    }

    public Object getLogsFor(String time, int downTo) {
        switch (downTo) {
            case 0 -> {
                String nanoTime = time;
                String secondTime = time = time.substring(0, time.contains(".") ? time.lastIndexOf('.') : time.length());
                String minuteTime = time = time.substring(0, time.contains(":") ? time.lastIndexOf(':') : time.length());
                String hourTime = time = time.substring(0, time.contains(":") ? time.lastIndexOf(':') : time.length());
                return this.logs.get(hourTime).get(minuteTime).get(secondTime).get(nanoTime);
            }
            case 1 -> {
                String secondTime = time;
                String minuteTime = time = time.substring(0, time.contains(":") ? time.lastIndexOf(':') : time.length());
                String hourTime = time = time.substring(0, time.contains(":") ? time.lastIndexOf(':') : time.length());
                return this.logs.get(hourTime).get(minuteTime).get(secondTime).values();
            }
            case 2 -> {
                String minuteTime = time;
                String hourTime = time = time.substring(0, time.contains(":") ? time.lastIndexOf(':') : time.length());
                return this.logs.get(hourTime).get(minuteTime).values();
            }
            case 3 -> {
                return this.logs.get(time).values();
            }
            default -> {
                return null;
            }
        }
    }
}

package com.runningmanstudios.caffeineGameEngine.util.log;

import com.runningmanstudios.caffeineGameEngine.util.resources.Resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClogWriter implements Runnable {
    public static final String finish = "{CLOG-FINISH:[TIME-IGNORE]};";
    private final String lastTime = "1977/07/20|15:27:30.0001";
    private int addTime = 1;
    private boolean open = false;
    private final Resource clogFile;
    private boolean logOpen;
    private final CopyOnWriteArrayList<String> toAdd = new CopyOnWriteArrayList<>();
    public static final String ClogVersion = "1.0";
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd|HH:mm:ss.n");

    public ClogWriter(Resource clogFile) {
        this.clogFile = clogFile;
        if (!clogFile.getExtension().equals("clog")) {
            throw new IllegalArgumentException("Resource  ->  \"" + clogFile + "\" <-\nresource is not a .clog file");
        }
        this.logOpen = true;
        Thread logToFile = new Thread(this);
        logToFile.setDaemon(true);
        logToFile.start();
    }

    public String requestLog(String text) {
        if (this.open) {
            String time = this.getTime();
            this.toAdd.add("{LOG-REQ:[" + time + "]{\"" + text + "\"}},\n");
            return time.split("\\|")[1];
        }
        return "";
    }

    public String requestOpen() {
        if (!this.open) {
            String time = this.getTime();
            this.toAdd.add("{OPEN-REQ:[" + time + "]},\n");
            this.open=true;
            return time.split("\\|")[1];
        }
        return "";
    }

    public String requestClose() {
        if (this.open) {
            String time = this.getTime();
            this.toAdd.add("{CLOSE-REQ:[" + time + "]},\n");
            this.open=false;
            return time.split("\\|")[1];
        }
        return "";
    }

    public void requestFinish() {
        if (this.open) {
            this.requestClose();
        }
        this.toAdd.add(finish);
    }

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        String time = this.timeFormatter.format(now);
        time = time.substring(0, Math.min(time.length(), 27));
        long curNano = Long.parseLong(time.substring(20));
        long lastNano = Long.parseLong(this.lastTime.substring(20));
        if (curNano<=lastNano) {
            lastNano += this.addTime;
            time = time.substring(0, 20) + lastNano;
            this.addTime+=1;
        } else {
            this.addTime = 1;
        }
        return time;
    }

    @Override
    public void run() {
        System.out.println("Started Clog AutoWriter");
        this.clogFile.clearContents();
        this.clogFile.open();
        this.clogFile.write("{CLOG-START:[TIME-IGNORE]{\""+ClogVersion+"\"}},\n");
        while (this.logOpen) {
            synchronized(this) {
                if (!this.toAdd.isEmpty()) {
                    CopyOnWriteArrayList<String> list;
                    list = this.toAdd;
                    for (String addition : list) {
                        this.clogFile.write(addition);
                        this.toAdd.remove(addition);
                        if (addition.equals(finish)) this.logOpen=false;
                    }
                }
            }
        }
        this.clogFile.close();
    }
}

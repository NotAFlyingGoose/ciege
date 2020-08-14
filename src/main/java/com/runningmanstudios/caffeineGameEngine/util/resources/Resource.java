package com.runningmanstudios.caffeineGameEngine.util.resources;

import com.runningmanstudios.caffeineGameEngine.util.log.GameLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class Resource {
    private final File resourceFile;
    private FileWriter resourceWriter;

    public Resource(File resourceFile) {
        if (!resourceFile.exists()) GameLogger.warn("this resource file is non existent");
        this.resourceFile = resourceFile;
    }

    public File getFile() {
        return this.resourceFile;
    }

    public String getContents() {
        if (!this.resourceFile.exists()) {
            GameLogger.warn("this resource file is non existent");
        } else {
            try {
                return String.join("\n", Files.readAllLines(Paths.get(this.resourceFile.getAbsolutePath()), StandardCharsets.ISO_8859_1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public void write(String content) {
        if (!this.resourceFile.exists()) {
            GameLogger.warn("you cannot write to a file that does not exist");
        } else {
            synchronized (this) {
                try {
                    this.resourceWriter.write(content);
                    this.resourceWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void close() {
        if (!this.resourceFile.exists()) {
            GameLogger.warn("you cannot close a file that does not exist");
        } else {
            try {
                this.resourceWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void open() {
        if (!this.resourceFile.exists()) {
            GameLogger.warn("you cannot open a file that does not exist");
        } else {
            try {
                this.resourceWriter = new FileWriter(this.resourceFile, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearContents() {
        if (!this.resourceFile.exists()) {
            GameLogger.warn("you cannot clear the contents of a file that does not exist");
        } else {
            try {
                new FileWriter(this.resourceFile, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getExtension() {
        String filename = this.resourceFile.getName();
        Optional<String> o = Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
        return o.orElse("null");
    }

    @Override
    public String toString() {
        return this.resourceFile.getName();
    }
}

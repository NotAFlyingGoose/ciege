package com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable;

public class StringVariable extends Variable {
    public static final String backSlashTag = "[<//*//>"+("backslash".hashCode())+"]";
    public StringVariable(String title, String string) {
        super(title, string.trim());
    }

    public String getContentRaw() {
        return (String) this.getData();
    }

    public String getContentIn() {
        try {
            if (isValid()) return getContentRaw().substring(1, getContentRaw().trim().length()-1);
            else throw new StringIndexOutOfBoundsException();
        } catch (StringIndexOutOfBoundsException e) {
            return getContentRaw();
        }
    }

    public String getContent() {
        return getContentIn()
                .replace(backSlashTag, "null")
                .replace("\\\\", backSlashTag)
                .replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\s", "\s")
                .replace("\\\"", "\"")
                .replace("\\", "null")
                .replace(backSlashTag, "\\");
    }

    public static boolean isValid(String data) {
        return (data.trim().startsWith("\"") && data.trim().endsWith("\""))||(data.trim().startsWith("'") && data.trim().endsWith("'"))||(data.trim().startsWith("`") && data.trim().endsWith("`"));
    }

    public boolean isValid() {
        String data = getContentRaw();
        return (data.trim().startsWith("\"") && data.trim().endsWith("\""))||(data.trim().startsWith("'") && data.trim().endsWith("'"))||(data.trim().startsWith("`") && data.trim().endsWith("`"));
    }

}

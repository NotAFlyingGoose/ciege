package com.runningmanstudios.caffeineGameEngine.console;

import com.runningmanstudios.caffeineGameEngine.console.script.window.CScriptWin;
import com.runningmanstudios.caffeineGameEngine.rendering.style.HtmlStyle;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Console {

    private final Map<String, String> variables = new HashMap<>();

    private boolean canInput;
    private Process onlyProc = null;
    
    public JFrame frame;
    public JTextPane console;
    public JTextField input;
    public JScrollPane scrollpane;

    public StyledDocument document;

    public final Set<Process> PROCESSES = new HashSet<>();

    public Process CLEAR = new Process(this, "clear", "cls") {
        @Override
        public void run(String[] args) {
            console.clear();
            console.println("Clearing screen", trace, new Color(155, 155, 255), null);
        }

        @Override
        public String getDescription() {
            return "clears the screen of all text";
        }

        @Override
        public String getUsage() {
            return "clear";
        }
    };
    public Process POPUP = new Process(this, "popup", "dialog") {
        @Override
        public void run(String[] args) {
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                message.append(args[i]);
                if (i != args.length - 1) {
                    message.append(" ");
                }
            }
            console.println("Showing popup with text \"" + message + "\"", trace, new Color(155, 155, 255), new Color(55, 55, 155));
            JOptionPane.showMessageDialog(null, message, "Popup", JOptionPane.INFORMATION_MESSAGE);
        }

        @Override
        public String getDescription() {
            return "shows a popup with text as arguments";
        }

        @Override
        public String getUsage() {
            return "popup \"text here\"";
        }
    };
    public Process SCRIPT = new Process(this, "script", "execute") {
        @Override
        public void run(String[] args) {
            console.println("Opening scripting window", trace, new Color(155, 155, 255), new Color(55, 55, 155));
            new CScriptWin();
        }

        @Override
        public String getDescription() {
            return "shows a popup with text as arguments";
        }

        @Override
        public String getUsage() {
            return "popup \"text here\"";
        }
    };
    public Process SET = new Process(this, "set") {
        @Override
        public void run(String[] args) {
            String setting = args[0];
            String to = args[1];
            boolean toBool;
            if (to.equalsIgnoreCase("true")) {
                toBool = true;
            } else if (to.equalsIgnoreCase("false")) {
                toBool = false;
            } else {
                throw new IllegalArgumentException("You must give either \"true\" or \"false\" as a parameter");
            }
            switch (setting) {
                case "math" -> {
                    console.math = toBool;
                    console.println("Set math to " + toBool, trace, new Color(155, 155, 255), null);
                }
                case "trace" -> {
                    console.trace = toBool;
                    console.println("Set trace to " + toBool, trace, new Color(155, 155, 255), null);
                }
                case "var", "dovars" -> {
                    console.dovars = toBool;
                    console.println("Set dovars to " + toBool, trace, new Color(155, 155, 255), null);
                }
            }
        }

        @Override
        public String getDescription() {
            return "turns on or off a certain setting in the console";
        }

        @Override
        public String getUsage() {
            return "set [setting] [true/false]";
        }
    };
    public Process PRINT = new Process(this, "print", "say") {
        @Override
        public void run(String[] args) {
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                message.append(args[i]);
                if (i != args.length - 1) {
                    message.append(" ");
                }
            }
            console.println(message.toString(), trace, new Color(155, 155, 255), null);
        }

        @Override
        public String getDescription() {
            return "prints a message to the screen with text as arguments";
        }

        @Override
        public String getUsage() {
            return "print \"text here\"";
        }
    };

    private boolean trace = false;
    private boolean math = true;
    private boolean dovars = true;

    ArrayList<String> recentCommands = new ArrayList<>();
    int recentUsedId = 0;
    int recentUsedMax = 10;

    public Console() {
        PROCESSES.add(CLEAR);
        PROCESSES.add(POPUP);
        PROCESSES.add(SET);
        PROCESSES.add(PRINT);
        PROCESSES.add(SCRIPT);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        this.frame = new JFrame();
        this.frame.setTitle("Console");
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
/*        this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                        null, "Are you sure that you want to close the application through this console?",
                        "Terminate Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Terminate", "Cancel"}, "Cancel");
                if (confirm == 0) {
                    System.exit(0);
                }
            }
        };
        this.frame.addWindowListener(exitListener);*/

        this.console = new JTextPane() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getUI().getPreferredSize(this).width
                        <= getParent().getSize().width;
            }
        };
        this.console.setEditable(false);
        this.console.setFont(new Font("Courier New", Font.PLAIN, 12));
        this.console.setOpaque(false);

        this.document = this.console.getStyledDocument();

        this.input = new JTextField();
        this.input.setEditable(true);
        this.input.setCaretColor(Color.WHITE);
        this.input.setForeground(Color.WHITE);
        this.input.setFont(new Font("Courier New", Font.PLAIN, 12));
        this.input.setBackground(new Color(105, 105, 105));
        //hello

        this.input.addActionListener(e -> {
            String text = Console.this.input.getText();
            if (text.length() > 1) {
                this.recentCommands.add(text);
                this.recentUsedId = 0;
                this.input.setText("");
                this.println("User> " + text, false, null, null);
                this.doCommand(text);
            }
        });

        this.input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (Console.this.recentUsedId < (Console.this.recentUsedMax - 1) && Console.this.recentUsedId < (Console.this.recentCommands.size() - 1)) {
                        Console.this.recentUsedId++;
                    }
                    Console.this.input.setText(Console.this.recentCommands.get(Console.this.recentCommands.size() - 1 - Console.this.recentUsedId));
                    System.out.println(Console.this.input.getText().length());
                    System.out.println(Console.this.input.getDocument().getLength());
                    Console.this.input.setCaretPosition(Console.this.input.getText().length());
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (Console.this.recentUsedId > 0) {
                        Console.this.recentUsedId--;
                    }
                    Console.this.input.setText(Console.this.recentCommands.get(Console.this.recentCommands.size() - 1 - Console.this.recentUsedId));
                    Console.this.input.setCaretPosition(Console.this.input.getDocument().getLength());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        this.scrollpane = new JScrollPane(this.console);
        this.scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollpane.setBorder(null);
        this.scrollpane.setOpaque(false);
        this.scrollpane.getViewport().setOpaque(false);

        this.frame.add(this.input, BorderLayout.SOUTH);
        this.frame.add(this.scrollpane, BorderLayout.CENTER);

        canInput = true;

        this.frame.getContentPane().setBackground(new Color(50, 50, 50));

        this.frame.setSize(660, 350);
        this.frame.setLocationRelativeTo(null);

        this.frame.setResizable(true);
    }

    public void setVisible(boolean visible) {
        this.frame.setVisible(visible);
    }

    public void removeInput() {
        if (canInput) {
            frame.remove(this.input);
            canInput = false;
        }
    }

    public void addInput() {
        if (!canInput) {
            frame.add(this.input, BorderLayout.SOUTH);
            canInput = true;
        }
    }

    private void scrollTop() {
        this.console.setCaretPosition(0);
    }

    private void scrollBottom() {
        this.console.setCaretPosition(this.console.getDocument().getLength());
    }

    private void print(Object text, boolean trace, Color foreground, Color background) {
        Style style = this.console.addStyle("HtmlStyle", null);

        if (trace) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();
            String caller = elements[0].getClassName();
            text = caller + " -> " + text;
        }

        try {
            String[] words = text.toString().split(" ", -1);
            for (int i = 0; i < words.length; i++) {
                boolean printSpace = false;
                String word = words[i];

                if (dovars && word.startsWith("$")) {
                    StyleConstants.setForeground(style, new Color(155, 255, 240));
                    StyleConstants.setBackground(style, new Color(89, 155, 84));
                    printSpace = true;
                } else {
                    StyleConstants.setForeground(style, foreground);
                    if (background!=null) StyleConstants.setBackground(style, background);
                    else StyleConstants.setBackground(style, this.frame.getContentPane().getBackground());
                }

                if (!printSpace) if (i < words.length-1) word+=" ";

                this.document.insertString(this.document.getLength(), word, style);

                if (printSpace) this.document.insertString(this.document.getLength(), " ", null);
            }

            scrollBottom();
        } catch (Exception ignored) {}
    }

    private void println(Object text, boolean trace, Color foreground, Color background) {
        this.print(text + "\n", trace, foreground, background);
    }

    private void clear() {
        try {
            this.document.remove(0, this.document.getLength());
        } catch (Exception ignored) {}
    }

    private void doCommand(String text) {
        String[] args = text.split(" ");
        String command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        try {
            if (dovars) {
                for (int i = 0; i < args.length; i++) {
                    String arg = args[i];
                    if (arg.startsWith("$")) {
                        if (arg.equals("$all")) {
                            args[i] = variables.toString();
                        } else {
                            String var = variables.get(arg);
                            if (var == null) continue;
                            args[i] = var;
                        }
                    }
                }
            }
            if (math) {
                for (int i = 0; i < args.length; i++) {
                    String arg = args[i];
                    switch (arg) {
                        case "+" -> {
                            double left = Double.parseDouble(args[i - 1]);
                            double right = Double.parseDouble(args[i + 1]);
                            ArrayList<String> newargs = new ArrayList<>(Arrays.asList(args));
                            newargs.set(i, String.valueOf(left + right));
                            newargs.remove(i + 1);
                            newargs.remove(i - 1);
                            // ArrayList to Array Conversion
                            String[] argscopy = new String[newargs.size()];
                            for (int in = 0; in < newargs.size(); in++)
                                argscopy[in] = newargs.get(in);
                            args = argscopy;
                        }
                        case "-" -> {
                            double left = Double.parseDouble(args[i - 1]);
                            double right = Double.parseDouble(args[i + 1]);
                            ArrayList<String> newargs = new ArrayList<>(Arrays.asList(args));
                            newargs.set(i, String.valueOf(left - right));
                            newargs.remove(i + 1);
                            newargs.remove(i - 1);
                            // ArrayList to Array Conversion
                            String[] argscopy = new String[newargs.size()];
                            for (int in = 0; in < newargs.size(); in++)
                                argscopy[in] = newargs.get(in);
                            args = argscopy;
                        }
                        case "*" -> {
                            double left = Double.parseDouble(args[i - 1]);
                            double right = Double.parseDouble(args[i + 1]);
                            ArrayList<String> newargs = new ArrayList<>(Arrays.asList(args));
                            newargs.set(i, String.valueOf(left * right));
                            newargs.remove(i + 1);
                            newargs.remove(i - 1);
                            // ArrayList to Array Conversion
                            String[] argscopy = new String[newargs.size()];
                            for (int in = 0; in < newargs.size(); in++)
                                argscopy[in] = newargs.get(in);
                            args = argscopy;
                        }
                        case "/" -> {
                            double left = Double.parseDouble(args[i - 1]);
                            double right = Double.parseDouble(args[i + 1]);
                            ArrayList<String> newargs = new ArrayList<>(Arrays.asList(args));
                            newargs.set(i, String.valueOf(left / right));
                            newargs.remove(i + 1);
                            newargs.remove(i - 1);
                            // ArrayList to Array Conversion
                            String[] argscopy = new String[newargs.size()];
                            for (int in = 0; in < newargs.size(); in++)
                                argscopy[in] = newargs.get(in);
                            args = argscopy;
                        }
                    }
                }
            }

            if (dovars && command.startsWith("$") && args.length > 0) {
                String operator = args[0].toLowerCase();
                switch (operator) {
                    case "=" -> {
                        if (command.equals("$all")) throw new IllegalArgumentException("That is a reserved variable");
                        else {
                            StringBuilder affect = new StringBuilder("");
                            for (int i = 1; i < args.length; i++) {
                                affect.append(args[i]);
                                if (i != args.length - 1) {
                                    affect.append(" ");
                                }
                            }
                            this.println(command + " = " + affect, this.trace, null, null);
                            variables.put(command, affect.toString());
                        }
                    }
                    case "+=" -> {
                        if (command.equals("$all")) throw new IllegalArgumentException("That is a reserved variable");
                        else {
                            double equal = Double.parseDouble(variables.get(command));
                            StringBuilder affect = new StringBuilder("");
                            for (int i = 1; i < args.length; i++) {
                                affect.append(args[i]);
                                if (i != args.length - 1) {
                                    affect.append(" ");
                                }
                            }
                            double match = Double.parseDouble(affect.toString());
                            this.println(command + " = " + (equal + match), this.trace, null, null);
                            variables.put(command, String.valueOf(equal+match));
                        }
                    }
                    case "-=" -> {
                        if (command.equals("$all")) throw new IllegalArgumentException("That is a reserved variable");
                        else {
                            double equal = Double.parseDouble(variables.get(command));
                            StringBuilder affect = new StringBuilder("");
                            for (int i = 1; i < args.length; i++) {
                                affect.append(args[i]);
                                if (i != args.length - 1) {
                                    affect.append(" ");
                                }
                            }
                            double match = Double.parseDouble(affect.toString());
                            this.println(command + " = " + (equal - match), this.trace, null, null);
                            variables.put(command, String.valueOf(equal-match));
                        }
                    }
                    case "*=" -> {
                        if (command.equals("$all")) throw new IllegalArgumentException("That is a reserved variable");
                        else {
                            double equal = Double.parseDouble(variables.get(command));
                            StringBuilder affect = new StringBuilder("");
                            for (int i = 1; i < args.length; i++) {
                                affect.append(args[i]);
                                if (i != args.length - 1) {
                                    affect.append(" ");
                                }
                            }
                            double match = Double.parseDouble(affect.toString());
                            this.println(command + " = " + (equal * match), this.trace, null, null);
                            variables.put(command, String.valueOf(equal*match));
                        }
                    }
                    case "/=" -> {
                        if (command.equals("$all")) throw new IllegalArgumentException("That is a reserved variable");
                        else {
                            double equal = Double.parseDouble(variables.get(command));
                            StringBuilder affect = new StringBuilder("");
                            for (int i = 1; i < args.length; i++) {
                                affect.append(args[i]);
                                if (i != args.length - 1) {
                                    affect.append(" ");
                                }
                            }
                            double match = Double.parseDouble(affect.toString());
                            this.println(command + " = " + (equal / match), this.trace, null, null);
                            variables.put(command, String.valueOf(equal/match));
                        }
                    }
                    case "==" -> {
                        String equal = variables.get(command);
                        StringBuilder affect = new StringBuilder("");
                        for (int i = 1; i < args.length; i++) {
                            affect.append(args[i]);
                            if (i != args.length - 1) {
                                affect.append(" ");
                            }
                        }
                        System.out.println(equal);
                        System.out.println(affect.toString());
                        this.println(equal.equals(affect.toString()), this.trace, null, null);
                    }
                    case ">" -> {
                        double equal = Double.parseDouble(variables.get(command));
                        StringBuilder affect = new StringBuilder("");
                        for (int i = 1; i < args.length; i++) {
                            affect.append(args[i]);
                            if (i != args.length - 1) {
                                affect.append(" ");
                            }
                        }
                        double match = Double.parseDouble(affect.toString());
                        this.println(equal > match, this.trace, null, null);
                    }
                    case "<" -> {
                        double equal = Double.parseDouble(variables.get(command));
                        StringBuilder affect = new StringBuilder("");
                        for (int i = 1; i < args.length; i++) {
                            affect.append(args[i]);
                            if (i != args.length - 1) {
                                affect.append(" ");
                            }
                        }
                        double match = Double.parseDouble(affect.toString());
                        this.println(equal < match, this.trace, null, null);
                    }
                    case ">=" -> {
                        double equal = Double.parseDouble(variables.get(command));
                        StringBuilder affect = new StringBuilder("");
                        for (int i = 1; i < args.length; i++) {
                            affect.append(args[i]);
                            if (i != args.length - 1) {
                                affect.append(" ");
                            }
                        }
                        double match = Double.parseDouble(affect.toString());
                        this.println(equal >= match, this.trace, null, null);
                    }
                    case "<=" -> {
                        double equal = Double.parseDouble(variables.get(command));
                        StringBuilder affect = new StringBuilder("");
                        for (int i = 1; i < args.length; i++) {
                            affect.append(args[i]);
                            if (i != args.length - 1) {
                                affect.append(" ");
                            }
                        }
                        double match = Double.parseDouble(affect.toString());
                        this.println(equal <= match, this.trace, null, null);
                    }
                    default -> throw new IllegalArgumentException("Incorrect operator \"" + operator + "\"");
                }
                return;
            }

            if (command.equals("help")) {
                println("=====Help Menu=====", this.trace, HtmlStyle.getColor("lightblue"), null);
                println("List of affiliated processes", this.trace, HtmlStyle.getColor("lightblue"), null);
                println("-------------------", this.trace, HtmlStyle.getColor("lightblue"), null);
                for (Process process : PROCESSES) {
                    print("NAME : ", this.trace, HtmlStyle.getColor("orange"), null);
                    println(process.names.get(0), this.trace, HtmlStyle.getColor("red"), null);
                    println("ALIASES : ", this.trace, HtmlStyle.getColor("orange"), null);
                    for (int ni = 1; ni < process.names.size(); ni++) {
                        println("   "+process.names.get(ni) + (ni!=process.names.size()-1?", ":""), this.trace, HtmlStyle.getColor("red"), null);
                    }
                    print("DESCRIPTION : ", this.trace, HtmlStyle.getColor("orange"), null);
                    println(process.getDescription(), this.trace, HtmlStyle.getColor("salmon"), null);
                    print("USAGE : ", this.trace, HtmlStyle.getColor("orange"), null);
                    println(process.getUsage(), this.trace, HtmlStyle.getColor("salmon"), null);
                    println("--------------", this.trace, HtmlStyle.getColor("lightblue"), null);
                }
                return;
            }
            for (Process process : PROCESSES) {
                if (process.names.contains(command)) {
                    process.run(args);
                }
            }
        } catch (Exception e) {
            this.println("Error -> " + e.getMessage(), this.trace, new Color(255, 155, 155), new Color(155, 55, 55));
        }
    }

    public boolean requestOnlyProc(Process process) {
        if (onlyProc==null)return false;
        println("Using " + process.names.get(0) + ":", trace, new Color(155, 255, 155), new Color(55, 155, 55));
        println(">", false, null, null);
        onlyProc = process;
        return true;
    }
    public abstract static class Process {
        protected final Console console;
        public final ArrayList<String> names = new ArrayList<>();

        public Process(Console console, String... names) {
            this.console = console;
            this.names.addAll(Arrays.asList(names));
        }

        public abstract void run(String[] args);

        public abstract String getDescription();

        public abstract String getUsage();
    }


    /**
     * request writing methods
     */

    public boolean requestPrint(Object text, Color foreground, Color background) {
        if (onlyProc==null) {
            this.print(text, this.trace, foreground, background);
            return true;
        }
        return false;
    }

    public boolean requestPrint(Object text) {
        if (onlyProc==null) {
            this.print(text, this.trace, null, null);
            return true;
        }
        return false;
    }

    public boolean requestPrintln(Object text, Color foreground, Color background) {
        if (onlyProc==null) {
            this.println(text, this.trace, foreground, background);
            return true;
        }
        return false;
    }

    public boolean requestPrintln(Object text) {
        if (onlyProc==null) {
            this.println(text, this.trace, null, null);
            return true;
        }
        return false;
    }

    public boolean requestCommand(String command) {
        if (onlyProc==null) {
            this.doCommand(command);
            return true;
        }
        return false;
    }

}

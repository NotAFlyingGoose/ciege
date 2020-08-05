package com.runningmanstudios.caffeineGameEngine.console;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Console {

    private final Map<String, String> variables = new HashMap<>();

    private boolean canInput = false;
    
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
            console.println("Clearing screen", new Color(155, 155, 255));
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
            console.println("Showing popup with text \"" + message + "\"", new Color(155, 155, 255));
            JOptionPane.showMessageDialog(null, message, "Popup", JOptionPane.INFORMATION_MESSAGE);

        }
    };
    public Process TRACE = new Process(this, "trace", "tracing") {
        @Override
        public void run(String[] args) {
            if (args[0].equalsIgnoreCase("true")) {
                console.trace = true;
            } else if (args[0].equalsIgnoreCase("false")) {
                console.trace = false;
            }
            console.println("Set command tracing to " + console.trace, new Color(155, 155, 255));
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
            console.println(message.toString(), new Color(155, 155, 255));
        }
    };

    boolean trace = false;

    ArrayList<String> recentCommands = new ArrayList<>();
    int recentUsedId = 0;
    int recentUsedMax = 10;

    public Console() {
        PROCESSES.add(CLEAR);
        PROCESSES.add(POPUP);
        PROCESSES.add(TRACE);
        PROCESSES.add(PRINT);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        this.frame = new JFrame();
        this.frame.setTitle("Console");
        this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
        this.frame.addWindowListener(exitListener);

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
                this.println(text, this.trace);
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
    
    public void scrollTop() {
        this.console.setCaretPosition(0);
    }

    public void scrollBottom() {
        this.console.setCaretPosition(this.console.getDocument().getLength());
    }

    public void print(Object text) {
        this.print(text, this.trace);
    }

    public void print(Object text, boolean trace) {
        this.print(text, trace, new Color(255, 255, 255));
    }

    public void print(Object text, Color color) {
        this.print(text, this.trace, color);
    }

    public void print(Object text, boolean trace, Color color) {
        Style style = this.console.addStyle("Style", null);

        if (trace) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();
            String caller = elements[0].getClassName();
            text = caller + " -> " + text;
        }

        try {
            for (String word : text.toString().split(" ")) {
                if (word.startsWith("$")) {
                    StyleConstants.setForeground(style, new Color(128, 255, 155));
                } else {
                    StyleConstants.setForeground(style, color);
                }
                this.document.insertString(this.document.getLength(), word + " ", style);
            }

            scrollBottom();
        } catch (Exception e) {
        }
    }

    public void println(Object text) {
        this.println(text, this.trace);
    }

    public void println(Object text, boolean trace) {
        this.println(text, trace, new Color(255, 255, 255));
    }

    public void println(Object text, Color color) {
        this.println(text, this.trace, color);
    }

    public void println(Object text, boolean trace, Color color) {
        this.print(text + "\n", trace, color);
    }

    public void clear() {
        try {
            this.document.remove(0, this.document.getLength());
        } catch (Exception e) {
        }
    }

    public void doCommand(String text) {
        String[] args = text.split(" ");
        String command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("$")) {
                String var = variables.get(arg);
                if (var == null) continue;
                args[i]=var;
            }
        }

        if (command.startsWith("$") && args.length > 0) {
            variables.put(command, args[0]);
            this.println(command + " = " + args[0]);
        }

        try {

            for (Process process : PROCESSES) {
                if (process.commandMatch(command)) {
                    process.run(args);
                }
            }
        } catch (Exception e) {
            this.println("Error -> " + e.getMessage(), this.trace, new Color(255, 155, 155));
        }
    }

    public abstract class Process {
        public Console console;
        public Set<String> names = new HashSet<>();

        public Process(Console console, String... names) {
            this.console = console;
            this.names.addAll(Arrays.asList(names));
        }

        public abstract void run(String[] args);

        public boolean commandMatch(String command) {
            return this.names.contains(command);
        }
    }

}

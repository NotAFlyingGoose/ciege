package com.runningmanstudios.caffeineGameEngine.console;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Console {
    //TODO make this class static, similar to GameLogger

    public JFrame frame;
    public JTextPane console;
    public JTextField input;
    public JScrollPane scrollpane;

    public StyledDocument document;

    boolean trace = false;

    ArrayList<String> recentCommands = new ArrayList<String>();
    int recentUsedId = 0;
    int recentUsedMax = 10;

    public Console() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        this.frame = new JFrame();
        this.frame.setTitle("Console");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.console = new JTextPane();
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

        this.input.addActionListener(e -> {
            String text = Console.this.input.getText();
            if (text.length() > 1) {
                this.recentCommands.add(text);
                this.recentUsedId = 0;
                Console.this.input.setText("");
                this.doCommand(text);
                this.scrollBottom();
                System.out.println(this.recentCommands);
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
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (Console.this.recentUsedId > 0) {
                        Console.this.recentUsedId--;
                    }
                    Console.this.input.setText(Console.this.recentCommands.get(Console.this.recentCommands.size() - 1 - Console.this.recentUsedId));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        this.scrollpane = new JScrollPane(this.console);
        this.scrollpane.setBorder(null);
        this.scrollpane.setOpaque(false);
        this.scrollpane.getViewport().setOpaque(false);

        this.frame.add(this.input, BorderLayout.SOUTH);
        this.frame.add(this.scrollpane, BorderLayout.CENTER);

        this.frame.getContentPane().setBackground(new Color(50, 50, 50));

        this.frame.setSize(660, 350);
        this.frame.setLocationRelativeTo(null);

        this.frame.setResizable(false);
        this.frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Console();
    }

    public void scrollTop() {
        this.console.setCaretPosition(0);
    }

    public void scrollBottom() {
        this.console.setCaretPosition(this.console.getDocument().getLength());
    }

    public void print(String text, boolean trace) {
        this.print(text, trace, new Color(255, 255, 255));
    }

    public void print(String text, boolean trace, Color color) {
        Style style = this.console.addStyle("Style", null);
        StyleConstants.setForeground(style, color);

        if (trace) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();
            String caller = elements[0].getClassName();
            text = caller + " -> " + text;
        }

        try {
            this.document.insertString(this.document.getLength(), text, style);
        } catch (Exception e) {
        }
    }

    public void println(String text, boolean trace) {
        this.println(text, trace, new Color(255, 255, 255));
    }

    public void println(String text, boolean trace, Color color) {
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


        try {
            switch (command) {
                case "clear" -> this.clear();
                case "popup" -> {
                    StringBuilder message = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        message.append(args[i]);
                        if (i != args.length - 1) {
                            message.append(" ");
                        }
                    }
                    this.println("Showing Popup with text : " + message, this.trace, new Color(155, 155, 255));
                    JOptionPane.showMessageDialog(null, message, "Popup", JOptionPane.INFORMATION_MESSAGE);
                }
                case "trace" -> {
                    if (args[0].equalsIgnoreCase("true")) {
                        this.trace = true;
                    } else if (args[0].equalsIgnoreCase("false")) {
                        this.trace = false;
                    }
                }
                default -> this.println(text, this.trace);
            }
        } catch (Exception e) {
            this.println("Error -> " + e.getMessage(), this.trace, new Color(255, 155, 155));
        }
    }

}

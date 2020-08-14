package com.runningmanstudios.caffeineGameEngine.console.script.window;

import com.formdev.flatlaf.FlatLightLaf;
import com.runningmanstudios.caffeineGameEngine.console.script.CScript;
import com.runningmanstudios.caffeineGameEngine.console.script.ScriptManager;
import com.runningmanstudios.caffeineGameEngine.console.script.OutPutter;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class CScriptWin extends JFrame {
    JButton run;
    JButton stop;
    JTextPane output;
    OutPutter outputter = new OutPutter() {
        @Override
        public CScript getScript(String packageName) {
            return manager.getScript(packageName);
        }
        @Override
        public void print(Object object) {
            CScriptWin.this.print(output, object.toString(), false, null, null);
        }

        @Override
        public void println(Object object) {
            CScriptWin.this.println(output, object.toString(), false, null, null);
        }

        @Override
        public void printError(Exception exception) {
            CScriptWin.this.println(output, "Error -> " + exception.getMessage(), false, new Color(255, 155, 155), new Color(155, 55, 55));
        }
    };
    ScriptManager manager = new ScriptManager();

    final Map<String, JTextPane> texts = new HashMap<>();
    final StyleContext cont = StyleContext.getDefaultStyleContext();
    final AttributeSet grey = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(155, 155, 155));
    final AttributeSet brown = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(155, 55, 55));
    final AttributeSet green = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(55, 155, 55));
    final AttributeSet red = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
    final AttributeSet blue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(55, 55, 255));
    final AttributeSet black = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);

    private static final Icon icon = UIManager.getIcon("FileView.fileIcon");
    public JTabbedPane tabs;
    public JButton pushToSystem;
    private int currentTabIndex = 0, previousTabIndex = 0;
    private CScript curScript;

    public CScriptWin() {
        FlatLightLaf.install();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("CScript Editor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(null);
        tabs = new JTabbedPane();

        tabs.addTab("+", null);
        tabs.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                final int index = tabs.getUI().tabForCoordinate(tabs, e.getX(), e.getY());
                if (index > 0) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        final JPopupMenu popupMenu = new JPopupMenu();

                        final JMenuItem addNew = new JMenuItem("Add new");
                        addNew.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                askForText(tabs, "Add Script", "Script Name:");
                            }
                        });
                        popupMenu.add(addNew);

                    /*final JMenuItem rename = new JMenuItem("Rename");
                    rename.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String newName = scriptWindow.askForNewName();
                            if (newName.equals("")) return;
                            tabbedPane.setTitleAt(index, newName);
                        }
                    });
                    popupMenu.add(rename);*/

                        final JMenuItem close = new JMenuItem("Close");
                        close.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                tabs.removeTabAt(index);
                            }
                        });
                        popupMenu.add(close);

                        final Rectangle tabBounds = tabs.getBoundsAt(index);
                        popupMenu.show(tabs, tabBounds.x, tabBounds.y + tabBounds.height);
                    }
                }
            }
        });
        tabs.addChangeListener(e -> {
            System.out.println(manager);
            JTabbedPane tabbedPane = (JTabbedPane) e.getSource();

            if (tabbedPane.getTabCount() < 2) {
                dispose();
                return;
            }
            previousTabIndex = currentTabIndex;
            currentTabIndex = tabbedPane.getSelectedIndex();

            if (tabbedPane.getSelectedIndex() == tabbedPane.indexOfTab("+")) {
                askForText(tabbedPane, "Add Script", "Script Name:");
            }
        });
        addScript("New Script");
        tabs.setSelectedIndex(1);

        run = new JButton("▶");
        run.setForeground(new Color(55, 155, 55));
        run.setBackground(getContentPane().getBackground());
        run.setFont(new Font(run.getFont().getFontName(), Font.BOLD, 15));
        run.setBorder(null);
        run.addActionListener(e -> {
            for (String title : texts.keySet()) {
                manager.useScript(title, new CScript(outputter, texts.get(title).getText()));
            }
            println(output, "Running Script \""+tabs.getTitleAt(tabs.getSelectedIndex())+"\"", false, null, null);
            runScript(manager.getScript(tabs.getTitleAt(tabs.getSelectedIndex())));
        });

        stop = new JButton("■");
        stop.setForeground(new Color(155, 55, 55));
        stop.setBackground(getContentPane().getBackground());
        stop.setOpaque(false);
        stop.setFont(new Font(stop.getFont().getFontName(), Font.BOLD, 30));
        stop.setBorder(null);
        stop.setEnabled(false);
        stop.addActionListener(e -> stopScript());

        output = new JTextPane() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getUI().getPreferredSize(this).width
                        <= getParent().getSize().width;
            }
        };
        output.setFont(new Font("Courier New", Font.BOLD, 14));
        output.setForeground(new Color(55, 55, 55));
        output.setBackground(new Color(155, 155, 155));
        output.setEditable(false);

        JScrollPane outputScroll = new JScrollPane(output);
        outputScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outputScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        outputScroll.setOpaque(false);
        outputScroll.getViewport().setOpaque(false);

        JSplitPane editor = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabs, outputScroll);
        editor.setOneTouchExpandable(true);
        editor.setDividerLocation(500);

        final JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

        int space = 25;
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                run.setBounds(getWidth()-space*3, 0, space, space);
                stop.setBounds(getWidth()-space*2, 0, space, space);
                separator.setBounds(0, space-2, getWidth(), getWidth());
                editor.setBounds(0, space, getWidth(), getHeight()-space);
            }
        });

        run.setBounds(850-space*3, 0, space, space);
        stop.setBounds(850-space*2, 0, space, space);
        separator.setBounds(0, space-2, 850, 850);
        editor.setBounds(0, space, 850, 680-space);
        add(run);
        add(stop);
        add(separator);
        add(editor);

        setSize(850, 680);
        setLocationRelativeTo(null);

        setResizable(true);

        setVisible(true);
    }

    public void runScript(CScript script) {
        stop.setEnabled(true);
        curScript = script;
        curScript.run();
        stopScript();
    }

    public void stopScript() {
        curScript.getParser().stop();
        stop.setEnabled(false);
    }

    public void askForText(JTabbedPane tabbedPane, String title, String text) {
        String s = JOptionPane.showInputDialog(
                CScriptWin.this,
                text,
                title,
                JOptionPane.PLAIN_MESSAGE);
        if (s != null) {
            if (s.length() > 0) {
                CScriptWin.this.addScript(s);
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                return;
            }

            //custom title, error icon
            JOptionPane.showMessageDialog(CScriptWin.this,
                    "You must provide text",
                    title,
                    JOptionPane.ERROR_MESSAGE);
        }
        tabbedPane.setSelectedIndex(previousTabIndex);
    }

    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    public void addScript(String title) {
        DefaultStyledDocument consoleStyle = new DefaultStyledDocument() {
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        if (text.substring(wordL, wordR).matches("(\\W)*(\\\\[nts\\\\]|/\\*|\\*/)"))
                            setCharacterAttributes(wordL, wordR - wordL, brown, false);
                        else if (text.substring(wordL, wordR).matches("(\\W)*(string|number|boolean|object|var)"))
                            setCharacterAttributes(wordL, wordR - wordL, green, false);
                        else if (text.substring(wordL, wordR).matches("(\\W)*(func|struct|use)"))
                            setCharacterAttributes(wordL, wordR - wordL, red, false);
                        else if (text.substring(wordL, wordR).matches("(\\W)([0-9]+)(\\.[0-9]+)*?"))
                            setCharacterAttributes(wordL, wordR - wordL, blue, false);
                        else
                            setCharacterAttributes(wordL, wordR - wordL, black, false);
                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches("(\\W)*(\\\\[nts\\\\]|\\/\\*|\\*\\/)")) {
                    setCharacterAttributes(before, after - before, brown, false);
                } else if (text.substring(before, after).matches("(\\W)*(string|number|boolean|object|var)")) {
                    setCharacterAttributes(before, after - before, green, false);
                } else if (text.substring(before, after).matches("(\\W)*(func|struct|use)")) {
                    setCharacterAttributes(before, after - before, red, false);
                } else if (text.substring(before, after).matches("(\\W)([0-9]+)(\\.[0-9]+)*?")) {
                    setCharacterAttributes(before, after - before, blue, false);
                } else {
                    setCharacterAttributes(before, after - before, black, false);
                }
            }
        };
        JTextPane console = new JTextPane(consoleStyle) {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getUI().getPreferredSize(this).width
                        <= getParent().getSize().width;
            }
        };
        console.setFont(new Font("Courier New", Font.PLAIN, 14));

        JScrollPane scrollpane = new JScrollPane(console);
        scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setOpaque(false);
        scrollpane.getViewport().setOpaque(false);

        tabs.addTab(title, icon, scrollpane);
        texts.put(title, console);

        int index = tabs.indexOfTab(title);
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        JButton btnClose = new JButton("x");
        btnClose.setBorder(null);
        btnClose.setOpaque(false);
        btnClose.setForeground(Color.red);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        pnlTab.add(lblTitle, gbc);

        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(btnClose, gbc);

        tabs.setTabComponentAt(index, pnlTab);

        btnClose.addActionListener(new ActionListener() {
            private final int index = tabs.indexOfTab(title);

            public void actionPerformed(ActionEvent evt) {
                if (index >= 1) {
                    manager.removeScript(tabs.getTitleAt(index));
                    tabs.removeTabAt(index);
                    btnClose.removeActionListener(this);
                }

            }
        });

    }

    private void println(JTextPane pane, Object text, boolean trace, Color foreground, Color background) {
        print(pane, text+"\n", trace, foreground, background);
    }

    private void print(JTextPane pane, Object text, boolean trace, Color foreground, Color background) {
        Style style = pane.addStyle("HtmlStyle", null);

        if (trace) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();
            String caller = elements[0].getClassName();
            text = caller + " -> " + text;
        }

        try {
            if (foreground != null) StyleConstants.setForeground(style, foreground);
            else StyleConstants.setForeground(style, pane.getForeground());
            if (background != null) StyleConstants.setBackground(style, background);
            else StyleConstants.setBackground(style, pane.getBackground());

            pane.getStyledDocument().insertString(pane.getStyledDocument().getLength(), text.toString(), style);

            scrollBottom(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scrollTop(JTextPane pane) {
        pane.setCaretPosition(0);
    }

    private void scrollBottom(JTextPane pane) {
        pane.setCaretPosition(pane.getDocument().getLength());
    }

}


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import sun.audio.*;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Raymond
 */
public final class ChatClient extends Thread implements ActionListener, KeyListener {

    String lineSeparator = System.getProperty("line.separator");
    String messageToAll, name;
    JPanel mainPanel;
    JLabel label_1, label_2, label_3, label_4, label_5, charCnt;
    JMenuBar menuBar;
    JMenu tool, help;
    JMenuItem save, log, exit, about, information;
    JTextField field_2;
    JTextArea area_2;
    JTextPane chatPane;
    JCheckBox cb1;
    JButton button_1, button_2, button_3, button_4, button_5, button_6, button_7;
    JScrollPane scroll, scroll2, scroll3;
    Icon send = new ImageIcon("Resources/Image/send.gif");
    Icon sv = new ImageIcon("Resources/Image/save.png");
    Icon lg = new ImageIcon("Resources/Image/logoff.gif");
    Icon ext = new ImageIcon("Resources/Image/exit.png");
    Icon About = new ImageIcon("Resources/Image/about.png");
    Icon clear = new ImageIcon("Resources/Image/clear.png");
    Icon info = new ImageIcon("Resources/Image/help.png");
    Font small = new Font("Sans Serif", Font.ITALIC, 9);
    Font prv = new Font("Helvetica", Font.ITALIC, 14);
    String strg[] = new String[10];
    int arr, ctr2 = 0;
    int charMax = 150;
    boolean ignoreInput = false;

    JList list;
    sortedListModel listModel;

    Socket client;
    BufferedReader fromServer;
    PrintStream toServer;

    Login login;
    time2 obj2 = new time2(this);
    Thread thr = new Thread(this);

    TrayIcon trayIcon;

    /**
     * @param name
     * @param IP
     * @param login
     */
    public ChatClient(String name, String IP, Login login) {
        super(name);
        try {
//            client = new Socket(IP, 8888);
            /////////////used with dynamic IP address\\\\\\\\\\\\
            client = new Socket();
            client.connect(new InetSocketAddress(IP, 8888), 5);
            /////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            toServer = new PrintStream(client.getOutputStream());
            fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            toServer.println("##" + name);
        } catch (Exception e) {
            chatPane.setText("no server detected!");
            JOptionPane.showMessageDialog(null, "      No server running! " + lineSeparator + "Sorry, You've to log out.... ");
            JOptionPane.showMessageDialog(null, "Error: " + e);
            System.exit(0);
        }
        createAndShowGUI(name);
    }

    @Override
    public void run() {
        while (thr != null) {
            try {
                FileInputStream fis1 = new FileInputStream(new File("Resources/Sound/explode3.wav"));
                AudioStream as1 = new AudioStream(fis1);
                AudioData ad1 = as1.getData();
                AudioDataStream aSound1 = new AudioDataStream(ad1);

                FileInputStream fis2 = new FileInputStream(new File("Resources/Sound/explode4.wav"));
                AudioStream as2 = new AudioStream(fis2);
                AudioData ad2 = as2.getData();
                AudioDataStream aSound2 = new AudioDataStream(ad2);

                FileInputStream fis3 = new FileInputStream(new File("Resources/Sound/explode5.wav"));
                AudioStream as3 = new AudioStream(fis3);
                AudioData ad3 = as3.getData();
                AudioDataStream aSound3 = new AudioDataStream(ad3);

                FileInputStream fis4 = new FileInputStream(new File("Resources/Sound/doorbell.wav"));
                AudioStream as4 = new AudioStream(fis4);
                AudioData ad4 = as4.getData();
                AudioDataStream aSound4 = new AudioDataStream(ad4);

                FileInputStream fis5 = new FileInputStream(new File("Resources/Sound/zombie.wav"));
                AudioStream as5 = new AudioStream(fis5);
                AudioData ad5 = as5.getData();
                AudioDataStream aSound5 = new AudioDataStream(ad5);

                FileInputStream fis6 = new FileInputStream(new File("Resources/Sound/msg.wav"));
                AudioStream as6 = new AudioStream(fis6);
                AudioData ad6 = as6.getData();
                AudioDataStream aSound6 = new AudioDataStream(ad6);

                FileInputStream fis7 = new FileInputStream(new File("Resources/Sound/prmsg.wav"));
                AudioStream as7 = new AudioStream(fis7);
                AudioData ad7 = as7.getData();
                AudioDataStream aSound7 = new AudioDataStream(ad7);

                FileInputStream fis8 = new FileInputStream(new File("Resources/Sound/useronline.wav"));
                AudioStream as8 = new AudioStream(fis8);
                AudioData ad8 = as8.getData();
                AudioDataStream aSound8 = new AudioDataStream(ad8);

                FileInputStream fis9 = new FileInputStream(new File("Resources/Sound/useroffline.wav"));
                AudioStream as9 = new AudioStream(fis9);
                AudioData ad9 = as9.getData();
                AudioDataStream aSound9 = new AudioDataStream(ad9);

                FileInputStream fis10 = new FileInputStream(new File("Resources/Sound/aom.wav"));
                AudioStream as10 = new AudioStream(fis10);
                AudioData ad10 = as10.getData();
                AudioDataStream aSound10 = new AudioDataStream(ad10);

                String receiver = fromServer.readLine();
                if (receiver.startsWith("#E")) {
                    String msg = receiver.substring(2);
                    listModel.removeElement(msg);
                } else if (receiver.startsWith("#L")) {
                    String msg = receiver.substring(2);
                    listModel.add(msg);
                } else if (receiver.startsWith("#RCN")) {
                    String msg = receiver.substring(4);
                    this.name = msg;
                } else if (receiver.startsWith("#N")) {
                    String msg = receiver.substring(2);
                    trayIcon.displayMessage("ChatClient", msg, TrayIcon.MessageType.NONE);
                    StyledDocument doc = chatPane.getStyledDocument();
                    SimpleAttributeSet style = new SimpleAttributeSet();
                    style.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
                    StyleConstants.setForeground(style, Color.RED);
                    try {
                        doc.insertString(doc.getLength(), msg + lineSeparator, style);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    AudioPlayer.player.start(aSound9);
                } else if (receiver.startsWith("SP1")) {
                    AudioPlayer.player.start(aSound4);
                } else if (receiver.startsWith("SP2")) {
                    AudioPlayer.player.start(aSound3);
                } else if (receiver.startsWith("SP3")) {
                    AudioPlayer.player.start(aSound1);
                } else if (receiver.startsWith("SP4")) {
                    AudioPlayer.player.start(aSound2);
                } else if (receiver.startsWith("SP5")) {
                    AudioPlayer.player.start(aSound5);
                } else if (receiver.startsWith("SP6")) {
                    AudioPlayer.player.start(aSound6);
                } else if (receiver.startsWith("SP7")) {
                    AudioPlayer.player.start(aSound7);
                } else if (receiver.startsWith("SP8")) {
                    AudioPlayer.player.start(aSound8);
                } else if (receiver.startsWith("SP9")) {
                    AudioPlayer.player.start(aSound9);
                } else if (receiver.startsWith("SA1")) {
                    AudioPlayer.player.start(aSound10);
                } else if (receiver.startsWith("#T")) {
                    @SuppressWarnings("LocalVariableHidesMemberVariable")
                    String name = receiver.substring(2);
                    String msg = fromServer.readLine();
                    trayIcon.displayMessage("ChatClient", name + msg, TrayIcon.MessageType.NONE);
                    StyledDocument doc = chatPane.getStyledDocument();
                    SimpleAttributeSet style = new SimpleAttributeSet();
                    style.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
                    StyleConstants.setForeground(style, Color.RED);
                    try {
                        doc.insertString(doc.getLength(), name + msg + lineSeparator, style);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    AudioPlayer.player.start(aSound8);
                } else if (receiver.startsWith("all")) {
                    String msg = receiver.substring(3);
                    trayIcon.displayMessage("ChatClient", msg, TrayIcon.MessageType.NONE);
                    StyledDocument doc = chatPane.getStyledDocument();
                    SimpleAttributeSet style = new SimpleAttributeSet();
                    style.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
                    StyleConstants.setForeground(style, Color.BLACK);
                    try {
                        doc.insertString(doc.getLength(), msg + lineSeparator, style);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    AudioPlayer.player.start(aSound6);
                } else if (receiver.startsWith("BC")) {
                    StringBuilder sb = null;
                    String msg = receiver.substring(3);
                    String temp;
                    while ((temp = fromServer.readLine()) != null) {
                        if (temp.startsWith("START")) {
                            sb = new StringBuilder();
                        } else if (temp.equals("END")) {
                            break;
                        } else {
                            sb.append(temp + "\n");
                        }
                        trayIcon.displayMessage("Broadcast", msg + lineSeparator + sb, TrayIcon.MessageType.NONE);
                    }
                    StyledDocument doc = chatPane.getStyledDocument();
                    SimpleAttributeSet style = new SimpleAttributeSet();
                    style.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
                    StyleConstants.setForeground(style, Color.RED);
                    try {
                        doc.insertString(doc.getLength(), msg + lineSeparator + sb, style);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    AudioPlayer.player.start(aSound7);
                } else if (receiver.startsWith("#Pr")) {
                    String msg = receiver.substring(3);
                    StringBuilder sb = null;
                    String temp;
                    while ((temp = fromServer.readLine()) != null) {
                        if (temp.startsWith("START")) {
                            sb = new StringBuilder();
                        } else if (temp.equals("END")) {
                            break;
                        } else {
                            sb.append(temp + "\n");
                        }
                        trayIcon.displayMessage("Private message received!", msg + lineSeparator + sb.toString().trim(), TrayIcon.MessageType.NONE);
                    }
                    StyledDocument doc = chatPane.getStyledDocument();
                    SimpleAttributeSet style = new SimpleAttributeSet();
                    style.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
                    StyleConstants.setForeground(style, Color.BLUE);
                    StyleConstants.setFontFamily(style, "Helvetica");
                    try {
                        doc.insertString(doc.getLength(), msg + sb.toString().trim() + lineSeparator, style);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    AudioPlayer.player.start(aSound7);
                } else if (receiver.startsWith("#name")) {
                    String msg = receiver.substring(5);
                    JOptionPane.showMessageDialog(null, "Sorry, someone've already use that name (" + msg + ")");
                    System.exit(0);
                } else if (receiver.startsWith("kick")) {
                    String msg = receiver.substring(4);
                    if (msg.equals(name)) {
                        JOptionPane.showMessageDialog(null, "Sorry, " + msg + " you have been kicked by admin..");
                        System.exit(0);
                    }
                } else if (receiver.startsWith("#SC")) {
                    throw new IllegalStateException();
                }
            } catch (IOException | HeadlessException ex) {
                JOptionPane.showMessageDialog(null, "Maximum number of users (100) has been reached!");
                System.exit(0);
            } catch (IllegalStateException e) {
                //Server Stop button will fire this\\
                String shutdownCmd = "shutdown -r -f -t 65 -c " + (char) 34 + "This computer will be restarted in 60 seconds for maintenance. Please save your work now. IT Dept." + (char) 34;
                String updateclient = "xcopy /s /e /d X:\\ChatClient\\* D:\\Softwares\\ChatClient\\ /Y";
                try {
                    client.close();
                    Process child = Runtime.getRuntime().exec(shutdownCmd);
                    Process child2 = Runtime.getRuntime().exec(updateclient);
                } catch (IOException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(0);
            }
        }
    }

    public void createAndShowGUI(final String name) {
        this.name = name;
        final JFrame frame;
        frame = new JFrame("Client - " + name);
        Image bd = Toolkit.getDefaultToolkit().getImage("Resources/Image/client.png");
        frame.setIconImage(bd);
//        frame.getContentPane().add(new init().mainPanel);
//        frame.setDefaultCloseOperation(JFrame.ICONIFIED);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setResizable(false);
//        defaultItem1.addActionListener(exitListener);
//        defaultItem2.addActionListener(restoreListener);
//        this.login = login;
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
        mainPanel = new JPanel();
        menuBar = new JMenuBar();
        tool = new JMenu("Tools");
        help = new JMenu("Help");
        save = new JMenuItem("Save");
        log = new JMenuItem("Login as..");
        exit = new JMenuItem("Exit");
        about = new JMenuItem("About");
        information = new JMenuItem("Information");
        label_1 = new JLabel("Online Users");
//        label_2 = new JLabel("Messages Box");
        label_3 = new JLabel("Messages: ");
        label_4 = new JLabel("Public Message");
        label_5 = new JLabel("");
        charCnt = new JLabel("");
        field_2 = new JTextField(29);
        field_2.setToolTipText("Date & Time");
        field_2.setEditable(false);
        field_2.setFocusable(false);
        cb1 = new JCheckBox("private");
        cb1.setToolTipText("Private message");
        chatPane = new JTextPane();
        chatPane.setText("");
        area_2 = new JTextArea(1, 10);
        area_2.setLineWrap(true);
        area_2.setToolTipText("type your message here..");
//        area_2.setCaretPosition(0);
        //chatPane.setVisible(true);
        listModel = new sortedListModel();
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setToolTipText("Client available");
        scroll = new JScrollPane(chatPane);
        DefaultCaret caret = (DefaultCaret) chatPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll2 = new JScrollPane(area_2);
        scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll3 = new JScrollPane(list);
        scroll3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //mainPanel.add(new JScrollPane(area_2));

//        scroll2.setViewportView(area_2);
        button_1 = new JButton("SEND");
        button_1.setToolTipText("send message");
        //button_2 = new JButton("");
        //button_2.setToolTipText("save conversation");
        //button_3 = new JButton("");
        //button_3.setToolTipText("log out");
        //button_4 = new JButton("");
        //button_4.setToolTipText("about");
        //button_5 = new JButton("");
        //button_5.setToolTipText("clear text area");
        //button_6 = new JButton("");
        //button_6.setToolTipText("login as..");
        //button_7 = new JButton("");
        //button_7.setToolTipText("information");

        frame.add(mainPanel);
        mainPanel.add(charCnt);
//        menuBar.add(tool);
//        menuBar.add(help);
//        tool.add(log);
//        tool.add(save);
//        tool.add(exit);
//        help.add(about);
//        help.add(information);
        mainPanel.add(label_1);
//        mainPanel.add(label_2);
        mainPanel.add(label_3);
        mainPanel.add(label_4);
        mainPanel.add(label_5);
//        mainPanel.add(area_2);
        mainPanel.add(field_2);
//        mainPanel.add(chatPane);
        mainPanel.add(scroll);
        mainPanel.add(scroll2);
        //mainPanel.add(list);
        mainPanel.add(scroll3);
        mainPanel.add(button_1);
//        mainPanel.add(button_2);
//        mainPanel.add(button_3);
//        mainPanel.add(button_4);
//        mainPanel.add(button_5);
//        mainPanel.add(button_6);
//        mainPanel.add(button_7);
        mainPanel.add(cb1);
        cb1.setFont(prv);
        button_1.setIcon(send);
//        button_2.setIcon(sv);
//        button_3.setIcon(ext);
//        button_4.setIcon(About);
//        button_5.setIcon(clear);
//        button_6.setIcon(lg);
//        button_7.setIcon(info);

        mainPanel.setLayout(null);
        menuBar.setBounds(0, 0, 600, 25);
        //Users
        label_1.setBounds(455, 5, 150, 20);
        //Messages Box
        //label_2.setBounds(245, 317, 150, 20);
        //Messages
        //label_3.setBounds(5, 335, 90, 25);
        //Public Messages
        label_4.setBounds(140, 5, 300, 20);
        //time
        label_5.setBounds(485, 297, 100, 25);
        //Characters count
        charCnt.setBounds(365, 358, 120, 15);
        charCnt.setFont(small);
        scroll.setBounds(5, 25, 390, 300);
        //chatPane.setFocusable(false);
        chatPane.setEditable(false);
        scroll2.setBounds(5, 325, 485, 48);
        scroll3.setBounds(400, 30, 185, 260);
//        list.setBounds(400, 30, 185, 260);

        //area_2.setBounds(15, 375, 412, 25);
        field_2.setBounds(477, 300, 112, 25);
        cb1.setBounds(398, 300, 70, 20);

        //button
        button_1.setBounds(490, 326, 100, 30);
//        button_2.setBounds(25, 25, 24, 24);
//        button_3.setBounds(50, 25, 24, 24);
//        button_4.setBounds(75, 25, 24, 24);
//        button_5.setBounds(400, 270, 70, 50);
//        button_6.setBounds(0, 25, 24, 24);
//        button_7.setBounds(100, 25, 24, 24);

        button_1.addActionListener(this);
        button_1.addKeyListener(this);
        button_1.setMnemonic(KeyEvent.VK_ENTER);
//        button_2.addActionListener(this);
//        button_2.addKeyListener(this);
//        button_2.setMnemonic(KeyEvent.VK_S);
//        button_3.addActionListener(this);
//        button_3.addKeyListener(this);
//        button_3.setMnemonic(KeyEvent.VK_E);
//        button_4.addActionListener(this);
//        button_4.addKeyListener(this);
//        button_4.setMnemonic(KeyEvent.VK_A);
//        button_5.addActionListener(this);
//        button_5.addKeyListener(this);
//        button_5.setMnemonic(KeyEvent.VK_C);
//        button_6.addActionListener(this);
//        button_6.addKeyListener(this);
//        button_6.setMnemonic(KeyEvent.VK_L);
//        button_7.addActionListener(this);
//        button_7.addKeyListener(this);
//        button_7.setMnemonic(KeyEvent.VK_I);
//        tool.setMnemonic(KeyEvent.VK_T);
//        log.addActionListener(this);
//        log.setMnemonic(KeyEvent.VK_L);
//        exit.addActionListener(this);
//        exit.setMnemonic(KeyEvent.VK_X);
//        save.addActionListener(this);
//        save.setMnemonic(KeyEvent.VK_S);
//        help.setMnemonic(KeyEvent.VK_H);
//        about.addActionListener(this);
//        information.addActionListener(this);
        area_2.addKeyListener(this);
        field_2.addKeyListener(this);
        list.addKeyListener(this);
        cb1.addActionListener(this);
        cb1.addKeyListener(this);
        cb1.setMnemonic(KeyEvent.VK_P);
        area_2.requestFocus(true);
        obj2.start();
        try {
            thr.start();
//            createAndShowUI(name);
        } catch (Exception e) {
        }
//        final ChatClient cl = new ChatClient();
        if (!SystemTray.isSupported()) {
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            System.out.println("SystemTray is not supported");
            return;
        }

        SystemTray tray;
        PopupMenu popup = new PopupMenu();
//        MenuItem defaultItem1 = new MenuItem("Restore");
//        MenuItem defaultItem2 = new MenuItem("Restore");

        Image trayImage = Toolkit.getDefaultToolkit().getImage("Resources/Image/client.png");
//        popup.add(defaultItem1);
//        defaultItem1.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                createAndShowUI(name);
//                fr.setVisible(true);
//            }
//        });
//        popup.add(defaultItem2);
        tray = SystemTray.getSystemTray();
        trayIcon = new TrayIcon(trayImage, "ChatClient by GACH", popup);
        trayIcon.setImageAutoSize(true);
        try {
            tray.add(trayIcon);
        } catch (AWTException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
//                createAndShowUI(name);
            }
        });
        String msg = "Welcome, " + name
                + "\n" + "Welcome to ChatClient!"
                + "\n" + "Developed by GACH";
//        trayIcon.displayMessage("ChatClient", msg, TrayIcon.MessageType.NONE);
//
    }

//    public void append(Color c, String s) {
//        StyleContext sc = StyleContext.getDefaultStyleContext();
//        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
//                StyleConstants.Foreground, c);
//        setCharacterAttributes(aset, false);
//    }
    @SuppressWarnings("empty-statement")
    public void store(String s) {
        for (int ctr = arr; ctr > 0; strg[ctr] = strg[ctr - 1], ctr--);
        strg[0] = s;
        if (arr < 9) {
            arr++;
        }
    }

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String arg[]) {
        new Login();
    }

    @Override
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void actionPerformed(ActionEvent AE) {

        Object src = AE.getSource();
        if (src == button_1) {
            if (area_2.getText().length() != 0 && area_2.getText().trim().length() > 0) {
                messageToAll = area_2.getText();
                if (messageToAll.startsWith("/")) {
                    check(messageToAll);
                    area_2.setText("");
                } else if (cb1.isSelected() && list.getSelectedValue() != null) {
                    String dstn = list.getSelectedValue().toString();
                    if (!dstn.equals(name)) {
                        StyledDocument doc = chatPane.getStyledDocument();
                        SimpleAttributeSet style = new SimpleAttributeSet();
                        style.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
                        StyleConstants.setForeground(style, Color.BLUE);
                        try {
                            doc.insertString(doc.getLength(), obj2.strTime2 + " " + name + "> " + messageToAll + lineSeparator, style);
                        } catch (BadLocationException ex) {
                            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        toServer.println("#Pr" + dstn);
                        toServer.println("START");
                        toServer.println(messageToAll);
                        toServer.println("END");
                        area_2.setText("");
                    }
                    if (dstn.equals(name)) {
                        JOptionPane.showMessageDialog(null, "You can't send message to yourself.");
                        area_2.setText("");
                    }
                } else if (!cb1.isSelected()) {
                    toServer.println("all" + name + "> " + messageToAll);
                    area_2.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "You have not selected any name from list.");
                }
                store(messageToAll);
                ctr2 = 0;
            }
            area_2.requestFocus();
        } /*else if (src == button_2) {
         try {
         JFileChooser jfc = new JFileChooser();
         jfc.showSaveDialog(frame);
         File file = jfc.getSelectedFile();
         String fileName = file.getPath();
         String textfile = chatPane.getText();
         RandomAccessFile logFile = new RandomAccessFile(fileName + ".txt", "rw");
         logFile.seek(logFile.length());
         logFile.writeBytes(textfile);
         chatPane.append("data saved!" + lineSeparator);
         } catch (HeadlessException | IOException e) {
         chatPane.append("data not saved." + lineSeparator);
         }
         area_2.requestFocus();
         } */ else if (src == button_3) {
            System.exit(0);
        } /*else if (src == button_4) {
         new about();
         } else if (src == button_5) {
         chatPane.setText("");
         area_2.requestFocus();
         } else if (src == button_6) {
         frame.dispose();
         toServer.println("out1" + this.name);
         new Login();
         area_2.requestFocus();
         }
         else if (src == log) {
         frame.dispose();
         toServer.println("out1" + this.name);
         new Login();
         area_2.requestFocus();
         } */ else if (src == exit) {
            System.exit(0);
        } /*else if (src == save) {
         try {
         JFileChooser jfc = new JFileChooser();
         jfc.showSaveDialog(frame);
         File file = jfc.getSelectedFile();
         String fileName = file.getPath();
         String textfile = chatPane.getText();
         RandomAccessFile logFile = new RandomAccessFile(fileName + ".txt", "rw");
         logFile.seek(logFile.length());
         logFile.writeBytes(textfile);
         chatPane.append("data saved!" + lineSeparator);
         } catch (HeadlessException | IOException e) {
         chatPane.append("data not saved." + lineSeparator);
         }
         area_2.requestFocus();
         } else if (src == about) {
         new about();
         } */ else if (src == cb1) {
            area_2.requestFocus();
        }
    }

    @Override
    public void keyTyped(KeyEvent KE) {
    }

    @Override
    public void keyPressed(KeyEvent KE) {
        int key = KE.getKeyCode();
        boolean ctrlPressed = false;
        boolean cPressed = false;
        boolean enterPressed = false;
        boolean entPressed = false;
        boolean shiftPressed = false;
        switch (key) {
            case KeyEvent.VK_ENTER:
                entPressed = true;
                break;
            case KeyEvent.VK_SHIFT:
                shiftPressed = true;
        }
        if (entPressed && shiftPressed) {
            area_2.append(lineSeparator);
            System.out.println("Blocked Enter");
            KE.consume();// Stop the event from propagating.
        }

        if (key == KeyEvent.VK_F4) {
            System.exit(0);
        }
        switch (key) {
            case KeyEvent.VK_C:
                cPressed = true;
                break;
            case KeyEvent.VK_CONTROL:
                ctrlPressed = true;
        }

        if (ctrlPressed && cPressed) {
            System.out.println("Blocked CTRl+C");
            KE.consume();// Stop the event from propagating.
        }
        switch (key) {
            case KeyEvent.VK_V:
                cPressed = true;
                break;
            case KeyEvent.VK_CONTROL:
                ctrlPressed = true;
        }

        if (ctrlPressed && cPressed) {
            System.out.println("Blocked CTRl+V");
            KE.consume();// Stop the event from propagating.
        }
        int newLen = 0;
        String charsRemaining = " characters remaining";
        // The key has just been pressed so Swing hasn't updated
        // the text area with the new KeyEvent.
        int currLen = area_2.getText().length();
        if (key == KeyEvent.VK_BACK_SPACE) {
            newLen = currLen - 1;
            ignoreInput = false;
        } else {
            newLen = currLen + 1;
        }
        if (newLen < 0) {
            newLen = 0;
        }
        if (newLen == 0) {
            charCnt.setText(charMax + " characters maximum!");
        } else if (newLen >= 0 && newLen < charMax) {
            charCnt.setText((charMax - newLen) + charsRemaining);
        } else if (newLen >= charMax) {
            ignoreInput = true;
            charCnt.setText("0 " + charsRemaining);
        }

        if (key == 10) {
            if (KE.isShiftDown()) {
                area_2.append(lineSeparator);
            } else if (area_2.getText().length() != 0 && area_2.getText().trim().length() > 0 && (!area_2.getText().contains(System.getProperty("line.separator")))) {
                messageToAll = area_2.getText();
//                toServer.println(messageToAll);
                if (messageToAll.startsWith("/")) {
                    check(messageToAll);
                    area_2.setText("");
                } else if (cb1.isSelected() && list.getSelectedValue() != null) {
                    String dstn = list.getSelectedValue().toString();
                    if (!dstn.equals(name)) {
                        StyledDocument doc = chatPane.getStyledDocument();
                        SimpleAttributeSet style = new SimpleAttributeSet();
                        style.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
                        StyleConstants.setForeground(style, Color.BLUE);
                        try {
                            doc.insertString(doc.getLength(), obj2.strTime2 + " " + name + "> " + messageToAll + lineSeparator, style);
                        } catch (BadLocationException ex) {
                            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        toServer.println("#Pr" + dstn);
                        toServer.println("START");
                        toServer.println(messageToAll.trim());
                        toServer.println("END");
                        area_2.setText("");
                    }
                    if (dstn.equals(name)) {
                        JOptionPane.showMessageDialog(null, "You can't send message to yourself man. C'mon!");
                        area_2.setText("");
                    }
                } else if (!cb1.isSelected()) {
                    toServer.println("all" + obj2.strTime2 + " " + name + " > " + messageToAll);
                    area_2.setText("");

                } else {
                    JOptionPane.showMessageDialog(null, "You have not select name from list");
                }
                store(messageToAll);
                ctr2 = 0;
            }
            area_2.requestFocus();
        }
    }

    @Override
    public void keyReleased(KeyEvent KE) {
        boolean ctrlPressed = false;
        boolean cPressed = false;
        int key = KE.getKeyCode();
        if (key == 10) {
            if (KE.isShiftDown()) {
                KE.consume();
            } else {
                area_2.setText("");
                charCnt.setText("150 characters remaining");
            }
        }
        if (ignoreInput == true) {
            area_2.setText(area_2.getText().substring(0, charMax));
            ignoreInput = false;
        }
        switch (KE.getKeyCode()) {
            case KeyEvent.VK_C:
                cPressed = false;

                break;
            case KeyEvent.VK_CONTROL:
                ctrlPressed = false;
        }

        if (ctrlPressed && cPressed) {
            System.out.println("Blocked CTRl+C");
            KE.consume();// Stop the event from propagating.
        }
        switch (KE.getKeyCode()) {
            case KeyEvent.VK_V:
                cPressed = false;

                break;
            case KeyEvent.VK_CONTROL:
                ctrlPressed = false;
        }

        if (ctrlPressed && cPressed) {
            System.out.println("Blocked CTRl+V");
            KE.consume();// Stop the event from propagating.
        }

        if (key == KeyEvent.VK_UP) {
            if (ctr2 < arr && ctr2 < 10) {
                area_2.setText(strg[ctr2++]);
            } else {
                area_2.setText("");
            }
        }

        if (key == KeyEvent.VK_DOWN) {
            if (ctr2 > 0) {
                area_2.setText(strg[(ctr2--) - 1]);
            } else {
                area_2.setText("");
                ctr2 = 0;
            }
        }
    }

    @SuppressWarnings({"ResultOfObjectAllocationIgnored", "UseSpecificCatch"})
    public void check(String field) {
        if (field.equals("/quit") || field.equals("/Quit") || field.equals("/QUIT") || field.equals("/QUit") || field.equals("/quIT")
                || field.equals("/exit") || field.equals("/Exit") || field.equals("/EXIT") || field.equals("EXit") || field.equals("/exIT")
                || field.equals("/close") || field.equals("/Close") || field.equals("/CLOSE") || field.equals("/CLose")) {
            System.exit(0);
        } else if (field.startsWith("/sound1 ")) {
            String dstn = field.substring(8);
            toServer.println("S1" + dstn);
        } else if (field.startsWith("/sound2 ")) {
            String dstn = field.substring(8);
            toServer.println("S2" + dstn);
        } else if (field.startsWith("/sound3 ")) {
            String dstn = field.substring(8);
            toServer.println("S3" + dstn);
        } else if (field.startsWith("/sound4 ")) {
            String dstn = field.substring(8);
            toServer.println("S4" + dstn);
        } else if (field.startsWith("/zombie ")) {
            String dstn = field.substring(8);
            toServer.println("S5" + dstn);
        } else if (field.startsWith("/sound6 ")) {
            String dstn = field.substring(8);
            toServer.println("S6" + dstn);
        } else if (field.startsWith("/sound7 ")) {
            String dstn = field.substring(8);
            toServer.println("S7" + dstn);
        } else if (field.startsWith("/sound8 ")) {
            String dstn = field.substring(8);
            toServer.println("S8" + dstn);
        } else if (field.startsWith("/sound9 ")) {
            String dstn = field.substring(8);
            toServer.println("S9" + dstn);
        } else if (field.startsWith("/sound5 ")) {
            String dstn = field.substring(8);
            toServer.println("SA1" + dstn);
        } else if (field.startsWith("/nick")) {
            String nickname = field.substring(6);
            toServer.println("out1" + name + "/nick" + nickname);
            listModel.removeElement(name);
        } else if (field.equals("/save") || field.equals("/SAVE") || field.equals("/Save")) {
            try {
                JFileChooser jfc = new JFileChooser();
//                jfc.showSaveDialog(frame);
                File file = jfc.getSelectedFile();
                String fileName = file.getName();
                String textfile = chatPane.getText();
                RandomAccessFile logFile = new RandomAccessFile(fileName + ".txt", "rw");
                logFile.seek(logFile.length());
                logFile.writeBytes(textfile);
                StyledDocument doc = chatPane.getStyledDocument();
                Style style = chatPane.addStyle("I'm a Style", null);

                StyleConstants.setForeground(style, Color.RED);
                try {
                    doc.insertString(doc.getLength(), "Data Saved to " + fileName + ".", style);
                } catch (BadLocationException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
//                chatPane.append("data saved!");
            } catch (Exception e) {
//                chatPane.append("data not saved." + lineSeparator);
            }
            area_2.requestFocus();
        } else if (field.equals("/clear") || field.equals("/Clear") || field.equals("/cLEAR") || field.equals("/CLear")) {
            chatPane.setText("");
        } else {
            StyledDocument doc = chatPane.getStyledDocument();
            Style style = chatPane.addStyle("I'm a Style", null);

            StyleConstants.setForeground(style, Color.RED);
            try {
                doc.insertString(doc.getLength(), "Invalid command. Please try again. Type /help for command.\n", style);
            } catch (BadLocationException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
//            chatPane.append("invalid command." + lineSeparator);
        }
    }
}


import sun.audio.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.text.DefaultCaret;

//************************Server Engine************************//
////////////////////////Object Declaration\\\\\\\\\\\\\\\\\\\\\\\
public class ChatServer extends Thread implements ActionListener, Runnable {

    String lineSeparator = System.getProperty("line.separator");
    public JFrame frame;
    JPanel mainPanel;
    JLabel label_1, label_2, label_3;
    JList list;
    JTextArea area_1, area_2;
    JButton button_1, button_2, button_3;
    JScrollBar scroll;
    Color color = new Color(255, 255, 255);
    sortedListModel listModel;

    ServerSocket server;
    Socket socket;
    boolean condition = false;
    String messageToAll;
    PrintStream toAll;
    BufferedReader fromAll;
    Thread thr;
    ThreadForClient ThreadClass[] = new ThreadForClient[101];
    int ctr, count = 0;

    String strTime;
    time obj = new time(this);
    ServerMsg obj2;

    ////////////////////Object Initialization\\\\\\\\\\\\\\\\\\\\
    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    public ChatServer() {
        Image bd = Toolkit.getDefaultToolkit().getImage("Resources/image/server.png");
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
        frame = new JFrame("Server engine");
        frame.setIconImage(bd);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(5, 5);
        mainPanel = new JPanel();
        label_1 = new JLabel("");
        label_2 = new JLabel("");
        label_3 = new JLabel("Developed by GACH.");
        list = new JList();
        listModel = new sortedListModel();
        list.setModel(listModel);
        area_1 = new JTextArea();
        scroll = new JScrollBar();
        button_1 = new JButton("START");
        button_2 = new JButton("STOP");
        button_3 = new JButton("");

        frame.getContentPane().add(mainPanel);
        mainPanel.add(label_1);
        mainPanel.add(label_2);
        mainPanel.add(label_3);
        mainPanel.add(area_1);
        mainPanel.add(button_1);
        mainPanel.add(button_2);
        button_2.setEnabled(false);
        mainPanel.add(button_3);
        mainPanel.add(list);
        mainPanel.setLayout(null);
        label_1.setBounds(130, 108, 150, 25);
        label_2.setBounds(5, 95, 150, 25);
        label_3.setBounds(3, 108, 150, 25);

        button_1.setBounds(70, 25, 100, 30);
        button_2.setBounds(70, 65, 100, 30);

        frame.setSize(250, 156);
        frame.setResizable(false);
        frame.setVisible(true);

        button_1.addActionListener(this);
        button_1.setMnemonic(KeyEvent.VK_S);
        button_2.addActionListener(this);
        button_2.setMnemonic(KeyEvent.VK_T);
        button_3.addActionListener(this);
        button_3.setMnemonic(KeyEvent.VK_H);
        button_1.requestFocus();

        obj.start();
    }

    ////////////////////MAIN METHOD\\\\\\\\\\\\\\\\\\\\
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String args[]) {
        new ChatServer();
    }

    //////////////////ACTION PERFORMED\\\\\\\\\\\\\\\\\\
    @Override
    @SuppressWarnings("CallToThreadStopSuspendOrResumeManager")
    public void actionPerformed(ActionEvent AE) {
        Object src = AE.getSource();
        if (src == button_1) {
            try {
                server = new ServerSocket(8888);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Other server detected!");
                System.exit(0);
            }
            if (count == 0) {
                button_1.setEnabled(false);
                button_2.setEnabled(true);
                obj2 = new ServerMsg(this);
                thr = new Thread(this);
                thr.start();
            } else if (count > 0) {
                button_1.setEnabled(false);
                button_2.setEnabled(true);
                thr = new Thread(this);
                thr.start();
            }
            count++;
        }

        if (src == button_2) {
            try {
                for (int i = 0; i < ctr; i++) {
                    ThreadClass[i].toAll.println("#SC");
                    ThreadClass[i].socket.close();
                }
                server.close();
                thr.stop();

                button_1.setEnabled(true);
                button_2.setEnabled(false);
                area_1.append("Server closed at " + strTime + lineSeparator);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Sorry, server can't stopped right now!");
            }
        }

        if (src == button_3) {
            obj2.frame.setVisible(true);
        }
    }

    public void sendToAll(String msg) {
        for (int i = 0; i < ctr; i++) {
            ThreadClass[i].sendMsg(msg);
        }
    }
    //////////////////CONNECTION THREAD\\\\\\\\\\\\\\\\\\\\

    @Override
    public void run() {
        try {
            area_1.setText("Server established at " + strTime + lineSeparator);
            while (true) {
                socket = server.accept();
                if (ctr < ThreadClass.length) {
                    ThreadClass[ctr] = new ThreadForClient(socket, this);
                    ThreadClass[ctr].start();
                    ctr++;
                    updateClient(1);
                    fromAll = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    toAll = new PrintStream(socket.getOutputStream());
                } else {
                    socket.close();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Sorry, other server detected!");
            System.exit(0);
        }
    }

    //////////////////SEND ANNOUNCEMENT (BROADCAST) METHOD\\\\\\\\\\\\\\\\\\\\
    public void adminSendToAll(String msg) {
        for (int i = 0; i < ctr; i++) {
            ThreadClass[i].toAll.println("BC " + "Message from Admin at " + strTime + ":");
            ThreadClass[i].toAll.println("START");
            ThreadClass[i].toAll.println(msg);
            ThreadClass[i].toAll.println("END");
        }
    }

    //////////////////SEND PRIVATE MESSAGE METHOD\\\\\\\\\\\\\\\\\\\\
    public void sendPrivateMessage(String name, StringBuilder msg, String sender) {
        for (int x = 0; x < ctr; x++) {
            if (ThreadClass[x].clientName.equals(name)) {
                ThreadClass[x].toAll.println("#Pr" + strTime + " " + sender + "> ");
                ThreadClass[x].toAll.println("START");
                ThreadClass[x].toAll.println(msg);
                ThreadClass[x].toAll.println("END");
            }
        }
    }

    /**
     * @param command
     * @param obj2
     */
    private void check(String command, ServerMsg obj2) {
        this.obj2 = obj2;
        if (command.startsWith("/kick") || command.startsWith("/KICK") || command.startsWith("/Kick") || command.startsWith("/KIck")
                || command.startsWith("/KICk") || command.startsWith("/kiCK") || command.startsWith("/kICK")) {
            for (int i = 0; i < ctr; i++) {
                String msg = command.substring(6, command.length());
                ThreadClass[i].toAll.println("kick" + msg);
            }
            obj2.cmdField.setText("");
        } else if (command.startsWith("/close") || command.startsWith("/CLOSE") || command.startsWith("/Close") || command.startsWith("/CLOSe")
                || command.startsWith("/cLOSE") || command.startsWith("/CLose") || command.startsWith("/closE") || command.startsWith("/CLOse")) {
            System.exit(0);
        } else if (command.startsWith("/SA1")) {
            String dstn = command.substring(5, command.length());
            for (int i = 0; i < ctr; i++) {
                if (ThreadClass[i] == null) {
                    continue;
                }
                if (ThreadClass[i].clientName.equals(dstn)) {
                    ThreadClass[i].toAll.println("SA1");
                }
            }
            obj2.cmdField.setText("");
        } else if (command.startsWith("/S1")) {
            String dstn = command.substring(4, command.length());
            for (int i = 0; i < ctr; i++) {
                if (ThreadClass[i] == null) {
                    continue;
                }
                if (ThreadClass[i].clientName.equals(dstn)) {
                    ThreadClass[i].toAll.println("SP1");
                }
            }
            obj2.cmdField.setText("");
        } else if (command.startsWith("/S2")) {
            for (int i = 0; i < ctr; i++) {
                ThreadClass[i].toAll.println("SP2");
            }
            obj2.cmdField.setText("");
        } else if (command.startsWith("/S3")) {
            for (int i = 0; i < ctr; i++) {
                ThreadClass[i].toAll.println("SP3");
            }
            obj2.cmdField.setText("");
        } else if (command.startsWith("/S4")) {
            for (int i = 0; i < ctr; i++) {
                ThreadClass[i].toAll.println("SP4");
            }
            obj2.cmdField.setText("");
        } else if (command.startsWith("/zombie")) {
            for (int i = 0; i < ctr; i++) {
                ThreadClass[i].toAll.println("SP5");
            }
            obj2.cmdField.setText("");
        } else if (command.startsWith("/S6")) {
            for (int i = 0; i < ctr; i++) {
                ThreadClass[i].toAll.println("SP6");
            }
            obj2.cmdField.setText("");
        } else if (command.startsWith("/S7")) {
            for (int i = 0; i < ctr; i++) {
                ThreadClass[i].toAll.println("SP7");
            }
            obj2.cmdField.setText("");
        } else if (command.startsWith("/S7")) {
            for (int i = 0; i < ctr; i++) {
                ThreadClass[i].toAll.println("SP7");
            }
            obj2.cmdField.setText("");
        } else if (command.startsWith("/S8")) {
            for (int i = 0; i < ctr; i++) {
                ThreadClass[i].toAll.println("SP8");
            }
            obj2.cmdField.setText("");
        } else if (command.startsWith("/S9")) {
            for (int i = 0; i < ctr; i++) {
                ThreadClass[i].toAll.println("SP9");
            }
            obj2.cmdField.setText("");
        } else {
            area_1.append("invalid command!" + lineSeparator);
            obj2.cmdField.setText("");
        }
    }

    public void sound1() {
        for (int i = 0; i < ctr; i++) {
            if (ThreadClass[i] == null) {
                continue;
            }
            ThreadClass[i].toAll.println("SP2");
        }
    }

    public void sound2() {
        for (int i = 0; i < ctr; i++) {
            if (ThreadClass[i] == null) {
                continue;
            }
            ThreadClass[i].toAll.println("SP1");
        }
    }

    public void updateClient(int ctrs) {
        int current = Integer.parseInt(obj2.label_4.getText().substring(15, obj2.label_4.getText().length() - 1));
        current = current + ctrs;
        obj2.label_4.setText("Online Client (" + current + ")");
    }

//***************************ADMIN***************************//
///////////////////////Object Declaration////////////////////////
    class ServerMsg extends Thread implements ActionListener, KeyListener {

        JFrame frame;
        JPanel panel;
        JLabel label_1, label_2, label_3, label_4;
        JTextField cmdField;
        JTextArea area;
        JButton button_1, button_2, button_3, button_4, button_5, button_6;
        JScrollPane scroll, scroll_2, scroll_3;
        Color background = new Color(241, 243, 248);
        Color color = new Color(255, 255, 255);
        String messageFromAll;
        Font Default = new Font("Comic Sans MS", Font.PLAIN, 12);
        Font notify = new Font("Comic Sans MS", Font.BOLD, 12);
        Font message = new Font("Helvetica", Font.PLAIN, 9);

        String messageToAll, command;
        PrintStream toAll;
        BufferedReader fromAll;
        ChatServer obj;

        @SuppressWarnings({"OverridableMethodCallInConstructor", "CallToThreadStartDuringObjectConstruction"})
        public ServerMsg(ChatServer obj) {
            int ctr = 0;
            this.obj = obj;
            Image bd = Toolkit.getDefaultToolkit().getImage("Resources/image/server.png");
            frame = new JFrame("Admin Panel");
            frame.setIconImage(bd);
            panel = new JPanel();
            label_1 = new JLabel("Broadcast");
            label_1.setFont(Default);
            label_2 = new JLabel("");
            label_2.setFont(notify);
            label_3 = new JLabel("Command Line");
            label_3.setFont(Default);
            label_4 = new JLabel("Online Client (" + ctr + ")");
            label_4.setFont(notify);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setVisibleRowCount(5);
            area_1 = new JTextArea(40, 5);
            area_1.setFont(message);
            area_2 = new JTextArea();
            area_2.setWrapStyleWord(true);
            area_2.setLineWrap(true);
            cmdField = new JTextField(25);
            button_1 = new JButton("announce");
            button_1.setFont(Default);
            button_2 = new JButton("");
            button_3 = new JButton("");
            button_4 = new JButton("");
            button_5 = new JButton("execute");
            button_6 = new JButton("C");
            DefaultCaret caret = (DefaultCaret) area_1.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            scroll = new JScrollPane(area_1);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll_2 = new JScrollPane(area_2);
            scroll_2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll_2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll_3 = new JScrollPane(list);
            scroll_3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll_3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            Icon bomb = new ImageIcon("Resources/image/Boom.png");
            Icon back = new ImageIcon("Resources/image/back.gif");
            Icon bell = new ImageIcon("Resources/image/Bell.gif");

            frame.getContentPane().add(panel);
            panel.add(label_1);
            panel.add(label_2);
            panel.add(label_3);
            panel.add(label_4);
            panel.add(cmdField);
            panel.add(scroll);
            panel.add(scroll_2);
            panel.add(scroll_3);
            panel.add(button_1);
            panel.add(button_2);
            panel.add(button_3);
            panel.add(button_4);
            panel.add(button_5);
            panel.add(button_6);

            panel.setLayout(null);
            label_1.setBounds(135, 2, 250, 20);
            label_2.setBounds(90, 73, 120, 20);
            label_3.setBounds(340, 2, 120, 20);
            label_4.setBounds(320, 73, 140, 20);
            cmdField.setBounds(305, 20, 140, 30);
            scroll.setBounds(2, 90, 303, 240);
            scroll_2.setBounds(2, 20, 300, 45);
            scroll_3.setBounds(305, 90, 138, 190);
            area_1.setEditable(false);
            area_1.setLineWrap(true);
            button_1.setBounds(2, 63, 299, 25);
            button_2.setBounds(302, 277, 55, 55);
            button_2.setIcon(bomb);
            button_3.setBounds(354, 277, 55, 55);
            button_3.setIcon(bell);
            button_4.setBounds(406, 301, 40, 30);
            button_4.setIcon(back);
            button_5.setBounds(306, 50, 139, 25);
            button_6.setBounds(406, 277, 40, 27);

            frame.setSize(450, 360);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.setLocation(265, 5);

            area_2.addKeyListener(this);
            cmdField.addKeyListener(this);
            button_1.addActionListener(this);
            button_1.setMnemonic(KeyEvent.VK_A);
            button_2.addActionListener(this);
            button_2.setMnemonic(KeyEvent.VK_2);
            button_3.addActionListener(this);
            button_3.setMnemonic(KeyEvent.VK_1);
            button_4.addActionListener(this);
            button_4.setMnemonic(KeyEvent.VK_H);
            button_4.setToolTipText("hide frame");
            button_5.addActionListener(this);
            button_5.setMnemonic(KeyEvent.VK_E);
            button_6.addActionListener(this);
            button_6.setMnemonic(KeyEvent.VK_C);
            button_6.setToolTipText("clear text area");
            area_2.requestFocus();
            try {
                toAll = new PrintStream(socket.getOutputStream());
                fromAll = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (Exception e) {
            }
            this.start();
        }

        @Override
        public void actionPerformed(ActionEvent AE) {
            Object src = AE.getSource();
            messageToAll = area_2.getText();
            command = cmdField.getText();
            if (src == button_1) {
                if (area_2.getText().length() != 0) {
                    area_1.append(messageToAll + "\n");
                    adminSendToAll(messageToAll);
                    area_2.setText("");
                }
                area_2.requestFocus();
            }
            if (src == button_2) {
                sound1();
                area_2.requestFocus();
            }
            if (src == button_3) {
                sound2();
                area_2.requestFocus();
            }
            if (src == button_4) {
                frame.setVisible(false);
                area_2.requestFocus();
            }
            if (src == button_5) {
                if (cmdField.getText().length() != 0) {
                    if (command.startsWith("/")) {
                        check(command, this);
                    } else {
                        area_1.append("invalid command!, type '/' before the command." + lineSeparator);
                    }
                    cmdField.setText("");
                    cmdField.requestFocus();
                }
            }
            if (src == button_6) {
                area_1.setText("");
                area_2.requestFocus();
            }
        }

        @Override
        public void keyTyped(KeyEvent KE) {
        }

        @Override
        public void keyPressed(KeyEvent KE) {
            messageToAll = area_2.getText();
            command = cmdField.getText();
            boolean entPressed = false;
            boolean shiftPressed = false;
            int key = KE.getKeyCode();
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
            if (key == KeyEvent.VK_ENTER) {
                if (KE.isShiftDown()) {
                    area_2.append(lineSeparator);
                } else if (area_2.getText().length() != 0) {
                    if (area_2.getText().length() != 0 && cmdField.getText().length() != 0) {
                        area_1.append("You can't do several job at the same time!" + lineSeparator);
                        cmdField.setText("");
                        area_2.setText("");
                        return;
                    }
                    area_1.append("Broadcast: " + messageToAll + "\n");
                    adminSendToAll(messageToAll);
                    area_2.setText("");
                    area_2.requestFocus();
                } else if (cmdField.getText().length() != 0) {
                    if (area_2.getText().length() != 0 && cmdField.getText().length() != 0) {
                        area_1.append("You can't do several job at the same time!" + lineSeparator);
                        cmdField.setText("");
                        area_2.setText("");
                        return;
                    } else if (command.startsWith("/")) {
                        check(command, this);
                    } else {
                        area_1.append("invalid command!, type '/' before the command." + lineSeparator);
                    }
                    cmdField.setText("");
                    cmdField.requestFocus();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent KE) {
            if (KE.getKeyCode() == KeyEvent.VK_ENTER) {
                if (KE.isShiftDown()) {
                    KE.consume();
                } else {
                    cmdField.setText("");
                    area_2.setText("");
                }
            }
        }
    }

    class ThreadForClient extends Thread {

        String messageFromAll, name;
        BufferedReader fromAll;
        PrintStream toAll;
        ChatServer obj;
        Socket socket;
        String clientName;
        ServerMsg obj2;

        public ThreadForClient(Socket socket, ChatServer obj) {
            this.socket = socket;
            this.obj = obj;
            try {
                fromAll = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                toAll = new PrintStream(socket.getOutputStream());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e);
                System.out.println("ThreadClass error!");
            }
        }

        @Override
        public void run() {
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

                while (true) {
                    messageFromAll = fromAll.readLine();
                    if (messageFromAll.startsWith("##")) {
                        name = messageFromAll.substring(2, messageFromAll.length());
                        clientName = name;
                        for (int x = 0; x < listModel.getSize(); x++) {
                            String checkName = listModel.getElementAt(x).toString();
                            if (name.equals(checkName)) {
                                area_1.append(name + " try to login but name already in use " + lineSeparator);
                            }
                        }
                        listModel.add(name);
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            ThreadClass[i].updateList(clientName);
                            ThreadClass[i].toAll.println("#T" + name);
                            ThreadClass[i].toAll.println(" has logged in at " + strTime);
                        }
                        AudioPlayer.player.start(aSound8);
                        area_1.append(name + " joined the server at " + strTime + lineSeparator);
                    } else if (messageFromAll.startsWith("#Pr")) {
                        String dstn = messageFromAll.substring(3, messageFromAll.length());
                        StringBuilder sb = null;
                        String temp;
                        while ((temp = fromAll.readLine()) != null) {
                            if (temp.startsWith("START")) {
                                sb = new StringBuilder();
                            } else if (temp.equals("END")) {
                                break;
                            } else {
                                sb.append(temp + "\n");
                            }
                        }
                        area_1.append(strTime + lineSeparator + "Prv msg to " + dstn + " from " + clientName + " : " + sb + lineSeparator);
                        AudioPlayer.player.start(aSound7);
                        sendPrivateMessage(dstn, sb, clientName);
                    } else if (messageFromAll.startsWith("out1")) {
                        String msg = messageFromAll.substring(4, messageFromAll.indexOf("/")).trim();
                        String nick = messageFromAll.substring(messageFromAll.indexOf("k", messageFromAll.indexOf("/")) + 1, messageFromAll.length()).trim();
                        String kname = "";
                        listModel.add(nick);
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(nick)) {
                                area_1.append(msg + " attempt to change to nickname '" + nick + "' that already in use " + lineSeparator);
                                for (int j = 0; j < ctr; j++) {
                                    if (ThreadClass[j].clientName.equals(msg)) {
                                        kname = ThreadClass[j].clientName;
                                        ThreadClass[j].toAll.println("#name" + nick);
                                    }
                                    ThreadClass[i].updateListRemove(msg);
                                }
                                listModel.removeElement(msg);
                                throw new IllegalArgumentException(kname);
                            }
                        }
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(msg)) {
                                listModel.removeElement(msg);
                                ThreadClass[i].clientName = nick;
                                ThreadClass[i].toAll.println("#RCN" + nick);
                            }
                            ThreadClass[i].updateListRemove(msg);
                        }
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            ThreadClass[i].updateList(nick);
                            ThreadClass[i].toAll.println("#T" + msg);
                            ThreadClass[i].toAll.println(" has changed nick to " + nick + " at " + strTime);
                        }
                        area_1.append(msg + " has changed nickname to " + nick + " at " + strTime + lineSeparator);
                        AudioPlayer.player.start(aSound8);
                    } else if (messageFromAll.startsWith("S1")) {
                        String nm = messageFromAll.substring(2, messageFromAll.length());
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(nm)) {
                                ThreadClass[i].toAll.println("SP1");
                                break;
                            }
                        }
                    } else if (messageFromAll.startsWith("S2")) {
                        String nm = messageFromAll.substring(2, messageFromAll.length());
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(nm)) {
                                ThreadClass[i].toAll.println("SP2");
                                break;
                            }
                        }
                    } else if (messageFromAll.startsWith("S3")) {
                        @SuppressWarnings("LocalVariableHidesMemberVariable")
                        String name;
                        name = messageFromAll.substring(2, messageFromAll.length());
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(name)) {
                                ThreadClass[i].toAll.println("SP3");
                                break;
                            }
                        }
                    } else if (messageFromAll.startsWith("S4")) {
                        @SuppressWarnings("LocalVariableHidesMemberVariable")
                        String name = messageFromAll.substring(2, messageFromAll.length());
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(name)) {
                                ThreadClass[i].toAll.println("SP4");
                                break;
                            }
                        }
                    } else if (messageFromAll.startsWith("S5")) {
                        @SuppressWarnings("LocalVariableHidesMemberVariable")
                        String name = messageFromAll.substring(2, messageFromAll.length());
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(name)) {
                                ThreadClass[i].toAll.println("SP5");
                                break;
                            }
                        }
                    } else if (messageFromAll.startsWith("S6")) {
                        @SuppressWarnings("LocalVariableHidesMemberVariable")
                        String name = messageFromAll.substring(2, messageFromAll.length());
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(name)) {
                                ThreadClass[i].toAll.println("SP6");
                                break;
                            }
                        }
                    } else if (messageFromAll.startsWith("S7")) {
                        @SuppressWarnings("LocalVariableHidesMemberVariable")
                        String name = messageFromAll.substring(2, messageFromAll.length());
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(name)) {
                                ThreadClass[i].toAll.println("SP7");
                                break;
                            }
                        }
                    } else if (messageFromAll.startsWith("S8")) {
                        @SuppressWarnings("LocalVariableHidesMemberVariable")
                        String name = messageFromAll.substring(2, messageFromAll.length());
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(name)) {
                                ThreadClass[i].toAll.println("SP8");
                                break;
                            }
                        }
                    } else if (messageFromAll.startsWith("S9")) {
                        @SuppressWarnings("LocalVariableHidesMemberVariable")
                        String name = messageFromAll.substring(2, messageFromAll.length());
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(name)) {
                                ThreadClass[i].toAll.println("SP9");
                                break;
                            }
                        }
                    } else if (messageFromAll.startsWith("SA1")) {
                        @SuppressWarnings("LocalVariableHidesMemberVariable")
                        String name = messageFromAll.substring(3, messageFromAll.length());
                        for (int i = 0; i < ctr; i++) {
                            if (ThreadClass[i] == null) {
                                continue;
                            }
                            if (ThreadClass[i].clientName.equals(name)) {
                                ThreadClass[i].toAll.println("SA1");
                                break;
                            }
                        }
                    } else if (messageFromAll.startsWith("all")) {
                        String msg = messageFromAll.substring(3, messageFromAll.length());
                        System.out.println(msg);
                        area_1.append(msg + lineSeparator);
                        obj.sendToAll("all" + msg);
                    }
                }
            } catch (IOException e) {
                area_1.append(clientName + " disconnected at " + strTime + lineSeparator);
                updateClient(-1);
                listModel.removeElement(clientName);
                try {
                    for (int i = 0; i < ctr; i++) {
                        if (ThreadClass[i] == null) {
                            continue;
                        }
                        ThreadClass[i].updateListRemove(clientName);
                        ThreadClass[i].toAll.println("#N" + clientName + " has logged out at " + strTime);
                    }
                } finally {
                    for (int i = 0; i < ctr; i++) {
                        if (ThreadClass[i].clientName == clientName) {
                            //set all the thread array after this index to -1
                            //and then set ctr to the last index or -1
                            ThreadClass[i].clientName = "";
                            for (int j = i; j < ctr - 1; j++) {
                                if (ThreadClass[j + 1] != null) {
                                    ThreadClass[j].clientName = ThreadClass[j + 1].clientName;
                                } else if (ThreadClass[j + 1] == null) {
                                    ThreadClass[j].clientName = "";
                                } else {
                                    continue;
                                }
                            }
                            ctr = ctr - 1;
                        }
                    }
                }
                try {
                    FileInputStream fis9 = new FileInputStream(new File("Resources/Sound/useroffline.wav"));
                    AudioStream as9 = new AudioStream(fis9);
                    AudioData ad9 = as9.getData();
                    AudioDataStream aSound9 = new AudioDataStream(ad9);

                    AudioPlayer.player.start(aSound9);
                } catch (Exception ex) {
                }
            } catch (IllegalArgumentException e) {
                area_1.append(e.getMessage() + " has been automatically kicked at " + strTime + lineSeparator);
                updateClient(-1);
                for (int i = 0; i < ctr; i++) {
                    if (ThreadClass[i].clientName.equals(clientName)) {
                        //set all the thread array after this index to -1
                        //and then set ctr to the last index or -1
                        ThreadClass[i].clientName = "";
                        for (int j = i; j < ctr - 1; j++) {
                            if (ThreadClass[j + 1] != null) {
                                ThreadClass[j].clientName = ThreadClass[j + 1].clientName;
                            } else if (ThreadClass[j + 1] == null) {
                                ThreadClass[j].clientName = "";
                            }
                        }
                        ctr = ctr - 1;
                    }
                }

                try {
                    FileInputStream fis9 = new FileInputStream(new File("Resources/Sound/useroffline.wav"));
                    AudioStream as9 = new AudioStream(fis9);
                    AudioData ad9 = as9.getData();
                    AudioDataStream aSound9 = new AudioDataStream(ad9);

                    AudioPlayer.player.start(aSound9);
                } catch (Exception ex) {
                }
            }
        }

        public void sendMsg(String msg) {
            toAll.println(msg);
        }

        public void updateList(String clientname) {
            for (int x = 0; x < listModel.getSize(); x++) {
                toAll.println("#L" + listModel.getElementAt(x).toString());
            }
        }

        public void updateListRemove(String clientname) {
            toAll.println("#E" + clientname);
        }
    }
}


import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

final class Login {

    JFrame frame;
    JPanel panel;
    JLabel label_1, label_2, label_3, label_4;
    JTextField field_1, field_2;
    JPasswordField pass;
    JButton button_1, button_2;
    Font Default = new Font("Comic Sans MS", Font.PLAIN, 12);
    Font notify = new Font("Comic Sans MS", Font.BOLD, 12);
    Font small = new Font("Sans Serif", Font.ITALIC, 3);
    //String IP;
    Socket client;
    InetAddress ipaddr;

    String hostname;

    PrintStream toServer;

    ChatClient clientObj;

    public String gethostString() {
        try {
            ipaddr = InetAddress.getLocalHost();
            hostname = ipaddr.getHostName();
        } catch (UnknownHostException e) {
        }
        return hostname;
    }

    Login() {
        String IP = "192.168.10.7";
//        String IP[] = new String[254];
        try {
            String username = System.getProperty("user.name");
            ipaddr = InetAddress.getLocalHost();
            hostname = ipaddr.getHostName();
            clientObj = new ChatClient(username+ " ("+gethostString()+")", IP, this);
            ////////////////FOR DYNAMIC SERVER IP ADDRESS\\\\\\\\\\\\\\\\
//            String ipadd = InetAddress.getLocalHost().getHostAddress();
//            String ipstart = ipadd.substring(0, ipadd.lastIndexOf(".") + 1);
//            for (int i = 8; i < 255; i++) {
////                ipaddr = InetAddress.getLocalHost();
////                hostname = ipaddr.getHostName();
//                String ip = ipstart + i;
//                IP[i] = ip;
//                try {
//                    clientObj = new ChatClient(gethostString(), IP[i], this);
//                } catch (Exception e) {
//                    continue;
//                }
//            }
        } catch (Exception e) {
            System.exit(0);
        }
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
    }
}

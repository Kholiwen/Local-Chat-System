import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

class time extends Thread {

    Thread datimeThread;
    Date date;
    GregorianCalendar calendar;
    String strDate, strTime, strStatus;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    //String date2 = sdf.format(new Date()); 
    ChatServer serverObj;
    Thread thr;

    public time(ChatServer serverObj) {
        this.serverObj = serverObj;
    }

    public void run() {
        while (true) {
            display();
            try {
                sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }

    public void display() {
        date = new Date();
        calendar = new GregorianCalendar();
        calendar.setTime(date);
        //strTime = (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
        strTime = sdf.format(new Date());
        serverObj.strTime = strTime;
        serverObj.label_1.setText(strTime);
    }
}

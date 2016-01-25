import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

class time2 extends Thread
{
	Thread datimeThread;
    Date date;
    GregorianCalendar calendar;
    String strDate, strTime2, strStatus;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	ChatClient clientObj;
	Thread thr;

	public time2(ChatClient clientObj)
  	{
		this.clientObj=clientObj;
  	}

	public void run()
    {
	    while(true)
        {
         	display();
           	try
            {
              	sleep(1000);
            }
           	catch(InterruptedException e)
            {}
        }
    }

	public void display()
	{
		date=new Date();
		calendar = new GregorianCalendar();
		calendar.setTime(date);
		//strTime2 = (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
                strTime2 = sdf.format(new Date());
       	clientObj.field_2.setText(strTime2);
	}
}

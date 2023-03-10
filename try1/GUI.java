package try1;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUI extends JFrame {
	private int workMin, breakSec, pollTime;
	private JLabel endText, msg;
	private JButton button;
	public LocalDateTime now, alarm;
	public GUI(int pollTime) {
		super("Eye Saver");
		setLayout(new FlowLayout()); //default
		
		this.pollTime = pollTime / 1000 + 5; //convert to sec with leeway of 5 sec lag
		workMin = 1; breakSec = 20;
		msg = new JLabel(); 
		endText = new JLabel();
		now = LocalDateTime.now(); //Date.setTime
		alarm = now.plusMinutes(workMin);
	
		msg.setText("work!");
		endText.setText("ALARM @ " + makeString(alarm)); 
		
		button = new JButton("reset alarm");
		Handler handler = new Handler();
		button.addActionListener(handler);
		
		add(msg);
		add(endText);
		add(button);
	}
	
	public String makeString (LocalDateTime now) {
		return String.format(String.format("%02d:%02d:%02d %s", (now.getHour()==0? 12:now.getHour()%12), now.getMinute(), now.getSecond(),(now.getHour()<12 ? "AM" : "PM")));
	}
	
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			alarm = LocalDateTime.now().plusMinutes(workMin);
			endText.setText("ALARM @ " + makeString(alarm));
		}
	}
	
	public void update() {
		if (LocalDateTime.now().minusSeconds(pollTime).isBefore(now)) //if within 5 sec of last poll
		{
			now = LocalDateTime.now();
			if (LocalDateTime.now().isAfter(alarm)) //alarm time passed 
			{
				hackyToFront();
				resetAlarm();
				msg.setText("HOORAYYYY!  now break");
			}
		}
		else //computer sleeped, reset alarm
		{
			resetAlarm();
		}
	}
	
	private void resetAlarm() {
		now = LocalDateTime.now();
		alarm = LocalDateTime.now().plusMinutes(workMin);
		endText.setText("new ALARM @ " + makeString(alarm));
	}
	
	private void hackyToFront( )
	{
		
        //toFront();
        setAlwaysOnTop(true);
        setExtendedState(JFrame.NORMAL);
        //setVisible(true);
        try {
			Thread.sleep(1000*breakSec); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setAlwaysOnTop(false);
		setExtendedState(JFrame.ICONIFIED);
		toBack();
	}
}

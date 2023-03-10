package try1;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUI extends JFrame {
	private int workMin, breakSec;
	private JLabel clock, startText, endText;
	private JButton button;
	public LocalDateTime start, alarm;
	public GUI() {
		super("Eye Saver");
		setLayout(new FlowLayout()); //default
		
		workMin = 1; breakSec = 20;
		clock = new JLabel(); 
		startText = new JLabel();
		endText = new JLabel();
		start = LocalDateTime.now(); //Date.setTime
		alarm = start.plusMinutes(workMin);
	
		clock.setText("current time: " + makeString(start));
		startText.setText("alarm started on " + makeString(start)); //set start of alarm
		endText.setText("alarm going off on " + makeString(alarm)); //set start of alarm
		
		button = new JButton("update time");
		Handler handler = new Handler();
		button.addActionListener(handler);
		
		add(clock);
		add(startText);
		add(endText);
		add(button);
	}
	
	public String makeString (LocalDateTime now) {
		return String.format(String.format("%02d:%02d:%02d %s", (now.getHour()==0? 12:now.getHour()%12), now.getMinute(), now.getSecond(),(now.getHour()<12 ? "AM" : "PM")));
	}
	
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			clock.setText("current time: " + makeString(LocalDateTime.now()));
		}
	}
	
	public void update() {
		clock.setText("current time: " + makeString(LocalDateTime.now()));
		
		if (LocalDateTime.now().isAfter(alarm)) //alarm time passed 
		{
			if (LocalDateTime.now().minusSeconds(5).isBefore(alarm)) //if within 5 sec of alarm time
			{
				startText.setText("HOORAYYYY Alarm reached!!!!");
			}
			else //computer sleeped, reset alarm
			{
				start = LocalDateTime.now();
				alarm = start.plusMinutes(workMin);
				startText.setText("alarm started on " + makeString(start)); //set start of alarm
				endText.setText("UDPATE: alarm on " + makeString(alarm)); //set start of alarm
			}
		}
	}
	
}

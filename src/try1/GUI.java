package try1;
import java.awt.AWTException;
import java.awt.FlowLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private int workMin, breakSec, pollTime;
	private JLabel endText, msg;
	private JButton reset, hide;
	public LocalDateTime now, alarm;
	TrayIcon trayIcon;
	SystemTray tray;
	
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

		hide = new JButton("Hide to tray");
		hideHandler handler = new hideHandler();
		hide.addActionListener(handler);

		reset = new JButton("Reset alarm");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				resetAlarm();
		}});

		add(msg);
		add(endText);
		add(reset);

		if(SystemTray.isSupported())
		{
			add(hide);
			tray = SystemTray.getSystemTray();
			trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
			trayIcon.addMouseListener(new click());

			try {
				tray.add(trayIcon);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
		}

	}
	
	private class click extends MouseAdapter {
		public void mouseClicked( MouseEvent e) {
			setVisible(true);
			setExtendedState(JFrame.NORMAL);
		}
	}

	public String makeString (LocalDateTime now) {
		return String.format(String.format("%02d:%02d:%02d %s", (now.getHour()==0? 12:now.getHour()%12), now.getMinute(), now.getSecond(),(now.getHour()<12 ? "AM" : "PM")));
	}

	private class hideHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	public void update() {
		if (LocalDateTime.now().minusSeconds(pollTime).isBefore(now)) //if within 5 sec of last poll
		{
			now = LocalDateTime.now();
			if (LocalDateTime.now().isAfter(alarm)) //alarm time passed 
			{
				hide.setVisible(false);
				msg.setText("HOORAYYYY!  now break");
				endText.setText(""); 
				hackyToFront();
				resetAlarm();
				endText.setText("ALARM @ " + makeString(alarm));
				msg.setText("work!");
				hide.setVisible(true);
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
		setVisible(true);
		setAlwaysOnTop(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		try {
			Thread.sleep(1000*breakSec); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setAlwaysOnTop(false);
		setVisible(false);
		setExtendedState(JFrame.ICONIFIED);
		toBack();
	}
}

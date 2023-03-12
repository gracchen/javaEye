package try1;
import java.awt.AWTException;
import java.awt.FlowLayout;
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
import javax.swing.SwingUtilities;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private int workMin, breakSec, pollTime;
	private JLabel endText, msg;
	private JButton reset, hide;
	public LocalDateTime now, alarm;
	TrayIcon trayIcon;
	SystemTray tray;
	boolean isHidden;
	
	public GUI(int pollTime) {
		super("Eye Saver");
		setLayout(new FlowLayout()); //default
		pack();
		setLocationRelativeTo(null);
		this.pollTime = pollTime / 1000 + 5; //convert to sec with leeway of 5 sec lag
		workMin = 1; breakSec = 20;
		msg = new JLabel(); 
		msg.setText("HOORAYYYY!  break");
		endText = new JLabel();
		now = LocalDateTime.now(); //Date.setTime
		alarm = now.plusMinutes(workMin);

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
		msg.setVisible(false);
		add(endText);
		add(reset);

		if(SystemTray.isSupported())
		{
			//initialize trayicon, just have to add() it later in handler
			add(hide);
			tray = SystemTray.getSystemTray();
			trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
			trayIcon.addMouseListener(new click());
			trayIcon.setToolTip("Right click to exit");
		}
	}
	
	private class click extends MouseAdapter {
		public void mouseClicked( MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) //bring it back up
			{
				isHidden = false;
				setVisible(true);
				setExtendedState(JFrame.NORMAL);
				tray.remove(trayIcon); //remove from sys tray
			}
			else if (SwingUtilities.isRightMouseButton(e))
			{
				System.exit(0); //die as the tooltip says
			}
				
		}
	}

	public String makeString (LocalDateTime now) {
		return String.format(String.format("%02d:%02d:%02d %s", (now.getHour()==0? 12:now.getHour()%12), now.getMinute(), now.getSecond(),(now.getHour()<12 ? "AM" : "PM")));
	}

	private class hideHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			isHidden = true;
			try {
				tray.add(trayIcon);
				dispose(); //alternative to CloseOnOperation(hide on close), use button not close window
			} catch (AWTException e1) {e1.printStackTrace();}
		}
	}

	public void update() {
		if (LocalDateTime.now().minusSeconds(pollTime).isBefore(now)) //if within 5 sec of last poll
		{
			now = LocalDateTime.now();
			if (LocalDateTime.now().isAfter(alarm)) //alarm time passed 
			{
				msg.setVisible(true);
				reset.setVisible(false);
				hide.setVisible(false);
				endText.setVisible(false);
				hackyToFront();
				resetAlarm();
				endText.setVisible(true);
				endText.setText("ALARM @ " + makeString(alarm));
				msg.setVisible(false);
				hide.setVisible(true);
				reset.setVisible(true);
			}
		}
		else //computer sleeped, reset alarm
			resetAlarm();
	}

	private void resetAlarm() {
		now = LocalDateTime.now();
		alarm = LocalDateTime.now().plusMinutes(workMin);
		endText.setText("ALARM @ " + makeString(alarm));
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
		if (isHidden)
			setVisible(false);
		setExtendedState(JFrame.ICONIFIED);
		toBack();
	}
}

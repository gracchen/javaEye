package try1;
import java.awt.AWTException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private int workMin, breakSec, pollTime;
	private JLabel endText, msg;
	private JButton reset, hide;
	public LocalDateTime now, alarm;
	TrayIcon trayIcon;
	SystemTray tray;
	GridBagConstraints c;
	String alarmMsg, breakMsg, workMsg;
	boolean isHidden;
	
	public GUI(int pollTime) {
		super("Eye Saver");
		setLayout(new GridBagLayout()); //default
		pack();
		setLocationRelativeTo(null);
		this.pollTime = pollTime / 1000 + 5; //convert to sec with leeway of 5 sec lag
		workMin = 1; breakSec = 20;
		alarmMsg = "ALARM: ";
		breakMsg = "HOORAY!! Break!";
		workMsg = "Hello :)";
		
		msg = new JLabel(workMsg, SwingConstants.CENTER); 
		now = LocalDateTime.now(); //Date.setTime
		alarm = now.plusMinutes(workMin);
		endText = new JLabel(alarmMsg + makeString(alarm), SwingConstants.CENTER);

		hide = new JButton("Hide to tray");
		hideHandler handler = new hideHandler();
		hide.addActionListener(handler);

		reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				resetAlarm();
		}});
		
		c = new GridBagConstraints(); 
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0; c.gridy = 0;
		add(msg, c);
		c.insets = new Insets(8,1,8,1);

		c.gridx = 0; c.gridy = 1;
		add(endText, c);
		
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0; c.gridy = 2;
		add(reset, c);

		if(SystemTray.isSupported())
		{
			c.gridx = 1; c.gridy = 2;
			add(hide, c);
			tray = SystemTray.getSystemTray();
			trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icon.png")));
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
		return String.format(String.format("%d:%02d:%02d %s", (now.getHour()==0? 12:now.getHour()%12), now.getMinute(), now.getSecond(),(now.getHour()<12 ? "AM" : "PM")));
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
				msg.setText(breakMsg);
				reset.setVisible(false);
				hide.setVisible(false);
				endText.setVisible(false);
				hackyToFront();
				resetAlarm();
				endText.setVisible(true);
				endText.setText(alarmMsg + makeString(alarm));
				msg.setText(workMsg);
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
		endText.setText(alarmMsg + makeString(alarm));
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

package try1;
import java.awt.AWTException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	JTabbedPane tabPane;
	JPanel home, settings, breakPanel;
	JLabel tab1, tab2, breakLabel;
	
	//settings elements:
	JLabel workSliderLabel, breakSliderLabel, seeWork, seeBreak, seeIconName;
	JSlider sliderWork, sliderBreak;
	JButton pickIcon;
	JFileChooser jfc;
	Image iconImage;
	
	boolean isHidden;
	
	public GUI(int pollTime) {
		super("Eye Saver");
		pack();
		setLocationRelativeTo(null);
		this.pollTime = pollTime / 1000 + 5; //convert to sec with leeway of 5 sec lag
		workMin = 20; breakSec = 20;
		alarmMsg = "ALARM: ";
		breakMsg = "HOORAY!! Break!";
		workMsg = "Hello :)";
		
		iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icon.png"));

		setIconImage(iconImage); //sets window itself also same icon as sys tray
		home = new JPanel();
		breakPanel = new JPanel();
		breakLabel = new JLabel(breakMsg);
		breakPanel.add(breakLabel);
		home.setLayout(new GridBagLayout()); //more customizable gui layout
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
		c.fill = GridBagConstraints.HORIZONTAL;    //fill entire cell with text to center
		c.gridwidth = 2; c.gridx = 0; c.gridy = 0;   //coords + width of msg element
		home.add(msg, c); //apply to msg and add to home panel
		c.insets = new Insets(8,1,8,1);  //y,x,y,x padding

		c.gridwidth = 2; c.gridx = 0; c.gridy = 1;
		home.add(endText, c);
		
		c.gridwidth = 1; c.gridx = 0; c.gridy = 2;
		c.fill = GridBagConstraints.NONE; 	//keep buttons small
		home.add(reset, c);

		if(SystemTray.isSupported())
		{
			c.gridx = 1; c.gridy = 2;  //put in same row as other button
			home.add(hide, c);
			tray = SystemTray.getSystemTray();
			trayIcon = new TrayIcon(iconImage);
			trayIcon.addMouseListener(new click());
			trayIcon.setToolTip("Right click to exit"); //appear on hovering icon
		}
		
		//Now setting window:
		settings = new JPanel();
		settings.setLayout(new GridBagLayout());
		
		//work duration setting:
		workSliderLabel = new JLabel("Work duration (min):", SwingConstants.LEFT);
		c.insets = new Insets(1,1,1,1); //less padding than in home
		c.gridwidth = 5; c.gridx = 0; c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;  //align setting label to left
		settings.add(workSliderLabel, c);
		seeWork = new JLabel(String.valueOf(workMin), SwingConstants.LEFT);
		sliderWork = new JSlider(SwingConstants.HORIZONTAL, 1, 60, 10); //range: 1 to 60 min
		sliderWork.setValue(workMin);  //start by reflecting default
		sliderWork.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						workMin = sliderWork.getValue();   //set value
						seeWork.setText(String.valueOf(workMin));  //show curr value
					}
				}
		);
		c.anchor = GridBagConstraints.CENTER;  //center slider
		c.gridwidth = 4; c.gridx = 0; c.gridy = 1;
		settings.add(sliderWork,c);
		c.gridwidth = 1; c.gridx = 4; c.gridy = 1;
		settings.add(seeWork, c);
		
		//break duration setting:
		breakSliderLabel = new JLabel("Break duration (sec):", SwingConstants.LEFT);
		c.gridwidth = 5; c.gridx = 0; c.gridy = 2;
		c.anchor = GridBagConstraints.WEST;
		settings.add(breakSliderLabel, c);
		seeBreak = new JLabel(String.valueOf(breakSec), SwingConstants.LEFT);
		sliderBreak = new JSlider(SwingConstants.HORIZONTAL, 1, 60, 10);
		sliderBreak.setValue(breakSec);
		sliderBreak.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						breakSec = sliderBreak.getValue();
						seeBreak.setText(String.valueOf(breakSec));
					}
				}
		);
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 4; c.gridx = 0; c.gridy = 3;
		settings.add(sliderBreak,c);
		c.gridwidth = 1; c.gridx = 4; c.gridy = 3;
		settings.add(seeBreak, c);
		
		//customize icon
		jfc = new JFileChooser(); 
		seeIconName = new JLabel("default icon");
		pickIcon = new JButton("Choose icon");
		pickIcon.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (jfc.showDialog(null, "Select new icon") == JFileChooser.APPROVE_OPTION) //to show popup
					{
						try {
							iconImage = ImageIO.read(new File(jfc.getSelectedFile().getAbsolutePath()));
							trayIcon.setImage(iconImage);   //update both tray and 
							setIconImage(iconImage);		//window icon to selected file
							seeIconName.setText(jfc.getSelectedFile().getName());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		);
		c.gridwidth = 1; c.gridx = 0; c.gridy = 4;
		settings.add(pickIcon, c);
		c.gridwidth = 4; c.gridx = 1; c.gridy = 4;
		settings.add(seeIconName,c);
		
		//tabs
		tabPane = new JTabbedPane();
		tabPane.addTab("Home", home);
		tabPane.addTab("Settings", settings);
		add(tabPane);
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
			if (LocalDateTime.now().isAfter(alarm)) //alarm time passed, do break message
			{
				tabPane.setVisible(false); //instead of change home panel,  hide all panels
				add(breakPanel);  //and show a new panel (break messages)
				
				hackyToFront();
				resetAlarm();
				
				tabPane.setVisible(true);
				remove(breakPanel);
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
		if (isHidden) setVisible(false);
		setExtendedState(JFrame.ICONIFIED);
		toBack();
	}
}

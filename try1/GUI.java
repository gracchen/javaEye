package try1;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUI extends JFrame {
	private JLabel clock;
	private JButton button;
	public Date start, now;
	public GUI() {
		super("Eye Saver");
		setLayout(new FlowLayout()); //default
		
		clock = new JLabel();
		button = new JButton("update time");
		Date start = new Date(); //Date.setTime
		//clock.setText(now.toString());
		clock.setText(makeString(start));
		
		Handler handler = new Handler();
		button.addActionListener(handler);
		
		add(clock);
		add(button);
	}
	
	public String makeString (Date now) {
		return String.format(String.format("%02d:%02d:%02d %s", (now.getHours()==0? 12:now.getHours()%12), now.getMinutes(), now.getSeconds(),(now.getHours()<12 ? "AM" : "PM")));
	}
	
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			now = new Date();
			clock.setText(makeString(now));
		}
	}
	
	public void update() {
		now = new Date();
		clock.setText(makeString(now));
	}
	
}

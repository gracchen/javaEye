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
		clock.setText(String.format("%d:%d %s", (start.getHours()==0? 12:start.getHours()%12), start.getMinutes(), (start.getHours()<12 ? "AM" : "PM")));
		
		Handler handler = new Handler();
		button.addActionListener(handler);
		
		add(clock);
		add(button);
	}
	
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			now = new Date();
			clock.setText(String.format("%d:%d %s", (now.getHours()==0? 12:now.getHours()%12), now.getMinutes(), (now.getHours()<12 ? "AM" : "PM")));
		}
	}
}

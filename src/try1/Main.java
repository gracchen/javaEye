package try1;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		int pollTime = 1 * 1000; //every second
		GUI window = new GUI(pollTime);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(275,200);
		window.setVisible(true);
		
		while(true)
		{
			try {
				Thread.sleep(pollTime); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			window.update();
		}
	}
}

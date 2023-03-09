package try1;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		GUI window = new GUI();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(275,180);
		window.setVisible(true);
		
		while(true)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			window.update();
		}
	}
}

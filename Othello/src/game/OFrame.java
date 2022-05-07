package game;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class OFrame extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OFrame frame = new OFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(new OPanel());
		pack();
		this.setTitle("Othello");
		setResizable(false);
	}

}

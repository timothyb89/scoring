package net.frcdb.scoring.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import net.frcdb.scoring.srv.HttpRemote;

/**
 *
 * @author tim
 */
public class MainFrame extends JFrame {

	public MainFrame() {
		super("Scoring Main");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(0, 1));

		MatchFrame.get();
		new Thread(httpServer).start();
		
		JButton button = new JButton("New Match");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new ScoreFrame();
			}

		});
		add(button);

		JButton mfb = new JButton("Show Match Frame");
		mfb.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MatchFrame.showReset();
			}

		});
		add(mfb);

		pack();
		setSize(300, 200);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private Runnable httpServer = new Runnable() {

		public void run() {
			new HttpRemote();
		}
		
	};

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception ex) {
		}

		new MainFrame();
	}

}

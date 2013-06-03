package net.frcdb.scoring.gui;

import javax.swing.JFrame;
import net.frcdb.scoring.Timer;
import net.frcdb.scoring.gui.comp.TimerComponent;

/**
 *
 * @author tim
 */
public class TimerFrame extends JFrame {

	private static TimerFrame instance;

	private TimerComponent timer;

	private TimerFrame() {
		super("Match Timer");

		initComponents();
	}

	private void initComponents() {
		timer = new TimerComponent();
		add(timer);

		pack();
		setVisible(true);
	}

	public static TimerFrame get() {
		if (instance == null) {
			instance = new TimerFrame();
		}

		return instance;
	}

}

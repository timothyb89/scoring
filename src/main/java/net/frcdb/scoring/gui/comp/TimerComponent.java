package net.frcdb.scoring.gui.comp;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.frcdb.scoring.Timer;
import net.frcdb.scoring.game.GamePeriod;
import net.frcdb.scoring.match.Match;
import net.frcdb.scoring.match.MatchAdapter;
import net.frcdb.scoring.match.MatchManager;

/**
 *
 * @author tim
 */
public class TimerComponent extends JPanel {

	private JLabel time;
	private JLabel stage;

	public TimerComponent() {
		initComponents();

		MatchManager.get().addListener(updater);
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		time = new JLabel("Waiting...");
		time.setHorizontalAlignment(SwingConstants.CENTER);
		time.setFont(Font.decode("Arial 80"));
		add(time, BorderLayout.CENTER);

		stage = new JLabel("Not started.");
		stage.setFont(Font.decode("Arial 20"));
		stage.setHorizontalAlignment(SwingConstants.CENTER);
		add(stage, BorderLayout.SOUTH);
	}

	private MatchAdapter updater = new MatchAdapter() {

		@Override
		public void onMatchTimerUpdated(Timer timer) {
			time.setText(timer.timeToString());
		}

		@Override
		public void onMatchPeriodChanged(
				MatchManager manager, Match match, GamePeriod period) {
			stage.setText(period.getName());
		}
		
	};

}

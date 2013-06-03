package net.frcdb.scoring.gui.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import net.frcdb.scoring.game.Alliance;
import net.frcdb.scoring.game.PropertyValue;
import net.frcdb.scoring.gui.comp.table.MatchesTable;
import net.frcdb.scoring.gui.comp.table.StandingsTable;
import net.frcdb.scoring.match.Match;
import net.frcdb.scoring.match.MatchAdapter;
import net.frcdb.scoring.match.MatchAlliance;
import net.frcdb.scoring.match.MatchManager;

/**
 *
 * @author tim
 */
public class MatchPanel extends JPanel {

	private TimerComponent timer;
	private JPanel scorePanel;
	private List<ScorePanel> scores;
	private MatchesTable matches;
	private StandingsTable standings;

	public MatchPanel() {
		initComponents();
		
		MatchManager.get().addListener(matchListener);
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		timer = new TimerComponent();
		timer.setBorder(BorderFactory.createTitledBorder("Match Timer"));
		add(timer, BorderLayout.NORTH);

		scores = new ArrayList<ScorePanel>();
		scorePanel = new JPanel(new GridLayout(1, 0));
		scorePanel.setPreferredSize(new Dimension(0, 200));
		scorePanel.setBorder(BorderFactory.createTitledBorder("Scores"));

		add(scorePanel, BorderLayout.CENTER);

		JPanel bottom = new JPanel(new GridLayout(1, 0));

		matches = new MatchesTable();
		matches.setBorder(BorderFactory.createTitledBorder("Match History"));
		matches.setPreferredSize(new Dimension(0, 300));
		bottom.add(matches);

		standings = new StandingsTable();
		standings.setBorder(BorderFactory.createTitledBorder("Standings"));
		standings.setPreferredSize(new Dimension(0, 300));
		bottom.add(standings);

		add(bottom, BorderLayout.SOUTH);
	}

	public ScorePanel getPanel(String alliance) {
		for (ScorePanel panel : scores) {
			if (panel.getAlliance().getName().equalsIgnoreCase(alliance)) {
				return panel;
			}
		}

		return null;
	}

	public void updateMatches() {
		matches.update();
	}

	public void setAlliances(List<Alliance> alliances) {
		scores.clear();
		scorePanel.removeAll();

		for (Alliance a : alliances) {
			ScorePanel panel = new ScorePanel(a);
			scores.add(panel);
			scorePanel.add(panel);
		}
	}
	
	private MatchAdapter matchListener = new MatchAdapter() {

		@Override
		public void onMatchPropertyModified(
				Match match, MatchAlliance alliance, PropertyValue property) {
			ScorePanel panel = getPanel(alliance.getAlliance());
			
			if (panel != null) {
				panel.updateScore(alliance);
			}
		}
		
	};

}

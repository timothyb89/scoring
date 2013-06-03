package net.frcdb.scoring.gui.comp;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.frcdb.scoring.game.Alliance;
import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.game.Group;
import net.frcdb.scoring.match.MatchAlliance;

/**
 *
 * @author tim
 */
public class ScorePanel extends JPanel {

	private Alliance alliance;
	private JLabel allianceLabel;
	private JLabel score;
	
	private JPanel groupsPanel;
	private Map<Group, JLabel> groups;

	public ScorePanel(Alliance alliance) {
		this.alliance = alliance;
		
		groups = new HashMap<Group, JLabel>();

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		allianceLabel = new JLabel(alliance.getName());
		allianceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(allianceLabel, BorderLayout.NORTH);

		score = new JLabel("0");
		score.setForeground(alliance.getColor());
		score.setFont(Font.decode("Arial 120"));
		score.setHorizontalAlignment(SwingConstants.CENTER);
		add(score, BorderLayout.CENTER);
		
		groupsPanel = new JPanel(new GridLayout(0, 2));
		for (Group g : GameLoader.sGetGame().getGroups()) {
			Font f = Font.decode("Arial 20");
			
			JLabel labelField = new JLabel(g.getName() + ": ");
			labelField.setFont(f);
			labelField.setHorizontalAlignment(SwingConstants.RIGHT);
			groupsPanel.add(labelField);
			
			JLabel scoreField = new JLabel("0");
			scoreField.setFont(f);
			scoreField.setHorizontalAlignment(SwingConstants.LEFT);
			groupsPanel.add(scoreField);
			groups.put(g, scoreField);
		}
		add(groupsPanel, BorderLayout.SOUTH);
	}
	
	public void updateScore(MatchAlliance a) {
		score.setText(String.valueOf(a.getScore()));
		
		Map<Group, Integer> groupPoints = a.getGroupScores();
		for (Group g : groupPoints.keySet()) {
			JLabel field = groups.get(g);
			field.setText(String.valueOf(groupPoints.get(g)));
		}
	}

	public Alliance getAlliance() {
		return alliance;
	}

	public void setAlliance(Alliance alliance) {
		this.alliance = alliance;
		score.setForeground(alliance.getColor());
	}

}

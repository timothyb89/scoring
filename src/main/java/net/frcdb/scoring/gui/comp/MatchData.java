package net.frcdb.scoring.gui.comp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import net.frcdb.scoring.game.Game;
import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.game.Property;
import net.frcdb.scoring.game.PropertyValue;
import net.frcdb.scoring.gui.ScoreFrame;
import net.frcdb.scoring.match.Match;
import net.frcdb.scoring.match.MatchAdapter;
import net.frcdb.scoring.match.MatchAlliance;
import net.frcdb.scoring.match.MatchManager;
import net.frcdb.scoring.util.StringUtil;

/**
 *
 * @author tim
 */
public class MatchData extends JPanel {

	private ScoreFrame frame;
	private MatchAlliance data;

	private JPanel fields;
	private JTextField finalScoreField;
	private JLabel teamsLabel;

	public MatchData(ScoreFrame frame) {
		this.frame = frame;

		MatchManager.get().addListener(matchListener);
		
		setLayout(new BorderLayout());

		fields = new JPanel(new GridLayout(0, 1));

		Game g = GameLoader.get().getGame();
		for (Property prop : g.getProperties()) {
			PropertyModifier mod = new PropertyModifier(prop);
			fields.add(mod);
		}

		JPanel finalScorePanel = new JPanel(new BorderLayout());

		// final score
		String finalScoreTool = "The final alliance score.";

		JLabel finalScoreLabel = new JLabel("Final Score: ");
		finalScoreLabel.setToolTipText(finalScoreTool);
		finalScoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		finalScoreLabel.setPreferredSize(new Dimension(100, 25));
		finalScorePanel.add(finalScoreLabel, BorderLayout.WEST);

		finalScoreField = new JTextField();
		finalScoreField.setToolTipText(finalScoreTool);
		finalScoreField.setEditable(false);
		finalScorePanel.add(finalScoreField, BorderLayout.CENTER);

		fields.add(finalScorePanel);

		add(fields, BorderLayout.NORTH);

		teamsLabel = new JLabel("<html><b>Teams:</b> Not selected yet.");
		teamsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(teamsLabel, BorderLayout.SOUTH);
	}

	public MatchAlliance getData() {
		return data;
	}

	public void setData(MatchAlliance data) {
		this.data = data;
		
		for (Component comp : fields.getComponents()) {
			if (comp instanceof PropertyModifier) {
				((PropertyModifier) comp).setAlliance(data);
			}
		}

		teamsLabel.setText("<html><b>Teams:</b> " + StringUtil.concat(data.getTeams(), ", "));
	}

	public void update() {
		finalScoreField.setText(String.valueOf(data.getScore()));
		frame.propagateUpdates();
	}
	
	private MatchAdapter matchListener = new MatchAdapter() {

		@Override
		public void onMatchPropertyModified(
				Match match, MatchAlliance alliance, PropertyValue property) {
			if (alliance == data) {
				data.updatePoint(property);
				update();
			}
		}
		
	};
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception ex) {

		}

		JFrame frame = new JFrame("Data test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MatchData data = new MatchData(null);
		MatchAlliance alliance = new MatchAlliance("Red", 1, 2);
		data.setData(alliance);

		frame.add(data);

		frame.pack();
		frame.setSize(300, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}

package net.frcdb.scoring.gui.comp;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.frcdb.scoring.game.Alliance;
import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.match.Match;
import net.frcdb.scoring.match.MatchAdapter;
import net.frcdb.scoring.match.MatchAlliance;
import net.frcdb.scoring.match.MatchManager;
import net.frcdb.scoring.team.Team;
import net.frcdb.scoring.team.TeamDatabase;

/**
 *
 * @author tim
 */
public class QueuePanel extends JPanel {
	
	private Font baseFont;
	private JLabel header;
	private JPanel teams;
	
	public QueuePanel() {
		initComponents();
		
		MatchManager.get().addListener(matchListener);
	}
	
	private void initComponents() {
		setLayout(new BorderLayout());
		
		baseFont = getFont();
		
		teams = new JPanel(new GridLayout(1, 0));
		add(teams, BorderLayout.CENTER);
	}
	
	private MatchAdapter matchListener = new MatchAdapter() {

		@Override
		public void onMatchInitialized(MatchManager manager, Match match) {
			teams.removeAll();
			
			for (MatchAlliance ma : match.getAlliances()) {
				System.out.println("alliance: " + ma.getAlliance());
				Alliance a = GameLoader.sGetGame().getAlliance(ma.getAlliance());
				
				JPanel alliancePanel = new JPanel(new BorderLayout());
				alliancePanel.setBorder(BorderFactory.createTitledBorder(""));
				
				JLabel allianceHeader = new JLabel(a.getName());
				allianceHeader.setHorizontalAlignment(SwingConstants.CENTER);
				allianceHeader.setFont(baseFont.deriveFont(36f).deriveFont(Font.BOLD));
				allianceHeader.setForeground(a.getColor());
				
				alliancePanel.add(allianceHeader, BorderLayout.NORTH);
				
				JPanel teamsPanel = new JPanel(new GridLayout(0, 1));
				for (int tnum : ma.getTeams()) {
					Team t = TeamDatabase.get().getTeam(tnum);
					
					JLabel teamLabel = new JLabel("<html><center>#" + t.getNumber() + "<br>" + t.getName());
					teamLabel.setHorizontalAlignment(SwingConstants.CENTER);
					teamLabel.setFont(baseFont.deriveFont(24f));
					teamsPanel.add(teamLabel);
				}
				alliancePanel.add(teamsPanel, BorderLayout.CENTER);
				
				teams.add(alliancePanel);
			}
			
			teams.validate();
			teams.repaint();
		}

		@Override
		public void onMatchFinished(MatchManager manager, Match match) {
			System.out.println("[Debug] reset");
			teams.removeAll();
			
			JLabel label = new JLabel("Waiting for next match.");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setFont(baseFont.deriveFont(24f));
			teams.add(label, BorderLayout.NORTH);
			
			teams.validate();
			teams.repaint();
		}
		
	};
	
}

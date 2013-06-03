package net.frcdb.scoring.gui.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import net.frcdb.scoring.gui.comp.table.MatchesTable;
import net.frcdb.scoring.gui.comp.table.StandingsTable;

/**
 *
 * @author tim
 */
public class ResetPanel extends JPanel {

	private StandingsTable standings;
	private QueuePanel queue;
	private MatchesTable matches;

	public ResetPanel() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		JPanel left = new JPanel(new GridLayout(0, 1));
		left.setPreferredSize(new Dimension(400, 0));
		
		standings = new StandingsTable();
		standings.setBorder(BorderFactory.createTitledBorder("Standings"));
		left.add(standings);
		
		queue = new QueuePanel();
		queue.setBorder(BorderFactory.createTitledBorder("Next Match"));
		left.add(queue);
		
		add(left, BorderLayout.WEST);

		matches = new MatchesTable();
		matches.setBorder(BorderFactory.createTitledBorder("Match History"));
		add(matches, BorderLayout.CENTER);
	}

	public void update() {
		standings.update();
		matches.update();
	}

}

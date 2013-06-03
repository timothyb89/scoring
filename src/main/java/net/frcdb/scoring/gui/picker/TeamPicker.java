package net.frcdb.scoring.gui.picker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.frcdb.scoring.game.Alliance;
import net.frcdb.scoring.game.Game;
import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.match.MatchAlliance;
import net.frcdb.scoring.team.Team;
import net.frcdb.scoring.team.TeamDatabase;

/**
 * For alliance selection.
 * @author tim
 */
public class TeamPicker extends JDialog {

	private TeamDatabase tdb;
	private List<Team> pool;

	private List<TeamChooser> choosers;

	private Map<Alliance, MatchAlliance> selected;

	public TeamPicker(JFrame owner) {
		super(owner, "Alliance Selection", true);

		tdb = TeamDatabase.get();
		pool = new ArrayList<Team>();
		pool.addAll(tdb.getTeams());

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(closeListener);

		initComponents();

		pack();
		setLocationRelativeTo(owner);
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		JPanel alliances = new JPanel(new GridLayout(1, 0));

		choosers = new ArrayList<TeamChooser>();
		Game game = GameLoader.get().getGame();
		for (Alliance a : game.getAlliances()) {
			TeamChooser chooser = new TeamChooser(this, a, pool);
			chooser.setPreferredSize(new Dimension(200, 200));

			choosers.add(chooser);
			alliances.add(chooser);
		}

		firePoolChanged();

		add(alliances, BorderLayout.CENTER);

		JButton done = new JButton("Finished");
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finish();
			}
		});
		add(done, BorderLayout.SOUTH);

		doFocus();
	}

	public void firePoolChanged() {
		for (TeamChooser tc : choosers) {
			tc.updatePool();
		}
	}

	public void doFocus() {
		if (choosers.size() > 0) {
			choosers.get(0).focus();
		}
	}

	public void finish() {
		selected = new HashMap<Alliance, MatchAlliance>();

		for (TeamChooser chooser : choosers) {
			List<Team> teams = chooser.getSelectedTeams();
			selected.put(
					chooser.getAlliance(), 
					new MatchAlliance(chooser.getAlliance().getName(), teams));
		}

		dispose();
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	public Map<Alliance, MatchAlliance> getSelectedTeams() {
		return selected;
	}

	private WindowAdapter closeListener = new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent e) {
			JOptionPane.showMessageDialog(TeamPicker.this,
					"Please select teams and press the 'finished' button!",
					"Warning", JOptionPane.WARNING_MESSAGE);
		}

	};

	public static Map<Alliance, MatchAlliance> selectTeams(JFrame owner) {
		TeamPicker picker = new TeamPicker(owner);
		picker.setVisible(true);

		return picker.getSelectedTeams();
	}

	public static Map<Alliance, MatchAlliance> selectTeams() {
		return selectTeams(null);
	}

	public static void main(String[] args) {
		Map<Alliance, MatchAlliance> teams = selectTeams();

		for (Alliance a : teams.keySet()) {
			System.out.println("Alliance " + a.getName() + ":");
			for (int i : teams.get(a).getTeams()) {
				System.out.println("\t" + i);
			}
		}
	}

}

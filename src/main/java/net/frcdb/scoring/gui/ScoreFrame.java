package net.frcdb.scoring.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import net.frcdb.scoring.Timer;
import net.frcdb.scoring.game.Alliance;
import net.frcdb.scoring.game.Game;
import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.gui.comp.MatchData;
import net.frcdb.scoring.gui.comp.TimerComponent;
import net.frcdb.scoring.gui.picker.TeamPicker;
import net.frcdb.scoring.match.Match;
import net.frcdb.scoring.match.MatchAdapter;
import net.frcdb.scoring.match.MatchAlliance;
import net.frcdb.scoring.match.MatchDatabase;
import net.frcdb.scoring.match.MatchManager;

/**
 *
 * @author tim
 */
public class ScoreFrame extends JFrame {

	private Game game;

	private MatchManager manager;
	private TimerComponent timerComponent;
	private int matchNumber;

	private Map<Alliance, MatchData> alliancePanels;
	private Map<Alliance, MatchAlliance> alliances;

	private JButton discardButton;
	private JButton startButton;
	private JButton pauseButton;
	private JButton saveButton;

	public ScoreFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		matchNumber = MatchDatabase.get().getMatches().size() + 1;
		setTitle("Scoring for Match #" + matchNumber);

		game = GameLoader.get().getGame();
		manager = MatchManager.get();
		manager.addListener(matchListener);

		alliancePanels = new HashMap<Alliance, MatchData>();

		initComponents();

		pack();
		setSize(500, 700);
		setVisible(true);

		if (manager.isMatchInProgress()) {
			alliances = new HashMap<Alliance, MatchAlliance>();
			
			for (MatchAlliance m : manager.getCurrentMatch().getAlliances()) {
				Alliance a = GameLoader.sGetGame().getAlliance(m.getAlliance());
				alliances.put(a, m);
			}
		} else {
			alliances = TeamPicker.selectTeams(this);

			List<MatchAlliance> as = new ArrayList<MatchAlliance>();
			as.addAll(alliances.values());
			MatchManager.get().initializeMatch(as);
		}

		for (Alliance a : alliances.keySet()) {
			alliancePanels.get(a).setData(alliances.get(a));
		}
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		JPanel topPanels = new JPanel(new GridLayout(1, 0));

		for (Alliance a : game.getAlliances()) {
			JPanel panel = new JPanel(new BorderLayout());
			panel.setBorder(BorderFactory.createTitledBorder(a.getName()));

			MatchData data = new MatchData(this);
			alliancePanels.put(a, data);

			panel.add(data, BorderLayout.CENTER);

			topPanels.add(panel);
		}

		add(topPanels, BorderLayout.CENTER);

		JPanel bottom = new JPanel(new BorderLayout());

		timerComponent = new TimerComponent();
		bottom.add(timerComponent, BorderLayout.CENTER);

		JPanel buttonsMain = new JPanel(new BorderLayout());
		buttonsMain.add(new JSeparator(), BorderLayout.NORTH);

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		discardButton = new JButton("Discard");
		discardButton.addActionListener(discardListener);
		buttons.add(discardButton);
		
		startButton = new JButton("Start");
		startButton.addActionListener(startListener);
		buttons.add(startButton);
		
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(pauseListener);
		pauseButton.setEnabled(false);
		buttons.add(pauseButton);

		saveButton = new JButton("Save");
		saveButton.addActionListener(saveListener);
		buttons.add(saveButton);

		buttonsMain.add(buttons, BorderLayout.CENTER);

		bottom.add(buttonsMain, BorderLayout.SOUTH);

		add(bottom, BorderLayout.SOUTH);
	}

	private MatchAdapter matchListener = new MatchAdapter() {
		
		@Override
		public void onMatchStarted(MatchManager manager, Match match) {
			startButton.setText("Stop");
			pauseButton.setEnabled(true);
		}

		@Override
		public void onMatchEnded(MatchManager manager, Match match) {
			startButton.setText("Start");
			pauseButton.setEnabled(false);
		}

		@Override
		public void onMatchPaused(MatchManager manager, Match match) {
			pauseButton.setText("Resume");
		}

		@Override
		public void onMatchResumed(MatchManager manager, Match match) {
			pauseButton.setText("Pause");
		}

		@Override
		public void onMatchFinished(MatchManager manager, Match match) {
			dispose();
		}

	};

	private ActionListener discardListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			manager.finishMatch();
		}
		
	};
	
	private ActionListener startListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if (manager.isMatchInProgress()) {
				manager.stopMatch();
			} else {
				manager.startMatch();
			}
		}

	};
	
	private ActionListener pauseListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if (!Timer.get().isPaused()) {
				manager.pauseMatch();
			} else {
				manager.resumeMatch();
			}
		}
		
	};

	private ActionListener saveListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			MatchManager.get().finishMatch(true);
		}

	};
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception ex) {
		}

		new ScoreFrame().setVisible(true);
	}

	public void propagateUpdates() {
		List<MatchAlliance> as = new ArrayList<MatchAlliance>();
		for (MatchData d : alliancePanels.values()) {
			as.add(d.getData());
		}
	}

}

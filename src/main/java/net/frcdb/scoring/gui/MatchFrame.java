package net.frcdb.scoring.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.frcdb.scoring.game.Alliance;
import net.frcdb.scoring.game.Game;
import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.game.PropertyValue;
import net.frcdb.scoring.gui.comp.MatchPanel;
import net.frcdb.scoring.gui.comp.ResetPanel;
import net.frcdb.scoring.match.Match;
import net.frcdb.scoring.match.MatchAdapter;
import net.frcdb.scoring.match.MatchAlliance;
import net.frcdb.scoring.match.MatchManager;

/**
 *
 * @author tim
 */
public class MatchFrame extends JFrame {
	
	private static MatchFrame instance;
	private static Game game = GameLoader.get().getGame();

	private ResetPanel reset;
	private MatchPanel match;

	private JPanel cards;
	
	private MatchFrame() {
		super("Scoring for " + game.getName());
		
		MatchManager.get().addListener(matchListener);
		
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		cards = new JPanel(new CardLayout());
		
		reset = new ResetPanel();
		cards.add(reset, "reset");
		
		match = new MatchPanel();
		cards.add(match, "match");

		add(cards, BorderLayout.CENTER);

		pack();
		setExtendedState(MAXIMIZED_BOTH);
	}

	public static MatchFrame get() {
		if (instance == null) {
			instance = new MatchFrame();
		}
		
		return instance;
	}
	
	private MatchAdapter matchListener = new MatchAdapter() {

		@Override
		public void onMatchInitialized(MatchManager manager, Match match) {
			setAlliances(GameLoader.get().getGame().getAlliances());
		}
		
		@Override
		public void onMatchStarted(MatchManager manager, Match match) {
			showMatch();
			updateMatches();
		}

		@Override
		public void onMatchFinished(MatchManager manager, Match match) {
			showReset();
			updateMatches();
		}
		
	};
	
	public MatchPanel getMatch() {
		return match;
	}

	public ResetPanel getReset() {
		return reset;
	}

	public static void showMatch() {
		get().setVisible(true);
		get()._showMatch();
	}
	
	public void _showMatch() {
		CardLayout cl = (CardLayout) cards.getLayout();
		cl.show(cards, "match");
	}

	public static void showReset() {
		get().setVisible(true);
		get()._showReset();
	}

	public void _showReset() {
		CardLayout cl = (CardLayout) cards.getLayout();
		cl.show(cards, "reset");
	}

	public static void updateReset() {
		get()._updateReset();
	}

	public void _updateReset() {
		reset.update();
	}

	public static void updateMatches() {
		get().reset.update();
		get().match.updateMatches();
	}

	public static void setAlliances(List<Alliance> alliances) {
		get().match.setAlliances(alliances);
	}
	
}

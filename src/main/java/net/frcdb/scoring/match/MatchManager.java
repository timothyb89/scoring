package net.frcdb.scoring.match;

import java.util.ArrayList;
import java.util.List;
import net.frcdb.scoring.Timer;
import net.frcdb.scoring.audio.SoundListener;
import net.frcdb.scoring.game.GamePeriod;
import net.frcdb.scoring.game.PropertyValue;

/**
 * Controls the starting, stopping, pausing, and point assignment during a 
 * match.
 * @author tim
 */
public class MatchManager {
	
	private static MatchManager instance;
	
	private List<MatchListener> listeners;
	private Timer timer;
	private Match currentMatch;
	private boolean matchInProgress;
	
	public MatchManager() {
		listeners = new ArrayList<MatchListener>();
		timer = Timer.get();
		matchInProgress = false;
		
		addListener(new SoundListener());
	}
	
	public static MatchManager get() {
		if (instance == null) {
			instance = new MatchManager();
		}
		
		return instance;
	}

	public Timer getTimer() {
		return timer;
	}
	
	public int getTimeLeft() {
		return timer.getTimeLeft();
	}
	
	public void addListener(MatchListener l) {
		listeners.add(l);
	}
	
	public void removeListener(MatchListener l) {
		listeners.remove(l);
	}

	public Match getCurrentMatch() {
		return currentMatch;
	}
	
	public boolean isMatchInProgress() {
		return matchInProgress;
	}
	
	public boolean isFinished() {
		return timer.getTimeLeft() <= 0;
	}
	
	public boolean isPaused() {
		return timer.isPaused();
	}
	
	public void initializeMatch(List<MatchAlliance> alliances) {
		int number = MatchDatabase.get().getMatches().size() + 1;
		
		currentMatch = new Match(number, alliances);
		currentMatch.setAlliances(alliances);
		
		for (MatchListener l : listeners) {
			l.onMatchInitialized(this, currentMatch);
		}
	}
	
	/**
	 * Starts the previously initialized match
	 */
	public void startMatch() {
		if (currentMatch == null) {
			throw new IllegalStateException("There is no current match.");
		}
		
		timer.start();
		matchInProgress = true;
		
		for (MatchListener l : listeners) {
			l.onMatchStarted(this, currentMatch);
		}
	}
	
	public void stopMatch() {
		if (currentMatch == null) {
			throw new IllegalStateException("There is no current match.");
		}
		
		timer.stop();
		matchInProgress = false;
		
		for (MatchListener l : listeners) {
			l.onMatchEnded(this, currentMatch);
		}
	}
	
	public void resetMatch() {
		if (currentMatch == null) {
			throw new IllegalStateException("There is no current match.");
		}
		
		timer.reset();
	}
	
	public void pauseMatch() {
		if (currentMatch == null) {
			throw new IllegalStateException("There is no current match.");
		}
		
		timer.pause();
		
		for (MatchListener l : listeners) {
			l.onMatchPaused(this, currentMatch);
		}
	}
	
	public void resumeMatch() {
		if (currentMatch == null) {
			throw new IllegalStateException("There is no current match.");
		}
		
		timer.resume();
		
		for (MatchListener l : listeners) {
			l.onMatchResumed(this, currentMatch);
		}
	}
	
	/**
	 * Finishes and saves the current match
	 */
	public void finishMatch() {
		finishMatch(false);
	}
	
	public void finishMatch(boolean save) {
		if (currentMatch == null) {
			throw new IllegalStateException("There is no current match.");
		}
		
		matchInProgress = false;
		
		if (save) {
			MatchDatabase.get().addMatch(currentMatch);
			MatchDatabase.get().save();
		}
		
		timer.reset();
		
		for (MatchListener l : listeners) {
			l.onMatchFinished(this, currentMatch);
		}
		
		currentMatch = null;
	}
	
	public void notifyTimerUpdated() {
		for (MatchListener l : listeners) {
			l.onMatchTimerUpdated(timer);
		}
	}
	
	public void notifyPeriodChanged(GamePeriod period) {
		for (MatchListener l : listeners) {
			l.onMatchPeriodChanged(this, currentMatch, period);
		}
	}
	
	public void notifyPropertyChanged(MatchAlliance alliance, PropertyValue prop) {
		for (MatchListener l : listeners) {
			l.onMatchPropertyModified(currentMatch, alliance, prop);
		}
	}
	
}

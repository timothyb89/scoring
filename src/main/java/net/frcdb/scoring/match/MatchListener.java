package net.frcdb.scoring.match;

import net.frcdb.scoring.Timer;
import net.frcdb.scoring.game.GamePeriod;
import net.frcdb.scoring.game.PropertyValue;

/**
 *
 * @author tim
 */
public interface MatchListener {
	
	public void onMatchInitialized(MatchManager manager, Match match);
	public void onMatchStarted(MatchManager manager, Match match);
	public void onMatchEnded(MatchManager manager, Match match);
	public void onMatchTimerUpdated(Timer timer);
	
	public void onMatchPeriodChanged(
			MatchManager manager, Match match, GamePeriod period);
	
	public void onMatchPaused(MatchManager manager, Match match);
	public void onMatchResumed(MatchManager manager, Match match);
	public void onMatchFinished(MatchManager manager, Match match);
	
	public void onMatchPropertyModified(
			Match match, MatchAlliance alliance, PropertyValue property);
	
}

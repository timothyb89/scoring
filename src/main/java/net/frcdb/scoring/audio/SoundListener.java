package net.frcdb.scoring.audio;

import java.io.File;
import net.frcdb.scoring.Timer;
import net.frcdb.scoring.game.GamePeriod;
import net.frcdb.scoring.game.PropertyValue;
import net.frcdb.scoring.match.Match;
import net.frcdb.scoring.match.MatchAdapter;
import net.frcdb.scoring.match.MatchAlliance;
import net.frcdb.scoring.match.MatchListener;
import net.frcdb.scoring.match.MatchManager;

/**
 *
 * @author tim
 */
public class SoundListener extends MatchAdapter {

	@Override
	public void onMatchPeriodChanged(
			MatchManager manager, Match match, GamePeriod period) {
		if (period.getSound() != null) {
			File file = new File(period.getSound());

			if (file.exists()) {
				System.out.println("[Debug] Playing: " + file.getPath());
				Audio.play(file);
			} else {
				System.out.println("[Warning] Audio file nonexistent: " +
						file.getPath());
			}
		}
	}

}

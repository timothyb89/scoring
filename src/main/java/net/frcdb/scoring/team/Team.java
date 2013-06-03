package net.frcdb.scoring.team;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import net.frcdb.scoring.match.Match;
import net.frcdb.scoring.match.MatchAlliance;
import net.frcdb.scoring.match.MatchDatabase;

/**
 *
 * @author tim
 */
@XStreamAlias("team")
public class Team {

	@XStreamAsAttribute
	private int number;

	@XStreamAsAttribute
	private String name;

	public Team(int number, String name) {
		this.number = number;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return getName() + " (#" + getNumber() + ")";
	}

	public int getWins() {
		int wins = 0;

		for (Match m : MatchDatabase.get().getMatchesForTeam(number)) {
			MatchAlliance a = m.getAlliance(number);

			if (m.getWinners().contains(a) && !m.getLosers().contains(a)) {
				wins++;
			}
		}

		return wins;
	}

	public int getTies() {
		int ties = 0;

		for (Match m : MatchDatabase.get().getMatchesForTeam(number)) {
			MatchAlliance a = m.getAlliance(number);

			if (m.getWinners().contains(a) && m.getLosers().contains(a)) {
				ties++;
			}
		}

		return ties;
	}

	public int getLosses() {
		int losses = 0;

		for (Match m : MatchDatabase.get().getMatchesForTeam(number)) {
			MatchAlliance a = m.getAlliance(number);

			if (!m.getWinners().contains(a) && m.getLosers().contains(a)) {
				losses++;
			}
		}

		return losses;
	}

	public int getTotalPoints() {
		int total = 0;

		for (Match m : MatchDatabase.get().getMatchesForTeam(number)) {
			MatchAlliance alliance = m.getAlliance(number);

			total += alliance.getScore();
		}

		return total;
	}

}

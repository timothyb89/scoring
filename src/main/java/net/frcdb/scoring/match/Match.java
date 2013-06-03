package net.frcdb.scoring.match;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import net.frcdb.scoring.game.Alliance;

/**
 *
 * @author tim
 */
@XStreamAlias("match")
public class Match {

	@XStreamAsAttribute
	@Getter
	private int number;
	private List<MatchAlliance> alliances;

	public Match(int number, List<MatchAlliance> alliances) {
		this.number = number;
		this.alliances = alliances;
	}

	public List<MatchAlliance> getAlliances() {
		return alliances;
	}

	public void setAlliances(List<MatchAlliance> alliances) {
		this.alliances = alliances;
	}

	public List<Integer> getTeams() {
		List<Integer> ret = new ArrayList<Integer>();

		for (MatchAlliance a : alliances) {
			ret.addAll(a.getTeams());
		}

		return ret;
	}

	public MatchAlliance getAlliance(int team) {
		for (MatchAlliance a : alliances) {
			if (a.getTeams().contains(team)) {
				return a;
			}
		}

		return null;
	}

	public MatchAlliance getAlliance(Alliance a) {
		for (MatchAlliance ma : alliances) {
			if (ma.getAlliance().equalsIgnoreCase(a.getName())) {
				return ma;
			}
		}

		return null;
	}

	public List<MatchAlliance> getWinners() {
		List<MatchAlliance> ret = new ArrayList<MatchAlliance>();

		MatchAlliance top = null;
		for (MatchAlliance a : alliances) {
			if (top == null) {
				top = a;
			} else {
				if (a.getScore() > top.getScore()) {
					top = a;
				}
			}
		}

		if (top == null) {
			return ret;
		}

		for (MatchAlliance a : alliances) {
			if (a.getScore() == top.getScore()) {
				ret.add(a);
			}
		}

		return ret;
	}

	public List<MatchAlliance> getLosers() {
		List<MatchAlliance> ret = new ArrayList<MatchAlliance>();
		
		MatchAlliance bottom = null;
		for (MatchAlliance a : alliances) {
			if (bottom == null) {
				bottom = a;
			} else {
				if (a.getScore() < bottom.getScore()) {
					bottom = a;
				}
			}
		}

		if (bottom == null) {
			return ret;
		}

		for (MatchAlliance a : alliances) {
			if (a.getScore() == bottom.getScore()) {
				ret.add(a);
			}
		}

		return ret;
	}

}

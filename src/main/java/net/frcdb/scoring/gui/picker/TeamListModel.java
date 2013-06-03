package net.frcdb.scoring.gui.picker;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import net.frcdb.scoring.team.Team;

/**
 *
 * @author tim
 */
public class TeamListModel extends AbstractListModel {

	private List<Team> teams;

	public TeamListModel() {
		teams = new ArrayList<Team>();
	}

	public TeamListModel(List<Team> teams) {
		this.teams = teams;
	}

	public int getSize() {
		return teams.size();
	}

	public Object getElementAt(int index) {
		return teams.get(index);
	}

	public void add(Team team) {
		teams.add(team);

		fireIntervalAdded(this, teams.size() - 1, teams.size() - 1);
	}

	public void remove(Team team) {
		if (!teams.contains(team)) {
			return;
		}

		int index = teams.indexOf(team);

		teams.remove(team);
		fireIntervalRemoved(this, index, index);
	}

	public List<Team> getTeams() {
		return teams;
	}

}

package net.frcdb.scoring.gui.comp.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import net.frcdb.scoring.team.Team;
import net.frcdb.scoring.team.TeamDatabase;

/**
 *
 * @author tim
 */
public class StandingsModel extends AbstractTableModel {

	private static String[] COLUMNS =
			{"Rank", "Team", "Wins", "Ties", "Losses", "Total Points"};

	private List<Team> teams;

	public StandingsModel() {
		teams = new ArrayList<Team>();
		teams.addAll(TeamDatabase.get().getTeams());
		Collections.sort(teams, teamComparator);
	}

	public int getRowCount() {
		return teams.size();
	}

	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public String getColumnName(int column) {
		return COLUMNS[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Team team = teams.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return rowIndex + 1;
			case 1:
				return team.getNumber();
			case 2:
				return team.getWins();
			case 3:
				return team.getTies();
			case 4:
				return team.getLosses();
			case 5:
				return team.getTotalPoints();
			default:
				return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	private Comparator<Team> teamComparator = new Comparator<Team>() {

		public int compare(Team o1, Team o2) {
			int winsA = o1.getWins();
			int winsB = o2.getWins();

			if (winsA > winsB) {
				return -1;
			} else if (winsA < winsB) {
				return 1;
			} else {
				int totalA = o1.getTotalPoints();
				int totalB = o2.getTotalPoints();

				if (totalA > totalB) {
					return -1;
				} else if (totalA < totalB) {
					return 1;
				} else {
					return 0;
				}
			}
		}

	};

}

package net.frcdb.scoring.gui.comp.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import net.frcdb.scoring.game.Alliance;
import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.match.Match;
import net.frcdb.scoring.match.MatchAlliance;
import net.frcdb.scoring.match.MatchDatabase;
import net.frcdb.scoring.util.StringUtil;

/**
 *
 * @author tim
 */
public class MatchesModel extends AbstractTableModel {

	private List<String> columns;
	private List<Match> matches;

	public MatchesModel() {
		columns = new ArrayList<String>();
		initColumns();

		matches = new ArrayList<Match>();
		matches.addAll(MatchDatabase.get().getMatches());
	}

	private void initColumns() {
		columns.add("#");
		for (Alliance a : GameLoader.get().getGame().getAlliances()) {
			columns.add(a.getName() + " Teams");
			columns.add(a.getName() + " Score");
		}
		columns.add("Winner");
	}

	public int getRowCount() {
		return matches.size();
	}

	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public String getColumnName(int column) {
		return columns.get(column);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Match match = matches.get(rowIndex);

		if (columnIndex == 0) {
			return match.getNumber();
		} else if (columnIndex == getColumnCount() - 1) {
			List<String> winners = new ArrayList<String>();
			for (MatchAlliance ma : match.getWinners()) {
				winners.add(ma.getAlliance());
			}

			return StringUtil.concat(winners, ", ");
		} else if (columnIndex % 2 == 1) { // teams
			int allianceIndex = (columnIndex - 1) / 2;

			Alliance a = GameLoader.get().getGame().getAlliances().get(allianceIndex);
			MatchAlliance ma = match.getAlliance(a);

			return StringUtil.concat(ma.getTeams(), ", ");
		} else if (columnIndex % 2 == 0) { // score
			int allianceIndex = (columnIndex - 1) / 2;

			Alliance a = GameLoader.get().getGame().getAlliances().get(allianceIndex);
			MatchAlliance ma = match.getAlliance(a);

			return ma.getScore();
		} else {
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}

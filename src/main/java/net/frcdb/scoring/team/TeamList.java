package net.frcdb.scoring.team;

import java.util.List;

/**
 *
 * @author tim
 */
public class TeamList {

	private List<Team> red;
	private List<Team> blue;

	public TeamList() {
	}

	public TeamList(List<Team> red, List<Team> blue) {
		this.red = red;
		this.blue = blue;
	}

	public List<Team> getRed() {
		return red;
	}

	public void setRed(List<Team> red) {
		this.red = red;
	}

	public List<Team> getBlue() {
		return blue;
	}

	public void setBlue(List<Team> blue) {
		this.blue = blue;
	}

}

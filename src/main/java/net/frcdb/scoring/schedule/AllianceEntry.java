package net.frcdb.scoring.schedule;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim
 */
@XStreamAlias("alliance")
public class AllianceEntry {
	
	@XStreamAlias("name")
	@XStreamAsAttribute
	private String allianceName;
	
	@XStreamImplicit
	@XStreamAsAttribute
	private List<Integer> teams;

	public AllianceEntry(String allianceName, List<Integer> teams) {
		this.allianceName = allianceName;
		this.teams = teams;
	}

	public AllianceEntry() {
		teams = new ArrayList<Integer>();
	}

	public String getAllianceName() {
		return allianceName;
	}

	public void setAllianceName(String allianceName) {
		this.allianceName = allianceName;
	}

	public List<Integer> getTeams() {
		return teams;
	}

	public void setTeams(List<Integer> teams) {
		this.teams = teams;
	}
	
}

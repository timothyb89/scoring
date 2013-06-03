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
@XStreamAlias("match")
public class ScheduledMatch {
	
	@XStreamAsAttribute
	private int number;
	
	@XStreamImplicit
	private List<AllianceEntry> alliances;

	public ScheduledMatch() {
		alliances = new ArrayList<AllianceEntry>();
	}
	
	public ScheduledMatch(int number) {
		this.number = number;
		
		alliances = new ArrayList<AllianceEntry>();
	}

	public ScheduledMatch(int number, List<AllianceEntry> alliances) {
		this.number = number;
		this.alliances = alliances;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<AllianceEntry> getAlliances() {
		return alliances;
	}

	public void setAlliances(List<AllianceEntry> alliances) {
		this.alliances = alliances;
	}
	
}

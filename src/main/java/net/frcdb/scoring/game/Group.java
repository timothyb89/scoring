package net.frcdb.scoring.game;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Defines a scoring group. Scoring groups define a set of scoring parameters
 * which will be combined and displayed on-screen in the match UI. The actual
 * group definition is simple and is primarily intended for ordering and naming.
 * @author tim
 */
@XStreamAlias("group")
public class Group {
	
	@XStreamAsAttribute
	private String name;

	public Group() {
	}

	public Group(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

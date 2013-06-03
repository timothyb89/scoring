package net.frcdb.scoring.game;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author tim
 */
@XStreamAlias("period")
public class GamePeriod {

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private int start;

	@XStreamAsAttribute
	private String sound;

	public GamePeriod(String name, int start) {
		this.name = name;
		this.start = start;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

}

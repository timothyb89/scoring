package net.frcdb.scoring.game;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author tim
 */
@XStreamAlias("constraints")
public class Constraints {
	
	public static final int DEFAULT_MINIMUM = 0;
	public static final int DEFAULT_MAXIMUM = Integer.MAX_VALUE;
	
	@XStreamAsAttribute
	private int minimum;
	
	@XStreamAsAttribute
	private int maximum;
	
	@XStreamAsAttribute
	private boolean exclusive;

	public Constraints() {
		minimum = DEFAULT_MINIMUM;
		maximum = DEFAULT_MAXIMUM;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}
	
}

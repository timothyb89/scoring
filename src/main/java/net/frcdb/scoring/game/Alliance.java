package net.frcdb.scoring.game;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.awt.Color;

/**
 *
 * @author tim
 */
@XStreamAlias("alliance")
public class Alliance {

	public static final Color DEFAULT_COLOR = Color.white;

	@XStreamAsAttribute
	private String name;
	private Color color;

	public Alliance(String name) {
		this.name = name;

		color = DEFAULT_COLOR;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}

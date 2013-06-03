package net.frcdb.scoring.game;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author tim
 */
@XStreamAlias("value")
public class PropertyValue {

	@XStreamAsAttribute
	private String name;
	
	@XStreamAsAttribute
	private int value;

	public PropertyValue() {
	}

	public PropertyValue(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}

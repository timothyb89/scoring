package net.frcdb.scoring.game;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim
 */
@XStreamAlias("property")
public class Property {

	public static final int DEFAULT_DEFAULT_VALUE = 0;
	
	@XStreamAsAttribute
	private String name;
	
	@XStreamAsAttribute
	private String group;
	
	@XStreamAsAttribute
	@XStreamAlias("default")
	private int defaultValue;

	private Constraints constraints;

	@XStreamImplicit
	private List<Multiplier> multipliers;

	public Property(String name) {
		this.name = name;

		defaultValue = DEFAULT_DEFAULT_VALUE;
		
		multipliers = new ArrayList<Multiplier>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public Constraints getConstraints() {
		return constraints;
	}

	public void setConstraints(Constraints constraints) {
		this.constraints = constraints;
	}

	public List<Multiplier> getMultipliers() {
		return multipliers;
	}

	public void setMultipliers(List<Multiplier> multipliers) {
		this.multipliers = multipliers;
	}

}

package net.frcdb.scoring.game;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim
 */
@XStreamAlias("game")
public class Game {

	@XStreamAsAttribute
	private String name;

	private GamePeriods periods;
	
	@XStreamAlias("scheduler")
	private SchedulerConfiguration schedulerConfig;

	private List<Alliance> alliances;
	private List<Group> groups;
	private List<Property> properties;

	public Game(String name) {
		this.name = name;
		
		alliances = new ArrayList<Alliance>();
		groups = new ArrayList<Group>();
		properties = new ArrayList<Property>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GamePeriods getPeriods() {
		return periods;
	}

	public void setPeriods(GamePeriods periods) {
		this.periods = periods;
	}

	public SchedulerConfiguration getSchedulerConfig() {
		return schedulerConfig;
	}

	public void setSchedulerConfig(SchedulerConfiguration schedulerConfig) {
		this.schedulerConfig = schedulerConfig;
	}
	
	public List<Alliance> getAlliances() {
		return alliances;
	}

	public void setAlliances(List<Alliance> alliances) {
		this.alliances = alliances;
	}

	public void addAlliance(Alliance a) {
		alliances.add(a);
	}

	public void addAlliance(String name, Color color) {
		Alliance a = new Alliance(name);
		a.setColor(color);

		addAlliance(a);
	}

	public Alliance getAlliance(String name) {
		for (Alliance a : alliances) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		
		return null;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	public Property getProperty(String name) {
		for (Property p : properties) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		
		return null;
	}

}

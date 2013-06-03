package net.frcdb.scoring.match;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.game.Group;
import net.frcdb.scoring.game.Multiplier;
import net.frcdb.scoring.game.Property;
import net.frcdb.scoring.game.PropertyValue;
import net.frcdb.scoring.team.Team;

/**
 *
 * @author tim
 */
@XStreamAlias("alliance")
public class MatchAlliance {

	@XStreamAsAttribute
	private String alliance;
	private List<Integer> teams;
	private List<PropertyValue> points;

	public MatchAlliance(String alliance, Integer... teams) {
		this.alliance = alliance;

		this.teams = Arrays.asList(teams);
		points = new ArrayList<PropertyValue>();
	}

	public MatchAlliance(String alliance, List<Team> teams) {
		this.alliance = alliance;

		this.teams = new ArrayList<Integer>();
		for (Team t : teams) {
			this.teams.add(t.getNumber());
		}

		points = new ArrayList<PropertyValue>();
		initDefaultValues();
	}
	
	private void initDefaultValues() {
		for (Property p : GameLoader.sGetGame().getProperties()) {
			points.add(new PropertyValue(p.getName(), p.getDefaultValue()));
		}
	}

	public String getAlliance() {
		return alliance;
	}

	public void setAlliance(String alliance) {
		this.alliance = alliance;
	}

	public int getScore() {
		int total = 0;

		for (Property prop : GameLoader.get().getGame().getProperties()) {
			PropertyValue value = getPoint(prop.getName());

			if (value != null) {
				int val = value.getValue();
				if (val < prop.getConstraints().getMinimum()) {
					val = prop.getConstraints().getMinimum();
				}

				if (val > prop.getConstraints().getMaximum()) {
					val = prop.getConstraints().getMaximum();
				}
				
				// don't let exponential scores have 1 for val=0
				if (val != 0) {
					for (Multiplier m : prop.getMultipliers()) {
						total += m.apply(val);
					}
				}
			}
		}

		return total;
	}

	public Map<Group, Integer> getGroupScores() {
		Map<Group, Integer> ret = new HashMap<Group, Integer>();
		
		for (Group g : GameLoader.sGetGame().getGroups()) {
			int groupTotal = 0;
			
			for (PropertyValue point : points) {
				Property p = GameLoader.sGetGame().getProperty(point.getName());
				if (p.getGroup() != null
						&& p.getGroup().equalsIgnoreCase(g.getName())) {
					// add all the point multipliers
					if (point.getValue() != 0) {
						for (Multiplier m : p.getMultipliers()) {
							groupTotal += m.apply(point.getValue());
						}
					}
				}
			}
			
			ret.put(g, groupTotal);
		}
		
		return ret;
	}
	
	public List<Integer> getTeams() {
		return teams;
	}

	public List<PropertyValue> getPoints() {
		return points;
	}
	
	public PropertyValue getPoint(String prop) {
		for (PropertyValue pv : points) {
			if (pv.getName().equalsIgnoreCase(prop)) {
				return pv;
			}
		}

		return null;
	}

	/**
	 * Sets the given point, overwriting previous values with the same name.
	 * @param point The point to set
	 */
	public void updatePoint(PropertyValue point) {
		List<PropertyValue> copy = new ArrayList<PropertyValue>();
		copy.addAll(points);
		for (PropertyValue pv : copy) {
			if (pv.getName().equalsIgnoreCase(point.getName())) {
				points.remove(pv);
			}
		}

		points.add(point);
	}

	public interface PropertyChangeListener {
		public void onPropertyChanged(Property property, PropertyValue value);
	}
	
}

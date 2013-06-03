package net.frcdb.scoring.game;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author tim
 */
@XStreamAlias("length")
public class GamePeriods {

	@XStreamAsAttribute
	private int length;

	@XStreamImplicit
	private List<GamePeriod> periods;

	public GamePeriods(int length) {
		this.length = length;

		periods = new ArrayList<GamePeriod>();
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<GamePeriod> getPeriods() {
		return periods;
	}

	public List<GamePeriod> getOrderedPeriods() {
		List<GamePeriod> ret = new ArrayList<GamePeriod>();
		ret.addAll(periods);

		Collections.sort(ret, new Comparator<GamePeriod>() {

			public int compare(GamePeriod o1, GamePeriod o2) {
				Integer a = o2.getStart();
				Integer b = o1.getStart();

				return a.compareTo(b);
			}

		});

		return ret;
	}

	public void addPeriod(GamePeriod p) {
		periods.add(p);
	}

	public void addPeriod(String name, int start) {
		addPeriod(new GamePeriod(name, start));
	}

	public void removePeriod(GamePeriod p) {
		periods.remove(p);
	}

}

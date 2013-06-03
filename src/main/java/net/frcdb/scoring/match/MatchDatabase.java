package net.frcdb.scoring.match;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author timTeam
 */
public class MatchDatabase {

	private static MatchDatabase instance;

	public static final File DEFAULT_FILE = new File("data/matches.xml");

	private XStream xstream;
	private List<Match> matches;

	private MatchDatabase() {
		initXStream();
		load();
	}

	public static MatchDatabase get() {
		if (instance == null) {
			instance = new MatchDatabase();
		}

		return instance;
	}

	private void initXStream() {
		xstream = new XStream();
		xstream.processAnnotations(new Class[] {Match.class, MatchAlliance.class});
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void addMatch(Match match) {
		matches.add(match);
	}

	public void removeMatch(Match match) {
		matches.remove(match);
	}

	public List<Match> getMatchesForTeam(int team) {
		List<Match> ret = new ArrayList<Match>();

		for (Match m : matches) {
			if (m.getTeams().contains(team)) {
				ret.add(m);
			}
		}

		return ret;
	}

	public void load(File file) {
		if (file.exists()) {
			try {
				InputStream is = file.toURI().toURL().openStream();
				matches = (ArrayList<Match>) xstream.fromXML(is);
				is.close();
			} catch (Exception ex) {
				System.out.println("[Error] Failed loading matches from " +
						file.getPath());
				ex.printStackTrace();

				matches = new ArrayList<Match>();
			}
		} else {
			matches = new ArrayList<Match>();
		}
	}

	public void load() {
		load(DEFAULT_FILE);
	}

	public void save(File file) {
		try {
			FileWriter fw = new FileWriter(file);
			xstream.toXML(matches, fw);
			fw.close();
		} catch (Exception ex) {
			System.out.println("[Error] Failed saving match list to " +
					file.getPath());
			ex.printStackTrace();
		}
	}

	public void save() {
		save(DEFAULT_FILE);
	}

}

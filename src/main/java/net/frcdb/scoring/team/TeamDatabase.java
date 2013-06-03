package net.frcdb.scoring.team;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim
 */
public class TeamDatabase {

	private static TeamDatabase instance;

	public static final File DEFAULT_FILE = new File("data/teams.xml");

	private XStream xstream;
	private List<Team> teams;

	private TeamDatabase() {
		initXStream();
		load();
	}

	public static TeamDatabase get() {
		if (instance == null) {
			instance = new TeamDatabase();
		}

		return instance;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public Team getTeam(String name) {
		for (Team t : teams) {
			if (t.getName().equalsIgnoreCase(name)
					|| String.valueOf(t.getNumber()).equals(name)) {
				return t;
			}
		}

		return null;
	}

	public Team getTeam(int number) {
		for (Team t : teams) {
			if (t.getNumber() == number) {
				return t;
			}
		}

		return null;
	}

	public void addTeam(Team t) {
		teams.add(t);
	}
	
	public void removeTeam(Team t) {
		teams.remove(t);
	}

	private void initXStream() {
		xstream = new XStream();
		xstream.processAnnotations(new Class[] {TeamDatabase.class, Team.class});
	}

	public void load(File file) {
		if (file.exists()) {
			try {
				InputStream is = file.toURI().toURL().openStream();
				teams = (ArrayList<Team>) xstream.fromXML(is);
				is.close();
			} catch (Exception ex) {
				System.out.println("[Error] Failed loading teams from " +
						file.getPath());
				ex.printStackTrace();

				teams = new ArrayList<Team>();
			}
		} else {
			teams = new ArrayList<Team>();
		}
	}

	public final void load() {
		load(DEFAULT_FILE);
	}

	public void save(File file) {
		try {
			FileWriter fw = new FileWriter(file);
			xstream.toXML(teams, fw);
			fw.close();
		} catch (Exception ex) {
			System.out.println("[Error] Failed saving team list to " +
					file.getPath());
			ex.printStackTrace();
		}
	}

	public void save() {
		save(DEFAULT_FILE);
	}

}

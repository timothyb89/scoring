package net.frcdb.scoring.game;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

/**
 *
 * @author tim
 */
public class GameLoader {

	private static GameLoader instance;

	public static final File DEFAULT_FILE = new File("data/game.xml");

	private XStream xstream;
	private Game game;

	private GameLoader() {
		initXStream();
		load();
	}

	public static GameLoader get() {
		if (instance == null) {
			instance = new GameLoader();
		}

		return instance;
	}
	
	public static Game sGetGame() {
		return get().getGame();
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	private void initXStream() {
		xstream = new XStream();
		xstream.processAnnotations(new Class[] {
			Game.class, Alliance.class, Property.class,
			SchedulerConfiguration.class, Group.class
		});
	}

	public void load(File file) {
		if (file.exists()) {
			try {
				InputStream is = file.toURI().toURL().openStream();
				game = (Game) xstream.fromXML(is);
				is.close();
			} catch (Exception ex) {
				System.err.println("[Error] Failed loading game config from " +
						file.getPath());
				ex.printStackTrace();
			}
		} else {
			System.err.println("[Error] Game config not found at " + file.getPath());
		}
	}

	public void load() {
		load(DEFAULT_FILE);
	}

	public void save(File file) {
		try {
			FileWriter fw = new FileWriter(file);
			xstream.toXML(game, fw);
			fw.close();
		} catch (Exception ex) {
			System.out.println("[Error] Failed saving game configuration to " +
					file.getPath());
			ex.printStackTrace();
		}
	}

	public void save() {
		save(DEFAULT_FILE);
		//System.out.println(xstream.toXML(game));
	}

	public static void main(String[] args) {
		GameLoader gl = GameLoader.get();
		gl.xstream.toXML(gl.getGame(), System.out);
	}
	
}

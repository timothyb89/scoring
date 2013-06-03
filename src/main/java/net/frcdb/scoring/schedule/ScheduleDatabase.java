package net.frcdb.scoring.schedule;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

/**
 *
 * @author tim
 */
public class ScheduleDatabase {
	
	private static ScheduleDatabase instance;
	
	public static final File DEFAULT_FILE = new File("data/schedule.xml");
	
	private XStream xstream;
	private MatchSchedule schedule;
	
	private ScheduleDatabase() {
		initXStream();
		load();
	}

	public static ScheduleDatabase get() {
		if (instance == null) {
			instance = new ScheduleDatabase();
		}

		return instance;
	}
	
	private void initXStream() {
		xstream = new XStream();
		xstream.processAnnotations(new Class[] {
			MatchSchedule.class, ScheduledMatch.class, AllianceEntry.class
		});
	}

	public void load(File file) {
		if (file.exists()) {
			try {
				InputStream is = file.toURI().toURL().openStream();
				schedule = (MatchSchedule) xstream.fromXML(is);
				is.close();
			} catch (Exception ex) {
				System.err.println("[Error] Failed loading schedule config from " +
						file.getPath());
				ex.printStackTrace();
			}
		} else {
			System.err.println("[Warn] Schedule config not found at "
					+ file.getPath() + ", creating new.");
			schedule = new MatchSchedule();
			schedule.generate(); // TODO: make sure this is a good place to gen
			save();
		}
	}

	public void load() {
		load(DEFAULT_FILE);
	}

	public void save(File file) {
		try {
			FileWriter fw = new FileWriter(file);
			xstream.toXML(schedule, fw);
			fw.close();
			//xstream.toXML(schedule, System.out);
		} catch (Exception ex) {
			System.out.println("[Error] Failed saving schedule configuration to " +
					file.getPath());
			ex.printStackTrace();
		}
	}

	public void save() {
		save(DEFAULT_FILE);
		//System.out.println(xstream.toXML(schedule));
	}

	public MatchSchedule getSchedule() {
		return schedule;
	}
	
	public static void main(String[] args) {
		ScheduleDatabase sd = ScheduleDatabase.get();
		sd.save();
		
	}
	
}

package net.frcdb.scoring.schedule;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;
import net.frcdb.scoring.game.Alliance;
import net.frcdb.scoring.game.Game;
import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.game.SchedulerConfiguration;
import net.frcdb.scoring.team.Team;
import net.frcdb.scoring.team.TeamDatabase;

/**
 *
 * @author tim
 */
@XStreamAlias("schedule")
public class MatchSchedule {
	
	@XStreamImplicit
	private List<ScheduledMatch> matches;
	
	public MatchSchedule() {
		
	}
	
	public void generate() {
		Schedule schedule = scheduleFromGame();
		matches = new ArrayList<ScheduledMatch>();
		
		Game game = GameLoader.sGetGame();
		SchedulerConfiguration sc = game.getSchedulerConfig();
		PairingConstraint pc = new PairingConstraint(
				sc.getPairingConstants().getTemperature(),
				sc.getPairingConstants().getTStep(),
				sc.getPairingConstants().getAnnealTime(),
				sc.getPairingConstants().getLowestTemp());
		FieldConstraint fc = new FieldConstraint(
				sc.getFieldConstants().getTemperature(),
				sc.getFieldConstants().getTStep(),
				sc.getFieldConstants().getAnnealTime(),
				sc.getFieldConstants().getLowestTemp());
		
		schedule = pc.anneal(schedule); // apply the pairing constraint
		schedule = fc.anneal(schedule); // apply the field constraint
		
		// convert to plain ScheduledMatches
		for (int i = 1; i <= schedule.lastMatch(); i++) {
			Object[][] matchObj = schedule.getMatch(i);
			
			List<AllianceEntry> finalAlliances = new ArrayList<AllianceEntry>();
			
			int alliance = 0;
			for (Object[] teams : matchObj) {
				Alliance a = game.getAlliances().get(alliance);
				
				AllianceEntry e = new AllianceEntry();
				e.setAllianceName(a.getName());
				for (Object team : teams) {
					e.getTeams().add(((Team) team).getNumber());
				}
				finalAlliances.add(e);
				
				alliance++;
			}
			
			ScheduledMatch match = new ScheduledMatch(i, finalAlliances);
			matches.add(match);
		}
	}
	
	/**
	 * Create a new MatchSchedule based on the current game configuration.
	 * @return a new match schedule
	 */
	private Schedule scheduleFromGame() {
		Game game = GameLoader.sGetGame();
		int alliances = game.getAlliances().size();
		int allianceSize = game.getSchedulerConfig().getAllianceSize();
		int rounds = game.getSchedulerConfig().getRounds();
		
		List<Team> teams = TeamDatabase.get().getTeams();
		
		Schedule<Team> sched = new Schedule<Team>(
				teams, rounds, alliances, allianceSize);
		
		return sched;
	}

	public List<ScheduledMatch> getMatches() {
		return matches;
	}

	public void setMatches(List<ScheduledMatch> matches) {
		this.matches = matches;
	}
	
	public static void main(String[] args) {
		MatchSchedule ms = new MatchSchedule();
		ms.generate();
	}
	
}

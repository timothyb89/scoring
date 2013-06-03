package net.frcdb.scoring.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.frcdb.scoring.game.Game;
import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.team.Team;
import net.frcdb.scoring.team.TeamDatabase;

/**
 *
 * @author tim
 */
public class Scheduler {
	
	public static final int ALLIANCE_SIZE = 2;
	public static final int MATCHES_PER_TEAM = 5;
	
	private static class ScheduleEntry {
		private int match;
		private List<Team> teams;
		
		private ScheduleEntry(int match) {
			this.match = match;
			teams = new ArrayList<Team>();
		}
	}
	
	public static void main(String[] args) {
		TeamDatabase db = TeamDatabase.get();
		Game game = GameLoader.get().getGame();
		
		int alliances = game.getAlliances().size();
		
		Map<Team, Integer> count = new HashMap<Team, Integer>();
		for (Team team : db.getTeams()) {
			count.put(team, MATCHES_PER_TEAM);
		}
		
		System.out.println("Teams: " + count.size());
		
		Random rand = new Random();
		
		List<ScheduleEntry> matches = new ArrayList<ScheduleEntry>();
		
		while (!count.isEmpty()) {
			List<Team> availableTeams = new ArrayList<Team>();
			for (Team t : count.keySet()) {
				availableTeams.add(t);
			}
			
			if (availableTeams.size() < ALLIANCE_SIZE * alliances) {
				System.out.println("The following teams will not fully compete in this schedule:");
				for (Team t : count.keySet()) {
					System.out.println("\t" + t + ": " + count.get(t) + " match(es) short.");
				}
				break;
			}
			
			ScheduleEntry entry = new ScheduleEntry(matches.size() + 1);
			for (int i = 0; i < alliances * ALLIANCE_SIZE; i++) {
				int randomIndex = rand.nextInt(availableTeams.size());
				Team randTeam = availableTeams.get(randomIndex);
				
				availableTeams.remove(randTeam);
				count.put(randTeam, count.get(randTeam) - 1);
				
				if (count.get(randTeam) == 0) {
					count.remove(randTeam);
				}
				
				entry.teams.add(randTeam);
			}
			
			matches.add(entry);
		}
		
		System.out.println("Matches:");
		for (int i = 0; i < matches.size(); i++) {
			ScheduleEntry e = matches.get(i);
			
			System.out.println("\t#" + i + ": " + StringUtil.concat(e.teams, ", "));
		}
		
	}
	
}

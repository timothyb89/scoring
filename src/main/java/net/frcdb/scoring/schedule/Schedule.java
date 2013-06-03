package net.frcdb.scoring.schedule;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavin
 */
public class Schedule<T> implements Cloneable {

    /**
     * An array containing all of the team objects, in the order they will appear on schedule
     * (For example, {1 2 3 4} could be 1 and 2 vs 3 and 4.)
     */
    private Object[] matchSchedule;
    /**
     * The number of teams on each alliance.
     */
    private final int allianceSize;
    /**
     * The number of alliances per match.
     */
    private final int allianceNum;
    /**
     * The size of a single rounds or how many teams there are.
     */
    private final int roundSize;
    /**
     * The number of rounds there are.
     */
    private final int roundNum;

    /**
     * Creates a schedule match schedule for the teams specified, to play the specified number of rounds, and
     * divides them into the appropriate alliances.  This function returns the simplest possible schedule; that is, 
     * the teams play in the order they are in the list, with the cycle repeating each round.  If the number of teams
     * and rounds does not allow for a whole number of matches (for example, five teams, three rounds, and two 
     * alliances of two teams each), then teams are taken from the start of the list to serve as surrogates in the
     * final match.  
     * @param teams A list of the teams to participate.  The teams can be any type.
     * @param rounds An integer specifying the number of rounds in the competition; that is, the minimum 
     * number of matches each team is expected to play (surrogates will play one more match than 
     * non-surrogates.)
     * @param alliances The number of alliances in each match.
     * @param teamsPerAlliance The number of teams on each alliance.
     */
    public Schedule(List<T> teams, int rounds, int alliances, int teamsPerAlliance) {
        /*This length ensures each team plays at least as many matches as there are rounds, adding surrogate 
         * teams as needed */
        int surrogatesNeeded = (rounds * teams.size()) % (alliances * teamsPerAlliance);
        surrogatesNeeded = (surrogatesNeeded == 0) ? (0) : (alliances * teamsPerAlliance - surrogatesNeeded);
        matchSchedule = new Object[rounds * teams.size() + surrogatesNeeded];
        for (int i = 0; i < matchSchedule.length; i++) {
            matchSchedule[i] = teams.get(i % teams.size());
        }
        allianceSize = teamsPerAlliance;
        allianceNum = alliances;
        roundSize = teams.size();
        roundNum = rounds;
    }

    /**
     * Copies a schedule.
     * @param oldSched The schedule to be copied. 
     */
    private Schedule(Schedule oldSched) {
        matchSchedule = (Object[]) oldSched.matchSchedule.clone();
        allianceNum = oldSched.allianceNum;
        allianceSize = oldSched.allianceSize;
        roundNum = oldSched.roundNum;
        roundSize = oldSched.roundSize;
    }

    /**
     * @return The number of alliances competing in each match.
     */
    public int getAllianceNum() {
        return allianceNum;
    }

    /**
     * @return The number of teams on each alliance. 
     */
    public int getAllianceSize() {
        return allianceSize;
    }

    /**
     * @return The number of rounds in the schedule, which is the same as the minimum number of teams a 
     * team can expect to play.
     */
    public int getRoundNum() {
        return roundNum;
    }

    /**
     * @return The number of teams in each round, or, more simply, then number of teams playing.
     */
    public int getRoundSize() {
        return roundSize;
    }

    /**
     * @return An Object array containing the teams in the order they are to play.
     */
    public Object[] getSchedule() {
        return matchSchedule;
    }

    /**
     * @return The length of the match schedule.
     */
    public int length() {
        return matchSchedule.length;
    }

    /**
     * @return The highest match number (equivalently, the total number of matches).
     */
    public int lastMatch() {
        return matchSchedule.length / allianceNum / allianceSize;
    }

    /**
     * Returns a two dimensional object array, the first index being the (zero indexed) alliance number, which 
     * then contains the teams of each alliance.
     * @param matchNum The one-indexed number of the match.
     * @return A two dimensional Object array containing the teams for the match, arranged into alliances.
     */
    public Object[][] getMatch(int matchNum) {
        if (matchNum * allianceNum * allianceSize > matchSchedule.length) {
            throw new IllegalArgumentException("Match does not exist");
        }
        Object[][] toReturn = new Object[allianceNum][allianceSize];
        for (int i = 0; i < allianceNum; i++) {
            for (int j = 0; j < allianceSize; j++) {
                toReturn[i][j] =
                        matchSchedule[(matchNum - 1) * allianceNum * allianceSize + i * allianceSize + j];
            }
        }
        return toReturn;
    }

    /**
     * Determines if a position exists in the schedule
     * @param position An integer representing the position to be checked.
     * @return True, if the position refers to a spot on the schedule, false otherwise
     */
    private boolean legalPosition(int position) {
        if (position > 0 || position <= matchSchedule.length) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * If the specified position is legal, this method returns the team at that position, typecast back to its original
     * type.
     * @param position The position of the team on the schedule.
     * @return The team found in the specified position.
     */
    public T getTeamAt(int position) {
        if (!legalPosition(position)) {
            throw new IllegalArgumentException("Position does not exist");
        }
        return (T) matchSchedule[position];
    }

    /**
     * Takes two teams and swaps them.
     * @param p1 The position of the first team.
     * @param p2  The position of the second team.
     */
    protected void swap(int p1, int p2) {
        if (legalPosition(p2) && legalPosition(p1)) {
            T team1 = getTeamAt(p1);
            matchSchedule[p1] = matchSchedule[p2];
            matchSchedule[p2] = team1;
        } else {
            throw new IllegalArgumentException("Position does not exist");
        }
    }

    /**
     * Swaps two teams in the same round.
     * @param p1 The position of the first team, taken from the start of the round.
     * @param p2 The position of the second team, taken from the start of the round.
     * @param round The round number (starting at one).
     * @see <code>Schedule.swap()</code>
     */
    protected void swapInRound(int p1, int p2, int round) {
        if (round > roundNum || round <= 0) {
            throw new IllegalArgumentException("Round does not exist");
        } else if (p1 >= roundSize || p2 >= roundSize) {
            throw new IllegalArgumentException("Position does not exist");
        }

        ArrayList match1 = new ArrayList();

        for (int i = 0; i < allianceNum * allianceSize; i++) {
            match1.add(matchSchedule[p1 + (round - 1) * roundSize - ((p1 + (round - 1) * roundSize) % (allianceNum * allianceSize)) + i]);
        }
        if (match1.contains(matchSchedule[p2 + (round - 1) * roundSize])) {
            return;
        } else {
            match1 = new ArrayList();
        }
        for (int i = 0; i < allianceNum * allianceSize; i++) {
            match1.add(matchSchedule[p2 + (round - 1) * roundSize - ((p2 + (round - 1) * roundSize) % (allianceNum * allianceSize)) + i]);
        }
        if (match1.contains(matchSchedule[p1 + (round - 1) * roundSize])) {
            return;
        }

        swap(p1 + (round - 1) * roundSize, p2 + (round - 1) * roundSize);
    }

    protected void swapInMatch(int p1, int p2, int match) {
        if (p1 < 0 || p2 < 0) {
            throw new IllegalArgumentException("Positions must be positive");
        } else if (match > lastMatch() || match <= 0) {
            throw new IllegalArgumentException("Match does not exist");
        }
        p1 %= allianceNum * allianceSize;
        p2 %= allianceNum * allianceSize;
        
        swap(p1 + (match - 1) * allianceNum * allianceSize, p2 + (match - 1) * allianceNum * allianceSize);
    }

    public static void main(String[] args) {
        ArrayList<Integer> teams = new ArrayList<Integer>();
        for (int i = 1; i <= 8; i++) {
            teams.add(i);
        }
        PairingConstraint pc = new PairingConstraint(1000, .95, 20, .001);
        Schedule<Integer> test = new Schedule(teams, 8, 2, 2);
        Schedule<Integer> test2 = pc.anneal(test);

        System.out.println("Score is: " + pc.eval(test2) + "\nOld score: " + pc.eval(test));
        for (int i = 0; i < test.length(); i++) {
            System.out.printf(test2.getTeamAt(i) + " ");
            if ((i + 1) % 4 == 0) {
                System.out.println();
            }
        }
    }

    @Override
    public Object clone() {
        Schedule<T> toReturn = new Schedule(this);
        return toReturn;
    }

}
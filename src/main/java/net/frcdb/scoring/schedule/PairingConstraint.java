package net.frcdb.scoring.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * A constraint used to ensure teams play against the most uniform distribution of opponents with the most 
 * uniform distribution of allies possible.
 * @author gavin
 */
public class PairingConstraint extends AnnealingConstraint {

    private static final double SAME_ALLY_PENALTY = 2.5;
    private static final double SAME_OPPONENT_PENALTY = 1.9;

    /**
     * Creates a new pairing constraint
     * @param temperature The initial temperature for the annealing algorithm
     * @param tStep The constant the temperature is multiplied by at each step of the annealing process.  It 
     * must be between 0 and 1, exclusively.
     * @param annealTime The number of moves computed at each temperature step.
     * @param lowestTemp The lower boundary for the temperature.  If the temperature falls below lowestTemp, 
     * the algorithm will terminate.
     */
    public PairingConstraint(double temperature, double tStep, double annealTime, double lowestTemp) {
        super(temperature, tStep, annealTime, 0, lowestTemp);
        //TODO: find the lowest score and optimal lowest temperature of a given dataset.
    }

    /**
     * Evaluates a schedule based on how often it has teams playing with and against other teams.  Repeated 
     * allies are punished more heavily than repeated opponents, as each team has more opponents than allies in
     * a match.
     * @param sched The schedule to score.
     * @return A score reflecting how well the schedule plays different combinations of teams.
     */
    @Override
    public double eval(Schedule sched) {
        double score = 0;
        HashMap<Object, HashMap<Object, Integer>> allies =
                new HashMap<Object, HashMap<Object, Integer>>();

        HashMap<Object, HashMap<Object, Integer>> opponents =
                new HashMap<Object, HashMap<Object, Integer>>();
        Object team1, team2;
        HashMap<Object, Integer> team1Hash, team2Hash;
        for (int i = 0; i < sched.lastMatch(); i++) {
            Object[][] match = sched.getMatch(i + 1);
            for (int j = 0; j < match.length - 1; j++) {
                //Add the current allies to the team's allies.
                for (int k = 0; k < match[j].length - 1; k++) {
                    team1 = match[j][k];
                    team1Hash = (allies.containsKey(team1)) ? (allies.get(team1)) : (new HashMap<Object, Integer>());
                    for (int m = k + 1; m < match[j].length; m++) {
                        team2 = match[j][m];
                        team2Hash = (allies.containsKey(team2)) ? (allies.get(team2)) : (new HashMap<Object, Integer>());
                        team1Hash.put(team2, (team1Hash.containsKey(team2)) ? (team1Hash.get(team2) + 1) : (1));
                        team2Hash.put(team1, (team2Hash.containsKey(team1)) ? (team2Hash.get(team1) + 1) : (1));
                        allies.put(team2, team2Hash);
                    }
                    allies.put(team1, team1Hash);
                    //Now for the opponents
                    team1Hash = (opponents.containsKey(team1)) ? (opponents.get(team1)) : (new HashMap<Object, Integer>());
                    for (int m = j + 1; m < match.length; m++) {
                        //Go through each opposing alliance above this one.
                        for (Object o : match[m]) {
                            team2Hash = (opponents.containsKey(o))
                                    ? (opponents.get(o)) : (new HashMap<Object, Integer>());
                            team1Hash.put(o, (team1Hash.containsKey(o)) ? (team1Hash.get(o) + 1) : (1));
                            team2Hash.put(team1, (team2Hash.containsKey(team1)) ? (team2Hash.get(team1) + 1) : (1));
                            opponents.put(o, team2Hash);
                        }
                        opponents.put(team1, team1Hash);
                    }
                }
            }
        }
        //The hashes are filled; now for the calculations.

        for (HashMap<Object, Integer> subHash : allies.values()) {
            for (Integer i : subHash.values()) {
                score += Math.pow(SAME_ALLY_PENALTY, (double) (i - 1)) - 1;
            }
        }

        for (HashMap<Object, Integer> subHash : opponents.values()) {
            for (Integer i : subHash.values()) {
                score += Math.pow(SAME_OPPONENT_PENALTY, (double) (i - 1)) - 1;
            }
        }

        return score;
    }

    /**
     * Randomly swaps two teams within a single round of the schedule, in hopes of finding a better schedule.
     * @param sched The schedule to apply the move to (this schedule will not be changed by the function).
     * @return A new schedule with the random moves applied.
     */
    @Override
    public Schedule move(Schedule sched) {
        Schedule toReturn = (Schedule) sched.clone();
        Random generator = new Random();
        int rand1 = generator.nextInt(toReturn.getRoundSize());
        int rand2 = generator.nextInt(toReturn.getRoundSize());
        int round = generator.nextInt(toReturn.getRoundNum()) + 1;
        while (rand2 == rand1) {
            rand2 = generator.nextInt(toReturn.getRoundSize());
        }
        toReturn.swapInRound(rand1, rand2, round);

        return toReturn;
    }

    public static void main(String[] args) {
        PairingConstraint pc = new PairingConstraint(10000, .9, 10, .001);
        ArrayList<Integer> il = new ArrayList<Integer>();
        for (int i = 1; i < 7; i++) {
            il.add(i);
        }
        Schedule<Integer> test = new Schedule<Integer>(il, 5, 2, 2);
        System.out.println("Eval: " + pc.eval(test));
    }
}

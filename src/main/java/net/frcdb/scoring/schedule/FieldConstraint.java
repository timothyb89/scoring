package net.frcdb.scoring.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * A constraint used to ensure each time plays a roughly equal number of times on each alliance.
 * @author gavin
 */
public class FieldConstraint extends AnnealingConstraint {
	
	public FieldConstraint(double temperature, double tStep, double annealTime, double lowestTemp) {
        super(temperature, tStep, annealTime, 0, lowestTemp);
    }

    @Override
    public double eval(Schedule sched) {
        int numAlliances = sched.getAllianceNum();
        double score = 0;
        /*Each int[] has numAlliances entries, with an entry corresponding to the number of times a team has 
         * played on that particular alliance 
         */
        HashMap<Object, Integer[]> teamAllianceHash = new HashMap<Object, Integer[]>();
        for (int i = 1; i < sched.lastMatch(); i++) {
            Object[][] match = sched.getMatch(i);
            for (int j = 0; j < match.length; j++) {
                for (Object team : match[j]) {
                    Integer[] value;
                    Integer val;
                    if (!teamAllianceHash.containsKey(team)) {
                        value = new Integer[numAlliances];
                        for (int k = 0; k < value.length; k++) {
                            value[k] = (j == k) ? ((Integer) (1)) : ((Integer) (0));
                        }
                        teamAllianceHash.put(team, value);
                    } else {
                        teamAllianceHash.get(team)[j]++;
                    }
                }
            }
        }
        //teamAllianceHash is now full, so determine score based on the values.
        for (Object team : teamAllianceHash.keySet()) {
            Integer[] values = teamAllianceHash.get(team);
            for (Integer i : values) {
                score += (Math.pow(2, i)); //TODO: Determine if optimal scoring function
            }
        }
        return score;
    }

    @Override
    public Schedule move(Schedule sched) {
        Schedule newSched = (Schedule) sched.clone();
        Random generator = new Random();
        int rand1 = generator.nextInt(newSched.getAllianceNum() * newSched.getAllianceSize());
        int rand2 = generator.nextInt(newSched.getAllianceNum() * newSched.getAllianceSize());
        int match = generator.nextInt(newSched.lastMatch()) + 1;
        while (rand1 == rand2) {
            rand2 = generator.nextInt(newSched.getAllianceNum() * newSched.getAllianceSize());
        }
        newSched.swapInMatch(rand1, rand2, match);
        return newSched;
    }

    public static void main(String[] args) {
        FieldConstraint fc = new FieldConstraint(100, .99, 10, .001);
        PairingConstraint pc = new PairingConstraint(100, .9, 10, .001);
        ArrayList<Integer> il = new ArrayList<Integer>();
        for (int i = 1; i <= 80; i++) {
            il.add(i);
        }
        Schedule<Integer> test = new Schedule<Integer>(il, 5, 2, 2);
        Schedule test1 = pc.anneal(test);
        Schedule test2 = fc.anneal(test1);
        System.out.println("Old score = " + fc.eval(test1) + "\nNew score = " + fc.eval(test2));
        for (int i = 0; i < test2.length(); i++) {
            System.out.printf(test2.getTeamAt(i) + " ");
            if ((i + 1) % 4 == 0) {
                System.out.println();
            }
        }
    }
	
}

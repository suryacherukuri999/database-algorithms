package com.dbproject.join;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RelationGenerator {
    private final Random rnd = new Random();

    // S(B,C): B random [10000,50000], C random [0,999] 
    public List<Tuple> generateRelationS(int count) {
        List<Tuple> out = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int b = rnd.nextInt(40001) + 10000;
            int c = rnd.nextInt(1000);
            out.add(new Tuple(b, c, false));
        }
        return out;
    }

    // R(A,B) with B chosen from existing S values 
    public List<Tuple> generateRelationRFromS(int count, List<Tuple> sTuples) {
        List<Tuple> out = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Tuple sample = sTuples.get(rnd.nextInt(sTuples.size()));
            int a = rnd.nextInt(1000);
            out.add(new Tuple(sample.getKeyB(), a, true));
        }
        return out;
    }

    // R(A,B) with B random in [minB,maxB]
    public List<Tuple> generateRelationRWithRandomB(int count, int minB, int maxB) {
        List<Tuple> out = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int b = rnd.nextInt(maxB - minB + 1) + minB;
            int a = rnd.nextInt(1000);
            out.add(new Tuple(b, a, true));
        }
        return out;
    }
}

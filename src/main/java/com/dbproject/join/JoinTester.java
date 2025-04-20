package com.dbproject.join;

import java.util.*;

public class JoinTester {
    private final RelationGenerator gen = new RelationGenerator();
    private final VirtualDisk       disk = new VirtualDisk();
    private final MainMemory        mem  = new MainMemory();
    private final HashJoin          hj   = new HashJoin(disk, mem);
    private final Random            rnd  = new Random();

    public void runTests() {
        // build S
        List<Tuple> S = gen.generateRelationS(5000);
        List<Integer> sBlocks = disk.storeRelation(S);

        // Part 5.1
        List<Tuple> R1 = gen.generateRelationRFromS(1000, S);
        List<Integer> r1 = disk.storeRelation(R1);
        disk.resetIoCount();
        List<JoinTuple> out1 = hj.performTwoPassJoin(r1, sBlocks);
        System.out.println("Two‑pass join #1: result=" + out1.size()
            + " tuples, I/O=" + hj.getDiskIoCount());
        printRandom(out1,20);

        // Part 5.2
        List<Tuple> R2 = gen.generateRelationRWithRandomB(1200,20000,30000);
        List<Integer> r2 = disk.storeRelation(R2);
        disk.resetIoCount();
        List<JoinTuple> out2 = hj.performTwoPassJoin(r2, sBlocks);
        System.out.println("Two‑pass join #2: result=" + out2.size()
            + " tuples, I/O=" + hj.getDiskIoCount());
        printAll(out2);
    }

    private void printRandom(List<JoinTuple> res, int k) {
        Set<Integer> bs = new HashSet<>();
        for (var t: res) bs.add(t.toString().hashCode());
        List<Integer> sample = new ArrayList<>(bs);
        Collections.shuffle(sample);
        sample = sample.subList(0, Math.min(k, sample.size()));
        System.out.println("Sample of " + sample.size() + " join tuples:");
        for (int h : sample) {
            for (var t: res) if (t.toString().hashCode()==h) {
                System.out.println("  "+t);
            }
        }
    }

    private void printAll(List<JoinTuple> res) {
        System.out.println("All " + res.size() + " join tuples:");
        for (var t: res) System.out.println("  "+t);
    }

    public static void main(String[] args) {
        new JoinTester().runTests();
    }
}

package com.dbproject.btree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeBuilder {

    // dense tree
    public BPlusTree buildDenseTree(List<Integer> keys, int order) {
        List<Integer> sorted = new ArrayList<>(keys);
        Collections.sort(sorted);

        int capacity = order - 1;
        List<LeafNode> leaves = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i += capacity) {
            List<Integer> part = sorted.subList(i, Math.min(i+capacity, sorted.size()));
            leaves.add(new LeafNode(order, part));
        }
        for (int i = 0; i+1 < leaves.size(); i++) {
            leaves.get(i).setNext(leaves.get(i+1));
            leaves.get(i+1).setPrev(leaves.get(i));
        }
        if (leaves.size() == 1) {
            return new BPlusTree(order, leaves.get(0));
        }

        // build up the tree
        List<Node> lvl = new ArrayList<>(leaves);
        while (lvl.size() > 1) {
            List<Integer> seps = new ArrayList<>();
            for (int i = 1; i < lvl.size(); i++) {
                seps.add(lvl.get(i).getKeys().get(0));
            }
            List<Node> next = new ArrayList<>();
            for (int i = 0; i < seps.size(); i += capacity) {
                List<Integer> lev = new ArrayList<>();
                List<Node>    cpart = new ArrayList<>();
                cpart.add(lvl.get(i));
                for (int j = i; j < Math.min(i+capacity, seps.size()); j++) {
                    lev.add(seps.get(j));
                    cpart.add(lvl.get(j+1));
                }
                next.add(new InternalNode(order, lev, cpart));
            }
            lvl = next;
        }
        return new BPlusTree(order, lvl.get(0));
    }

    // sparse tree
    public BPlusTree buildSparseTree(List<Integer> keys, int order) {
        BPlusTree t = new BPlusTree(order);
        List<Integer> sorted = new ArrayList<>(keys);
        Collections.sort(sorted);
        for (int k : sorted) t.insert(k);
        return t;
    }
}

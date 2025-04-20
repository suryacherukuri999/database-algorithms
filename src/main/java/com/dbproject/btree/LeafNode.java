package com.dbproject.btree;

import java.util.ArrayList;
import java.util.List;

public class LeafNode implements Node {
    private final int order;
    private final List<Integer> keys;
    private LeafNode prev, next;

    public LeafNode(int order) {
        this(order, new ArrayList<>());
    }

    public LeafNode(int order, List<Integer> initKeys) {
        this.order = order;
        this.keys  = new ArrayList<>(initKeys);
    }

    public List<Integer> getKeys() { 
        return keys; 
    }
    public boolean  isLeaf()  { 
        return true; 
    }
    public LeafNode  getNext() { 
        return next; 
    }
    public LeafNode getPrev() { 
        return prev; 
    }
    public void  setNext(LeafNode n){
         next = n;
    }
    public void  setPrev(LeafNode p){ 
        prev = p; 
    }

    public void print() {
        System.out.println("  Leaf: " + keys);
    }

    public boolean search(int key) {
        return keys.contains(key);
    }

    public Node insert(int key) {
        // find insert position
        int i = 0;
        while (i < keys.size() && keys.get(i) < key) 
            i++;
        if (i < keys.size() && keys.get(i) == key) 
            return null; // no duplicates
        keys.add(i, key);

        if (keys.size() < order) 
            return null;
        int mid = keys.size()/2;
        List<Integer> rightK = new ArrayList<>(keys.subList(mid, keys.size()));
        keys.subList(mid, keys.size()).clear();

        LeafNode sibling = new LeafNode(order, rightK);
        sibling.next = this.next;
        if (this.next != null) this.next.prev = sibling;
        this.next = sibling;
        sibling.prev = this;
        return sibling;
    }

    public Node delete(int key) {
        if (!keys.remove(Integer.valueOf(key))) return this;
        int maxK = order - 1;
        int minK = (maxK + 1)/2;        // ceil(maxK/2)

        // no underflow
        if (keys.size() >= minK) return this;

        // try borrow from prev
        if (prev != null && prev.keys.size() > minK) {
            int stolen = prev.keys.remove(prev.keys.size()-1);
            keys.add(0, stolen);
            return this;
        }
        // try borrow from next
        if (next != null && next.keys.size() > minK) {
            int stolen = next.keys.remove(0);
            keys.add(stolen);
            return this;
        }
        // merging
        if (prev != null) {
            // merge into prev
            prev.keys.addAll(keys);
            prev.next = this.next;
            if (this.next != null) 
                this.next.prev = prev;
            return null;
        } else if (next != null) {
            // merge next into this
            keys.addAll(next.keys);
            this.next = next.next;
            if (next.next != null) 
                next.next.prev = this;
            return null;
        }
        return this;
    }

    public List<Integer> rangeSearch(int lo, int hi) {
        List<Integer> out = new ArrayList<>();
        for (int k : keys) {
            if (k >= lo && k <= hi) out.add(k);
        }
        if (next != null && next.keys.get(0) <= hi) {
            out.addAll(next.rangeSearch(lo,hi));
        }
        return out;
    }
}

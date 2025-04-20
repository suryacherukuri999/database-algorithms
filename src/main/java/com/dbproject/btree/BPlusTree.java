package com.dbproject.btree;

import java.util.List;

public class BPlusTree {
    private Node   root;
    private final int order;

    public BPlusTree(int order) {
        this.order = order;
        this.root  = new LeafNode(order);
    }

    public BPlusTree(int order, Node root) {
        this.order = order;
        this.root  = root;
    }

    public void print() {
        System.out.println("B+ Tree (order=" + order + "):");
        root.print();
    }

    public boolean search(int key) {
        return root.search(key);
    }

    public List<Integer> rangeSearch(int lo, int hi) {
        // descend to the first leaf
        Node cur = root;
        while (!cur.isLeaf()) {
            InternalNode in = (InternalNode)cur;
            int idx = 0;
            while (idx < in.getKeys().size() && lo >= in.getKeys().get(idx)) idx++;
            cur = in.getChildren().get(idx);
        }
        return ((LeafNode)cur).rangeSearch(lo,hi);
    }

    public void insert(int key) {
        Node split = root.insert(key);
        if (split == null) return;

        // root split to a new root
        InternalNode r = new InternalNode(order);
        int sep = split.getKeys().get(0);
        r.getKeys().add(sep);
        r.getChildren().add(root);
        r.getChildren().add(split);
        root = r;
    }

    public void delete(int key) {
        Node nr = root.delete(key);
        if (nr == null) {
            // if root merged away or underflow then collapse if internal
            if (!root.isLeaf()) {
                InternalNode r = (InternalNode)root;
                if (r.getChildren().size() == 1) {
                    root = r.getChildren().get(0);
                }
            }
        } else if (nr != root) {
            root = nr;
        }
    }
}

package com.dbproject.btree;

import java.util.ArrayList;
import java.util.List;

public class InternalNode implements Node {
    private final int order;
    private final List<Integer> keys;
    private final List<Node>    children;

    public InternalNode(int order) {
        this.order    = order;
        this.keys     = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public InternalNode(int order, List<Integer> keys, List<Node> children) {
        this.order    = order;
        this.keys     = new ArrayList<>(keys);
        this.children = new ArrayList<>(children);
    }

    public List<Integer> getKeys()     { 
        return keys; 
    }
    public List<Node> getChildren() { 
        return children; 
    }
    public boolean isLeaf()      { 
        return false; 
    }

    public void print() {
        System.out.println("Internal: " + keys);
        for (Node node : children) node.print();
    }

    public boolean search(int key) {
        int i = 0;
        while (i < keys.size() && key >= keys.get(i)) 
            i++;
        if (i >= children.size()) 
            i = children.size()-1;
        return children.get(i).search(key);
    }

    public Node insert(int key) {
        // descend to child
        int i = 0;
        while (i < keys.size() && key >= keys.get(i)) 
            i++;
        Node split = children.get(i).insert(key);
        if (split == null) 
            return null;

        // insert separator + pointer
        int sep = split.getKeys().get(0);
        keys.add(i, sep);
        children.add(i+1, split);

        if (keys.size() < order) 
            return null;
        int mid = keys.size()/2;
        int midKey = keys.get(mid);

        // carve right half
        List<Integer> rightKey = new ArrayList<>(keys.subList(mid+1, keys.size()));
        List<Node>    rightChild = new ArrayList<>(children.subList(mid+1, children.size()));

        keys.subList(mid, keys.size()).clear();
        children.subList(mid+1, children.size()).clear();

        // 4) sibling without the midKey
        InternalNode sibling = new InternalNode(order, rightKey, rightChild);
        return sibling;
    }

    public Node delete(int key) {
        // descend
        int i = 0;
        while (i < keys.size() && key >= keys.get(i)) 
            i++;
        if (i >= children.size()) 
            i = children.size()-1;

        Node child = children.get(i);
        Node res   = child.delete(key);

        // child stayed or height‐shrunk
        if (res == child) {
            if (i > 0) {
                keys.set(i-1, children.get(i).getKeys().get(0));
            }
            return this;
        }
        children.remove(i);
        if (i == 0) {
            keys.remove(0);
        } else {
            keys.remove(i-1);
        }

        int min = (order + 1)/2;  // ceil((m)/2) children
        if (children.size() < min) {
            return null;
        }
        return this;
    }
}

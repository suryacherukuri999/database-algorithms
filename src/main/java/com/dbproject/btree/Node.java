package com.dbproject.btree;

import java.util.List;

public interface Node {
    List<Integer> getKeys();
    boolean isLeaf();
    void print();
    boolean search(int key);
    Node insert(int key);
    Node delete(int key);
}

package com.dbproject.join;

public class JoinTuple {
    private final int a, b, c;
    public JoinTuple(int a, int b, int c) {
        this.a = a; this.b = b; this.c = c;
    }
    @Override
    public String toString() {
        return "(" + a + "," + b + "," + c + ")";
    }
}

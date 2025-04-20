package com.dbproject.join;

public class Tuple {
    private final int keyB;    // join attribute
    private final int other;   // A if from R, or C if from S
    private final boolean fromR;

    public Tuple(int keyB, int other, boolean fromR) {
        this.keyB  = keyB;
        this.other = other;
        this.fromR = fromR;
    }

    public int getKeyB()    { return keyB; }
    public int getOther()   { return other; }
    public boolean isFromR(){ return fromR; }

    @Override
    public String toString() {
        return (fromR ? "R" : "S") + "(" + other + "," + keyB + ")";
    }
}

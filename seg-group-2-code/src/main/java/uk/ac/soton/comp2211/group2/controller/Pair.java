package uk.ac.soton.comp2211.group2.controller;

public class Pair<T1, T2> {
    private T1 value1;
    private T2 value2;

    public Pair(T1 v1, T2 v2) {
        this.value1 = v1;
        this.value2 = v2;
    }

    public T1 getFirst() {
        return this.value1;
    }

    public T2 getSecond() {
        return this.value2;
    }

    public void setFirst(T1 v1) {
        this.value1 = v1;
    }

    public void setSecond(T2 v2) {
        this.value2 = v2;
    }

    public String toString() {
        return "(" + value1.toString() + ", " + value2.toString() + ")";
    }
}

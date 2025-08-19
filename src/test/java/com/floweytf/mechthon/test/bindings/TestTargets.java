package com.floweytf.mechthon.test.bindings;

public class TestTargets {
    public static class Sample {
        public int x;

        public Sample(int x) {
            this.x = x;
        }
    }

    public static int staticField = 5;

    public static void staticVoid() {
    }

    public static String staticEcho(String s) {
        return s;
    }

    public static int getter(Sample s) {
        return s.x;
    }

    public static void setter(Sample s, int v) {
        s.x = v;
    }

    public static void method(Sample s, int a) {
        s.x += a;
    }
}

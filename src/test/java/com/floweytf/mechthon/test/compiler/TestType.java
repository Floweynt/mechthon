package com.floweytf.mechthon.test.compiler;

public class TestType {
    public static class A {
        public void foo(A a) {
        }

        public void bar(B b) {
        }

        public A a;
        public B b;
    }

    public static class B {
        public void foo(A a) {
        }

        public void bar(B b) {
        }

        public A getA() {
            return a;
        }

        public void setA(A a) {
            this.a = a;
        }

        public B getB() {
            return b;
        }


        public A a;
        public B b;
    }

    public int someThing(A a) {
        return 0;
    }

    public int otherThing(B a, long c, char e, String d) {
        return 3;
    }
}

package dev.devgroup;

public class Pair<P> {
    private P x1, x2;

    public Pair(P x1, P x2) {
        this.x1 = x1;
        this.x2 = x2;
    }

    public void setX1(P x1) {
        this.x1 = x1;
    }

    public void setX2(P x2) {
        this.x2 = x2;
    }

    public P getX1() {
        return x1;
    }

    public P getX2() {
        return x2;
    }
}

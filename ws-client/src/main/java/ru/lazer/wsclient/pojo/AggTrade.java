package ru.lazer.wsclient.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AggTrade {

    @JsonProperty("e")
    private String e; // Event type
    @JsonProperty("E")
    private long E;   // Event time
    @JsonProperty("s")
    private String s; // Symbol
    @JsonProperty("a")
    private int a;    // Aggregate trade ID
    @JsonProperty("p")
    private double p; // Price
    @JsonProperty("q")
    private double q; // Quantity
    @JsonProperty("f")
    private int f;    // First trade ID
    @JsonProperty("l")
    private int l;    // Last trade ID
    @JsonProperty("T")
    private long T;   // Trade time
    @JsonProperty("m")
    private boolean m; // Is buyer the market maker

    public AggTrade() {
    }

    public AggTrade(String e, long E, String s, int a, double p, double q, int f, int l, long T, boolean m) {
        this.e = e;
        this.E = E;
        this.s = s;
        this.a = a;
        this.p = p;
        this.q = q;
        this.f = f;
        this.l = l;
        this.T = T;
        this.m = m;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public long getETime() {
        return E;
    }

    public void setETime(long E) {
        this.E = E;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public long getT() {
        return T;
    }

    public void setT(long T) {
        this.T = T;
    }

    public boolean isM() {
        return m;
    }

    public void setM(boolean m) {
        this.m = m;
    }

    @Override
    public String toString() {
        return "AggTrade{" +
                "e='" + e + '\'' +
                ", E=" + E +
                ", s='" + s + '\'' +
                ", a=" + a +
                ", p=" + p +
                ", q=" + q +
                ", f=" + f +
                ", l=" + l +
                ", T=" + T +
                ", m=" + m +
                '}';
    }
}

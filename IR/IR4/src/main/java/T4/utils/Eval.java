package T4.utils;

/**
 * Created by pmatos9 on 19/11/17.
 */
public class Eval {

    private double true_pos=0, true_neg=0, false_pos=0, false_neg=0;
    private double mrr = 0.0;
    private double map = 0.0;
    private double mp10 = 0.0;
    private double truePosRank = 0, falsePosRank = 0;

    private double precision, recal, fmeasure;

    public double getMp10() {
        return mp10;
    }

    public void setMp10(double mp10) {
        this.mp10 = mp10;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecal() {
        return recal;
    }

    public void setRecal(double recal) {
        this.recal = recal;
    }

    public double getFmeasure() {
        return fmeasure;
    }

    public void setFmeasure(double fmeasure) {
        this.fmeasure = fmeasure;
    }

    public double getTruePosRank() {
        return truePosRank;
    }

    public void setTruePosRank(double truePosRank) {
        this.truePosRank = truePosRank;
    }

    public double getFalsePosRank() {
        return falsePosRank;
    }

    public void setFalsePosRank(double falsePosRank) {
        this.falsePosRank = falsePosRank;
    }

    public double getMap() {
        return map;
    }

    public void setMap(double map) {
        this.map = map;
    }

    public double getMrr() {
        return mrr;
    }

    public void setMrr(double mrr) {
        this.mrr = mrr;
    }

    public double getTrue_pos() {
        return true_pos;
    }

    public void setTrue_pos(double true_pos) {
        this.true_pos = true_pos;
    }

    public double getTrue_neg() {
        return true_neg;
    }

    public void setTrue_neg(double true_neg) {
        this.true_neg = true_neg;
    }

    public double getFalse_pos() {
        return false_pos;
    }

    public void setFalse_pos(double false_pos) {
        this.false_pos = false_pos;
    }

    public double getFalse_neg() {
        return false_neg;
    }

    public void setFalse_neg(double false_neg) {
        this.false_neg = false_neg;
    }
}

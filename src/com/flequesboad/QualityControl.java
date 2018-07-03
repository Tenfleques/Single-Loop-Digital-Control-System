package com.flequesboad;

import java.util.Collections;
import java.util.List;

public class QualityControl {
    int d, N;
    Double T0;
    public QualityControl(){
        d = 0;
        N = 0;
        T0 = 0.1;
    }
    public QualityControl(int d, int N, Double T0){
        this.d = d;
        this.N = N;
        this.T0 = T0;
    }
    public Double getIntegralSquareError(List<Double> y, List<Double> yz){
        Double S = 0.0;
        int max_i = d + 1;
        for(int i = max_i; i < N - 10; ++i){
            S += Math.pow(yz.get(i) - y.get(i), 2.0);
        }
        return S;
    }
    public Double getRegulationTime(List<Double> y, List<Double> yz){
        int i = N - 1;
        Double tmp = (yz.get(i) - y.get(i)) / yz.get(i);
        while(Math.abs(tmp) < 0.05){
            tmp = (yz.get(i) - y.get(i)) / yz.get(i);
            i --;
        }
        return i*T0;
    }
    public Double getOverRegulation(List<Double> y, List<Double> yz){
        Double overReg = Collections.max(y) - yz.get(yz.size() - 1);
        return (overReg < 0.0) ? 0.0 : overReg;
    }
    public Double coefficientZatukaniya(List<Double> y, List<Double> yz){
        Double A1 = this.getOverRegulation(y,yz);
        Double firstMax = Collections.max(y);
        int indexOfFirstMax = y.indexOf(firstMax);
        Double secondMax = Collections.max(y.subList(indexOfFirstMax + 1, y.size() - 1));
        Double A3 = secondMax - yz.get(yz.size() - 1);

        if(A1.equals(0.0))
            return 100.0;
        return 100 * (A1 - A3)/A1;
    }
    public Double getStaticError(List<Double> y, List<Double> yz){
        return yz.get(yz.size() - 1) - y.get(y.size() - 1);
    }
}

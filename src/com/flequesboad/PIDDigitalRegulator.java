package com.flequesboad;

import java.util.*;

// TODO make methods universal to cater for all orders

public class PIDDigitalRegulator{
    protected Double [] q, a;
    protected Double b, T0, eps;
    protected Integer d, N;
    protected int max_i;
    public PIDDigitalRegulator(){
        this.q = new Double[0];
        this.a = new Double[0];

        this.b = 0.0;
        this.T0 = 0.0;
        this.eps = 0.0;
        this.d = 0;
        this.N = 0;
        this.max_i = d + 1;
    }

    public PIDDigitalRegulator(Double [] q, Double [] a, Double b, Double T0, Double eps, Integer d, Integer N){
        this.q = Arrays.copyOf(q,q.length);
        this.a = Arrays.copyOf(a,a.length);

        this.b = b;
        this.T0 = T0;
        this.eps = eps;
        this.d = d;
        this.N = N;
        this.max_i = d + 1;
    }
    public Double[] setYz(){
        Double[] yz = new Double[N];
        for(int i = 0; i < max_i; i ++){
            yz[i] = 0.0;
        }
        for(int i = max_i; i < N; i ++){
            yz[i] = 1.0;
        }
        return yz;
    }
    public List<List<Double>> optimizeSettings(){
        Double[] u, y;
        Double[][] dudq, dydq;
        y = new Double[N + 1];
        u = new Double[N];
        Double[] yz = this.setYz();
        dudq = new Double[3][N];
        dydq = new Double[3][N + 1];

        for(int i = 0; i < max_i; i ++){
            u[i] = 0.0;
            dudq[0][i] = 0.0;
            dudq[1][i] = 0.0;
            dudq[2][i] = 0.0;
        }
        for(int i = 0; i < max_i + 1; i ++){
            y[i] = 0.0;
            dydq[0][i] = 0.0;
            dydq[1][i] = 0.0;
            dydq[2][i] = 0.0;
        }
        List<List<Double>> result = new ArrayList<>();
        int j = 0;
        Double gradient = .0;
        Double[][] dSdq = {
            {0.0, 0.0},
            {0.0, 0.0},
            {0.0, 0.0}
        };
        Double H0 = 0.5, H1 = 0.5, H2 = 0.5;
        while(j < N){
            for (int i =  max_i; i < N; ++i){
                u[i] = u[i - 1] + q[0] * (yz[i] - y[i]) + q[1]*(yz[i - 1] - y[i -1]) + q[2]*(yz[i-2] - y[i-2]);
                y[i + 1] = a[0]*y[i] + a[1]*y[i - 1] + b*u[i-d];

                dudq[0][i] = dudq[0][i - 1] + (yz[i] - y[i]) - q[0]*dydq[0][i] - q[1]*dydq[0][i-1] - q[2]*dydq[0][i-2];
                dydq[0][i + 1] = a[0] * dydq[0][i] + a[1] * dydq[0][i - 1]  + b*dudq[0][i-d];

                dudq[1][i] = dudq[1][i - 1] + (yz[i-1] - y[i-1]) - q[0]*dydq[1][i] - q[1]*dydq[1][i-1] -
                        q[2]*dydq[1][i-2];
                dydq[1][i + 1] = a[0] * dydq[1][i] + a[1] * dydq[1][i - 1]  + b*dudq[1][i-d];

                dudq[2][i] = dudq[2][i - 1] + (yz[i-2] - y[i-2]) - q[0]*dydq[2][i] - q[1]*dydq[2][i-1] -
                        q[2]*dydq[2][i-2];
                dydq[2][i + 1] = a[0] * dydq[2][i] + a[1] * dydq[2][i - 1]  + b*dudq[2][i-d];
            }
            dSdq[0][0] = 0.0;
            dSdq[1][0] = 0.0;
            dSdq[2][0] = 0.0;
            for(int k = max_i; k < N; ++ k){
                dSdq[0][0] += (yz[k] - y[k])*(-1 * dydq[0][k]);
                dSdq[1][0] += (yz[k] - y[k])*(-1 * dydq[1][k]);
                dSdq[2][0] += (yz[k] - y[k])*(-1 * dydq[2][k]);
            }
            dSdq[0][0] *= 2.0;
            dSdq[1][0] *= 2.0;
            dSdq[2][0] *= 2.0;

            gradient = Math.sqrt(Math.pow(dSdq[0][0],2.0) + Math.pow(dSdq[1][0],2.0) + Math.pow(dSdq[2][0],2.0));

            if(gradient < eps){
                break;
            }
            if(j == 0){
                H0 = 0.5;
                H1 = 0.5;
                H2 = 0.5;
            }else{
                H0 = (dSdq[0][0] * dSdq[0][1] < 0.0) ? (1.0/3.0)*H0 : 1.2 * H0;
                H1 = (dSdq[1][0] * dSdq[1][1] < 0.0) ? (1.0/3.0)*H0 : 1.2 * H0;
                H2 = (dSdq[2][0] * dSdq[2][1] < 0.0) ? (1.0/3.0)*H0 : 1.2 * H0;
            }

            dSdq[0][1] = dSdq[0][0];
            dSdq[1][1] = dSdq[1][0];
            dSdq[2][1] = dSdq[2][0];
            q[0] = q[0] - H0 * dSdq[0][0]/gradient;
            q[1] = q[1] - H1 * dSdq[1][0]/gradient;
            q[2] = q[2] - H2 * dSdq[2][0]/gradient;
            j ++;
        }
        List<Double> meta = new ArrayList<>();

        meta.add(Keys.NORM_GRADIENT.getValue(),gradient);

        result.add(Keys.META.getValue(),meta);
        result.add(Keys.Y.getValue(),Arrays.asList(y));
        result.add(Keys.Q_OPTIMUM.getValue(),Arrays.asList(q));
        return result;
    }
    public HashMap<String,List<Double>> dynamicCharacter(List<Double> qOpt){
        Double[] u, y;
        y = new Double[N + 1];
        u = new Double[N];
        int max_i = d + 1;
        for(int i = 0; i < max_i; i ++){
            u[i] = 0.0;
        }
        for(int i = 0; i < max_i + 1; i ++){
            y[i] = 0.0;
        }
        Double[] yz = setYz();
        for(int i = max_i; i < N; ++i){
            u[i] = u[i-1] + qOpt.get(0)*(yz[i] - y[i]) + qOpt.get(1)*(yz[i-1] - y[i-1]) + qOpt.get(2)*(yz[i-2]-
                    y[i-2]);
            y[i + 1] = a[0] * y[i] + a[1] * y[i - 1] + b*u[i - d];
        }
        HashMap<String,List<Double>> res = new HashMap<>();
        res.put(Locale.DYNAMIC_CHARACTER.getValue(), Arrays.asList(y));
        res.put(Locale.PROVIDED_Y.getValue(), Arrays.asList(yz));
        return res;
    }
    public HashMap<String,List<Double>> disturbedDynamicCharacter(List<Double> qOpt){
        Double[] u, y, f;
        y = new Double[N + 1];
        u = new Double[N];
        f = new Double[N];
        Double[] yz = new Double[N];
        int max_i = d + 1;
        for(int i = 0; i < N; ++i){
            yz[i] = 0.0;
            f[i] = 0.0;
        }
        for(int i = 0; i < max_i; i ++){
            u[i] = 0.0;
        }
        for(int i = 0; i < max_i + 1; i ++){
            y[i] = 0.0;
        }
        f[max_i] = 1.0;

        for(int i = max_i; i < N; ++i){
            u[i] = u[i-1] + qOpt.get(0)*(yz[i] - y[i]) + qOpt.get(1)*(yz[i - 1] - y[i - 1]) + qOpt.get(2)*(yz[i - 2] -
                    y[i - 2]);
            y[i + 1] = a[0] * y[i] + a[1] * y[i -1]  + b*(u[i - d] + f[i - d]);
        }
        HashMap<String,List<Double>> res = new HashMap<>();
        res.put(Locale.DISTURBED_DYNAMIC_CHARACTER.getValue(), Arrays.asList(y));
        res.put(Locale.PROVIDED_Y.getValue(), Arrays.asList(yz));
        return res;
    }
}

package com.flequesboad;


import java.util.HashMap;

public class ImmitatedModel {
    ImmitatedModel(){

    }
    public String kriteriaFishera(Double S2y, Double S2ost, Double F){
        Double Fraschetno = S2y/S2ost;
        String html = "Fp = " + Fraschetno;
        html +=  (Fraschetno > F)? " > " + F + " то адекватно" : " < " + F + " то не адекватно";
        return html;
    }
    HashMap<String,Double> raschetaDispersiaOtnositelnoSrednego(Double[] yu,Double[] ym, int N, int d){
        Double yubar = .0; //средное значение yu
        int size = 0;
        for(int i = 2 + d; i < N+1+d; ++i, size ++ ){
            yubar += yu[i];
        }
        yubar /= size;
        Double S2y = .0;
        Double S2ost = .0;

        for(int i = 2 + d; i < N+1+d; ++i ){
            S2y += Math.pow(yu[i] - yubar,2);
            S2ost += Math.pow(ym[i] - yu[i],2);
        }
        int l = 3; //число связей, наложенных на выборку
        S2y /= (N-1);
        S2ost /= (N - 1 - l);

        HashMap ans = new HashMap<String, Double>();
        ans.put("S2y" , S2y);
        ans.put("S2ost" , S2ost);
        return  ans;
    }
    public Double[] paraObyektaSvyazPomexoi3x(int d, int N, HashMap<String,Double> params){
        Double[] y = new Double[N + 1 + d];
        for(int i = 0; i < 3 + d; ++i){
            y[i] = 0.0;
        }
        Double [] u = new Double[N + 1 + d];
        u[0] = 0.0;
        for(int i = 1; i < N; ++i){
            u[i] = 1.0;
        }
        for(int i = 3 + d; i < N + 1 + d; ++i){
            y[i] = params.get("a1") * y[i-1]
                    + params.get("a2") * y[i-2]
                    + params.get("a3") * y[i-3]
                    + params.get("b") * u[i-d-1];
        }
        return  y;
    }
    Double[] paraObyektaSvyazPomexoi(int d, int N,HashMap<String,Double>params){
        Double[] y = new Double[N + 1 + d];
        for(int i = 0; i < 3 + d; ++i){
            y[i] = 0.0;
        }
        double [] u = new double[N + 1 + d];
        u[0] = 0.0;
        for(int i = 1; i < N; ++i){
            u[i] = 1.0;
        }
        for(int i = 2 + d; i < N + 1 + d; ++i){
            y[i] = params.get("a1")*y[i-1]
                    + params.get("a2")*y[i-2]
                    + params.get("b")*u[i-d-1];
        }

        return y;
    }
    public HashMap<String,Double[][]> vichislenieCummPriIsckomnikKoeffitsientIMassivaSvobodnixChlenov
            (int d,int N,Double[] yu){
        Double [] u = new Double[N + 1 + d];
        u[0] = 0.0;
        for(int i = 1; i < N; ++i){
            u[i] = 1.0;
        }

        Double[][] matrixA = {
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        };
        Double[] vectorB = {0.0, 0.0, 0.0};

        for(int i = 2 + d; i < N + 1 + d; ++i){
            matrixA[0][0] += Math.pow(yu[i-1],2);
            matrixA[0][1] += yu[i-1] * yu[i-2];
            matrixA[0][2] += yu[i-1] * u[i - d - 1];

            matrixA[1][0] += yu[i-1] * yu[i-2];
            matrixA[1][1] += Math.pow(yu[i-2],2);
            matrixA[1][2] += yu[i-2] * u[i - d - 1];

            matrixA[2][0] += yu[i-1] * u[i - d - 1];
            matrixA[2][1] += yu[i-2] * u[i - d - 1];
            matrixA[2][2] += Math.pow(u[i - d - 1],2);

            vectorB[0] += yu[i]*yu[i-1];
            vectorB[1] += yu[i]*yu[i-2];
            vectorB[2] += yu[i]*u[i-d-1];
        }
        HashMap<String,Double[][]> ans = new HashMap<>();
        ans.put("matrixA", matrixA);
        ans.put("vectorB", new Double[][]{vectorB});
        return  ans;
    }

    public HashMap<String,Double[][]> vichislenieCummPriIsckomnikKoeffitsientIMassivaSvobodnixChlenov3x
            (int d,int N,Double[] yu){
        Double [] u = new Double[N + 1 + d];
        u[0] = 0.0;
        for(int i = 1; i < N; ++i){
            u[i] = 1.0;
        }

        Double[][] matrixA = {
                {0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0}
        };
        Double[] vectorB = {0.0, 0.0, 0.0, 0.0};

        for(int i = 2 + d; i < N + 1 + d; ++i){
            matrixA[0][0] += yu[i-1] * yu[i-1];
            matrixA[0][1] += yu[i-2] * yu[i-1];
            matrixA[0][2] += yu[i-3] * yu[i-1];
            matrixA[0][3] += yu[i-1] * u[i - d - 1];

            matrixA[1][0] += yu[i-1] * yu[i-2];
            matrixA[1][1] += yu[i-2] * yu[i-2];
            matrixA[1][2] += yu[i-3] * yu[i-2];
            matrixA[1][3] += yu[i-2] * u[i - d - 1];

            matrixA[2][0] += yu[i-1] * yu[i-3];
            matrixA[2][1] += yu[i-2] * yu[i-3];
            matrixA[2][2] += yu[i-3] * yu[i-3];
            matrixA[2][3] += yu[i-3] * u[i - d - 1];

            matrixA[3][0] += yu[i-1] * u[i - d - 1];
            matrixA[3][1] += yu[i-2] * u[i - d - 1];
            matrixA[3][2] += yu[i-3] * u[i - d - 1];
            matrixA[3][3] += u[i - d - 1] * u[i - d - 1];

            vectorB[0] += yu[i]*yu[i-1];
            vectorB[1] += yu[i]*yu[i-2];
            vectorB[2] += yu[i]*yu[i-3];
            vectorB[3] += yu[i]*u[i-d-1];
        }
        HashMap<String,Double[][]> ans = new HashMap<>();
        ans.put("matrixA", matrixA);
        ans.put("vectorB", new Double[][]{vectorB});
        return  ans;
    }

}

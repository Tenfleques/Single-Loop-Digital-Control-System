package com.flequesboad;

public class DigitalRegulator {
    private Double[] yz, y, u;
    private Double[] q, a;
    private Double b;
    private Double[][] dudq, dydq;
    private Double[] dSdq;
    private Integer N, d;
    private Double eps;
    private Double j;
    private Double T0;
    private Double[] k, Hj;
    private Double paramYz;

    public DigitalRegulator(Double[] a, Double b, Integer d, Double[] q, Double T0, Double[] k, Double paramYz, Double
            eps,int N){
        this.T0 = T0;
        this.eps = eps;
        this.N = N;
        this.d = d;
        this.b = b;
        this.a = a;

        this.k = k;
        this.paramYz = paramYz;

        this.q = q;

        y = new Double[N];
        yz = new Double[N];
        u = new Double[N];

        dudq = new Double[2][N];
        dydq = new Double[2][N];
        dSdq = new Double[2];
        j = 1.0;
        init();
    }
    private void init(){
        u[0] = 0.0;
        dudq[0][0] = 0.0;
        dudq[1][0] = 0.0;
        Hj = getCoefficientStep();

        for(int i = 0; i < N; ++i){
            yz[i] = (i < 2) ? 0 : paramYz;
            if(i < 2 + d){
                y[i] = .0;
                dydq[0][i] = .0;
                dydq[1][i] = .0;
            }
        }
    }
    void run(){
        //
        calculateDynamicCharacteristicDCR();
        calculateNumericalPartDifferential();
        calculateDifferentialIntegrationalQualityCriteria();
        Double normaGradient = getNormaGradient();
        /*13*/
        if(normaGradient < eps){
            //23
            System.out.println(a);
            System.out.println(b);
            System.out.println(d);
            System.out.println(T0);
            System.out.println(q);
            return;
        }

        if(j != 1.0){ //14
            /*15*/
            if(Math.pow(dSdq[0],j)*Math.pow(dSdq[0],j-1) < 0.0){
                /*17*/
                Hj[0] = k[1]*Hj[0];
            }else{
                /*16*/
                Hj[0] = k[0]*Hj[0];
            }
            /*18*/
            if(Math.pow(dSdq[1],j)*Math.pow(dSdq[1],j-1) < 0.0){
                /*20*/
                Hj[1] = k[1]*Hj[1];
            }else{
                /*19*/
                Hj[1] = k[0]*Hj[1];
            }
        }
        /*21*/
        calculateOptimumQ(normaGradient);
        j += 1.0;
        run();
    }
    private void calculateOptimumQ(Double normaGradient){
        q[0] = q[0] - Hj[0]*dSdq[0]/normaGradient;
        q[1] = q[1] - Hj[1]*dSdq[1]/normaGradient;
    }
    private void calculateDynamicCharacteristicDCR(){
        for(int i = 1; i < N; ++i){
            u[i] = u[i-1] + q[0]*(yz[i] - y[i]) + q[1]*(yz[i-1] - y[i-1]);
            if(i + d + 1 < N)
                y[i + d + 1] = a[0]*y[i+d] + a[1]*y[i+d-1] + b*u[i];
        }
    }
    private void calculateNumericalPartDifferential(){
        for(int i = 2; i < N; ++i ){
            dudq[0][i] = dudq[0][i-1] + (yz[i] - y[i]) - q[0]*dydq[0][i] - q[1]*dydq[0][i-1];
            dudq[1][i] = dudq[1][i-1] - q[0]*dydq[1][0] + (yz[i-1] - y[i-1]) - q[1]*dydq[1][i-1];

            dydq[0][i + d + 1] = a[0]*dydq[0][i+d] + a[1]*dydq[0][i+d-1] + b*dudq[0][i];
            dydq[1][i + d + 1] = a[1]*dydq[1][i+d] + a[1]*dydq[1][i+d-1] + b*dudq[1][i];
        }
    }
    private void calculateDifferentialIntegrationalQualityCriteria(){
        for(int i = 2; i < N; i++){
            dSdq[0] += 2*(yz[i] - y[i])*(-1 * dydq[0][i]);
            dSdq[1] += 2*(yz[i] - y[i])*(-1 * dydq[1][i]);
        }
    }
    private Double getNormaGradient(){
        return Math.sqrt(Math.pow(dSdq[0],2.0) + Math.pow(dSdq[1],2.0));
    }
    private Double[] getCoefficientStep(){
        if(j.equals(1.0)) {
            Hj = new Double[]{0.5, 0.5};
            return Hj;
        }
        Hj[0] = (Math.pow(dSdq[0],j) * Math.pow(dSdq[0],j - 1.0) > 0)? k[0]*Hj[0] : k[1]*Hj[0];
        Hj[1] = (Math.pow(dSdq[0],j) * Math.pow(dSdq[0],j - 1.0) > 0)? k[0]*Hj[1] : k[1]*Hj[1];
        return Hj;
    }
}

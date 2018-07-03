package com.flequesboad;
import java.util.HashMap;

public class DiscreteModel {
    Double [] dmClean, dmDisturbed;
    DiscreteModel(Double[] coeffsSrc, Double T0, Double k, int d, int N, Double p){
        //html += buildKeyValueTable(coeffs);
        HashMap<String,Double> coeffs = pereschetKoefitsientPoPostoyanimVremeni(coeffsSrc);
        HashMap<String,Double> paramsDiscreteModel
                = raschetParametrovDiskretnoiModeliCZapadnimTaktom(T0
                    ,coeffs.get("T11")
                    ,coeffs.get("T22")
                    ,k);
        //html += buildKeyValueTable(paramsDiscreteModel,"параметри дискретной модели")
        Double[] y = new Double[N + 1 + d],
                ym = new Double[N + 1 + d],
                ymp = new Double[N + 1 + d],
                u = new Double[N + 1 + d],
                yu = new Double[N + 1 + d];
        for(int i = 0; i < 3 + d; ++i){
            y[i] = 0.0;
            ym[i] = 0.0;
            ymp[i] = 0.0;
            yu[i] = 0.0;
        }
        u[0] = 0.0;
        /*
         * Формирование единичного ступенчатого
         * воздействия на вход объекта
         */
        for(int i = 1; i < N; ++i){
            u[i] = 1.0;
        }
        /*
         * расчет переходного процесса объекта
         * по имитациой модели без помехи и с учетом помехи &
         */
        System.out.println(paramsDiscreteModel);
        HashMap<String,Double[]> discreteModel = paraObyektaSvyazPomexoi(d,N,y,yu,u,p,
                paramsDiscreteModel);
        /*for(Double i : discreteModel.get("b")){
            System.out.println(i);
        }*/
    }
    public HashMap<String,Double> pereschetKoefitsientPoPostoyanimVremeni(Double[] spiskaTov){
        int n = spiskaTov.length;
        HashMap<String,Double> ans = new HashMap<>();
        switch(n){
            case 1:
                ans.put("T11",spiskaTov[0]);
                return ans;
            case 2:
                ans.put("T11", spiskaTov[0] + spiskaTov[1]);
                ans.put("T22", spiskaTov[0] * spiskaTov[1]);
                return ans;
            case 3:
                ans.put("T11", spiskaTov[0] + spiskaTov[1]
                    + spiskaTov[2]);
                ans.put("T22", spiskaTov[0] * spiskaTov[1]
                    + spiskaTov[0] * spiskaTov[2]
                    + spiskaTov[1] * spiskaTov[2]);
                ans.put("T33", spiskaTov[0]
                    * spiskaTov[1]
                    * spiskaTov[2]);
            default:
                return ans;
        }
    }
    public HashMap<String,Double> raschetParametrovDiskretnoiModeliCZapadnimTaktom(Double T0, Double T11, Double T22,
                                                                                   Double k){
        Double a1 = (T11*T0 + 2*T22 - T0*T0)/(T22 + T11*T0);
        Double a2 = -(T22)/(T22 + T11*T0);
        Double b = k*T0*T0/(T22 + T11*T0);

        HashMap<String,Double> ans = new HashMap<>();
        ans.put("a1",a1);
        ans.put("a2",a2);
        ans.put("b",b);
        return ans;
    }

    public Double vihodObyekataCPomexoi(Double yi, Double p){
        Double rand = Math.random();
        Double pomexa =   rand * 2.0 - 1.0; // -1 <= pomexa < 1
        return yi + p*pomexa;
    }

    HashMap<String,Double[]> paraObyektaSvyazPomexoi(int d, int N,Double[] y, Double[] yu, Double[] u,Double p,
                                                   HashMap<String,Double>params){

        for(int i = 2 + d; i < N + 1 + d; ++i){
            y[i] = params.get("a1")*y[i-1]
                    + params.get("a2")*y[i-2]
                    + params.get("b")*u[i-d-1];

            yu[i] = vihodObyekataCPomexoi(y[i],p);
        }
        HashMap<String, Double[]> ans = new HashMap<>();
        this.dmClean = y;
        this.dmDisturbed = yu;
        ans.put("b", y);
        ans.put("c", yu);
        return ans;
    }
    Double[] getDmClean(){
        return this.dmClean;
    }
    Double[] getDmDisturbed(){
        return this.dmDisturbed;
    }
}

package com.flequesboad;

import java.util.HashMap;

public class Matrix {
    public HashMap<String, Double> metodKramera3x(Double[][] matrixA, Double[] vectorB){
        Double deltaA, deltaA123B, deltaA12B4, deltaA1B34, deltaB234;
        Determinant det = new Determinant(matrixA);
        deltaA =  det.determinant();

        Double[][] A123B, A12B4, A1B34, B234;
        A123B = det.replaceColumn(vectorB, 3);
        A12B4 = det.replaceColumn(vectorB, 2);
        A1B34 = det.replaceColumn(vectorB, 1);
        B234 = det.replaceColumn(vectorB, 0);


        deltaA123B = new Determinant(A123B).determinant();
        deltaA12B4 = new Determinant(A12B4).determinant();
        deltaA1B34 = new Determinant(A1B34).determinant();
        deltaB234 = new Determinant(B234).determinant();

        HashMap<String,Double> ans = new HashMap<>();
        ans.put("a1",deltaB234/deltaA);
        ans.put("a2",deltaA1B34/deltaA);
        ans.put("a3",deltaA12B4/deltaA);
        ans.put("b",deltaA123B/deltaA);
        return ans;
    }
    HashMap<String, Double> metodKramera2x(Double[][] matrixA, Double[] vectorB){
        Double deltaA, deltaA12B, deltaA1B3, deltaB23;
        Determinant det = new Determinant(matrixA);
        deltaA =  det.determinant();

        Double[][] A12B, A1B3, B23;
        A12B = det.replaceColumn(vectorB, 2);
        A1B3 = det.replaceColumn(vectorB, 1);
        B23 = det.replaceColumn(vectorB, 0);


        deltaA12B = new Determinant(A12B).determinant();
        deltaA1B3 = new Determinant(A1B3).determinant();
        deltaB23 = new Determinant(B23).determinant();

        HashMap<String,Double> ans = new HashMap<>();
        ans.put("a1",deltaB23/deltaA);
        ans.put("a2",deltaA1B3/deltaA);
        ans.put("b",deltaA12B/deltaA);
        return ans;
    }
}

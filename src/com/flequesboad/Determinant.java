package com.flequesboad;
import java.util.Arrays;


public class Determinant {
    private Double[][] matrix;
    private int sign = 1;
    public Determinant(Double[][] matrix) {
        this.matrix = new Double[matrix.length][matrix.length];
        for(int i = 0; i < matrix.length; ++i){
            this.matrix[i] = Arrays.copyOf(matrix[i],matrix.length);
        }
    }
    public int getSign() {
        return sign;
    }
    public Double determinant() {
        Double deter;
        if (!isUpperTriangular() && !isLowerTriangular())
            makeTriangular();
        deter = multiplyDiameter()*sign;
        return deter;
    }
    public void makeTriangular() {

        for (int j = 0; j < matrix.length; j++) {
            sortCol(j);
            for (int i = matrix.length - 1; i > j; i--) {
                if (matrix[i][j] == 0)
                    continue;

                Double x = matrix[i][j];
                Double y = matrix[i - 1][j];
                multiplyRow(i, (-y / x));
                addRow(i, i - 1);
                multiplyRow(i, (-x / y));
            }
        }
    }
    public boolean isUpperTriangular() {

        if (matrix.length < 2)
            return false;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < i; j++) {
                if (matrix[i][j] != 0)
                    return false;

            }

        }
        return true;
    }


    public boolean isLowerTriangular() {

        if (matrix.length < 2)
            return false;

        for (int j = 0; j < matrix.length; j++) {
            for (int i = 0; j > i; i++) {
                if (matrix[i][j] != 0)
                    return false;

            }

        }
        return true;
    }


    public Double multiplyDiameter() {

        Double result = 1.0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i == j)
                    result *= matrix[i][j];

            }

        }
        return result;
    }
    public void addRow(int row1, int row2) {

        for (int j = 0; j < matrix.length; j++)
            matrix[row1][j] += matrix[row2][j];
    }
    public void multiplyRow(int row, Double num) {

        if (num < 0)
            sign *= -1;


        for (int j = 0; j < matrix.length; j++) {
            matrix[row][j] *= num;
        }
    }
    public void sortCol(int col) {

        for (int i = matrix.length - 1; i >= col; i--) {
            for (int k = matrix.length - 1; k >= col; k--) {
                Double tmp1 = matrix[i][col];
                Double tmp2 = matrix[k][col];

                if (Math.abs(tmp1) < Math.abs(tmp2))
                    replaceRow(i, k);
            }
        }
    }
    public void replaceRow(int row1, int row2) {

        if (row1 != row2)
            sign *= -1;

        Double[] tempRow = new Double[matrix.length];

        for (int j = 0; j < matrix.length; j++) {
            tempRow[j] = matrix[row1][j];
            matrix[row1][j] = matrix[row2][j];
            matrix[row2][j] = tempRow[j];
        }
    }
    public Double [][] replaceColumn(Double [] vector,int index){
        Double[][] res = new Double[matrix.length][matrix.length];
        for(int i = 0; i < matrix.length; i++){
            res[i] = Arrays.copyOf(matrix[i],matrix.length);
            res[i][index] = vector[i];
        }
        return  res;
    }
}


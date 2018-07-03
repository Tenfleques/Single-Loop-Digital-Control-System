package com.flequesboad;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public static void charting(String stageTitle, HashMap<String,List<Double>> seriesTitleData){
        Stage stage = new Stage();
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel("y");
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setCreateSymbols(false);
        seriesTitleData.forEach((key,value)->{
            XYChart.Series series = new XYChart.Series();
            series.setName(key);
            for(int i = 0; i < value.size(); ++i) {
                series.getData().add(i, new XYChart.Data<>(i, value.get(i)));
            }
            lineChart.getData().add(series);
        });

        stage.setTitle(stageTitle);
        Scene scene = new Scene(lineChart,800,600);
        stage.setScene(scene);
        stage.show();
    }
    private void IDigitalRegulatorTest(){
        Double[] a = new Double[] {1.947071, -0.947776},
                q = new Double[] {0.2};
        Double b = 0.008539,
                T0 = 0.1,
                eps = 0.001;

        Integer N = 1500,
                d = 5;


        IDigitalRegulator dRI = new IDigitalRegulator(q,a,b,T0,eps,d,N);
        List<List<Double>> results =  dRI.optimizeSettings();
        List<Double> y = results.get(Keys.Y.getValue());

        List<Double> meta = results.get(Keys.META.getValue());
        List<Double> qOpt = results.get(Keys.Q_OPTIMUM.getValue());

        HashMap<String,List<Double>> dynamicCharacter = dRI.dynamicCharacter(qOpt);
        //dynamicCharacter.put(Locale.OPTIMUM_SETTINGS.getValue(),y);
        charting("Расчет динамической характеристики по заданию", dynamicCharacter);

        System.out.println("Оптимальные настройки q[0]_opt = " + qOpt);
        System.out.println("Расчет нормы градиента = " + meta.get(Keys.NORM_GRADIENT.getValue()));

        System.out.println();
        System.out.println("Показатели качества СР");
        System.out.println();

        QualityControl qc = new QualityControl(d,N,T0);

        Double S = qc.getIntegralSquareError(dynamicCharacter.get(Locale.DYNAMIC_CHARACTER.getValue()),
                dynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("Интегрально квадратичная ошибка (ИКО) S = " + S);

        Double regTime = qc.getRegulationTime(dynamicCharacter.get(Locale.DYNAMIC_CHARACTER.getValue()),
                dynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("время регулирования = " + regTime);

        Double overRegulation = qc.getOverRegulation(dynamicCharacter.get(Locale.DYNAMIC_CHARACTER.getValue()),
                dynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("перерегулирования = " + overRegulation);

        Double coeffZatuxania = qc.coefficientZatukaniya(dynamicCharacter.get(Locale.DYNAMIC_CHARACTER.getValue()),
                dynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("коэффициент затухания = " + coeffZatuxania);

        Double staticError = qc.getStaticError(dynamicCharacter.get(Locale.DYNAMIC_CHARACTER.getValue()),
                dynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("статическая ошибка = " + staticError);


        System.out.println();
        System.out.println("Расчет динамической характеристики по возмущению");
        System.out.println();

        /***** ==================================================  ****/
        HashMap<String,List<Double>> disturbedDynamicCharacter = dRI.disturbedDynamicCharacter(qOpt);
        charting("Расчет динамической характеристики по возмущеннию", disturbedDynamicCharacter);

        Double dS = qc.getIntegralSquareError(disturbedDynamicCharacter.get(Locale.DISTURBED_DYNAMIC_CHARACTER.getValue()),
                disturbedDynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("Интегрально квадратичная ошибка (ИКО) S = " + dS);

        Double dRegTime = qc.getRegulationTime(disturbedDynamicCharacter.get(Locale.DISTURBED_DYNAMIC_CHARACTER.getValue()),
                disturbedDynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("время регулирования = " + dRegTime);

        Double dOverRegulation = qc.getOverRegulation(disturbedDynamicCharacter.get(Locale.DISTURBED_DYNAMIC_CHARACTER.getValue()),
                disturbedDynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("перерегулирования = " + dOverRegulation);

        Double dCoeffZatuxania = qc.coefficientZatukaniya(disturbedDynamicCharacter.get(Locale
                        .DISTURBED_DYNAMIC_CHARACTER
                        .getValue()),
                disturbedDynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("коэффициент затухания = " + dCoeffZatuxania);

        Double dStaticError = qc.getStaticError(disturbedDynamicCharacter.get(Locale.DISTURBED_DYNAMIC_CHARACTER.getValue()),
                disturbedDynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("статическая ошибка = " + dStaticError);

        System.out.println();
        System.out.println("/***** ==================================================  ****/");
        System.out.println();
    }
    private void PIDDigitalRegulatorTest(){
        Double[] a = new Double[] {1.947071, -0.947776},
                q = new Double[] {0.15, -0.3, 0.05};
        Double b = 0.008539,
                T0 = 0.1,
                eps = 0.001;

        Integer N = 500,
                d = 5;


        PIDDigitalRegulator dRI = new PIDDigitalRegulator(q,a,b,T0,eps,d,N);
        List<List<Double>> results =  dRI.optimizeSettings();
        List<Double> y = results.get(Keys.Y.getValue());

        List<Double> meta = results.get(Keys.META.getValue());
        List<Double> qOpt = results.get(Keys.Q_OPTIMUM.getValue());

        HashMap<String,List<Double>> dynamicCharacter = dRI.dynamicCharacter(qOpt);
        //dynamicCharacter.put(Locale.OPTIMUM_SETTINGS.getValue(),y);
        charting("Расчет динамической характеристики по заданию", dynamicCharacter);

        System.out.println("Оптимальные настройки q[0]_opt = " + qOpt);
        System.out.println("Расчет нормы градиента = " + meta.get(Keys.NORM_GRADIENT.getValue()));

        System.out.println();
        System.out.println("Показатели качества СР");
        System.out.println();

        QualityControl qc = new QualityControl(d,N,T0);
        Double S = qc.getIntegralSquareError(dynamicCharacter.get(Locale.DYNAMIC_CHARACTER.getValue()),
                dynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("Интегрально квадратичная ошибка (ИКО) S = " + S);

        Double regTime = qc.getRegulationTime(dynamicCharacter.get(Locale.DYNAMIC_CHARACTER.getValue()),
                dynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("время регулирования = " + regTime);

        Double overRegulation = qc.getOverRegulation(dynamicCharacter.get(Locale.DYNAMIC_CHARACTER.getValue()),
                dynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("перерегулирования = " + overRegulation);

        Double coeffZatuxania = qc.coefficientZatukaniya(dynamicCharacter.get(Locale.DYNAMIC_CHARACTER.getValue()),
                dynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("коэффициент затухания = " + coeffZatuxania);

        Double staticError = qc.getStaticError(dynamicCharacter.get(Locale.DYNAMIC_CHARACTER.getValue()),
                dynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("статическая ошибка = " + staticError);


        System.out.println();
        System.out.println("Расчет динамической характеристики по возмущению");
        System.out.println();

        /***** ==================================================  ****/
        HashMap<String,List<Double>> disturbedDynamicCharacter = dRI.disturbedDynamicCharacter(qOpt);
        charting("Расчет динамической характеристики по возмущеннию", disturbedDynamicCharacter);

        Double dS = qc.getIntegralSquareError(disturbedDynamicCharacter.get(Locale.DISTURBED_DYNAMIC_CHARACTER.getValue()),
                disturbedDynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("Интегрально квадратичная ошибка (ИКО) S = " + dS);

        Double dRegTime = qc.getRegulationTime(disturbedDynamicCharacter.get(Locale.DISTURBED_DYNAMIC_CHARACTER.getValue()),
                disturbedDynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("время регулирования = " + dRegTime);

        Double dOverRegulation = qc.getOverRegulation(disturbedDynamicCharacter.get(Locale.DISTURBED_DYNAMIC_CHARACTER.getValue()),
                disturbedDynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("перерегулирования = " + dOverRegulation);

        Double dCoeffZatuxania = qc.coefficientZatukaniya(disturbedDynamicCharacter.get(Locale.DISTURBED_DYNAMIC_CHARACTER
                        .getValue()),
                disturbedDynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("коэффициент затухания = " + dCoeffZatuxania);

        Double dStaticError = qc.getStaticError(disturbedDynamicCharacter.get(Locale.DISTURBED_DYNAMIC_CHARACTER.getValue()),
                disturbedDynamicCharacter.get(Locale.PROVIDED_Y.getValue()));
        System.out.println("статическая ошибка = " + dStaticError);
    }
    @Override
    public void start(Stage primaryStage) {
        //IDigitalRegulatorTest();
        PIDDigitalRegulatorTest();
        //System.exit(0);
    }
}

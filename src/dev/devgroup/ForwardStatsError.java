package dev.devgroup;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Scanner;


/**
 * Класс предназначен для вычисления погрешностей прямых измерений
 */

public class ForwardStatsError extends Stats {

    /** Class Scanner for input */
    private static final Scanner sc = new Scanner(System.in);

    private int scaling;

    //Размер выборки
    private int N;

    //Среднее выборочное значение
    private double X;

    //Среднеквадратичное отклонение
    private double RMS;

    //Случайная погрешность
    private double dX;
    
    //Приборная погрешность
    private double theta;

    //Выборочное СКО среднего
    private double measurementError;

    /**
     * This method calculates errors of direct measurements.
     * @return object Pair with mean and error in calc
     */
    @Override
    public Pair<Double> statsCalc() {
        System.out.println("Перед вводом выборки убедитесь, что в ней нет очевидных промахов!!!");
        System.out.println("Введите выборку (конец последовательности обозначить любым не числовым значением) >>>");

        //Получаем выборку
        Take_Selection();

        //Сортируем выборку в порядке возрастания
        SortSelection();

        //Проверка на промахи
        MissCheck();

        //Среднее выборочное значение
        Sample_mean();

        //Вычисление СКО
        Calculating_RMS();

        //Оценка случайной погрешности
        Random_error();

        //Вычисление (полуение) приборной погрешности
        Instrument_error();

        //Вычисление выборочного СКО среднего
        Sample_RMS_average();

        //Округление
        int floating = Round_local();

        if (BigDecimal.valueOf(X).scale() < floating){
            System.out.print("X = L- +/- (delta))L- = " + X + AddZeros(X, floating));
        }else{
            System.out.print("X = L- +/- (delta))L- = " + X);
        }

        if (BigDecimal.valueOf(measurementError).scale() < floating){
            System.out.print(" ± " + measurementError + AddZeros(measurementError, floating));
        }else{
            System.out.print(" ± " + measurementError);
        }

        System.out.println();
        return new Pair<>(X, measurementError);
    }



    private void Take_Selection(){
        while (sc.hasNextDouble()) {
            selection.add(sc.nextDouble());
        }
        sc.next();

        System.out.println();


        //scaling = BigDecimal.valueOf(selection.get(0)).scale() + 2;

        N = selection.size();

        int[] floating = new int[6];

        for (int i = 0; i < N; i++) {
            if (selection.get(i) == (int)selection.get(i).doubleValue()){
                floating[0]++;
            }else{
                floating[BigDecimal.valueOf(selection.get(i)).scale()%(floating.length-1)]++;
            }
        }

        int max = Integer.MIN_VALUE;
        int iter = -1;

        for (int i = 0; i < floating.length; i++) {
            if (floating[i] > max){
                max = floating[i];
                iter = i;
            }
        }

        scaling = iter + 2;
    }

    private void SortSelection(){
        //Вывод неотсортированной выборки
        SelectionView("Неотсортированная выборка:");

        System.out.println();
        //Сортируем выборку в порядке возрастания
        Collections.sort(selection);

        //Вывод отсортированной выборки
        SelectionView("Отсортированная выборка:");
    }

    private void Sample_mean(){
        //Получение суммы элементов выборки
        double sum = 0;

        for (double el :
                selection) {
            sum += el;
        }

        X = round(sum / selection.size(), scaling);

        if (BigDecimal.valueOf(X).scale() < scaling){
            System.out.println("Среднее выборочное значение: " + X + AddZeros(X));
        }else{
            System.out.println("Среднее выборочное значение: " + X);
        }

        System.out.println();

    }

    private void Calculating_RMS(){
        double pre = 0;
        int i = 0;

        System.out.print("СКО = sqrt(( ");

        for (double el :
                selection) {
            pre += Math.pow(el - X, 2);
            i++;
            if (i == selection.size()){
                System.out.print("(" + el + "-" + X + ")^2");
            }else
                System.out.print("(" + el + "-" + X + ")^2 + ");
        }

        RMS = Math.sqrt((pre) / (N * (N - 1)));

        System.out.println(" )/( " + N + "*" + (N-1) + " ))");

        RMS = round(RMS, scaling);

        if (BigDecimal.valueOf(RMS).scale() < scaling){
            System.out.println("СКО: " + RMS + AddZeros(RMS));
        }else{
            System.out.println("СКО: " + RMS);
        }

        System.out.println();
    }

    private void Instrument_error(){
        System.out.println("Введите тип вводимых данных:\n" +
                "1: Цена одного деления\n" +
                "2: Приборная погрешность");

        int answ = sc.nextInt();
        boolean type = answ == 1;

        System.out.println("Введите значение:");
        theta = 0;
        if (type)
            theta = sc.nextDouble() / 2;
        else
            theta = sc.nextDouble();

        System.out.println("(theta)(L) = " + theta + "\n");
    }

    private void Random_error(){

        double stEr = StudentError();

        dX = round(stEr * RMS, scaling);
        System.out.println("Оценка случайной погрешности по Стьюденту: ");

        if (BigDecimal.valueOf(dX).scale() < scaling){
            System.out.println("(delta)L = " + stEr + "*" + RMS + " = " + (stEr * RMS) + " (примерно равно) " + dX + AddZeros(dX) + " (коэффициент Стьюдента: " + StudentError() + ")");
        }else{
            System.out.println("(delta)L = " + stEr + "*" + RMS + " = " + (stEr * RMS) + " (примерно равно) " + dX + " (коэффициент Стьюдента: " + StudentError() + ")");

        }
        System.out.println();
    }

    private void Sample_RMS_average(){
        System.out.println("Выберите способ получения выборочного СКО среднего:\n" +
                "1. Сложение\n" +
                "2. Корень из суммы квадратов");

        if (sc.nextInt() == 1){
            measurementError = dX + theta;

            System.out.println("(delta)L-) = " + dX + " + " + theta + " = " + measurementError);
        } else {
            measurementError = Math.sqrt(dX*dX + theta * theta);
            measurementError = round(measurementError, scaling);

            System.out.println("(delta)L- = sqrt(" + dX*dX + " + " + theta*theta + ") = " + measurementError);
        }
        System.out.println();
    }

    private int Round_local(){
        System.out.println("Округление конечного результата:");
        System.out.println("Было: ");
        System.out.println("(delta)L- = " + measurementError);
        System.out.println("L- = " + X);
        System.out.println();

        int floating = 0;

        /*System.out.println("Пожалуйста, посмотрите на погрешность измерений (1ое) " +
                "и выбирите до какого знака после запятой округлять\n" +
                "(В случае, если округлять надо до запятой, значение надо сделать отрицательным)\n");


        int floating = sc.nextInt();*/

        String[] num = String.valueOf(measurementError).split("\\.");

        boolean b_bumPre = false;
        int buf = 0;
        char[] numPre = num[0].toCharArray();
        char[] numPost = num[1].toCharArray();

        for (int i = 0; i < numPre.length; i++) {
            int n = Integer.parseInt(String.valueOf(numPre[i]));
            if (n != 0){
                if (n != 1)
                {
                    floating = 1;
                    b_bumPre = true;
                    buf -= numPre.length - i;
                    measurementError *= Math.pow(10, buf);
                    X *= Math.pow(10, buf);
                    break;
                }
            }
        }

        if (!b_bumPre){
            for (int i = 0; i < numPost.length; i++) {
                int n = Integer.parseInt(String.valueOf(numPost[i]));
                if (n != 0){
                    if (n != 1)
                    {
                        floating = i+1;
                        break;
                    }
                }
            }
        }

        int i_m = 0, i_X = 0;

        if (b_bumPre){
            i_m = (int)(round(measurementError, floating) * Math.pow(10, -buf));
            i_X = (int)(round(X, floating) * Math.pow(10, -buf));

            System.out.println("Стало: ");
            System.out.println("(delta)L- = " + i_m);
            System.out.println("L- = " + i_X);
        }else{
            measurementError = round(measurementError, floating);
            X = round(X, floating);



            System.out.println("Стало: ");
            if (BigDecimal.valueOf(measurementError).scale() < floating)
                System.out.println("(delta)L- = " + measurementError + AddZeros(measurementError, floating));
            else
                System.out.println("(delta)L- = " + measurementError);

            if (BigDecimal.valueOf(X).scale() < floating)
                System.out.println("L- = " + X + AddZeros(X, floating));
            else
                System.out.println("L- = " + X);
        }

        return floating;
    }

    private StringBuilder AddZeros(double num){
        StringBuilder s = new StringBuilder(scaling);
        for (int i = 0; i < scaling - BigDecimal.valueOf(num).scale(); i++) {
            s.append("0");
        }
        return s;
    }

    private StringBuilder AddZeros(double num, int floating){
        StringBuilder s = new StringBuilder(floating);
        for (int i = 0; i < floating - BigDecimal.valueOf(num).scale(); i++) {
            s.append("0");
        }
        return s;
    }


}

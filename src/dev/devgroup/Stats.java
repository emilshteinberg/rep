package dev.devgroup;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Абстрактный класс для наследования функциями.
 * Он является основой для классов по вычислению погрешностей.
 */
public abstract class Stats {

    /**
     * Выборки
     */
    protected static final ArrayList<Double> selection = new ArrayList<>();

    /**
     * Abstract class for error calculations
     *
     * @return mean and error in calc
     */
    public abstract Pair<Double> statsCalc();

    /**
     * Проверка на промахи
     */
    protected static void MissCheck() {

        System.out.println("\n________________________________\nПроверка на промахи!");


        int scaling = BigDecimal.valueOf(selection.get(0)).scale() + 2;

        boolean f1 = true, f2 = true, f = true;
        double x_cur;


        double Upn = 0;

        while (f1 || f2 || f) {

            System.out.println("_____________________________\n");

            //Вычисление размаха выборки
            double R = round(selection.get(selection.size() - 1) - selection.get(0), scaling);

            if ((selection.get(1) - selection.get(0)) == 0 || (selection.get(1) - selection.get(0)) == R
                    || selection.get(selection.size() - 1) - selection.get(selection.size() - 2) == 0
                    || selection.get(selection.size() - 1) - selection.get(selection.size() - 2) == R) {

                System.out.println("!!! Метод с отклонением от центра и S !!!");

                f1 = true; f2 = true; f = false;

                double mid = 0;
                int N = selection.size();

                for (double el :
                        selection) {
                    mid += el;
                }

                mid /= N;

                System.out.println("Current middle: " + mid);

                double pre = 0;
                for (double el :
                        selection) {
                    pre += Math.pow(el - mid, 2);
                }

                double pop = Math.sqrt((pre) / (N - 1));

                System.out.println("\nCurrent S: " + pop);

                double errorC = round(CoefError(), 2);

                System.out.println("\nCurrent Vpn: " + errorC);


                f1 = Math.abs(selection.get(0) - mid) >= errorC * pop;
                f2 = Math.abs(selection.get(selection.size() - 1) - mid) >= errorC * pop;

                if (f1) {
                    System.out.println("L(1) - промах, т.к. |" + selection.get(0) + " - " + mid + "| >= " + errorC + " * " + pop);
                    selection.remove(0);
                } else {
                    System.out.println("L(1) - не промах, т.к. |" + selection.get(0) + " - " + mid + "| < " + errorC + " * " + pop);
                }

                System.out.println();

                if (f2) {
                    System.out.println("L(last) - промах, т.к. |" + selection.get(selection.size() - 1) + " - " + mid + "| >= " + errorC + " * " + pop);
                    selection.remove(selection.size() - 1);
                } else {
                    System.out.println("L(last) - не промах, т.к. |" + selection.get(selection.size() - 1) + " - " + mid + "| < " + errorC + " * " + pop);
                }

                System.out.println("_____________________________\n");

            } else {
                System.out.println("!!! Метод с Upn !!!");

                System.out.println("R = " + R);

                f1 = false; f2 = false; f = true;
                Upn = round(UPNTaking(), 2);

                for (int i = 0; i < Math.round(selection.size()/2.0); i++) {
                    x_cur = round((selection.get(i+1) - selection.get(i)) / R, scaling);
                    System.out.println("L(" + (i + 1) + ") = (" + selection.get(i+1) + " - " + selection.get(i) + ")/" + R + " = " + x_cur);

                    f = !(x_cur < Upn);

                    if(f){
                        System.out.println("L(" + (i + 1) + ") >= " + Upn + " (Upn) - ПРОМАХ!\n");
                        selection.remove(i);
                        SelectionView("Выборка после удаления:");
                        System.out.println();
                        break;
                    }
                }

                System.out.println("А теперь в обратном порядке...");

                for (int i = selection.size() - 1; i > selection.size() - Math.round(selection.size()/2.0); i--) {
                    x_cur = round((selection.get(i) - selection.get(i-1)) / R, scaling);
                    System.out.println("L(" + (i + 1) + ") = (" + selection.get(i) + " - " + selection.get(i-1) + ")/" + R + " = " + x_cur);

                    f = !(x_cur < Upn);

                    if(f){
                        System.out.println("L(" + (i + 1) + ") >= " + Upn + " (Upn) - ПРОМАХ!\n");
                        selection.remove(i);
                        SelectionView("Выборка после удаления:");
                        System.out.println();
                        break;
                    }
                }
            }
        }

        SelectionView("\nAfter job:");

        System.out.println("\nПроверка на промахи завершена!\n___________________________________");
    }

    /**
     * Choosing student coefficient based on N
     *
     * @return student coefficient
     */
    protected static double StudentError() {

        double tpn = 0;
        int N = selection.size();

        if (N <= 2) {
            tpn = 12.7062047364D;
        } else if (N == 3) {
            tpn = 4.30265272991D;
        } else if (N == 4) {
            tpn = 3.18244630528D;
        } else if (N == 5) {
            //tpn = 2.7764451052D;
            tpn = 2.8D;
        } else if (N == 6) {
            tpn = 2.57058183661D;
        } else if (N == 7) {
            tpn = 2.44691184879D;
        } else if (N == 8) {
            tpn = 2.36462425101D;
        } else if (N == 9) {
            tpn = 2.30600413503D;
        } else if (N == 10) {
            tpn = 2.26215716274D;
        } else if (N == 11) {
            tpn = 2.22813885196D;
        } else if (N == 12) {
            tpn = 2.20098516008D;
        } else if (N == 13) {
            tpn = 2.17881282966D;
        } else if (N == 14) {
            tpn = 2.16036865646D;
        } else if (N == 15) {
            tpn = 2.14478668792D;
        } else if (N == 16) {
            tpn = 2.13144954556D;
        } else if (N == 17) {
            tpn = 2.11990529922D;
        } else if (N == 18) {
            tpn = 2.10981557783D;
        } else if (N == 19) {
            tpn = 2.10092204024D;
        } else if (N == 20) {
            tpn = 2.09302405441D;
        } else if (N == 21) {
            tpn = 2.08596344727D;
        } else if (N == 22) {
            tpn = 2.07961384473D;
        } else if (N == 23) {
            tpn = 2.0738730679D;
        } else if (N == 24) {
            tpn = 2.06865761042D;
        } else if (N == 25) {
            tpn = 2.06389856163D;
        } else if (N == 26) {
            tpn = 2.05953855275D;
        } else if (N == 27) {
            tpn = 2.05552943864D;
        } else if (N == 28) {
            tpn = 2.05183051648D;
        } else if (N == 29) {
            tpn = 2.0484071418D;
        } else if (N == 30) {
            tpn = 2.04522964213D;
        } else if (N == 31) {
            tpn = 2.0422724563D;
        } else if (N <= 41) {
            tpn = 2.021075383D;
        } else if (N <= 61) {
            tpn = 2.00029782106D;
        } else if (N <= 121) {
            tpn = 1.97993040505D;
        } else {
            tpn = 1.95996635682D;
        }

        return tpn;

    }

    /**
     * Choosing coefficient for another check based on N
     *
     * @return coefficient
     */
    protected static double CoefError() {

        double un = 0;
        int N = selection.size();

        if (N <= 3) {
            un = 1.15D;
        } else if (N == 4) {
            un = 1.46D;
        } else if (N == 5) {
            //un = 2.7764451052D;
            un = 1.67D;
        } else if (N == 6) {
            un = 1.82D;
        } else if (N == 7) {
            un = 1.94D;
        } else if (N == 8) {
            un = 2.03D;
        } else if (N == 9) {
            un = 2.11D;
        } else if (N == 10) {
            un = 2.18D;
        } else if (N == 11) {
            un = 2.23D;
        } else if (N == 12) {
            un = 2.29D;
        }
        return un;
    }

    /**
     * Choosing Upn coefficient based on N
     *
     * @return Upn coefficient
     */
    protected static double UPNTaking() {
        float Upn = 0;
        int N = selection.size();

        if (N <= 3) {
            Upn = 0.94f;
        } else if (N == 4) {
            Upn = 0.76f;
        } else if (N == 5) {
            Upn = 0.64f;
        } else if (N <= 7) {
            Upn = 0.51f;
        } else if (N <= 10) {
            Upn = 0.41f;
        } else if (N <= 15) {
            Upn = 0.34f;
        } else if (N <= 20) {
            Upn = 0.3f;
        } else if (N <= 30) {
            Upn = 0.26f;
        } else if (N <= 100) {
            Upn = 0.2f;
        }

        return Upn;
    }

    /**
     * Method printing message and current selection
     *
     * @param message - message to print before selection
     */
    protected static void SelectionView(String message) {
        System.out.println(message);
        System.out.println(Arrays.toString(selection.toArray()));
    }

    protected static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}

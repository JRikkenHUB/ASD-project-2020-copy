package nl.ritogames;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.ritogames.trui.Trui;

import java.util.Scanner;

public class ASDungeon {
   Injector injector;
   Trui trui;


    public static void main(String[] args) {
        ASDungeon asDungeon = new ASDungeon();
        asDungeon.injector = Guice.createInjector(new ASDInjector());
        asDungeon.trui = asDungeon.injector.getInstance(Trui.class);
        asDungeon.startTrui();
    }


    void startTrui() {
            trui.start();
    }

    public static int sumOfNumbersFromSystemIn() {
        Scanner scanner = new Scanner(System.in);
        int firstSummand = scanner.nextInt();
        int secondSummand = scanner.nextInt();
        return firstSummand + secondSummand;
    }

}

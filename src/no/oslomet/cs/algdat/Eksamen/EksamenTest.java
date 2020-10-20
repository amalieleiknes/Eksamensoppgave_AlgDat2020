package no.oslomet.cs.algdat.Eksamen;


////// Testprogram for mappeeksamen ////////////////////////

import no.oslomet.cs.algdat.Eksamen.EksamenSBinTre;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EksamenTest {

    // OPPGAVE 1 ////////////////////////////////////////////////
    @org.junit.jupiter.api.Test
    void oppgave1() {
        int antallFeil = 0;
        EksamenSBinTre<Integer> tre =
                new EksamenSBinTre<>(Comparator.naturalOrder());

        try {
            if (tre.antall() != 0) {
                antallFeil++;
                System.out.println("Oppgave 1a: Feil antall i et tomt tre!");
            }
        } catch (Exception e) {
            antallFeil++;
            System.out.println
                    ("Oppgave 1b: Skal ikke kaste unntak for et tomt tre");
        }

        tre.leggInn(1);
        tre.leggInn(2);
        tre.leggInn(3);

        if (tre.antall() != 3) {
            antallFeil++;
            System.out.println
                    ("Oppgave 1c: Antall blir ikke oppdatert!");
        }

        assertEquals(antallFeil, 0);
    }  // slutt på Oppgave 1


    // OPPGAVE 2 ////////////////////////////////////////////////
    @org.junit.jupiter.api.Test
    void oppgave2() {
        EksamenSBinTre<Integer> tre =
                new EksamenSBinTre<>(Comparator.naturalOrder());

        int antallFeil = 0;

        tre.leggInn(1);

        try {
            if (tre.antall(1) != 1) {
                antallFeil++;
                System.out.println("Oppgave 2a: Feil antall(T)-metoden!");
            }
        } catch (Exception e) {
            antallFeil++;
            System.out.println
                    ("Oppgave 2b: Skal ikke kaste unntak her!");
        }

        // Nytt tre
        tre = new EksamenSBinTre<>(Comparator.naturalOrder());
        int[] a = {1, 7, 1, 6, 1, 5, 1, 4, 1, 1, 1, 3};
        for (int verdi : a) tre.leggInn(verdi);

        if (tre.antall(7) != 1 || tre.antall(6) != 1
                || tre.antall(5) != 1 || tre.antall(4) != 1
                || tre.antall(3) != 1 || tre.antall(2) != 0
                || tre.antall(1) != 7 || tre.antall(0) != 0) {
            antallFeil++;
            System.out.println("Oppgave 2c: Feil antall(T)-metoden!");
        }


        assertEquals(antallFeil, 0);
    }  // slutt på Oppgave 2


    // OPPGAVE 3 ////////////////////////////////////////////////
    @org.junit.jupiter.api.Test
    void oppgave3() {
        int antallFeil = 0;
        EksamenSBinTre<Integer> tre =
                new EksamenSBinTre<>(Comparator.naturalOrder());

        String s;

        try {
            s = tre.toStringPostOrder();
            if (!s.equals("[]")) {
                antallFeil++;
                System.out.println("Oppgave 3a: Feil i toStringPostOrder() for et tomt tre!!");
            }
        } catch (Exception e) {
            antallFeil++;
            System.out.println
                    ("Oppgave 3b: Skal ikke kaste unntak for et tomt tre!");
        }

        // legger inn 10
        tre.leggInn(10);

        s = tre.toStringPostOrder();
        if (!s.equals("[10]")) {
            antallFeil++;
            System.out.println("Oppgave 3c: Feil i toStringPostOrder() for et tre med kun en verdi!");
        }

        // legger inn flere verdier
        int[] a = {6, 14, 1, 8, 12, 3, 7, 9, 11, 13, 2, 5, 4};
        for (int verdi : a) tre.leggInn(verdi);

        try {
            s = tre.toStringPostOrder();
            if (!s.equals("[2, 4, 5, 3, 1, 7, 9, 8, 6, 11, 13, 12, 14, 10]")) {
                antallFeil++;
                System.out.println("Oppgave 3d: Feil i toStringPostOrder()! Men feilen kan");
                System.out.println("ligge i leggInn() eller i nesteInorden().");
            }
        } catch (Exception e) {
            antallFeil++;
            System.out.println
                    ("Oppgave 3e: Her kastes et unntak! Det skal ikke skje!");
        }

        // Et nytt tre
        tre = new EksamenSBinTre<>(Comparator.naturalOrder());

        // legger inn 10 flere ganger
        for (int i = 0; i < 4; i++) tre.leggInn(10);

        s = tre.toStringPostOrder();
        if (!s.equals("[10, 10, 10, 10]")) {
            antallFeil++;
            System.out.println("Oppgave 3f: Feil i toStringPostOrder()! Men feilen kan");
            System.out.println("ligge i leggInn() eller i nesteInorden().");
        }

        // Et nytt tre
        tre = new EksamenSBinTre<>(Comparator.naturalOrder());

        int[] b = {5, 4, 3, 2, 1};
        for (int k : b) tre.leggInn(k);

        s = tre.toStringPostOrder();
        if (!s.equals("[1, 2, 3, 4, 5]")) {
            antallFeil++;
            System.out.println("Oppgave 3g: Feil i toStringPostOrder()! Men feilen kan");
            System.out.println("ligge i leggInn() eller i nesteInorden().");
        }
        assertEquals(antallFeil, 0);
    }  // slutt på Oppgave 3


    // OPPGAVE 4 ////////////////////////////////////////////////
    @org.junit.jupiter.api.Test
    void oppgave4() {
        EksamenSBinTre<Integer> tre =
                new EksamenSBinTre<>(Comparator.naturalOrder());

        // legger inn verdier i treet
        int[] a = {10, 6, 14, 1, 8, 12, 3, 7, 9, 11, 13, 2, 5, 4};
        for (int verdi : a) tre.leggInn(verdi);

        //Denne lambda-funksjonen skriver ut mellomrom før nodens verdi.
        AtomicReference<String> postorden = new AtomicReference<>();
        Oppgave<Integer> oppgave = c -> postorden.set(postorden.get() + " " + c.toString()) ;

        //Test at postorden fungerer
        postorden.set("");
        tre.postorden(oppgave);
        assertEquals(postorden.get(), " 2 4 5 3 1 7 9 8 6 11 13 12 14 10");

        //Test at rekursiv postorden fungerer
        postorden.set("");
        tre.postordenRecursive(oppgave);
        assertEquals(postorden.get(), " 2 4 5 3 1 7 9 8 6 11 13 12 14 10");
    }  // slutt på Oppgave 4


    // OPPGAVE 5 ////////////////////////////////////////////////
    @org.junit.jupiter.api.Test
    void oppgave5() {
        EksamenSBinTre<Integer> tre =
                new EksamenSBinTre<>(Comparator.naturalOrder());

        int[] a = {10, 14, 6, 8, 1, 12, 7, 3, 11, 9, 13, 5, 2, 4};
        for (int verdi : a) tre.leggInn(verdi);

        ArrayList<Integer> data = tre.serialize();

        Integer[] truth = {10, 6, 14, 1, 8, 12, 3, 7, 9, 11, 13, 2, 5, 4};
        assertArrayEquals(data.toArray(), truth);

        EksamenSBinTre<Integer> tre2 = EksamenSBinTre.deserialize(data, Comparator.naturalOrder());
        assertEquals(tre.toStringPostOrder(), tre2.toStringPostOrder());
    } // slutt på Oppgave 5


    // OPPGAVE 6 ////////////////////////////////////////////////
    @org.junit.jupiter.api.Test
    void oppgave6() {
        int antallFeil = 0;

        EksamenSBinTre<Integer> tre =
                new EksamenSBinTre<>(Comparator.naturalOrder());

        String s;

        tre.leggInn(6);
        tre.fjern(6);

        s = tre.toStringPostOrder();
        if (!s.equals("[]")) {
            antallFeil++;
            System.out.println("Oppgave 6a: Feil i fjern(T)!");
        }

        int[] a = {6, 3, 9, 1, 5, 7, 10, 2, 4, 8, 11, 6, 8};
        for (int verdi : a) tre.leggInn(verdi);

        boolean fjernet = tre.fjern(12);
        s = tre.toStringPostOrder();

        if (!s.equals("[2, 1, 4, 5, 3, 6, 8, 8, 7, 11, 10, 9, 6]")) {
            antallFeil++;
            System.out.println("Oppgave 6b: Feil i fjern(T)! Tallet 12 er ikke i treet!");
        }

        if (fjernet == true) {
            antallFeil++;
            System.out.println("Oppgave 6c: Feil i fjern(T)! Skal returnere false når");
            System.out.println("verdien ikke er i treet.");
        }

        if (tre.antall() != 13) {
            antallFeil++;
            System.out.println("Oppgave 6d: Feil i fjern(T)! Variabelen antall skal");
            System.out.println("ikke endres for en mislykket fjerning.");
        }

        fjernet = tre.fjern(2);
        s = tre.toStringPostOrder();

        if (!s.equals("[1, 4, 5, 3, 6, 8, 8, 7, 11, 10, 9, 6]")) {
            antallFeil++;
            System.out.println("Oppgave 6e: Feil i fjern(T)!");
        }

        if (fjernet == false) {
            antallFeil++;
            System.out.println("Oppgave 6f: Feil i fjern(T)! Skal returnere true");
            System.out.println("for en vellykket fjerning.");
        }

        if (tre.antall() != 12) {
            antallFeil++;
            System.out.println("Oppgave 6g: Feil i fjern(T)! Variabelen antall skal");
            System.out.println("reduseres med 1 for en vellykket fjerning.");
        }

        tre.fjern(4);
        s = tre.toStringPostOrder();

        if (!s.equals("[1, 5, 3, 6, 8, 8, 7, 11, 10, 9, 6]")) {
            antallFeil++;
            System.out.println("Oppgave 6h: Feil i fjern(T)!");
        }

        tre.fjern(6);
        s = tre.toStringPostOrder();

        if (!s.equals("[1, 5, 3, 8, 8, 7, 11, 10, 9, 6]")) {
            antallFeil++;
            System.out.println("Oppgave 6i: Feil i fjern(T)!");
        }

        tre.fjern(8);
        s = tre.toStringPostOrder();

        if (!s.equals("[1, 5, 3, 8, 7, 11, 10, 9, 6]")) {
            antallFeil++;
            System.out.println("Oppgave 6j: Feil i fjern(T)!");
        }

        tre.fjern(10);
        tre.fjern(11);
        tre.fjern(8);
        tre.fjern(7);
        s = tre.toStringPostOrder();

        if (!s.equals("[1, 5, 3, 9, 6]")) {
            antallFeil++;
            System.out.println("Oppgave 6k: Feil i fjern(T)!");
        }

        tre.fjern(1);
        tre.fjern(3);
        tre.fjern(5);
        tre.fjern(9);

        s = tre.toStringPostOrder();

        if (!s.equals("[6]")) {
            antallFeil++;
            System.out.println("Oppgave 6l: Feil i fjern(T)!");
        }

        tre.nullstill();

        if (tre.antall() != 0) {
            antallFeil++;
            System.out.println("Oppgave 6m: Feil i nullstill() - antall er feil!");
        }

        s = tre.toStringPostOrder();

        if (!s.equals("[]")) {
            antallFeil++;
            System.out.println("Oppgave 6n: Feil i nullstill()!");
        }

        try {
            tre.nullstill();
        } catch (Exception e) {
            antallFeil++;
            System.out.println
                    ("Oppgave 6o: Skal ikke kaste unntak når et tomt tre nullstilles!");
        }

        try {
            if (tre.fjernAlle(0) != 0) {
                antallFeil++;
                System.out.println("Oppgave 6p: Feil i fjernAlle(T) for tomt tre!");
            }
        } catch (Exception e) {
            antallFeil++;
            System.out.println
                    ("Oppgave 6q: Kaster unntak i fjernAlle(T) for tomt tre!");
        }

        tre.leggInn(0);

        try {
            if (tre.fjernAlle(0) != 1) {
                antallFeil++;
                System.out.println
                        ("Oppgave 6r: Feil i fjernAlle(T) for tre med en verdi!");
            }
        } catch (Exception e) {
            antallFeil++;
            System.out.println
                    ("Oppgave 6s: Kaster unntak i fjernAlle(T) for tre med en verdi!");
        }

        int[] b = {1, 4, 1, 3, 1, 2, 1, 1};
        for (int verdi : b) tre.leggInn(verdi);

        if (tre.fjernAlle(1) != 5) {
            antallFeil++;
            System.out.println("Oppgave 6t: Feil i fjernAlle(T)!");
        }

        s = tre.toStringPostOrder();
        if (!s.equals("[2, 3, 4]")) {
            antallFeil++;
            System.out.println("Oppgave 6u: Feil i fjernAlle(T)!");
        }

        tre = new EksamenSBinTre<>(Comparator.naturalOrder());

        Random r = new Random();
        for (int i = 0; i < 500_000; i++) tre.leggInn(r.nextInt(1_000_000));

        long tid = System.currentTimeMillis();
        tre.nullstill();
        tid = System.currentTimeMillis() - tid;

        if (tid < 10) {
            antallFeil++;
            System.out.println("Oppgave 6v: Har du kodet nullstill() ved kun");
            System.out.println("nullstille hode og antall? Alle nodeverdier og");
            System.out.println("pekere i treet skal nulles!");
        }
        assertEquals(antallFeil, 0);
    }  // slutt på Oppgave 5


} // Oblig3Test

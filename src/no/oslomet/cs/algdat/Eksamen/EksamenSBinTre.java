package no.oslomet.cs.algdat.Eksamen;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.StringJoiner;

public class EksamenSBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public EksamenSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }





    /** Oppgave 1
     * Metode som legger inn en ny node tatt fra kompendiet,
     * korrigert så den gir riktig verdi i foreldrenode
     * @param verdi Verdien som skal legges inn
     * @return Returnerer true om veriden ble lagt inn, false hvis ikke
     */
    public boolean leggInn(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    /** Oppgave 6 - del 1
     * Tatt fra kompendiet - korrigert så den fungerer i min kode
     * @param verdi Verdien som skal fjernes
     * @return Returnerer true om den blir fjernet
     */
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    /** Oppgave 6 - del 2
     *
     * @param verdi Tar inn verdi som skal fjernes i alle noder
     * @return Returnerer hvor mange noder som er fjernet
     */
    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    /** Oppgave 2
     * @param verdi Verdien vi ska sjekke antall forekomster av
     * @return Returnerer antallet av verdien
     */
    public int antall(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    /** Oppgave 6 - del 3
     * Skal traversere (rekursivt eller iterativt treet i en rekkefølge og
     * sørge for at pekere og nodeverdier blir nullet ut.
     */
    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    /** Oppgave 3 - del 1
     *
     * @param p
     * @param <T>
     * @return
     */
    private static <T> Node<T> førstePostorden(Node<T> p) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    /** Oppgave 3 - del 2
     *
     * @param p
     * @param <T>
     * @return
     */
    private static <T> Node<T> nestePostorden(Node<T> p) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    /** Oppgave 4 - hjelpemetode del 1
     *
     * @param oppgave
     */
    public void postorden(Oppgave<? super T> oppgave) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    /** Oppgave 4 - hjelpemetode del 2
     *
     * @param p
     * @param oppgave
     */
    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    /** Oppgave 5 - del 1
     *
     * @return
     */
    public ArrayList<T> serialize() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    /** Oppgave 5 - del 2
     *
     * @param data
     * @param c
     * @param <K>
     * @return
     */
    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


} // ObligSBinTre

package no.oslomet.cs.algdat.Eksamen;


import java.util.*;

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
     * Metode som legger inn en ny node er tatt fra kompendiet (5.2.3 a),
     * korrigert så den gir riktig verdi i foreldrenode
     * @param verdi Verdien som skal legges inn
     * @return Returnerer true om veriden ble lagt inn, false hvis ikke
     */
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot;
        Node<T> forelder = null;           // hjelpevariabel som viser forelder til p

        int cmp = 0;                             // hjelpevariabel

        while (p != null){                      // så lenge p ikke er null skal comp sjekke verdi mot verdi
            forelder = p;
            cmp = comp.compare(verdi,p.verdi);  // returnerer positivt tall hvis verdi er større enn p.verdi

            if(cmp<0){                          // p.verdi er størst, vi går mot venstre
                p = p.venstre;                  // p flyttes ned til venstre for å sjekkes ytterligere
            }
            else{
                p = p.høyre;
            }
        }

        // p er nå null, dvs. ute av treet, forelder er den siste vi passerte
        p = new Node<T>(verdi, forelder); // oppretter en ny node

        if (forelder == null) {
            rot = p;                      // p blir rotnode
        }
        else if (cmp < 0) {
            forelder.venstre = p;         // venstre barn til forelder
        }
        else {
            forelder.høyre = p;           // høyre barn til forelder
        }

        antall++;                         // én verdi mer i treet
        endringer++;                      // har endret treet
        return true;                      // vellykket innlegging
    }


    // TODO
    /** Oppgave 6 - del 1
     * Tatt fra kompendiet - korrigert så den fungerer i min kode
     * @param verdi Verdien som skal fjernes
     * @return Returnerer true om den blir fjernet
     */
    public boolean fjern(T verdi) {
        if (verdi == null) return false;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
            else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) rot = b;
            else if (p == q.venstre) q.venstre = b;
            else q.høyre = b;
        }
        else  // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null)
            {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) s.venstre = r.høyre;
            else s.høyre = r.høyre;
        }

        antall--;   // det er nå én node mindre i treet
        return true;
    }

    // TODO
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

        if(verdi == null){
            return 0;       //treet har ingen verdier
        }

        Node<T> p = rot;            // hjelpepeker
        int antallAvVerdi = 0;

        while (p != null){                           // sjekker p

            int cmp = comp.compare(verdi, p.verdi);     // hvis verdi>p.verdi returneres positivt tall

            if(cmp < 0){        //rotnodes verdi er mindre enn verdien vi leter etter
                p = p.venstre;
            }

            else if(cmp > 0){        //rotnodes verdi er større enn verdien vi leter etter
                p = p.høyre;
            }

            else {
                antallAvVerdi++;
                p = p.høyre;        // går til høyre fordi den kan være lik på høyre side
            }
        }

        return antallAvVerdi;
    }


    // TODO
    /** Oppgave 6 - del 3
     * Skal traversere (rekursivt eller iterativt treet i en rekkefølge og
     * sørge for at pekere og nodeverdier blir nullet ut.
     */
    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    // TODO
    /** Oppgave 3 - del 1
     * @param p rot
     * @param <T> .
     * @return Returnerer første node i post orden med p som rot
     */
    private static <T> Node<T> førstePostorden(Node<T> p) {
        Node<T> første = p;      // starter med neste i rotnoden

        // starter med å sjekke om noden er alene
        if(første.venstre == null && første.høyre==null && første.forelder == null){
            return p;
        }

        // fortsetter med å sjekke om foreldren har barn, ellers er forelder neste så lenge den ikke har andre barn
        else if(første.venstre == null && første.høyre == null && første.forelder.høyre == null){
            første = første.forelder;
        }


        // sjekker om søsken har barn, og da skal alle til venstre sjekkes først
        while(første.venstre != null || første.høyre != null) {
            while (første.venstre != null) {
                første = første.venstre;
            }
            while (første.høyre != null && første.venstre == null) {
                første = første.høyre;
            }
        }
        return første;
    }

    // TODO
    /** Oppgave 3 - del 2
     * @param p .
     * @param <T> .
     * @return Returnerer noden som kommer etter p i postorden
     */
    private static <T> Node<T> nestePostorden(Node<T> p) {
        Node<T> neste = p;      // starter med neste

        // starter med å sjekke om noden er alene
        if(neste.venstre == null && neste.høyre==null && neste.forelder == null){
            return null;
        }

        // fortsetter med å sjekke om foreldren har barn, ellers er forelder neste så lenge den ikke har andre barn
        else if(neste.venstre == null && neste.høyre == null && neste.forelder.høyre == null){
            neste = neste.forelder;
        }


        // sjekker om søsken har barn, og da skal alle til venstre sjekkes først
        while(neste.venstre != null || neste.høyre != null) {
            while (neste.venstre != null) {
                neste = neste.venstre;
            }
            while (neste.høyre != null && neste.venstre == null) {
                neste = neste.høyre;
            }
        }

        return neste;
    }

    // TODO
    /** Oppgave 4 - hjelpemetode del 1
     *
     * @param oppgave .
     */
    public void postorden(Oppgave<? super T> oppgave) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    // TODO
    /** Oppgave 4 - hjelpemetode del 2
     *
     * @param p
     * @param oppgave
     */
    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    // TODO
    /** Oppgave 5 - del 1
     *
     * @return
     */
    public ArrayList<T> serialize() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    // TODO
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

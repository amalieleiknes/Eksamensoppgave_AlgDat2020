package no.oslomet.cs.algdat.Eksamen;

import java.io.*;
import java.util.*;

public class EksamenSBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, hoyre;    // venstre og hoyre barn
        private Node<T> forelder;          // forelder

        // konstruktor
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            hoyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktor
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

    public EksamenSBinTre(Comparator<? super T> c)    // konstruktor
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
            else if (cmp > 0) p = p.hoyre;
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

        Node<T> p = forstePostorden(rot); // gaar til den forste i postorden
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
     * korrigert saa den gir riktig verdi i foreldrenode
     * @param verdi Verdien som skal legges inn
     * @return Returnerer true om veriden ble lagt inn, false hvis ikke
     */
    public boolean leggInn(T verdi)   {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.hoyre;     // flytter p
        }

        // p er naa null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<T>(verdi, q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.hoyre = p;                        // hoyre barn til q

        antall++;                                // én verdi mer i treet
        endringer++;
        return true;                             // vellykket innlegging
    }

    /** Oppgave 6 - del 1
     * Tatt fra kompendiet - korrigert saa den fungerer i min kode
     * @param verdi Verdien som skal fjernes
     * @return Returnerer true om den blir fjernet
     */
    public boolean fjern(T verdi) {  // horer til klassen SBinTre
        if (verdi == null || rot == null) return false;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal vaere forelder til p

        // leter etter noden og setter den som p
        while (p != null) {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre; }      // gaar til venstre
            else if (cmp > 0) { q = p; p = p.hoyre; }   // gaar til hoyre
            else break;    // den sokte verdien ligger i p
        }

        // om verdien ikke finnes i treet er p lik null
        if (p == null) {
            return false;   // finner ikke verdi
        }


        if (p.venstre == null || p.hoyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.hoyre;  // b for barn
            if (p == rot){
                rot = b;
            }
            else if (p == q.venstre){
                q.venstre = b;
                if(b!= null){
                    b.forelder = q;
                }
            }
            else {
                q.hoyre = b;
                if(b!= null){
                    b.forelder = q;
                }
            }
        }

        // Tilfelle 3: to barn
        else {
            Node<T> s = p;
            Node<T> r = p.hoyre;   // finner neste node i inorden

            // saalenge
            while (r.venstre != null) {
                s = r;              // s er forelder til r
                r = r.venstre;
            }

            // kopierer verdien i r til p,
            p.verdi = r.verdi;

            if (s != p) {
                s.venstre = r.hoyre;
            }
            else {
                s.hoyre = r.hoyre;
            }
        }

        antall--;   // det er naa én node mindre i treet
        return true;
    }

    /** Oppgave 6 - del 2
     * @param verdi Tar inn verdi som skal fjernes i alle noder
     * @return Returnerer hvor mange noder som er fjernet
     */
    public int fjernAlle(T verdi) {
        int antallFjernet = 0;

        if(rot==null || verdi==null || tom()){
            return antallFjernet;
        }
        else {
            Node<T> p = rot;

            while (p != null) {
                int cmp = comp.compare(verdi, p.verdi);      // sammenligner
                if (cmp < 0) {
                    p.forelder = p;
                    p = p.venstre;          // gaar til venstre
                } else if (cmp > 0) {
                    p.forelder = p;
                    p = p.hoyre;            // gaar til hoyre
                } else break;    // den sokte verdien ligger i p
            }

            if (p == null) {
                return 0;
            }

            if (p.venstre == null && p.hoyre == null && p.verdi == verdi && p.forelder == null) {
                rot.forelder = null;
                rot=null;
                antall = 0;
                antallFjernet++;
            } else {
                if (p.verdi == verdi) {
                    while(fjern(verdi)) {
                        antallFjernet++;
                        rot.forelder =null;
                    }
                }
            }
        }
        return antallFjernet;
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

            else if(cmp > 0){        //rotnodes verdi er storre enn verdien vi leter etter
                p = p.hoyre;
            }

            else {
                antallAvVerdi++;
                p = p.hoyre;        // gaar til hoyre fordi den kan vaere lik paa hoyre side
            }
        }

        return antallAvVerdi;
    }

    /** Oppgave 6 - del 3
     * Skal traversere (rekursivt eller iterativt treet i en rekkefolge og
     * sorge for at pekere og nodeverdier blir nullet ut.
     */
    public void nullstill() {
        if(!tom()) {
            Node<T> p = forstePostorden(rot);
            fjern(p.verdi);

            while (nestePostorden(p) != null) {
                p = nestePostorden(p);
                // fjern(p.verdi);
                antall--;
            }
        }
    }

    /** Oppgave 3 - del 1
     * @param p rot
     * @param <T> .
     * @return Returnerer forste node i post orden med p som rot
     */
    private static <T> Node<T> forstePostorden(Node<T> p) {
        Node<T> forste = p;      // starter med neste i rotnoden

        if(p==null){
            return null;
        }

        // starter med aa sjekke om noden er alene
        if(forste.venstre == null && forste.hoyre==null && forste.forelder == null){
            return p;
        }
        

        // fortsetter med aa sjekke om foreldren har barn, ellers er forelder neste saa lenge den ikke har andre barn
        else if(forste.venstre == null && forste.hoyre == null && forste.forelder.hoyre == null){
            forste = forste.forelder;
        }


        // sjekker om sosken har barn, og da skal alle til venstre sjekkes forst
        while(forste.venstre != null || forste.hoyre != null) {
            while (forste.venstre != null) {
                forste = forste.venstre;
            }
            while (forste.hoyre != null && forste.venstre == null) {
                forste = forste.hoyre;
            }
        }
        return forste;
    }

    /** Oppgave 3 - del 2
     * @param p .
     * @param <T> .
     * @return Returnerer noden som kommer etter p i postorden
     */
    private static <T> Node<T> nestePostorden(Node<T> p) {
        Node<T> neste = p;      // starter med neste

        // starter med aa sjekke om noden er alene
        if(neste.forelder == null){
            return null;
        }

        // sjekker om forelder har noen barn til hoyre som ikke er deg og ikke er null
        if(neste.forelder.hoyre != p && neste.forelder.hoyre != null){
            neste = neste.forelder.hoyre;               // hvis startnode er venstre barn gaar vi altsaa til hoyre barn for forelder

            // her kommer man til bunnen av treet, altsaa neste node
            // sjekker om noden har barn og da skal alle til venstre gaas gjennom forst
            if(neste.venstre != null || neste.hoyre != null) {

                while (neste.venstre != null || neste.hoyre != null) {      // loopes saa lenge det er barn. Venstre sjekkes forst

                    if (neste.venstre != null) {            // noden har venstrebarn
                        neste = neste.venstre;              // noden settes til venstre
                    }

                    else {
                        neste = neste.hoyre;
                    }
                }
            }
        }


        // hvis venstre og hoyre barn er null, gaar vi til forelder
        else {
            neste = neste.forelder;
        }

        return neste;
    }

    /** Oppgave 4 - hjelpemetode del 1
     * @param oppgave tar inn en oppgave som skal utføres
     */
    public void postorden(Oppgave<? super T> oppgave) {
        Node<T> p = forstePostorden(rot);

        // kaller metoden med den forste
        oppgave.utførOppgave(p.verdi);            // utforOppgave tar inn en T verdi

        // deretter bruker vi resten av verdiene
        while(nestePostorden(p)!=null){
            p = nestePostorden(p);          // husker paa aa endre p til nestePostorden
            oppgave.utførOppgave(p.verdi);  // legger inn p sin verdi som parameter og kaller paa utforOppgave
        }

    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    /** Oppgave 4 - hjelpemetode del 2
     * @param p Tar inn rotnoden forst, og deretter neste node i rekken
     * @param oppgave Tar inn en oppgave som skal kunne utfores
     */
    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {

        if (p==rot) {
            // finner forst den forste postorden (og passer paa at den aldri gaar inn der igjen)
            while (p.venstre != null || p.hoyre != null) {
                if (p.venstre != null) {
                    p = p.venstre;
                } else {
                    p = p.hoyre;
                }
            }
            // kaller metoden med den forste
            oppgave.utførOppgave(forstePostorden(p).verdi);
        }

        p = nestePostorden(p);
        if(p!=rot) {
            oppgave.utførOppgave(p.verdi);
            postordenRecursive(p, oppgave);
        }
        else{
            oppgave.utførOppgave(rot.verdi);
        }

    }

    /** Oppgave 5 - del 1
     * @return Returnerer en serialisert ArrayList (skal vaere paa nivaaorden)
     */
    public ArrayList<T> serialize() {
        ArrayList<T> valueList = new ArrayList<T>();
        ArrayList<Node<T>> nodeList = new ArrayList<Node<T>>();
        Node<T> midl;

        // forste som skal legges inn er roten i nivaaorden
        valueList.add(rot.verdi);
        nodeList.add(rot);

        // sjekker nivaaene nedover (legger til venstre og hoyre barn i listen
        // sjekker neste node i listen om den har barn og legger de til)
        // gaar igjen videre til neste i listen og sjekker dens barn
        for(int i = 0; i < antall; i++) {                   // starter paa nivaa 1, altsaa sjekker barna til rot
            midl = nodeList.get(i);                         // henter noden vi skal sjekke barna til

                if (midl.venstre != null) {
                    valueList.add(midl.venstre.verdi);      // legger til nodeverdien i valueList
                    nodeList.add(midl.venstre);             // legger til venstre node i nodeList, har oversikt over noderekkefolgen
                }
                if (midl.hoyre != null) {
                    valueList.add(midl.hoyre.verdi);
                    nodeList.add(midl.hoyre);
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream("listData");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(valueList);
            oos.close();
            fos.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return valueList;
    }

    /** Oppgave 5 - del 2
     * @param data Tar inn arrayListen som skal deserialiseres
     * @param c Tar inn en parameter som skal sammenligne verdier
     * @param <K> .
     * @return Returnerer deserialisert array
     */
    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        EksamenSBinTre<K> tre = new EksamenSBinTre<K>(c);

        // hvis tomt array
        if(data.size() == 0){
            return null;
        }

        // lager en rot med forste verdi i arrayet og forelder lik null
        Node<K> rot = new Node<K>(data.get(0), null);

        // prover aa legge inn nodene i treet igjen
        try {
            FileInputStream fos = new FileInputStream("listData");
            ObjectInputStream oos = new ObjectInputStream(fos);
            oos.read();
            oos.close();
            fos.close();

            tre.leggInn(rot.verdi);
            for (int i = 1; i < data.size(); i++){
                tre.leggInn(data.get(i));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return tre;
    }

} // ObligSBinTre
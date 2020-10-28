package no.oslomet.cs.algdat.Eksamen;


import java.io.*;
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
    public boolean leggInn(T verdi)   {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<T>(verdi, q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q

        antall++;                                // én verdi mer i treet
        endringer++;
        return true;                             // vellykket innlegging
    }

    // TODO
    /** Oppgave 6 - del 1
     * Tatt fra kompendiet - korrigert så den fungerer i min kode
     * @param verdi Verdien som skal fjernes
     * @return Returnerer true om den blir fjernet
     */
    public boolean fjern(T verdi) {
        if (verdi == null || rot == null) {
            return false;  // treet har ingen nullverdier og man kan ikke fjerne noe i et tomt tre
        } else {
            Node<T> p = rot;
            Node<T> q = null;   // q skal være forelder til p

            while (p != null) {                              // leter etter verdi til den enten er funnet og ligger i p eller ikke er funnet
                int cmp = comp.compare(verdi, p.verdi);      // sammenligner verdien tatt inn med p sin verdi
                if (cmp < 0) {
                    q = p;
                    p = p.venstre;
                }      // går til venstre hvis verdien er mindre
                else if (cmp > 0) {
                    q = p;
                    p = p.høyre;
                }   // går til høyre hvis verdien er større
                else break;                                 // den søkte verdien ligger i node p
            }

            // fant ikke verdi, returnerer false
            if (p == null) {
                return false;
            }

            // hvis kun roten er i treet
            else if(p == rot && p.venstre == null && p.høyre == null){
                rot = null;
            }

            // Tilfelle 1: ingen barn
            else if (p.venstre == null && p.høyre == null) {
                p.forelder = null;

            }

            // Tilfelle 2a: nøyaktig ett barn på venstre side
            else if (p.venstre != null && p.høyre == null) {          //høyre barn
                Node<T> b = p.venstre;                           // barnet settes til b

                if (p == q.venstre) {
                    q.venstre = b;
                } else {      // ellers peker den høyre på barn
                    q.høyre = b;
                }
            }

            // Tilfelle 2b: nøyaktig ett barn på høyre side
            else if (p.venstre == null) {          // venstre barn
                Node<T> b = p.høyre;

                assert q != null;
                if (p == q.venstre) {
                    q.venstre = b;
                } else {      // ellers peker den høyre på barn
                    q.høyre = b;
                }

            }

            // Tilfelle 3: noden har to barn
            else {
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
            endringer++;
            return true;
        }
    }

    /** Oppgave 6 - del 2
     *
     * @param verdi Tar inn verdi som skal fjernes i alle noder
     * @return Returnerer hvor mange noder som er fjernet
     */
    public int fjernAlle(T verdi) {
        int antallFjernet = 0;
        while(fjern(verdi)){
            antall--;
            endringer++;
            antallFjernet++;
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
        Node<T> p = førstePostorden(rot);

        if(p!=null) {
            fjern(p.verdi);
            p = nestePostorden(p);

            while (p!=null){
                if (!fjern(p.verdi)) break;
                p = nestePostorden(p);
                assert p != null;
                fjern(p.verdi);
            }

        }
        antall = 0;
    }

    /** Oppgave 3 - del 1
     * @param p rot
     * @param <T> .
     * @return Returnerer første node i post orden med p som rot
     */
    private static <T> Node<T> førstePostorden(Node<T> p) {
        Node<T> første = p;      // starter med neste i rotnoden

        if(p==null){
            return null;
        }

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

    /** Oppgave 3 - del 2
     * @param p .
     * @param <T> .
     * @return Returnerer noden som kommer etter p i postorden
     */
    private static <T> Node<T> nestePostorden(Node<T> p) {
        Node<T> neste = p;      // starter med neste

        // starter med å sjekke om noden er alene
        if(neste.forelder == null){
            return null;
        }

        // sjekker om forelder har noen barn til høyre som ikke er deg og ikke er null
        if(neste.forelder.høyre != p && neste.forelder.høyre != null){
            neste = neste.forelder.høyre;               // hvis startnode er venstre barn går vi altså til høyre barn før forelder

            // her kommer man til bunnen av treet, altså neste node
            // sjekker om noden har barn og da skal alle til venstre gås gjennom først
            if(neste.venstre != null || neste.høyre != null) {

                while (neste.venstre != null || neste.høyre != null) {      // loopes så lenge det er barn. Venstre sjekkes først

                    if (neste.venstre != null) {            // noden har venstrebarn
                        neste = neste.venstre;              // noden settes til venstre
                    }

                    else {
                        neste = neste.høyre;
                    }
                }
            }
        }


        // hvis venstre og høyre barn er null, går vi til forelder
        else {
            neste = neste.forelder;
        }

        return neste;
    }

    /** Oppgave 4 - hjelpemetode del 1
     * @param oppgave tar inn en oppgave
     */
    public void postorden(Oppgave<? super T> oppgave) {
        Node<T> p = rot;

        // finner først den første postorden
        while(p.venstre!=null || p.høyre!=null){
            if(p.venstre!=null){
                p = p.venstre;
            }
            else {
                p = p.høyre;
            }
        }

        // kaller metoden med den første
        T verdi = førstePostorden(p).verdi;
        oppgave.utførOppgave(verdi);            // utførOppgave tar inn en T verdi


        // deretter bruker vi resten av verdiene
        while(nestePostorden(p)!=null){
            p = nestePostorden(p);          // husker på å endre p til nestePostorden
            oppgave.utførOppgave(p.verdi);  // legger inn p sin verdi som parameter og kaller på utførOppgave
        }

    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    /** Oppgave 4 - hjelpemetode del 2
     * @param p Tar inn rotnoden først, og deretter neste node i rekken
     * @param oppgave Tar inn en oppgave som skal kunne utføres
     */
    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {

        if (p==rot) {
            // finner først den første postorden (og passer på at den aldri går inn der igjen)
            while (p.venstre != null || p.høyre != null) {
                if (p.venstre != null) {
                    p = p.venstre;
                } else {
                    p = p.høyre;
                }
            }
            // kaller metoden med den første
            oppgave.utførOppgave(førstePostorden(p).verdi);
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
     * @return Returnerer en serialisert ArrayList (skal være på nivåorden)
     */
    public ArrayList<T> serialize() {
        ArrayList<T> valueList = new ArrayList<T>();
        ArrayList<Node<T>> nodeList = new ArrayList<Node<T>>();
        Node<T> midl;

        // første som skal legges inn er roten i nivåorden
        valueList.add(rot.verdi);
        nodeList.add(rot);

        // sjekker nivåene nedover (legger til venstre og høyre barn i listen
        // sjekker neste node i listen om den har barn og legger de til)
        // går igjen videre til neste i listen og sjekker dens barn
        for(int i = 0; i < antall; i++) {                   // starter på nivå 1, altså sjekker barna til rot
            midl = nodeList.get(i);                         // henter noden vi skal sjekke barna til

                if (midl.venstre != null) {
                    valueList.add(midl.venstre.verdi);      // legger til nodeverdien i valueList
                    nodeList.add(midl.venstre);             // legger til venstre node i nodeList, har oversikt over noderekkefølgen
                }
                if (midl.høyre != null) {
                    valueList.add(midl.høyre.verdi);
                    nodeList.add(midl.høyre);
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

        // lager en rot med første verdi i arrayet og forelder lik null
        Node<K> rot = new Node<K>(data.get(0), null);

        // prøver å legge inn nodene i treet igjen
        try {
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

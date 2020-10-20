package no.oslomet.cs.algdat.Eksamen;

/** Kopiert fra kildekode i kompendiet - delkapittel 3.1
 *
 */

import java.util.*;
import java.util.function.Predicate;

public interface Beholder<T> extends Iterable<T>  // ny versjon
{
    public boolean leggInn(T verdi);    // legger inn verdi i beholderen
    public boolean inneholder(T verdi); // sjekker om den inneholder verdi
    public boolean fjern(T verdi);      // fjerner verdi fra beholderen
    public int antall();                // returnerer antallet i beholderen
    public boolean tom();               // sjekker om beholderen er tom
    public void nullstill();            // tømmer beholderen
    public Iterator<T> iterator();      // returnerer en iterator

    default boolean fjernHvis(Predicate<? super T> p)  // betingelsesfjerning
    {
        Objects.requireNonNull(p);                       // kaster unntak

        boolean fjernet = false;
        for (Iterator<T> i = iterator(); i.hasNext(); )  // løkke
        {
            if (p.test(i.next()))                          // betingelsen
            {
                i.remove(); fjernet = true;                  // fjerner
            }
        }
        return fjernet;
    }
} // grensesnitt Beholder

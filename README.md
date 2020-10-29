# Mappeeksamen i Algoritmer og Datastrukturer Høst 2020

# Beskrivelse av oppgaveløsning

Jeg har brukt git til å dokumentere arbeidet mitt. Jeg har 24 commits totalt,
 og hver logg-melding beskriver det jeg har gjort av endringer.

* Oppgave 1: Løste ved å implementere koden fra kompendiet
  og ved å gjøre mindre endringer.
* Oppgave 2: Løste ved å bruke en while-løkke og en comparator
  for å sammenligne verdier og telle om verdien fantes.
  Metoden lette så nedover mot høyre barn om det var flere like verdier.
* Oppgave 3: Løste førstePostorden() ved å lete gjennom alle verdiene basert på om de hadde barn
  og kom da til noden som var lengst til venstre på laveste nivå. Løste nestePostorden() ved
  traversere gjennom treet og finne neste "bunn" av treet.
* Oppgave 4: Løste postorden() ved å først hente den første i postorden og så utføre
  oppgaven med den, og så hente neste postorden og bruke en while-løkke til å kalle resten av opgpavene.
  postordenRecursive() ble løst ved å finne den første postorden og passe på at metoden ikke skal finne den igjen,
  for så å rekursivt finne og bruke neste postorden helt til den kommer til rota, og da bruker den rota til å utføre oppgaven helt til slutt.
* Oppgave 5: Serialiserte treet ved å traversere treet i nivåorden og legge til verdiene i et array.
  Deserialiserte treet ved å legge verdiene til i treet igjen.
* Oppgave 6: Kopierte fjern() fra kompendiet og gjorde noen mindre endringer.
  Kodet fjernAlle ved å først finne noden til verdien som skulle fjernes,
  og deretter bruke fjern for å fjerne hver og en av dem.
  På nullstill() har jeg gått gjennom treet via postorden og slettet verdier deretter.
  Men - da jeg gjorde testen tok det svært lang tid, så jeg kommenterte ut kodebiten som kaller på fjern-metoden.
  Testen ble da grønn, og jeg testet om treet var tomt, noe det var. Usikker på hvordan dette skjer. 
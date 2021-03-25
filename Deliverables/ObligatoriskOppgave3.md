#Obligatorisk oppgave 3

###Brukerhistorier:
1) Som spiller vil jeg kunne si ifra om at jeg er klar, og når alle er klare skal runden begynne, slik at spillet
går framover.
   
Akseptansekriterier:
- Det går an å "si ifra" om at du er klar.
- Når alle spillere er klare skal runden begynne.
    - Kort i programregister skal spilles.
    - Nye kort skal deles ut.
    
Arbeidsoppgaver:
- Implementere en knapp som sier fra om spilleren er klar.
- Implementer en metode som kjører når alle spillere er klare.
    - Denne metoden skal kjøre gjennom alle kortene i rett rekkefølge og sette opp neste runde (dele ut nye kort)

2) Som server host vil jeg kunne se når de andre spillerene som er koblet til serveren min
gjør trekk, slik at jeg vet at de andre er koblet til.
   
Akseptansekriterier:
- Gitt at en spiller hoster en server og minst en annen spiller er koblet til, og begge spillerene
  gjør en turn, så skal de kunne se hverandres bevegelser på hver sin skjerm.
  
Arbeidsoppgaver:
- Implementere updatePlayer metode i GameClient som sender client data til serveren.
- Implementere hashmap med uuid key og playerList value som oppdateres av serveren.
- Implementere at man kan sende cards til serveren og at serveren sender cards tilbake til clienten.

3) Som spiller vil jeg ha flere kart å spille på slik at jeg får mer variasjon i spillet.

Akseptansekriterier:
- Gitt at en spiller ser på map selection, så skal det være flere kart å velge fra.

Arbeidsoppgaver:
- Lage kart i Tiled.

4) Som spiller ønsker jeg kun å vinne hvis jeg treffer flaggene i riktig rekkefølge, slik at spillet
er vanskelig og medfører heder og ære når jeg vinnner.
   
Akseptansekriterier:
- Gitt at det er 0 < x < 5 flagg på kartet, så må en spiller besøke flaggene i riktig rekkefølge
for å vinne spillet.
  
Arbeidsoppgaver:
- Implementere hashmap flagVisits i Robot som holder styr på hvilke flagg spilleren har besøkt.
- Implementere metode checkFlag() i BoardLogic som sjekker f.eks at robot har besøkt flagg 1 og 2 før den kan få nr 3.
- Implementere metode setFlagPositions() i BoardLogic som fyller ut flagVisits med riktig mengde flagg
på starten av spillet.
- Implementere checkWin() i Robot som sjekker om roboten har tatt alle flaggene.

5) Som spiller ønsker jeg at jeg kan dytte andre roboter når jeg kolliderer med dem, slik at jeg
kan ødelegge for de andre spillerene.
   
Akseptansekriterier:
- Gitt at robot 1 beveger seg inn i ruten der robot 2 står, skal det skje en kollisjon som gjør
at robot 2 blir dyttet i motsatt retning av der robot 1 kom fra.
- Gitt at robot 2 ville blitt dyttet inn i en vegg, skal robot 2 oppføre seg som en vegg for
  robot 1, slik at verken av robotene beveger seg.
  
Arbeidsoppgaver:
- Implementere kode i movePlayer() i BoardLogic som sjekker om den nye posisjonen inneholder
en robot.
- Implementere kode i movePlayer() som kjører movePlayer() rekursivt på roboten som blir dyttet.

6) som kort vil jeg ha egen prioritet slik at rekkefølgen til flere kort
   som tilhører forskjellige spillere alltid er bestemt.

Akseptansekriterier:
- Gitt at en spiller har valgt kortene sine, skal spillerens kort brukes
 i riktig rekkefølge (i forhold til spillerens andre kort).
- Gitt at alle spillere har valgt sine kort, skal kortene på hvert steg
 brukes i riktig rekkefølge i forhold til prioriteten til andre spillernes
 kort.

Arbeidsoppgaver:
- Implementere en metode i GameLogic som setter spillerens registers i riktig
 rekkefølge.
- Implementere en metode i GameLogic som sammenligner spillernes første, andre,
 tredje,... registers og setter de i riktig rekkefølge.

7) som spiller ønsker jeg at et hvert spiller-trekk blir presentert på en korrekt måte
   i de andre spiller-klientene slik at jeg vet at jeg spiller det samme spillet som 
   de andre spillerene.

Akseptansekriterier:
- Gitt at en spiller har valgt kortene sine, skal denne spilleren også ha valgt
  de samme kortene i de andre klientene.
- Gitt at alle spillerene har valgt kort, skal spillet utføres på en deterministisk
  måte slik at alle klientene viser det samme resultatet på brettet etter en runde.

Arbeidsoppgaver:
- Implementere en metode som får klienten til å vente helt til alle spillerene har
  låst inn kortene sine.
- Når alle spillene har låst inn kortene sine skal klienten bruke den sist oppdaterte
  informasjonen til å spille ut runden.

8) som spiller vil jeg kunne bruke en power-down token til å hoppe over robottens
   neste tur.

Akseptansekriterier:
- Gitt at robotten har en eller flere damage tokens, så skal spilleren kunne
  velge å bruke en power-down token.
- Gitt at spilleren har valgt å bruke en power-down token etter å ha valgt sine
  registers, så skal de få skippe sin neste tur.

Arbeidsoppgaver:
- Implementere en powerDown metode i GameLogic som setter powerDown variabelen
  hos player lik true.
- Endre på ready() metoden i GameLogic slik at spilleren ikke får delta i
  spillet når powerDown variabelen er lik true.
  
###Møtereferater:

10.03 (Onsdag, 12:15-14:00)
- Lagde en konkret plan for hva som må gjennomføres i denne sprinten
- Bestemte oss for at møte Torsdag (11.03) utgikk

15.03 (Mandag, 12:00-14:00)
- Fikk implementert en fungerende multiplayer
- Bestemte oss for å ta "meta"-deloppgavene på Onsdag (24.03)
- Skrev noen brukerhistorier og la til "To do" i prosjekttavle

17.03 (Onsdag, 12:15-14:15)
- Alle var tilstede 
- La til ting på project-boardet som vi begynte å jobbe med
- Simon lagde nye maps
- Sassan feilsøkte egen bransje og begynte på kort prioritering
- Asle kombinerte knappene "Submit" og "Do turn" til en knapp "Do turn"
  og implementerte en form for synkronisering mellom klientene

18.03 (Torsdag 12:00-14:00)
- Alle var tilstede
- Simon jobbet med robot kollisjon
- Sassan jobbet med kort prioritering (Ørjan fikk det til) og begynte på testing
- Ørjan jobbet med å vise hvilket kort som blir spilt
<<<<<<< HEAD
- Asle fikset bugs angående synkronisering av rotasjon
=======
- Asle jobbet med at flerspiller-logikken ble synkronisert

>>>>>>> 8205e294db08f5f0ad148a6fb72fabc346b6deb7

22.03 (Mandag 14:00 - 16:15)
- Asle, Sassan og Simon var på møtet
- Simon skrev test kode
- Sassan forsøkte å teste metodene fra forrige gang og implementerte powerDown
  funksjonalitet (ikke ferdig)
- Asle implementerte conveyorbelts  

24.03 (Onsdag 12:15-14:00)
- Alle var tilstede
- Sassan gjorde ferdig powerDown funksjonaliteten og begynte på testing
- Simon skrev tester og teknisk dokumentasjon
###Prosjekt og prosjektstruktur

###Retrospektiv

###Teknisk informasjon
####Krav og kjøreinstruksjoner
Krav til software:
- Java 9 og opp
- Maven 3.6.3 og opp

Åpne command promt (cmd) og skriv følgende kommandoer:
- cd *lokasjon til spill-filene*
- mvn clean install
- mvn exec:java

#### Hvordan manuelt teste programmet
Det ligger instruksjoner i ManualTest folderen.

#### Spillinstruksjoner
Når spillet er åpnet får man valget mellom "host" og "join". Trykk "host" for å kjøre en spillserver,
eller trykk "join" hvis du vil koble til en annen persons server.

Hvis du trykket "host" vil du møte en skjerm hvor du kan velge kart ved å trykke på de ulike kartene på listen.
Et bilde av kartet vil henholdsvis bli vist på høyresiden. Nede til venstre får du mulighet til å skrive inn spill-taggen din.
Deretter trykk "play" nede i høyre hjørne for å starte serveren.

Hvis du trykket "join" blir du sendt til en skjerm som lar deg skrive inn en ip-adresse til en annen server i feltet det står "localhost"
og spill-taggen din i feltet det står "name". Deretter trykk "play" knappen nede i venstre hjørne for å koble til serveren.

Nå vil du komme til spill lobbyen. Hvor man venter på at alle skal få koble seg til serveren før man starter
spillet. Her kan du også se roboten du vil spille som. Hosten kan da trykke "play" nede i venstre hjørne for å starte spillet.

Når spillet har startet får du muligheten til å programmere roboten din med kortene du får tildelt.
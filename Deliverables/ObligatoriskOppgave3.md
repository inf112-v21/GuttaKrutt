#Obligatorisk oppgave 3

###Brukerhistorier:
1) som spiller vil jeg kunne varsle serveren når jeg er ferdig med min tur.

   Akseptansekriterier:
  - Gitt at spilleren har valgt kortene sine (registers), så skal serveren
    få beskjed om at turen er ferdig.
  - Gitt at alle spillerne utenom en er ferdig med sin tur, skal serveren
    gi beskjed til den manglende spilleren.

   Arbeidsoppgaver:
   - 

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

5) som kort vil jeg ha egen prioritet slik at rekkefølgen til flere kort
   som tilhører forskjellige spillere alltid er bestemt.

   Akseptansekriterier:
  - Gitt at
   
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
- Simon lagde nye maps
- Sassan feilsøkte egen bransje og begynte på kort prioritering

18.03 (Torsdag 12:00-14:00)
- Alle var tilstede
- Simon jobbet med robot kollisjon
- Sassan jobbet med kort prioritering (Ørjan fikk det til) og begynte på testing
- Ørjan jobbet med å vise hvilket kort som blir spilt

###Retrospektiv

###Teknisk informasjon

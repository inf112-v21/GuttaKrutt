#Obligatorisk Oppgave 2

###Brukerhistorier
1) Som robot vil jeg miste liv når jeg går i en laser slik at brettet får flere hindringer.
  
   Akseptansekriterier:
   - Gitt at en robot står i en laser når laseren oppdateres, tar roboten 1 damage
   - Gitt at en robot ikke står i en laser, tar roboten ikke damage fra lasere 
     
   Arbeidsoppgaver:
   - Legge til en kondisjon i laseren som sjekker om en robot står i ruten laseren krysser, hvis det er tilfellet
     kaller den funksjonen Robot[i].addDamage(1) og stopper å iterere laseren.

    
2) Som laser vil jeg kunne krysse andre lasere slik at oppførselen min stemmer overens
med regelboken.
   
   Akseptansekriterier:
   - Gitt at 2 vinkelrette lasere peker inn mot en rute, skal begge
     laserene krysse og så fortsette i sine respektive retninger (så lenge de ikke blir blokkert av robot/vegg)
   - Gitt at 2 vinkelrette lasere okkuperer samme rute, skal grafikken
     represente dette ved at laserene krysser hverandre
     
   Arbeidsoppgaver:
   - Implementere slik at den laser-iteratoren sjekker om ruten den er i har en vinkelrett laser fra før. Hvis den
     har det, skal hele ruten erstattes med en tredje type laser som representerer 2 kryssende lasere.
   - Laserlayer skal gi informasjon om hvilke typer laser som okkuperer en gitt tile.
    
3) Som vegg vil jeg kunne blokke lasere slik at roboter kan bruke meg som dekning.
   
   Akseptansekriterier:
   - Gitt at en laser peker inn mot en rute med en vegg, skal laseren
     blokkeres hvis veggen ligger på den siden som laseren kommer fra
   - Gitt at en laser peker ut fra en rute med en vegg, skal laseren
     blokkeres hvis veggen ligger på den siden som laseren peker mot
     
   Arbeidsoppgaver:
   - Implementere en if-condition i laser-iteratoren som sjekker om det er en vegg i ruten motsatt fra der laseren
     kommer fra, eller om det er en vegg i den neste ruten laseren iterer mot som blokkerer der laseren vil inn.
     Denne if-conditionen stopper laseren hvis en eller begge av disse veggene eksisterer.

4) Som spiller vil jeg kunne hoste en server slik at andre spillere kan koble seg til serveren min.

    Akseptansekriterier:
    - Gitt at hosten har internett, så skal hosten kunne kjøre en server.
    - Gitt at serveren kjører, så skal andre spillere kunne koble seg til serveren.
    
    Arbeidsoppgaver:
    - Implementere GameServer klasse, som skal hoste en kryonet server
    - Implementere GameClient klasse, som skal klare å sende informasjon til serveren,
      samt motta informasjon fra serveren.
   

5) Som spiller vil jeg få utdelt kort fra en kortstokk slik at jeg kan velge fem kort å bruke hver runde.

   Akseptansekriterier:
   - Gitt at en spiller har X damageTokens, skal den spilleren få
     utdelt 9-X kort.
   - Gitt at en spiller har valgt kortene sine, skal den ikke kunne
     se på eller forandre sine kort.

   Arbeidsoppgaver:
   - en metode dealCard() som skal ha en for-loop som deler ut kort og
     kjører (9-damageTokens) mange ganger hos hver spiller.
   - en nettverk-styrt tur som avslutter når alle spillere har valgt
     kortene sine, der spillere må vente til turen er ferdig.

6) Som robot vil jeg få fem kort som jeg kan bruke til å utføre
   forskjellige handlinger.

   Akseptansekriterier:
   - Gitt at bare en spiller er igjen som ikke har valgt kortene til
     robotten, skal det kjøres en timer på 30 sekunder.
   - Gitt at robotten ikke er shut-down og har fått fem kort, skal alle
     handlingene utføres i riktig rekkefølge.

   Arbeidsoppgaver:
   - en runTimer() metode i serveren som kjøres når alle spillere utenom
     en har valgt kortene sine og venter på at turen blir ferdig.
   - en programCards() metode som setter kortene i en liste med riktig
     rekkefølge og sier til Board klassen hva alle board elementene skal
     gjøre.

7) Som spiller i et multiplayer spill vil jeg kunne se hvordan robotene til de andre
   spillerene beveger seg slik at jeg kan planlegge rundt de andres bevegelser.

   Akseptansekriterier:
    - Gitt at alle spillerene er koblet til serveren, skal alle kunne se hverandre i sitt eget spill.
    - Gitt at alle spillerene er koblet til samme server, skal de kunne se hverandres bevegelser.

   Arbeidsoppgaver:
    - Implementere at hver spiller sender en oppdatering av roboten sin hver gang etter de beveger seg.
    - Implementere at hver spiller holder oversikt over oppdateringene til de andre robotene.
    - Implementere at serveren tar imot oppdateringer fra spillerene og videresender oppdateringene
      til de andre spillerene.
   
### Oppsummering møter - oblig 1

17.02 (Onsdag, 12:15-14:00)
- Gikk gjennom punktene i oblig 2
- Kom frem til at vi skulle bli ferdig med flere av
MVP-kravene fra oblig 1
- Få til mer seperasjon av grafikk og logikk
- Snakket med gruppeleder angående testing av logikk
- Bestemte oss for å droppe møte den 18.02
- Sassan begynte på en enkel logikkfil som behandler robot,
kort og spiller-klassen
  
18.02 (Torsdag, Utgått)

22.02 (Mandag, 12:00-14:00)
- Ørjan begynte å jobbe med kollisjon
- Asle begynte å jobbe med implementasjon av testing
- Simon begynte å jobbe med multiplayer
- Sassan begynte å jobbe med kort/kortstokk klasser
- Satt mål om å separere logikken til kartet fra GUI'en (Ørjan og Asle tar ansvar)

24.02 (Onsdag, 12:15-14:00)
- Testing av ulike funksjoner krever mer seperasjon av logikk fra grafikk,
unit testing er nedprioritert til logikken er separert
- Ørjan implementerte matrise-kart. Logikken operer med en matrise av kartet istedet for tilemap, dette
gjør det mulig å teste kartet/controls uten å kjøre applikasjonen
- Simon implementerte online funksjonalitet, opprettet en enkel online chat
- Sassan opprettet Card og Deck klassen og begynte å jobbe med en gameLogic klasse som
omhandler disse 2 klassene.

25.02 (Torsdag, 12:00-14:00)
- Alle arbeidet videre på det som ble nevnt i forrige møte. Ingenting nytt ble bestemt.

01.03 (Mandag, 12:00-14:00)
- Asle begynte å jobbe med laser-logikk, med mål om å flytte all laser-logikken vekk
fra GUI'en over til Controls klassen og at laseren kommer fra spesifikke vegger istedet for spillere.
- Ble enige om å utsette refleksjoner/gruppevurdering til onsdagen

03.03 (Onsdag, 12:15-14:00)
- Gikk gjennom møte-referatene 

###Deloppgave 1


###Prosjekt og prosjektstruktur

Rollene til nå har fungerert fint. Vi vil bestemme diverse nye roller etterhvert som flere arbeidsoppgaver oppstår.
Scrum metodikken fungerte bra, slik vi jobber går sprints hånd i hånd med oblig-fristene. Gruppedynamikken har vært god
og balansert, og vi har hatt hyppige møter der vi får kommunisert effektivt og alle får bidratt med sine ideer om hvordan
prosjektet skal utvikles fremover.

###Retrospektiv
I etterkant kom vi frem til at vi bør skrive flere brukerhistorier og akseptansekriterier før vi begynner å skrive kode.
Vi bør også skrive tester hyppigere, men det har ikke vært så lett før vi fikk separert logikken fra grafikken.
Slik det har vært til nå har mengden commits vært relativt jevnt, det er ingen som ikke bidrar skikkelig. 
(Av en eller annen grunn kommer ikke Asle opp på contributors)
De tre forbedringspunktene vi ble enige om er:
1) Skrive flere brukerhistorier/akseptansekriterier før vi begynner å kode
2) Ha mer hyppig test-skriving
3) Skrive møtereferater på dagen vi har hatt møte

### Hvordan bygge og kjøre programmet
Krav til software:
- Java 9 og opp
- Maven 3.6.3 og opp

Åpne command promt (cmd) og skriv følgende kommandoer:
- cd *lokasjon til spill-filene*
- mvn clean install
- mvn exec:java -Dexec.mainClass="Main"

### Hvordan manuelt teste programmet
Det ligger instruksjoner i ManualTest folderen.

###Deloppgave 2
I begynnelsen av sprint 2 bestemte vi oss for å fokusere på punkt 6 - 9 i MVP kravene fra
oblig 1, som i hovedsak var multiplayer og kort funksjonalitet. I tillegg bestemte vi oss
for å implementere vegger og lasere. For å implementere disse måtte vi først separere GUI'en 
og forretningslogikken skikkelig, så denne seperasjons-prosessen fikk en høyere prioritet. 
Ettersom MVP-kravene avhenger av forretningslogikken til brettet, ble disse nedprioritert og 
noen av dem utsatt til neste sprint.

###Bugs:
- Laserene opprettes ikke før spilleren har beveget seg ett steg.

###Klassediagram
Se "Roborally UML.png" i deliverables mappe.
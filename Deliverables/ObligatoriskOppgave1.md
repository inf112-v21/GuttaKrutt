# Obligatorisk Oppgave 1


## Spesifikasjon
### Beskrivelse

Det overordnede målet for applikasjonen er å lage vår egen digitale versjon av roborally som flere spillere
kan spille sammen over nettet. Applikasjonen skal fungere som en komplett versjon av roborally, med unntak
av enkelte funksjoner spillet har, som for eksempel option cards og hammer ruten. Prosjektet lages
i Maven java.

### Hvordan bygge og kjøre programmet

Åpne command promt (cmd) og skriv følgende kommandoer:
- cd *lokasjon til spill-filene*
- mvn clean install
- mvn exec:java -Dexec.mainClass="Main"


### Brukerhistorier

Brukerhistoriene under er arrangert i prioritert rekkefølge.

1) Som spiller vil jeg kunne se brettet og faste objekter slik at innlevelsen øker.  

  Akseptansekriterier:
- Gitt at det er et brett i spillet, så vises brettet grafisk.
- Gitt at det er objekt på brettet, så vises objekter
Som spiller vil jeg kunne se spillebrikken min slik at jeg vet hvor på brettet jeg står. 
  
Arbeidsoppgaver:

- Redigere create() metoden for å opprette brettet.
- Redigere render() metoden for å vise brettet.
- Finne tile-assets til brettet.
        
Akseptansekriterier:
- Gitt at en spillerbrikke eksisterer, så skal brikken vises grafisk.
- Gitt at brikken flytter seg skal grafikken oppdateres med den nye posisjonen.
- Gitt at spillerbrikken ligger på en posisjon på brettet skal brikken 
  vises grafisk på denne posisjonen
  
Arbeidsoppgaver:

- Opprette brikken i create() metoden og vise den i render() metoden.
- Finne robot-assets til brikken.

2) Som spiller vil jeg kunne flytte robotten min.
        Akseptansekriterier:
- Gitt at spilleren trykker på flyttetastene skal spillerbrikken flytte seg henholdsvis.

Arbeidsoppgaver:

- Opprette en ny metode keyUp() som reagerer på arrow- og WASD-taster.
- Bør kunne også resette brettet. Opprette en ny metode movePlayer() som
flytter brikken på brettet.

3) Som spiller vil jeg kunne besøke et flagg for å vinne.
        Akseptansekriterier:
- Gitt at det er en spiller og et flagg på brettet, skal spilleren kunne flytte seg til flagget.
- Gitt at spilleren står på ruten med flagg, så vinner spilleren.


Arbeidsoppgaver:

- Legge en betingelse til checkTile() slik at spilleren får en melding
når de er i samme tile som flagget (de har vunnet).

### Del 1 & 2
Programmeringsbakgrunnen til teamet:

- Asle: Går tredje år på bachelor i matematikk og har hatt INF100 og INF101.

- Ørjan: Går tredje år på datateknologi og har hatt INF100, INF101 og INF102.

- Simon: Går tredje år på datavitskap og har hatt INF100, INF101 og INF102.

- Sassan: Går tredje år på datavitskap og har hatt INF100, INF101 og INF102.

Rollene i teamet er Teamlead Asle, Grafikkutvikler Ørjan og Logikkutviklere Sassan og Simon. Siden alle i teamet 
har ca like mye forkunnskaper i java, så er rollevalg basert på hvilke roller vi følte vi trengte 
og hva hver enkelt person ønsket å jobbe med. 

Teamet vårt har bestemt oss for å holde møter mandag kl 12, onsdag kl 12 og tordag kl 12
siden disse tidpunktene passer best for de ulike timeplanene våres. Møtene  vil foregå over discord,
samt kommunikasjon ellers. Arbeidsfordelingen blir at hver person tar på seg oppgaver som trengs i sitt felt.
Oppfølging av arbeid vil i hovedsak foregå under
møtene, hvor vi diskuterer hverandres arbeid og planlegger hvordan vi skal gå videre.
Fildeling gjør vi i github repoet vårt, hvor vi alle har vår egen branch og en master branch som inneholder
den fullstendige koden vår.

Prosjektmetodikk:


Teamet vårt har bestemt oss for å bruke kanban med elementer fra scrum. Fra scrum vil vi bruke scrum sprints, der
hver oblig blir et sprint, hvor vi planlegger en rekke brukerhistorier vi skal gjøre ferdig og ikke arbeide mer enn planlagt.
Vi skal også bruke et scrum board, så når et nytt scrum sprint begynner tar vi og reseter project boardet vårt.
Retrospektivt så har vi de to siste ukene ikke hatt en fast arbeidsmetodikk utenom å bruke projectboardet
til å visualisere hvilken fase arbeidsoppgavene var i. Framover har vi bestemt oss for å være tydelige på hvilke
brukerhistorier som skal løses og arbeidsoppgaver som skal implementeres i sprintet før vi begynner. Grunnen til
at vi bestemte oss for dette er for å ha bedre struktur i arbeidet vårt, slik at prosjektet blir mer ryddig og oversiktlig
for å minimere forvirrelse og øke produktivitet.

### Oppsummering møter - oblig 1

03.02 (Onsdag, 12:15-13:50)
- Introduksjon til teamet
- Oppretting av repo/tekniske detaljer rundt github
Prosjekt/opretting av prioritet system
- Generell orientering rundt faget/prosjektet
- Gått gjennom arbeidskravene til obligen
- Begynte ikke skikkelig på obligen pga kun 3/5 møtte opp

10.02 (Onsdag, 12:15-1400)
- Diskuterte strukturen til applikasjonen
- Laget et diagram for hele applikasjonen (inkl. objekter og interface)
- Laget en del brukerhistorier

11.02 (Torsdag, 12:00-13:30)
- Oppdaterte oss på den grafiske delen (libgdx)
- Planla timeplan for møter (mandag-kl12/onsdag-kl12/torsdag-kl12)

12.02 (Fredag, 16:00-18:00)
- Introduksjon til nytt team-medlem Sassan
- Gjort ferdig hele libgdx tutorial og pushet til github
- Diskutert nærmere strukturen til logikken og hvordan
den samspiller med grafikken
- Deligering av roller
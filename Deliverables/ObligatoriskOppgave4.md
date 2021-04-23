# Obligatorisk oppgave 4

### Brukerhistorier:
1) Som spiller vil jeg kunne bruke conveyor belts til å flytte meg rundt på kartet,
   slik at spillet blir dypere strategisk og at det blir morsommere å spille med andre.

Akseptansekriterier:
- Gitt at en robot bruker et kort og ender opp på et conveyorbelt tile som peker i retning x, skal roboten
  bli flyttet ett tile i retning x.
- Gitt at en robot bruker et kort og ender opp på et blått conveyorbelt tile som peker i retning x, og at neste tile i retning x er et tilsvarende blått/gult conveyorbelt tile,
  skal roboten bli flyttet to tiles i retning x.
- Gitt at en robot bruker et kort og ender opp på et conveyorbelt tile som peker i retning x, og at neste tile i retning x er et conveyorbelt med en sving.
  Da skal roboten bli flyttet ett tile i retning x og rotere seg venstre/høyre tilsvarende til retningen av svingen.  
- Gitt at en robot bruker et kort og ender opp på et conveyorbelt til som peker i retning x, og at neste tile i retning x er okkupert av en robot som ikke står på et conveyorbelt,
  skal roboten på conveyor-beltet ikke bli beveget av conveyorbeltet.
- Gitt at en robot bruker et kort og ender opp på et conveyorbelt med en sving, skal roboten ikke rotere med mindre den er flyttet inn i en ny sving av conveyorbeltet.

Arbeidsoppgaver:
- Implementere gule conveyor belts.
- Implementere blå conveyor belts.

2) Som spiller vil jeg kunne ha tre liv, slik at robotten kan gjenopplives to ganger på sist checkpoint.

Akseptansekriterer:
- Om en robot dør og spilleren har ett eller flere liv igjen skal robotten gjenopplives neste runde.
- Robotten skal gjenopplives på det forrige checkpointet sitt (flagg,skiftenøkkel, etc.)

Arbeidsoppgaver:
- Implementere gjenoppliving.
- Implementere checkpoints.

3) Som en spiller som har vunnet vil jeg at de andre spillerene skal få melding om at jeg vant,
slik at vi kan begynne på et nytt spill eller avslutte helt.

Akseptansekriterier:
- Hvis en av spillerene har vunnet, skal de andre spillerene få en melding på skjermen sin at
spillet er over.
  
- Når spillet er over, skal ikke noen spillere kunne fortsette å bevege seg.

Arbeidsoppgaver:
- Implementere at hver spiller sjekker om noen har vunnet på starten av en tur.

4) Som spiller vil jeg at roboten min skal rotere i henhold til spillreglene når jeg går
på et tannhjul, slik at jeg kan bruke tannhjulene på brettet til å posisjonere roboten min
uten å bruke et programkort.

Akseptansekriterier:
- Hvis roboten står på et grønt tannhjul etter å ha brukt et programkort, skal roboten
rotere 90 grader til høyre.
  
- Hvis roboten står på et rødt tannhjul etter å ha brukt et programkort,
skal roboten rotere 90 grader til venstre.
  
Arbeidsoppgaver:
- Implementere kode som sjekker om man står på et tannhjul, og som roterer
roboten i riktig retning i henhold til fargen på tannhjulet.

5) Som en spiller ønsker jeg at roboten min skal skyte laser i retningen den står i slik at
jeg kan skade eller ødelegge de andre spillerenes roboter.
   
Akseptansekriterier:
- Gitt at en robot står på brettet, så skal en laser fyres av i retningen roboten står i.
Laseren skal oppføre seg lik lasere som skytes fra vegger.
  
- Gitt at det står to roboter, A og B, på brettet i posisjon (1,1) og (1,2) henholdsvis og robot
A er vendt mot nord (vendt mot robot B), så skal robot B bli skadet av laseren til robot A.

- Gitt at det står en robot på brettet og at det er en vegg mellom roboten og ruten foran roboten,
så skal ingen laser fyres av.

Arbeidsoppgaver:
- Implementere funksjon robotsShootsLasers() i BoardLogic, som skyter laser fra alle robotene på brettet.
- Implementere robotsShootsLasers() i doTurn() funksjonen i GameLogic, slik at robot laserene fyres av
  samtidig som laserene fra veggene.

6) Som spiller vil jeg kun bevege robotten min når jeg har fylt alle fem av mine registers.

Akseptansekriterier:
- Minst fem kort må bli valgt før spilleren kan avslutte sin tur.
- Ingen kort blir brukt hvis ikke alle registers er fylt.

Arbedisoppgaver:
- Implementere en cardCounter som sjekker antall kort i programRegister array-en (i GameScreen) som ikke er
  lik `null` (virkelige Card variabler).
- Implementere betingelser til Ready() knappen slik at knappen bare fungerer når antall valgte kort er lik 5.

7) Som spiller vil jeg at roboten min skal dø når den tar 10 eller flere damage tokens.

Akseptansekriterier:
- Robotten skal dø akkurat når den får >=10 DT.
- Robotten skal miste et Life Token og respawne (hvis den har >=1 LT) etter den dør.

Arbeidsoppgaver:
- Implementere en betingelse i addDamage metoden (i Robot) slik at et Life Token er trukket når damageTokens
  er >=10.
- Implementere en nested betingelse som setter alive som `false` når LT=0.

8) Som spiller vil jeg kunne stemme på kart i lobby, slik at alle er med på å bestemme kart, og gjør det mulig å bytte kart om spillerene spiller nytt spill.

Akseptansekriterier:
 - I lobby kan spillere grafisk stemme på kart.
 - Da spillet startes skal kartet med flest stemmer bli valgt. Om det er uavgjort velges et tilfeldig kart fra vinnerene.

Arbeidsoppgaver:
 - Implementere stemmingsystem.
 - Implementere valg av kart fra stemmer.

9) Som spiller vil jeg kunne spille på nytt om spillet konkluderer, slik at alle spillere og vert må starte programmet på nytt.

Akseptansekriterer:
 - I EndScreen kan spillere velge "New Game" for å stemme for nytt spill.
 - Om alle spillere har valgt "New Game" skal spillet bytte til lobby igjen.

Arbeidsoppgaver:
 - Implementere system for å resette spillet (fjerne alle spillere, etc.)

### Møtereferater:

29.03 (Mandag, 14:00-14:40)
- Sassan, Simon og Ørjan var tilstedes
- Diskuterte litt hva vi ville jobbe med i påsken
- Bestemte oss for at neste møtet skulle være tirsdag 6. april (p.g.a påske)

06.04 (Tirsdag, 12:00 - 12:30)
- Alle var tilstede
- Diskuterte hva som må gjøres i sprint 4
- Oppsummerte hvordan vi lå an etter påsken
- Sassan startet på testing av conveyor belts
- Ørjan startet på gjenoppliving av robotter
- Simon startet å jobbe med tannhjul

07.04 (Onsdag, 12:15 - 14:00)
- Alle var tilstede
- Asle la til rotasjon på conveyorbelts og fikset bugs
- Simon implementerte tannhjul og skrev tester for det
- Ørjan fikset resizing for setupscreen
- Sassan la til restock metode til deck-klassen

12.04 (Mandag 14:00 - 16:00)
- Alle var tilstedeGitt at en robot bruker et kort og ender opp på et conveyorbel
- Ørjan fikset et problem med git
- Asle merget ny conveyorbelt logikk
- Sassan testet restock() metoden og debugget spillet
- Simon startet å implementere at roboter skyter lasere
- Gikk litt gjennom generelle ting som må bli gjort i løpet av sprinten

14.04 (Onsdag 12:15 - 14:00)
- Alle var tilstede
- Asle la til pauser, fikset nullpointer i boardlogic via map.get(String), begynte å skrive tester conveyorbelts
- Sassan erstattet en gammel fix med restock() og la til
  robot destruction etter 10 DamageTokens
- Ørjan endret standardverdi for IP og navn til sist brukt, i stedet for "localhost" og "name"
- Ørjan jobbet på ny skjerm etter noen vinner, der man kan starte nytt spill
- Simon implementerte at roboter skyter lasere og skrev tester for det

16.04 (Fredag 12:00 - 14:30)
- Alle var tilstede
- Ørjan jobbet med å farge robotter ved runtime, så det kan være flere typer robotter uten mye ekstra arbeid
- Simon skrev robot laser tester
- Sassan la til en betingelse at spillere må velge fem kort før de kan fortsette til neste tur
  (ikke ferdig; buggy)
- Asle skrev flere tester til conveyorbelts

19.04 (Mandag 14:00 - 16:00)
- Alle var tilstede
- Simon fikset en bug der roboter ikke gikk fram etter å ha dyttet en annen robot
- Sassan fikset minimum antall kort betingelsen og
forsøkte å legge til en pop-up dialog til power-down
  metoden (klarte ikke det)
- Ørjan forbedret måten robottene blir farget, og begynte på UIen som viser damage tokens og life tokens.
- Asle fikset bug som gjorde at conveyorbelts forårsaket outOfBoundsException
  og gjorde at laserene fjernes før robotene beveger seg
  
21.04 (Onsdag 12:15 - 16:00)
- Alle var tilstede
- Ørjan implementerte stemmingsystem for kart.
- Vi hadde en code with me session der vi skrev om prosjektet sammen

22.04 (Torsdag 12:00 - 17:00)
- Alle var tilstede
- Vi hadde en ny code with me session der vi fullførte skrive-arbeidet fra dagen før
- Alle play-testet spillet sammen
- Jobbet på bugs som ble funnet i løpet av play-testingen.

23.04 (Fredag 12:00 - 14:30)
- Alle var tilstede
- Play-testet sammen

###Prosjekt og prosjektstruktur
- Roller:
  Vi står fast på rollebestemmelsene vi nevnte i forrige obligatoriske innlevering, der rollene 
  for det meste er blitt mer fleksible, mens vi fortsatt har spesialiserte roller som går mer 
  i dybden i en spesifikk del av programmet. 
  Rollene som går mer i dybden:
  GUI - Ørjan
  Brett-Logikk - Asle
  Brett-Logikk - Simon
  Spill-Logikk - Sassan
  

- Prosjektmetodikk:
  Vi har samme konklusjon som forrige gang:
  "Det er ingen nye erfaringer verdt å nevne med henhold til prosjektmetodikk.
  Vi synes at den blandingen vi har valgt av Scrum og Kanban har fungert bra
  hittil."
  Åpenheten til Kanban har gjort det lettere å fordele arbeidsoppgaver og å fremme kunnskapsoverføring, mens den
  rigide sprint-strukturen til Scrum har gått hånd i hånd med innleveringsfristene.

###Retrospektiv
Prosjektet har gått bra siden vi startet i januar. Gruppedynamikken og kommunikasjonen i teamet startet greit og har
bare blitt bedre etterhvert som vi har blitt bedre kjent og mer komfortable med å jobbe sammen. Som nevnt i forrige 
innlevering, har den gode kommunikasjonen og trivelige atmosfæren ført til et bra arbeidsmiljø der det er lett for 
alle å kunne bidra og å lære av hverandre.


Da prosjektet startet var prosjektmetodikken litt ubestemt, men vi ble enige mot slutten av første innlevering at
vi skulle bruke en kombinasjon av Scrum og Kanban. Scrum var naturlig bedre egnet til både første og 
andre innlevering, siden MVP-kravene ble konkrete mål for project-boardet i henhold til en scrum sprint.
De to siste innleveringene var derimot mer åpne for valg av gjøremål. Dette førte til at målene vi satt i starten av
sprintene ble utført før sprinten var over, og dermed ble det mer naturlig med en kombinasjon av Scrum og Kanban der
vi brukte Scrum-sprints, mens arbeidsoppgavene ble kontinuerlig oppdatert.

Hva hadde vi gjort annerledes:
- Investert mer tid til å lære om Kanban i begynnelsen.
- Kanskje implementert uPnP multiplayer istedenfor server-client, fordi da hadde vi sluppet port forwarding.
  Server-client systemet var også vanskelig å implementere i begynnelsen, men lett å bruke når man først fikk det til,
  så det hadde sannsynligvis vært lettere for oss i begynnelsen hvis vi implementerte en form for p2p.
- I større grad være mer proaktive med testing og skriving av brukerhistorier. Vi har til nå hatt en tilstrekkelig test-dekning,
  men i noen tilfeller har test-skriving vært mer en ettertanke.

###Krav
Hva vi har prioritert siden forrige gang:
- Conveyor belts (Ferdig implementert)
- Tannhjul (Ferdig implementert)
- Robot lasere (Ferdig implementert)
- Grafisk vise damage- og lifetokens (Ferdig implementert)
- Checkpoints (Ferdig implementert)
- Grafikk for ferdig spill (Nesten ferdig implementert)
- Respawning hvis man dør (Ferdig implementert)
- Fiksing av bugs som beskrevet i forrige innlevering

####Mål hvis vi skulle jobbet videre på prosjektet
- Option cards
- Flere maps
- Lage større utvalg av brikker å velge mellom
- Velge å respawne i power down, samt velge retningen man står ved respawn 

###Bugs
Bugs som er fikset siden forrige innlevering:
- Fikset hardcoded layers som førte til problemer i BoardLogic
- Fikset visuell desync
- Fikset kollisjon på conveyorbelts

Lavprioriterte bugs som fortsatt eksisterer:
- Logikk rundt conveyorbelts når 2 roboter konvergerer mot en rute
- Spillet blir låst hvis en spiller forlater spillet før det er fullført
- GLFW er ikke støttet på Apple Silicon, så grafikken kan kræsje på en Apple Silicon mac.

###Arbeidsfordeling
Arbeidsfordelingen har vært gjevn. Fikset en bug den 24.03 som gjorde
at Asle sine commits ikke ble vist på github. Alle code with me sesjonene blir hostet av Simon,
derfor vil nesten alle commitene komme fra Simon, selv om alle har deltatt.

###Teknisk informasjon
####Krav og kjøreinstruksjoner
Krav til software:
- Java 9 og opp
- Maven 3.6.3 og opp

Åpne command prompt (cmd) og skriv følgende kommandoer:
- cd *lokasjon til spill-filene*
- mvn clean install
- mvn exec:java

#### Hvordan manuelt teste programmet
Det ligger instruksjoner i ManualTest folderen.

#### Spillinstruksjoner
Når spillet er åpnet får man valget mellom "host" og "join". Trykk "host" for å kjøre en spillserver,
eller trykk "join" hvis du vil koble til en annen persons server.

Begge disse er veldig like i forhold til brukergrensesnitt. Det er to tekstfelt. Den første er IP-adressen, og den andre
er navnet på spilleren. I "host" er IPen alltid "localhost". I "join" skal man skrive inn IP-adressen til verten.
Navn kan være hva som helst, og blir brukt til å gjenkjenne deg.

Nå vil du komme til spill lobbyen. Hvor man venter på at alle skal få koble seg til serveren før man starter
spillet. Her kan du også se roboten du vil spille som og velge farge på den ved å flytte på de tre glidebryterene, som ligger rett
under robotfigurene. I lobbyen skal spillerene også stemme på kartet de har lyst til å spille. Når alle spillerene har trykket på "ready", så
starter spillet på kartet med flest stemmer. Hvis to eller flere kart har like mange stemmer, så blir ett av dem valgt tilfeldig. 

Når spillet har startet får du muligheten til å programmere roboten din med kortene du får tildelt.
På høyresiden av skjermen kan du se alle robotene som er med, samt hvor mange life- og damagetokens de har.
Trykk på "edit" nede på skjermen for å se kortene dine. Programmering fungerer som et drag and drop system.
Når du har programmert roboten din, trykk på "ready". Når alle i spillet har trykket ready, så vil spillet oppdateres
og neste runde vil starte.

Regler for spillet:
- Du må gå innom alle flaggene i riktig rekkefølge.
- Flagg og skiftenøkler virker som checkpoints, så hvis roboten din dør blir du gjenopplivet på siste checkpoint.
- Samlebåndene flytter på roboten din etter hvert kort er spilt. Gule flytter deg 1 rute, mens blå flytter deg 2 ruter.
- Tannhjul roterer deg 90 grader etter hvert kort som blir spilt. Grønne tannhjul roterer mot høyre, mens røde roterer mot venstre.
- Tannhjul og samlebånd aktiveres etter hvert kort blir spilt.
- Du kan ikke gå gjennom vegger.
- Lasere blir skutt fra veggene med laserskytere og ut fra fronten på robotene.
- Laserskudd blir registrert etter samlebånd og tannhjul aktiveres.
- Hvis roboten din blir truffet av en laser, så mottar roboten en damage token.
- Hvis du går utenfor kartet eller over et hull, så dør roboten din.
- Hvis roboten din får 10 damage tokens, så dør den.
- Hvis roboten din går tom for life tokens (dør 2 ganger), kan du ikke gjenopplives lenger.

Første spiller som er innom alle flaggene i riktig rekkefølge vinner!
Hvis roboten din dør 3 ganger er du derimot ute av spillet.

Når spillet er konkludert kan spillerene spille på nytt om alle aktive spillere
trykker på på "New Game".

For å avslutte spillet, lukk vinduet ved å trykke på krysset oppe til høyre. (Server og klient blir automatisk lukket om Gdx-applikasjonen lukkes.)
Om en spiller forlater skal spillet fortsatt fortsette for alle andre (Med mindre verten forlater).

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

2) Som spiller vil jeg kunne ha tre liv, slik at robotten kan gjenopplives to ganger.

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

6) Brukerhistorie til Sassan *** (5 kort betingelse + robot dør etter 10 DT)

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

###Prosjekt og prosjektstruktur

####Retrospektiv

###Krav

###Bugs

###Arbeidsfordeling

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

Hvis du trykket "host" vil du møte en skjerm hvor du kan velge kart ved å trykke på de ulike kartene på listen.
Et bilde av kartet vil henholdsvis bli vist på høyresiden. Nede til venstre får du mulighet til å skrive inn spill-taggen din.
Deretter trykk "play" nede i høyre hjørne for å starte serveren.

Hvis du trykket "join" blir du sendt til en skjerm som lar deg skrive inn en ip-adresse til en annen server i feltet det står "localhost"
og spill-taggen din i feltet det står "name". Deretter trykk "play" knappen nede i venstre hjørne for å koble til serveren.

Nå vil du komme til spill lobbyen. Hvor man venter på at alle skal få koble seg til serveren før man starter
spillet. Her kan du også se roboten du vil spille som. Hosten kan da trykke "play" nede i venstre hjørne for å starte spillet.

Når spillet har startet får du muligheten til å programmere roboten din med kortene du får tildelt.
Trykk på "edit" nede på skjermen for å se kortene dine. Programmering fungerer som et drag and drop system.
Når du har programmert roboten din, trykk på "ready". Når alle i spillet har trykket ready, så vil spillet oppdateres
og neste runde vil starte.

Første spiller som er innom alle flaggene i riktig rekkefølge vinner!

For å lukke spillet, lukk vinduet ved å trykke på krysset oppe til høyre. (Server og klient blir automatisk lukket om Gdx-applikasjonen lukkes.)
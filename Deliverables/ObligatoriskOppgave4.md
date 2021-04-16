# Obligatorisk oppgave 4

### Brukerhistorier:
1) Som spiller vil jeg kunne bruke conveyor belts til å flytte meg rundt på kartet,
   slik at spillet blir dypere strategisk og at det blir morsommere å spille med andre.

Akseptansekriterier:
- Gitt at en robot står på et gult conveyorbelt tile som peker i retning x, skal roboten
  bli flyttet ett tile i retning x etter roboten har brukt et kort.
- Gitt at en robot står på et blått conveyorbelt tile som peker i retning x og at neste tile i retning x er et tilsvarende blått conveyorbelt tile, skal roboten
  bli flyttet to tiles i retning x etter at roboten har brukt et kort.

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

### Møtereferater:

29.03 (Mandag, 14:00-14:40)
- Sassan, Simon og Ørjan var tilstedes.
- Diskuterte litt hva vi ville jobbe med i påsken.
- Bestemte oss for at neste møtet skulle være tirsdag 6. april (p.g.a påske).

06.04 (Tirsdag, 12:00 - 12:30)
- Alle var tilstede
- Diskuterte hva som må gjøres i sprint 4
- Oppsummerte hvordan vi lå an etter påsken
- Sassan startet på testing av conveyor belts
- Ørjan startet på gjenoppliving av robotter.
- Simon startet å jobbe med tannhjul

07.04 (Onsdag, 12:15 - 14:00)
- Alle var tilstede
- Simon implementerte tannhjul og skrev tester for det

12.04 (Mandag 14:00 - 16:00)
- Alle var tilstede (?)
- Sassan testet restock() metoden og debugget spillet
- Simon startet å implementere at roboter skyter lasere

14.04 (Onsdag 12:15 - 14:00)
- Alle var tilstede
- Sassan erstattet en gammel fix med restock() og la til
  robot destruction etter 10 DamageTokens
- Ørjan endret standardverdi for IP og navn til sist brukt, i stedet for "localhost" og "name".
- Ørjan jobbet på ny skjerm etter noen vinner, der man kan starte nytt spill.
- Simon implementerte at roboter skyter lasere og skrev tester for det
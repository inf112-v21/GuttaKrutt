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


### Møtereferater:

29.03 (Mandag, 14:00-14:40)
- Sassan, Simon og Ørjan var tilstedes.
- Diskuterte lett hva vi ville jobbe med i påsken.
- Bestemte oss for at neste møtet skulle være tirsdag 6. april (p.g.a påske).

06.04 (Tirsdag, 12:00 - 12:30)
- Alle var tilstede
- Diskuterte hva som må gjøres i sprint 4
- Oppsummerte hvordan vi lå an etter påsken
- Sassan startet på testing av conveyor belts
- Ørjan startet på gjenoppliving av robotter.

07.04 (Onsdag, 12:15 - 14:00)
- a
- b

12.04 (Mandag 14:00 - 16:00)
- a
- Sassan testet restock() metoden og debugget spillet
- 

14.04 (Onsdag 12:15 - 14:00)
- Alle var tilstede
- Sassan erstattet en gammel fix med restock() og la til
  robot destruction etter 10 DamageTokens
- Ørjan endret standardverdi for IP og navn til sist brukt, i stedet for "localhost" og "name".
- Ørjan jobbet på ny skjerm etter noen vinner, der man kan starte nytt spill.
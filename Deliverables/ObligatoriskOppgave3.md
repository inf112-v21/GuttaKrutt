#Obligatorisk oppgave 3

###Brukerhistorier:
1) turn system

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

###Retrospektiv

###Teknisk informasjon
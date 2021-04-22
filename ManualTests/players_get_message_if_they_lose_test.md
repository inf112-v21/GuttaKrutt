##Players get a message if the lose test

Start game by following technical documentation, connect with two players.
Choose the map "TiledTestWithOneFlag".
Then one player moves to the flag, while the other player moves somewhere else
(do not die or walk on the flag).

####Fail
- Any of the players are able to move after the player on the flag won (rotating is allowed).
- The player that lost did not get any notification about losing.

####Success
- If the player that lost got the message "You lost, loser!" in their console,
  then the test was successful

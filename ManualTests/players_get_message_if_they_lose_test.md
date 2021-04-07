##Players get a message if the lose test

Start game by following technical documentation, connect with two players.
Choose the map "TiledTestWithOneFlag".
Then one player moves to the flag, while the other player stands still
(do one rotation or power down).

####Fail
- Any of the players are able to move after the player on the flag won (rotating is allowed).
- The player that lost did not get any notification about losing.

####Success
- If the player that lost got the message "You lost, loser!" in their console,
  then the test was successful

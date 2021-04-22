## Using a power-down token test

Start game by following the technical documentation, playing either in the
single-player or multiplayer mode. Play a round by clicking on "Edit" and
choosing your registers and press "Power Down" **before** pressing "Ready"
(thereby finishing your round). Start the new round by clicking on "Edit"
again.

Pay attention that a requirement for the power-down method to function as
intended is for the robot to be damaged (as stated in the rules). You can
either change this requirement in the powerDown() function in GameLogic (by changing it to
`if (robot.getDamage() == 0) {`), or damage the robot by running into a
laser in order to test this functionality.

#### Success
- if the menu where you normally choose your registers contains no cards,
  then you have successfully used the power-down token.

#### Fail
- if the menu contains new cards to choose from, enabling you to play this
  round with no limitations.
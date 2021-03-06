## Controls
- Use W or UP to go forward
- Use S or DOWN to go back
- Turn right with D or RIGHT, and left with A or LEFT

- Reset the player with R


## Starting the game
To start the game, first run GameServer.main(). Then start Main.main().

### Testing movement
1. Press W or UP.
    - The player should have moved up one tile.
    
2. Press S or DOWN.
    - The player should have moved back to its starting point.
    
3. Press D or RIGHT, and then W or UP.
    - The player should have turned to the right and moved to the right.
    
### Testing wall collision inside tile
1. Move to (1,1). Possible Route: W, D, W.
2. There should be a wall at the top and to the right in the tile.
3. Try to move past them.
4. The robot should not move.

### Testing wall collision in next tile
1. Move to (3,0). Possible route: D, W, W, W.
2. There should be a wall in the next tile over the robot.
3. Try to move through the wall.
4. The robot should not move.

### Testing death on hole
1. Move to (2,2). Possible route: D,W,W,A,W,W.
2. The player should now be over a hole.
3. The player should now have changed texture into an owl with crossed out red eyes.

### Testing win on flag
1. Move to (0,4). Possible route: W,W,W,W.
2. The player should now stand over a flag.
3. The player should now have blue stars shooting out of it in the texture.
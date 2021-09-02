# Pac-Man

<p align="center">
    <img src='images/Game%20Screenshot.png' alt='Screenshot' width='300' height='300'>
</p>

A simple Pac-Man game built on Java. Players play as Pac-Man and the goal is to eat all dots on the board. There are also four ghosts (Blinky, Pinky, Inky and Clyde) which will move randomly and are able to eat Pac-Man. If one of the ghosts eats Pac-Man before Pac-Man successfully eat all dots, players lose the game. If Pac-Man successfully eat all dots without being eaten by one of the ghosts, players win the game.

There are also several big dots. If Pac-Man eats one of those big dot, all ghosts will not be able to eat Pac-Man and vice versa, Pac-Man will be able to eat the ghosts. This condition only lasts for five seconds.

Pac-Man can move left, right, up and down inside a maze by using arrow keys. Pac-Man cannot pass through the wall. In addition, players can customize the maze by editing `maze.txt` in `resources`. In `maze.txt`, `x` means empty space which Pac-Man can pass through and `@` means wall which Pac-Man cannot pass through. Size of the maze is currently fixed (11x11). You can edit the size manually in `Tester.java`.

## Lincense

Distributed under the MIT Lincese. See `LICENSE` for more information.
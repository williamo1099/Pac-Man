# Pac-Man

A simple [Pac-Man](https://en.wikipedia.org/wiki/Pac-Man) desktop game built in Java. Players play as Pac-Man and the main objective of the game is to eat all dots on the board.

<p align="center">
    <img alt="Game Demo" width="300" height="300" src="images/Game%20Demo.gif">
</p>

## Gameplay

Gameplay of this game quite resembles gameplay of the original Pac-Man. As mentioned above, players play as Pac-Man and the main objective of this game is to eat all the dots on the board. To accomplish the objective, Pac-Man can move left, right, up and down inside the maze (by using arrow keys). While eating all dots, Pac-Man must avoid four ghosts (Blinky, Pinky, Inky and Clyde) which will move randomly. If a ghost eats Pac-Man, players lose the game.

On the board, Pac-Man can eat dots or big dots. If Pac-Man eats a dot, players will gain 100 score. If Pac-Man eats a big dot, players will gain 200 score. In addition, all ghosts on the board will turn blue and become eatable by Pac-Man for 5 seconds. If Pac-Man eats a blue ghost, players will gain 500 score.

## How to Run

1. Install [NetBeans IDE](https://www.oracle.com/tools/technologies/netbeans-ide.html).
2. Clone the repository.
3. Open the repository as a project in NetBeans IDE.
4. Run `Tester.java`.
5. Input the `n` (board size) and `tile length`.

Players can customize the maze by editing `maze.txt` in `resources`. In `maze.txt`, `x` means empty space which Pac-Man can pass through and `@` means wall which Pac-Man cannot pass through. Make sure that size of the maze in `maze.txt` matches the input `n`.

## Lincense

Distributed under the MIT Lincese. See `LICENSE` for more information.
package Logic.Objects;

import java.util.ArrayList;
import java.util.Random;

public class GameBoard {
    private Stellarsystem[][] board = new Stellarsystem[12][12];
    private Random randomCalls = new Random();

    public void initBoard(Nation nation) {

        for (int i = 0; i < 12; i++) {
            for (int j = 11; j >= 0; j--) {
                board[i][j] = new Stellarsystem(randomCalls.nextInt(104) + 154 * i + 39, randomCalls.nextInt(34) + 84 * j + 39, randomCalls.nextInt(4));
                // System.out.println(i+" "+j+" "+"x: "+getBoardTile(i,j).getPixelX()+"y: "+getBoardTile(i,j).getPixelY());
            }
        }
        int rX = randomCalls.nextInt(12);
                int rY = randomCalls.nextInt(11);
        board[rX][rY].setEnemyPower(0);
        board[rX][rY].setEnemy(false);
        board[rX][rY].setNationality(nation.getName());

    }

    public Stellarsystem getBoardTile(int x, int y) {
        if (x < board.length && y < board[x].length) {
            return board[x][y];
        }
        return null;
    }
}

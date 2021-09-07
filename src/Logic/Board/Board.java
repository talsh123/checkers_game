/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic.Board;

import GUI.GamePanel;
import Players.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

/**
 *
 * @author Administrator
 */
public class Board {

    static GamePanel panel; // Panel the game is played on
    Image img; // Background Image
    public static int N = 8; // Number Of rows & Cols in the Panel 8 x 8 Board
    public static Player[] players = new Player[2]; //Array of checkers pieces (Player 1, Player 2(Computer, in certain cases))
    Integer choosePiece = null; //key of the chosen piece

    public Board(GamePanel gamePanel) { //Gets a gamePanel and creates a new Board
        Board.panel = gamePanel;
        img = Toolkit.getDefaultToolkit().getImage("Images\\Board1.png");

        players[0] = new Computer(this, 0, 2);
        players[1] = new Player(this, N - 3, N - 1);
        this.choosePiece = null;
    }

    public void Draw(Graphics graphics) { //Draws the checker pieces
        Graphics2D graphics2d = (Graphics2D) graphics;

        graphics2d.drawImage(img, 0, 0, 722, 722, panel); // draws the checker pieces
        players[0].Draw(graphics2d); //draws black checker pieces
        players[1].Draw(graphics2d);

    }

    public void Click(Point point) { // Gets a Point on the Board and performes a Click event
        int col = (int) point.getX();
        int row = (int) point.getY();
        row = this.getDimensions(row);//Calcualtes the row of the chosen piece
        col = this.getDimensions(col);//Calcualtes the col of the chosen piece
        this.choosePiece = (row * 8) + (col); //calulated the position on the Board for the Piece
        players[1].Click(choosePiece); //Click in Players
    }

    public int getDimensions(int dimension) { //Gets a dimenstion and returns the col/row in the Board
        if (dimension >= 0 && dimension <= 90) { // 0
            dimension = 0;
        }
        if (dimension > 90 && dimension <= 180) { // 1
            dimension = 1;
        }
        if (dimension > 180 && dimension <= 270) { // 2
            dimension = 2;
        }
        if (dimension > 270 && dimension <= 360) { // 3
            dimension = 3;
        }
        if (dimension > 360 && dimension <= 450) { // 4
            dimension = 4;
        }
        if (dimension > 450 && dimension <= 540) { // 5
            dimension = 5;
        }
        if (dimension > 540 && dimension <= 630) { // 6
            dimension = 6;
        }
        if (dimension > 630 && dimension <= 720) { // 7
            dimension = 7;
        }
        return dimension;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic.Board;

import Players.Move;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList; 
    
/**
 *
 * @author Administrator
 */
public class Piece {

    int row, col; //row and col of the Piece
    boolean black; //is Black Piece? true - yes, else false
    Piece lastPiece; // Last Piece chosen is a series of jumps

    public static int PieceSize = 90; //Size each Piece
    static Image imgBlack = Toolkit.getDefaultToolkit().getImage("Images\\BlackTemp.png"); //Image of Black Piece
    static Image imgWhite = Toolkit.getDefaultToolkit().getImage("Images\\White.png"); //Image of White Piece
    static Image imgWhiteKing = Toolkit.getDefaultToolkit().getImage("Images\\WhiteKing.png");
    static Image imgBlackKing = Toolkit.getDefaultToolkit().getImage("Images\\BlackKing.png");
    static int[][] Dir = { // DirectionMatrix for Move
        {1, -1}, // DOWNLEFT
        {1, 1}, // DOWNRIGHT
        {-1, -1}, // UPLEFT
        {-1, 1} // UPRIGHT
    };
    static int[][] DirEat = { //Direction Matrix for Eat
        {2, -2}, // DOWNLEFT
        {2, 2}, // DOWNRIGHT
        {-2, -2}, // UPLEFT
        {-2, 2} // UPRIGHT
    };
    public static int turn = 1; // if 1 -  White turn, else Black turn
    boolean isKing = false;

    public Piece(int row, int col, boolean black, boolean isKing) { //Gets a row,col and a bolean value and creates a new Piece
        this.row = row;
        this.col = col;
        this.black = black;
        this.isKing = isKing;
    }

    public void Draw(Graphics2D graphics) { //Draws a new Piece
        Image img;
        if (this.black == true) {
            img = (this.isKing == true) ? imgBlackKing : imgBlack;
        } else {
            img = (this.isKing == true) ? imgWhiteKing : imgWhite;
        }

        graphics.drawImage(img, col * PieceSize, row * PieceSize, PieceSize, PieceSize, Board.panel); //draws a checker piece

    }

    public ArrayList<Move> GetPossibleMoves(Board board) {
        ArrayList<Move> moves = new ArrayList<>();

        int index = this.black ? 0 : 2;
        for (int i = index; i < index + 2; i++) { //chooses the currect index according to the Piece (if black or white)
            int rowne = row + Dir[i][0]; // row of the place we check
            int colne = col + Dir[i][1]; // col of the place we check
            Integer keyNe = rowne * Board.N + colne; // calculation of the key of the place we check
            if (IsLegal(rowne, colne) && IsEmpty(keyNe)) {
                moves.add(new Move(this, keyNe));
            }
        }
        return moves;
    }

    public void GetPossibleMovesEat(Board board, int row, int col, ArrayList<Move> moves) {
        //ArrayList<Move> moves = new ArrayList<>();
        // currow - row of the chose Piece of the move
        // curcol - col of the chose Piece of the move
        // rowne - row of the diagnolly place to move
        // colne - col of the diagnolly place to move
        // keyNe - Place in Board of the diagnoll place to move
        int currow = row;
        int curcol = col;
        int index = this.black ? 0 : 2; //chooses the currect index according to the Piece (if black or white)
        //int tempIndex = index;
        for (int i = index; i < index + 2; i++) {
            int rowne = currow + DirEat[i][0]; // row of the place we check
            int colne = curcol + DirEat[i][1]; // col of the place we check
            Integer keyNe = rowne * Board.N + colne; // calculation of the key of the place we check
            while (IsLegal(rowne, colne) && IsEmpty(keyNe)) //if the move is Legal and the place is Empty
            {
                if (IsEmpty(CalculatePlaceInBoard(currow + Dir[i][0], curcol + Dir[i][1])) == false) //calculated the place before the 
                //place we jump to if there is a Piece there
                {
                    //for (int i1 = tempIndex; i1 < tempIndex + 2; i1++) {
                    turn = (this.black == true) ? 0 : 1;
                    int BetKey = CalculatePlaceInBoard(currow + Dir[i][0], curcol + Dir[i][1]); // betKey represents the key value of the place between the place we go to
                    if (Board.players[turn & 1].pieces.get(BetKey) == null && IsEmpty(BetKey) == false) { // There is no Checker Piece of my Own Team is the place between
                        moves.add(new Move(new Piece(currow, curcol, turn == 0, isKing), keyNe));
                        lastPiece = Board.players[turn & 1].pieces.get(currow * 8 + curcol);
                        // At this point we need to check again if we can double jump or more
                        currow = keyNe / 8; // Updates the current row
                        curcol = keyNe % 8; // Updates the current row
                        GetPossibleMovesEat(board, currow, curcol, moves);
                    }
                    //  }
                    break;
                } else { // else no need to check anymore because this is not a legal move
                    break;
                }
            }
        }
    }

    public ArrayList<Move> GetPossibleMovesKing(Board board) {
        ArrayList<Move> moves = new ArrayList<>();

        for (int i = 0; i < 4; i++) { //chooses the currect index according to the Piece (if black or white) or King - In this case we need to check all 4 corners
            int rowne = row + Dir[i][0]; // row of the place we check
            int colne = col + Dir[i][1]; // col of the place we check
            Integer keyNe = rowne * Board.N + colne; // calculation of the key of the place we check
            if (IsLegal(rowne, colne) && IsEmpty(keyNe) && this.isKing == true) {
                moves.add(new Move(this, keyNe));
            }
        }
        return moves;
    }

    /*public ArrayList<Move> GetPossibleMovesEatKing(Board board) {
        ArrayList<Move> moves = new ArrayList<>();
        turn = this.black ? 0 : 1;
        for (int i = 0; i < 4; i++) {
            int rowne = row + DirEat[i][0]; // row of the place we check
            int colne = col + DirEat[i][1]; // col of the place we check
            Integer keyNe = rowne * Board.N + colne; // calculation of the key of the place we check
            if (IsLegal(rowne, colne) && IsEmpty(keyNe)) //if the move is Legal and the place is Empty
            {
                if (IsEmpty(CalculatePlaceInBoard(row + Dir[i][0], col + Dir[i][1])) == false) //calculated the place before the 
                //place we jump to if there is a Piece there
                {
                    int BetKey = CalculatePlaceInBoard(row + Dir[i][0], col + Dir[i][1]); // betKey represents the key calue of the place between the place we go to
                    if (Board.players[turn].pieces.get(BetKey) == null && IsEmpty(BetKey) == false) {
                        moves.add(new Move(this, keyNe));
                    }
                }
            }
        }
        return moves;
    }*/
    public void GetPossibleMovesEatKing(Board board, int row, int col, ArrayList<Move> moves) {
        //ArrayList<Move> moves = new ArrayList<>();
        // currow - row of the chose Piece of the move
        // curcol - col of the chose Piece of the move
        // rowne - row of the diagnolly place to move
        // colne - col of the diagnolly place to move
        // keyNe - Place in Board of the diagnoll place to move
        int currow = row;
        int curcol = col;
        for (int i = 0; i < 4; i++) { // Runs on all 4 corners as we are a King
            int rowne = currow + DirEat[i][0]; // row of the place we check
            int colne = curcol + DirEat[i][1]; // col of the place we check
            Integer keyNe = rowne * Board.N + colne; // calculation of the key of the place we check
            while (IsLegal(rowne, colne) && IsEmpty(keyNe) && this.isKing == true) //if the move is Legal and the place is Empty
            {
                if (IsEmpty(CalculatePlaceInBoard(currow + Dir[i][0], curcol + Dir[i][1])) == false) //calculated the place before the 
                //place we jump to if there is a Piece there
                {
                    turn = (this.black == true) ? 0 : 1;
                    int BetKey = CalculatePlaceInBoard(currow + Dir[i][0], curcol + Dir[i][1]); // betKey represents the key value of the place between the place we go to
                    if (Board.players[turn & 1].pieces.get(BetKey) == null && IsEmpty(BetKey) == false) { // There is no Checker Piece of my Own Team is the place between
                        moves.add(new Move(new Piece(currow, curcol, turn == 0, isKing), keyNe));
                        lastPiece = Board.players[turn & 1].pieces.get(currow * 8 + curcol);
                        // At this point we need to check again if we can double jump or more
                        currow = keyNe / 8; // Updates the current row
                        curcol = keyNe % 8; // Updates the current row
                        GetPossibleMovesEat(board, currow, curcol, moves);
                    }
                    break;
                } else { // else no need to check anymore because this is not a legal move
                    break;
                }
            }
        }
    }

    public int CalculatePlaceInBoard() { //Calculated the place in Board for the this Piece
        return this.getCol() + this.getRow() * 8;
    }

    public int CalculatePlaceInBoard(int row, int col) { //Calculated the place in Board for a Piece
        return (row * 8 + col);
    }

    public int getRow() { // Returns the Row
        return this.row;
    }

    public int getCol() { // Returns the Col
        return this.col;
    }

    public void setRow(int row) { // Sets the Row
        this.row = row;
    }

    public void setCol(int col) { //Sets the Col
        this.col = col;
    }

    public boolean getBlack() { // Returns true if the Piece is Black, else False
        return this.black;
    }

    public void setKing(boolean king) {
        this.isKing = king;
    }

    public boolean isKing() {
        return this.isKing;
    }

    public int getTurn() {
        return Piece.turn;
    }

    private boolean IsEmpty(Integer keyNe) { // Gets a key in a board and a board and returns true if that place if true, else is false 
        return Board.players[0].pieces.containsKey(keyNe) == false // Checks if a black Piece is there
                && Board.players[1].pieces.containsKey(keyNe) == false; // Checks if a white Piece is there
    }

    private boolean IsLegal(int rowne, int colne) { // Checks if the move is Legal
        return rowne >= 0 && rowne < Board.N && colne >= 0 && colne < Board.N;
    }

}

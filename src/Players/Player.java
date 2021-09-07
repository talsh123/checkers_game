/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Players;

import Logic.Board.Board;
import static Logic.Board.Board.players;
import Logic.Board.Piece;
import java.awt.Color;
import java.awt.Graphics2D;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author Administrator
 */
public class Player {

    public boolean colurPlaces = true; //true if lehitza rishona, false if lehitza shnia
    public HashMap<Integer, Piece> pieces = new HashMap<>(); //Key - integer , Value - Piece
    ArrayList<Move> piecePossibleMoves = new ArrayList<>(); // ArrayList of Possible Moves
    ArrayList<Move> piecePossibleMovesEat = new ArrayList<>(); //ArrayList of Possible Eat Moves
    Piece lastPiece; // A Piece representing a the last Piece the user clicked on.
    Board board; // The Board we play on.
    Stack<Move> tempStack = new Stack<>(); // Temporary stack being used in order to trace multiples jumps while eating and delete the right pieces in order

    public Player(Board board, int startR, int endR) { // Sets the Players on the Board with the According keys in the HashMap
        this.board = board;
        for (int i = startR; i <= endR; i++) {
            for (int j = 0; j < Board.N; j++) {
                if (((i + j) & 1) == 0) {
                    pieces.put(i * Board.N + (j), new Piece(i, j, (startR == 0), false));
                }
            }
        }
    }

    public void Draw(Graphics2D graphics) { // Draws the Pieces onto the Board for each Players
        // along with the possible moves 
        for (int i = 0; i < 64; i++) {
            if (pieces.containsKey(i) == true) {
                pieces.get(i).Draw(graphics);
            }
        }

        //pieces.values().forEach((Piece) -> {
        //    Piece.Draw(graphics);
        //});
        for (int i = 0; i < this.piecePossibleMoves.size(); i++) { // Draws the Places where you can move
            Move move = this.piecePossibleMoves.get(i);
            graphics.setColor(Color.GREEN);
            graphics.fillRect(move.destination % Board.N * Piece.PieceSize, move.destination / Board.N * Piece.PieceSize,
                    Piece.PieceSize, Piece.PieceSize);
        }
        for (int i = 0; i < this.piecePossibleMovesEat.size(); i++) { // Draws the Places where you can eat
            Move move = this.piecePossibleMovesEat.get(i);
            graphics.setColor(Color.GREEN);
            graphics.fillRect(move.destination % Board.N * Piece.PieceSize, move.destination / Board.N * Piece.PieceSize,
                    Piece.PieceSize, Piece.PieceSize);
        }
    }

    public void Click(Integer choosePiece) { //choosePiece - current location of piece(key)
        if (isWhitePiece(choosePiece) == true) {
            this.colurPlaces = true;
        } else {
            if (isEmpty(choosePiece) == true && isExistInOneOfMoves(choosePiece) == true) {
                this.colurPlaces = false;
            } else {
                return;
            }
        }
        if (this.colurPlaces == true) { // Checks if I am choosing a piece to move
            piecePossibleMovesEat.clear();
            piecePossibleMoves.clear();
            lastPiece = pieces.get(choosePiece); // Stored the piece I chose to be used later
            if (this.pieces.get(choosePiece).getRow() == 0) { // Checks if I moves the Piece into the end of the board - thus turning it into a King
                this.pieces.get(choosePiece).setKing(true); // Sets the isKing boolean attribute as true
            }
            if (pieces.get(choosePiece).isKing() == true) { // If the Piece is King
                pieces.get(choosePiece).GetPossibleMovesEatKing(board, pieces.get(choosePiece).getRow(), pieces.get(choosePiece).getCol(), this.piecePossibleMovesEat); // Creates an array of possible moves to eat for King
                piecePossibleMoves = pieces.get(choosePiece).GetPossibleMovesKing(board); // Creates an array of possible moves for King
            } else {
                pieces.get(choosePiece).GetPossibleMovesEat(board, lastPiece.getRow(), lastPiece.getCol(), piecePossibleMovesEat); // Creates an array of possible moves to eat
                piecePossibleMoves = pieces.get(choosePiece).GetPossibleMoves(board); // Creates an array of possible moves
            }
        } else { // I am currently Moving/ Eating
            if (isExistInOneOfMoves(choosePiece) == true) {
                pieces.put(choosePiece, new Piece(choosePiece / Board.N, choosePiece % Board.N, lastPiece.getBlack(), lastPiece.isKing())); // Putting a key in the HashMap and placing a Piece
                if (piecePossibleMovesEat.isEmpty() == false) { // If the array of Eat moves is not empty
                    Move temp = FindDestinationInArray(piecePossibleMovesEat, choosePiece); // temp points to the Move object in the middle of the 2 jumps
                    while (temp.getPiece().CalculatePlaceInBoard() != lastPiece.CalculatePlaceInBoard()) { // circulates backwards from the end point to the start point and inserts every move from end to start in the Stack
                        tempStack.push(temp); // Inserts into the Stack
                        temp = FindDestinationInArray(piecePossibleMovesEat, temp.getPiece().CalculatePlaceInBoard()); // Updates temp to the next Move
                    }
                    tempStack.push(temp); // Pushes last (which is the first move in the User's eyes) Move
                    while (tempStack.isEmpty() == false) { // Empties the stack and while doing so uses the Moves in order to find the Piece between 2 Moves and deletes it
                        players[0].pieces.remove(FindMiddlePiece(tempStack.peek().getPiece().CalculatePlaceInBoard(), tempStack.peek().destination));
                        tempStack.pop(); // Removes from the Stack
                    }
                }
                pieces.remove(lastPiece.CalculatePlaceInBoard()); // removes the last piece from the old spot on the board
                (((Computer) players[0])).DoMove(); // the AI moves
                piecePossibleMoves.clear(); // clears the Array
                piecePossibleMovesEat.clear(); // clears the Array
                tempStack.clear(); // Clears the temporary Stack
                this.colurPlaces = true;
            }
            this.colurPlaces = true;
        }
    }

    ArrayList<Move> GetPossibleMoveEat() { // Returns the AI an array filled with moves to eat
        pieces.values().forEach((piece) -> {
            piece.GetPossibleMovesEat(board, piece.getRow(), piece.getCol(), this.piecePossibleMovesEat);
        });
        return this.piecePossibleMovesEat;
    }

    ArrayList<Move> GetPossibleMove() { // Returns the AI an array filled with moves
        ArrayList<Move> moves = new ArrayList<>();
        pieces.values().forEach((piece) -> {
            moves.addAll(piece.GetPossibleMoves(board));
        });
        return moves;
    }

    ArrayList<Move> GetPossibleMoveEatKing() { // Returns the AI an array filled with moves to eat
        pieces.values().forEach((piece) -> {
            piece.GetPossibleMovesEatKing(board, piece.getRow(), piece.getCol(), this.piecePossibleMovesEat);
        });
        return this.piecePossibleMovesEat;
    }

    ArrayList<Move> GetPossibleMoveKing() { // Returns the AI an array filled with moves to eat
        ArrayList<Move> moves = new ArrayList<>();
        pieces.values().forEach((piece) -> {
            moves.addAll(piece.GetPossibleMovesKing(board));
        });
        return moves;
    }

    public int FindMiddlePiece(int start, int end) //Gets a start point ,end point and a boolean value representing if it is black or not
    //and finds the middle piece resebling in eating 
    //action and return the place in the array when the piece 
    //piece was eaten
    {

        if (start < end) // if the checker Piece that is eating above the eaten Piece in the Board 
        {
            if (start % 8 < end % 8) // If eat diagnoally down right
            {
                return start + (abs(start - end) / 2);
            } else {
                return start + (abs(start - end) / 2); // If eat diagnoally down left
            }
        } else { // if the checker Piece that is eating above the eaten Piece in the Board 
            if (start % 8 > end % 8) // If eat diagnoally up left
            {
                return start - (abs(start - end) / 2);
            } else {
                return start - (abs(start - end) / 2); // If eat diagnoally up right
            }
        }
    }

    public boolean isWhitePiece(int choosePiece) { // Checks if the given position on the board has a White Piece
        return players[1].pieces.get(choosePiece) != null;
    }

    public boolean isEmpty(int choosePiece) { // // Checks if the given position on the board has no Pieces
        return players[0].pieces.get(choosePiece) == null && players[1].pieces.get(choosePiece) == null;
    }

    public boolean isExistInOneOfMoves(int choosePiece) { // Checks if the given position on the board exists as a move
        for (int i = 0; i < this.piecePossibleMoves.size(); i++) {
            if (this.piecePossibleMoves.get(i).destination == choosePiece) {
                return true;
            }
        }
        for (int i = 0; i < this.piecePossibleMovesEat.size(); i++) {
            if (this.piecePossibleMovesEat.get(i).destination == choosePiece) {
                return true;
            }
        }
        return false;
    }

    public Move FindPieceInArray(ArrayList<Move> a, Piece piece) { // Gets a ArrayList of type Move and a piece and returns a Move containing that Piece
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).getPiece().CalculatePlaceInBoard() == piece.CalculatePlaceInBoard()) { // Searches in the Array for a Move item containing the given Piece
                return a.get(i);
            }
        }
        return null;
    }

    public Move FindPlaceInArray(ArrayList<Move> a, int placeInBoard) { // Gets a ArrayList of type Move and a place in board and returns a Move containing the Piece which is located in placeInBoard
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).getPiece().CalculatePlaceInBoard() == placeInBoard) {
                return a.get(i);
            }
        }
        return null;
    }

    public Move FindDestinationInArray(ArrayList<Move> a, int destination) { // Gets a ArrayList of type Move and a place in board and returns a Move containing the same destination as given
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).destination == destination) {
                return a.get(i);
            }
        }
        return null;
    }
}

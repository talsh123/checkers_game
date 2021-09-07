/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Players;

import Logic.Board.Board;
import static Logic.Board.Board.players;
import Logic.Board.Piece;

/**
 *
 * @author Administrator
 */
public class Computer extends Player {

    public Computer(Board board, int startR, int endR) { // Creates a new AI
        super(board, startR, endR);
    }

    public void DoMove() {
        for (int i = 0; i < 64; i++) { // Runs on all of the checker pieces and checks if they reached the end of the board. If they did they are set as king.
            if (this.pieces.get(i) != null && this.pieces.get(i).getRow() == 7) {
                this.pieces.get(i).setKing(true);
            }
        }
        for (int i = 0; i < 64; i++) { // For the checker Pieces that are kings we calculate the possile moves
            if (this.pieces.get(i) != null && this.pieces.get(i).isKing() == true) {
                this.piecePossibleMovesEat = this.GetPossibleMoveEatKing();
                this.piecePossibleMoves = this.GetPossibleMoveKing();
            }

        }
        if (this.piecePossibleMovesEat.isEmpty() == false) {
            pieces.put(this.piecePossibleMovesEat.get(0).destination, new Piece(this.piecePossibleMovesEat.get(0).destination / 8, this.piecePossibleMovesEat.get(0).destination % 8, true, this.piecePossibleMovesEat.get(0).getPiece().isKing())); // puts a piece in the new location
            pieces.remove(this.piecePossibleMovesEat.get(0).getPiece().CalculatePlaceInBoard()); // Removes the last piece 
            players[1].pieces.remove(FindMiddlePiece(this.piecePossibleMovesEat.get(0).getPiece().CalculatePlaceInBoard(), this.piecePossibleMovesEat.get(0).destination)); // removes the eaten piece from the board
            this.piecePossibleMovesEat.clear(); // clears the array
            this.piecePossibleMoves.clear(); // clears the array
            this.colurPlaces = true;
        } else {

            if (this.piecePossibleMoves.isEmpty() == false) { // The AI moves
                pieces.put(this.piecePossibleMoves.get(0).destination, new Piece(this.piecePossibleMoves.get(0).destination / 8, this.piecePossibleMoves.get(0).destination % 8, true, this.piecePossibleMoves.get(0).getPiece().isKing())); // puts a piece in the new location
                pieces.remove(this.piecePossibleMoves.get(0).getPiece().CalculatePlaceInBoard());// removes the piece from the last location
                this.piecePossibleMoves.clear(); // clears the array
                this.piecePossibleMovesEat.clear(); // clears the array
                this.colurPlaces = true;
            } else {
                this.piecePossibleMovesEat = GetPossibleMoveEat();
                if (this.piecePossibleMovesEat.isEmpty() == true) { // If the AI has nothing to eat
                    this.piecePossibleMoves = GetPossibleMove();
                    if (this.piecePossibleMoves.isEmpty() == false) { // The AI moves
                        pieces.put(this.piecePossibleMoves.get(0).destination, new Piece(this.piecePossibleMoves.get(0).destination / 8, this.piecePossibleMoves.get(0).destination % 8, true, this.piecePossibleMoves.get(0).getPiece().isKing())); // puts a piece in the new location
                        pieces.remove(this.piecePossibleMoves.get(0).getPiece().CalculatePlaceInBoard());// removes the piece from the last location
                        this.piecePossibleMoves.clear(); // clears the array
                        this.piecePossibleMovesEat.clear(); // clears the array
                        this.colurPlaces = true;
                    }
                } else { //The AI eats
                    pieces.put(this.piecePossibleMovesEat.get(0).destination, new Piece(this.piecePossibleMovesEat.get(0).destination / 8, this.piecePossibleMovesEat.get(0).destination % 8, true, this.piecePossibleMovesEat.get(0).getPiece().isKing())); // puts a piece in the new location
                    pieces.remove(this.piecePossibleMovesEat.get(0).getPiece().CalculatePlaceInBoard()); // Removes the last piece 
                    players[1].pieces.remove(FindMiddlePiece(this.piecePossibleMovesEat.get(0).getPiece().CalculatePlaceInBoard(), this.piecePossibleMovesEat.get(0).destination)); // removes the eaten piece from the board
                    this.piecePossibleMovesEat.clear(); // clears the array
                    this.piecePossibleMoves.clear(); // clears the array
                    this.colurPlaces = true;
                }

            }
        }

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Players;

import Logic.Board.Piece;

/**
 *
 * @author Administrator
 */
public class Move {

    Piece piece; // Piece
    public Integer destination; // Destination

    public Move(Piece piece, Integer destination) { // Creates a new Piece
        this.piece = piece;
        this.destination = destination;

    }

    public Piece getPiece() { // Returns the Piece
        return this.piece;
    }

    public void setPiece(Piece piece) { // Sets the Piece
        this.piece = piece;
    }
}

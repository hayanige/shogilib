package com.github.hayanige.shogilib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestZobristHash {

  @Test
  public void testZobrist1() {
    Position position1 = Position.createHiratePosition();
    long hash1 = position1.getZobristHash();

    Move move1 = Move.makeMove(Square.SQ_77, Square.SQ_76);
    Move move2 = Move.makeMove(Square.SQ_33, Square.SQ_34);
    Move move3 = Move.makeMovePromote(Square.SQ_88, Square.SQ_22);
    position1.doMove(move1);
    position1.doMove(move2);
    position1.doMove(move3);
    long hash2 = position1.getZobristHash();

    Position position2 = Position.createPositionFromSfen(position1.getSfen());
    long hash3 = position2.getZobristHash();
    Assertions.assertEquals(hash2, hash3);

    position1.undoMove();
    position1.undoMove();
    position1.undoMove();
    long hash4 = position1.getZobristHash();
    Assertions.assertEquals(hash1, hash4);
  }

  @Test
  public void testZobrist2() {
    Position position = Position.createHiratePosition();
    position.setPiece(Square.SQ_55, Piece.W_ROOK);
    position.addPieceToHand(Color.BLACK, PieceType.PAWN);
    position.addPieceToHand(Color.BLACK, PieceType.PAWN);
    long hash1 = position.getZobristHash();

    position.doMove(Move.makeMoveDrop(PieceType.PAWN, Square.SQ_25));
    position.doMove(Move.makeMove(Square.SQ_55, Square.SQ_25));
    position.doMove(Move.makeMoveDrop(PieceType.PAWN, Square.SQ_55));
    position.doMove(Move.makeMove(Square.SQ_25, Square.SQ_55));
    long hash2 = position.getZobristHash();
    Assertions.assertNotEquals(hash1, hash2);
  }
}

package com.github.hayanige.shogilib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPosition {

  @Test
  public void testRepetition() {
    Position position = Position.createHiratePosition();
    Assertions.assertFalse(position.isRepetition());
    Move move1 = Move.makeMove(Square.SQ_77, Square.SQ_76);
    Move move2 = Move.makeMove(Square.SQ_33, Square.SQ_34);
    position.doMove(move1);
    position.doMove(move2);
    Assertions.assertFalse(position.isRepetition());

    Move move3 = Move.makeMove(Square.SQ_88, Square.SQ_77);
    Move move4 = Move.makeMove(Square.SQ_22, Square.SQ_33);
    Move move5 = Move.makeMove(Square.SQ_77, Square.SQ_88);
    Move move6 = Move.makeMove(Square.SQ_33, Square.SQ_22);
    position.doMove(move3);
    position.doMove(move4);
    position.doMove(move5);
    position.doMove(move6);
    Assertions.assertFalse(position.isRepetition());

    position.doMove(move3);
    position.doMove(move4);
    position.doMove(move5);
    position.doMove(move6);
    Assertions.assertFalse(position.isRepetition());

    position.doMove(move3);
    position.doMove(move4);
    position.doMove(move5);
    position.doMove(move6);
    Assertions.assertTrue(position.isRepetition());
  }

  @Test
  public void testStalemate() {
    Position position = Position.createPositionFromSfen("+K+K1+L+S+R3/+P6+P1/+P1PK5/6S1+B/9/4Gn2p/1ppp2+Rp+p/p+k+l+pppp+p+b/gg+s+s+l+p+l+pk w G 1");
    Assertions.assertTrue(position.isMated());
  }

  @Test
  public void testUndoMove() {
    Position position = Position.createHiratePosition();
    Move move1 = Move.makeMove(Square.SQ_77, Square.SQ_76);
    Move move2 = Move.makeMove(Square.SQ_33, Square.SQ_34);
    position.doMove(move1);
    position.doMove(move2);
    Assertions.assertEquals(move2, position.undoMove());
    Assertions.assertEquals(move1, position.undoMove());
    Assertions.assertEquals(Move.MOVE_NONE, position.undoMove());
    Assertions.assertEquals(Move.MOVE_NONE, position.undoMove());
  }

  @Test
  public void testNullMove() {
    Position position = Position.createHiratePosition();
    Move move = Move.MOVE_NULL;
    position.doMove(move);
    Assertions.assertEquals(Color.WHITE, position.getSideToMove());
    Assertions.assertTrue(position.getZobristHash() < 0);
  }
}

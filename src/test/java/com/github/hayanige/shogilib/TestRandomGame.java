package com.github.hayanige.shogilib;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * The random game test can detect a bunch of bugs because there are many java assertions
 * in the source code. This is the idea of the following article.
 * https://yaneuraou.yaneu.com/2015/12/19/%e9%80%a3%e8%bc%89%e3%82%84%e3%81%ad%e3%81%86%e3%82%89%e7%8e%8bmini%e3%81%a7%e9%81%8a%e3%81%bc%e3%81%86%ef%bc%819%e6%97%a5%e7%9b%ae/
 */
public class TestRandomGame {

  @Test
  public void testRandomGame100() throws Exception {
    doRandomGame(100);
  }

  @Test
  @Disabled
  public void testRandomGame100000() throws Exception {
    doRandomGame(100000);
  }

  void doRandomGame(final int number) throws Exception {
    Random random = new Random();
    Position position;
    List<Move> moves;
    Move move;
    for (int i = 0; i < number; i++) {
      position = Position.createHiratePosition();

      // do random game
      do {
        try {
          moves = position.getLegalMoves();
          move = moves.get(random.nextInt(moves.size()));
          position.doMove(move);
        } catch (Exception e) {
          // print the position for debugging
          System.out.println(position.pretty());
          System.out.println(position.getSfen());
          throw new Exception(e);
        }
      } while (!position.isMated() && position.getMoveCounter() < 1500);

      // check zobrist hash
      long hash1 = position.getZobristHash();
      Position position2 = Position.createPositionFromSfen(position.getSfen());
      long hash2 = position2.getZobristHash();
      Assertions.assertEquals(hash1, hash2);

      if (position.getSideToMove() == Color.BLACK) {
        Assertions.assertTrue(position.getZobristHash() > 0);
      } else {
        Assertions.assertTrue(position.getZobristHash() < 0);
      }
    }
  }
}

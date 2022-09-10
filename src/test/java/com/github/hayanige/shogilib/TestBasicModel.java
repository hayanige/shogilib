package com.github.hayanige.shogilib;

import static com.github.hayanige.shogilib.Color.*;
import static com.github.hayanige.shogilib.File.*;
import static com.github.hayanige.shogilib.Piece.*;
import static com.github.hayanige.shogilib.PieceType.*;
import static com.github.hayanige.shogilib.Rank.*;
import static com.github.hayanige.shogilib.Square.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestBasicModel {

  @Test
  public void testColor() {
    Assertions.assertEquals(BLACK, WHITE.getOpponent());
    Assertions.assertEquals(WHITE, BLACK.getOpponent());
  }

  @Test
  public void testMove() {
    Move move1 = Move.makeMove(SQ_77, SQ_76);
    Assertions.assertFalse(move1.isPromote());
    Assertions.assertFalse(move1.isDrop());

    Move move2 = Move.makeMovePromote(SQ_88, SQ_22);
    Assertions.assertTrue(move2.isPromote());
    Assertions.assertFalse(move2.isDrop());

    Move move3 = Move.makeMoveDrop(PAWN, SQ_53);
    Assertions.assertFalse(move3.isPromote());
    Assertions.assertTrue(move3.isDrop());
  }

  @Test
  public void testFile() {
    Assertions.assertEquals(FILE_3, File.valueOf(2));
  }

  @Test
  public void testRank() {
    Assertions.assertEquals(RANK_7, Rank.valueOf(6));
  }

  @Test
  public void testSquare() {
    Assertions.assertEquals(FILE_7, SQ_76.getFile());
    Assertions.assertEquals(RANK_4, SQ_34.getRank());
    Assertions.assertTrue(SQ_53.canPromote(BLACK));
    Assertions.assertFalse(SQ_66.canPromote(WHITE));
    Assertions.assertEquals(SQ_59, Square.valueOf(44));
    Assertions.assertEquals(SQ_51, Square.valueOf(FILE_5, RANK_1));
  }

  @Test
  public void testPiece() {
    Assertions.assertEquals(B_PRO_PAWN, B_PAWN.getPromoted());
    Assertions.assertEquals(W_ROOK, W_DRAGON.getReversePromoted());
    Assertions.assertEquals(HORSE, B_HORSE.getPieceType());
    Assertions.assertEquals(BISHOP, B_HORSE.getRawType());
    Assertions.assertEquals(W_KNIGHT, KNIGHT.getColoredPiece(WHITE));
  }

  @Test
  public void testHand() {
    Hand hand = new Hand();
    Assertions.assertTrue(hand.isZero());
    Assertions.assertFalse(hand.existsExceptPawn());
    hand.add(PAWN);
    hand.add(PAWN);
    Assertions.assertFalse(hand.isZero());
    Assertions.assertFalse(hand.existsExceptPawn());
    hand.add(ROOK);
    hand.add(ROOK);
    hand.subtract(ROOK);
    Assertions.assertEquals(2, hand.count(PAWN));
    Assertions.assertEquals(1, hand.count(ROOK));
    Assertions.assertEquals(0, hand.count(SILVER));
    Assertions.assertFalse(hand.isZero());
    Assertions.assertTrue(hand.existsExceptPawn());
  }
}

package com.github.hayanige.shogilib;

import static com.github.hayanige.shogilib.PieceType.BISHOP;
import static com.github.hayanige.shogilib.Position.HIRATE_SFEN;
import static com.github.hayanige.shogilib.Square.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSfen {

  @Test
  public void testSfen1() {
    Position position = Position.createHiratePosition();
    Assertions.assertEquals(HIRATE_SFEN, position.getSfen());
    position.doMove(Move.makeMove(SQ_77, SQ_76));
    position.doMove(Move.makeMove(SQ_33, SQ_34));
    position.doMove(Move.makeMovePromote(SQ_88, SQ_22));
    position.doMove(Move.makeMove(SQ_31, SQ_22));
    position.doMove(Move.makeMoveDrop(BISHOP, SQ_55));
    Assertions.assertEquals( "lnsgkg1nl/1r5s1/pppppp1pp/6p2/4B4/2P6/PP1PPPPPP/7R1/LNSGKGSNL w b 6",
        position.getSfen());
  }

  @Test
  public void testSfen2() {
    Position position = Position.createPositionFromSfen(
        "R8/2K1S1SSk/4B4/9/9/9/9/9/1L1L1L3 b RBGSNLP3g3n17p 1");
    Assertions.assertEquals("R8/2K1S1SSk/4B4/9/9/9/9/9/1L1L1L3 b RBGSNLP3g3n17p 1",
        position.getSfen());
  }

  @Test
  public void testSfenStartPos() {
    Position position1 = Position.createPositionFromSfen("startpos");
    Position position2 = Position.createHiratePosition();
    Assertions.assertEquals(position2.getSfen(), position1.getSfen());
  }

  @Test
  public void testInvalidSfen() {
    Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        Position.createPositionFromSfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL"));
    Assertions.assertTrue(exception.getMessage().contains("The number of elements is not enough."));

    exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        Position.createPositionFromSfen("R8/2K1S1SSk/4B4/9/9/9/9/9 b RBGSNLP3g3n17p 1"));
    Assertions.assertTrue(exception.getMessage().contains("The number of ranks is not enough."));

    exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        Position.createPositionFromSfen("R8/abcdefghi/4B4/9/9/9/9/9/1L1L1L3 a RBGSNLP3g3n17p 1"));
    Assertions.assertTrue(exception.getMessage().contains("A piece is invalid."));

    exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        Position.createPositionFromSfen("R8/2K1SSSk/4B4/9/9/9/9/9/1L1L1L3 b RBGSNLP3g3n17p 1"));
    Assertions.assertTrue(exception.getMessage().contains("The number of pieces is not enough."));

    exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        Position.createPositionFromSfen("R8/2K1S1SSk/4B4/9/9/9/9/9/1L1L1L3 a RBGSNLP3g3n17p 1"));
    Assertions.assertTrue(exception.getMessage().contains("The side is invalid."));

    exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        Position.createPositionFromSfen("R8/2K1S1SSk/4B4/9/9/9/9/9/1L1L1L3 b abc 1"));
    Assertions.assertTrue(exception.getMessage().contains("The hand is invalid."));

    exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        Position.createPositionFromSfen("R8/2K1S1SSk/4B4/9/9/9/9/9/1L1L1L3 b RBGSNLP3g3n17p a"));
    Assertions.assertTrue(exception.getMessage().contains("The next move number is invalid."));
  }
}

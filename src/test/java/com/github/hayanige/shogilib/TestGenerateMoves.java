package com.github.hayanige.shogilib;

import static com.github.hayanige.shogilib.Perft.perftWithTime;

import com.github.hayanige.shogilib.Perft.PerftResult;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TestGenerateMoves {

  @Test
  public void testHirateMoves() {
    Move[] expectedMoves = new Move[] {
        Move.makeMoveUSI("1g1f"), Move.makeMoveUSI("1i1h"), Move.makeMoveUSI("2g2f"), Move.makeMoveUSI("2h1h"),
        Move.makeMoveUSI("2h3h"), Move.makeMoveUSI("2h4h"), Move.makeMoveUSI("2h5h"), Move.makeMoveUSI("2h6h"),
        Move.makeMoveUSI("2h7h"), Move.makeMoveUSI("3g3f"), Move.makeMoveUSI("3i3h"), Move.makeMoveUSI("3i4h"),
        Move.makeMoveUSI("4g4f"), Move.makeMoveUSI("4i3h"), Move.makeMoveUSI("4i4h"), Move.makeMoveUSI("4i5h"),
        Move.makeMoveUSI("5g5f"), Move.makeMoveUSI("5i4h"), Move.makeMoveUSI("5i5h"), Move.makeMoveUSI("5i6h"),
        Move.makeMoveUSI("6g6f"), Move.makeMoveUSI("6i5h"), Move.makeMoveUSI("6i6h"), Move.makeMoveUSI("6i7h"),
        Move.makeMoveUSI("7g7f"), Move.makeMoveUSI("7i6h"), Move.makeMoveUSI("7i7h"), Move.makeMoveUSI("8g8f"),
        Move.makeMoveUSI("9g9f"), Move.makeMoveUSI("9i9h")
    };

    Position position = Position.createHiratePosition();
    List<Move> moves = position.getLegalMoves();
    Collections.sort(moves);
    Assertions.assertArrayEquals(expectedMoves, moves.toArray());
  }

  // The position that generates max moves.
  private static final String MAX_MOVES_POSITION_SFEN
      = "R8/2K1S1SSk/4B4/9/9/9/9/9/1L1L1L3 b RBGSNLP3g3n17p 1";

  @Test
  public void testMaxMovesPositionMoves() {
    Move[] expectedMoves = new Move[]{
        Move.makeMoveUSI("2b1a"), Move.makeMoveUSI("2b1c"), Move.makeMoveUSI("2b2a"), Move.makeMoveUSI("2b3a"),
        Move.makeMoveUSI("2b3c"), Move.makeMoveUSI("3b2a"), Move.makeMoveUSI("3b2c"), Move.makeMoveUSI("3b3a"),
        Move.makeMoveUSI("3b4a"), Move.makeMoveUSI("3b4c"), Move.makeMoveUSI("4i4b"), Move.makeMoveUSI("4i4c"),
        Move.makeMoveUSI("4i4d"), Move.makeMoveUSI("4i4e"), Move.makeMoveUSI("4i4f"), Move.makeMoveUSI("4i4g"),
        Move.makeMoveUSI("4i4h"), Move.makeMoveUSI("5b4a"), Move.makeMoveUSI("5b4c"), Move.makeMoveUSI("5b5a"),
        Move.makeMoveUSI("5b6a"), Move.makeMoveUSI("5b6c"), Move.makeMoveUSI("5c1g"), Move.makeMoveUSI("5c2f"),
        Move.makeMoveUSI("5c3a"), Move.makeMoveUSI("5c3e"), Move.makeMoveUSI("5c4b"), Move.makeMoveUSI("5c4d"),
        Move.makeMoveUSI("5c6b"), Move.makeMoveUSI("5c6d"), Move.makeMoveUSI("5c7a"), Move.makeMoveUSI("5c7e"),
        Move.makeMoveUSI("5c8f"), Move.makeMoveUSI("5c9g"), Move.makeMoveUSI("6i6b"), Move.makeMoveUSI("6i6c"),
        Move.makeMoveUSI("6i6d"), Move.makeMoveUSI("6i6e"), Move.makeMoveUSI("6i6f"), Move.makeMoveUSI("6i6g"),
        Move.makeMoveUSI("6i6h"), Move.makeMoveUSI("7b6a"), Move.makeMoveUSI("7b6b"), Move.makeMoveUSI("7b6c"),
        Move.makeMoveUSI("7b7a"), Move.makeMoveUSI("7b7c"), Move.makeMoveUSI("7b8a"), Move.makeMoveUSI("7b8b"),
        Move.makeMoveUSI("7b8c"), Move.makeMoveUSI("8i8b"), Move.makeMoveUSI("8i8c"), Move.makeMoveUSI("8i8d"),
        Move.makeMoveUSI("8i8e"), Move.makeMoveUSI("8i8f"), Move.makeMoveUSI("8i8g"), Move.makeMoveUSI("8i8h"),
        Move.makeMoveUSI("9a1a"), Move.makeMoveUSI("9a2a"), Move.makeMoveUSI("9a3a"), Move.makeMoveUSI("9a4a"),
        Move.makeMoveUSI("9a5a"), Move.makeMoveUSI("9a6a"), Move.makeMoveUSI("9a7a"), Move.makeMoveUSI("9a8a"),
        Move.makeMoveUSI("9a9b"), Move.makeMoveUSI("9a9c"), Move.makeMoveUSI("9a9d"), Move.makeMoveUSI("9a9e"),
        Move.makeMoveUSI("9a9f"), Move.makeMoveUSI("9a9g"), Move.makeMoveUSI("9a9h"), Move.makeMoveUSI("9a9i"),
        Move.makeMoveUSI("P*1c"), Move.makeMoveUSI("P*1d"), Move.makeMoveUSI("P*1e"), Move.makeMoveUSI("P*1f"),
        Move.makeMoveUSI("P*1g"), Move.makeMoveUSI("P*1h"), Move.makeMoveUSI("P*1i"), Move.makeMoveUSI("P*2c"),
        Move.makeMoveUSI("P*2d"), Move.makeMoveUSI("P*2e"), Move.makeMoveUSI("P*2f"), Move.makeMoveUSI("P*2g"),
        Move.makeMoveUSI("P*2h"), Move.makeMoveUSI("P*2i"), Move.makeMoveUSI("P*3c"), Move.makeMoveUSI("P*3d"),
        Move.makeMoveUSI("P*3e"), Move.makeMoveUSI("P*3f"), Move.makeMoveUSI("P*3g"), Move.makeMoveUSI("P*3h"),
        Move.makeMoveUSI("P*3i"), Move.makeMoveUSI("P*4b"), Move.makeMoveUSI("P*4c"), Move.makeMoveUSI("P*4d"),
        Move.makeMoveUSI("P*4e"), Move.makeMoveUSI("P*4f"), Move.makeMoveUSI("P*4g"), Move.makeMoveUSI("P*4h"),
        Move.makeMoveUSI("P*5d"), Move.makeMoveUSI("P*5e"), Move.makeMoveUSI("P*5f"), Move.makeMoveUSI("P*5g"),
        Move.makeMoveUSI("P*5h"), Move.makeMoveUSI("P*5i"), Move.makeMoveUSI("P*6b"), Move.makeMoveUSI("P*6c"),
        Move.makeMoveUSI("P*6d"), Move.makeMoveUSI("P*6e"), Move.makeMoveUSI("P*6f"), Move.makeMoveUSI("P*6g"),
        Move.makeMoveUSI("P*6h"), Move.makeMoveUSI("P*7c"), Move.makeMoveUSI("P*7d"), Move.makeMoveUSI("P*7e"),
        Move.makeMoveUSI("P*7f"), Move.makeMoveUSI("P*7g"), Move.makeMoveUSI("P*7h"), Move.makeMoveUSI("P*7i"),
        Move.makeMoveUSI("P*8b"), Move.makeMoveUSI("P*8c"), Move.makeMoveUSI("P*8d"), Move.makeMoveUSI("P*8e"),
        Move.makeMoveUSI("P*8f"), Move.makeMoveUSI("P*8g"), Move.makeMoveUSI("P*8h"), Move.makeMoveUSI("P*9b"),
        Move.makeMoveUSI("P*9c"), Move.makeMoveUSI("P*9d"), Move.makeMoveUSI("P*9e"), Move.makeMoveUSI("P*9f"),
        Move.makeMoveUSI("P*9g"), Move.makeMoveUSI("P*9h"), Move.makeMoveUSI("P*9i"), Move.makeMoveUSI("L*1c"),
        Move.makeMoveUSI("L*1d"), Move.makeMoveUSI("L*1e"), Move.makeMoveUSI("L*1f"), Move.makeMoveUSI("L*1g"),
        Move.makeMoveUSI("L*1h"), Move.makeMoveUSI("L*1i"), Move.makeMoveUSI("L*2c"), Move.makeMoveUSI("L*2d"),
        Move.makeMoveUSI("L*2e"), Move.makeMoveUSI("L*2f"), Move.makeMoveUSI("L*2g"), Move.makeMoveUSI("L*2h"),
        Move.makeMoveUSI("L*2i"), Move.makeMoveUSI("L*3c"), Move.makeMoveUSI("L*3d"), Move.makeMoveUSI("L*3e"),
        Move.makeMoveUSI("L*3f"), Move.makeMoveUSI("L*3g"), Move.makeMoveUSI("L*3h"), Move.makeMoveUSI("L*3i"),
        Move.makeMoveUSI("L*4b"), Move.makeMoveUSI("L*4c"), Move.makeMoveUSI("L*4d"), Move.makeMoveUSI("L*4e"),
        Move.makeMoveUSI("L*4f"), Move.makeMoveUSI("L*4g"), Move.makeMoveUSI("L*4h"), Move.makeMoveUSI("L*5d"),
        Move.makeMoveUSI("L*5e"), Move.makeMoveUSI("L*5f"), Move.makeMoveUSI("L*5g"), Move.makeMoveUSI("L*5h"),
        Move.makeMoveUSI("L*5i"), Move.makeMoveUSI("L*6b"), Move.makeMoveUSI("L*6c"), Move.makeMoveUSI("L*6d"),
        Move.makeMoveUSI("L*6e"), Move.makeMoveUSI("L*6f"), Move.makeMoveUSI("L*6g"), Move.makeMoveUSI("L*6h"),
        Move.makeMoveUSI("L*7c"), Move.makeMoveUSI("L*7d"), Move.makeMoveUSI("L*7e"), Move.makeMoveUSI("L*7f"),
        Move.makeMoveUSI("L*7g"), Move.makeMoveUSI("L*7h"), Move.makeMoveUSI("L*7i"), Move.makeMoveUSI("L*8b"),
        Move.makeMoveUSI("L*8c"), Move.makeMoveUSI("L*8d"), Move.makeMoveUSI("L*8e"), Move.makeMoveUSI("L*8f"),
        Move.makeMoveUSI("L*8g"), Move.makeMoveUSI("L*8h"), Move.makeMoveUSI("L*9b"), Move.makeMoveUSI("L*9c"),
        Move.makeMoveUSI("L*9d"), Move.makeMoveUSI("L*9e"), Move.makeMoveUSI("L*9f"), Move.makeMoveUSI("L*9g"),
        Move.makeMoveUSI("L*9h"), Move.makeMoveUSI("L*9i"), Move.makeMoveUSI("N*1c"), Move.makeMoveUSI("N*1d"),
        Move.makeMoveUSI("N*1e"), Move.makeMoveUSI("N*1f"), Move.makeMoveUSI("N*1g"), Move.makeMoveUSI("N*1h"),
        Move.makeMoveUSI("N*1i"), Move.makeMoveUSI("N*2c"), Move.makeMoveUSI("N*2d"), Move.makeMoveUSI("N*2e"),
        Move.makeMoveUSI("N*2f"), Move.makeMoveUSI("N*2g"), Move.makeMoveUSI("N*2h"), Move.makeMoveUSI("N*2i"),
        Move.makeMoveUSI("N*3c"), Move.makeMoveUSI("N*3d"), Move.makeMoveUSI("N*3e"), Move.makeMoveUSI("N*3f"),
        Move.makeMoveUSI("N*3g"), Move.makeMoveUSI("N*3h"), Move.makeMoveUSI("N*3i"), Move.makeMoveUSI("N*4c"),
        Move.makeMoveUSI("N*4d"), Move.makeMoveUSI("N*4e"), Move.makeMoveUSI("N*4f"), Move.makeMoveUSI("N*4g"),
        Move.makeMoveUSI("N*4h"), Move.makeMoveUSI("N*5d"), Move.makeMoveUSI("N*5e"), Move.makeMoveUSI("N*5f"),
        Move.makeMoveUSI("N*5g"), Move.makeMoveUSI("N*5h"), Move.makeMoveUSI("N*5i"), Move.makeMoveUSI("N*6c"),
        Move.makeMoveUSI("N*6d"), Move.makeMoveUSI("N*6e"), Move.makeMoveUSI("N*6f"), Move.makeMoveUSI("N*6g"),
        Move.makeMoveUSI("N*6h"), Move.makeMoveUSI("N*7c"), Move.makeMoveUSI("N*7d"), Move.makeMoveUSI("N*7e"),
        Move.makeMoveUSI("N*7f"), Move.makeMoveUSI("N*7g"), Move.makeMoveUSI("N*7h"), Move.makeMoveUSI("N*7i"),
        Move.makeMoveUSI("N*8c"), Move.makeMoveUSI("N*8d"), Move.makeMoveUSI("N*8e"), Move.makeMoveUSI("N*8f"),
        Move.makeMoveUSI("N*8g"), Move.makeMoveUSI("N*8h"), Move.makeMoveUSI("N*9c"), Move.makeMoveUSI("N*9d"),
        Move.makeMoveUSI("N*9e"), Move.makeMoveUSI("N*9f"), Move.makeMoveUSI("N*9g"), Move.makeMoveUSI("N*9h"),
        Move.makeMoveUSI("N*9i"), Move.makeMoveUSI("S*1a"), Move.makeMoveUSI("S*1c"), Move.makeMoveUSI("S*1d"),
        Move.makeMoveUSI("S*1e"), Move.makeMoveUSI("S*1f"), Move.makeMoveUSI("S*1g"), Move.makeMoveUSI("S*1h"),
        Move.makeMoveUSI("S*1i"), Move.makeMoveUSI("S*2a"), Move.makeMoveUSI("S*2c"), Move.makeMoveUSI("S*2d"),
        Move.makeMoveUSI("S*2e"), Move.makeMoveUSI("S*2f"), Move.makeMoveUSI("S*2g"), Move.makeMoveUSI("S*2h"),
        Move.makeMoveUSI("S*2i"), Move.makeMoveUSI("S*3a"), Move.makeMoveUSI("S*3c"), Move.makeMoveUSI("S*3d"),
        Move.makeMoveUSI("S*3e"), Move.makeMoveUSI("S*3f"), Move.makeMoveUSI("S*3g"), Move.makeMoveUSI("S*3h"),
        Move.makeMoveUSI("S*3i"), Move.makeMoveUSI("S*4a"), Move.makeMoveUSI("S*4b"), Move.makeMoveUSI("S*4c"),
        Move.makeMoveUSI("S*4d"), Move.makeMoveUSI("S*4e"), Move.makeMoveUSI("S*4f"), Move.makeMoveUSI("S*4g"),
        Move.makeMoveUSI("S*4h"), Move.makeMoveUSI("S*5a"), Move.makeMoveUSI("S*5d"), Move.makeMoveUSI("S*5e"),
        Move.makeMoveUSI("S*5f"), Move.makeMoveUSI("S*5g"), Move.makeMoveUSI("S*5h"), Move.makeMoveUSI("S*5i"),
        Move.makeMoveUSI("S*6a"), Move.makeMoveUSI("S*6b"), Move.makeMoveUSI("S*6c"), Move.makeMoveUSI("S*6d"),
        Move.makeMoveUSI("S*6e"), Move.makeMoveUSI("S*6f"), Move.makeMoveUSI("S*6g"), Move.makeMoveUSI("S*6h"),
        Move.makeMoveUSI("S*7a"), Move.makeMoveUSI("S*7c"), Move.makeMoveUSI("S*7d"), Move.makeMoveUSI("S*7e"),
        Move.makeMoveUSI("S*7f"), Move.makeMoveUSI("S*7g"), Move.makeMoveUSI("S*7h"), Move.makeMoveUSI("S*7i"),
        Move.makeMoveUSI("S*8a"), Move.makeMoveUSI("S*8b"), Move.makeMoveUSI("S*8c"), Move.makeMoveUSI("S*8d"),
        Move.makeMoveUSI("S*8e"), Move.makeMoveUSI("S*8f"), Move.makeMoveUSI("S*8g"), Move.makeMoveUSI("S*8h"),
        Move.makeMoveUSI("S*9b"), Move.makeMoveUSI("S*9c"), Move.makeMoveUSI("S*9d"), Move.makeMoveUSI("S*9e"),
        Move.makeMoveUSI("S*9f"), Move.makeMoveUSI("S*9g"), Move.makeMoveUSI("S*9h"), Move.makeMoveUSI("S*9i"),
        Move.makeMoveUSI("B*1a"), Move.makeMoveUSI("B*1c"), Move.makeMoveUSI("B*1d"), Move.makeMoveUSI("B*1e"),
        Move.makeMoveUSI("B*1f"), Move.makeMoveUSI("B*1g"), Move.makeMoveUSI("B*1h"), Move.makeMoveUSI("B*1i"),
        Move.makeMoveUSI("B*2a"), Move.makeMoveUSI("B*2c"), Move.makeMoveUSI("B*2d"), Move.makeMoveUSI("B*2e"),
        Move.makeMoveUSI("B*2f"), Move.makeMoveUSI("B*2g"), Move.makeMoveUSI("B*2h"), Move.makeMoveUSI("B*2i"),
        Move.makeMoveUSI("B*3a"), Move.makeMoveUSI("B*3c"), Move.makeMoveUSI("B*3d"), Move.makeMoveUSI("B*3e"),
        Move.makeMoveUSI("B*3f"), Move.makeMoveUSI("B*3g"), Move.makeMoveUSI("B*3h"), Move.makeMoveUSI("B*3i"),
        Move.makeMoveUSI("B*4a"), Move.makeMoveUSI("B*4b"), Move.makeMoveUSI("B*4c"), Move.makeMoveUSI("B*4d"),
        Move.makeMoveUSI("B*4e"), Move.makeMoveUSI("B*4f"), Move.makeMoveUSI("B*4g"), Move.makeMoveUSI("B*4h"),
        Move.makeMoveUSI("B*5a"), Move.makeMoveUSI("B*5d"), Move.makeMoveUSI("B*5e"), Move.makeMoveUSI("B*5f"),
        Move.makeMoveUSI("B*5g"), Move.makeMoveUSI("B*5h"), Move.makeMoveUSI("B*5i"), Move.makeMoveUSI("B*6a"),
        Move.makeMoveUSI("B*6b"), Move.makeMoveUSI("B*6c"), Move.makeMoveUSI("B*6d"), Move.makeMoveUSI("B*6e"),
        Move.makeMoveUSI("B*6f"), Move.makeMoveUSI("B*6g"), Move.makeMoveUSI("B*6h"), Move.makeMoveUSI("B*7a"),
        Move.makeMoveUSI("B*7c"), Move.makeMoveUSI("B*7d"), Move.makeMoveUSI("B*7e"), Move.makeMoveUSI("B*7f"),
        Move.makeMoveUSI("B*7g"), Move.makeMoveUSI("B*7h"), Move.makeMoveUSI("B*7i"), Move.makeMoveUSI("B*8a"),
        Move.makeMoveUSI("B*8b"), Move.makeMoveUSI("B*8c"), Move.makeMoveUSI("B*8d"), Move.makeMoveUSI("B*8e"),
        Move.makeMoveUSI("B*8f"), Move.makeMoveUSI("B*8g"), Move.makeMoveUSI("B*8h"), Move.makeMoveUSI("B*9b"),
        Move.makeMoveUSI("B*9c"), Move.makeMoveUSI("B*9d"), Move.makeMoveUSI("B*9e"), Move.makeMoveUSI("B*9f"),
        Move.makeMoveUSI("B*9g"), Move.makeMoveUSI("B*9h"), Move.makeMoveUSI("B*9i"), Move.makeMoveUSI("R*1a"),
        Move.makeMoveUSI("R*1c"), Move.makeMoveUSI("R*1d"), Move.makeMoveUSI("R*1e"), Move.makeMoveUSI("R*1f"),
        Move.makeMoveUSI("R*1g"), Move.makeMoveUSI("R*1h"), Move.makeMoveUSI("R*1i"), Move.makeMoveUSI("R*2a"),
        Move.makeMoveUSI("R*2c"), Move.makeMoveUSI("R*2d"), Move.makeMoveUSI("R*2e"), Move.makeMoveUSI("R*2f"),
        Move.makeMoveUSI("R*2g"), Move.makeMoveUSI("R*2h"), Move.makeMoveUSI("R*2i"), Move.makeMoveUSI("R*3a"),
        Move.makeMoveUSI("R*3c"), Move.makeMoveUSI("R*3d"), Move.makeMoveUSI("R*3e"), Move.makeMoveUSI("R*3f"),
        Move.makeMoveUSI("R*3g"), Move.makeMoveUSI("R*3h"), Move.makeMoveUSI("R*3i"), Move.makeMoveUSI("R*4a"),
        Move.makeMoveUSI("R*4b"), Move.makeMoveUSI("R*4c"), Move.makeMoveUSI("R*4d"), Move.makeMoveUSI("R*4e"),
        Move.makeMoveUSI("R*4f"), Move.makeMoveUSI("R*4g"), Move.makeMoveUSI("R*4h"), Move.makeMoveUSI("R*5a"),
        Move.makeMoveUSI("R*5d"), Move.makeMoveUSI("R*5e"), Move.makeMoveUSI("R*5f"), Move.makeMoveUSI("R*5g"),
        Move.makeMoveUSI("R*5h"), Move.makeMoveUSI("R*5i"), Move.makeMoveUSI("R*6a"), Move.makeMoveUSI("R*6b"),
        Move.makeMoveUSI("R*6c"), Move.makeMoveUSI("R*6d"), Move.makeMoveUSI("R*6e"), Move.makeMoveUSI("R*6f"),
        Move.makeMoveUSI("R*6g"), Move.makeMoveUSI("R*6h"), Move.makeMoveUSI("R*7a"), Move.makeMoveUSI("R*7c"),
        Move.makeMoveUSI("R*7d"), Move.makeMoveUSI("R*7e"), Move.makeMoveUSI("R*7f"), Move.makeMoveUSI("R*7g"),
        Move.makeMoveUSI("R*7h"), Move.makeMoveUSI("R*7i"), Move.makeMoveUSI("R*8a"), Move.makeMoveUSI("R*8b"),
        Move.makeMoveUSI("R*8c"), Move.makeMoveUSI("R*8d"), Move.makeMoveUSI("R*8e"), Move.makeMoveUSI("R*8f"),
        Move.makeMoveUSI("R*8g"), Move.makeMoveUSI("R*8h"), Move.makeMoveUSI("R*9b"), Move.makeMoveUSI("R*9c"),
        Move.makeMoveUSI("R*9d"), Move.makeMoveUSI("R*9e"), Move.makeMoveUSI("R*9f"), Move.makeMoveUSI("R*9g"),
        Move.makeMoveUSI("R*9h"), Move.makeMoveUSI("R*9i"), Move.makeMoveUSI("G*1a"), Move.makeMoveUSI("G*1c"),
        Move.makeMoveUSI("G*1d"), Move.makeMoveUSI("G*1e"), Move.makeMoveUSI("G*1f"), Move.makeMoveUSI("G*1g"),
        Move.makeMoveUSI("G*1h"), Move.makeMoveUSI("G*1i"), Move.makeMoveUSI("G*2a"), Move.makeMoveUSI("G*2c"),
        Move.makeMoveUSI("G*2d"), Move.makeMoveUSI("G*2e"), Move.makeMoveUSI("G*2f"), Move.makeMoveUSI("G*2g"),
        Move.makeMoveUSI("G*2h"), Move.makeMoveUSI("G*2i"), Move.makeMoveUSI("G*3a"), Move.makeMoveUSI("G*3c"),
        Move.makeMoveUSI("G*3d"), Move.makeMoveUSI("G*3e"), Move.makeMoveUSI("G*3f"), Move.makeMoveUSI("G*3g"),
        Move.makeMoveUSI("G*3h"), Move.makeMoveUSI("G*3i"), Move.makeMoveUSI("G*4a"), Move.makeMoveUSI("G*4b"),
        Move.makeMoveUSI("G*4c"), Move.makeMoveUSI("G*4d"), Move.makeMoveUSI("G*4e"), Move.makeMoveUSI("G*4f"),
        Move.makeMoveUSI("G*4g"), Move.makeMoveUSI("G*4h"), Move.makeMoveUSI("G*5a"), Move.makeMoveUSI("G*5d"),
        Move.makeMoveUSI("G*5e"), Move.makeMoveUSI("G*5f"), Move.makeMoveUSI("G*5g"), Move.makeMoveUSI("G*5h"),
        Move.makeMoveUSI("G*5i"), Move.makeMoveUSI("G*6a"), Move.makeMoveUSI("G*6b"), Move.makeMoveUSI("G*6c"),
        Move.makeMoveUSI("G*6d"), Move.makeMoveUSI("G*6e"), Move.makeMoveUSI("G*6f"), Move.makeMoveUSI("G*6g"),
        Move.makeMoveUSI("G*6h"), Move.makeMoveUSI("G*7a"), Move.makeMoveUSI("G*7c"), Move.makeMoveUSI("G*7d"),
        Move.makeMoveUSI("G*7e"), Move.makeMoveUSI("G*7f"), Move.makeMoveUSI("G*7g"), Move.makeMoveUSI("G*7h"),
        Move.makeMoveUSI("G*7i"), Move.makeMoveUSI("G*8a"), Move.makeMoveUSI("G*8b"), Move.makeMoveUSI("G*8c"),
        Move.makeMoveUSI("G*8d"), Move.makeMoveUSI("G*8e"), Move.makeMoveUSI("G*8f"), Move.makeMoveUSI("G*8g"),
        Move.makeMoveUSI("G*8h"), Move.makeMoveUSI("G*9b"), Move.makeMoveUSI("G*9c"), Move.makeMoveUSI("G*9d"),
        Move.makeMoveUSI("G*9e"), Move.makeMoveUSI("G*9f"), Move.makeMoveUSI("G*9g"), Move.makeMoveUSI("G*9h"),
        Move.makeMoveUSI("G*9i"), Move.makeMoveUSI("2b1a+"), Move.makeMoveUSI("2b1c+"), Move.makeMoveUSI("2b2a+"),
        Move.makeMoveUSI("2b3a+"), Move.makeMoveUSI("2b3c+"), Move.makeMoveUSI("3b2a+"), Move.makeMoveUSI("3b2c+"),
        Move.makeMoveUSI("3b3a+"), Move.makeMoveUSI("3b4a+"), Move.makeMoveUSI("3b4c+"), Move.makeMoveUSI("4i4a+"),
        Move.makeMoveUSI("4i4b+"), Move.makeMoveUSI("4i4c+"), Move.makeMoveUSI("5b4a+"), Move.makeMoveUSI("5b4c+"),
        Move.makeMoveUSI("5b5a+"), Move.makeMoveUSI("5b6a+"), Move.makeMoveUSI("5b6c+"), Move.makeMoveUSI("5c1g+"),
        Move.makeMoveUSI("5c2f+"), Move.makeMoveUSI("5c3a+"), Move.makeMoveUSI("5c3e+"), Move.makeMoveUSI("5c4b+"),
        Move.makeMoveUSI("5c4d+"), Move.makeMoveUSI("5c6b+"), Move.makeMoveUSI("5c6d+"), Move.makeMoveUSI("5c7a+"),
        Move.makeMoveUSI("5c7e+"), Move.makeMoveUSI("5c8f+"), Move.makeMoveUSI("5c9g+"), Move.makeMoveUSI("6i6a+"),
        Move.makeMoveUSI("6i6b+"), Move.makeMoveUSI("6i6c+"), Move.makeMoveUSI("8i8a+"), Move.makeMoveUSI("8i8b+"),
        Move.makeMoveUSI("8i8c+"), Move.makeMoveUSI("9a1a+"), Move.makeMoveUSI("9a2a+"), Move.makeMoveUSI("9a3a+"),
        Move.makeMoveUSI("9a4a+"), Move.makeMoveUSI("9a5a+"), Move.makeMoveUSI("9a6a+"), Move.makeMoveUSI("9a7a+"),
        Move.makeMoveUSI("9a8a+"), Move.makeMoveUSI("9a9b+"), Move.makeMoveUSI("9a9c+"), Move.makeMoveUSI("9a9d+"),
        Move.makeMoveUSI("9a9e+"), Move.makeMoveUSI("9a9f+"), Move.makeMoveUSI("9a9g+"), Move.makeMoveUSI("9a9h+"),
        Move.makeMoveUSI("9a9i+")
    };

    Position position = Position.createPositionFromSfen(MAX_MOVES_POSITION_SFEN);
    List<Move> moves = position.getLegalMoves();
    Collections.sort(moves);
    Assertions.assertArrayEquals(expectedMoves, moves.toArray());
  }

  @Test
  public void perftMaxMovesDepth1() {
    Position position = Position.createPositionFromSfen(MAX_MOVES_POSITION_SFEN);
    PerftResult result = perftWithTime(position, 1, true);
    Assertions.assertEquals(593, result.getNodes());
    Assertions.assertEquals(0, result.getCaptures());
    Assertions.assertEquals(52, result.getPromotions());
    Assertions.assertEquals(40, result.getChecks());
    Assertions.assertEquals(6, result.getCheckmates());
  }

  @Test
  public void perftMaxMovesDepth2() {
    Position position = Position.createPositionFromSfen(MAX_MOVES_POSITION_SFEN);
    PerftResult result = perftWithTime(position, 2, true);
    Assertions.assertEquals(105677, result.getNodes());
    Assertions.assertEquals(538, result.getCaptures());
    Assertions.assertEquals(0, result.getPromotions());
    Assertions.assertEquals(3802, result.getChecks());
    Assertions.assertEquals(0, result.getCheckmates());
  }

  // depth 3 or deeper tests also check that drop pawn mates don't exist
  @Test
  public void perftMaxMovesDepth3() {
    Position position = Position.createPositionFromSfen(MAX_MOVES_POSITION_SFEN);
    PerftResult result = perftWithTime(position, 3, true);
    Assertions.assertEquals(53393368, result.getNodes());
    Assertions.assertEquals(197899, result.getCaptures());
    Assertions.assertEquals(4875102, result.getPromotions());
    Assertions.assertEquals(3493971, result.getChecks());
    Assertions.assertEquals(566203, result.getCheckmates());
  }

  @Test
  @Disabled
  public void perftMaxMovesDepth4() {
    Position position = Position.createPositionFromSfen(MAX_MOVES_POSITION_SFEN);
    PerftResult result = perftWithTime(position, 4, true);
    Assertions.assertEquals(9342410965L, result.getNodes());
    Assertions.assertEquals(60043133, result.getCaptures());
    Assertions.assertEquals(19957096, result.getPromotions());
    Assertions.assertEquals(335329926, result.getChecks());
    Assertions.assertEquals(52, result.getCheckmates());
  }
}

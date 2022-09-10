package com.github.hayanige.shogilib;

import static com.github.hayanige.shogilib.Square.SQ_76;
import static com.github.hayanige.shogilib.bitboard.Bitboard.and;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.ALL_BB;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.FILE7_BB;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.RANK6_BB;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.getFileBitboard;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.getRankBitboard;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.getSquareBitboard;

import com.github.hayanige.shogilib.bitboard.Bitboard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestBitboard {

  @Test
  public void testBitboardOperations() {
    Assertions.assertEquals(getSquareBitboard(SQ_76), and(FILE7_BB, RANK6_BB));

    Bitboard bitboard = new Bitboard();
    Assertions.assertTrue(bitboard.isZero());

    for (File file: File.getFiles()) {
      bitboard.or(getFileBitboard(file));
    }
    Assertions.assertEquals(ALL_BB, bitboard);

    for (Rank rank: Rank.getRanks()) {
      bitboard.and(getRankBitboard(rank));
    }
    Assertions.assertTrue(bitboard.isZero());
  }
}

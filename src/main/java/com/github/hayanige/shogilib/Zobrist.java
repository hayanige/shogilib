package com.github.hayanige.shogilib;

import static com.github.hayanige.shogilib.Piece.PIECES_LENGTH;
import static com.github.hayanige.shogilib.Square.SQUARES_LENGTH;

import java.util.Random;

public final class Zobrist {

  private Zobrist() {}

  // the seed number is the publication date
  private static final Random random = new Random(20220829);

  // zobrist Keys when a piece is placed in a certain square
  private static final long[][] board = new long[PIECES_LENGTH][SQUARES_LENGTH];

  /**
   * Zobrist Key for the side to move. Only the left most bit is 1.
   * This key is applied only when the side to move is white. Therefore,
   * - if the side to move is black(Sente), the zobrist hash of the position is a positive number.
   * - if the side to move is white(Gote), the zobrist hash of the position is a negative number.
   *
   * This is a similar idea of the following article.
   * https://yaneuraou.yaneu.com/2015/12/17/%e9%80%a3%e8%bc%89%e3%82%84%e3%81%ad%e3%81%86%e3%82%89%e7%8e%8bmini%e3%81%a7%e9%81%8a%e3%81%bc%e3%81%86%ef%bc%818%e6%97%a5%e7%9b%ae/
   */
  public static final long sideKey = 1L << 63;

  static {
    for (int piece = 0; piece < PIECES_LENGTH; piece++) {
      for (int square = 0; square < SQUARES_LENGTH; square++) {
        board[piece][square] = next();
      }
    }
  }

  // generates random long value
  private static long next() {
    byte[] bytes = new byte[16];
    random.nextBytes(bytes);
    long hash = 0L;
    for (int i = 0; i < bytes.length; i++) {
      hash ^= ((long) (bytes[i] & 0xFF)) << ((i * 8) % 64);
    }

    // the left most bit is reserved for the side
    return hash & ~sideKey;
  }

  /**
   * Returns a Zobrist Key base on the piece and the square.
   *
   * @param piece piece that exists on the square
   * @param square square that the piece exists
   * @return  the zobrist key
   */
  public static long getBoardKey(Piece piece, Square square) {
    return board[piece.ordinal()][square.ordinal()];
  }
}

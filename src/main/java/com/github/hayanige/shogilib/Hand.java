package com.github.hayanige.shogilib;

import static com.github.hayanige.shogilib.PieceType.*;

/**
 * The Hand. A Hand is a set of captured pieces.
 * It is called Mochigoma in Japanese.
 */
public class Hand {

  // 片方の持ち駒を 32bit で表現
  // 歩の枚数を8bit、香・桂・銀・角・飛・金を4bitずつで持つ（なのはのアイデアらしい）
  private int hand = 0;

  // bit position of the piece
  private static final int[] PIECE_BITS = new int[] {
      0,
      0,  // PAWN
      8,  // LANCE
      12, // KNIGHT
      16, // SILVER
      20, // BISHOP
      24, // ROOK
      28  // GOLD
  };

  // table from PieceType to the hand
  private static final int[] PIECE_TO_HAND = new int[] {
      0,
      1 << PIECE_BITS[PAWN.ordinal()],
      1 << PIECE_BITS[LANCE.ordinal()],
      1 << PIECE_BITS[KNIGHT.ordinal()],
      1 << PIECE_BITS[SILVER.ordinal()],
      1 << PIECE_BITS[BISHOP.ordinal()],
      1 << PIECE_BITS[ROOK.ordinal()],
      1 << PIECE_BITS[GOLD.ordinal()],
  };

  private static final int[] PIECE_BIT_MASK = new int[] {
      0,
      31, // PAWN
      7,  // LANCE
      7,  // KNIGHT
      7,  // SILVER
      3,  // BISHOP
      3,  // ROOK
      7   // GOLD
  };

  private static final int[] PIECE_BIT_MASK2 = new int[] {
      0,
      PIECE_BIT_MASK[PAWN.ordinal()]   << PIECE_BITS[PAWN.ordinal()],
      PIECE_BIT_MASK[LANCE.ordinal()]  << PIECE_BITS[LANCE.ordinal()],
      PIECE_BIT_MASK[KNIGHT.ordinal()] << PIECE_BITS[KNIGHT.ordinal()],
      PIECE_BIT_MASK[SILVER.ordinal()] << PIECE_BITS[SILVER.ordinal()],
      PIECE_BIT_MASK[BISHOP.ordinal()] << PIECE_BITS[BISHOP.ordinal()],
      PIECE_BIT_MASK[ROOK.ordinal()]   << PIECE_BITS[ROOK.ordinal()],
      PIECE_BIT_MASK[GOLD.ordinal()]   << PIECE_BITS[GOLD.ordinal()]
  };

  // The pieces are always listed in the order rook, bishop, gold, silver, knight, lance, pawn;
  // https://web.archive.org/web/20080131070731/http://www.glaurungchess.com/shogi/usi.html
  private static final PieceType[] PIECE_USI_ORDER = new PieceType[] {
      ROOK, BISHOP, GOLD, SILVER, KNIGHT, LANCE, PAWN
  };

  /**
   * Is there any piece in the hand?
   *
   * @return  true if there is at least one piece.
   */
  public boolean isZero() {
    return hand == 0;
  }

  /**
   * The number of the given piece.
   *
   * @param pr  raw piece (not colored and not promoted)
   * @return  the number of the piece
   */
  public int count(final PieceType pr) {
    assert !pr.isPromoted();
    return (hand >> PIECE_BITS[pr.ordinal()]) & PIECE_BIT_MASK[pr.ordinal()];
  }

  /**
   * Does the given piece exist in the hand?
   *
   * @param pr  raw piece (not colored and not promoted)
   * @return  true if the piece exists
   */
  public boolean exists(final PieceType pr) {
    assert !pr.isPromoted();
    return (hand & PIECE_BIT_MASK2[pr.ordinal()]) != 0;
  }

  /**
   * Are there any pieces in the hand other than Pawn?
   *
   * @return  true if the other piece exists
   */
  public boolean existsExceptPawn() {
    return (hand & ~PIECE_BIT_MASK2[PAWN.ordinal()]) != 0;
  }

  /**
   * Add the given piece to the hand.
   *
   * @param pr  raw piece (not colored and not promoted)
   */
  public void add(final PieceType pr) {
    assert !pr.isPromoted();
    if (pr == KING) {
      return;
    }
    hand += PIECE_TO_HAND[pr.ordinal()];
  }

  /**
   * Subtract the given piece from the hand.
   *
   * @param pr  raw piece (not colored and not promoted)
   */
  public void subtract(final PieceType pr) {
    assert !pr.isPromoted();
    if (pr == KING) {
      return;
    }
    hand -= PIECE_TO_HAND[pr.ordinal()];
  }

  @Override
  public int hashCode() {
    return hand;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < PIECE_USI_ORDER.length; i++) {
      int count = count(PIECE_USI_ORDER[i]);
      if (count == 1) {
        sb.append(PIECE_USI_ORDER[i]);
      } else if (count > 1) {
        sb.append(count).append(PIECE_USI_ORDER[i]);
      }
    }
    return sb.toString();
  }

  /**
   * Returns the pretty output in Japanese
   *
   * @return the pretty output in Japanese
   */
  public String pretty() {
    return PAWN.pretty() + ":" + count(PAWN) + ", "
        + LANCE.pretty() + ":" + count(LANCE) + ", "
        + KNIGHT.pretty() + ":" + count(KNIGHT) + ", "
        + SILVER.pretty() + ":" + count(SILVER) + ", "
        + GOLD.pretty() + ":" + count(GOLD) + ", "
        + BISHOP.pretty() + ":" + count(BISHOP) + ", "
        + ROOK.pretty() + ":" + count(ROOK);
  }
}

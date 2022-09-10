package com.github.hayanige.shogilib.bitboard;

import com.github.hayanige.shogilib.Square;

/**
 * Bitboard
 *
 * <pre>
 * lower bits
 *    9  8  7  6  5  4  3  2  1
 * +----------------------------+
 * |  -  - 54 45 36 27 18  9  0 | 一
 * |  -  - 55 46 37 28 19 10  1 | 二
 * |  -  - 56 47 38 29 20 11  2 | 三
 * |  -  - 57 48 39 30 21 12  3 | 四
 * |  -  - 58 49 40 31 22 13  4 | 五
 * |  -  - 59 50 41 32 23 14  5 | 六
 * |  -  - 60 51 42 33 24 15  6 | 七
 * |  -  - 61 52 43 34 25 16  7 | 八
 * |  -  - 62 53 44 35 26 17  8 | 九
 * +----------------------------+
 *
 * upper bits
 *    9  8  7  6  5  4  3  2  1
 * +----------------------------+
 * |  9  0 　-  -  -  -  -  -  -| 一
 * | 10  1 　-  -  -  -  -  -  -| 二
 * | 11  2 　-  -  -  -  -  -  -| 三
 * | 12  3 　-  -  -  -  -  -  -| 四
 * | 13  4 　-  -  -  -  -  -  -| 五
 * | 14  5 　-  -  -  -  -  -  -| 六
 * | 15  6 　-  -  -  -  -  -  -| 七
 * | 16  7 　-  -  -  -  -  -  -| 八
 * | 17  8 　-  -  -  -  -  -  -| 九
 * +----------------------------+
 * </pre>
 */
public class Bitboard {

  public static final int LOWER_LENGTH = 63;
  public static final int UPPER_LENGTH = 18;

  private long p0;  // lower bits
  private long p1;  // upper bits

  public Bitboard() {
    this(0, 0);
  }

  public Bitboard(final long p0, final long p1) {
    this.p0 = p0;
    this.p1 = p1;
  }

  /**
   * AND operation of this Bitboard and the given Bitboard.
   * This bitboard is overwritten by the result of the operation.
   * Returns this bitboard.
   *
   * @param b the given bitboard
   * @return  the result bitboard (this bitboard)
   */
  public Bitboard and(final Bitboard b) {
    p0 &= b.p0;
    p1 &= b.p1;
    return this;
  }

  /**
   * OR operation of this Bitboard and the given Bitboard.
   * This bitboard is overwritten by the result of the operation.
   * Returns this bitboard.
   *
   * @param b the given bitboard
   * @return  the result bitboard (this bitboard)
   */
  public Bitboard or(final Bitboard b) {
    p0 |= b.p0;
    p1 |= b.p1;
    return this;
  }

  /**
   * XOR operation of this Bitboard and the given Bitboard.
   * This bitboard is overwritten by the result of the operation.
   * Returns this bitboard.
   *
   * @param b the given bitboard
   * @return  the result bitboard (this bitboard)
   */
  public Bitboard xor(final Bitboard b) {
    p0 ^= b.p0;
    p1 ^= b.p1;
    return this;
  }

  /**
   * AND-NOT operation of this Bitboard and the given Bitboard.
   * This bitboard is overwritten by the result of the operation.
   * Returns this bitboard.
   *
   * @param b the given bitboard
   * @return  the result bitboard (this bitboard)
   */
  public Bitboard andNot(final Bitboard b) {
    p0 &= ~b.p0;
    p1 &= ~b.p1;
    return this;
  }

  /**
   * Right shift operation of this bitboard.
   *
   * @return  the result bitboard (this bitboard)
   */
  public Bitboard rightShift() {
    p0 >>= 1;
    p1 >>= 1;
    return this;
  }

  /**
   * Left shift operation of this bitboard.
   *
   * @return  the result bitboard (this bitboard)
   */
  public Bitboard leftShift() {
    p0 <<= 1;
    p1 <<= 1;
    return this;
  }

  /**
   * Is there a next bit(piece)?
   *
   * @return true if there is
   */
  public boolean hasNext() {
    return p0 != 0 || p1 != 0;
  }

  /**
   * Returns the Square of the smallest bit number where it is 1 and set the bit to 0
   *
   * @return  the Square of the smallest bit number where it is 1
   */
  public Square getNextSquare() {
    final int number;
    if (p0 != 0) {
      number = Long.numberOfTrailingZeros(p0);
      p0 = p0 & (p0 - 1);
    } else {
      number = Long.numberOfTrailingZeros(p1) + LOWER_LENGTH;
      p1 = p1 & (p1 - 1);
    }

    return Square.valueOf(number);
  }

  /**
   * Returns a new copy instance of this bitboard.
   *
   * @return  a new copy instance of this bitboard
   */
  public Bitboard newInstance() {
    return new Bitboard(p0, p1);
  }

  /**
   * Returns the result of XOR of the upper bits and the lower bits.
   * This method is used when calculating Magic Bitboard.
   *
   * @return  the result of xor of the upper bits and the lower bits
   */
  public long merge() {
    return p0 ^ p1;
  }

  /**
   * Is the bitboard zero?
   *
   * @return  true if there is no 1 bit
   */
  public boolean isZero() {
    return p0 == 0 && p1 == 0;
  }

  /**
   * Returns the AND result of two specified bitboards as a new bitboard.
   *
   * @param b1  a bitboard
   * @param b2  a bitboard
   * @return  the new bitboard of the AND result of the specified bitboards
   */
  public static Bitboard and(final Bitboard b1, final Bitboard b2) {
    return new Bitboard(b1.p0 & b2.p0, b1.p1 & b2.p1);
  }

  /**
   * Returns the OR result of two specified bitboards as a new bitboard.
   *
   * @param b1  a bitboard
   * @param b2  a bitboard
   * @return  the new bitboard of the OR result of the specified bitboards
   */
  public static Bitboard or(final Bitboard b1, final Bitboard b2) {
    return new Bitboard(b1.p0 | b2.p0, b1.p1 | b2.p1);
  }

  /**
   * Returns the XOR result of two specified bitboards as a new bitboard.
   *
   * @param b1  a bitboard
   * @param b2  a bitboard
   * @return  the new bitboard of the XOR result of the specified bitboards
   */
  public static Bitboard xor(final Bitboard b1, final Bitboard b2) {
    return new Bitboard(b1.p0 ^ b2.p0, b1.p1 ^ b2.p1);
  }

  /**
   * Returns the NOT result of the specified bitboard as a new bitboard.
   *
   * @param bitboard  a bitboard
   * @return  the new bitboard of the NOT result of the specified bitboard
   */
  public static Bitboard not(final Bitboard bitboard) {
    return new Bitboard(~bitboard.p0, ~bitboard.p1);
  }
  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Bitboard)) {
      return false;
    }

    Bitboard b = (Bitboard) o;
    return p0 == b.p0 && p1 == b.p1;
  }

  // the layout of the output is the same as the shogi board
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 9; i++) {
      sb.append(p1 >> (9+i) & 1).append(p1 >> i & 1);
      for (int j = 6; j >= 0; j--) {
        sb.append(p0 >> (9*j+i) & 1);
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}

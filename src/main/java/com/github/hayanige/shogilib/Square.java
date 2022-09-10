package com.github.hayanige.shogilib;

import static com.github.hayanige.shogilib.File.FILE_1;
import static com.github.hayanige.shogilib.File.FILE_2;
import static com.github.hayanige.shogilib.File.FILE_3;
import static com.github.hayanige.shogilib.File.FILE_4;
import static com.github.hayanige.shogilib.File.FILE_5;
import static com.github.hayanige.shogilib.File.FILE_6;
import static com.github.hayanige.shogilib.File.FILE_7;
import static com.github.hayanige.shogilib.File.FILE_8;
import static com.github.hayanige.shogilib.File.FILE_9;
import static com.github.hayanige.shogilib.Rank.RANKS_LENGTH;
import static com.github.hayanige.shogilib.Rank.RANK_1;
import static com.github.hayanige.shogilib.Rank.RANK_2;
import static com.github.hayanige.shogilib.Rank.RANK_3;
import static com.github.hayanige.shogilib.Rank.RANK_4;
import static com.github.hayanige.shogilib.Rank.RANK_5;
import static com.github.hayanige.shogilib.Rank.RANK_6;
import static com.github.hayanige.shogilib.Rank.RANK_7;
import static com.github.hayanige.shogilib.Rank.RANK_8;
import static com.github.hayanige.shogilib.Rank.RANK_9;

import java.util.List;

/**
 * The Squares in the Shogi board.
 *
 * <pre>
 * Ordinal numbers and squares correspondence table
 *     9  8  7  6  5  4  3  2  1
 *  +----------------------------+
 *  | 72 63 54 45 36 27 18  9  0 | 一
 *  | 73 64 55 46 37 28 19 10  1 | 二
 *  | 74 65 56 47 38 29 20 11  2 | 三
 *  | 75 66 57 48 39 30 21 12  3 | 四
 *  | 76 67 58 49 40 31 22 13  4 | 五
 *  | 77 68 59 50 41 32 23 14  5 | 六
 *  | 78 69 60 51 42 33 24 15  6 | 七
 *  | 79 70 61 52 43 34 25 16  7 | 八
 *  | 80 71 62 53 44 35 26 17  8 | 九
 *  +----------------------------+
 * </pre>
 *
 */
public enum Square {

  SQ_11(FILE_1, RANK_1), SQ_12(FILE_1, RANK_2), SQ_13(FILE_1, RANK_3), SQ_14(FILE_1, RANK_4), SQ_15(FILE_1, RANK_5), SQ_16(FILE_1, RANK_6), SQ_17(FILE_1, RANK_7), SQ_18(FILE_1, RANK_8), SQ_19(FILE_1, RANK_9),
  SQ_21(FILE_2, RANK_1), SQ_22(FILE_2, RANK_2), SQ_23(FILE_2, RANK_3), SQ_24(FILE_2, RANK_4), SQ_25(FILE_2, RANK_5), SQ_26(FILE_2, RANK_6), SQ_27(FILE_2, RANK_7), SQ_28(FILE_2, RANK_8), SQ_29(FILE_2, RANK_9),
  SQ_31(FILE_3, RANK_1), SQ_32(FILE_3, RANK_2), SQ_33(FILE_3, RANK_3), SQ_34(FILE_3, RANK_4), SQ_35(FILE_3, RANK_5), SQ_36(FILE_3, RANK_6), SQ_37(FILE_3, RANK_7), SQ_38(FILE_3, RANK_8), SQ_39(FILE_3, RANK_9),
  SQ_41(FILE_4, RANK_1), SQ_42(FILE_4, RANK_2), SQ_43(FILE_4, RANK_3), SQ_44(FILE_4, RANK_4), SQ_45(FILE_4, RANK_5), SQ_46(FILE_4, RANK_6), SQ_47(FILE_4, RANK_7), SQ_48(FILE_4, RANK_8), SQ_49(FILE_4, RANK_9),
  SQ_51(FILE_5, RANK_1), SQ_52(FILE_5, RANK_2), SQ_53(FILE_5, RANK_3), SQ_54(FILE_5, RANK_4), SQ_55(FILE_5, RANK_5), SQ_56(FILE_5, RANK_6), SQ_57(FILE_5, RANK_7), SQ_58(FILE_5, RANK_8), SQ_59(FILE_5, RANK_9),
  SQ_61(FILE_6, RANK_1), SQ_62(FILE_6, RANK_2), SQ_63(FILE_6, RANK_3), SQ_64(FILE_6, RANK_4), SQ_65(FILE_6, RANK_5), SQ_66(FILE_6, RANK_6), SQ_67(FILE_6, RANK_7), SQ_68(FILE_6, RANK_8), SQ_69(FILE_6, RANK_9),
  SQ_71(FILE_7, RANK_1), SQ_72(FILE_7, RANK_2), SQ_73(FILE_7, RANK_3), SQ_74(FILE_7, RANK_4), SQ_75(FILE_7, RANK_5), SQ_76(FILE_7, RANK_6), SQ_77(FILE_7, RANK_7), SQ_78(FILE_7, RANK_8), SQ_79(FILE_7, RANK_9),
  SQ_81(FILE_8, RANK_1), SQ_82(FILE_8, RANK_2), SQ_83(FILE_8, RANK_3), SQ_84(FILE_8, RANK_4), SQ_85(FILE_8, RANK_5), SQ_86(FILE_8, RANK_6), SQ_87(FILE_8, RANK_7), SQ_88(FILE_8, RANK_8), SQ_89(FILE_8, RANK_9),
  SQ_91(FILE_9, RANK_1), SQ_92(FILE_9, RANK_2), SQ_93(FILE_9, RANK_3), SQ_94(FILE_9, RANK_4), SQ_95(FILE_9, RANK_5), SQ_96(FILE_9, RANK_6), SQ_97(FILE_9, RANK_7), SQ_98(FILE_9, RANK_8), SQ_99(FILE_9, RANK_9);

  private static final Square[] SQUARES = Square.values();
  public static final int SQUARES_LENGTH = SQUARES.length;

  private final File file;
  private final Rank rank;

  Square(File file, Rank rank) {
    this.file = file;
    this.rank = rank;
  }

  /**
   * Returns the file of the square.
   *
   * @return the file of the square
   */
  public File getFile() {
    return file;
  }

  /**
   * Returns the rank of the square.
   *
   * @return  the rank of the square
   */
  public Rank getRank() {
    return rank;
  }

  /**
   * Can a piece of the given color promote in this square?
   *
   * @param color the color
   * @return true if it can promote
   */
  public boolean canPromote(final Color color) {
    if (color.equals(Color.BLACK)) {
      return getRank().ordinal() <= 2;
    } else  {
      return getRank().ordinal() >= 6;
    }
  }

  /**
   * Returns the next square from the current square and the given direction.
   *
   * @param direction direction to the next square
   * @return  the next square
   */
  public Square getNextSquare(final Direction direction) {
    if (isValidOrdinal(ordinal() + direction.delta)) {
      Square nextSquare = SQUARES[ordinal() + direction.delta];

      // if the distance is more than 2, the move is invalid.
      if (getDistance(nextSquare) <= 2) {
        return nextSquare;
      }
    }

    return null;
  }

  /**
   * Returns the square given its file and its rank.
   *
   * @param file  file of the square
   * @param rank  rank of the square
   * @return  the square
   */
  public static Square valueOf(final File file, final Rank rank) {
    return SQUARES[file.ordinal() * RANKS_LENGTH + rank.ordinal()];
  }

  /**
   * Returns the square given its ordinal.
   *
   * @param ordinal ordinal of the square
   * @return  the square
   */
  public static Square valueOf(final int ordinal) {
    return SQUARES[ordinal];
  }

  /**
   * Returns a square given its USI string.
   *
   * @param usi usi string of the square
   * @return  the square (null if the usi is invalid)
   */
  public static Square parseUSI(final String usi) {
    File file = File.parseUSI(usi.substring(0, 1));
    Rank rank = Rank.parseUSI(usi.substring(1, 2));
    if (file != null && rank != null) {
      return Square.valueOf(file, rank);
    } else {
      return null;
    }
  }

  /**
   * Returns the array containing all squares in ordinal order.
   *
   * @return the list containing all squares
   */
  public static List<Square> getSquares() {
    return List.of(SQUARES);
  }

  // Is the given ordinal number within the squares?
  private static boolean isValidOrdinal(final int ordinal) {
    return  0 <= ordinal && ordinal <= 80;
  }

  // Returns the distance(infinity norm) between this square and the given square.
  private int getDistance(final Square nextSquare) {
    int distFile = Math.abs(getFile().ordinal() - nextSquare.getFile().ordinal());
    int distRank = Math.abs(getRank().ordinal() - nextSquare.getRank().ordinal());
    return Math.max(distFile, distRank);
  }

  /**
   * Returns the USI string of the square.
   *
   * @return the USI string
   */
  @Override
  public String toString() {
    return getFile().toString() + getRank().toString();
  }

  /**
   * Returns the Japanese string of the square.
   *
   * @return the Japanese string
   */
  public String pretty() {
    return getFile().pretty() + getRank().pretty();
  }

  /**
   * The Direction. These are a combination of north, east, south and west.
   */
  public enum Direction {
    N(SQ_54.ordinal() - SQ_55.ordinal()),
    E(SQ_45.ordinal() - SQ_55.ordinal()),
    S(SQ_56.ordinal() - SQ_55.ordinal()),
    W(SQ_65.ordinal() - SQ_55.ordinal()),
    NE(SQ_44.ordinal() - SQ_55.ordinal()),
    SE(SQ_46.ordinal() - SQ_55.ordinal()),
    SW(SQ_66.ordinal() - SQ_55.ordinal()),
    NW(SQ_64.ordinal() - SQ_55.ordinal()),
    NNE(SQ_43.ordinal() - SQ_55.ordinal()),
    SSE(SQ_47.ordinal() - SQ_55.ordinal()),
    SSW(SQ_67.ordinal() - SQ_55.ordinal()),
    NNW(SQ_63.ordinal() - SQ_55.ordinal());

    // delta between the origin square and the destination square
    private final int delta;

    Direction(int delta) {
      this.delta = delta;
    }
  }
}

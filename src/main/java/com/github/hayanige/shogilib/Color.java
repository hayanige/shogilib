package com.github.hayanige.shogilib;

import java.util.List;

/**
 * One of the two sides, Sente(the 1st player) or Gote(the 2nd player) in Shogi.
 * Note that Sente is black and Gote is white. This is the opposite in chess.
 */
public enum Color {
  BLACK,
  WHITE;

  private static final Color[] COLORS = Color.values();
  public static final int COLORS_LENGTH = COLORS.length;

  /**
   * Returns the opposite of this color.
   *
   * @return the opposite color
   */
  public Color getOpponent() {
    return COLORS[ordinal()^1];
  }

  /**
   * Returns the array including two colors.
   *
   * @return the list including two colors
   */
  public static List<Color> getColors() {
    return List.of(COLORS);
  }

  /**
   * Returns a color given its USI string.
   *
   * @param usi USI string of the color
   * @return  the color (null if the usi is invalid)
   */
  public static Color parseUSI(final String usi) {
    if (usi.equals("b")) {
      return BLACK;
    } else if (usi.equals("w")) {
      return WHITE;
    } else {
      return null;
    }
  }

  /**
   * Returns the USI string of the color.
   *
   * @return the USI string
   */
  @Override
  public String toString() {
    if (this == BLACK) {
      return "b";
    } else {
      return "w";
    }
  }

  /**
   * Returns the Japanese string of the color.
   *
   * @return the Japanese string
   */
  public String pretty() {
    if (this == BLACK) {
      return "先手";
    } else {
      return "後手";
    }
  }
}

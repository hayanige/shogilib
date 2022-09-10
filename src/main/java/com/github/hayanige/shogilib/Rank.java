package com.github.hayanige.shogilib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ranks in a board. It is called Suji in Japanese.
 * A <i>rank</i> is a raw in the board, and it is identified from 一(1) to 九(9).
 */
public enum Rank {
  RANK_1("a", "一"),
  RANK_2("b", "二"),
  RANK_3("c", "三"),
  RANK_4("d", "四"),
  RANK_5("e", "五"),
  RANK_6("f", "六"),
  RANK_7("g", "七"),
  RANK_8("h", "八"),
  RANK_9("i", "九");

  private final String usiString;
  private final String prettyString;

  private static final Rank[] RANKS = Rank.values();
  public static final int RANKS_LENGTH = RANKS.length;

  private static final Map<String, Rank> usi2Rank = new HashMap<>();
  static {
    for (Rank rank: RANKS) {
      usi2Rank.put(rank.usiString, rank);
    }
  }

  Rank(final String usiString, final String prettyString) {
    this.usiString = usiString;
    this.prettyString = prettyString;
  }

  /**
   * Returns the array containing all ranks in ordinal order.
   *
   * @return the list containing all ranks
   */
  public static List<Rank> getRanks() {
    return List.of(RANKS);
  }

  /**
   * Returns a rank given its ordinal.
   *
   * @param ordinal ordinal of the rank
   * @return  the rank
   */
  public static Rank valueOf(final int ordinal) {
    return RANKS[ordinal];
  }

  /**
   * Returns a rank given its USI string.
   *
   * @param usi usi string of the rank
   * @return  the rank (null if the usi is invalid)
   */
  public static Rank parseUSI(final String usi) {
    return usi2Rank.get(usi);
  }

  /**
   * Returns the USI string of the rank.
   *
   * @return the USI string
   */
  @Override
  public String toString() {
    return usiString;
  }

  /**
   * Returns the Japanese string of the rank.
   *
   * @return the Japanese string
   */
  public String pretty() {
    return prettyString;
  }
}

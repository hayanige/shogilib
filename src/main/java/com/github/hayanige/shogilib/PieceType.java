package com.github.hayanige.shogilib;

/**
 * The piece types without considering color.
 */
public enum PieceType {

  NONE(" ", "口"),
  PAWN("P", "歩"),
  LANCE("L", "香"),
  KNIGHT("N", "桂"),
  SILVER("S", "銀"),
  BISHOP("B", "角"),
  ROOK("R", "飛"),
  GOLD("G", "金"),
  KING("K", "王"),
  PRO_PAWN("+P", "と"),
  PRO_LANCE("+L", "杏"),
  PRO_KNIGHT("+K", "圭"),
  PRO_SILVER("+S", "全"),
  HORSE("+B", "馬"),
  DRAGON("+R", "龍");

  private static final PieceType[] PIECE_TYPES = PieceType.values();
  public static final int PIECE_RAW_NB = KING.ordinal(); // the last ordinal of non-promoted piece

  private final String usiString;
  private final String prettyString;

  PieceType(final String usiString, final String prettyString) {
    this.usiString = usiString;
    this.prettyString = prettyString;
  }

  /**
   * Returns the colored piece given its color and this piece type.
   *
   * @param color color of the piece
   * @return the colored piece
   */
  public Piece getColoredPiece(final Color color) {
    return Piece.valueOf(color, this);
  }

  /**
   * Is the piece type promoted?
   *
   * @return  true if it is promoted
   */
  public boolean isPromoted() {
    if (ordinal() <= PIECE_RAW_NB) {
      return false;
    } else {
      return this != NONE;
    }
  }

  /**
   * Returns the piece type given its ordinal.
   *
   * @param ordinal ordinal of the piece type
   * @return  the piece type
   */
  public static PieceType valueOf(final int ordinal) {
    return PIECE_TYPES[ordinal];
  }

  /**
   * Returns the USI string of the piece type.
   *
   * @return the USI string
   */
  @Override
  public String toString() {
    return usiString;
  }

  /**
   * Returns the Japanese string of the piece type.
   *
   * @return the Japanese string
   */
  public String pretty() {
    return prettyString;
  }
}

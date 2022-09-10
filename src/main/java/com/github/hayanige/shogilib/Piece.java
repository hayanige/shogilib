package com.github.hayanige.shogilib;

import static com.github.hayanige.shogilib.Color.BLACK;
import static com.github.hayanige.shogilib.Color.WHITE;
import static com.github.hayanige.shogilib.Square.*;
import static com.github.hayanige.shogilib.Square.Direction.*;
import static com.github.hayanige.shogilib.PieceType.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The colored pieces.
 */
public enum Piece {

  NO_PIECE(null, NONE, NONE, ".", "口", null, null),
  B_PAWN(BLACK, PAWN, PAWN, "P", "歩", new Direction[]{ N }, new Direction[]{}),
  B_LANCE(BLACK, LANCE, LANCE, "L", "香", new Direction[]{}, new Direction[]{ N }),
  B_KNIGHT(BLACK, KNIGHT, KNIGHT, "N", "桂", new Direction[]{ NNE, NNW }, new Direction[]{}),
  B_SILVER(BLACK, SILVER, SILVER, "S", "銀", new Direction[]{ N, NE, SE, SW, NW }, new Direction[]{}),
  B_BISHOP(BLACK, BISHOP, BISHOP, "B", "角", new Direction[]{}, new Direction[]{ NE, SE, SW, NW }),
  B_ROOK(BLACK, ROOK, ROOK, "R", "飛", new Direction[]{}, new Direction[]{ N, E, S, W }),
  B_GOLD(BLACK, GOLD, GOLD, "G", "金", new Direction[]{ N, NE, E, S, W, NW }, new Direction[]{}),
  B_KING(BLACK, KING, KING, "K", "王", new Direction[]{ N, NE, E, SE, S, SW, W, NW }, new Direction[]{}),
  B_PRO_PAWN(BLACK, PRO_PAWN, PAWN, "+P", "と", new Direction[]{ N, NE, E, S, W, NW }, new Direction[]{} ),
  B_PRO_LANCE(BLACK, PRO_LANCE, LANCE, "+L", "杏", new Direction[]{ N, NE, E, S, W, NW }, new Direction[]{}),
  B_PRO_KNIGHT(BLACK, PRO_KNIGHT, KNIGHT, "+K", "圭", new Direction[]{ N, NE, E, S, W, NW }, new Direction[]{}),
  B_PRO_SILVER(BLACK, PRO_SILVER, SILVER, "+S", "全", new Direction[]{ N, NE, E, S, W, NW }, new Direction[]{}),
  B_HORSE(BLACK, HORSE, BISHOP, "+B", "馬", new Direction[] { N, E, S, W }, new Direction[]{ NE, SE, SW, NW }),
  B_DRAGON(BLACK, DRAGON, ROOK, "+R", "龍", new Direction[]{ NE, SE, SW, NW }, new Direction[]{ N, E, S, W }),
  W_PAWN(WHITE, PAWN, PAWN, "p", "歩", new Direction[]{ S }, new Direction[]{}),
  W_LANCE(WHITE, LANCE, LANCE, "l", "香", new Direction[]{}, new Direction[]{ S }),
  W_KNIGHT(WHITE, KNIGHT, KNIGHT, "n", "桂", new Direction[]{ SSE, SSW }, new Direction[]{}),
  W_SILVER(WHITE, SILVER, SILVER, "s", "銀", new Direction[]{ NE, SE, S, SW, NW}, new Direction[]{}),
  W_BISHOP(WHITE, BISHOP, BISHOP, "b", "角", new Direction[]{}, new Direction[]{ NE, SE, SW, NW }),
  W_ROOK(WHITE, ROOK, ROOK, "r", "飛", new Direction[]{}, new Direction[]{ N, E, S, W }),
  W_GOLD(WHITE, GOLD, GOLD, "g", "金", new Direction[]{ N, E, SE, S, SW, W }, new Direction[]{}),
  W_KING(WHITE, KING, KING, "k", "王", new Direction[]{ N, NE, E, SE, S, SW, W, NW }, new Direction[]{}),
  W_PRO_PAWN(WHITE, PRO_PAWN, PAWN, "+p", "と", new Direction[]{ N, E, SE, S, SW, W }, new Direction[]{}),
  W_PRO_LANCE(WHITE, PRO_LANCE, LANCE, "+l", "杏", new Direction[]{ N, E, SE, S, SW, W }, new Direction[]{}),
  W_PRO_KNIGHT(WHITE, PRO_KNIGHT, KNIGHT, "+k", "圭", new Direction[]{ N, E, SE, S, SW, W }, new Direction[]{}),
  W_PRO_SILVER(WHITE, PRO_SILVER, SILVER, "+s", "全", new Direction[]{ N, E, SE, S, SW, W }, new Direction[]{}),
  W_HORSE(WHITE, HORSE, BISHOP, "+b", "馬", new Direction[]{ N, E, S, W }, new Direction[]{ NE, SE, SW, NW }),
  W_DRAGON(WHITE, DRAGON, ROOK, "+r", "龍", new Direction[]{ NE, SE, SW, NW }, new Direction[]{ N, E, S, W });

  private static final Piece[] PIECES = values();
  public static final int PIECES_LENGTH = PIECES.length;

  private final Color color;
  private final PieceType pieceType;
  private final PieceType rawType;
  private final String usiString;
  private final String prettyString;
  private final Direction[] stepMoves;
  private final Direction[] slidingMoves;

  // Adding this constant makes a non-promoted piece the promoted piece.
  private static final int PIECE_PROMOTE = 8;
  // Adding this constant makes a black piece the white piece.
  private static final int PIECE_WHITE = 14;

  Piece(final Color color, final PieceType pieceType, final PieceType rawType,
      final String usiString, final String prettyString, final Direction[] stepMoves,
      final Direction[] slidingMoves) {
    this.color = color;
    this.pieceType = pieceType;
    this.rawType = rawType;
    this.usiString = usiString;
    this.prettyString = prettyString;
    this.stepMoves = stepMoves;
    this.slidingMoves = slidingMoves;
  }

  public Direction[] getStepMoves() {
    return stepMoves;
  }

  public Direction[] getSlidingMoves() {
    return slidingMoves;
  }

  /**
   * Returns the color of the piece.
   *
   * @return  the color
   */
  public Color getColor() {
    return color;
  }

  /**
   * Returns the non-colored piece of the piece.
   *
   * @return  the non-colored piece
   */
  public PieceType getPieceType() {
    return pieceType;
  }

  /**
   * Returns the non-colored and non-promoted piece.
   * It is called a raw piece.
   *
   * e.g. {@link Piece#W_HORSE} becomes {@link PieceType#HORSE}.
   *
   * @return  the raw piece
   */
  public PieceType getRawType() {
    return rawType;
  }

  /**
   * Is the piece promoted?
   *
   * @return  true if it is promoted
   */
  public boolean isPromoted() {
    return pieceType.isPromoted();
  }

  /**
   * Returns the promoted piece of the piece.
   *
   * @return  the promoted piece
   */
  public Piece getPromoted() {
    assert !isPromoted();
    return PIECES[ordinal() + PIECE_PROMOTE];
  }

  /**
   * Returns the reverse-promoted piece of the piece.
   *
   * @return  the reverse-promoted piece
   */
  public Piece getReversePromoted() {
    assert isPromoted();
    return PIECES[ordinal() - PIECE_PROMOTE];
  }

  /**
   * Returns the array containing all pieces in ordinal order.
   *
   * @return the list containing all pieces
   */
  public static List<Piece> getPieces() {
    return List.of(PIECES);
  }

  /**
   * Returns the piece given its color and its piece type.
   *
   * @param color color of the piece
   * @param pt  piece type of the piece
   * @return  the piece
   */
  public static Piece valueOf(final Color color, final PieceType pt) {
    if (color.equals(BLACK)) {
      return PIECES[pt.ordinal()];
    } else {
      return PIECES[pt.ordinal() + PIECE_WHITE];
    }
  }

  private static final Map<String, Piece> usi2Piece = new HashMap<>();
  static {
    for (Piece piece: PIECES) {
      usi2Piece.put(piece.usiString, piece);
    }
  }

  /**
   * Returns the piece given its USI string.
   *
   * @param usi usi string of the piece
   * @return  the piece (null if the usi is invalid)
   */
  public static Piece parseUSI(final String usi) {
    return usi2Piece.get(usi);
  }

  /**
   * Returns the USI string of the piece.
   *
   * @return the USI string
   */
  @Override
  public String toString() {
    return usiString;
  }

  /**
   * Returns the Japanese string of the piece.
   *
   * @return the Japanese string
   */
  public String pretty() {
    return prettyString;
  }
}

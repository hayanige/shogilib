package com.github.hayanige.shogilib;

/**
 * A piece movement.
 */
public class Move implements Comparable {

  private static final int NONE     = 0;            // Invalid move
  private static final int DROP     = 1 << 14;      // Drop move flag
  private static final int PROMOTE  = 1 << 15;      // Promoting move flag
  private static final int NULL     = (1 << 7) + 1; // Null move
  private static final int RESIGN   = (2 << 7) + 2; // Resign
  private static final int WIN      = (3 << 7) + 3; // Declared win when entering of a king

  /*
   * 16-bit representation of a move
   * - bit0-6: the destination square
   * - bit7-13: the origin square or piece type of the drop move
   * - bit14: a drop move?
   * - bit15: a promoting move?
   */
  private final int move;

  private Move() {
    move = NONE;
  }

  private Move(final int move){
    this.move = move;
  }

  /**
   * The Invalid Move
   */
  public static Move MOVE_NONE = new Move(NONE);

  /**
   * The Null Move
   */
  public static Move MOVE_NULL = new Move(NULL);

  /**
   * The Resigning Move
   */
  public static Move MOVE_RESIGN = new Move(RESIGN);

  /**
   * The Winning Move
   */
  public static Move MOVE_WIN = new Move(WIN);

  /**
   * Returns the move given its origin square and its destination square.
   *
   * @param fromSquare  the origin square
   * @param toSquare    the destination square
   * @return the move
   */
  public static Move makeMove(final Square fromSquare, final Square toSquare) {
    return new Move(toSquare.ordinal() + (fromSquare.ordinal() << 7));
  }

  /**
   * Returns the promoting move given its origin square and its destination square.
   *
   * @param fromSquare  the origin square
   * @param toSquare    the destination square
   * @return the promoting move
   */
  public static Move makeMovePromote(final Square fromSquare, final Square toSquare) {
    return new Move(toSquare.ordinal() + (fromSquare.ordinal() << 7) + PROMOTE);
  }

  /**
   * Returns the drop move given its raw piece (not colored and not promoted)
   * and its destination square.
   *
   * @param rawPiece the raw piece of the drop move
   * @param toSquare the destination square
   * @return the drop move
   */
  public static Move makeMoveDrop(final PieceType rawPiece, final Square toSquare) {
    assert rawPiece.ordinal() < PieceType.PIECE_RAW_NB;
    return new Move(toSquare.ordinal() + (rawPiece.ordinal() << 7) + DROP);
  }

  /**
   * Returns the move given its USI string.
   *
   * @param usi usi string of the move
   * @return  the move ({@link Move#MOVE_NONE} if the usi is invalid)
   */
  public static Move makeMoveUSI(final String usi) {
    if (usi.charAt(1) == '*') {
      Piece piece = Piece.parseUSI(String.valueOf(usi.charAt(0)));
      Square to = Square.parseUSI(usi.substring(2, 4));
      if (piece == null || to == null) {
        return MOVE_NONE;
      }
      return Move.makeMoveDrop(piece.getRawType(), to);
    } else {
      Square from = Square.parseUSI(usi.substring(0, 2));
      Square to = Square.parseUSI(usi.substring(2, 4));
      if (from == null || to == null) {
        return MOVE_NONE;
      }
      if (usi.length() == 5 && usi.charAt(4) == '+') {
        return Move.makeMovePromote(from, to);
      } else {
        return Move.makeMove(from, to);
      }
    }
  }

  /**
   * Returns the original square of the move
   *
   * @return the original square
   */
  public Square getFromSquare() {
    assert !isDrop();
    return Square.valueOf((move >> 7) & 0x7f);
  }

  /**
   * Returns the piece type of the drop move
   *
   * @return the piece type
   */
  public PieceType getPieceType() {
    assert isDrop();
    return PieceType.valueOf((move >> 7) & 0x7f);
  }

  /**
   * Returns the destination square of the move
   *
   * @return the destination square
   */
  public Square getToSquare() {
    return Square.valueOf(move & 0x7f);
  }

  /**
   * Is the move a drop move?
   *
   * @return  true if it is a drop move
   */
  public boolean isDrop() {
    return (move & DROP) != 0;
  }

  /**
   * Is the move a promoting move?
   *
   * @return  true if it is a promoting move
   */
  public boolean isPromote() {
    return (move & PROMOTE) != 0;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Move)) {
      return false;
    }

    Move m = (Move)o;
    return m.move == this.move;
  }

  @Override
  public int compareTo(final Object m) {
    return move - ((Move)m).move;
  }

  /**
   * Returns the USI string of the move.
   *
   * @return the USI string
   */
  @Override
  public String toString() {
    if (move == NONE) {
      return "NONE";
    } else if (move == NULL) {
      return "NULL";
    } else if (move == RESIGN) {
      return "RESIGN";
    } else if (move == WIN) {
      return "WIN";
    } else if ((move & DROP) != 0) {
      return getPieceType() + "*" + getToSquare();
    } else if ((move & PROMOTE) != 0) {
      return "" + getFromSquare() + getToSquare() + "+";
    } else {
      return "" + getFromSquare() + getToSquare();
    }
  }

  /**
   * Returns the Japanese string of the move.
   *
   * @return the Japanese string
   */
  public String pretty() {
    if (move == NONE) {
      return "無し";
    } else if (move == NULL) {
      return "パス";
    } else if (move == RESIGN) {
      return "投了";
    } else if (move == WIN) {
      return "勝利宣言";
    } else if ((move & DROP) != 0) {
      return getToSquare().pretty() + getPieceType().pretty() + "打";
    } else if ( (move & PROMOTE) != 0) {
      return getFromSquare().pretty() + getToSquare().pretty() + "成";
    } else {
      return getFromSquare().pretty() + getToSquare().pretty();
    }
  }
}

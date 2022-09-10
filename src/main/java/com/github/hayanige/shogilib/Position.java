package com.github.hayanige.shogilib;

import com.github.hayanige.shogilib.bitboard.PositionBitboardImpl;
import java.util.List;

/**
 * The interface of a shogi board position. A position consists of a board, a side, and two hands.
 * It defines methods to manipulate the board, evolve the position moving pieces around, revert
 * already performed moves, and retrieve the status. Each position is uniquely identified by hashes
 * that could be retrieved using {@link Position#getZobristHash()} methods.
 * Furthermore, it offers a handy way for creating a position from a Shogi Forsyth-Edwards Notation
 * (SFEN) string and exporting it in the same format.
 */
public interface Position {

  /**
   * SFEN of Hirate(starting position)
   */
  String HIRATE_SFEN = "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1";

  /**
   * Returns the next side to move.
   *
   * @return the next side to move
   */
  Color getSideToMove();

  /**
   * Returns the counter of full moves played.
   *
   * @return the counter of full moves
   */
  int getMoveCounter();

  /**
   * Returns the last played move.
   *
   * @return the last played move
   */
  Move getLastMove();

  /**
   * Returns the last captured piece.
   *
   * @return the last captured piece.
   */
  Piece getLastCapturedPiece();

  /**
   * Sets a piece on a square.
   * <p>
   * The operation does not perform any move, but rather simply puts a piece onto a square.
   *
   * @param square  the square the piece has to be set to
   * @param piece the piece to be placed
   */
  void setPiece(Square square, Piece piece);

  /**
   * Unsets a piece on a square.
   * <p>
   * The operation does not perform any move, but rather simply remove a piece from a square.
   *
   * @param square  the square the piece has to be unset from
   */
  void unsetPiece(Square square);

  /**
   * Add a piece to a hand.
   *
   * @param color the color of the hand
   * @param pr  the raw piece(non-colored and non-promoted) to be added
   */
  void addPieceToHand(Color color, PieceType pr);

  /**
   * Subtract a piece from a hand
   *
   * @param color the color of the hand
   * @param pr  the raw piece(non-colored and non-promoted) to be subtracted
   */
  void subtractPieceFromHand(Color color, PieceType pr);

  /**
   * The number of a piece in a hand.
   *
   * @param color the color of the hand
   * @param pr  the raw piece(non-colored and non-promoted)
   * @return  the number of the piece in the hand
   */
  int getNumberOfPieceInHand(Color color, PieceType pr);

  /**
   * Does a given piece exist in a hand?
   *
   * @param color the color of the hand
   * @param pr  the raw piece(non-colored and non-promoted)
   * @return  true if the piece exists
   */
  default boolean isPieceExistInHand(final Color color, final PieceType pr) {
    return getNumberOfPieceInHand(color, pr) != 0;
  }

  /**
   * Executes a move on the board without performing a full validation of the position.
   *
   * @param move the move to execute
   */
  void doMove(Move move);

  /**
   * Reverts the latest move played on the board and returns it.
   */
  Move undoMove();

  /**
   * Returns the list of all possible legal moves for the current position.
   * If such moves are played, it is guaranteed the resulting position will also be legal.
   *
   * @return the list of legal moves available in the current position
   */
  List<Move> getLegalMoves();

  /**
   * Returns the list of all possible pseudo-legal moves for the current position.
   * A pseudo-legal move may leave the king checked. (It is called Oute Hochi in Japanese.)
   *
   * @return the list of pseudo-legal moves available in the current position
   */
  List<Move> getPseudoLegalMoves();

  /**
   * Is the king of the current side checked?
   *
   * @return  true if the king is checked
   */
  boolean isKingAttacked();

  /**
   * Is the position a fourfold repetition?
   *
   * It means if the same position occurs four times during the game.
   * A fourfold repetition is called Sennichite in Japanese.
   *
   * Even if the previous position is a fourfold repetition, it returns false if the current
   * position is not a fourfold repetition.
   *
   * @return true if the current position is a fourfold repetition
   */
  boolean isRepetition();

  /**
   * Verifies in the current position if the king of the side to move is mated.
   *
   * @return {@code true} if the king of the side to move is checkmated
   */
  boolean isMated();

  /**
   * Returns the piece at the specified square.
   *
   * @param square  the square to get the piece from
   * @return  the piece
   */
  Piece getPiece(Square square);

  /**
   * Returns the piece at the specified file and rank.
   *
   * @param file  the file to get the piece from
   * @param rank  the rank to get the piece from
   * @return  the piece
   */
  Piece getPiece(File file, Rank rank);

  /**
   * Generates the Shogi Forsyth-Edwards Notation (SFEN) representation of the current position.
   *
   * @return the string that represents the current position in SFEN notation
   */
  String getSfen();

  /**
   * Returns a Zobrist hash code value for this position. A Zobrist hashing assures the same
   * position returns the same hash value. It is calculated using the position of the pieces,
   * the side to move and the hands.
   *
   * In the current implementation, the left most bit is reserved for the side to move.
   * If the side to move is black(Sente), the zobrist hash of the position is a positive number.
   * If the side to move is white(Gote), the zobrist hash of the position is a negative number.
   * See also: {@link Zobrist}
   *
   * @return a Zobrist hash value for this position
   */
  long getZobristHash();

  /**
   * Returns a human-readable output of the current position with Japanese.
   *
   * @return  a human-readable output with Japanese
   */
  String pretty();

  /**
   * A static factory method returning a new position instance from SFEN.
   *
   * At present, it creates a {@link PositionBitboardImpl} instance.
   * Although the class of the instance may change in the future, users don't need to be aware of
   * the implementation.
   *
   * @param sfen  SFEN
   * @return  a new position from SFEN
   */
  static Position createPositionFromSfen(final String sfen) {
    return PositionBitboardImpl.createPositionFromSfen(sfen);
  }

  /**
   * A static factory method returning a new Hirate position instance.
   *
   * @return  a new Hirate position
   */
  static Position createHiratePosition() {
    return Position.createPositionFromSfen(HIRATE_SFEN);
  }
}

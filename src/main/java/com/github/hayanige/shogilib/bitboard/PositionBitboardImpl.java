package com.github.hayanige.shogilib.bitboard;

import static com.github.hayanige.shogilib.Color.BLACK;
import static com.github.hayanige.shogilib.Color.COLORS_LENGTH;
import static com.github.hayanige.shogilib.Color.WHITE;
import static com.github.hayanige.shogilib.File.FILES_LENGTH;
import static com.github.hayanige.shogilib.Piece.NO_PIECE;
import static com.github.hayanige.shogilib.Piece.PIECES_LENGTH;
import static com.github.hayanige.shogilib.PieceType.BISHOP;
import static com.github.hayanige.shogilib.PieceType.DRAGON;
import static com.github.hayanige.shogilib.PieceType.GOLD;
import static com.github.hayanige.shogilib.PieceType.HORSE;
import static com.github.hayanige.shogilib.PieceType.KING;
import static com.github.hayanige.shogilib.PieceType.KNIGHT;
import static com.github.hayanige.shogilib.PieceType.LANCE;
import static com.github.hayanige.shogilib.PieceType.PAWN;
import static com.github.hayanige.shogilib.PieceType.PRO_KNIGHT;
import static com.github.hayanige.shogilib.PieceType.PRO_LANCE;
import static com.github.hayanige.shogilib.PieceType.PRO_PAWN;
import static com.github.hayanige.shogilib.PieceType.PRO_SILVER;
import static com.github.hayanige.shogilib.PieceType.ROOK;
import static com.github.hayanige.shogilib.PieceType.SILVER;
import static com.github.hayanige.shogilib.Rank.RANKS_LENGTH;
import static com.github.hayanige.shogilib.Square.SQUARES_LENGTH;
import static com.github.hayanige.shogilib.bitboard.Bitboard.and;
import static com.github.hayanige.shogilib.bitboard.Bitboard.or;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.ALL_BB;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.getBishopSlidingEffectBitboard;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.getGoldEffectBitboard;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.getKnightEffectBitboard;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.getSquareBitboard;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.newLanceSlidingEffectBitboard;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.getPawnEffectBitboard;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.getRookSlidingEffectBitboard;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.getSilverEffectBitboard;

import com.github.hayanige.shogilib.Position;
import com.github.hayanige.shogilib.Zobrist;
import com.github.hayanige.shogilib.Color;
import com.github.hayanige.shogilib.File;
import com.github.hayanige.shogilib.Hand;
import com.github.hayanige.shogilib.Move;
import com.github.hayanige.shogilib.Piece;
import com.github.hayanige.shogilib.PieceType;
import com.github.hayanige.shogilib.Rank;
import com.github.hayanige.shogilib.Square;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Position implementation using Bitboard.
 */
public class PositionBitboardImpl implements Position {

  private final Piece[] board;
  private final Hand[] hands;
  private Color sideToMove;
  private final Square[] kingSquare;  // squares where kings exist
  private int moveCounter;
  private static final int MAX_PLY = 2000;  // not consider any case over this max ply for now
  private final Map<Integer, Piece> capturedMap;
  private final Move[] lastMoves; // last moves history
  private long zobristKey;
  private final long[] zobristHistory;
  private int zobristIndex;

  private final Bitboard occupiedBB;    // occupied bitboard of all pieces
  private final Bitboard[] pieceOccupiedBB; // occupied bitboards for each piece
  private final Bitboard[] colorOccupiedBB; // occupied bitboards for each color

  // checker bitboard: checker pieces against the king of the current side to move.
  private Bitboard checkerBB;

  private PositionBitboardImpl() {
    board = new Piece[SQUARES_LENGTH];
    for (int i = 0; i < SQUARES_LENGTH; i++) {
      board[i] = NO_PIECE;
    }
    hands = new Hand[] { new Hand(), new Hand() };
    kingSquare = new Square[COLORS_LENGTH];
    moveCounter = 0;
    capturedMap = new HashMap<>();
    lastMoves = new Move[MAX_PLY];
    lastMoves[0] = Move.MOVE_NONE;
    zobristKey = 0L;
    zobristHistory = new long[MAX_PLY];
    zobristIndex = 0;
    pieceOccupiedBB = new Bitboard[PIECES_LENGTH];
    for (Piece piece: Piece.getPieces()) {
      if (piece == NO_PIECE) {
        pieceOccupiedBB[piece.ordinal()] = ALL_BB.newInstance();
      } else {
        pieceOccupiedBB[piece.ordinal()] = new Bitboard();
      }
    }
    colorOccupiedBB = new Bitboard[COLORS_LENGTH];
    colorOccupiedBB[0] = new Bitboard();
    colorOccupiedBB[1] = new Bitboard();
    occupiedBB = new Bitboard();
  }

  /**
   * A static factory method returning a new position instance from SFEN.
   *
   * @param sfen  SFEN
   * @return  a new position from SFEN
   */
  public static Position createPositionFromSfen(String sfen) {
    PositionBitboardImpl position = new PositionBitboardImpl();

    if (sfen.equals("startpos")) {
      sfen = HIRATE_SFEN;
    }

    String[] sfenElements = sfen.split(" ");
    if (sfenElements.length != 4) {
      throw new IllegalArgumentException("Invalid SFEN: The number of elements is not enough.");
    }

    String[] ranks = sfenElements[0].split("/");
    if (ranks.length != RANKS_LENGTH) {
      throw new IllegalArgumentException("Invalid SFEN: The number of ranks is not enough.");
    }

    // Set pieces on the board
    for (int i = 0; i < ranks.length; i++) {
      int leftSquareIndex = Square.SQ_91.ordinal() + i;
      int fileOffset = 0;
      int fileLength = 0;
      for (int j = 0; j < ranks[i].length(); j++) {
        if (Character.isDigit(ranks[i].charAt(j))) {
          int length = ranks[i].charAt(j) - '0';
          for (int k = 0; k < length; k++) {
            position.setPiece(Square.valueOf(leftSquareIndex - fileOffset), NO_PIECE);
            fileOffset += RANKS_LENGTH;
          }
          fileLength += length;
        } else {
          String pieceUsi;
          if (ranks[i].charAt(j) == '+') {
            pieceUsi = ranks[i].substring(j, j + 2);
            j++;
          } else {
            pieceUsi = ranks[i].substring(j, j + 1);
          }
          Piece piece = Piece.parseUSI(pieceUsi);
          if (piece == null) {
            throw new IllegalArgumentException("Invalid SFEN: A piece is invalid.");
          }
          Square square = Square.valueOf(leftSquareIndex - fileOffset);
          position.setPiece(square, piece);
          if (piece == Piece.B_KING) {
            position.kingSquare[BLACK.ordinal()] = square;
          } else if (piece == Piece.W_KING) {
            position.kingSquare[WHITE.ordinal()] = square;
          }
          fileOffset += RANKS_LENGTH;
          fileLength++;
        }
      }
      if (fileLength != 9) {
        throw new IllegalArgumentException("Invalid SFEN: The number of pieces is not enough.");
      }
    }

    // Set side
    Color color = Color.parseUSI(sfenElements[1]);
    if (color == null) {
      throw new IllegalArgumentException("Invalid SFEN: The side is invalid.");
    }
    position.sideToMove = BLACK;
    if (color.equals(WHITE)) {
      position.changeSide();
    }

    // Set hands
    String fenHands = sfenElements[2];
    if (!fenHands.equals("-")) {
      // parse by piece and number of pieces
      // e.g. "RBGSNLP3g3n17p" -> { "R", "B", "G", "S", "N", "L", "P", "3", "g", "3", "n", "17", "p" }
      String[] pieceElements = sfenElements[2].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)|(?=\\D)(?=\\D)");
      int num = 1;
      for (String pieceElement : pieceElements) {
        if (pieceElement.matches("\\d+")) {
          num = Integer.parseInt(pieceElement);
          continue;
        }
        for (int i = 0; i < num; i++) {
          Piece piece = Piece.parseUSI(pieceElement);
          if (piece == null) {
            throw new IllegalArgumentException("Invalid SFEN: The hand is invalid.");
          }
          position.addHand(piece);
        }
        num = 1;
      }
    }

    // Set move counter
    try {
      int nextMoveNumber = Integer.parseInt(sfenElements[3]);
      position.moveCounter = nextMoveNumber - 1;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid SFEN: The next move number is invalid.");
    }

    position.updateCheckerBitboard();
    position.zobristHistory[position.zobristIndex++] = position.getZobristHash();

    return position;
  }

  Hand getHand(final Color color) {
    return hands[color.ordinal()];
  }

  @Override
  public Color getSideToMove() {
    return sideToMove;
  }

  @Override
  public int getMoveCounter() {
    return moveCounter;
  }

  @Override
  public Move getLastMove() {
    return lastMoves[moveCounter];
  }

  @Override
  public Piece getLastCapturedPiece() {
    return capturedMap.getOrDefault(moveCounter, NO_PIECE);
  }

  @Override
  public Piece getPiece(final Square square) {
    return board[square.ordinal()];
  }

  @Override
  public Piece getPiece(final File file, final Rank rank) {
    return board[file.ordinal() * RANKS_LENGTH + rank.ordinal()];
  }

  private Piece getPiece(final int file, final int rank) {
    return board[file * RANKS_LENGTH + rank];
  }

  @Override
  public void setPiece(final Square square, final Piece piece) {
    unsetPiece(square);
    board[square.ordinal()] = piece;
    if (piece != NO_PIECE) {
      occupiedBB.or(getSquareBitboard(square));
      pieceOccupiedBB[NO_PIECE.ordinal()].xor(getSquareBitboard(square));
      pieceOccupiedBB[piece.ordinal()].or(getSquareBitboard(square));
      colorOccupiedBB[piece.getColor().ordinal()].or(getSquareBitboard(square));
      zobristKey ^= Zobrist.getBoardKey(piece, square);
    }
  }

  @Override
  public void unsetPiece(final Square square) {
    Piece piece = board[square.ordinal()];
    if (piece != NO_PIECE) {
      board[square.ordinal()] = NO_PIECE;
      occupiedBB.xor(getSquareBitboard(square));
      pieceOccupiedBB[NO_PIECE.ordinal()].or(getSquareBitboard(square));
      pieceOccupiedBB[piece.ordinal()].xor(getSquareBitboard(square));
      colorOccupiedBB[piece.getColor().ordinal()].xor(getSquareBitboard(square));
      zobristKey ^= Zobrist.getBoardKey(piece, square);
    }
  }

  @Override
  public void addPieceToHand(final Color color, final PieceType pr) {
    zobristKey ^= handsHash();
    hands[color.ordinal()].add(pr);
    zobristKey ^= handsHash();
  }

  private void addHand(final Piece piece) {
    addPieceToHand(piece.getColor(), piece.getRawType());
  }

  @Override
  public void subtractPieceFromHand(final Color color, final PieceType pr) {
    zobristKey ^= handsHash();
    hands[color.ordinal()].subtract(pr);
    zobristKey ^= handsHash();
  }

  @Override
  public int getNumberOfPieceInHand(Color color, PieceType pr) {
    return hands[color.ordinal()].count(pr);
  }

  private void changeSide() {
    sideToMove = sideToMove.getOpponent();
    zobristKey ^= Zobrist.sideKey;
  }

  @Override
  public void doMove(final Move move) {
    moveCounter++;
    lastMoves[moveCounter] = move;

    if (move == Move.MOVE_NULL) {
      // do nothing
    } else if (move.isDrop()) {
      PieceType pt = move.getPieceType();
      Piece piece = pt.getColoredPiece(sideToMove);
      Square toSquare = move.getToSquare();
      assert getPiece(toSquare) == NO_PIECE;
      setPiece(toSquare, piece);
      subtractPieceFromHand(sideToMove, pt);
    } else {
      Square fromSquare = move.getFromSquare();
      Square toSquare = move.getToSquare();

      // If a piece exists on the destination square, add the piece to the hand
      Piece captured = board[toSquare.ordinal()];
      if (captured != NO_PIECE) {
        capturedMap.put(moveCounter, captured);
        addPieceToHand(sideToMove, captured.getRawType());
      }

      // move the piece from the origin square to the destination square
      Piece fromPiece = board[fromSquare.ordinal()];
      if (move.isPromote()) {
        fromPiece = fromPiece.getPromoted();
      }
      setPiece(toSquare, fromPiece);
      unsetPiece(fromSquare);

      if (fromPiece.getPieceType() == KING) {
        kingSquare[sideToMove.ordinal()] = toSquare;
      }
    }

    changeSide();
    updateCheckerBitboard();
    zobristHistory[zobristIndex++] = getZobristHash();
  }

  @Override
  public Move undoMove() {
    if (moveCounter == 0) {
      // do nothing
      return Move.MOVE_NONE;
    }

    Move move = lastMoves[moveCounter];

    if (move == Move.MOVE_NULL) {
      // do nothing
    } else if (move.isDrop()) {
      Square toSquare = move.getToSquare();
      unsetPiece(toSquare);
      PieceType pt = move.getPieceType();
      addPieceToHand(sideToMove.getOpponent(), pt);
    } else {
      Square fromSquare = move.getFromSquare();
      Square toSquare = move.getToSquare();

      // move the piece from the destination square to the origin square
      Piece toPiece = board[toSquare.ordinal()];
      if (move.isPromote()) {
        toPiece = toPiece.getReversePromoted();
      }
      setPiece(fromSquare, toPiece);

      // reset the destination square
      Piece captured = capturedMap.get(moveCounter);
      if (captured != null) {
        subtractPieceFromHand(sideToMove.getOpponent(), captured.getRawType());
        setPiece(toSquare, captured);
        capturedMap.remove(moveCounter);
      } else {
        unsetPiece(toSquare);
      }

      if (toPiece.getPieceType() == KING) {
        kingSquare[sideToMove.getOpponent().ordinal()] = fromSquare;
      }
    }

    changeSide();
    updateCheckerBitboard();
    zobristHistory[zobristIndex--] = 0;
    moveCounter--;
    return move;
  }

  @Override
  public List<Move> getLegalMoves() {
    return BitboardMoveGenerator.getLegalMoves(this);
  }

  @Override
  public List<Move> getPseudoLegalMoves() {
    return BitboardMoveGenerator.getPseudoLegalMoves(this);
  }

  // Returns bitboard of the given colored attacker, effective for the given square.
  Bitboard getAttackersTo(Color attacker, Square square) {
    if (square == null) return null;
    Bitboard occ = getOccupiedBitboard();
    Color attacked = attacker.getOpponent();

    // Bitboard integrating GOLD group
    Bitboard GOLDS = or(getPieceBitboard(attacker, GOLD), getPieceBitboard(attacker, PRO_PAWN))
        .or(getPieceBitboard(attacker, PRO_LANCE))
        .or(getPieceBitboard(attacker, PRO_KNIGHT))
        .or(getPieceBitboard(attacker, PRO_SILVER));

    // Bitboard integrating HORSE, DRAGON, KING (HDK)
    Bitboard HDK = or(getPieceBitboard(attacker, HORSE), getPieceBitboard(attacker, DRAGON))
        .or(getPieceBitboard(attacker, KING));

    // Bitboard integrating SILVER and HDK
    Bitboard SILVER_HDK = or(getPieceBitboard(attacker, SILVER), HDK);

    // Bitboard integrating GOLD group and HDK
    Bitboard GOLDS_HDK = or(GOLDS, HDK);

    // Bitboard integrating BISHOP and HORSE
    Bitboard BISHOP_HORSE = or(getPieceBitboard(attacker, BISHOP), getPieceBitboard(attacker, HORSE));

    // Bitboard integrating ROOK and DRAGON
    Bitboard ROOK_DRAGON = or(getPieceBitboard(attacker, ROOK), getPieceBitboard(attacker, DRAGON));

    // Suppose the attacker is black and the attacked is white. If a white piece exists
    // on the square, and a black piece exists on the effect of the white piece,
    // it means that the black piece has an effect on the square. The reverse is also true.

    return and(getPawnEffectBitboard(attacked, square), getPieceBitboard(attacker, PAWN))
        .or(and(getKnightEffectBitboard(attacked, square), getPieceBitboard(attacker, KNIGHT)))
        .or(and(getSilverEffectBitboard(attacked, square), SILVER_HDK))
        .or(and(getGoldEffectBitboard(attacked, square), GOLDS_HDK))
        .or(and(getBishopSlidingEffectBitboard(square, occ), BISHOP_HORSE))
        .or(and(getRookSlidingEffectBitboard(square, occ), ROOK_DRAGON))
        .or(and(newLanceSlidingEffectBitboard(attacked, square, occ), getPieceBitboard(attacker, LANCE)));
  }

  private Bitboard getPieceBitboard(final Color color, final PieceType pt) {
    return pieceOccupiedBB[pt.getColoredPiece(color).ordinal()];
  }

  Bitboard getOccupiedBitboard() {
    return occupiedBB;
  }

  Bitboard getUnoccupiedBitboard() {
    return pieceOccupiedBB[NO_PIECE.ordinal()];
  }

  private void updateCheckerBitboard() {
    checkerBB = getAttackersTo(sideToMove.getOpponent(), kingSquare[sideToMove.ordinal()]);
  }

  @Override
  public boolean isKingAttacked() {
    return !checkerBB.isZero();
  }

  // Is the king of the given color attacked?
  boolean isKingAttacked(final Color color) {
    Bitboard bb = getAttackersTo(color.getOpponent(), kingSquare[color.ordinal()]);
    return !bb.isZero();
  }

  Square getKingSquare(final Color color) {
    return kingSquare[color.ordinal()];
  }

  Bitboard getPieceBitboard(final Piece piece) {
    return pieceOccupiedBB[piece.ordinal()];
  }

  Bitboard getColorBitboard(final Color color) {
    return colorOccupiedBB[color.ordinal()];
  }

  // Create 64-bit hash from the black hand and the white hand.
  // Since the hand doesn't use the left most bit, this hash doesn't affect the side bit.
  private long handsHash() {
    return ((long)hands[WHITE.ordinal()].hashCode() << 32) | (long)hands[BLACK.ordinal()].hashCode();
  }

  @Override
  public long getZobristHash() {
    return zobristKey;
  }

  @Override
  public boolean isRepetition() {
    int count = 1;
    // the current position and the previous position don't need to be checked
    for (int i = zobristIndex - 3; i >= 0; i--) {
      // Simply assume the same position if the zobrist hashes are the same.
      // TODO: check a fourfold repetition strictly
      if (zobristHistory[i] == getZobristHash()) {
        count++;
        if (count == 4) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean isMated() {
    return BitboardMoveGenerator.getLegalMoves(this).size() == 0;
  }

  @Override
  public String getSfen() {
    // board
    StringBuilder sb = new StringBuilder();
    int spaces = 0;
    for (int i = 0; i < RANKS_LENGTH; i++) {
      if (i > 0) {
        sb.append("/");
      }
      for (int j = FILES_LENGTH - 1; j >= 0; j--) {
        Piece piece = getPiece(j, i);
        if (piece == NO_PIECE) {
          spaces++;
        } else {
          if (spaces > 0) {
            sb.append(spaces);
            spaces = 0;
          }
          sb.append(piece);
        }
      }
      if (spaces > 0) {
        sb.append(spaces);
        spaces = 0;
      }
    }

    // side
    sb.append(" ");
    sb.append(sideToMove);

    // hands
    sb.append(" ");
    if (hands[0].isZero() && hands[1].isZero()) {
      sb.append("-");
    } else {
      sb.append(hands[0]);
      sb.append(hands[1].toString().toLowerCase());
    }

    // the next move counter
    sb.append(" ");
    sb.append(moveCounter + 1);

    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Side: ").append(getSideToMove().name()).append("\n");
    sb.append("WHITE Hand: ").append(getHand(WHITE).toString().toLowerCase()).append("\n");

    for (int rank = 0; rank <= 8; rank++) {
      for (int file = 8; file >= 0; file--) {
        sb.append(" ");
        sb.append(getPiece(file, rank));
      }
      sb.append("\n");
    }

    sb.append("BLACK Hand: ").append(getHand(BLACK)).append("\n");

    return sb.toString();
  }

  @Override
  public String pretty() {
    StringBuilder sb = new StringBuilder();

    sb.append("手番: ").append(getSideToMove().pretty()).append("\n");
    sb.append("手数: ").append(getMoveCounter()).append("手目\n");
    sb.append("最終手: ").append(getLastMove().pretty()).append("\n");

    sb.append("後手の持ち駒: ");
    sb.append(getHand(WHITE).pretty());
    sb.append("\n");

    for (int rank = 0; rank <= 8; rank++) {
      for (int file = 8; file >= 0; file--) {
        if (getPiece(file, rank).getColor() == WHITE) {
          sb.append("^");
        } else {
          sb.append(" ");
        }
        sb.append(getPiece(file, rank).pretty());
      }
      sb.append("\n");
    }

    sb.append("先手の持ち駒: ");
    sb.append(getHand(BLACK).pretty());
    sb.append("\n");

    return sb.toString();
  }
}

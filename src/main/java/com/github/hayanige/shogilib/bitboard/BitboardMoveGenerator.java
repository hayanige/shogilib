package com.github.hayanige.shogilib.bitboard;

import static com.github.hayanige.shogilib.Color.*;
import static com.github.hayanige.shogilib.PieceType.*;
import static com.github.hayanige.shogilib.Rank.*;
import static com.github.hayanige.shogilib.Square.Direction.N;
import static com.github.hayanige.shogilib.Square.Direction.S;
import static com.github.hayanige.shogilib.bitboard.Bitboard.and;
import static com.github.hayanige.shogilib.bitboard.BitboardConstants.*;

import com.github.hayanige.shogilib.Color;
import com.github.hayanige.shogilib.Hand;
import com.github.hayanige.shogilib.Move;
import com.github.hayanige.shogilib.Piece;
import com.github.hayanige.shogilib.PieceType;
import com.github.hayanige.shogilib.Square;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class BitboardMoveGenerator {

  // generates genuine legal moves
  static List<Move> getLegalMoves(final PositionBitboardImpl position) {
    List<Move> moves = getPseudoLegalMoves(position);
    Iterator<Move> itr = moves.iterator();
    while (itr.hasNext()) {
      Move move = itr.next();
      position.doMove(move);

      if (position.isKingAttacked(position.getSideToMove().getOpponent())) {
        // removes hand that leaves the king checked
        itr.remove();
      } else if (move.isDrop() && move.getPieceType() == PAWN && position.isKingAttacked()) {
        // removes drop pawn mate
        if (getLegalMoves(position).size() == 0) {
          itr.remove();
        }
      } else if (position.isRepetition() && position.isKingAttacked()) {
        // removes fourfold repetition for the king
        itr.remove();
      }

      position.undoMove();
    }
    return moves;
  }

  // pseudo-legal moves may include moves that leave the king checked.
  static List<Move> getPseudoLegalMoves(final PositionBitboardImpl position) {
    List<Move> moves = new ArrayList<>();
    generateNonDropMoves(position, moves);
    generateDropMoves(position, moves);
    return moves;
  }

  private static void generateNonDropMoves(final PositionBitboardImpl position, final List<Move> moves) {
    generatePawnMoves(position, moves);
    generateKnightMoves(position, moves);
    generateSilverMoves(position, moves);
    generateGoldGroupMoves(position, moves);
    generateKingMoves(position, moves);
    generateLanceMoves(position, moves);
    generateBishopMoves(position, moves);
    generateRookMoves(position, moves);
    generateHorseMoves(position, moves);
    generateDragonMoves(position, moves);
  }

  private static void generatePawnMoves(PositionBitboardImpl position, List<Move> moves) {
    Color color = position.getSideToMove();
    Bitboard friendBB = position.getColorBitboard(color);
    Piece pawn = PAWN.getColoredPiece(color);
    Bitboard fromBB = position.getPieceBitboard(pawn).newInstance();
    // an effect of a pawn is the next square of the square that the pawn exists
    Bitboard toBB = color == BLACK ? fromBB.rightShift() : fromBB.leftShift();
    toBB.andNot(friendBB);

    while (toBB.hasNext()) {
      Square to = toBB.getNextSquare();
      Square from = color == BLACK ? to.getNextSquare(S) : to.getNextSquare(N);
      assert from != null;
      if ((color == BLACK && to.getRank() == RANK_1) ||
          (color == WHITE && to.getRank() == RANK_9)
      ) {
        moves.add(Move.makeMovePromote(from, to));
      } else if (from.canPromote(color) || to.canPromote(color)) {
        moves.add(Move.makeMove(from, to));
        moves.add(Move.makeMovePromote(from, to));
      } else {
        moves.add(Move.makeMove(from, to));
      }
    }
  }

  private static void generateKnightMoves(PositionBitboardImpl position, List<Move> moves) {
    Color color = position.getSideToMove();
    Bitboard friendBB = position.getColorBitboard(color);
    Piece knight = KNIGHT.getColoredPiece(color);
    Bitboard fromBB = position.getPieceBitboard(knight).newInstance();

    while (fromBB.hasNext()) {
      Square from = fromBB.getNextSquare();
      Bitboard toBB = getKnightEffectBitboard(color, from).newInstance();
      toBB.andNot(friendBB);
      while (toBB.hasNext()) {
        Square to = toBB.getNextSquare();
        if ((color == BLACK && (to.getRank() == RANK_1 || to.getRank() == RANK_2)) ||
            (color == WHITE && (to.getRank() == RANK_9 || to.getRank() == RANK_8))
        ) {
          moves.add(Move.makeMovePromote(from, to));
        } else if (to.canPromote(color)) {
          moves.add(Move.makeMove(from, to));
          moves.add(Move.makeMovePromote(from, to));
        } else {
          moves.add(Move.makeMove(from, to));
        }
      }
    }
  }

  private static void generateSilverMoves(PositionBitboardImpl position, List<Move> moves) {
    Color color = position.getSideToMove();
    Bitboard friendBB = position.getColorBitboard(color);
    Piece silver = SILVER.getColoredPiece(color);
    Bitboard fromBB = position.getPieceBitboard(silver).newInstance();

    while (fromBB.hasNext()) {
      Square from = fromBB.getNextSquare();
      Bitboard toBB = getSilverEffectBitboard(color, from).newInstance();
      toBB.andNot(friendBB);
      while (toBB.hasNext()) {
        Square to = toBB.getNextSquare();
        moves.add(Move.makeMove(from, to));
        if (from.canPromote(color) || to.canPromote(color)) {
          moves.add(Move.makeMovePromote(from, to));
        }
      }
    }
  }

  private static void generateGoldGroupMoves(PositionBitboardImpl position, List<Move> moves) {
    Color color = position.getSideToMove();
    Bitboard friendBB = position.getColorBitboard(color);
    PieceType[] goldPieces = new PieceType[]{GOLD, PRO_PAWN, PRO_LANCE, PRO_KNIGHT, PRO_SILVER};

    for (PieceType goldPiece : goldPieces) {
      Piece gold = goldPiece.getColoredPiece(color);
      Bitboard fromBB = position.getPieceBitboard(gold).newInstance();
      while (fromBB.hasNext()) {
        Square from = fromBB.getNextSquare();
        Bitboard toBB = getGoldEffectBitboard(color, from).newInstance();
        toBB.andNot(friendBB);
        while (toBB.hasNext()) {
          Square to = toBB.getNextSquare();
          moves.add(Move.makeMove(from, to));
        }
      }
    }
  }

  private static void generateKingMoves(PositionBitboardImpl position, List<Move> moves) {
    Color color = position.getSideToMove();
    Bitboard friendBB = position.getColorBitboard(color);
    Square from = position.getKingSquare(color);  // King is only one
    Bitboard toBB = getKingEffectBitboard(from).newInstance();
    toBB.andNot(friendBB);
    while (toBB.hasNext()) {
      Square to = toBB.getNextSquare();
      moves.add(Move.makeMove(from, to));
    }
  }

  private static void generateLanceMoves(PositionBitboardImpl position, List<Move> moves) {
    Color color = position.getSideToMove();
    Bitboard friendBB = position.getColorBitboard(color);
    Bitboard occupiedBB = position.getOccupiedBitboard();
    Piece lance = LANCE.getColoredPiece(color);
    Bitboard fromBB = position.getPieceBitboard(lance).newInstance();

    while (fromBB.hasNext()) {
      Square from = fromBB.getNextSquare();
      Bitboard toBB = newLanceSlidingEffectBitboard(color, from, occupiedBB);
      toBB.andNot(friendBB);
      while (toBB.hasNext()) {
        Square to = toBB.getNextSquare();
        if ((color == BLACK && to.getRank() == RANK_1) ||
            (color == WHITE && to.getRank() == RANK_9)
        ) {
          moves.add(Move.makeMovePromote(from, to));
        } else if (from.canPromote(color) || to.canPromote(color)) {
          moves.add(Move.makeMove(from, to));
          moves.add(Move.makeMovePromote(from, to));
        } else {
          moves.add(Move.makeMove(from, to));
        }
      }
    }
  }

  private static void generateBishopMoves(PositionBitboardImpl position, List<Move> moves) {
    Color color = position.getSideToMove();
    Bitboard friendBB = position.getColorBitboard(color);
    Bitboard occupiedBB = position.getOccupiedBitboard();
    Piece bishop = BISHOP.getColoredPiece(color);
    Bitboard fromBB = position.getPieceBitboard(bishop).newInstance();

    while (fromBB.hasNext()) {
      Square from = fromBB.getNextSquare();
      Bitboard toBB = getBishopSlidingEffectBitboard(from, occupiedBB).newInstance();
      toBB.andNot(friendBB);
      while (toBB.hasNext()) {
        Square to = toBB.getNextSquare();
        moves.add(Move.makeMove(from, to));
        if (from.canPromote(color) || to.canPromote(color)) {
          moves.add(Move.makeMovePromote(from, to));
        }
      }
    }
  }

  private static void generateRookMoves(PositionBitboardImpl position, List<Move> moves) {
    Color color = position.getSideToMove();
    Bitboard friendBB = position.getColorBitboard(color);
    Bitboard occupiedBB = position.getOccupiedBitboard();
    Piece rook = ROOK.getColoredPiece(color);
    Bitboard fromBB = position.getPieceBitboard(rook).newInstance();

    while (fromBB.hasNext()) {
      Square from = fromBB.getNextSquare();
      Bitboard toBB = getRookSlidingEffectBitboard(from, occupiedBB).newInstance();
      toBB.andNot(friendBB);
      while (toBB.hasNext()) {
        Square to = toBB.getNextSquare();
        moves.add(Move.makeMove(from, to));
        if (from.canPromote(color) || to.canPromote(color)) {
          moves.add(Move.makeMovePromote(from, to));
        }
      }
    }
  }

  private static void generateHorseMoves(PositionBitboardImpl position, List<Move> moves) {
    Color color = position.getSideToMove();
    Bitboard friendBB = position.getColorBitboard(color);
    Piece horse = HORSE.getColoredPiece(color);
    Bitboard occupiedBB = position.getOccupiedBitboard();
    Bitboard fromBB = position.getPieceBitboard(horse).newInstance();

    while (fromBB.hasNext()) {
      Square from = fromBB.getNextSquare();
      Bitboard toBB = getBishopSlidingEffectBitboard(from, occupiedBB).newInstance();
      toBB.or(getRookStepEffectBitboard(from)).andNot(friendBB);
      while (toBB.hasNext()) {
        Square to = toBB.getNextSquare();
        moves.add(Move.makeMove(from, to));
      }
    }
  }

  private static void generateDragonMoves(PositionBitboardImpl position, List<Move> moves) {
    Color color = position.getSideToMove();
    Bitboard friendBB = position.getColorBitboard(color);
    Bitboard occupiedBB = position.getOccupiedBitboard();
    Piece dragon = DRAGON.getColoredPiece(color);
    Bitboard fromBB = position.getPieceBitboard(dragon).newInstance();

    while (fromBB.hasNext()) {
      Square from = fromBB.getNextSquare();
      Bitboard toBB = getRookSlidingEffectBitboard(from, occupiedBB).newInstance();
      toBB.or(getBishopStepEffectBitboard(from)).andNot(friendBB);
      while (toBB.hasNext()) {
        Square to = toBB.getNextSquare();
        moves.add(Move.makeMove(from, to));
      }
    }
  }

  private static void generateDropMoves(final PositionBitboardImpl position, final List<Move> moves) {
    Color color = position.getSideToMove();

    Hand hand = position.getHand(color);
    // do nothing if the hand is empty
    if (hand.isZero()) {
      return;
    }

    Bitboard unoccupied = position.getUnoccupiedBitboard();

    // generate drop pawn
    if (hand.exists(PAWN)) {
      Bitboard rank29Target;

      // remove drop pawn moves if the destination is the first rank
      if (color == BLACK) {
        rank29Target = and(unoccupied, ForwardRanksBB[color.getOpponent().ordinal()][0]);
      } else {
        rank29Target = and(unoccupied, ForwardRanksBB[color.getOpponent().ordinal()][8]);
      }

      // remove Nifu (double pawn in a file)
      Piece pawn = PAWN.getColoredPiece(color);
      Bitboard pawnBB = position.getPieceBitboard(pawn).newInstance();
      while (pawnBB.hasNext()) {
        Square pawnSquare = pawnBB.getNextSquare();
        rank29Target.andNot(FILE_BB[pawnSquare.getFile().ordinal()]);
      }

      // generate moves
      while (rank29Target.hasNext()) {
        Square to = rank29Target.getNextSquare();
        moves.add(Move.makeMoveDrop(PAWN, to));
      }
    }

    if (!hand.existsExceptPawn()) {
      return;
    }

    Bitboard rank1Target;
    Bitboard rank2Target;
    Bitboard rank39Target;
    if (color == BLACK) {
      rank1Target = and(RANK1_BB, unoccupied);
      rank2Target = and(RANK2_BB, unoccupied);
      rank39Target = and(ForwardRanksBB[WHITE.ordinal()][1], unoccupied);
    } else {
      rank1Target = and(RANK9_BB, unoccupied);
      rank2Target = and(RANK8_BB, unoccupied);
      rank39Target = and(ForwardRanksBB[BLACK.ordinal()][7], unoccupied);
    }

    PieceType[] piecesInHand = new PieceType[6];
    int numHands = 0;
    int numNight = 0;
    int numLanceNight = 0;

    if (hand.exists(ROOK))    piecesInHand[numHands++] = ROOK;
    if (hand.exists(BISHOP))  piecesInHand[numHands++] = BISHOP;
    if (hand.exists(GOLD))    piecesInHand[numHands++] = GOLD;
    if (hand.exists(SILVER))  piecesInHand[numHands++] = SILVER;
    if (hand.exists(LANCE)) {
      numLanceNight++;
      piecesInHand[numHands++] = LANCE;
    }
    if (hand.exists(KNIGHT)) {
      numLanceNight++;
      numNight++;
      piecesInHand[numHands++] = KNIGHT;
    }

    // remove drop lance moves and drop knight moves if the destination is the first rank
    while (rank1Target.hasNext()) {
      Square to = rank1Target.getNextSquare();
      for (int i = 0; i < numHands - numLanceNight; i++) {
        moves.add(Move.makeMoveDrop(piecesInHand[i], to));
      }
    }

    // remove drop knight moves if the destination is the second rank
    while (rank2Target.hasNext()) {
      Square to = rank2Target.getNextSquare();
      for (int i = 0; i < numHands - numNight; i++) {
        moves.add(Move.makeMoveDrop(piecesInHand[i], to));
      }
    }

    // generate all drop moves if the destination is between the third rank and the ninth rank
    while (rank39Target.hasNext()) {
      Square to = rank39Target.getNextSquare();
      for (int i = 0; i < numHands; i++) {
        moves.add(Move.makeMoveDrop(piecesInHand[i], to));
      }
    }
  }
}

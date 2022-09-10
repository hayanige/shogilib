package com.github.hayanige.shogilib.bitboard;

import static com.github.hayanige.shogilib.Color.COLORS_LENGTH;
import static com.github.hayanige.shogilib.Piece.B_BISHOP;
import static com.github.hayanige.shogilib.Piece.B_KING;
import static com.github.hayanige.shogilib.Piece.B_ROOK;
import static com.github.hayanige.shogilib.PieceType.GOLD;
import static com.github.hayanige.shogilib.PieceType.KNIGHT;
import static com.github.hayanige.shogilib.PieceType.PAWN;
import static com.github.hayanige.shogilib.PieceType.SILVER;
import static com.github.hayanige.shogilib.Rank.RANKS_LENGTH;
import static com.github.hayanige.shogilib.Square.SQUARES_LENGTH;
import static com.github.hayanige.shogilib.bitboard.Bitboard.LOWER_LENGTH;
import static com.github.hayanige.shogilib.bitboard.Bitboard.and;
import static com.github.hayanige.shogilib.bitboard.Bitboard.or;

import com.github.hayanige.shogilib.Color;
import com.github.hayanige.shogilib.File;
import com.github.hayanige.shogilib.Piece;
import com.github.hayanige.shogilib.Rank;
import com.github.hayanige.shogilib.Square;
import com.github.hayanige.shogilib.Square.Direction;

/**
 * Bitboard Constants used in Bitboard operations.
 */
public class BitboardConstants {

  public static final Bitboard ALL_BB  = new Bitboard(0x7FFFFFFFFFFFFFFFL, 0x3FFFFL);
  public static final Bitboard ZERO_BB = new Bitboard();

  public static final Bitboard FILE1_BB = new Bitboard(0x1ffL << (9 * 0), 0);
  public static final Bitboard FILE2_BB = new Bitboard(0x1ffL << (9 * 1), 0);
  public static final Bitboard FILE3_BB = new Bitboard(0x1ffL << (9 * 2), 0);
  public static final Bitboard FILE4_BB = new Bitboard(0x1ffL << (9 * 3), 0);
  public static final Bitboard FILE5_BB = new Bitboard(0x1ffL << (9 * 4), 0);
  public static final Bitboard FILE6_BB = new Bitboard(0x1ffL << (9 * 5), 0);
  public static final Bitboard FILE7_BB = new Bitboard(0x1ffL << (9 * 6), 0);
  public static final Bitboard FILE8_BB = new Bitboard(0, 0x1ffL << (9 * 0));
  public static final Bitboard FILE9_BB = new Bitboard(0, 0x1ffL << (9 * 1));

  public static final Bitboard RANK1_BB = new Bitboard(0x40201008040201L << 0, 0x201L << 0);
  public static final Bitboard RANK2_BB = new Bitboard(0x40201008040201L << 1, 0x201L << 1);
  public static final Bitboard RANK3_BB = new Bitboard(0x40201008040201L << 2, 0x201L << 2);
  public static final Bitboard RANK4_BB = new Bitboard(0x40201008040201L << 3, 0x201L << 3);
  public static final Bitboard RANK5_BB = new Bitboard(0x40201008040201L << 4, 0x201L << 4);
  public static final Bitboard RANK6_BB = new Bitboard(0x40201008040201L << 5, 0x201L << 5);
  public static final Bitboard RANK7_BB = new Bitboard(0x40201008040201L << 6, 0x201L << 6);
  public static final Bitboard RANK8_BB = new Bitboard(0x40201008040201L << 7, 0x201L << 7);
  public static final Bitboard RANK9_BB = new Bitboard(0x40201008040201L << 8, 0x201L << 8);

  public static final Bitboard[] FILE_BB = { FILE1_BB, FILE2_BB, FILE3_BB, FILE4_BB, FILE5_BB, FILE6_BB, FILE7_BB, FILE8_BB, FILE9_BB };
  public static final Bitboard[] RANK_BB = { RANK1_BB, RANK2_BB, RANK3_BB, RANK4_BB, RANK5_BB, RANK6_BB, RANK7_BB, RANK8_BB, RANK9_BB };

  // this is used when calculating lance effects
  public static final Bitboard[][] ForwardRanksBB = new Bitboard[COLORS_LENGTH][RANKS_LENGTH];

  // Bitboards where only the bit corresponding to the square is 1
  private static final Bitboard[] SquareBB = new Bitboard[SQUARES_LENGTH];


  // effect bitboards for each piece.
  private static final Bitboard[][] PawnEffectBB = new Bitboard[COLORS_LENGTH][SQUARES_LENGTH];
  private static final Bitboard[][] KnightEffectBB = new Bitboard[COLORS_LENGTH][SQUARES_LENGTH];
  private static final Bitboard[][] SilverEffectBB = new Bitboard[COLORS_LENGTH][SQUARES_LENGTH];
  private static final Bitboard[][] GoldEffectBB = new Bitboard[COLORS_LENGTH][SQUARES_LENGTH];
  private static final Bitboard[] KingEffectBB = new Bitboard[SQUARES_LENGTH];
  private static final Bitboard[] BishopStepEffectBB = new Bitboard[SQUARES_LENGTH];
  private static final Bitboard[] RookStepEffectBB = new Bitboard[SQUARES_LENGTH];

  /*
   * Implementation of Magic Bitboard in Shogi
   * See: https://ipsj.ixsq.nii.ac.jp/ej/?action=repository_action_common_download&item_id=71312&item_no=1&attribute_id=1&file_no=1
   *
   * The following source code was implemented with reference to Yaneuraou, Apery and Gikou.
   */

  /*
   * Array to store all sliding effect patterns.
   *
   * Basically, total number of indices is the sum of the sum of the subsets of the candidate
   * effects of the pieces in each square. The total number of Bishop indexes is calculated as
   * follows:
   * (1<<12) + (1<<10)*8 + (1<<8)*16 + (1<<6)*52 + (1<<7)*4 = 20224
   *
   * The Rook table needs to be expanded more.
   * See: https://hiraoka64.hatenablog.com/entry/20161201/1480603616
   */
  private static final Bitboard[] BishopAttack = new Bitboard[20224];
  private static final Bitboard[] RookAttack = new Bitboard[512000];
  private static final int[] BishopAttackIndex = new int[SQUARES_LENGTH];
  private static final int[] RookAttackIndex = new int[SQUARES_LENGTH];
  private static final Bitboard[] BishopMaskBB = new Bitboard[SQUARES_LENGTH];
  private static final Bitboard[] RookMaskBB = new Bitboard[SQUARES_LENGTH];

  // The number of effective square candidates for Bishop in each square.
  private static final int[] BishopBlockBits = new int[] {
      7,  6,  6,  6,  6,  6,  6,  6,  7,
      6,  6,  6,  6,  6,  6,  6,  6,  6,
      6,  6,  8,  8,  8,  8,  8,  6,  6,
      6,  6,  8, 10, 10, 10,  8,  6,  6,
      6,  6,  8, 10, 12, 10,  8,  6,  6,
      6,  6,  8, 10, 10, 10,  8,  6,  6,
      6,  6,  8,  8,  8,  8,  8,  6,  6,
      6,  6,  6,  6,  6,  6,  6,  6,  6,
      7,  6,  6,  6,  6,  6,  6,  6,  7
  };

  // The number of effective square candidates for Rook in each square.
  private static final int[] RookBlockBits = new int[] {
      14, 13, 13, 13, 13, 13, 13, 13, 14,
      13, 12, 12, 12, 12, 12, 12, 12, 13,
      13, 12, 12, 12, 12, 12, 12, 12, 13,
      13, 12, 12, 12, 12, 12, 12, 12, 13,
      13, 12, 12, 12, 12, 12, 12, 12, 13,
      13, 12, 12, 12, 12, 12, 12, 12, 13,
      13, 12, 12, 12, 12, 12, 12, 12, 13,
      13, 12, 12, 12, 12, 12, 12, 12, 13,
      14, 13, 13, 13, 13, 13, 13, 13, 14
  };

  // The number of right shift for calculating Bishop effects in each square.
  private static final int[] BishopShift = new int[] {
      57, 58, 58, 58, 58, 58, 58, 58, 57,
      58, 58, 58, 58, 58, 58, 58, 58, 58,
      58, 58, 56, 56, 56, 56, 56, 58, 58,
      58, 58, 56, 54, 54, 54, 56, 58, 58,
      58, 58, 56, 54, 52, 54, 56, 58, 58,
      58, 58, 56, 54, 54, 54, 56, 58, 58,
      58, 58, 56, 56, 56, 56, 56, 58, 58,
      58, 58, 58, 58, 58, 58, 58, 58, 58,
      57, 58, 58, 58, 58, 58, 58, 58, 57
  };

  // The number of right shift for calculating Rook effects in each square.
  private static final int[] RookShift = new int[] {
      50, 51, 51, 51, 51, 51, 51, 51, 50,
      51, 52, 52, 52, 52, 52, 52, 52, 50, // [17] changes from 51 to 50.
      51, 52, 52, 52, 52, 52, 52, 52, 51,
      51, 52, 52, 52, 52, 52, 52, 52, 51,
      51, 52, 52, 52, 52, 52, 52, 52, 51,
      51, 52, 52, 52, 52, 52, 52, 52, 50, // [53] changes from 51 to 50.
      51, 52, 52, 52, 52, 52, 52, 52, 51,
      51, 52, 52, 52, 52, 52, 52, 52, 51,
      50, 51, 51, 51, 51, 51, 51, 51, 50
  };

  // Magic Numbers for Bishop
  private static final long[] BishopMagics = new long[] {
      0x20101042c8200428L, 0x840240380102L,     0x800800c018108251L,
      0x82428010301000L,   0x481008201000040L,  0x8081020420880800L,
      0x804222110000L,     0xe28301400850L,     0x2010221420800810L,
      0x2600010028801824L, 0x8048102102002L,    0x4000248100240402L,
      0x49200200428a2108L, 0x460904020844L,     0x2001401020830200L,
      0x1009008120L,       0x4804064008208004L, 0x4406000240300ca0L,
      0x222001400803220L,  0x226068400182094L,  0x95208402010d0104L,
      0x4000807500108102L, 0xc000200080500500L, 0x5211000304038020L,
      0x1108100180400820L, 0x10001280a8a21040L, 0x100004809408a210L,
      0x202300002041112L,  0x4040a8000460408L,  0x204020021040201L,
      0x8120013180404L,    0xa28400800d020104L, 0x200c201000604080L,
      0x1082004000109408L, 0x100021c00c410408L, 0x880820905004c801L,
      0x1054064080004120L, 0x30c0a0224001030L,  0x300060100040821L,
      0x51200801020c006L,  0x2100040042802801L, 0x481000820401002L,
      0x40408a0450000801L, 0x810104200000a2L,   0x281102102108408L,
      0x804020040280021L,  0x2420401200220040L, 0x80010144080c402L,
      0x80104400800002L,   0x1009048080400081L, 0x100082000201008cL,
      0x10001008080009L,   0x2a5006b80080004L,  0xc6288018200c2884L,
      0x108100104200a000L, 0x141002030814048L,  0x200204080010808L,
      0x200004013922002L,  0x2200000020050815L, 0x2011010400040800L,
      0x1020040004220200L, 0x944020104840081L,  0x6080a080801c044aL,
      0x2088400811008020L, 0xc40aa04208070L,    0x4100800440900220L,
      0x48112050L,         0x818200d062012a10L, 0x402008404508302L,
      0x100020101002L,     0x20040420504912L,   0x2004008118814L,
      0x1000810650084024L, 0x1002a03002408804L, 0x2104294801181420L,
      0x841080240500812L,  0x4406009000004884L, 0x80082004012412L,
      0x80090880808183L,   0x300120020400410L,  0x21a090100822002L
  };

  // Magic Numbers for Rook
  private static final long[] RookMagics = new long[] {
      0x140000400809300L,  0x1320000902000240L, 0x8001910c008180L,
      0x40020004401040L,   0x40010000d01120L,   0x80048020084050L,
      0x40004000080228L,   0x400440000a2a0aL,   0x40003101010102L,
      0x80c4200012108100L, 0x4010c00204000c01L, 0x220400103250002L,
      0x2600200004001L,    0x40200052400020L,   0xc00100020020008L,
      0x9080201000200004L, 0x2200201000080004L, 0x80804c0020200191L,
      0x45383000009100L,   0x30002800020040L,   0x40104000988084L,
      0x108001000800415L,  0x14005000400009L,   0xd21001001c00045L,
      0xc0003000200024L,   0x40003000280004L,   0x40021000091102L,
      0x2008a20408000d00L, 0x2000100084010040L, 0x144080008008001L,
      0x50102400100026a2L, 0x1040020008001010L, 0x1200200028005010L,
      0x4280030030020898L, 0x480081410011004L,  0x34000040800110aL,
      0x101000010c0021L,   0x9210800080082L,    0x6100002000400a7L,
      0xa2240800900800c0L, 0x9220082001000801L, 0x1040008001140030L,
      0x40002220040008L,   0x28000124008010cL,  0x40008404940002L,
      0x40040800010200L,   0x90000809002100L,   0x2800080001000201L,
      0x1400020001000201L, 0x180081014018004L,  0x1100008000400201L,
      0x80004000200201L,   0x420800010000201L,  0x2841c00080200209L,
      0x120002401040001L,  0x14510000101000bL,  0x40080000808001L,
      0x834000188048001L,  0x4001210000800205L, 0x4889a8007400201L,
      0x2080044080200062L, 0x80004002861002L,   0xc00842049024L,
      0x8040000202020011L, 0x400404002c0100L,   0x2080028202000102L,
      0x8100040800590224L, 0x2040009004800010L, 0x40045000400408L,
      0x2200240020802008L, 0x4080042002200204L, 0x4000b0000a00a2L,
      0xa600000810100L,    0x1410000d001180L,   0x2200101001080L,
      0x100020014104e120L, 0x2407200100004810L, 0x80144000a0845050L,
      0x1000200060030c18L, 0x4004200020010102L, 0x140600021010302L
  };

  public static Bitboard getFileBitboard(final File file) {
    return FILE_BB[file.ordinal()];
  }
  public static Bitboard getRankBitboard(final Rank rank) {
    return RANK_BB[rank.ordinal()];
  }

  // generates Square Bitboards
  static {
    for (Square square: Square.getSquares()) {
      if (square.ordinal() < LOWER_LENGTH) {
        SquareBB[square.ordinal()] = new Bitboard(1L << square.ordinal(), 0);
      } else {
        SquareBB[square.ordinal()] = new Bitboard(0, 1L << square.ordinal() - LOWER_LENGTH);
      }
    }
  }

  /**
   * Returns a bitboard where the only one bit corresponding to the given square is 1.
   *
   * @param square square of the square bitboard
   * @return  the square bitboard
   */
  public static Bitboard getSquareBitboard(final Square square) {
    return SquareBB[square.ordinal()];
  }

  // generates step effects
  static {
    // create PAWN effects bitboards
    for (final Color color : Color.getColors()) {
      for (final Square square : Square.getSquares()) {
        PawnEffectBB[color.ordinal()][square.ordinal()] = new Bitboard();
        Direction[] directions = PAWN.getColoredPiece(color).getStepMoves();
        for (Direction direction : directions) {
          Square toSquare = square.getNextSquare(direction);
          if (toSquare != null) {
            PawnEffectBB[color.ordinal()][square.ordinal()].or(SquareBB[toSquare.ordinal()]);
          }
        }
      }
    }

    // create KNIGHT effects bitboards
    for (final Color color : Color.getColors()) {
      for (final Square square : Square.getSquares()) {
        KnightEffectBB[color.ordinal()][square.ordinal()] = new Bitboard();
        Direction[] directions = KNIGHT.getColoredPiece(color).getStepMoves();
        for (Direction direction : directions) {
          Square toSquare = square.getNextSquare(direction);
          if (toSquare != null) {
            KnightEffectBB[color.ordinal()][square.ordinal()].or(SquareBB[toSquare.ordinal()]);
          }
        }
      }
    }

    // create SILVER effects bitboards
    for (final Color color : Color.getColors()) {
      for (final Square square : Square.getSquares()) {
        SilverEffectBB[color.ordinal()][square.ordinal()] = new Bitboard();
        Direction[] directions = SILVER.getColoredPiece(color).getStepMoves();
        for (Direction direction : directions) {
          Square toSquare = square.getNextSquare(direction);
          if (toSquare != null) {
            SilverEffectBB[color.ordinal()][square.ordinal()].or(SquareBB[toSquare.ordinal()]);
          }
        }
      }
    }

    // create GOLD effects bitboards
    for (final Color color : Color.getColors()) {
      for (final Square square : Square.getSquares()) {
        GoldEffectBB[color.ordinal()][square.ordinal()] = new Bitboard();
        Direction[] directions = GOLD.getColoredPiece(color).getStepMoves();
        for (Direction direction : directions) {
          Square toSquare = square.getNextSquare(direction);
          if (toSquare != null) {
            GoldEffectBB[color.ordinal()][square.ordinal()].or(SquareBB[toSquare.ordinal()]);
          }
        }
      }
    }

    // create KING effects bitboards
    for (final Square square : Square.getSquares()) {
      KingEffectBB[square.ordinal()] = new Bitboard();
      Direction[] directions = B_KING.getStepMoves();
      for (Direction direction : directions) {
        Square toSquare = square.getNextSquare(direction);
        if (toSquare != null) {
          KingEffectBB[square.ordinal()].or(SquareBB[toSquare.ordinal()]);
        }
      }
    }

    // create BISHOP step effects bitboards
    for (final Square square : Square.getSquares()) {
      BishopStepEffectBB[square.ordinal()] = new Bitboard();
      Direction[] directions = B_BISHOP.getSlidingMoves();
      for (Direction direction : directions) {
        Square toSquare = square.getNextSquare(direction);
        if (toSquare != null) {
          BishopStepEffectBB[square.ordinal()].or(SquareBB[toSquare.ordinal()]);
        }
      }
    }

    // create ROOK step effects bitboards
    for (final Square square : Square.getSquares()) {
      RookStepEffectBB[square.ordinal()] = new Bitboard();
      Direction[] directions = B_ROOK.getSlidingMoves();
      for (Direction direction : directions) {
        Square toSquare = square.getNextSquare(direction);
        if (toSquare != null) {
          RookStepEffectBB[square.ordinal()].or(SquareBB[toSquare.ordinal()]);
        }
      }
    }
  }

  // generates Mask Bitboards for Bishop and Rook
  static {
    for (final Square square : Square.getSquares()) {
      int rank = square.getRank().ordinal();
      int file = square.getFile().ordinal();
      BishopMaskBB[square.ordinal()] = new Bitboard();

      for (final Square s : Square.getSquares()) {
        int r = s.getRank().ordinal();
        int f = s.getFile().ordinal();
        if (Math.abs(rank - r) == Math.abs(file - f)) {
          BishopMaskBB[square.ordinal()].or(SquareBB[s.ordinal()]);
        }
      }

      BishopMaskBB[square.ordinal()].andNot(RANK1_BB);
      BishopMaskBB[square.ordinal()].andNot(RANK9_BB);
      BishopMaskBB[square.ordinal()].andNot(FILE1_BB);
      BishopMaskBB[square.ordinal()].andNot(FILE9_BB);
      BishopMaskBB[square.ordinal()].andNot(SquareBB[square.ordinal()]);
    }

    for (final Square square : Square.getSquares()) {
      RookMaskBB[square.ordinal()] = new Bitboard();
      RookMaskBB[square.ordinal()].or(RANK_BB[square.getRank().ordinal()]);
      RookMaskBB[square.ordinal()].or(FILE_BB[square.getFile().ordinal()]);

      if (square.getRank() != Rank.RANK_1) {
        RookMaskBB[square.ordinal()].andNot(RANK1_BB);
      }
      if (square.getRank() != Rank.RANK_9) {
        RookMaskBB[square.ordinal()].andNot(RANK9_BB);
      }
      if (square.getFile() != File.FILE_1) {
        RookMaskBB[square.ordinal()].andNot(FILE1_BB);
      }
      if (square.getFile() != File.FILE_9) {
        RookMaskBB[square.ordinal()].andNot(FILE9_BB);
      }
      RookMaskBB[square.ordinal()].andNot(SquareBB[square.ordinal()]);
    }
  }

  // generates Bishop effects bitboards
  static {
    int index = 0;
    for (Square square : Square.getSquares()) {
      BishopAttackIndex[square.ordinal()] = index;
      for (int i = 0; i < 1 << BishopBlockBits[square.ordinal()]; i++) {
        Bitboard occ = computeOccupancy(BishopMaskBB[square.ordinal()], i);
        BishopAttack[index + occupiedToIndex(occ, BishopMagics[square.ordinal()], BishopShift[square.ordinal()])]
            = computeSlidingAttacks(Piece.B_BISHOP, square, occ);
      }
      index += 1 << (64 - BishopShift[square.ordinal()]);
    }
  }

  // generates Rook effects bitboards
  static {
    int index = 0;
    for (Square square : Square.getSquares()) {
      RookAttackIndex[square.ordinal()] = index;
      for (int i = 0; i < 1 << RookBlockBits[square.ordinal()]; i++) {
        Bitboard occ = computeOccupancy(RookMaskBB[square.ordinal()], i);
        RookAttack[index + occupiedToIndex(occ, RookMagics[square.ordinal()], RookShift[square.ordinal()])]
            = computeSlidingAttacks(Piece.B_ROOK, square, occ);
      }
      index += 1 << (64 - RookShift[square.ordinal()]);
    }
  }

  // generates Lance effects bitboards
  static {
    ForwardRanksBB[Color.BLACK.ordinal()] = new Bitboard[] {
        ZERO_BB,
        RANK1_BB,
        or(RANK1_BB, RANK2_BB),
        or(or(RANK1_BB, RANK2_BB), RANK3_BB),
        or(or(or(RANK1_BB, RANK2_BB), RANK3_BB), RANK4_BB),
        or(or(or(or(RANK1_BB, RANK2_BB), RANK3_BB), RANK4_BB), RANK5_BB),
        or(or(or(or(or(RANK1_BB, RANK2_BB), RANK3_BB), RANK4_BB), RANK5_BB), RANK6_BB),
        or(or(or(or(or(or(RANK1_BB, RANK2_BB), RANK3_BB), RANK4_BB), RANK5_BB), RANK6_BB), RANK7_BB),
        or(or(or(or(or(or(or(RANK1_BB, RANK2_BB), RANK3_BB), RANK4_BB), RANK5_BB), RANK6_BB), RANK7_BB), RANK8_BB),
    };

    ForwardRanksBB[Color.WHITE.ordinal()] = new Bitboard[] {
        or(or(or(or(or(or(or(RANK9_BB, RANK8_BB), RANK7_BB), RANK6_BB), RANK5_BB), RANK4_BB), RANK3_BB), RANK2_BB),
        or(or(or(or(or(or(RANK9_BB, RANK8_BB), RANK7_BB), RANK6_BB), RANK5_BB), RANK4_BB), RANK3_BB),
        or(or(or(or(or(RANK9_BB, RANK8_BB), RANK7_BB), RANK6_BB), RANK5_BB), RANK4_BB),
        or(or(or(or(RANK9_BB, RANK8_BB), RANK7_BB), RANK6_BB), RANK5_BB),
        or(or(or(RANK9_BB, RANK8_BB), RANK7_BB), RANK6_BB),
        or(or(RANK9_BB, RANK8_BB), RANK7_BB),
        or(RANK9_BB, RANK8_BB),
        RANK9_BB,
        ZERO_BB
    };
  }

  /**
   * Returns a PAWN bitboard with the specified color and square.
   *
   * @param color color of the bitboard
   * @param square square the piece exists
   * @return  the effects bitboard
   */
  public static Bitboard getPawnEffectBitboard(final Color color, final Square square) {
    return PawnEffectBB[color.ordinal()][square.ordinal()];
  }

  /**
   * Returns a KNIGHT bitboard with the specified color and square.
   *
   * @param color color of the bitboard
   * @param square square the piece exists
   * @return  the effects bitboard
   */
  public static Bitboard getKnightEffectBitboard(final Color color, final Square square) {
    return KnightEffectBB[color.ordinal()][square.ordinal()];
  }

  /**
   * Returns a SILVER bitboard with the specified color and square.
   *
   * @param color color of the bitboard
   * @param square square the piece exists
   * @return  the effects bitboard
   */
  public static Bitboard getSilverEffectBitboard(final Color color, final Square square) {
    return SilverEffectBB[color.ordinal()][square.ordinal()];
  }

  /**
   * Returns a GOLD bitboard with the specified color and square.
   *
   * @param color color of the bitboard
   * @param square square the piece exists
   * @return  the effects bitboard
   */
  public static Bitboard getGoldEffectBitboard(final Color color, final Square square) {
    return GoldEffectBB[color.ordinal()][square.ordinal()];
  }

  /**
   * Returns a KING bitboard with the specified square.
   *
   * @param square square the piece exists
   * @return  the effects bitboard
   */
  public static Bitboard getKingEffectBitboard(final Square square) {
    return KingEffectBB[square.ordinal()];
  }

  /**
   * Returns a BISHOP step bitboard with the specified square.
   *
   * @param square square the piece exists
   * @return  the effects bitboard
   */
  public static Bitboard getBishopStepEffectBitboard(final Square square) {
    return BishopStepEffectBB[square.ordinal()];
  }

  /**
   * Returns a ROOK step bitboard with the specified square.
   *
   * @param square square the piece exists
   * @return  the effects bitboard
   */
  public static Bitboard getRookStepEffectBitboard(final Square square) {
    return RookStepEffectBB[square.ordinal()];
  }

  /**
   * Returns a BISHOP sliding effect bitboard with the specified square and the occupied bitboard.
   *
   * @param square square the piece exists
   * @param occupied  occupied bitboard
   * @return  the effects bitboard
   */
  public static Bitboard getBishopSlidingEffectBitboard(final Square square, final Bitboard occupied) {
    Bitboard block = and(occupied, BishopMaskBB[square.ordinal()]);
    return BishopAttack[BishopAttackIndex[square.ordinal()]
        + occupiedToIndex(block, BishopMagics[square.ordinal()], BishopShift[square.ordinal()])];
  }

  /**
   * Returns a ROOK sliding effect bitboard with the specified square and the occupied bitboard.
   *
   * @param square square the piece exists
   * @param occupied  occupied bitboard
   * @return  the effects bitboard
   */
  public static Bitboard getRookSlidingEffectBitboard(final Square square, final Bitboard occupied) {
    Bitboard block = and(occupied, RookMaskBB[square.ordinal()]);
    return RookAttack[RookAttackIndex[square.ordinal()]
        + occupiedToIndex(block, RookMagics[square.ordinal()], RookShift[square.ordinal()])];
  }

  /**
   * Returns a new LANCE sliding effect bitboard with the specified color and square
   * and the occupied bitboard.
   *
   * @param color color of the lance
   * @param square square the piece exists
   * @param occupied  occupied bitboard
   * @return  the effects bitboard
   */
  public static Bitboard newLanceSlidingEffectBitboard(final Color color, final Square square,
      final Bitboard occupied) {
    return and(getRookSlidingEffectBitboard(square, occupied),
        ForwardRanksBB[color.ordinal()][square.getRank().ordinal()]);
  }

  // calculate the index of the sliding array from the occupied bitboard
  private static int occupiedToIndex(Bitboard occ, long magic, int shiftBits) {
    return (int) ((occ.merge() * magic) >>> shiftBits);
  }

  /**
   * Returns the Occupied Bitboard of a specific pattern on the Mask Bitboard.
   *
   * @param mask  the Mask Bitboard
   * @param index index of the occupied bitboard
   * @return the occupied bitboard
   */
  private static Bitboard computeOccupancy(Bitboard mask, int index) {
    Bitboard maskCopy = mask.newInstance();
    Bitboard occ = new Bitboard();
    int count = 0;
    while (maskCopy.hasNext()) {
      Square square = maskCopy.getNextSquare();
      if ((index & (1 << count)) != 0) {
        occ.or(SquareBB[square.ordinal()]);
      }
      count++;
    }
    return occ;
  }

  /**
   * Returns a sliding effects bitboard with the specified piece, the square, the occupied bitboard.
   *
   * @param piece piece that has sliding effects
   * @param fromSquare  square the piece exists
   * @param occupied  the occupied bitboard
   * @return the sliding effects bitboard
   */
  private static Bitboard computeSlidingAttacks(Piece piece, Square fromSquare, Bitboard occupied) {
    Bitboard attacksBB = new Bitboard();
    Direction[] directions = piece.getSlidingMoves();

    for (Direction direction: directions) {
      Square pre = fromSquare;
      while (true) {
        Square next = pre.getNextSquare(direction);
        if (next == null) {
          break;
        }

        attacksBB.or(SquareBB[next.ordinal()]);
        if (!and(occupied, SquareBB[next.ordinal()]).isZero()) {
          break;
        }

        pre = next;
      }
    }

    return attacksBB;
  }

}

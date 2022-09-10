package com.github.hayanige.shogilib;

import java.util.List;

public class Perft {

  public static void main(String[] args) {
    Position position = Position.createHiratePosition();
    System.out.println("nodes: " + perftWithTime(position, 1, false).getNodes());
    System.out.println("nodes: " + perftWithTime(position, 2, false).getNodes());
    System.out.println("nodes: " + perftWithTime(position, 3, false).getNodes());
    System.out.println("nodes: " + perftWithTime(position, 4, false).getNodes());
    System.out.println("nodes: " + perftWithTime(position, 5, false).getNodes());
    System.out.println("nodes: " + perftWithTime(position, 6, false).getNodes());
  }

  public static PerftResult perftWithTime(final Position position, final int depth,
      final boolean detail) {
    long start = System.currentTimeMillis();
    PerftResult result = perft(position, depth, detail);
    long end = System.currentTimeMillis();
    System.out.printf("Time[s]: %f\n", (end - start) / 1000.0);
    return result;
  }

  static PerftResult perft(final Position position, final int depth, final boolean detail) {
    PerftResult result = new PerftResult();
    if (depth == 0) {
      result.nodes++;
      if (detail) {
        if (position.getLastCapturedPiece() != Piece.NO_PIECE) {
          result.captures++;
        }
        if (position.getLastMove().isPromote()) {
          result.promotions++;
        }
        if (position.isKingAttacked()) {
          result.checks++;
          if (position.isMated()) {
            result.checkmates++;
          }
        }
      }
    } else {
      List<Move> moves = position.getLegalMoves();
      for (Move move: moves) {
        position.doMove(move);
        result.add(perft(position, depth - 1, detail));
        position.undoMove();
      }
    }
    return result;
  }

  static class PerftResult {
    private long nodes;
    private long captures;
    private long promotions;
    private long checks;
    private long checkmates;

    void add(PerftResult result) {
      nodes += result.nodes;
      captures += result.captures;
      promotions += result.promotions;
      checks += result.checks;
      checkmates += result.checkmates;
    }

    long getNodes() {
      return nodes;
    }

    long getCaptures() {
      return captures;
    }

    long getPromotions() {
      return promotions;
    }

    long getChecks() {
      return checks;
    }

    long getCheckmates() {
      return checkmates;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("nodes:\t").append(nodes).append("\n")
          .append("caps:\t\t").append(captures).append("\n")
          .append("promos:\t").append(promotions).append("\n")
          .append("checks:\t").append(checks).append("\n")
          .append("mates:\t").append(checkmates).append("\n");
      return sb.toString();
    }
  }
}

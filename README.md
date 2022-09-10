# Shogilib

Shogilib is a simple Shogi library written in 100% pure Java with didactic intent. It provides Shogi Java models and useful APIs, and generates legal moves from a position.

Shogilib is implemented with reference to the following open source software.
- [YaneuraOu](https://github.com/yaneurao/YaneuraOu)
- [Gikou](https://github.com/gikou-official/Gikou)
- [Apery](https://github.com/HiraokaTakuya/apery)
- [chesslib](https://github.com/bhlangonijr/chesslib)
- [pulse](https://github.com/fluxroot/pulse)

[![Maven build](https://github.com/hayanige/shogilib/actions/workflows/push.yml/badge.svg?branch=main)](https://github.com/hayanige/shogilib/actions/workflows/push.yml)
[![JitPack build](https://jitpack.io/v/hayanige/shogilib.svg)](https://jitpack.io/#hayanige/shogilib)
---


# License

Shogilib is released under GPLv3.

---

# Set up

## Build from source

```
$ git clone git@github.com:hayanige/shogilib.git
$ cd shoglib/
$ mvn clean install
```

## Build with Maven from JitPack repository

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

```xml
<dependency>
  <groupId>com.github.hayanige</groupId>
  <artifactId>shogilib</artifactId>
  <version>(,1.0)</version>
</dependency>
```

# How To Use

## Create a Position

To create a Hirate position.
```java
Position position = Position.createHiratePosition();
```

To create a position from SFEN.
```java
Position position = Position.createPositionFromSfen("lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1");
```

## Print a Position

`Position#toString()` prints a simple output.

```
> System.out.println(position);

Side: BLACK
WHITE Hand:
 l n s g k g s n l
 . r . . . . . b .
 p p p p p p p p p
 . . . . . . . . .
 . . . . . . . . .
 . . . . . . . . .
 P P P P P P P P P
 . B . . . . . R .
 L N S G K G S N L
BLACK Hand:
```

`Position#pretty()` prints a detailed output with Japanese Kanji.

```
> System.out.println(position.pretty());

手番: 先手
手数: 0手目
最終手: 無
後手の持ち駒: 歩:0, 香:0, 桂:0, 銀:0, 金:0, 角:0, 飛:0
^香^桂^銀^金^王^金^銀^桂^香
 口^飛 口 口 口 口 口^角 口
^歩^歩^歩^歩^歩^歩^歩^歩^歩
 口 口 口 口 口 口 口 口 口
 口 口 口 口 口 口 口 口 口
 口 口 口 口 口 口 口 口 口
 歩 歩 歩 歩 歩 歩 歩 歩 歩
 口 角 口 口 口 口 口 飛 口
 香 桂 銀 金 王 金 銀 桂 香
先手の持ち駒: 歩:0, 香:0, 桂:0, 銀:0, 金:0, 角:0, 飛:0
```

## Get all legal moves from the current position

```java
List<Move> moves = position.getLegalMoves();
```
```
> System.out.println(moves);

[1g1f, 2g2f, 3g3f, 4g4f, 5g5f, 6g6f, 7g7f, 8g8f, 9g9f, 3i3h, 3i4h, 7i6h, 7i7h, 4i3h, 4i4h, 4i5h, 6i5h, 6i6h, 6i7h, 5i4h, 5i5h, 5i6h, 1i1h, 9i9h, 2h1h, 2h3h, 2h4h, 2h5h, 2h6h, 2h7h]
```

## Create a move

Create a move from USI string or Shogi model objects.

```java
Move move1 = Move.makeMoveUSI("7g7f");
Move move2 = Move.makeMove(Square.SQ_33, Square.SQ_34);
Move move3 = Move.makeMovePromote(Square.SQ_88, Square.SQ_22);
Move move4 = Move.makeMove(Square.SQ_31, Square.SQ_22);
Move move5 = Move.makeMoveDrop(PieceType.BISHOP, Square.SQ_55);
```

## Do a move

```java
position.doMove(move1);
position.doMove(move2);
position.doMove(move3);
position.doMove(move4);
position.doMove(move5);
```
```
> System.out.println(position);

Side: WHITE
WHITE Hand: b
 l n s g k g . n l
 . r . . . . . s .
 p p p p p p . p p
 . . . . . . p . .
 . . . . B . . . .
 . . P . . . . . .
 P P . P P P P P P
 . . . . . . . R .
 L N S G K G S N L
BLACK Hand:
```

## Undo a move

```java
position.undoMove();
position.undoMove();
```
```
> System.out.println(position);

Side: WHITE
WHITE Hand:
 l n s g k g s n l
 . r . . . . . +B .
 p p p p p p . p p
 . . . . . . p . .
 . . . . . . . . .
 . . P . . . . . .
 P P . P P P P P P
 . . . . . . . R .
 L N S G K G S N L
BLACK Hand: B
```

## Get a SFEN

```
> System.out.println(sfen.getSfen());

lnsgkgsnl/1r5+B1/pppppp1pp/6p2/9/2P6/PP1PPPPPP/7R1/LNSGKGSNL w B 4
```

## Other APIs

- `position.isKingAttacked()`
- `position.isMated()`
- `position.isRepetition()`
- `position.getSideToMove()`
- `position.getPiece(Square.SQ_11)`
- `position.getPiece(File.FILE_7, Rank.RANK_6)`
- `position.getNumberOfPieceInHand(Color.BLACK, PieceType.ROOK)`
- `position.getLastMove()`
- `position.getMoveCounter()`
- `position.getZobristHash()`
- ...

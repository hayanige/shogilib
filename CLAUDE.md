# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Test Commands

### Build
```bash
mvn clean install          # Clean build with tests
mvn clean compile          # Compile only
mvn clean package          # Create JAR without installing
```

### Testing
```bash
mvn test                   # Run all tests
mvn test -Dtest=TestClassName    # Run specific test class
mvn test -Dtest=TestClassName#testMethodName    # Run specific test method
```

### Code Analysis
```bash
mvn clean test             # Run tests with 4 parallel forks (configured in pom.xml)
```

## Architecture Overview

### Core Design Pattern: Interface + Bitboard Implementation

The codebase follows a clean separation between API interfaces and high-performance implementations:

1. **Position Interface** (`Position.java`)
   - Main API entry point for all position operations
   - Factory methods: `createHiratePosition()`, `createPositionFromSfen()`
   - Consumers interact primarily through this interface

2. **PositionBitboardImpl** (`bitboard/PositionBitboardImpl.java`)
   - High-performance implementation using 81-bit custom bitboards
   - Bitboards split across two longs (63 + 18 bits) for the 9x9 board
   - Incremental Zobrist hashing for position identification

3. **BitboardMoveGenerator** (`bitboard/BitboardMoveGenerator.java`)
   - Separated move generation logic
   - Two-phase generation: pseudo-legal → legal filtering
   - Uses pre-computed magic bitboards for sliding pieces

### Key Architectural Decisions

1. **Immutable Move Encoding**: Moves encoded in 16 bits for memory efficiency
   - Bits 0-6: destination, 7-13: origin/piece, 14: drop flag, 15: promotion

2. **State Management**: Position tracks:
   - Board array + piece bitboards
   - Captured pieces (hands)
   - Move history with captured piece info
   - Zobrist hash history for repetition detection

3. **Shogi-Specific Rules**: Implements all official rules including:
   - Drop moves for captured pieces
   - Promotion zones (last 3 ranks)
   - Nifu (double pawn) prevention
   - Drop pawn mate detection
   - Sennichite (repetition) detection

### Performance Considerations

- Bitboard operations for bulk move generation
- Pre-computed constants in `BitboardConstants`
- Cached king positions
- Incremental updates instead of full recalculation

## Development Notes

- Uses Java 23 (configured in pom.xml)
- JUnit 5 for testing
- No runtime dependencies (pure Java)
- Parallel test execution (4 forks) for faster test runs
- UTF-8 encoding throughout
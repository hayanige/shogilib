package com.github.hayanige.shogilib;

import static com.github.hayanige.shogilib.Perft.perftWithTime;

import com.github.hayanige.shogilib.Perft.PerftResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Validate the number of moves from Hirate(Starting) Position.
 * See also: https://qiita.com/ak11/items/8bd5f2bb0f5b014143c8
 */
public class TestHirateDepth {

  @Test
  public void testHirateDepth1() {
    Position position = Position.createHiratePosition();
    PerftResult result = perftWithTime(position, 1, true);
    Assertions.assertEquals(30, result.getNodes());
    Assertions.assertEquals(0, result.getCaptures());
    Assertions.assertEquals(0, result.getPromotions());
    Assertions.assertEquals(0, result.getChecks());
    Assertions.assertEquals(0, result.getCheckmates());
  }

  @Test
  public void testHirateDepth2() {
    Position position = Position.createHiratePosition();
    PerftResult result = perftWithTime(position, 2, true);
    Assertions.assertEquals(900, result.getNodes());
    Assertions.assertEquals(0, result.getCaptures());
    Assertions.assertEquals(0, result.getPromotions());
    Assertions.assertEquals(0, result.getChecks());
    Assertions.assertEquals(0, result.getCheckmates());
  }

  @Test
  public void testHirateDepth3() {
    Position position = Position.createHiratePosition();
    PerftResult result = perftWithTime(position, 3, true);
    Assertions.assertEquals(25470, result.getNodes());
    Assertions.assertEquals(59, result.getCaptures());
    Assertions.assertEquals(30, result.getPromotions());
    Assertions.assertEquals(48, result.getChecks());
    Assertions.assertEquals(0, result.getCheckmates());
  }

  @Test
  public void testHirateDepth4() {
    Position position = Position.createHiratePosition();
    PerftResult result = perftWithTime(position, 4, true);
    Assertions.assertEquals(719731, result.getNodes());
    Assertions.assertEquals(1803, result.getCaptures());
    Assertions.assertEquals(842, result.getPromotions());
    Assertions.assertEquals(1121, result.getChecks());
    Assertions.assertEquals(0, result.getCheckmates());
  }

  @Test
  public void testHirateDepth5() {
    Position position = Position.createHiratePosition();
    PerftResult result = perftWithTime(position, 5, true);
    Assertions.assertEquals(19861490, result.getNodes());
    Assertions.assertEquals(113680, result.getCaptures());
    Assertions.assertEquals(57214, result.getPromotions());
    Assertions.assertEquals(71434	, result.getChecks());
    Assertions.assertEquals(0, result.getCheckmates());
  }

  @Test
  public void testHirateDepth6() {
    Position position = Position.createHiratePosition();
    PerftResult result = perftWithTime(position, 6, true);
    Assertions.assertEquals(547581517, result.getNodes());
    Assertions.assertEquals(3387051, result.getCaptures());
    Assertions.assertEquals(1588324, result.getPromotions());
    Assertions.assertEquals(1730177, result.getChecks());
    Assertions.assertEquals(0, result.getCheckmates());
  }

  @Disabled
  @Test
  public void testHirateDepth7() {
    Position position = Position.createHiratePosition();
    PerftResult result = perftWithTime(position, 7, true);
    Assertions.assertEquals(15086269607L, result.getNodes());
    Assertions.assertEquals(156289904, result.getCaptures());
    Assertions.assertEquals(78496954, result.getPromotions());
    Assertions.assertEquals(79636812, result.getChecks());
    Assertions.assertEquals(29, result.getCheckmates());
  }
}

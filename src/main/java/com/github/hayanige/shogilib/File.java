package com.github.hayanige.shogilib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The files in a board. It is called Dan in Japanese.
 * A <i>file</i> is a column in the board, and it is identified as a letter from 1 to 9.
 */
public enum File {
  FILE_1("1", "１"),
  FILE_2("2", "２"),
  FILE_3("3", "３"),
  FILE_4("4", "４"),
  FILE_5("5", "５"),
  FILE_6("6", "６"),
  FILE_7("7", "７"),
  FILE_8("8", "８"),
  FILE_9("9", "９");

  private final String usiString;
  private final String prettyString;

  private static final File[] FILES = File.values();
  public static final int FILES_LENGTH = FILES.length;

  private static final Map<String, File> usi2File = new HashMap<>();
  static {
    for (File file : FILES) {
      usi2File.put(file.usiString, file);
    }
  }

  File(final String usiString, final String prettyString) {
    this.usiString = usiString;
    this.prettyString = prettyString;
  }

  /**
   * Returns the array containing all files in ordinal order.
   *
   * @return the list containing all files
   */
  public static List<File> getFiles() {
    return List.of(FILES);
  }

  /**
   * Returns a file given its ordinal.
   *
   * @param ordinal ordinal of the file
   * @return  the file
   */
  public static File valueOf(final int ordinal) {
    return FILES[ordinal];
  }

  /**
   * Returns a file given its USI string.
   *
   * @param usi usi string of the file
   * @return  the file (null if the usi is invalid)
   */
  public static File parseUSI(final String usi) {
    return usi2File.get(usi);
  }

  /**
   * Returns the USI string of the file.
   *
   * @return the USI string
   */
  @Override
  public String toString() {
    return usiString;
  }

  /**
   * Returns the Japanese string of the file.
   *
   * @return the Japanese string
   */
  public String pretty() {
    return prettyString;
  }
}

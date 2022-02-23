package king.squares.pixelwidth;

import net.kyori.adventure.text.format.Style;

/**
 * A function that takes a character(represented by its UTF-16 codepoint) and a {@link Style} and returns
 * the characters width as an {@code int}.
 *
 * <p>Should return {@code -1} if the character width is unknown to this function</p>
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface CharacterWidthFunction {
  /**
   * Gets the width for the given character(represented by its UTF-16 codepoint). {@code char}s will
   * automatically be converted to codepoints.
   *
   * @since 1.0.0
   */
  float widthOf(int codepoint, Style style);
}


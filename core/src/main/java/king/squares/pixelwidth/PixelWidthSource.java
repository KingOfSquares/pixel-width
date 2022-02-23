package king.squares.pixelwidth;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A source able to return the width of text with the usage of {@link CharacterWidthFunction}s.
 *
 * @since 1.0.0
 */
public interface PixelWidthSource {

  /**
   * A pixel width source calculating width. Returns a static instance.
   *
   * @return a pixel width source
   * @since 1.0.0
   */
  static @NotNull PixelWidthSource pixelWidth() {
    return PixelWidthSourceImpl.INSTANCE;
  }

  /**
   * A pixel width source calculating width using the provided flattener.
   *
   * @param flattener used to turn components into linear text
   * @return a pixel width source
   * @since 1.0.0
   */
  static @NotNull PixelWidthSource pixelWidth(final @NotNull ComponentFlattener flattener) {
    return pixelWidth(flattener, DefaultCharacterWidthFunction.INSTANCE);
  }

  /**
   * A pixel width source calculating width using the provided character width function.
   *
   * @param characterWidthFunction used to get width values for each character
   * @return a pixel width source
   * @since 1.0.0
   */
  static @NotNull PixelWidthSource pixelWidth(final @NotNull CharacterWidthFunction characterWidthFunction) {
    return pixelWidth(ComponentFlattener.basic(), characterWidthFunction);
  }

  /**
   * A pixel width source calculating width using the provided flattener and character width function.
   *
   * @param flattener              used to turn components into linear text
   * @param characterWidthFunction used to get width values for each character
   * @return a pixel width source
   * @since 1.0.0
   */
  static @NotNull PixelWidthSource pixelWidth(final @NotNull ComponentFlattener flattener, final @NotNull CharacterWidthFunction characterWidthFunction) {
    Objects.requireNonNull(flattener, "flattener");
    Objects.requireNonNull(characterWidthFunction, "characterWidthFunction");
    return new PixelWidthSourceImpl<>(flattener, cx -> characterWidthFunction);
  }

  /**
   * Calculates the pixel width of a component without any context.
   *
   * @param component a component
   * @return the pixel width of the component
   * @since 1.0.0
   */
  float width(final @NotNull Component component);

  /**
   * Calculates the pixel width of a string without any context.
   *
   * @param string a string
   * @param style  the style of the string
   * @return the pixel width of the string
   * @since 1.0.0
   */
  float width(final @NotNull String string, final @NotNull Style style);

  /**
   * Calculates the pixel width of a character without any context.
   *
   * @param character a character
   * @param style     the style of the character
   * @return the pixel width of the character
   * @since 1.0.0
   */
  float width(final char character, final @NotNull Style style);

  /**
   * Calculates the pixel width of a character represented by a codepoint without any context.
   *
   * @param codepoint a codepoint representing a character
   * @param style     the style of the character
   * @return the pixel width of the character
   * @since 1.0.0
   */
  float width(final int codepoint, final @NotNull Style style);
}


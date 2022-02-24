/*
 * This file is part of pixel-width, licensed under the MIT License.
 *
 * Copyright (c) 2022 KingOfSquares
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package solar.squares.pixelwidth;

import java.util.Objects;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A source able to return the width of text with the usage of {@link CharacterWidthFunction}s and a context {@code CX}.
 *
 * <p>If context is pointless use a generic {@link PixelWidthSource}</p>
 *
 * @param <CX> The context to take account for when choosing a Context to CharacterWidthFunction function
 * @since 1.0.0
 */
public interface ContextualPixelWidthSource<CX> extends PixelWidthSource {

  /**
   * A pixel width source calculating width using the provided flattener and character width function.
   *
   * @param flattener a flattener used to turn components into linear text
   * @param function  a function that provides a character width function
   * @param <CX>      context a context type (player, server, locale)
   * @return a pixel width source
   * @since 1.0.0
   */
  static <CX> @NotNull ContextualPixelWidthSource<CX> contextualPixelWidth(final @NotNull ComponentFlattener flattener, final @NotNull Function<@Nullable CX, @NotNull CharacterWidthFunction> function) {
    Objects.requireNonNull(flattener, "flattener");
    Objects.requireNonNull(function, "function");
    return new PixelWidthSourceImpl<>(flattener, function);
  }

  /**
   * A pixel width source calculating width using {@link ComponentFlattener#basic()} and the provided character width function.
   *
   * @param function a function that provides a character width function
   * @param <CX>     context a context type (player, server, locale)
   * @return a pixel width source
   * @since 1.0.0
   */
  static <CX> @NotNull ContextualPixelWidthSource<CX> contextualPixelWidth(final @NotNull Function<@Nullable CX, @NotNull CharacterWidthFunction> function) {
    return contextualPixelWidth(ComponentFlattener.basic(), function);
  }

  /**
   * Calculates the pixel width of a component, given a context.
   *
   * @param component a component
   * @param context   the context of this calculation
   * @return the pixel width of the component
   * @since 1.0.0
   */
  float width(final @NotNull Component component, final @Nullable CX context);

  @Override
  default float width(final @NotNull Component component) {
    return this.width(component, null);
  }

  /**
   * Calculates the pixel width of a string, given a context.
   *
   * @param string  a string
   * @param style   the style of the string
   * @param context the context of this calculation
   * @return the pixel width of the string
   * @since 1.0.0
   */
  float width(final @NotNull String string, final @NotNull Style style, final @Nullable CX context);

  @Override
  default float width(final @NotNull String string, final @NotNull Style style) {
    return this.width(string, style, null);
  }

  /**
   * Calculates the pixel width of a character, given a context.
   *
   * @param character a character
   * @param style     the style of the character
   * @param context   the context of this calculation
   * @return the pixel width of the character
   * @since 1.0.0
   */
  float width(final char character, final @NotNull Style style, final @Nullable CX context);

  @Override
  default float width(final char character, final @NotNull Style style) {
    return this.width(character, style, null);
  }

  /**
   * Calculates the pixel width of a character represented by a codepoint, given a context.
   *
   * @param codepoint a codepoint representing a character
   * @param style     the style of the character
   * @param context   the context of this calculation
   * @return the pixel width of the character
   * @since 1.0.0
   */
  float width(final int codepoint, final @NotNull Style style, final @Nullable CX context);

  @Override
  default float width(final int codepoint, final @NotNull Style style) {
    return this.width(codepoint, style, null);
  }
}

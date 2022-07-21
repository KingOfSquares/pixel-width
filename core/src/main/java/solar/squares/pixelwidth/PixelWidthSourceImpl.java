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

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solar.squares.pixelwidth.function.CharacterWidthFunction;

/**
 * An implementation of the pixel width source which handles serialization with a set of functions
 * for resolving all components except {@link TextComponent}s.
 *
 * @param <CX> a context (player, server, locale)
 * @since 1.0.0
 */

final class PixelWidthSourceImpl<CX> implements ContextualPixelWidthSource<CX> {

  static final PixelWidthSource INSTANCE = new PixelWidthSourceImpl<>(ComponentFlattener.basic(), cx -> DefaultCharacterWidthFunction.INSTANCE);

  private final ComponentFlattener flattener;
  private final Function<CX, CharacterWidthFunction> characterWidthFunction;

  /**
   * Creates a pixel width source with a function used for getting a {@link CharacterWidthFunction}.
   *
   * <p>Any {@link CharacterWidthFunction} returned by the function should accept at least all
   * english alphanumerics and most punctuation and handle {@link TextDecoration#BOLD} in the style.
   * See {@link DefaultCharacterWidthFunction#INSTANCE} for an example.</p>
   *
   * @param characterWidthFunction a function that can provide a {@link CharacterWidthFunction} given a context
   * @since 1.0.0
   */
  PixelWidthSourceImpl(final @NotNull ComponentFlattener flattener, final @NotNull Function<@Nullable CX, CharacterWidthFunction> characterWidthFunction) {
    this.flattener = flattener;
    this.characterWidthFunction = characterWidthFunction;
  }

  @Override
  public float width(final @NotNull Component component, final @Nullable CX context) {
    final float[] length = {0};

    this.flattener.flatten(component, new FlattenerListener() {
      final List<Style> styles = new LinkedList<>();
      Style currentStyle = Style.empty();

      @Override
      public void pushStyle(final @NotNull Style style) {
        this.styles.add(style);
        this.calculateStyle();
      }

      @Override
      public void component(final @NotNull String text) {
        length[0] += PixelWidthSourceImpl.this.width(text, this.currentStyle, context);
      }

      @Override
      public void popStyle(final @NotNull Style style) {
        this.styles.remove(this.styles.size() - 1);
        this.calculateStyle();
      }

      private void calculateStyle() {
        final Style.Builder newStyle = Style.style();
        for (final Style style : this.styles) {
          newStyle.merge(style);
        }
        this.currentStyle = newStyle.build();
      }
    });

    return length[0];
  }

  @Override
  public float width(final @NotNull String string, final @NotNull Style style, final @Nullable CX context) {
    return (float) string.codePoints().mapToDouble(codepoint -> this.characterWidthFunction.apply(context).widthOf(codepoint, style)).sum();
  }

  @Override
  public float width(final char c, final @NotNull Style style, final @Nullable CX context) {
    return this.characterWidthFunction.apply(context).widthOf(c, style);
  }

  @Override
  public float width(final int codepoint, final @NotNull Style style, final @Nullable CX context) {
    return this.characterWidthFunction.apply(context).widthOf(codepoint, style);
  }

  @Override
  public ComponentFlattener flattener() {
    return this.flattener;
  }
}


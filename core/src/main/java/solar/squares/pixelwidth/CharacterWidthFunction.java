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

import net.kyori.adventure.text.format.Style;

/**
 * A function that takes a character(represented by its UTF-16 codepoint) and a {@link Style} and returns
 * the characters width as an {@code int}.
 *
 * <p>Should return {@code -1} if the character width is unknown to this function</p>
 *
 * @deprecated for removal since 1.1.0, use {@link solar.squares.pixelwidth.function.CharacterWidthFunction} instead
 * @since 1.0.0
 */
@Deprecated
@FunctionalInterface
public interface CharacterWidthFunction {
  /**
   * Gets the width for the given character(represented by its UTF-16 codepoint). {@code char}s will
   * automatically be converted to codepoints.
   *
   * @deprecated for removal since 1.1.0, use {@link solar.squares.pixelwidth.function.CharacterWidthFunction#widthOf(int, Style)} instead
   * @since 1.0.0
   */
  @Deprecated
  float widthOf(final int codepoint, final Style style);
}


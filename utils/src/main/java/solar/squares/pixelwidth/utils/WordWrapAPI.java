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
package solar.squares.pixelwidth.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import solar.squares.pixelwidth.ContextualPixelWidthSource;
import solar.squares.pixelwidth.PixelWidthSource;

/**
 * API turning a single {@link Component} into a list by limiting the pixel width of each {@link Component}. By default
 * every set of characters that are not seperated by a space will be kept even though it does not max out the pixel width.
 *
 * @since 1.1.0
 */
public interface WordWrapAPI {

  char DEFAULT_WORD_SPLIT_CHAR = ' ';

  /**
   * Turn a single {@link Component} into a list. This is mostly internal
   *
   * @param component the component to wrap
   * @param splitChar the character to treat as the "word splitter", NULL character means that split could be done at any character(Should only be used for long words with no spaces)
   * @param flattener the flattener used to render the component
   * @param maxWidth the max width of each component
   * @return A list components not longer (in pixel width) than the provided max width
   * @since 1.1.0
   */
  @ApiStatus.Internal
  static List<Component> wrap(final @NotNull Component component, final char splitChar, final Function<Component, Float> pixelWidthFunction, final @NotNull ComponentFlattener flattener, final float maxWidth) {
    final List<Component> components = new LinkedList<>();
    final boolean ruthlessSplit = splitChar == 0;

    //Check if the given component actually needs to be wrapped
    if (pixelWidthFunction.apply(component) <= maxWidth) {
      final List<Component> list = new ArrayList<>(1); //java8 compat
      list.add(component);
      return list;
    }

    //Flattener resolves all non-text components, so we can treat all received text as normal text
    flattener.flatten(component.append(ruthlessSplit ? Component.space() : Component.text(splitChar))/*Done to properly detect the component end*/, new FlattenerListener() {
      final List<Style> styles = new LinkedList<>();
      Style currentStyle = Style.empty();
      TextComponent.Builder currentComponent = Component.text();
      TextComponent.Builder currentWordComponent = Component.text();
      StringBuilder currentStringPart = new StringBuilder();
      float currentComponentWidth = 0;
      StringBuilder currentWord = new StringBuilder();

      @Override
      public void pushStyle(final @NotNull Style style) {
        this.styles.add(style);
        this.calculateStyle();
        this.popStringPartToComponent();
      }

      @Override
      public void component(final @NotNull String text) {
          text.codePoints().forEach(i -> {
            this.appendString(i);
            if (i == splitChar || ruthlessSplit) { //See javadoc for special NULL char case
              this.addWord();
            }
          });
      }

      @Override
      public void popStyle(final @NotNull Style style) {
        this.styles.remove(this.styles.size() - 1);
        this.calculateStyle();
        this.popStringPartToComponent();
      }

      private void popStringPartToComponent() {
        this.currentWordComponent.append(Component.text(this.currentStringPart.toString(), this.currentStyle));
        this.currentStringPart = new StringBuilder();
      }

      private void appendString(final int codepoint) {
        this.currentStringPart.append(codepoint);
        this.currentWord.append(codepoint);
      }

      private void calculateStyle() {
        final Style.Builder newStyle = Style.style();
        for (final Style style : this.styles) {
          newStyle.merge(style);
        }
        this.currentStyle = newStyle.build();
      }

      private void addWord() {
        this.popStringPartToComponent(); //Add remaining string to component
        final Component wordComponent = this.currentWordComponent.build();
        final float newWordLength = pixelWidthFunction.apply(wordComponent);
        boolean wrapped = false;

        if (newWordLength > maxWidth) {
          //Cut long word into smaller pieces
          final List<Component> splitLongWord = wrap(wordComponent, (char) 0, pixelWidthFunction, flattener, maxWidth);

          final int sizeMinusOne = splitLongWord.size() - 1;
          for (int i = 0; i < sizeMinusOne; i++) {
            components.add(splitLongWord.get(i));
          }

          //Reset state, the next word will be in a new line that starts with the end of the previous long word
          this.currentComponent = Component.text().append(splitLongWord.get(sizeMinusOne));
          wrapped = true;
        } else if ((this.currentComponentWidth + newWordLength) >= maxWidth) {
          //Wrapping time
          components.add(this.currentComponent.build()); //Send component to list

          //Reset state, the next word will be in a new line
          this.currentComponent = Component.text().append(wordComponent);
          wrapped = true;
        } else { //TODO optimize for NULL char cases(there will be a lot of children)
          this.currentComponent.append(wordComponent);
        }

        if (ruthlessSplit && this.currentWord.toString().endsWith(" ")) {
          components.add(this.currentComponent.build()); //Send component to list
        }

        if (wrapped) this.currentComponentWidth = 0;

        this.currentWord = new StringBuilder();
        this.currentWordComponent = Component.text();
      }

    });

    return components;
  }

  /**
   * Turn a single {@link Component} into a list. This is mostly internal
   *
   * @param component the component to wrap
   * @param source the pixel width source used to calculate width of the component parts
   * @param context the context of this wrap action(player, server)
   * @param maxWidth the max width of each component
   * @param splitChar the character to treat as the "word splitter"
   * @return A list components not longer (in pixel width) than the provided max width
   * @since 1.1.0
   */
  static <CX> List<Component> wrap(final @NotNull Component component, final @NotNull ContextualPixelWidthSource<CX> source, CX context, final float maxWidth, char splitChar) {
    return wrap(component, splitChar, c -> source.width(c, context), source.flattener(), maxWidth);
  }

  /**
   * Turn a single {@link Component} into a list. Uses the space char as a "word splitter"
   *
   * @param component the component to wrap
   * @param source the pixel width source used to calculate width of the component parts
   * @param context the context of this wrap action(player, server)
   * @param maxWidth the max width of each component
   * @return A list components not longer (in pixel width) than the provided max width
   * @since 1.1.0
   */
  static <CX> List<Component> wrap(final @NotNull Component component, final @NotNull ContextualPixelWidthSource<CX> source, CX context, final float maxWidth) {
    return wrap(component, source, context, maxWidth, DEFAULT_WORD_SPLIT_CHAR);
  }

  /**
   * Turn a single {@link Component} into a list.
   *
   * @param component the component to wrap
   * @param source the pixel width source used to calculate width of the component parts
   * @param maxWidth the max width of each component
   * @param splitChar the character to treat as the "word splitter"
   * @return A list components not longer (in pixel width) than the provided max width
   * @since 1.1.0
   */
  static List<Component> wrap(final @NotNull Component component, final @NotNull PixelWidthSource source, final float maxWidth, char splitChar) {
    return wrap(component, splitChar, source::width, source.flattener(), maxWidth);
  }

  /**
   * Turn a single {@link Component} into a list. Uses the space char as a "word splitter"
   *
   * @param component the component to wrap
   * @param source the pixel width source used to calculate width of the component parts
   * @param maxWidth the max width of each component
   * @return A list components not longer (in pixel width) than the provided max width
   * @since 1.1.0
   */
  static List<Component> wrap(final @NotNull Component component, final @NotNull PixelWidthSource source, final float maxWidth) {
    return wrap(component, source, maxWidth, DEFAULT_WORD_SPLIT_CHAR);
  }

  /**
   * Turn a single {@link Component} into a list. Uses the space char as a "word splitter" and the default
   * PixelWidthSource
   *
   * @param component the component to wrap
   * @param maxWidth the max width of each component
   * @return A list components not longer (in pixel width) than the provided max width
   * @since 1.1.0
   */
  static List<Component> wrap(final @NotNull Component component, final float maxWidth) {
    return wrap(component, PixelWidthSource.pixelWidth(), maxWidth);
  }

}

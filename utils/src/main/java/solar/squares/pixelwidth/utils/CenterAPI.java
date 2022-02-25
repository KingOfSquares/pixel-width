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

import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import solar.squares.pixelwidth.ContextualPixelWidthSource;
import solar.squares.pixelwidth.PixelWidthSource;

/**
 * API for centering text in a component with various possibilities for complexity.
 *
 * @since 1.0.0
 */
//TODO Use Service to provide component flattener(resolve custom components)
//TODO document throws IllegalArgumentException
public interface CenterAPI {

  int DEFAULT_CHAT_WIDTH = 320;

  /**
   * Center a component with a padding used to add space on both sides of the component. This is mostly internal
   *
   * @param component The component to center
   * @param componentWidth The width of the component
   * @param padding the text to use as padding
   * @param paddingWidth the width of the padding text
   * @param goalWidth the width of the chat this component is getting shown in
   * @return a component with padding to hopefully center it
   * @throws IllegalArgumentException if padding is too wide to fit on both sides of the center component at least once
   * @since 1.0.0
   */
  static Component center(final @NotNull Component component, final float componentWidth, final @NotNull TextComponent padding, final float paddingWidth, final float goalWidth) {
    final float widthToPad = (goalWidth - componentWidth) / 2;
    if (paddingWidth > widthToPad)
      throw new IllegalArgumentException("Padding component is too big to fit at least once on both sides of the center component at least once. Max for given max width(" + goalWidth + ") is " + widthToPad + " Was " + paddingWidth);
    final StringBuilder paddingBuilder = new StringBuilder();
    //We use a StringBuilder to circumvent creating a Component with lots of unnecessary children
    final String content = padding.content();
    for (float i = paddingWidth; i < widthToPad; i += paddingWidth) {
      paddingBuilder.append(content);
    }
    final Component fullPadding = Component.text(paddingBuilder.toString(), padding.style());
    //Siblings to prevent accidental style bleeding
    return Component.text().append(fullPadding).append(component).append(fullPadding).build();
  }

  /**
   * Center a component with a padding used to add space on both sides of the component.
   *
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @param context the context of the pixel width calculation
   * @param padding the text to use as padding
   * @param contextToChatWidthFunction function to find a chat width given the context
   * @return a component with padding to hopefully center it
   * @throws IllegalArgumentException if padding is too wide to fit on both sides of the center component at least once
   * @since 1.0.0
   */
  static <CX> Component center(final @NotNull Component component, final @NotNull ContextualPixelWidthSource<CX> source, final @NotNull CX context, final @NotNull TextComponent padding, Function<CX, Float> contextToChatWidthFunction) {
    final float componentWidth = source.width(component, context);
    final float paddingWidth = source.width(padding, context);
    return center(component, componentWidth, padding, paddingWidth, contextToChatWidthFunction.apply(context));
  }

  /**
   * Center a component with a padding used to add space on both sides of the component.
   *
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @param context the context of the pixel width calculation
   * @param padding the text to use as padding
   * @param chatWidth the width of the chat this component is getting shown in
   * @return a component with padding to hopefully center it
   * @throws IllegalArgumentException if padding is too wide to fit on both sides of the center component at least once
   * @since 1.0.0
   */
  static <CX> Component center(final @NotNull Component component, final @NotNull ContextualPixelWidthSource<CX> source, final @NotNull CX context, final @NotNull TextComponent padding, final float chatWidth) {
    return center(component, source, context, padding, cx -> chatWidth);
  }

  /**
   * Center a component with a padding used to add space on both sides of the component. Uses empty spaces for padding
   * and the static chat width({@value DEFAULT_CHAT_WIDTH}) for calculation.
   *
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @param context the context of the pixel width calculation
   * @return a component with padding to hopefully center it
   * @throws IllegalArgumentException if padding is too wide to fit on both sides of the center component at least once
   * @since 1.0.0
   */
  static <CX> Component center(final @NotNull Component component, final @NotNull ContextualPixelWidthSource<CX> source, final @NotNull CX context) {
    return center(component, source, context, Component.space(), DEFAULT_CHAT_WIDTH);
  }

  /**
   * Center a component with a padding used to add space on both sides of the component.
   *
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @param padding the text to use as padding
   * @param chatWidth the width of the chat this component is getting shown in
   * @return a component with padding to hopefully center it
   * @throws IllegalArgumentException if padding is too wide to fit on both sides of the center component at least once
   * @since 1.0.0
   */
  static Component center(final @NotNull Component component, final @NotNull PixelWidthSource source, final @NotNull TextComponent padding, final float chatWidth) {
    final float componentWidth = source.width(component);
    final float paddingWidth = source.width(padding);
    return center(component, componentWidth, padding, paddingWidth, chatWidth);
  }

  /**
   * Center a component with a padding used to add space on both sides of the component. Uses the static chat width({@value DEFAULT_CHAT_WIDTH})
   * for calculation.
   *
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @param padding the text to use as padding
   * @return a component with padding to hopefully center it
   * @throws IllegalArgumentException if padding is too wide to fit on both sides of the center component at least once
   * @since 1.0.0
   */
  static Component center(final @NotNull Component component, @NotNull PixelWidthSource source, @NotNull TextComponent padding) {
    return center(component, source, padding, DEFAULT_CHAT_WIDTH);
  }

  /**
   * Center a component with a padding used to add space on both sides of the component. Uses empty spaces for padding and
   * the static chat width({@value DEFAULT_CHAT_WIDTH}) for calculation. Uses the static pixel width source.
   *
   * @param component the component to center
   * @param padding the text to use as padding
   * @return a component with padding to hopefully center it
   * @throws IllegalArgumentException if padding is too wide to fit on both sides of the center component at least once
   * @since 1.0.0
   */
  static Component center(final @NotNull Component component, final @NotNull TextComponent padding) {
    return center(component, PixelWidthSource.pixelWidth(), padding);
  }

  /**
   * Center a component with a padding used to add space on both sides of the component. Uses empty spaces for padding
   * and the static chat width({@value DEFAULT_CHAT_WIDTH}) for calculation.
   *
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @return a component with padding to hopefully center it
   * @throws IllegalArgumentException if padding is too wide to fit on both sides of the center component at least once
   * @since 1.0.0
   */
  static Component center(final @NotNull Component component, PixelWidthSource source) {
    return center(component, source, Component.space());
  }

  /**
   * Center a component with a padding used to add space on both sides of the component. Uses empty spaces for padding and
   * the static chat width({@value DEFAULT_CHAT_WIDTH}) for calculation. Uses the static pixel width source.
   *
   * @param component the component to center
   * @return a component with padding to hopefully center it
   * @throws IllegalArgumentException if padding is too wide to fit on both sides of the center component at least once
   * @since 1.0.0
   */
  static Component center(final @NotNull Component component) {
    return center(component, PixelWidthSource.pixelWidth());
  }
}

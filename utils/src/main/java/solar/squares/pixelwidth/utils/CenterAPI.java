package solar.squares.pixelwidth.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import solar.squares.pixelwidth.ContextualPixelWidthSource;
import solar.squares.pixelwidth.PixelWidthSource;

import java.util.function.Function;

/**
 * API for centering text in a component with various possibilities for complexity
 * @since 1.0.0
 */
public interface CenterAPI {

  int DEFAULT_CHAT_WIDTH = 320;

  /**
   * Center a component with a padding used to add space on both sides of the component.
   * @param component The component to center
   * @param componentWidth The width of the component
   * @param padding the text to use as padding
   * @param paddingCharWidth the width of the padding text
   * @param chatWidth the width of the chat this component is getting shown in
   * @return a component with padding to hopefully center it
   * @since 1.0.0
   */
  default Component center(final @NotNull Component component, final float componentWidth, final @NotNull TextComponent padding, final float paddingCharWidth, final float chatWidth) {
    if(padding.content().chars().count() > 1) throw new IllegalArgumentException("Padding can only be a single character, was " + padding.content());
    final float widthToPad = chatWidth - componentWidth / 2;
    final StringBuilder paddingBuilder = new StringBuilder();
    final char c = padding.content().charAt(0);
    for (float i = paddingCharWidth; i < widthToPad; i+=paddingCharWidth) {
      paddingBuilder.append(c);
    }
    final Component fullPadding = Component.text(paddingBuilder.toString());
    return fullPadding.append(component).append(fullPadding);
  }

  /**
   * Center a component with a padding used to add space on both sides of the component.
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @param context the context of the pixel width calculation
   * @param padding the text to use as padding
   * @param contextToChatWidthFunction function to find a chat width given the context
   * @return a component with padding to hopefully center it
   * @since 1.0.0
   */
  default <CX> Component center(final @NotNull Component component, final @NotNull ContextualPixelWidthSource<CX> source, final @NotNull CX context, final @NotNull TextComponent padding, Function<CX, Float> contextToChatWidthFunction) {
    final float componentWidth = source.width(component, context);
    final float paddingWidth = source.width(padding, context);
    return this.center(component, componentWidth, padding, paddingWidth, contextToChatWidthFunction.apply(context));
  }

  /**
   * Center a component with a padding used to add space on both sides of the component.
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @param context the context of the pixel width calculation
   * @param padding the text to use as padding
   * @param chatWidth the width of the chat this component is getting shown in
   * @return a component with padding to hopefully center it
   * @since 1.0.0
   */
  default <CX> Component center(final @NotNull Component component, final @NotNull ContextualPixelWidthSource<CX> source, final @NotNull CX context, final @NotNull TextComponent padding, final float chatWidth) {
    return center(component, source, context, padding, cx -> chatWidth);
  }


  /**
   * Center a component with a padding used to add space on both sides of the component. Uses empty spaces for padding
   * and the default chat width({@value DEFAULT_CHAT_WIDTH}) for calculation.
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @param context the context of the pixel width calculation
   * @return a component with padding to hopefully center it
   * @since 1.0.0
   */
  default <CX> Component center(final @NotNull Component component, final @NotNull ContextualPixelWidthSource<CX> source, final @NotNull CX context) {
    return center(component, source, context, Component.space(), DEFAULT_CHAT_WIDTH);
  }

  /**
   * Center a component with a padding used to add space on both sides of the component.
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @param padding the text to use as padding
   * @param chatWidth the width of the chat this component is getting shown in
   * @return a component with padding to hopefully center it
   * @since 1.0.0
   */
  default Component center(final @NotNull Component component, final @NotNull PixelWidthSource source, final @NotNull TextComponent padding, final float chatWidth) {
    final float componentWidth = source.width(component);
    final float paddingWidth = source.width(padding);
    return this.center(component, componentWidth, padding, paddingWidth, chatWidth);
  }

  /**
   * Center a component with a padding used to add space on both sides of the component. Uses the default chat width({@value DEFAULT_CHAT_WIDTH})
   * for calculation.
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @param padding the text to use as padding
   * @return a component with padding to hopefully center it
   * @since 1.0.0
   */
  default Component center(final @NotNull Component component, @NotNull PixelWidthSource source, @NotNull TextComponent padding) {
    return center(component, source, padding, DEFAULT_CHAT_WIDTH);
  }

  /**
   * Center a component with a padding used to add space on both sides of the component. Uses empty spaces for padding
   * and the default chat width({@value DEFAULT_CHAT_WIDTH}) for calculation.
   * @param component the component to center
   * @param source the pixel width source used to calculate width of the component and the padding
   * @return a component with padding to hopefully center it
   * @since 1.0.0
   */
  default Component center(final @NotNull Component component, PixelWidthSource source) {
    return center(component, source, Component.space());
  }

  /**
   * Center a component with a padding used to add space on both sides of the component. Uses empty spaces for padding and
   * the default chat width({@value DEFAULT_CHAT_WIDTH}) for calculation. Uses the default pixel width source.
   * @param component the component to center
   * @return a component with padding to hopefully center it
   * @since 1.0.0
   */
  default Component center(final @NotNull Component component) {
    return center(component, PixelWidthSource.pixelWidth());
  }
}

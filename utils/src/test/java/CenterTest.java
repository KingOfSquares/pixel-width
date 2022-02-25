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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import solar.squares.pixelwidth.PixelWidthSource;
import solar.squares.pixelwidth.utils.CenterAPI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CenterTest {

  @SuppressWarnings("SameParameterValue")
  private void testCommonCenterProperties(final String original, final Component result, final float goalWidth, final Style style, final PixelWidthSource source) {
    final String serializedResult = PlainTextComponentSerializer.plainText().serialize(result);
    final float resultWidth = source.width(result);
    if (resultWidth > goalWidth) throw new AssertionFailedError("The final component can not be wider than the goal width");
    final String[] paddings = serializedResult.split(original);
    assertEquals(paddings[0], paddings[1]);
    final float l1 = source.width(paddings[0], style);
    final float l2 = source.width(paddings[1], style);
    assertEquals(l1, l2);
    final float l3 = source.width(original, style);
    result.compact();
    assertEquals(l1 + l2 + l3, resultWidth);
    System.out.println(serializedResult);
  }

  @Test
  public void testDefaultCenter() {
    final String s = "WOW";
    final Component component = CenterAPI.center(Component.text(s), Component.text("-"));
    this.testCommonCenterProperties(s, component, CenterAPI.DEFAULT_CHAT_WIDTH, Style.empty(), PixelWidthSource.pixelWidth());
  }

  @Test
  public void testCenterWithCustomStyle() {
    final String s = "WOW";
    final Style style = Style.style(TextDecoration.BOLD);
    final Component component = CenterAPI.center(Component.text(s, style), Component.text("-", style));
    this.testCommonCenterProperties(s, component, CenterAPI.DEFAULT_CHAT_WIDTH, style, PixelWidthSource.pixelWidth());
  }

  @Test
  public void testCenterWithLargerPadding() {
    final String s = "WOW";
    final Component component = CenterAPI.center(Component.text(s), Component.text("padding"));
    this.testCommonCenterProperties(s, component, CenterAPI.DEFAULT_CHAT_WIDTH, Style.empty(), PixelWidthSource.pixelWidth());
  }

  @Test
  public void testCenterComponentStructure() {
    //Test for bleeding styles/malformed components
    final String s = "WOW";
    final Style bold = Style.style(TextDecoration.BOLD);
    final Style italic = Style.style(TextDecoration.ITALIC);
    final Component component = CenterAPI.center(Component.text(s, italic), Component.text("-", bold));
    int foundPadding = 0;
    int foundContent = 0;
    for (final Component child : component.children()) {
      if (child instanceof TextComponent && ((TextComponent) child).content().equals(s)) {
        //Simulate style merge
        final Style merged = child.style().merge(component.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
        assertEquals(merged.decoration(TextDecoration.BOLD), TextDecoration.State.NOT_SET);
        foundContent++;
      }
      if (child instanceof TextComponent && ((TextComponent) child).content().startsWith("-")) {
        final Style merged = child.style().merge(component.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
        assertEquals(merged.decoration(TextDecoration.ITALIC), TextDecoration.State.NOT_SET);
        foundPadding++;
      }
    }
    if (foundContent != 1 || foundPadding != 2) throw new AssertionError("Wrong number of paddings or content found");
  }
}

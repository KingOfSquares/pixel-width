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

import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import solar.squares.pixelwidth.context.CustomFontCharacterWidthFunction;

import static net.kyori.adventure.text.Component.keybind;
import static net.kyori.adventure.text.Component.text;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static solar.squares.pixelwidth.PixelWidthSource.pixelWidth;

public class PixelWidthSourceTest {

  @Test
  public void testCatWidth() {
    final PixelWidthSource source = PixelWidthSource.pixelWidth();
    assertEquals(18, source.width(Component.text("CAT")));
  }

  @Test
  public void testBoldCatWidth() {
    final PixelWidthSource source = PixelWidthSource.pixelWidth();
    assertEquals(21, source.width(Component.text("CAT", Style.style(TextDecoration.BOLD))));
  }

  @Test
  public void testWidthCustomResolver() {
    final @NotNull Map<String, String> keybinds = new HashMap<>();
    keybinds.put("key.jump", "spacebar");
    keybinds.put("key.forward", "w");
    final ComponentFlattener keybindFlattener = ComponentFlattener.builder().mapper(KeybindComponent.class, k -> keybinds.get(k.keybind())).build();
    final PixelWidthSource custom = PixelWidthSource.pixelWidth(keybindFlattener);
    assertEquals(48, custom.width(keybind("key.jump")));
  }

  @Test
  public void testWidthUsingCustomCharacterFunction() {
    final PixelWidthSource custom = pixelWidth(new CustomFontCharacterWidthFunction());
    assertEquals(17, custom.width(text("aA1 ").append(text("2", NamedTextColor.RED, TextDecoration.OBFUSCATED))));
  }

  @Test
  public void testWidthNonBMPCharacter() {
    final PixelWidthSource custom = pixelWidth(new CustomFontCharacterWidthFunction());
    assertEquals(8, custom.width(text("\uD800\uDD92"))); // 𐆒
  }

  @Test
  public void testSpaces() {
    final PixelWidthSource source = PixelWidthSource.pixelWidth();
    assertEquals(8, source.width(text("        ")));
  }
}

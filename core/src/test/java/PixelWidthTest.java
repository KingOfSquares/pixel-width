import king.squares.pixelwidth.PixelWidthSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PixelWidthTest {

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
}

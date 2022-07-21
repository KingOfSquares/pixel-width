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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.ApiStatus;

/**
 * Hack(?) to allow logging throughout the tools in different modules to use a common {@link Logger}.
 * The used logger can be customized by library users.
 *
 * @since 1.1.0
 */
public final class Logging {

  private static Logger logger = Logger.getLogger("pixel-width");

  private Logging() {
  }

  /**
   * Logging, should only be used internally.
   *
   * @since 1.1.0
   */
  @ApiStatus.Internal
  public static void log(final Level level, final String message) {
    logger.log(level, message);
  }

  /**
   * Can be used to set the logger that will be used by this library.
   *
   * @since 1.1.0
   */
  public static void logger(final Logger logger0) {
    logger = logger0;
  }

}

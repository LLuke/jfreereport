package org.jfree.report.modules.output.pageable.plaintext;

import org.jfree.report.style.FontDefinition;

/**
 * A data carrier to collect and store text data for the output.
 */
public class PlaintextDataChunk
{
  /** The text that should be printed. */
  private final String text;

  /** The font definition stores the font style. */
  private final FontDefinition font;

  /** the column where the text starts. */
  private final int x;

  /** the row of the text. */
  private final int y;

  /** the text width. */
  private final int width;

  private final String encoding;

  /**
   * Creates a new text data chunk.
   *
   * @param text the text that should be printed
   * @param font the font style for the text
   * @param x the column where the text starts
   * @param y the row of the text
   * @param w the number of characters of the text that should be printed.
   */
  protected PlaintextDataChunk(final String text,
                               final FontDefinition font,
                               final String encoding,
                               final int x, final int y, final int w)
  {
    if (font == null)
    {
      throw new NullPointerException("Font must not be null");
    }
    if (text == null)
    {
      throw new NullPointerException("Text must not be null");
    }
    if (x < 0)
    {
      throw new IllegalArgumentException();
    }

    if (y < 0)
    {
      throw new IllegalArgumentException();
    }

    if (w < 1)
    {
      throw new IllegalArgumentException();
    }
    if (w > text.length())
    {
      throw new IllegalArgumentException("Size limit: " + w + " vs. " + text.length());
    }

    this.x = x;
    this.y = y;
    this.width = w;
    this.encoding = font.getFontEncoding(encoding);
    this.text = text;
    this.font = font;
  }

  /**
   * Gets the text stored in this chunk.
   *
   * @return the text
   */
  public String getText()
  {
    return text;
  }

  /**
   * Gets the font definition used to define the text style.
   *
   * @return the font definition.
   */
  public FontDefinition getFont()
  {
    return font;
  }

  /**
   * The column of the text start.
   *
   * @return the column of the first character.
   */
  public int getX()
  {
    return x;
  }

  /**
   * Gets the row where to print the text.
   *
   * @return the row.
   */
  public int getY()
  {
    return y;
  }

  /**
   * Gets the width of the text, the number of character which should be printed.
   *
   * @return the number of printable characters.
   */
  public int getWidth()
  {
    return width;
  }

  public String getEncoding ()
  {
    return encoding;
  }
}

/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ------------------
 * TextParagraph.java
 * ------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TextParagraph.java,v 1.9 2003/10/30 22:14:59 taqua Exp $
 *
 * Changes
 * -------
 *
 * 21-Jan-2003: BugFix: Breakline was inaccurate
 * 07-Feb-2003: Documentation
 */
package org.jfree.report.content;

import java.awt.geom.Rectangle2D;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.util.WordBreakIterator;

/**
 * A paragraph of an given text content. A paragraph consists of one or more
 * {@link TextLine}s.
 * <p>
 * Todo: Can the content size be cached?
 *
 * @author Thomas Morgner.
 */
public strictfp class TextParagraph extends ContentContainer
{
  /** The reserved size. */
  private float reservedSize;

  /** The reserved size. */
  private float lineHeight;

  /** The reserved size. */
  private boolean trimTextContent;

  /** The size calculator. */
  private final SizeCalculator sizeCalculator;

  /** The reserved literal that should be appended to the text. */
  private final String reservedLiteral;

  /**
   * Creates a new text paragraph using the specified size calculator.
   *
   * @param calc  the size calculator.
   * @param lineHeight the height of the lines contained in this paragraph.
   * @param reservedLiteral the text that should be appended if the text does 
   * not fit into the bounds
   * @param trimTextContent defines, whether to remove whitespaces from the start
   * and end of an line.
   */
  public TextParagraph(final SizeCalculator calc, final float lineHeight, 
                       final String reservedLiteral, final boolean trimTextContent)
  {
    super(new Rectangle2D.Float());
    this.sizeCalculator = calc;
    this.reservedLiteral = reservedLiteral;
    this.reservedSize = getSizeCalculator().getStringWidth
        (reservedLiteral, 0, reservedLiteral.length());
    this.lineHeight = lineHeight;
    this.trimTextContent = trimTextContent;
  }

  /**
   * Returns the size calculator.
   *
   * @return the size calculator.
   */
  private SizeCalculator getSizeCalculator()
  {
    return sizeCalculator;
  }

  /**
   * Sets the content.
   *
   * @param content  the content.
   * @param x the x coordinates for the bounds.
   * @param y the y coordinates for the bounds.
   * @param width the width of the bounds.
   * @param height the height of the bounds.
   */
  public void setContent
      (final String content, final float x, final float y, final float width, final float height)
  {
    if (content == null)
    {
      throw new NullPointerException("MaxBounds must not be null");
    }
    if (x < 0)
    {
      throw new IllegalArgumentException();
    }
    if (y < 0)
    {
      throw new IllegalArgumentException();
    }
    if (width < 0)
    {
      throw new IllegalArgumentException();
    }
    if (height < 0)
    {
      throw new IllegalArgumentException();
    }

    float usedHeight = 0;

    final int maxLines = (int) Math.floor(height / getSizeCalculator().getLineHeight());

    if (maxLines > 0)
    {
      final List l = breakLines(content, width, maxLines);
      /*
      if (l.size() > maxLines)
      {
        throw new IllegalStateException("MaxLines not working anymore");
      }
      */
      for (int i = 0; i < l.size(); i++)
      {
        // create Lines
        final String lineText = (String) l.get(i);
        final TextLine line = new TextLine(getSizeCalculator(), lineHeight);

/*
        // strict assertation ... is expensive and not enabled by default ...
        float tt = getSizeCalculator().getStringWidth(lineText, 0, lineText.length());
        if (tt >= width)
        {
          throw new IllegalStateException("LineBreak failed!:" + width + " -> " + tt);
        }
*/
        line.setContent(lineText, x, y + usedHeight, width, height - usedHeight);

        usedHeight += line.getHeight();
        if (line.getBounds().getHeight() > 0)
        {
          addContentPart(line);
        }
      }
    }
    setBounds(x, y, width, usedHeight);
  }

  /**
   * Breaks the text into multiple lines. The given string is broken either when a manual
   * linebreak is encountered or when the current line's rendered width exceeds the given limit.
   * If the text contains more than <code>maxLines</code> lines, all lines over the limit
   * are ignored.
   * <p>
   * If more than one line is expected or the strictmode is enabled and there are more lines
   * in the given text than space is available, the text is cut down to the given number of
   * lines and the RESERVED_LITERAL is appended to signal the cut down to the user.
   * <p>
   * The text is broken on word boundaries.
   *
   * @param mytext  the text to be broken down
   * @param width  the boundary on which the text is broken if no manual linebreak is encountered.
   * @param maxLines  the number of lines to be displayed. Must be at least 1
   *
   * @return a list of lines.
   */
  private List breakLines(final String mytext, final float width, final int maxLines)
  {
    if (width <= 0)
    {
      throw new IllegalArgumentException("Width must greater than 0, was " + width);
    }

    // a 0 maxLines is no longer allowed - to test the max size, ask the layoutmanager to
    // create a Max-Bounds and test that bound...
    if (maxLines < 1)
    {
      throw new IllegalArgumentException("MaxLines of <1 is not allowed");
    }

    // Reserve some space for the last line if there is more than one line to display.
    // If there is only one line, don't cut the line yet. Perhaps we intruduce the strict
    // mode later, but without any visual editing it would be cruel to any report designer.
    final WordBreakIterator breakit = new WordBreakIterator(mytext);
    final ArrayList returnLines = new ArrayList(5);

    int lineStartPos = 0;
    final int lineLength = mytext.length();
    if (lineLength == 0)
    {
      returnLines.add("");
      return returnLines;
    }

    while (lineStartPos < lineLength)
    {
      // the whole text section contains white spaces ... must be skipped ..
      // we can assume, that if the first character is a white space, then all
      // characters are whitespaces, as the word break iterator searches for
      // whitespace boundries ...
      if (isTrimTextContent())
      {
        while ((lineStartPos < lineLength) && (isWhitespace(mytext.charAt(lineStartPos))))
        {
          lineStartPos++;
        }
      }
      final boolean forceEnd = ((returnLines.size() + 1) == maxLines);
      final int nextPos = findNextBreak(mytext, lineStartPos, width, forceEnd, breakit);

      // the complete text is finished, noting more to do here.
      if (nextPos == BreakIterator.DONE)
      {
        final String addString = mytext.substring(lineStartPos);
        if (isTrimTextContent())
        {
          returnLines.add (addString.trim());
        }
        else
        {
          returnLines.add(addString);
        }
        return returnLines;
      }

      if (forceEnd)
      {
        // it won't fit in, so break ...
        String addString = appendReserveLit(mytext, lineStartPos, nextPos, width);
        if (isTrimTextContent())
        {
          returnLines.add(addString.trim());
        }
        else
        {
          returnLines.add (addString);
        }
        return returnLines;
      }

      // End the line and restart for the next line ...
      final String addString = mytext.substring(lineStartPos, nextPos);
      if (isTrimTextContent())
      {
        returnLines.add (addString.trim());
      }
      else
      {
        returnLines.add(addString);
      }
      lineStartPos = nextPos;
    }
    return returnLines;
  }

  /**
   * Defines, whether whitespaces were removed from the lines of this paragraph
   * after performing the linebreaking.
   *  
   * @return true, if whitespaces could have been removed, false otherwise.
   */
  public boolean isTrimTextContent()
  {
    return trimTextContent;
  }
  
  /**
   * Tests, whether the given character is a whitespace (but not a breakline).
   *
   * @param c the character that should be tested.
   * @return true, if this is a whitespace character, but not a
   * linebreak character, false otherwise.
   */
  private boolean isWhitespace(final char c)
  {
    if (c == '\n' || c == '\r')
    {
      return false;
    }
    else
    {
      return Character.isWhitespace(c);
    }
  }

  /**
   * Locates the next linebreak for the given text.
   *
   * @param text the text that should be broken into lines
   * @param lineStart the position where the current line beginns
   * @param width the maximum width for a single line
   * @param forceEnd whether to enforce the end of the paragraph after that line
   * @param breakit the break iterator for the text
   * @return the most suitable position for an linebreak.
   */
  private int findNextBreak(final String text,
                            final int lineStart,
                            final float width,
                            final boolean forceEnd,
                            final WordBreakIterator breakit)
  {
    int startPos = lineStart;
    int endPos;
    float x = 0;
    // by default, we assume, that the line contains no break-positions,
    // like whitespaces etc.
    boolean enableCharBreak = true;

    // Break on WORD boundries ...
    // ---------------------------
    // check the complete line, break when the complete text is done or
    // the end of the line has been reached.
    while (((endPos = breakit.next()) != BreakIterator.DONE))
    {
      x += getSizeCalculator().getStringWidth(text, startPos, endPos);
      if (forceEnd)
      {
        // here is the last line, check whether the reserved literal will fit
        // in to the line, if not, then we have reached the end of the text field.
        if (x >= (width - reservedSize))
        {
          // return the last known linebreak pos...
          return startPos;
        }
      }
      else
      {
        if (x > width)
        {
          //when the first word of the line is too big then cut the word ...
          if (enableCharBreak)
          {
            while (getSizeCalculator().getStringWidth(text, startPos, endPos) > width)
            {
              endPos--;
            }
            breakit.setPosition(endPos);
            return endPos;
          }
          else
          {
            endPos = breakit.previous();
            return endPos;
          }
        }
      }

      // we found at least one break-boundry ...
      enableCharBreak = false;

      // the word is complete ... check whether the next word will fit on the line.
      startPos = endPos;
    }
    return BreakIterator.DONE;
  }


  /**
   * Add the reserve literal to the end of the string, but make sure that as much as
   * possible is printed in the line before the literal is finally added. So the last
   * word is not totally lost, maybe some characters can be printed ...
   *
   * @param base  the base string.
   * @param lineStart  the position of the first character in the line.
   * @param lastCheckedChar  the last character of the line, which is known to fit into the given
   *                         width.
   * @param width  the maximum width.
   *
   * @return a string with '..' appended.
   */
  private String appendReserveLit(final String base, final int lineStart,
                                  final int lastCheckedChar, final float width)
  {
    if (lastCheckedChar < 0)
    {
      throw new IllegalArgumentException("Start must not be negative");
    }
    if (width < 0)
    {
      throw new IllegalArgumentException("Width must not be negative");
    }
    if (lineStart < 0)
    {
      throw new IllegalArgumentException("LineStart must not be negative");
    }

    // check whether the complete line would fit into the given width
    final float toTheEnd = getSizeCalculator().getStringWidth(base, lineStart, base.length());
    if (toTheEnd <= width)
    {
      return base.substring(lineStart);
    }

    final String baseLine = base.substring(lineStart, lastCheckedChar);
    final float filler = width -
        (getSizeCalculator().getStringWidth(baseLine, 0, baseLine.length())) - reservedSize;

    final int maxFillerLength = base.length() - lastCheckedChar;
    for (int i = 1; i < maxFillerLength; i++)
    {
      final float fillerWidth = getSizeCalculator().getStringWidth(base, lastCheckedChar,
          lastCheckedChar + i);
      if (filler < fillerWidth)
      {
        return base.substring(lineStart, lastCheckedChar + i - 1) + reservedLiteral;
      }
    }
    return baseLine + reservedLiteral;
  }

  /**
   * Removes all whitespaces from the given String and replaces them with a space character.
   *
   * @param text  the string to process.
   *
   * @return  a string with all whitespace replaced by space characters.
   */
  protected static String clearWhitespaces(final String text)
  {
    final char[] textdata = text.toCharArray();
    for (int i = 0; i < textdata.length; i++)
    {
      if (Character.isWhitespace(textdata[i]))
      {
        textdata[i] = ' ';
      }
    }
    return new String(textdata);
  }

}

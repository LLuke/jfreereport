/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: TextParagraph.java,v 1.13 2003/04/06 18:11:30 taqua Exp $
 *
 * Changes
 * -------
 *
 * 21-Jan-2003: BugFix: Breakline was inaccurate
 * 07-Feb-2003: Documentation
 */
package com.jrefinery.report.targets.base.content;

import java.awt.geom.Rectangle2D;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import com.jrefinery.report.targets.base.layout.SizeCalculator;
import com.jrefinery.report.util.WordBreakIterator;

/**
 * A paragraph of an given text content. A paragraph consists of one or more
 * {@link TextLine}s.
 *
 * @author Thomas Morgner.
 */
public class TextParagraph extends ContentContainer
{
  /** A literal used for lines that get shortened by the linebreak implementation. */
  public static final String RESERVED_LITERAL = "..";
  
  /** The reserved size. */
  private float reservedSize = 0;

  /** The reserved size. */
  private float lineHeight = 0;

  /** The size calculator. */
  private SizeCalculator sizeCalculator;

  /**
   * Creates a new text paragraph using the specified size calculator.
   *
   * @param calc  the size calculator.
   * @param lineHeight the height of the lines contained in this paragraph.
   */
  public TextParagraph(SizeCalculator calc, float lineHeight)
  {
    super(new Rectangle2D.Float());
    this.sizeCalculator = calc;
    this.reservedSize = getSizeCalculator().getStringWidth(RESERVED_LITERAL, 0, 
                                                           RESERVED_LITERAL.length());
    this.lineHeight = lineHeight;
  }

  /**
   * Returns the size calculator.
   *
   * @return the size calculator.
   */
  public SizeCalculator getSizeCalculator()
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
  public void setContent (String content, float x, float y, float width, float height)
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

    int maxLines = (int) Math.floor(height / getSizeCalculator().getLineHeight());

    if (maxLines > 0)
    {
      List l = breakLines(content, width, maxLines);
      for (int i = 0; i < l.size(); i++)
      {
        // create Lines
        String lineText = (String) l.get(i);
        TextLine line = new TextLine(getSizeCalculator(), lineHeight);
        line.setContent(lineText, x, y, width, height);

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
  private List breakLines(String mytext, final float width, int maxLines)
  {
    if (width <= 0)
    {
      throw new IllegalArgumentException("Width must not be less or equal 0, was " + width);
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
    WordBreakIterator breakit = new WordBreakIterator(mytext);
    ArrayList returnLines = new ArrayList(5);

    int lineStartPos = 0;
    int lineLength = mytext.length();
    if (lineLength == 0)
    {
      return returnLines;
    }

    while (lineStartPos < lineLength)
    {
      int startPos = lineStartPos;

      int endPos;
      float x = 0;
      float w = width;

      // add by leonlyong
      int wordCnt = 0;

      // check the complete line, break when the complete text is done or
      // the end of the line has been reached.
      while (((endPos = breakit.next()) != BreakIterator.DONE))
      {

        // the whole text section contains white spaces ... must be skipped ..
        // we can assume, that if the first character is a white space, then all
        // characters are whitespaces, as the word break iterator searches for
        // whitespace boundries ...
        if (Character.isWhitespace(mytext.charAt(startPos)))
        {
          startPos = endPos;
          continue;
        }

        // add by leonlyong
        wordCnt++;

        x += getSizeCalculator().getStringWidth(mytext, startPos, endPos);
        if (returnLines.size() == (maxLines - 1))
        {
          // here is the last line, check whether the reserved literal will fit in here.
          if (x >= (w - reservedSize))
          {
            break;
          }
        }
        else
        {
          if (x > w)
          {
            // add by leonlyong
            //when the first word of the line is too big then cut the word ...
            if (wordCnt == 1)
            {
              while (getSizeCalculator().getStringWidth(mytext, startPos, endPos) > w)
              {
                endPos--;
              }
              startPos = endPos;
              endPos = breakit.previous();
            }

            break;
          }
        }
        startPos = endPos;
      }

      // the complete text is finished, noting more to do here.
      if (endPos == BreakIterator.DONE)
      {
        String addString = mytext.substring(lineStartPos);
        returnLines.add(addString);
        break;
      }

      // if this is the last allowed row, add the RESERVED_LITERAL to the string ..
      if (returnLines.size() == (maxLines - 1))
      {
        // altered:
        returnLines.add(appendReserveLit(mytext, lineStartPos, startPos, width));
        return returnLines;
      }
      else
      {

        // End the line and restart for the next line ...
        String addString = mytext.substring(lineStartPos, startPos);
        returnLines.add(addString);
        lineStartPos = startPos;
      }
    }
    return returnLines;
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
  private String appendReserveLit(String base, int lineStart, int lastCheckedChar, float width)
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
    float toTheEnd = getSizeCalculator().getStringWidth(base, lineStart, base.length());
    if (toTheEnd <= width)
    {
      return base.substring(lineStart);
    }

    String baseLine = base.substring(lineStart, lastCheckedChar);
    float filler = width - (getSizeCalculator().getStringWidth(baseLine, 0, baseLine.length())) 
                         - reservedSize;

    int maxFillerLength = base.length() - lastCheckedChar;
    for (int i = 0; i < maxFillerLength; i++)
    {
      float fillerWidth = getSizeCalculator().getStringWidth(base, lastCheckedChar, 
                                                             lastCheckedChar + i);
      if (filler < fillerWidth)
      {
        return base.substring(lineStart, lastCheckedChar + i) + RESERVED_LITERAL;
      }
    }
    return baseLine + RESERVED_LITERAL;
  }

  /**
   * Removes all whitespaces from the given String and replaces them with a space character.
   *
   * @param text  the string to process.
   *
   * @return  a string with all whitespace replaced by space characters.
   */
  protected String clearWhitespaces(String text)
  {
    char[] textdata = text.toCharArray();
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

/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * WordBreakIterator.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LineBreakIterator.java,v 1.1 2003/03/13 17:43:11 taqua Exp $
 *
 * Changes
 * -------
 * 13-03-2003 : Initial version
 */
package com.jrefinery.report.util;

/**
 * Same as BreakIterator.getLineInstance().
 */
public class LineBreakIterator
{
  public static final int DONE = -1;

  private char[] text;
  private int position;
  private int lastFound;

  // ignore the next lf that may be encountered ...
  private boolean skipLF;

  public LineBreakIterator()
  {
    setText("");
  }

  public LineBreakIterator(String text)
  {
    setText(text);
  }

  public synchronized int next()
  {
    if (text == null) return DONE;
    if (position == DONE) return DONE;

    // recognize \n, \n\r, \r\n
    boolean omitLF = skipLF;

    int nChars = text.length;
    int nextChar = position;
    lastFound = position;

    for (; ;)
    {
      if (nextChar >= nChars)
      {
        /* End of text reached */
        position = DONE;
        return DONE;
      }

      boolean eol = false;
      char c = 0;
      int i;

      /* Skip a leftover '\n', if necessary */
      if (omitLF && (text[nextChar] == '\n'))
        nextChar++;
      skipLF = false;
      omitLF = false;

      // search the next line break, either \n or \r
      for (i = nextChar; i < nChars; i++)
      {
        c = text[i];
        if ((c == '\n') || (c == '\r'))
        {
          eol = true;
          break;
        }
      }

      nextChar = i;
      if (eol)
      {
        nextChar++;
        if (c == '\r')
        {
          skipLF = true;
        }
        position = nextChar;
        return position;
      }
    }
  }

  /**
   * Same like next(), but returns the End-Of-Text as
   * if there was a linebreak added (Reader.readLine() compatible)
   * @return
   */
  public int nextWithEnd ()
  {
    int pos = position;
    if (pos == DONE || pos == text.length)
    {
      return DONE;
    }
    int retval = next();
    if (retval == DONE)
    {
      return text.length;
    }
    return retval;
  }

  public String getText()
  {
    return new String(text);
  }

  public void setText(String text)
  {
    position = 0;
    skipLF = false;
    this.text = text.toCharArray();
  }

  public int previous()
  {
    return lastFound;
  }

  public static void main (String[] args)
  {
    String test = "The lazy \n fox \r\n jumps \nover the funny tree\n";
    LineBreakIterator lbi = new LineBreakIterator(test);
    int pos = lbi.next();
    int oldPos = 0;
    for (int i = 0; (i < pos) && (pos != DONE); i++)
    {
      System.out.println ("Text: " + test.substring(oldPos, pos));
      oldPos = pos;
      pos = lbi.next();
    }

  }
}

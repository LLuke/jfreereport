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
 * $Id $
 *
 * Changes
 * -------
 * 18-03-2003 : Initial version
 */
package com.jrefinery.report.util;

import java.text.BreakIterator;

/**
 * Behaves similiar to BreakIterator.getWordInstance() but handles line break
 * delimeters as simple whitespaces.
 */
public class WordBreakIterator
{
  public static final int DONE = -1;
  private int position;
  private int lastFound;
  private char[] text;

  public WordBreakIterator(String text)
  {
    setText(text);
  }

  public synchronized int next()
  {
    if (position == DONE) return DONE;
    if (text == null) return DONE;
    if (position == text.length) return DONE;

    lastFound = position;

    if (Character.isWhitespace(text[position]))
    {
      // search the first non whitespace character ..., this is the beginning of the word
      while ((position < text.length) && (Character.isWhitespace(text[position])))
      {
        position++;
      }
      return position;
    }
    else
    {
      // now search the first whitespace character ..., this is the end of the word
      while ((position < text.length) && (Character.isWhitespace(text[position]) == false))
      {
        position++;
      }
      return position;
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

  public int previous()
  {
    return lastFound;
  }

  public String getText()
  {
    return new String(text);
  }

  public void setText(String text)
  {
    position = 0;
    this.text = text.toCharArray();
  }

  public static void main (String[] args)
  {
    String test = "The lazy \n fox \r\n jumps \nover the funny tree\n";

    {
      WordBreakIterator lbi = new WordBreakIterator(test);
      int pos = lbi.next();
      int oldPos = 0;
      for (int i = 0; (i < pos) && (pos != DONE); i++)
      {
        System.out.println ("Text: " + test.substring(oldPos, pos));
        oldPos = pos;
        pos = lbi.next();
      }
    }

    {
      BreakIterator bi = BreakIterator.getWordInstance();
      bi.setText(test);

      int pos = bi.next();
      int oldPos = 0;
      for (int i = 0; (i < pos) && (pos != DONE); i++)
      {
        System.out.println ("Text: " + test.substring(oldPos, pos));
        oldPos = pos;
        pos = bi.next();
      }
    }

  }

}

/**
 * Date: Mar 13, 2003
 * Time: 5:13:28 PM
 *
 * $Id$
 */
package com.jrefinery.report.util;

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
    String test = "The lazy \n fox \r\n jumps \n\rover the funny tree\n";
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

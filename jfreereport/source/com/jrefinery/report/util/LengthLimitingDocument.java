package com.jrefinery.report.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class LengthLimitingDocument extends PlainDocument
{
  private int maxlen;

  public LengthLimitingDocument ()
  {
    this (-1);
  }

  public LengthLimitingDocument (int maxlen)
  {
    super ();
    this.maxlen = maxlen;
  }

  public void setMaxLength (int maxlen)
  {
    this.maxlen = maxlen;
  }

  public int getMaxLength ()
  {
    return maxlen;
  }

  public void insertString (int offs, String str, AttributeSet a)
          throws BadLocationException
  {
    if (str == null)
    {
      return;
    }

    char[] numeric = str.toCharArray ();
    StringBuffer b = new StringBuffer ();
    for (int i = 0; i < numeric.length; i++)
    {
      if ((maxlen == -1) || (getLength () + b.length ()) < maxlen)
      {
        b.append (numeric[i]);
      }
    }
    super.insertString (offs, b.toString (), a);
  }
}

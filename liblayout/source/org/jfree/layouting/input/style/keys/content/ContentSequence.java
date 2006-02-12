package org.jfree.layouting.input.style.keys.content;

import org.jfree.layouting.input.style.values.CSSValue;

public class ContentSequence implements CSSValue
{
  private CSSValue[] contents;

  public ContentSequence (CSSValue[] contents)
  {
    this.contents = (CSSValue[]) contents.clone();
  }

  public CSSValue[] getContents ()
  {
    return (CSSValue[]) contents.clone();
  }

  public String getCSSText ()
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < contents.length; i++)
    {
      if (i != 0)
      {
        b.append(" ");
      }
      CSSValue content = contents[i];
      b.append(content.getCSSText());
    }
    return b.toString();
  }
}

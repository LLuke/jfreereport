package org.jfree.layouting.model.content;

public class StringContent implements ContentToken
{
  private String content;

  public StringContent (String content)
  {
    if (content == null)
    {
      throw new NullPointerException();
    }
    this.content = content;
  }

  public String getContent ()
  {
    return content;
  }

  public int getType ()
  {
    return ContentToken.STRING_CONTENT;
  }
}

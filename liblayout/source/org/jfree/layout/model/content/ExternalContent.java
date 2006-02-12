package org.jfree.layouting.model.content;

public class ExternalContent implements ContentToken
{
  private Object data;

  public ExternalContent (Object data)
  {
    this.data = data;
  }

  public Object getData ()
  {
    return data;
  }

  public int getType ()
  {
    return EXTERNAL_CONTENT;
  }
}

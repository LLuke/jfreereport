package org.jfree.layouting.layouter.loader;

import java.net.URL;

import org.jfree.layouting.input.ExternalResourceData;

public class GenericExternalResource implements ExternalResourceData
{
  private Object data;
  private URL source;

  public GenericExternalResource (Object data, URL source)
  {
    this.data = data;
    this.source = source;
  }

  public URL getSource ()
  {
    // todo implement me
    return source;
  }

  public Object getData ()
  {
    return data;
  }
}

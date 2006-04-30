package org.jfree.report.function.strings;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

import org.jfree.report.function.AbstractExpression;
import org.jfree.report.DataSourceException;

public class URLEncodeExpression extends AbstractExpression
{
  private String field;

  public URLEncodeExpression ()
  {
  }

  public String getField ()
  {
    return field;
  }

  public void setField (String field)
  {
    this.field = field;
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue () throws DataSourceException
  {
    Object value = getDataRow().get(getField());
    if (value == null)
    {
      return null;
    }
    try
    {
      return URLEncoder.encode(String.valueOf(value), "iso-8859-1");
    }
    catch (UnsupportedEncodingException e)
    {
      return null;
    }
  }


}

/**
 * Date: Jan 11, 2003
 * Time: 2:00:07 PM
 *
 * $Id: AbstractTemplate.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

public abstract class AbstractTemplate extends AbstractObjectDescription implements Template
{
  public static final String VALUE_PARAMETER = "value";
  public static final String NULL_VALUE_PARAMETER = "null-value";
  public static final String FORMAT_PARAMETER = "format";
  public static final String FIELD_PARAMETER = "field";
  public static final String BASE_URL_PARAMETER = "base-url";

  private String name;

  public AbstractTemplate(String name)
  {
    super(DataSource.class);
    this.name = name;
  }

  public final Object createObject()
  {
    return createDataSource();
  }

  public abstract DataSource createDataSource();

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    if (name == null) throw new NullPointerException();
    this.name = name;
  }
}

/**
 * Date: Jan 14, 2003
 * Time: 6:31:18 PM
 *
 * $Id$
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.filter.SimpleDateFormatFilter;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.DataRow;

public class LabelTemplate extends AbstractTemplate
{
  private StaticDataSource staticDataSource;
  private StringFilter stringFilter;

  public LabelTemplate()
  {
    staticDataSource = new StaticDataSource();
    stringFilter = new StringFilter();
    stringFilter.setDataSource(staticDataSource);
  }

  public void setContent(String content)
  {
    staticDataSource.setValue(content);
  }

  public String getContent()
  {
    return (String) (staticDataSource.getValue());
  }

  public String getNullValue()
  {
    return stringFilter.getNullValue();
  }

  public void setNullValue(String nullValue)
  {
    stringFilter.setNullValue(nullValue);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return stringFilter.getValue();
  }

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    LabelTemplate template = (LabelTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.staticDataSource = (StaticDataSource) template.stringFilter.getDataSource();
    return template;
  }

}

package org.jfree.report.filter.templates;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.MessageFormatFilter;
import org.jfree.report.filter.StringFilter;
import org.jfree.report.filter.ReportConnectable;

public class MessageFieldTemplate extends AbstractTemplate
        implements ReportConnectable
{
  /** A string filter. */
  private StringFilter stringFilter;

  private MessageFormatFilter messageFormatFilter;

  /**
   * Creates a new string field template.
   */
  public MessageFieldTemplate()
  {
    messageFormatFilter = new MessageFormatFilter();
    stringFilter = new StringFilter();
    stringFilter.setDataSource(messageFormatFilter);
  }

  public String getFormat ()
  {
    return messageFormatFilter.getFormatString();
  }

  public void setFormat (final String format)
  {
    this.messageFormatFilter.setFormatString(format);
  }

  /**
   * Returns the value displayed by the field when the data source value is <code>null</code>.
   *
   * @return A value to represent <code>null</code>.
   */
  public String getNullValue()
  {
    return stringFilter.getNullValue();
  }

  /**
   * Sets the value displayed by the field when the data source value is <code>null</code>.
   *
   * @param nullValue  the value that represents <code>null</code>.
   */
  public void setNullValue(final String nullValue)
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
   * Clones the template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final MessageFieldTemplate template = (MessageFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.messageFormatFilter = (MessageFormatFilter) template.stringFilter.getDataSource();
    return template;
  }

  public void registerReportDefinition(final ReportDefinition reportDefinition)
  {
    messageFormatFilter.registerReportDefinition(reportDefinition);
  }

  public void unregisterReportDefinition(final ReportDefinition reportDefinition)
  {
    messageFormatFilter.unregisterReportDefinition(reportDefinition);
  }
}

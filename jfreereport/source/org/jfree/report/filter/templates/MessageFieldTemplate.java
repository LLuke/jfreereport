package org.jfree.report.filter.templates;

import java.text.MessageFormat;

import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.StringFilter;
import org.jfree.report.filter.MessageFormatFilter;
import org.jfree.report.ReportDefinition;

public class MessageFieldTemplate extends AbstractTemplate
{
  /** The data-row data source. */
  private DataRowDataSource dataRowDataSource;

  /** A string filter. */
  private StringFilter stringFilter;

  private MessageFormatFilter messageFormatFilter;

  /**
   * Creates a new string field template.
   */
  public MessageFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    messageFormatFilter = new MessageFormatFilter();
    messageFormatFilter.setDataSource(dataRowDataSource);
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

  public MessageFormat getMessageFormat ()
  {
    return (MessageFormat) messageFormatFilter.getFormatter();
  }

  public void setMessageFormat (final MessageFormat messageFormat)
  {
    messageFormatFilter.setFormatter(messageFormat);
  }

  /**
   * Returns the field name.
   *
   * @return The field name.
   */
  public String getField()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field  the field name.
   */
  public void setField(final String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
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
    template.dataRowDataSource = (DataRowDataSource) template.messageFormatFilter.getDataSource();
    return template;
  }

  public void registerReportDefinition(final ReportDefinition reportDefinition)
  {
    getDataRowDataSource().registerReportDefinition(reportDefinition);
  }

  public void unregisterReportDefinition(final ReportDefinition reportDefinition)
  {
    getDataRowDataSource().unregisterReportDefinition(reportDefinition);
  }

  protected DataRowDataSource getDataRowDataSource()
  {
    return dataRowDataSource;
  }
}

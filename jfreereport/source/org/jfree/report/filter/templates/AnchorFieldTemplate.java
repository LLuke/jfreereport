package org.jfree.report.filter.templates;

import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.StringFilter;
import org.jfree.report.filter.AnchorFilter;
import org.jfree.report.ReportDefinition;

public class AnchorFieldTemplate extends AbstractTemplate
{
  /** The data-row data source. */
  private DataRowDataSource dataRowDataSource;

  /** A string filter. */
  private AnchorFilter anchorFilter;

  /**
   * Creates a new string field template.
   */
  public AnchorFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    anchorFilter = new AnchorFilter();
    anchorFilter.setDataSource(dataRowDataSource);
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
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return anchorFilter.getValue();
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
    final AnchorFieldTemplate template = (AnchorFieldTemplate) super.clone();
    template.anchorFilter = (AnchorFilter) anchorFilter.clone();
    template.dataRowDataSource = (DataRowDataSource) template.anchorFilter.getDataSource();
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

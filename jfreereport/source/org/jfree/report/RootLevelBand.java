package org.jfree.report;

public interface RootLevelBand
{
  /**
   * Assigns the report definition. Don't play with that function, unless you know what
   * you are doing. You might get burned.
   *
   * @param reportDefinition the report definition.
   */
  public void setReportDefinition (ReportDefinition reportDefinition);

  /**
   * Returns the assigned report definition (or null).
   *
   * @return the report definition.
   */
  public ReportDefinition getReportDefinition ();
}

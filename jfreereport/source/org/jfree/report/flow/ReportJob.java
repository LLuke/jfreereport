package org.jfree.report.flow;

import java.awt.print.PageFormat;
import java.io.Serializable;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.ReportDataFactory;
import org.jfree.report.i18n.ResourceBundleFactory;
import org.jfree.report.util.ReportParameters;

/**
 * A report job holds all properties that are required to successfully execute
 * a report process. A report job does not hold output target specific
 * parameters like target file names etc.
 */
public interface ReportJob extends Serializable, Cloneable
{
  ModifiableConfiguration getConfiguration();

  ReportParameters getParameters();

  ReportStructureRoot getReportStructureRoot();

  ReportDataFactory getDataFactory();

  ReportJob derive();

  void close();

  ResourceBundleFactory getResourceBundleFactory();

  String getName();

//  PageFormat getPageFormat();
//
//  void setPageFormat(PageFormat pageFormat);
}

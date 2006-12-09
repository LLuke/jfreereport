package org.jfree.report.flow;

import java.awt.print.PageFormat;
import java.io.Serializable;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.ReportDataFactory;
import org.jfree.report.i18n.ResourceBundleFactory;
import org.jfree.report.util.ReportParameters;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 08.12.2006
 * Time: 15:09:50
 * To change this template use File | Settings | File Templates.
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

  PageFormat getPageFormat();

  void setPageFormat(PageFormat pageFormat);
}

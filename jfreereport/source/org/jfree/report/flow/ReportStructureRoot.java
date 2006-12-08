package org.jfree.report.flow;

import java.util.Locale;

import org.jfree.report.ReportDataFactory;
import org.jfree.report.util.ReportParameters;
import org.jfree.util.Configuration;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.ResourceKey;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 08.12.2006
 * Time: 14:53:30
 * To change this template use File | Settings | File Templates.
 */
public interface ReportStructureRoot
{
  ReportDataFactory getDataFactory();

  ReportParameters getInputParameters();

  Configuration getConfiguration();

  Locale getLocale();

  ResourceManager getResourceManager();

  ResourceKey getBaseResource();
}

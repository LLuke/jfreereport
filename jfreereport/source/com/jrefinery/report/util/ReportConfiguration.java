/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ------------------------
 * ReportConfiguration.java
 * ------------------------
 *
 * $Id: ReportConfiguration.java,v 1.5 2002/11/27 12:20:34 taqua Exp $
 *
 * Changes
 * -------
 * 12-Nov-2002 : Added Javadoc comments (DG);
 *
 */

package com.jrefinery.report.util;

import com.lowagie.text.pdf.BaseFont;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Global and Local configurations for JFreeReport. More documentation needed
 *
 * @author TM
 */
public class ReportConfiguration
{
  /** The 'disable logging' property key. */
  public static final String DISABLE_LOGGING = "com.jrefinery.report.NoDefaultDebug";

  /** The default 'disable logging' property value. */
  public static final String DISABLE_LOGGING_DEFAULT = "false";

  /** The 'og level' property key. */
  public static final String LOGLEVEL = "com.jrefinery.report.LogLevel";

  /** The default 'log level' property value. */
  public static final String LOGLEVEL_DEFAULT = "Info";

  /** The 'log target' property key. */
  public static final String LOGTARGET = "com.jrefinery.report.LogTarget";

  /** The default 'log target' property value. */
  public static final String LOGTARGET_DEFAULT = SystemOutLogTarget.class.getName();

  /** The 'PDF auto init' property key. */
  public static final String PDFTARGET_AUTOINIT
      = "com.jrefinery.report.targets.PDFOutputTarget.AUTOINIT";

  /** The default 'PDF auto init' property value. */
  public static final String PDFTARGET_AUTOINIT_DEFAULT = "true";

  /** The 'PDF encoding' property key. */
  public static final String PDFTARGET_ENCODING
      = "com.jrefinery.report.targets.PDFOutputTarget.ENCODING";

  /** The default 'PDF encoding' property value. */
  public static final String PDFTARGET_ENCODING_DEFAULT = BaseFont.WINANSI;

  /** The path to a local copy of the DTD. */
  public static final String PARSER_DTD = "com.jrefinery.report.dtd";

  /** The 'ResultSet factory mode'. */
  public static final String RESULTSET_FACTORY_MODE = "com.jrefinery.report.TableFactoryMode";

  /** Storage for the configuration properties. */
  private Properties configuration;

  /** The parent configuration (null if this is the root configuration). */
  private ReportConfiguration parentConfiguration;

  /** The global configuration. */
  private static ReportConfiguration globalConfig;

  /**
   * A report configuration that reads its values from the jfreereport.properties file.
   */
  private static class PropertyFileReportConfiguration extends ReportConfiguration
  {
    /**
     * Creates a new report properties configuration.
     */
    public PropertyFileReportConfiguration()
    {
      getConfiguration().put (DISABLE_LOGGING, DISABLE_LOGGING_DEFAULT);
      getConfiguration().put (LOGLEVEL, LOGLEVEL_DEFAULT);
      getConfiguration().put (PDFTARGET_AUTOINIT, PDFTARGET_AUTOINIT_DEFAULT);
      getConfiguration().put (PDFTARGET_ENCODING, PDFTARGET_ENCODING_DEFAULT);

      InputStream in = this.getClass().getResourceAsStream("/jfreereport.properties");
      if (in == null)
      {
        in = this.getClass().getResourceAsStream("/com/jrefinery/report/jfreereport.properties");
      }
      if (in != null)
      {
        try
        {
          getConfiguration().load(in);
        }
        catch (IOException ioe)
        {
          Log.warn ("Unable to read global configuration", ioe);
        }
      }
    }
  }

  /**
   * A property configuration based on system properties.
   */
  private static class SystemPropertyConfiguration extends ReportConfiguration
  {
    /**
     * Default constructor.
     */
    public SystemPropertyConfiguration()
    {
      super(new PropertyFileReportConfiguration());
      getConfiguration().putAll (System.getProperties());
    }
  }

  /**
   * Default constructor.
   */
  protected ReportConfiguration ()
  {
    configuration = new Properties();
  }

  /**
   * Creates a new report configuration.
   *
   * @param globalConfig  ??.
   */
  public ReportConfiguration (ReportConfiguration globalConfig)
  {
    this();
    parentConfiguration = globalConfig;
  }

  /**
   * Returns the configuration property with the specified key.
   *
   * @param key  the property key.
   *
   * @return the property value.
   */
  public String getConfigProperty (String key)
  {
    return getConfigProperty(key, null);
  }

  /**
   * Returns the configuration property with the specified key (or the specified default value
   * if there is no such property).
   *
   * @param key  the property key.
   * @param defaultValue  the default value.
   *
   * @return the property value.
   */
  public String getConfigProperty (String key, String defaultValue)
  {
    String value = configuration.getProperty(key);
    if (value == null)
    {
      if (isRootConfig())
      {
        value = defaultValue;
      }
      else
      {
        value = parentConfiguration.getConfigProperty(key, defaultValue);
      }
    }
    return value;
  }

  /**
   * Sets a configuration property.
   *
   * @param key  the property key.
   * @param value  the property value.
   */
  public void setConfigProperty (String key, String value)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }

    if (value == null)
    {
      configuration.remove(key);
    }
    else
    {
      configuration.setProperty(key, value);
    }
  }

  /**
   * Returns true if this object has no parent.
   *
   * @return boolean.
   */
  private boolean isRootConfig ()
  {
    return parentConfiguration == null;
  }

  /**
   * Returns the log level.
   *
   * @return the log level.
   */
  public String getLogLevel()
  {
    return getConfigProperty(LOGLEVEL, LOGLEVEL_DEFAULT);
  }

  /**
   * Sets the log level.  Permitted log levels are: ERROR, WARN, INFO and DEBUG.
   *
   * @param logLevel  the new log level.
   */
  public void setLogLevel(String logLevel)
  {
    setConfigProperty(LOGLEVEL, logLevel);
  }

  /**
   * Returns ??.
   *
   * @return ??.
   */
  public boolean isPDFTargetAutoInit()
  {
    return getConfigProperty(PDFTARGET_AUTOINIT,
                             PDFTARGET_AUTOINIT_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * Sets the PDF target auto init status.
   *
   * @param autoInit  the new status.
   */
  public void setPDFTargetAutoInit(boolean autoInit)
  {
    setConfigProperty(PDFTARGET_AUTOINIT, String.valueOf(autoInit));
  }

  /**
   * Returns true if logging is disabled.
   *
   * @return boolean.
   */
  public boolean isDisableLogging()
  {
    return getConfigProperty(DISABLE_LOGGING, DISABLE_LOGGING_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * Sets the flag that disables logging.
   *
   * @param disableLogging  the flag.
   */
  public void setDisableLogging(boolean disableLogging)
  {
    setConfigProperty(DISABLE_LOGGING, String.valueOf(disableLogging));
  }

  /**
   * Returns the PDF encoding property value.
   *
   * @return the PDF encoding property value.
   */
  public String getPdfTargetEncoding()
  {
    return getConfigProperty(PDFTARGET_ENCODING, PDFTARGET_ENCODING_DEFAULT);
  }

  /**
   * Sets the PDF encoding property value.
   *
   * @param pdfTargetEncoding  the new encoding.
   */
  public void setPdfTargetEncoding(String pdfTargetEncoding)
  {
    setConfigProperty(PDFTARGET_ENCODING, pdfTargetEncoding);
  }

  /**
   * Returns the collection of properties for the configuration.
   *
   * @return the properties.
   */
  protected Properties getConfiguration()
  {
    return configuration;
  }

  /**
   * Returns the global configuration.
   *
   * @return the global configuration.
   */
  public static ReportConfiguration getGlobalConfig ()
  {
    if (globalConfig == null)
    {
      globalConfig = new ReportConfiguration (new SystemPropertyConfiguration());
    }
    return globalConfig;
  }

  /**
   * Returns the current log target.
   *
   * @return the log target.
   */
  public String getLogTarget()
  {
    return getConfigProperty(LOGTARGET, LOGTARGET_DEFAULT);
  }

  /**
   * Sets the log target.
   *
   * @param logTarget  the new log target.
   */
  public void setLogTarget(String logTarget)
  {
    setConfigProperty(LOGTARGET, logTarget);
  }

}

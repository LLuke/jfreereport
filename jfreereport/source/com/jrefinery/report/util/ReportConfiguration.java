/**
 * ----------------------
 * ReportConfiguration.java
 * ----------------------
 * 
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.util;

import com.lowagie.text.pdf.BaseFont;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Global and Local configurations for JFreeReport. More documentation needed
 */
public class ReportConfiguration
{
  // same as LogLevel "off"
  public static final String DISABLE_LOGGING = "com.jrefinery.report.NoDefaultDebug";
  public static final String DISABLE_LOGGING_DEFAULT = "false";

  // Debug, Warn, Error, Off
  public static final String LOGLEVEL = "com.jrefinery.report.LogLevel";
  public static final String LOGLEVEL_DEFAULT = "debug";

  // Debug, Warn, Error, Off
  public static final String LOGTARGET = "com.jrefinery.report.LogTarget";
  public static final String LOGTARGET_DEFAULT = SystemOutLogTarget.class.getName();

  // Read Fonts from FontDir
  public static final String PDFTARGET_AUTOINIT = "com.jrefinery.report.targets.PDFOutputTarget.AUTOINIT";
  public static final String PDFTARGET_AUTOINIT_DEFAULT = "true";

  // PDFEncoding
  public static final String PDFTARGET_ENCODING = "com.jrefinery.report.targets.PDFOutputTarget.ENCODING";
  public static final String PDFTARGET_ENCODING_DEFAULT = BaseFont.WINANSI;

  // path to a local copy of the DTD
  public static final String PARSER_DTD = "com.jrefinery.report.dtd";

  // simple or ""
  public static final String RESULTSET_FACTORY_MODE = "com.jrefinery.report.TableFactoryMode";

  private Properties configuration;
  private ReportConfiguration parentConfiguration;
  private static ReportConfiguration globalConfig;

  private static class PropertyFileReportConfiguration extends ReportConfiguration
  {
    public PropertyFileReportConfiguration()
    {
      getConfiguration().put (DISABLE_LOGGING, DISABLE_LOGGING_DEFAULT);
      getConfiguration().put (LOGLEVEL, LOGLEVEL_DEFAULT);
      getConfiguration().put (PDFTARGET_AUTOINIT, PDFTARGET_AUTOINIT_DEFAULT);
      getConfiguration().put (PDFTARGET_ENCODING, PDFTARGET_ENCODING_DEFAULT);

      InputStream in = this.getClass().getResourceAsStream("com/jrefinery/report/jfreereport.properties");
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

  private static class SystemPropertyConfiguration extends ReportConfiguration
  {
    public SystemPropertyConfiguration()
    {
      super(new PropertyFileReportConfiguration());
      getConfiguration().putAll (System.getProperties());
    }
  }

  private ReportConfiguration ()
  {
    configuration = new Properties();
  }

  public ReportConfiguration (ReportConfiguration globalConfig)
  {
    this();
    parentConfiguration = globalConfig;
  }

  public String getConfigProperty (String key)
  {
    return getConfigProperty(key, null);
  }

  public String getConfigProperty (String key, String defaultValue)
  {
    String value = (String) configuration.getProperty(key);
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

  private boolean isRootConfig ()
  {
    return parentConfiguration == null;
  }

  public void setConfigProperty (String key, String value)
  {
    if (key == null) throw new NullPointerException();
    if (value == null)
    {
      configuration.remove(key);
    }
    else
    {
      configuration.setProperty(key, value);
    }
  }

  public String getLogLevel()
  {
    return getConfigProperty(LOGLEVEL, LOGLEVEL_DEFAULT);
  }

  public void setLogLevel(String logLevel)
  {
    setConfigProperty(LOGLEVEL, logLevel);
  }

  public boolean isPDFTargetAutoInit()
  {
    return getConfigProperty(PDFTARGET_AUTOINIT, PDFTARGET_AUTOINIT_DEFAULT).equalsIgnoreCase("true");
  }

  public void setPDFTargetAutoInit(boolean autoInit)
  {
    setConfigProperty(PDFTARGET_AUTOINIT, String.valueOf(autoInit));
  }

  public boolean isDisableLogging()
  {
    return getConfigProperty(DISABLE_LOGGING, DISABLE_LOGGING_DEFAULT).equalsIgnoreCase("true");
  }

  public void setDisableLogging(boolean disableLogging)
  {
    setConfigProperty(DISABLE_LOGGING, String.valueOf(disableLogging));
  }

  public String getPdfTargetEncoding()
  {
    return getConfigProperty(PDFTARGET_ENCODING, PDFTARGET_ENCODING_DEFAULT);
  }

  public void setPdfTargetEncoding(String pdfTargetEncoding)
  {
    setConfigProperty(PDFTARGET_ENCODING, pdfTargetEncoding);
  }

  protected Properties getConfiguration()
  {
    return configuration;
  }

  public static ReportConfiguration getGlobalConfig ()
  {
    if (globalConfig == null)
    {
      globalConfig = new ReportConfiguration (new SystemPropertyConfiguration());
    }
    return globalConfig;
  }

  public String getLogTarget()
  {
    return getConfigProperty(LOGTARGET, LOGTARGET_DEFAULT);;
  }

  public void setLogTarget(String logTarget)
  {
    setConfigProperty(LOGTARGET, logTarget);
  }
}

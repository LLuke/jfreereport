/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * (C)opyright 2002-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportConfiguration.java,v 1.17 2005/02/23 21:06:06 taqua Exp $
 *
 * Changes
 * -------
 * 06-Nov-2002 : Initial release
 * 12-Nov-2002 : Added Javadoc comments (DG);
 * 29-Nov-2002 : Fixed bugs reported by CheckStyle (DG)
 * 05-Dec-2002 : Documentation
 *
 */

package org.jfree.report.util;

import java.util.Enumeration;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.JFreeReportBoot;
import org.jfree.util.Configuration;

/**
 * Global and local configurations for JFreeReport.
 * <p/>
 * You can access the global configuration using this statement in your code:
 * <p/>
 * <code>ReportConfiguration config = ReportConfiguration.getGlobalConfig();</code>
 * <p/>
 * You can access the local configuration for a report using this:
 * <p/>
 * <code>ReportConfiguration local = myReport.getReportConfiguration();
 * <p/>
 * You can also specify the local configuration for a report via the XML report template
 * file.
 * <p/>
 * The report configuration can be modified using the configuration editor in the
 * gui-config module.
 *
 * @author Thomas Morgner
 */
public class ReportConfiguration extends HierarchicalConfiguration
{
  /**
   * A wrappper around the user supplied global configuration.
   */
  private static class UserConfigWrapper extends HierarchicalConfiguration
          implements Configuration
  {
    /** The wrapped configuration. */
    private Configuration wrappedConfiguration;

    /**
     * Default constructor.
     */
    public UserConfigWrapper ()
    {
    }

    /**
     * Sets a new configuration. This configuration will be inserted into the
     * report configuration hierarchy. Set this property to null to disable
     * the user defined configuration.
     *
     * @param wrappedConfiguration the wrapped configuration.
     */
    public void setWrappedConfiguration (final Configuration wrappedConfiguration)
    {
      this.wrappedConfiguration = wrappedConfiguration;
    }

    /**
     * Returns the user supplied global configuration, if exists.
     *
     * @return the user configuration.
     */
    public Configuration getWrappedConfiguration ()
    {
      return wrappedConfiguration;
    }

    /**
     * Returns the configuration property with the specified key.
     *
     * @param key the property key.
     * @return the property value.
     */
    public String getConfigProperty (final String key)
    {
      if (wrappedConfiguration == null)
      {
        return getParentConfig().getConfigProperty(key);
      }

      final String retval = wrappedConfiguration.getConfigProperty(key);
      if (retval != null)
      {
        return retval;
      }
      return getParentConfig().getConfigProperty(key);
    }

    /**
     * Returns the configuration property with the specified key
     * (or the specified default value if there is no such property).
     * <p/>
     * If the property is not defined in this configuration, the code
     * will lookup the property in the parent configuration.
     *
     * @param key          the property key.
     * @param defaultValue the default value.
     * @return the property value.
     */
    public String getConfigProperty (final String key, final String defaultValue)
    {
      if (wrappedConfiguration == null)
      {
        return getParentConfig().getConfigProperty(key, defaultValue);
      }

      final String retval = wrappedConfiguration.getConfigProperty(key, null);
      if (retval != null)
      {
        return retval;
      }
      return getParentConfig().getConfigProperty(key, defaultValue);
    }

    /**
     * Sets a configuration property.
     *
     * @param key   the property key.
     * @param value the property value.
     */
    public void setConfigProperty (final String key, final String value)
    {
      if (wrappedConfiguration instanceof ModifiableConfiguration)
      {
        final ModifiableConfiguration modConfiguration =
                (ModifiableConfiguration) wrappedConfiguration;
        modConfiguration.setConfigProperty(key, value);
      }
    }

    /**
     * Returns all defined configuration properties for the report. The enumeration
     * contains all keys of the changed properties, properties set from files or
     * the system properties are not included.
     *
     * @return all defined configuration properties for the report.
     */
    public Enumeration getConfigProperties ()
    {
      if (wrappedConfiguration instanceof ModifiableConfiguration)
      {
        final ModifiableConfiguration modConfiguration =
                (ModifiableConfiguration) wrappedConfiguration;
        return modConfiguration.getConfigProperties();
      }
      return super.getConfigProperties();
    }
  }

  /**
   * The text aliasing configuration key.
   */
  public static final String FONTRENDERER_USEALIASING
          = "org.jfree.report.layout.fontrenderer.UseAliasing";

  /**
   * The text aliasing configuration default value. Is "false".
   */
  public static final String FONTRENDERER_USEALIASING_DEFAULT = "false";

  /**
   * The G2 fontrenderer bug override configuration key.
   */
  public static final String FONTRENDERER_ISBUGGY_FRC
          = "org.jfree.report.layout.fontrenderer.IsBuggyFRC";

  /**
   * The G2 fontrenderer bug override. Is "false".
   */
  public static final String FONTRENDERER_ISBUGGY_FRC_DEFAULT = "false";

  /**
   * The property key, which defines whether to be verbose when generating the content.
   */
  public static final String PRINT_OPERATION_COMMENT
          = "org.jfree.report.PrintOperationComment";

  /**
   * The default 'print operation comment' property value.
   */
  public static final String PRINT_OPERATION_COMMENT_DEFAULT = "false";

  /**
   * The property key, which defines whether errors abort the report generation.
   */
  public static final String STRICT_ERRORHANDLING
          = "org.jfree.report.StrictErrorHandling";

  /**
   * The default 'strict errorhandling' property value.
   */
  public static final String STRICT_ERRORHANDLING_DEFAULT = "true";

  /**
   * The 'warn on invalid columns' property key.
   */
  public static final String WARN_INVALID_COLUMNS
          = "org.jfree.report.WarnInvalidColumns";

  /**
   * The default 'warn on invalid columns' property value.
   */
  public static final String WARN_INVALID_COLUMNS_DEFAULT = "false";

  /**
   * The 'disable logging' property key.
   */
  public static final String DISABLE_LOGGING
          = "org.jfree.report.NoDefaultDebug";

  /**
   * The 'no-printer-available' property key.
   */
  public static final String NO_PRINTER_AVAILABLE
          = "org.jfree.report.NoPrinterAvailable";

  /**
   * The default 'disable logging' property value.
   */
  public static final String DISABLE_LOGGING_DEFAULT = "false";

  /**
   * The 'log level' property key.
   */
  public static final String LOGLEVEL
          = "org.jfree.report.LogLevel";

  /**
   * The default 'log level' property value.
   */
  public static final String LOGLEVEL_DEFAULT = "Info";

  /**
   * The 'log target' property key.
   */
  public static final String LOGTARGET = "org.jfree.report.LogTarget";

  /**
   * The default 'log target' property value.
   */
  public static final String LOGTARGET_DEFAULT = SystemOutLogTarget.class.getName();

  /**
   * The default resourcebundle that should be used for ResourceFileFilter. This property
   * must be applied by the parser.
   */
  public static final String REPORT_RESOURCE_BUNDLE_KEY
          = "org.jfree.report.ResourceBundle";

  /**
   * Helper method to read the platform default encoding from the VM's system properties.
   *
   * @return the contents of the system property "file.encoding".
   */
  public static String getPlatformDefaultEncoding ()
  {
    try
    {
      return System.getProperty("file.encoding", "Cp1252");
    }
    catch (SecurityException se)
    {
      return "Cp1252";
    }
  }

  /**
   * The global configuration.
   */
  private static transient ReportConfiguration globalConfig;

  /** The user supplied configuration. */
  private static transient UserConfigWrapper userConfig = new UserConfigWrapper();

  /**
   * Default constructor.
   */
  protected ReportConfiguration ()
  {
    this(null);
  }

  /**
   * Creates a new report configuration.
   *
   * @param globalConfig the global configuration.
   */
  public ReportConfiguration (final ReportConfiguration globalConfig)
  {
    super(globalConfig);
  }

  /**
   * Returns the log level.
   *
   * @return the log level.
   */
  public String getLogLevel ()
  {
    return getConfigProperty(LOGLEVEL, LOGLEVEL_DEFAULT);
  }

  /**
   * Sets the log level, which is read from the global report configuration at the point
   * that the classloader loads the {@link org.jfree.util.Log} class.
   * <p/>
   * Valid log levels are:
   * <p/>
   * <ul> <li><code>"Error"</code> - error messages;</li> <li><code>"Warn"</code> -
   * warning messages;</li> <li><code>"Info"</code> - information messages;</li>
   * <li><code>"Debug"</code> - debug messages;</li> </ul>
   * <p/>
   * Notes: <ul> <li>the setting is not case sensitive.</li> <li>changing the log level
   * after the {@link org.jfree.util.Log} class has been loaded will have no effect.</li> <li>to turn of
   * logging altogether, use the {@link #setDisableLogging} method.</li> </ul>
   *
   * @param level the new log level.
   */
  public void setLogLevel (final String level)
  {
    setConfigProperty(LOGLEVEL, level);
  }

  /**
   * Returns <code>true</code> if logging is disabled, and <code>false</code> otherwise.
   *
   * @return true, if logging is completly disabled, false otherwise.
   */
  public boolean isDisableLogging ()
  {
    return getConfigProperty
            (DISABLE_LOGGING, DISABLE_LOGGING_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * Sets the flag that disables logging.
   * <p/>
   * To switch off logging globally, you can use the following code:
   * <p/>
   * <code>ReportConfiguration.getGlobalConfig().setDisableLogging(true);</code>
   *
   * @param disableLogging the flag.
   */
  public void setDisableLogging (final boolean disableLogging)
  {
    setConfigProperty(DISABLE_LOGGING, String.valueOf(disableLogging));
  }

  /**
   * Returns the global configuration for JFreeReport.
   * <p/>
   * In the current implementation, the configuration has no properties defined, but
   * references a parent configuration that: <ul> <li>copies across all the
   * <code>System</code> properties to use as report configuration properties (obviously
   * the majority of them will not apply to reports);</li> <li>itself references a parent
   * configuration that reads its properties from a file <code>jfreereport.properties</code>.
   * </ul>
   *
   * @return the global configuration.
   */
  public static synchronized ReportConfiguration getGlobalConfig ()
  {
    if (globalConfig == null)
    {
      globalConfig = new ReportConfiguration();

      final PropertyFileReportConfiguration rootProperty = new PropertyFileReportConfiguration();
      rootProperty.load("/org/jfree/report/jfreereport.properties");
      rootProperty.load("/org/jfree/report/ext/jfreereport-ext.properties");
      globalConfig.insertConfiguration(rootProperty);
      globalConfig.insertConfiguration(JFreeReportBoot.getInstance().getPackageManager()
              .getPackageConfiguration());

      final PropertyFileReportConfiguration baseProperty = new PropertyFileReportConfiguration();
      baseProperty.load("/jfreereport.properties");
      globalConfig.insertConfiguration(baseProperty);

      globalConfig.insertConfiguration(userConfig);

      final SystemPropertyConfiguration systemConfig = new SystemPropertyConfiguration();
      globalConfig.insertConfiguration(systemConfig);
      // just in case it is not already started ...

      JFreeReportBoot.getInstance().start();
    }
    return globalConfig;
  }

  /**
   * Returns the current log target.
   *
   * @return the log target.
   */
  public String getLogTarget ()
  {
    return getConfigProperty(LOGTARGET, LOGTARGET_DEFAULT);
  }

  /**
   * Sets the log target.
   *
   * @param logTarget the new log target.
   */
  public void setLogTarget (final String logTarget)
  {
    setConfigProperty(LOGTARGET, logTarget);
  }

  /**
   * Returns true, if invalid columns in the dataRow are logged as warning.
   *
   * @return true, if invalid columns in the dataRow are logged as warning.
   */
  public boolean isWarnInvalidColumns ()
  {
    return getConfigProperty(WARN_INVALID_COLUMNS,
            WARN_INVALID_COLUMNS_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * Set to true, if you want to get informed when invalid column-requests are made to the
   * DataRow.
   *
   * @param warnInvalidColumns the warning flag
   */
  public void setWarnInvalidColumns (final boolean warnInvalidColumns)
  {
    setConfigProperty(WARN_INVALID_COLUMNS, String.valueOf(warnInvalidColumns));
  }

  /**
   * Returns true, if physical operation comments should be added to the physical page.
   * This is only usefull for debugging.
   *
   * @return true, if comments are enabled.
   */
  public boolean isPrintOperationComment ()
  {
    return getConfigProperty(PRINT_OPERATION_COMMENT,
            PRINT_OPERATION_COMMENT_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * set to true, if physical operation comments should be added to the physical page.
   * This is only usefull for debugging.
   *
   * @param print set to true, if comments should be enabled.
   */
  public void setPrintOperationComment (final boolean print)
  {
    setConfigProperty(PRINT_OPERATION_COMMENT, String.valueOf(print));
  }

  /**
   * Returns true, if the Graphics2D should use aliasing to render text. Defaults to
   * false.
   *
   * @return true, if aliasing is enabled.
   */
  public boolean isFontRendererUseAliasing ()
  {
    return getConfigProperty(FONTRENDERER_USEALIASING,
            FONTRENDERER_USEALIASING_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * set to true, if the Graphics2D should use aliasing to render text. Defaults to
   * false.
   *
   * @param alias set to true, if the Graphics2D should use aliasing.
   */
  public void setFontRendererUseAliasing (final boolean alias)
  {
    setConfigProperty(FONTRENDERER_USEALIASING, String.valueOf(alias));
  }

  /**
   * Returns true, if the Graphics2D implementation is buggy and is not really able to
   * place/calculate the fontsizes correctly. Defaults to false. (SunJDK on Windows is
   * detected and corrected, Linux SunJDK 1.3 is buggy, but not detectable).
   *
   * @return true, if the Graphics2D implementation does not calculate the string
   *         positions correctly and an alternative implementation should be used.
   */
  public boolean isFontRendererBuggy ()
  {
    return getConfigProperty(FONTRENDERER_ISBUGGY_FRC,
            FONTRENDERER_ISBUGGY_FRC_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * set to true, if the Graphics2D implementation is buggy and is not really able to
   * place/calculate the fontsizes correctly. Defaults to false. (SunJDK on Windows is
   * detected and corrected, Linux SunJDK 1.3 is buggy, but not detectable).
   *
   * @param buggy set to true, if the Graphics2D implementation does not calculate the
   *              string positions correctly and an alternative implementation should be
   *              used.
   */
  public void setFontRendererBuggy (final boolean buggy)
  {
    setConfigProperty(FONTRENDERER_ISBUGGY_FRC, String.valueOf(buggy));
  }

  /**
   * Checks, whether a stricter error handling is applied to the report processing. If set
   * to true, then errors in the handling of report events will cause the report
   * processing to fail. This is a safe-and-paranoid setting, life is simpler with this
   * set to false. The property defaults to false, as this is the old behaviour.
   * <p/>
   * A properly defined report should not throw exceptions, so it is safe to set this to
   * true.
   *
   * @return true, if the strict handling is enabled, false otherwise.
   */
  public boolean isStrictErrorHandling ()
  {
    return getConfigProperty(STRICT_ERRORHANDLING,
            STRICT_ERRORHANDLING_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * Defines, whether a stricter error handling is applied to the report processing. If
   * set to true, then errors in the handling of report events will cause the report
   * processing to fail. This is a safe-and-paranoid setting, life is simpler with this
   * set to false. The property defaults to false, as this is the old behaviour.
   * <p/>
   * A properly defined report should not throw exceptions, so it is safe to set this to
   * true.
   *
   * @param strictErrorHandling if set to true, then errors in the event dispatching will
   *                            cause the reporting to fail.
   */
  public void setStrictErrorHandling (final boolean strictErrorHandling)
  {
    setConfigProperty(STRICT_ERRORHANDLING, String.valueOf(strictErrorHandling));
  }

  protected boolean isParentSaved ()
  {
    return this != globalConfig;
  }

  protected void configurationLoaded ()
  {
    if (isParentSaved() == false)
    {
      setParentConfig(globalConfig);
    }
  }

  /**
   * Returns the user supplied global configuration.
   *
   * @return the user configuration, if any.
   */
  public static Configuration getUserConfig ()
  {
    return userConfig.getWrappedConfiguration();
  }

  /**
   * Defines the global user configuration.
   *
   * @param config the user configuration.
   */
  public static void setUserConfig (final Configuration config)
  {
    userConfig.setWrappedConfiguration(config);
  }
}

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
 * $Id: ReportConfiguration.java,v 1.6 2003/08/26 17:35:51 taqua Exp $
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

import org.jfree.report.modules.PackageManager;
import org.jfree.util.Configuration;

/**
 * Global and local configurations for JFreeReport.
 * <p>
 * You can access the global configuration using this statement in your code:
 * <p>
 * <code>ReportConfiguration config = ReportConfiguration.getGlobalConfig();</code>
 * <p>
 * You can access the local configuration for a report using this:
 * <p>
 * <code>ReportConfiguration local = myReport.getReportConfiguration();
 * <p>
 * You can also specify the local configuration for a report via the XML report
 * template file.
 * <p>
 * todo document the initialization order ...
 * <p>
 * The report configuration can be modified using the configuration editor
 * in the gui-config module.
 *
 * @author Thomas Morgner
 */
public class ReportConfiguration implements Configuration, Serializable
{
  /** The text aliasing configuration key. */
  public static final String FONTRENDERER_USEALIASING
      = "org.jfree.report.layout.fontrenderer.UseAliasing";

  /** The text aliasing configuration default value. Is "false". */
  public static final String FONTRENDERER_USEALIASING_DEFAULT = "false";

  /** The G2 fontrenderer bug override configuration key. */
  public static final String FONTRENDERER_ISBUGGY_FRC
      = "org.jfree.report.layout.fontrenderer.IsBuggyFRC";

  /** The G2 fontrenderer bug override. Is "false". */
  public static final String FONTRENDERER_ISBUGGY_FRC_DEFAULT = "false";

  /** The property key, which defines whether to be verbose when generating the content. */
  public static final String PRINT_OPERATION_COMMENT
      = "org.jfree.report.PrintOperationComment";

  /** The default 'print operation comment' property value. */
  public static final String PRINT_OPERATION_COMMENT_DEFAULT = "false";

  /** The property key, which defines whether errors abort the report generation. */
  public static final String STRICT_ERRORHANDLING
      = "org.jfree.report.StrictErrorHandling";

  /** The default 'strict errorhandling' property value. */
  public static final String STRICT_ERRORHANDLING_DEFAULT = "true";

  /** The 'warn on invalid columns' property key. */
  public static final String WARN_INVALID_COLUMNS
      = "org.jfree.report.WarnInvalidColumns";

  /** The default 'warn on invalid columns' property value. */
  public static final String WARN_INVALID_COLUMNS_DEFAULT = "false";

  /** The 'disable logging' property key. */
  public static final String DISABLE_LOGGING
      = "org.jfree.report.NoDefaultDebug";

  /** The default 'disable logging' property value. */
  public static final String DISABLE_LOGGING_DEFAULT = "false";

  /** The 'log level' property key. */
  public static final String LOGLEVEL
      = "org.jfree.report.LogLevel";

  /** The default 'log level' property value. */
  public static final String LOGLEVEL_DEFAULT = "Info";

  /** The 'log target' property key. */
  public static final String LOGTARGET = "org.jfree.report.LogTarget";

  /** The default 'log target' property value. */
  public static final String LOGTARGET_DEFAULT = SystemOutLogTarget.class.getName();

  /**
   * The default resourcebundle that should be used for ResourceFileFilter.
   * This property must be applied by the parser.
   */
  public static final String REPORT_RESOURCE_BUNDLE_KEY
      = "org.jfree.report.ResourceBundle";

  /**
   * Helper method to read the platform default encoding from the VM's system properties.
   * @return the contents of the system property "file.encoding".
   */
  public static String getPlatformDefaultEncoding()
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

  /** Storage for the configuration properties. */
  private final Properties configuration;

  /** The parent configuration (null if this is the root configuration). */
  private transient ReportConfiguration parentConfiguration;

  /** The global configuration. */
  private static transient ReportConfiguration globalConfig;

  /**
   * Default constructor.
   */
  protected ReportConfiguration()
  {
    configuration = new Properties();
  }

  /**
   * Creates a new report configuration.
   *
   * @param globalConfig  the global configuration.
   */
  public ReportConfiguration(final ReportConfiguration globalConfig)
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
  public String getConfigProperty(final String key)
  {
    return getConfigProperty(key, null);
  }

  /**
   * Returns the configuration property with the specified key
   * (or the specified default value if there is no such property).
   * <p>
   * If the property is not defined in this configuration, the code
   * will lookup the property in the parent configuration.
   *
   * @param key  the property key.
   * @param defaultValue  the default value.
   *
   * @return the property value.
   */
  public String getConfigProperty(final String key, final String defaultValue)
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
  public void setConfigProperty(final String key, final String value)
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
   * @return true, if this report is the root configuration, false otherwise.
   */
  private boolean isRootConfig()
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
   * Sets the log level, which is read from the global report configuration at
   * the point that the classloader loads the {@link Log} class.
   * <p>
   * Valid log levels are:
   *
   * <ul>
   * <li><code>"Error"</code> - error messages;</li>
   * <li><code>"Warn"</code> - warning messages;</li>
   * <li><code>"Info"</code> - information messages;</li>
   * <li><code>"Debug"</code> - debug messages;</li>
   * </ul>
   *
   * Notes:
   * <ul>
   * <li>the setting is not case sensitive.</li>
   * <li>changing the log level after the {@link Log} class has been
   * loaded will have no effect.</li>
   * <li>to turn of logging altogether, use the {@link #setDisableLogging} method.</li>
   * </ul>
   *
   * @param level  the new log level.
   */
  public void setLogLevel(final String level)
  {
    setConfigProperty(LOGLEVEL, level);
  }

  /**
   * Returns <code>true</code> if logging is disabled, and <code>false</code> otherwise.
   *
   * @return true, if logging is completly disabled, false otherwise.
   */
  public boolean isDisableLogging()
  {
    return getConfigProperty
        (DISABLE_LOGGING, DISABLE_LOGGING_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * Sets the flag that disables logging.
   * <p>
   * To switch off logging globally, you can use the following code:
   * <p>
   * <code>ReportConfiguration.getGlobalConfig().setDisableLogging(true);</code>
   *
   * @param disableLogging  the flag.
   */
  public void setDisableLogging(final boolean disableLogging)
  {
    setConfigProperty(DISABLE_LOGGING, String.valueOf(disableLogging));
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
   * Returns the global configuration for JFreeReport.
   * <p>
   * In the current implementation, the configuration has no properties defined,
   * but references a parent configuration that:
   * <ul>
   * <li>copies across all the <code>System</code> properties to use as report
   * configuration properties (obviously the majority of them will not apply to reports);</li>
   * <li>itself references a parent configuration that reads its properties from a file
   *     <code>jfreereport.properties</code>.
   * </ul>
   *
   * @return the global configuration.
   */
  public static synchronized ReportConfiguration getGlobalConfig()
  {
    if (globalConfig == null)
    {
      globalConfig = new ReportConfiguration();

      final PropertyFileReportConfiguration rootProperty = new PropertyFileReportConfiguration();
      rootProperty.load("/org/jfree/report/jfreereport.properties");
      rootProperty.load("/org/jfree/report/ext/jfreereport-ext.properties");
      globalConfig.insertConfiguration(rootProperty);
      globalConfig.insertConfiguration(PackageManager.getInstance().getPackageConfiguration());

      final PropertyFileReportConfiguration baseProperty = new PropertyFileReportConfiguration();
      baseProperty.load("/jfreereport.properties");
      globalConfig.insertConfiguration(baseProperty);

      final SystemPropertyConfiguration systemConfig = new SystemPropertyConfiguration();
      globalConfig.insertConfiguration(systemConfig);

      PackageManager.getInstance().init();
    }
    return globalConfig;
  }

  /**
   * The new configuartion will be inserted into the list of report configuration,
   * so that this configuration has the given report configuration instance as parent.
   *
   * @param config the new report configuration.
   */
  protected void insertConfiguration(final ReportConfiguration config)
  {
    config.setParentConfig(getParentConfig());
    setParentConfig(config);
  }

  /**
   * Set the parent configuration. The parent configuration is queried, if the
   * requested configuration values was not found in this report configuration.
   *
   * @param config  the parent configuration.
   */
  protected void setParentConfig(final ReportConfiguration config)
  {
    if (parentConfiguration == this)
    {
      throw new IllegalArgumentException("Cannot add myself as parent configuration.");
    }
    parentConfiguration = config;
  }

  /**
   * Returns the parent configuration. The parent configuration is queried, if the
   * requested configuration values was not found in this report configuration.
   *
   * @return the parent configuration.
   */
  protected ReportConfiguration getParentConfig()
  {
    return parentConfiguration;
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
  public void setLogTarget(final String logTarget)
  {
    setConfigProperty(LOGTARGET, logTarget);
  }

  /**
   * Returns true, if invalid columns in the dataRow are logged as warning.
   *
   * @return true, if invalid columns in the dataRow are logged as warning.
   */
  public boolean isWarnInvalidColumns()
  {
    return getConfigProperty(WARN_INVALID_COLUMNS,
        WARN_INVALID_COLUMNS_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * Set to true, if you want to get informed when invalid column-requests are
   * made to the DataRow.
   *
   * @param warnInvalidColumns the warning flag
   */
  public void setWarnInvalidColumns(final boolean warnInvalidColumns)
  {
    setConfigProperty(WARN_INVALID_COLUMNS, String.valueOf(warnInvalidColumns));
  }

  /**
   * Returns true, if physical operation comments should be added to the physical page.
   * This is only usefull for debugging.
   *
   * @return true, if comments are enabled.
   */
  public boolean isPrintOperationComment()
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
  public void setPrintOperationComment(final boolean print)
  {
    setConfigProperty(PRINT_OPERATION_COMMENT, String.valueOf(print));
  }

  /**
   * Returns true, if the Graphics2D should use aliasing to render text. Defaults to false.
   *
   * @return true, if aliasing is enabled.
   */
  public boolean isFontRendererUseAliasing()
  {
    return getConfigProperty(FONTRENDERER_USEALIASING,
        FONTRENDERER_USEALIASING_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * set to true, if the Graphics2D should use aliasing to render text. Defaults to false.
   *
   * @param alias set to true, if the Graphics2D should use aliasing.
   */
  public void setFontRendererUseAliasing(final boolean alias)
  {
    setConfigProperty(FONTRENDERER_USEALIASING, String.valueOf(alias));
  }

  /**
   * Returns true, if the Graphics2D implementation is buggy and is not really able
   * to place/calculate the fontsizes correctly. Defaults to false. (SunJDK on Windows
   * is detected and corrected, Linux SunJDK 1.3 is buggy, but not detectable).
   *
   * @return true, if the Graphics2D implementation does not calculate the string
   * positions correctly and an alternative implementation should be used.
   */
  public boolean isFontRendererBuggy()
  {
    return getConfigProperty(FONTRENDERER_ISBUGGY_FRC,
        FONTRENDERER_ISBUGGY_FRC_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * set to true, if the Graphics2D implementation is buggy and is not really able
   * to place/calculate the fontsizes correctly. Defaults to false. (SunJDK on Windows
   * is detected and corrected, Linux SunJDK 1.3 is buggy, but not detectable).
   *
   * @param buggy set to true, if the Graphics2D implementation does not calculate the string
   * positions correctly and an alternative implementation should be used.
   */
  public void setFontRendererBuggy(final boolean buggy)
  {
    setConfigProperty(FONTRENDERER_ISBUGGY_FRC, String.valueOf(buggy));
  }

  /**
   * Returns all defined configuration properties for the report. The enumeration
   * contains all keys of the changed properties, properties set from files or
   * the system properties are not included.
   *
   * @return all defined configuration properties for the report.
   */
  public Enumeration getConfigProperties()
  {
    return configuration.keys();
  }

  /**
   * Checks, whether a stricter error handling is applied to the report processing.
   * If set to true, then errors in the handling of report events will cause the report
   * processing to fail. This is a safe-and-paranoid setting, life is simpler with
   * this set to false. The property defaults to false, as this is the old behaviour.
   * <p>
   * A properly defined report should not throw exceptions, so it is safe to set this
   * to true.
   *
   * @return true, if the strict handling is enabled, false otherwise.
   */
  public boolean isStrictErrorHandling()
  {
    return getConfigProperty(STRICT_ERRORHANDLING,
        STRICT_ERRORHANDLING_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * Defines, whether a stricter error handling is applied to the report processing.
   * If set to true, then errors in the handling of report events will cause the report
   * processing to fail. This is a safe-and-paranoid setting, life is simpler with
   * this set to false. The property defaults to false, as this is the old behaviour.
   * <p>
   * A properly defined report should not throw exceptions, so it is safe to set this
   * to true.
   *
   * @param strictErrorHandling if set to true, then errors in the event dispatching will
   * cause the reporting to fail.
   */
  public void setStrictErrorHandling(final boolean strictErrorHandling)
  {
    setConfigProperty(STRICT_ERRORHANDLING, String.valueOf(strictErrorHandling));
  }

  /**
   * Helper method for serialization.
   *
   * @param out the output stream where to write the object.
   * @throws IOException if errors occur while writing the stream.
   */
  private void writeObject(final ObjectOutputStream out)
      throws IOException
  {
    out.defaultWriteObject();
    if (parentConfiguration == globalConfig)
    {
      out.writeBoolean(false);
    }
    else
    {
      out.writeBoolean(true);
      out.writeObject(parentConfiguration);
    }
  }

  /**
   * Helper method for serialization.
   *
   * @param in the input stream from where to read the serialized object.
   * @throws IOException when reading the stream fails.
   * @throws ClassNotFoundException if a class definition for a serialized object
   * could not be found.
   */
  private void readObject(final ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    final boolean readParent = in.readBoolean();
    if (readParent)
    {
      parentConfiguration = (ReportConfiguration) in.readObject();
    }
    else
    {
      parentConfiguration = ReportConfiguration.getGlobalConfig();
    }
  }

  /**
   * Searches all property keys that start with a given prefix.
   *
   * @param prefix the prefix that all selected property keys should share
   * @return the properties as iterator.
   */
  public Iterator findPropertyKeys(final String prefix)
  {
    final TreeSet keys = new TreeSet();
    collectPropertyKeys(prefix, this, keys);
    return Collections.unmodifiableSet(keys).iterator();
  }

  /**
   * Collects property keys from this and all parent report configurations, which
   * start with the given prefix.
   *
   * @param prefix the prefix, that selects the property keys.
   * @param config the currently processed report configuration.
   * @param collector the target list, that should receive all valid keys.
   */
  private void collectPropertyKeys(final String prefix, final ReportConfiguration config,
                                   final TreeSet collector)
  {
    final Enumeration enum = config.getConfigProperties();
    while (enum.hasMoreElements())
    {
      final String key = (String) enum.nextElement();
      if (key.startsWith(prefix))
      {
        if (collector.contains(key) == false)
        {
          collector.add(key);
        }
      }
    }

    if (config.parentConfiguration != null)
    {
      collectPropertyKeys(prefix, config.parentConfiguration, collector);
    }
  }
}

/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: ReportConfiguration.java,v 1.32 2003/02/05 17:56:03 taqua Exp $
 *
 * Changes
 * -------
 * 06-Nov-2002 : Initial release
 * 12-Nov-2002 : Added Javadoc comments (DG);
 * 29-Nov-2002 : Fixed bugs reported by CheckStyle (DG)
 * 05-Dec-2002 : Documentation
 *
 */

package com.jrefinery.report.util;

import com.lowagie.text.pdf.BaseFont;

import java.util.Enumeration;
import java.util.Properties;

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
 * You can also specify the local configuration for a report via the XML report template file.
 *
 * <h3>Global Configuration keys</h3>
 * <p>
 * These keys affect the whole VM and usually cannot be changed once they are configured unless
 * the VM is restarted.
 * <p>
 * <ul>
 *
 * <li><code>com.jrefinery.report.LogLevel</code>
 * <p>
 * The minimum loglevel that is logged.  Valid values are:
 * <ul>
 *   <li><code>"Error"</code> - prints error messages only;</li>
 *   <li><code>"Warn"</code> - prints warnings and errors;</li>
 *   <li><code>"Info"</code> - prints informational messages as well as warnings and errors;</li>
 *   <li><code>"Debug"</code> - prints all messages, including all debugging messages.</li>
 * </ul>
 * The default setting is <code>"Debug"</code>.
 * <p>
 * </li>
 * <li><code>com.jrefinery.report.LogTarget</code>
 * <p>
 * The default log target. This should be set to the classname of a  valid {@link LogTarget}
 * implementation -- the given class is loaded and instantiated and added as the primary logging
 * target. This defaults to <code>"com.jrefinery.report.util.SystemOutLogTarget"</code>.
 * <p>
 * An alternative {@link LogTarget} for Log4J output can be found in the "Extension" package.
 * <p>
 *
 * <li><code>com.jrefinery.report.targets.PDFOutputTarget.AUTOINIT</code>
 * <p>AutoInit the PDFTarget when the class is loaded? This will search and register
 * all ttf-fonts on the system. The search will access the system-directorys and can
 * therefore collide with the SecurityManager. The property defaults to "true", set
 * to "false" to disable this feature and have a look at the PDFOutputTarget on how
 * to register the fonts manually.
 * <p>
 * <li><code>com.jrefinery.report.dtd</code>
 * <p>A URL to the JFreeReport-DTD if not specified in the Document itself. This can
 * be used for validating parsers to check the xml-document. This property is not set
 * by default, but the DTD can be found at "http://jfreereport.sourceforge.net/report.dtd".
 * This property expects a string with a valid URL, like the one above.
 * <p>
 * <li><code>com.jrefinery.report.PrintOperationComment</code>
 * <p>
 * Should the logical page add comments to the generated PhysicalOperations? This is
 * for debugging only and will cause heavy load on your LogTarget if enabled.
 * This option defaults to "false".
 *
 * <li><code>com.jrefinery.report.WarnInvalidColumns</code>
 * <p>Should the datarow print warning on invalid columns? Invalid column requests always
 * return null, if set to "true", an additional logging entry is added for every invalid
 * column. This is useful when writing report-definitions, so this option defaults to "true".
 *
 * <li><code>com.jrefinery.report.TableFactoryMode</code>
 * <p>
 * The table model factory mode for the {@link com.jrefinery.report.tablemodel.ResultSetTableModelFactory}.
 * If set to "simple" the factory will always return a <code>DefaultTableModel</code>.
 * This property is not set by default and it should not be necessary to change this.
 *
 * <li><code>com.jrefinery.report.NoDefaultDebug</code>
 * <p>
 * Should the debugging system be disabled by default. This option will suppress all
 * output, no single line of debug information will be printed. If you want to remove
 * System.out-debugging on the server side, try to switch to a Log4J-LogTarget instead.
 * This option can be dangerous, as you won't see any error messages, so it is set to "false"
 * by default.
 * <p>
 * </ul>
 * <h3>Local configuration keys</h3>
 * <p>The following keys can be redefined for all report-instances.
 * <ul>
 * <li><code>com.jrefinery.report.preview.PreferredWidth</code>
 * <p>
 * Defines a preferred size for the preview frame. Both width and height must be set,
 * proportional values are allowed (100%, 90% etc). They have the same syntax as the
 * proportional values in the xml definition.
 * <p>
 * None of the values is defined by default.
 * <li><code>com.jrefinery.report.preview.PreferredHeight</code>
 * <p>
 * Defines a preferred size for the preview frame. Both width and height must be set,
 * proportional values are allowed (100%, 90% etc). They have the same syntax as the
 * proportional values in the xml definition.
 * <p>
 * None of the values is defined by default.
 * <li><code>com.jrefinery.report.preview.MaximumWidth</code>
 * <p>
 * Defines the maximum width for the preview frame. If the width is defined, a component
 * listener will be used to enforce the defined width.
 * The value is not defined by default.
 * <li><code>com.jrefinery.report.preview.MaximumHeight</code>
 * Defines the maximum height for the preview frame. If the height is defined, a component
 * listener will be used to enforce that value.
 * The value is not defined by default.
 * </ul>
 * <p>
 * <h4>PDFOutputTarget properties</h4>
 * The PDFTarget can be configured using these properties. This will create a default configuration
 * which can be altered by the programm using the usual operations.
 * <ul>
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.Encoding
 * <p>The default font-encoding for the OutputTarget. A PDF does not use UNICODE by default.
 * Inside a PDF strings are stored with a specifiy byte-encoding, also known as CodePage
 * for 8Bit encodings. The PDFOutputTarget uses the Java-VM to convert Java-Strings into
 * encoded byte-sequences when printing text. The conversion is done via {@link String#getBytes}.
 * <p>
 * To print non-ascii characters, the string must be encoded with the correct encoding into
 * a byte-sequence. Additionally the font, which should display the string on the target
 * plattform has to support the given encoding, or the result will not be as expected.
 * <p>
 * To encode Strings which contain the 'Euro' symbol, use the encoding "iso-8859-15", for
 * polish Strings, the encoding "iso-8859-2" is suitable. If you want to encode chinese
 * characters, try "GB2312". A complete list of encodings can be found
 * at <a href="http://www.iana.org/assignments/character-sets"
 * >http://www.iana.org/assignments/character-sets</a>
 * For a complete set of supported encodings, you will also need the international version
 * of the JDK (the bigger one :)).
 * <p>
 * As thumb rule, try to write some of your target text in your favourite text processor.
 * If the font you select there is a true-type-font and does display the text well, the font
 * will be suitable for your report.
 * <p>
 * The default value can be changed with
 * {@link com.jrefinery.report.targets.pageable.output.PDFOutputTarget#setFontEncoding}
 * for a specific instance if needed.
 * <p>
 * If not defined otherwise, this property defaults to "Cp1252", the standard Windows-encoding.
 * <p>
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.Author
 * <p>Defines the Author of the PDF-Document. The author is written into the PDFMetaData.
 * If not explicitly set, this property is ignored and no author is set.
 * <p>
 * </ul>
 * <h3>A word to the PDF-Security flags</h3>
 * PDFSecurity is only enabled, if the document is encrypted. Encryption itself will
 * not protect the document from being modified, if no userpassword and/or ownerpassword
 * is set for the document.
 * <p>
 * The <code>userpassword</code> will protect access to the document. If empty, everybody
 * is able to read the document. The <code>ownerpassword</code> will protect access to the
 * security settings of the document. If empty, everybody who is able to read the document,
 * will also be able to change the security settings of the document (and so the document
 * itself).
 * If only the ownerpassword is set, everybody may read the document, but only the owner
 * is able to fully access the documents security settings.
 * <p>
 * <ul>PDFSecurity settings
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.AllowPrinting
 * <p>Should the PDF Security setting allow printing of the document by default.
 * This property defaults to "false", no one may print the document.
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.AllowCopy
 * <p>Should the PDF Security setting allow copying of the document's contents by default
 * This property defaults to "false".
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.AllowModifyContents
 * <p>Should the PDF Security setting allow modifying of the document by default.
 * This property defaults to "false".
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.AllowModifyAnnotations
 * <p>Should the PDF Security setting allow document annotations by default
 * This property defaults to "false".
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.AllowFillIn
 * <p>Should the PDF Security setting allow the fill-in of document forms (inputfields etc).
 * This property defaults to "false".
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.AllowScreenReaders
 * <p>Should the PDF Security setting allow access for screenreaders by default.
 * ScreenReader may help blind people to read your document, but I may also open a gate for
 * automated replication of your documents contents. As today every OCR program is also able to
 * capture the contents of PDF-Files, it is safe to say "true" here to allow access for
 * screenreaders.
 * But as we are paranoid, this property defaults to "false" if not defined otherwise.
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.AllowAssembly
 * Should the PDF Security setting allow re(assembly) of the document by default
 * This property defaults to "false".
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.AllowDegradedPrinting
 * <p>Should the PDF Security setting allow low quality printing of the document's contents by
 * default
 * If you disabled printing, this property defines whether the reader of your document may print
 * a low-quality (draft) version of your document. If you enabled printing, the users right
 * to print a high-quality version also includes the right to print the low-level version of the
 * document. This setting defaults to "false".
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.Encryption
 * <p>
 * Should the PDF file be encrypted by default? Set to "none", "40bit", "128bit". In some
 * countries the 128bit encoding may be illegal for use or export. If this property is set
 * to "none", none of the other security properties are applied. Enabling security always
 * involves encryption.
 * <P>
 * The 40bit encoding should not be used for securing sensitive data,
 * a 40 bit key does not create any real barriers for interessed evil people out there.
 * This property is not set by default, and is therefore treated as if it was set to "none".
 * <p>
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.userpassword
 * <b>Warning:</b>Specifying passwords in the property file can introduce a security risk.
 * Do not use these properties on the client side or in unprotected (world-readable) server
 * environments. In these cases use other means of defining the password. Any unsecured
 * access to the "jfreereport.properties" file would cause a security leak, if you store
 * clear-text passwords there.
 * <p>
 * The userpassword protects global access to your document. This property is not set by default.
 * This property should be set to a non-null value to enable the security of the document.
 * <p>
 * <li>com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.ownerpassword
 * <p>
 * The ownerpassword protects access to the security settings of the document. A user with
 * owner-access has all permissions to modify the contents and settings of the document.
 * This property is not set by default.
 * This property should be set to a non-null value to enable the security of the document.
 * </ul>
 *
 * @author Thomas Morgner
 */
public class ReportConfiguration
{
  /** The text aliasing configuration key */
  public static final String G2TARGET_USEALIASING
                             = "com.jrefinery.report.targets.G2OutputTarget.useAliasing";

  /** The text aliasing configuration default value. Is "false" */
  public static final String G2TARGET_USEALIASING_DEFAULT = "false";

  /** The G2 fontrenderer bug override configuration key */
  public static final String G2TARGET_ISBUGGY_FRC
                             = "com.jrefinery.report.targets.G2OutputTarget.isBuggyFRC";

  /** The G2 fontrenderer bug override. Is "false" */
  public static final String G2TARGET_ISBUGGY_FRC_DEFAULT = "false";

  /** The preferred width key. */
  public static final String PREVIEW_PREFERRED_WIDTH
                             = "com.jrefinery.report.preview.PreferredWidth";

  /** The preferred height key. */
  public static final String PREVIEW_PREFERRED_HEIGHT
                             = "com.jrefinery.report.preview.PreferredHeight";

  /** The maximum width key. */
  public static final String PREVIEW_MAXIMUM_WIDTH = "com.jrefinery.report.preview.MaximumWidth";

  /** The maximum height key. */
  public static final String PREVIEW_MAXIMUM_HEIGHT = "com.jrefinery.report.preview.MaximumHeight";

  /** The 'disable logging' property key. */
  public static final String PRINT_OPERATION_COMMENT = "com.jrefinery.report.PrintOperationComment";

  /** The default 'disable logging' property value. */
  public static final String PRINT_OPERATION_COMMENT_DEFAULT = "false";

  /** The 'disable logging' property key. */
  public static final String WARN_INVALID_COLUMNS = "com.jrefinery.report.WarnInvalidColumns";

  /** The default 'disable logging' property value. */
  public static final String WARN_INVALID_COLUMNS_DEFAULT = "false";

  /** The 'disable logging' property key. */
  public static final String DISABLE_LOGGING = "com.jrefinery.report.NoDefaultDebug";

  /** The default 'disable logging' property value. */
  public static final String DISABLE_LOGGING_DEFAULT = "false";

  /** The 'log level' property key. */
  public static final String LOGLEVEL = "com.jrefinery.report.LogLevel";

  /** The default 'log level' property value. */
  public static final String LOGLEVEL_DEFAULT = "Info";

  /** The 'log target' property key. */
  public static final String LOGTARGET = "com.jrefinery.report.LogTarget";

  /** The default 'log target' property value. */
  public static final String LOGTARGET_DEFAULT = SystemOutLogTarget.class.getName();

  /** The 'PDF auto init' property key. */
  public static final String PDFTARGET_AUTOINIT
      = "com.jrefinery.report.targets.pageable.output.PDFOutputTarget.AutoInit";

  /** The default 'PDF auto init' property value. */
  public static final String PDFTARGET_AUTOINIT_DEFAULT = "true";

  /** The 'PDF embed fonts' property key. */
  public static final String PDFTARGET_EMBED_FONTS
      = "com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.EmbedFonts";

  /** The default 'PDF embed fonts' property value. */
  public static final String PDFTARGET_EMBED_FONTS_DEFAULT = "true";

  /** The 'PDF encoding' property key. */
  public static final String PDFTARGET_ENCODING
      = "com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.Encoding";

  /** The default 'PDF encoding' property value. */
  public static final String PDFTARGET_ENCODING_DEFAULT = BaseFont.WINANSI;

  /** The 'ResultSet factory mode'. */
  public static final String RESULTSET_FACTORY_MODE = "com.jrefinery.report.TableFactoryMode";

  /** Enable DTD validation of the parsed XML? */
  public static final String PARSER_VALIDATE = "com.jrefinery.report.io.validate";

  /** disable DTD validation by default */
  public static final String PARSER_VALIDATE_DEFAULT = "true";

  /**
   * The default resourcebundle that should be used for ResourceFileFilter.
   * This property must be applied by the parser.
   */
  public static final String REPORT_RESOURCE_BUNDLE = "com.jrefinery.report.ResourceBundle";

  /** Enable stricter table layouting for all TableProcessors? */
  public static final String STRICT_TABLE_LAYOUT = "com.jrefinery.report.targets.table.StrictLayout";

  /** Disable strict layout by default */ 
  public static final String STRICT_TABLE_LAYOUT_DEFAULT = "false";

  /** Storage for the configuration properties. */
  private Properties configuration;

  /** The parent configuration (null if this is the root configuration). */
  private ReportConfiguration parentConfiguration;

  /** The global configuration. */
  private static ReportConfiguration globalConfig;

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
   * @param globalConfig  the global configuration.
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
   * <p>
   * If the property is not defined in this configuration, the code will lookup the property in
   * the parent configuration.
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
   * Sets the log level, which is read from the global report configuration at the point that
   * the classloader loads the {@link Log} class.
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
   * <li>changing the log level after the {@link Log} class has been loaded will have no effect.</li>
   * <li>to turn of logging altogether, use the {@link #setDisableLogging} method.</li>
   * </ul>
   *
   * @param level  the new log level.
   */
  public void setLogLevel(String level)
  {
    setConfigProperty(LOGLEVEL, level);
  }

  /**
   * Returns whether to search for ttf-fonts when the PDFOutputTarget is loaded..
   *
   * @return the PDFOutputTarget autoinitialisation value.
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
   * Returns <code>true</code> if logging is disabled, and <code>false</code> otherwise.
   *
   * @return boolean.
   */
  public boolean isDisableLogging()
  {
    return getConfigProperty(DISABLE_LOGGING, DISABLE_LOGGING_DEFAULT).equalsIgnoreCase("true");
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
   * Returns the global configuration for JFreeReport.
   * <p>
   * In the current implementation, the configuration has no properties defined, but references
   * a parent configuration that:
   * <ul>
   * <li>copies across all the <code>System</code> properties to use as report configuration
   *     properties (obviously the majority of them will not apply to reports);</li>
   * <li>itself references a parent configuration that reads its properties from a file
   *     <code>jfreereport.properties</code>.
   * </ul>
   * This three-level set up is probably not necessary - it looks like it has arisen from
   * evolving code.
   *
   * @return the global configuration.
   */
  public static ReportConfiguration getGlobalConfig ()
  {
    if (globalConfig == null)
    {
      PropertyFileReportConfiguration rootProperty = new PropertyFileReportConfiguration();
      rootProperty.load("/com/jrefinery/report/jfreereport.properties");

      PropertyFileReportConfiguration baseProperty = new PropertyFileReportConfiguration();
      baseProperty.load("/jfreereport.properties");
      baseProperty.setParentConfig(rootProperty);

      SystemPropertyConfiguration systemConfig = new SystemPropertyConfiguration();
      systemConfig.setParentConfig(baseProperty);

      globalConfig = new ReportConfiguration ();
      globalConfig.setParentConfig(systemConfig);

    }
    return globalConfig;
  }

  /**
   * Set the parent configuration. The parent configuration is queried, if the
   * requested configuration values was not found in this report configuration.
   *
   * @param config
   */
  protected void setParentConfig (ReportConfiguration config)
  {
    parentConfiguration = config;
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

  /**
   * Returns true, if invalid columns in the dataRow are logged as warning.
   *
   * @return true, if invalid columns in the dataRow are logged as warning.
   */
  public boolean isWarnInvalidColumns()
  {
    return getConfigProperty(WARN_INVALID_COLUMNS ,
        WARN_INVALID_COLUMNS_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * Set to true, if you want to get informed when invalid column-requests are
   * made to the DataRow.
   *
   * @param warnInvalidColumns the warning flag
   */
  public void setWarnInvalidColumns(boolean warnInvalidColumns)
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
  public void setPrintOperationComment(boolean print)
  {
    setConfigProperty(PRINT_OPERATION_COMMENT, String.valueOf(print));
  }

  /**
   * Returns true, if the Graphics2D should use aliasing to render text. Defaults to false.
   *
   * @return true, if aliasing is enabled.
   */
  public boolean isG2TargetUseAliasing()
  {
    return getConfigProperty(G2TARGET_USEALIASING,
        G2TARGET_USEALIASING_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * set to true, if the Graphics2D should use aliasing to render text. Defaults to false.
   *
   * @param alias set to true, if the Graphics2D should use aliasing.
   */
  public void setG2TargetUseAliasing(boolean alias)
  {
    setConfigProperty(G2TARGET_USEALIASING, String.valueOf(alias));
  }

  /**
   * Returns true, if the Graphics2D should use aliasing to render text. Defaults to false.
   *
   * @return true, if aliasing is enabled.
   */
  public boolean isPDFTargetEmbedFonts()
  {
    return getConfigProperty(PDFTARGET_EMBED_FONTS,
        PDFTARGET_EMBED_FONTS_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * set to true, if the PDFOutputTarget should embed all fonts.
   *
   * @param embed set to true, if the PDFOutputTarget should use embedded fonts.
   */
  public void setPDFTargetEmbedFonts(boolean embed)
  {
    setConfigProperty(PDFTARGET_EMBED_FONTS, String.valueOf(embed));
  }

  /**
   * Returns true, if the Graphics2D implementation is buggy and is not really able
   * to place/calculate the fontsizes correctly. Defaults to false. (SunJDK on Windows
   * is detected and corrected, Linux SunJDK 1.3 is buggy, but not detectable).
   *
   * @return true, if the Graphics2D implementation does not calculate the string
   * positions correctly and an alternative implementation should be used.
   */
  public boolean isG2BuggyFRC()
  {
    return getConfigProperty(G2TARGET_ISBUGGY_FRC,
        G2TARGET_ISBUGGY_FRC_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * set to true, if the Graphics2D implementation is buggy and is not really able
   * to place/calculate the fontsizes correctly. Defaults to false. (SunJDK on Windows
   * is detected and corrected, Linux SunJDK 1.3 is buggy, but not detectable).
   *
   * @param buggy set to true, if the Graphics2D implementation does not calculate the string
   * positions correctly and an alternative implementation should be used.
   */
  public void setG2BuggyFRC(boolean buggy)
  {
    setConfigProperty(G2TARGET_ISBUGGY_FRC, String.valueOf(buggy));
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
    return configuration.keys();
  }

  /**
   * Set to false, to globaly disable the xml-validation.
   *
   * @param validate true, if the parser should validate the xml files.
   */
  public void setValidateXML (boolean validate)
  {
    setConfigProperty(PARSER_VALIDATE, String.valueOf(validate));
  }

  /**
   * returns true, if the parser should validate the xml files against the DTD
   * supplied with JFreeReport.
   *
   * @return true, if the parser should validate, false otherwise.
   */
  public boolean isValidateXML ()
  {
    return getConfigProperty(PARSER_VALIDATE, PARSER_VALIDATE_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * returns true, if the TableWriter should perform a stricter layout translation.
   * When set to true, all element bounds are used to create the table. This could result
   * in a complex layout, more suitable for printing. If set to false, only the starting
   * bounds (the left and the upper border) are used to create the layout. This will result
   * is lesser cells and rows, the layout will be better suitable for later processing.
   *
   * @return true, if strict layouting rules should be applied, false otherwise.
   */
  public boolean isStrictTableLayout()
  {
    return getConfigProperty(STRICT_TABLE_LAYOUT, STRICT_TABLE_LAYOUT_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * Defines whether strict layouting rules should be used for the TableLayouter.
   *
   * @see ReportConfiguration#isStrictTableLayout
   * @param strict set to true, to use strict layouting rules, false otherwise.
   */
  public void setStrictTableLayout (boolean strict)
  {
    setConfigProperty(STRICT_TABLE_LAYOUT, String.valueOf(strict));
  }
}

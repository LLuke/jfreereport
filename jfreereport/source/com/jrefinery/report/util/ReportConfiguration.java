/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportConfiguration.java,v 1.20 2002/12/13 14:49:20 mungady Exp $
 *
 * Changes
 * -------
 * 06-Nov-2002 : Initial release
 * 12-Nov-2002 : Added Javadoc comments (DG);
 *
 */

package com.jrefinery.report.util;

import com.lowagie.text.pdf.BaseFont;

import java.io.IOException;
import java.io.InputStream;
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
 * These keys affect the whole VM and cannot be changed once
 * they are configured unless the VM is restarted.
 * <p>
 * <ul>
 * <li><code>com.jrefinery.report.LogLevel</code>
 * <p>The minimum loglevel that is logged. Defaults to "Debug",
 * possible values are "Error", "Warn", "Info", "Debug", "None".
 * "None" disables the logging. Log-level "Error" prints error-messages
 * only. "Warn" prints warnings and errors. "Info" prints informational
 * messages as well as warnings and errors. "Debug" prints all messages,
 * inclusive all debugging messages.
 * <p>
 * <li><code>com.jrefinery.report.LogTarget</code>
 * <p>The default log-target. Give a classname of a  valid LogTarget implementation.
 * The given class is loaded and instantiated and added as primary loggin target.
 * This defaults to "com.jrefinery.report.util.SystemOutLogTarget". An alternative
 * LogTarget for Log4J-Printing can be found in the "Extension" package.
 * <p>
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
 * <p>The tablemodel factory mode for the ResultSetTableModelFactory.
 * if set to "simple" the factory will always return a DefaultTableModel. This property is
 * not set by default and it should not be necessary to change this.
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

  /** A report configuration that reads its values from the jfreereport.properties file. */
  private static class PropertyFileReportConfiguration extends ReportConfiguration
  {
    /**
     * Creates a new report properties configuration.
     */
    public PropertyFileReportConfiguration()
    {
      this.getConfiguration().put (DISABLE_LOGGING, DISABLE_LOGGING_DEFAULT);
      this.getConfiguration().put (LOGLEVEL, LOGLEVEL_DEFAULT);
      this.getConfiguration().put (PDFTARGET_AUTOINIT, PDFTARGET_AUTOINIT_DEFAULT);
      this.getConfiguration().put (PDFTARGET_ENCODING, PDFTARGET_ENCODING_DEFAULT);

      InputStream in = this.getClass().getResourceAsStream("/jfreereport.properties");
      if (in == null)
      {
        in = this.getClass().getResourceAsStream("/com/jrefinery/report/jfreereport.properties");
      }
      if (in != null)
      {
        try
        {
          this.getConfiguration().load(in);
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
      this.getConfiguration().putAll (System.getProperties());
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
    /*
    if (value == null)
    {
      Log.debug ("Unable to handle Property : " + key);
    }
    */
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
}

/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * -------------------
 * HtmlProcessor.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlProcessor.java,v 1.19 2006/02/08 18:03:35 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.meta.MetaBandProducer;
import org.jfree.report.modules.output.table.base.LayoutCreator;
import org.jfree.report.modules.output.table.base.TableCreator;
import org.jfree.report.modules.output.table.base.TableProcessor;

/**
 * The HtmlProcessor handles the initialisation of the report writer and starts and
 * manages the report process.
 * <p/>
 * The Html content is not written directly into an OutputStream. As Html-Files are able
 * to use external references to include images and style information, the output target
 * is provided using an abstract HtmlFilesystem.
 * <p/>
 * Depending on the implementation, the output is written into a directory, a ZipFile or a
 * single Html-stream.
 * <p/>
 * This output target supports the generation of XHTML or HTML4 output. All recent
 * browsers should be able to handle XHTML output. XHTML is standard XML code, so that any
 * XML parser is able to read and parse the generated output.
 * <p/>
 * If not specified otherwise, this Processor uses the System Encoding as output
 * enconding, or UTF-8 if no system encoding is defined. When generating XHTML output,
 * make sure that your parser supports your selected encoding, all JAXP compatible parsers
 * must support at least UTF-8 and US-ASCII encoding.
 *
 * @author Thomas Morgner
 * @see HtmlFilesystem
 */
public class HtmlProcessor extends TableProcessor
{
  /**
   * the filesystem implementation used for writing the generated content.
   */
  private HtmlFilesystem filesystem;

  /**
   * The configuration prefix when reading the configuration settings from the report
   * configuration.
   */
  public static final String CONFIGURATION_PREFIX =
          "org.jfree.report.modules.output.table.html";

  /**
   * The configuration key that defines whether to generate XHTML code.
   */
  public static final String GENERATE_XHTML = "GenerateXHTML";

  /**
   * the fileencoding for the main html file.
   */
  public static final String ENCODING = "Encoding";
  /**
   * a default value for the fileencoding of the main html file.
   */
  public static final String ENCODING_DEFAULT = "UTF-8";

  /**
   * The property key to define whether to build a html body fragment.
   */
  public static final String BODY_FRAGMENT = "BodyFragment";

  public static final String EMPTY_CELLS_USE_CSS = "EmptyCellsUseCSS";

  public static final String TABLE_ROW_BORDER_DEFINITION = "TableRowBorderDefinition";

  public static final String USE_DEVICE_INDEPENDENT_IMAGESIZES = "UseDeviceIndependentImageSize";
  public static final String PROPORTIONAL_COLUMN_WIDTHS = "ProportionalColumnWidths";

  /**
   * Creates a new HtmlProcessor, which generates HTML4 output and uses the standard file
   * encoding.
   *
   * @param report the report that should be processed.
   * @throws ReportProcessingException if the report initialization failed
   */
  public HtmlProcessor (final JFreeReport report)
          throws ReportProcessingException
  {
    super(report);
    init();
  }

  /**
   * Creates a new HtmlProcessor, which uses the standard file encoding.
   *
   * @param report   the report that should be processed.
   * @param useXHTML true, if XML output should be generated, false for HTML4 compatible
   *                 output.
   * @throws ReportProcessingException if the report initialization failed
   */
  public HtmlProcessor (final JFreeReport report, final boolean useXHTML)
          throws ReportProcessingException
  {
    super(report);
    setGenerateXHTML(useXHTML);
    init();
  }

  /**
   * Gets the XHTML flag.
   *
   * @return true, if XHTML output is generated, false otherwise.
   */
  public boolean isGenerateXHTML ()
  {
    return getReport().getReportConfiguration().getConfigProperty
            (getReportConfigurationPrefix() + "." + GENERATE_XHTML, "false").equals("true");
  }

  /**
   * Defines the XHTML flag. Set to true, to generate XHTML output, false for HTML4
   * compatible code.
   *
   * @param useXHTML the XHTML flag.
   */
  public void setGenerateXHTML (final boolean useXHTML)
  {
    getReport().getReportConfiguration().setConfigProperty
            (getReportConfigurationPrefix() + "." + GENERATE_XHTML, String.valueOf(useXHTML));
  }


  /**
   * Gets the 'generateBodyFragment' flag.
   *
   * @return true, if a HTML-body fragment should be generated, false for a complete document.
   */
  public boolean isGenerateBodyFragment ()
  {
    return getReport().getReportConfiguration().getConfigProperty
            (getReportConfigurationPrefix() + "." + BODY_FRAGMENT, "false").equals("true");
  }

  /**
   * Defines the generate Body Fragment flag. If enabled, then all style information
   * will be inlined into the document and no header or HTML-Body tag will be generated.
   *
   * @param generateBodyFragment the generate BodyFragment flag.
   */
  public void setGenerateBodyFragment (final boolean generateBodyFragment)
  {
    getReport().getReportConfiguration().setConfigProperty
            (getReportConfigurationPrefix() + "." + BODY_FRAGMENT, String.valueOf(generateBodyFragment));
  }

  /**
   * Gets the 'empty-cells-use-css' flag.
   *
   * @return true, if the target browser is CSS2 compatible, false otherwise.
   */
  public boolean isEmptyCellsUseCSS()
  {
    return getReport().getReportConfiguration().getConfigProperty
            (getReportConfigurationPrefix() + "." + EMPTY_CELLS_USE_CSS, "false").equals("true");
  }

  /**
   * Defines, whether CSS rules should be used to force the display of
   * empty cells. Otherwise empty cells will be filled with a no-break-space entity
   * to force the display.
   * <p>
   * As usual, the InternetExplorer is not able to handle standards and using
   * 'true' in such environments causes troubles.
   *
   * @param emptyCellsUseCSS the 'empty-cells-use-css' flag.
   */
  public void setEmptyCellsUseCSS (final boolean emptyCellsUseCSS)
  {
    getReport().getReportConfiguration().setConfigProperty
            (getReportConfigurationPrefix() + "." + EMPTY_CELLS_USE_CSS, String.valueOf(emptyCellsUseCSS));
  }

  public boolean isUseDeviceIndependentImageSize ()
  {
    return getReport().getReportConfiguration().getConfigProperty
            (getReportConfigurationPrefix() + "." + USE_DEVICE_INDEPENDENT_IMAGESIZES, "false").equals("true");
  }

  public void setUseDeviceIndependentImageSize (final boolean useDeviceIndependentImageSize)
  {
    getReport().getReportConfiguration().setConfigProperty
            (getReportConfigurationPrefix() + "." + USE_DEVICE_INDEPENDENT_IMAGESIZES, String.valueOf(useDeviceIndependentImageSize));
  }

  public String getEncoding()
  {
    return getReport().getReportConfiguration().getConfigProperty
            (getReportConfigurationPrefix() + "." + ENCODING, ENCODING_DEFAULT);
  }

  public void setEncoding (final String encoding)
  {
    getReport().getReportConfiguration().setConfigProperty
            (getReportConfigurationPrefix() + "." + ENCODING, encoding);
  }

  /**
   * Gets the HTMLFilesystem, which should be used, or null, if no filesystem is defined
   * yet.
   *
   * @return the filesystem, which should be used to store the created content.
   */
  public HtmlFilesystem getFilesystem ()
  {
    return filesystem;
  }

  /**
   * Defines the HTMLFileSystem, that should be used to store the content.
   *
   * @param filesystem the filesystem for storing the generated content.
   */
  public void setFilesystem (final HtmlFilesystem filesystem)
  {
    this.filesystem = filesystem;
  }

  /**
   * Gets the report configuration prefix for that processor. This prefix defines how to
   * map the property names into the global report configuration.
   *
   * @return the report configuration prefix.
   */
  protected String getReportConfigurationPrefix ()
  {
    return CONFIGURATION_PREFIX;
  }

  /**
   * Creates the LayoutProcessor for the current report export. This default
   * implementation simply returns a LayoutCreator instance.
   *
   * @return a html layout creator
   */
  protected LayoutCreator createLayoutCreator ()
  {
    return new HtmlLayoutCreator(getReportConfigurationPrefix());
  }

  protected TableCreator createContentCreator ()
  {
    final HtmlLayoutCreator layoutCreator =
            (HtmlLayoutCreator) getLayoutCreator();
    return new HtmlContentCreator(getFilesystem(), isGenerateXHTML(),
            layoutCreator.getStyleCollection(), layoutCreator.getSheetLayoutCollection());
  }

  protected MetaBandProducer createMetaBandProducer ()
  {
    return new HtmlMetaBandProducer(isGenerateXHTML(),
            isUseDeviceIndependentImageSize(), isMaxLineHeightUsed());
  }

  protected String getExportDescription()
  {
    return "table/html";
  }
}

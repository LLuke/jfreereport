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
 * -------------------
 * HtmlProcessor.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlProcessor.java,v 1.8 2003/09/09 15:52:53 taqua Exp $
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
 * <p>
 * The Html content is not written directly into an OutputStream. As Html-Files are able
 * to use external references to include images and style information, the output
 * target is provided using an abstract HtmlFilesystem.
 * <p>
 * Depending on the implementation, the output is written into a directory, a ZipFile
 * or a single Html-stream.
 * <p>
 * This output target supports the generation of XHTML or HTML4 output. All recent
 * browsers should be able to handle XHTML output. XHTML is standard XML code, so that
 * any XML parser is able to read and parse the generated output.
 * <p>
 * If not specified otherwise, this Processor uses the System Encoding as output enconding,
 * or UTF-8 if no system encoding is defined. When generating XHTML output, make sure that
 * your parser supports your selected encoding, all JAXP compatible parsers must support
 * at least UTF-8 and US-ASCII encoding.
 *
 * @see HtmlFilesystem
 *
 * @author Thomas Morgner
 */
public class HtmlProcessor extends TableProcessor
{
  /** the filesystem implementation used for writing the generated content. */
  private HtmlFilesystem filesystem;
  
  /** 
   * The configuration prefix when reading the configuration settings 
   * from the report configuration.
   */
  public static final String CONFIGURATION_PREFIX = 
    "org.jfree.report.modules.output.table.html";

  /** The configuration key that defines whether to generate XHTML code. */
  public static final String GENERATE_XHTML = "GenerateXHTML";

  /** the fileencoding for the main html file. */
  public static final String ENCODING = "Encoding";
  /** a default value for the fileencoding of the main html file. */
  public static final String ENCODING_DEFAULT = "UTF-8";

  /** The property key to define whether to build a html body fragment. */
  public static final String BODY_FRAGMENT = "BodyFragment";


  /**
   * Creates a new HtmlProcessor, which generates HTML4 output and uses the
   * standard file encoding.
   *
   * @param report the report that should be processed.
   * @throws ReportProcessingException if the report initialization failed
   */
  public HtmlProcessor(final JFreeReport report)
      throws ReportProcessingException
  {
    super(report);
  }

  /**
   * Creates a new HtmlProcessor, which uses the standard file encoding.
   *
   * @param report  the report that should be processed.
   * @param useXHTML  true, if XML output should be generated, false for HTML4 compatible output.
   *
   * @throws ReportProcessingException if the report initialization failed
   */
  public HtmlProcessor(final JFreeReport report, final boolean useXHTML)
      throws ReportProcessingException
  {
    super(report);
    setGenerateXHTML(useXHTML);
  }

  /**
   * Gets the XHTML flag.
   *
   * @return true, if XHTML output is generated, false otherwise.
   */
  public boolean isGenerateXHTML()
  {
    return getReport().getReportConfiguration().getConfigProperty
            (getReportConfigurationPrefix() + "." + GENERATE_XHTML, "false").equals("true");
  }

  /**
   * Defines the XHTML flag. Set to true, to generate XHTML output, false for
   * HTML4 compatible code.
   *
   * @param useXHTML the XHTML flag.
   */
  public void setGenerateXHTML(final boolean useXHTML)
  {
    getReport().getReportConfiguration().setConfigProperty
            (getReportConfigurationPrefix() + "." + GENERATE_XHTML, String.valueOf(useXHTML));
  }

  /**
   * Gets the HTMLFilesystem, which should be used, or null, if no filesystem
   * is defined yet.
   *
   * @return the filesystem, which should be used to store the created content.
   */
  public HtmlFilesystem getFilesystem()
  {
    return filesystem;
  }

  /**
   * Defines the HTMLFileSystem, that should be used to store the content.
   *
   * @param filesystem the filesystem for storing the generated content.
   */
  public void setFilesystem(final HtmlFilesystem filesystem)
  {
    this.filesystem = filesystem;
  }

  /**
   * Gets the report configuration prefix for that processor. This prefix defines
   * how to map the property names into the global report configuration.
   *
   * @return the report configuration prefix.
   */
  protected String getReportConfigurationPrefix()
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
    return new HtmlTableCreator(getFilesystem(), isGenerateXHTML(),
            layoutCreator.getStyleCollection(), layoutCreator.getSheetLayoutCollection());
  }

  protected MetaBandProducer createMetaBandProducer ()
  {
    return new HtmlMetaBandProducer(isGenerateXHTML());
  }
}

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
 * ExcelProcessor.java
 * -------------------
 * (C)opyright 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann
 * Contributor(s):   Thomas Morgner;
 *                   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExcelProcessor.java,v 1.7 2003/09/09 15:52:53 taqua Exp $
 *
 * Changes
 * -------
 * 14-Jan-2003 : Initial version
 * 23-May-2003 : Enabled configurable POI-CellData format strings.
 */
package org.jfree.report.modules.output.table.xls;

import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.meta.MetaBandProducer;
import org.jfree.report.modules.output.table.base.TableCreator;
import org.jfree.report.modules.output.table.base.TableProcessor;
import org.jfree.report.style.StyleKey;

/**
 * A report processor that coordinates the output process for generating Excel files.
 * <P>
 * The Jakarta POI library is used to write files in Excel format. If the property
 * "EnhancedDataFormat" is set to true, this target uses the extended Cell-Data format
 * capabilities of POI 1.10 to format the cell value as numeric or date value. An
 * excel-specific format string can be defined for every element using the element-
 * style key "Excel.CellDataFormat".
 *
 * @author Heiko Evermann
 */
public class ExcelProcessor extends TableProcessor
{
  /** The property key to enable the enhanced data formats of POI 1.10. */
  public static final String ENHANCED_DATA_FORMAT_PROPERTY = "EnhancedDataFormat";

  /** The StyleKey for the user defined cell data format. */
  public static final StyleKey WRAP_TEXT =
      StyleKey.getStyleKey("Excel.WrapText", Boolean.class);

  /** The StyleKey for the user defined cell data format. */
  public static final StyleKey DATA_FORMAT_STRING =
      StyleKey.getStyleKey("Excel.CellDataFormat", String.class);

  /** The output stream that is used to write the excel file. */
  private OutputStream outputStream;

  /** 
   * The configuration prefix when reading the configuration settings 
   * from the report configuration.
   */
  public static final String CONFIGURATION_PREFIX =
      "org.jfree.report.modules.output.table.xls";

  /**
   * Creates a new ExcelProcessor for the given report.
   *
   * @param report the report that should be processed.
   * @throws ReportProcessingException if the report initialization failed
   */
  public ExcelProcessor(final JFreeReport report)
      throws ReportProcessingException
  {
    super(report);
  }

  /**
   * Gets the output stream, that should be used to write the generated content.
   *
   * @return the output stream.
   */
  public OutputStream getOutputStream()
  {
    return outputStream;
  }

  /**
   * Sets the output stream, that should be used to write the generated content.
   *
   * @param outputStream the output stream.
   */
  public void setOutputStream(final OutputStream outputStream)
  {
    this.outputStream = outputStream;
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

  protected TableCreator createContentCreator ()
  {
    // todo implement me
    return null;
  }

  protected MetaBandProducer createMetaBandProducer ()
  {
    return new ExcelMetaBandProducer(isDefineDataFormats());
  }

  /**
   * Defines whether to map java objects into excel extended cell formats. This feature
   * can be used to create numeric and date cells in the excel sheet, but the mapping may
   * contain errors.
   * <p/>
   * We try to directly map the java.text.SimpleDateFormat and java.text.DecimalFormat
   * into their excel counter parts and hope that everything works fine. If not, you will
   * have to adjust the format afterwards.
   *
   * @return true if cells should contain a custom data format for numeric or date cells
   *         or false when all cells should contain strings.
   */
  public boolean isDefineDataFormats ()
  {
    return getReport().getReportConfiguration().getConfigProperty
            (CONFIGURATION_PREFIX + "." + ENHANCED_DATA_FORMAT_PROPERTY,
                    "true").equals("true");
  }

  /**
   * Defines whether to map java objects into excel extended cell formats. This feature
   * can be used to create numeric and date cells in the excel sheet, but the mapping may
   * contain errors.
   * <p/>
   * We try to directly map the java.text.SimpleDateFormat and java.text.DecimalFormat
   * into their excel counter parts and hope that everything works fine. If not, you will
   * have to adjust the format afterwards.
   *
   * @param defineDataFormats set to true if cells should contain a custom data format for
   *                          numeric or date cells or false when all cells should contain
   *                          strings.
   */
  public void setDefineDataFormats (final boolean defineDataFormats)
  {
    getReport().getReportConfiguration().setConfigProperty
            (CONFIGURATION_PREFIX + "." + ENHANCED_DATA_FORMAT_PROPERTY,
                    String.valueOf(defineDataFormats));
  }


}

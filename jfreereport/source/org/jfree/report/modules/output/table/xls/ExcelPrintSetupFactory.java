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
 * ------------------
 * ExcelProducer.java
 * ------------------
 * (C)opyright 2003, Thomas Morgner and Contributers
 *
 * Original Author:  Thomas Morgner
 * Contributor(s):   -;
 *
 * $Id: ExcelPrintSetupFactory.java,v 1.3 2003/08/25 14:29:32 taqua Exp $
 *
 * Changes
 * -------
 * 19-Aug-2003 : initial version
 */
package org.jfree.report.modules.output.table.xls;

import java.awt.print.PageFormat;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.jfree.report.util.PageFormatFactory;

/**
 * Performs all steps to setup the printer page of an excel sheet.
 * <p>
 * This list is based on the specifications found in the OpenOffice documentation.
 * <a href="http://sc.openoffice.org/excelfileformat.pdf">
 * http://sc.openoffice.org/excelfileformat.pdf</a>.
 *
 * @author user
 */
public final class ExcelPrintSetupFactory
{
  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition LETTER = new ExcelPageDefinition
      ((short) 1, PageFormatFactory.LETTER[0], PageFormatFactory.LETTER[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition LETTER_SMALL = new ExcelPageDefinition
      ((short) 2, PageFormatFactory.LETTER_SMALL[0], PageFormatFactory.LETTER_SMALL[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition TABLOID = new ExcelPageDefinition
      ((short) 3, PageFormatFactory.TABLOID[0], PageFormatFactory.TABLOID[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition LEDGER = new ExcelPageDefinition
      ((short) 4, PageFormatFactory.LEDGER[0], PageFormatFactory.LEDGER[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition LEGAL = new ExcelPageDefinition
      ((short) 5, PageFormatFactory.LEGAL[0], PageFormatFactory.LEGAL[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition STATEMENT = new ExcelPageDefinition
      ((short) 6, PageFormatFactory.STATEMENT[0], PageFormatFactory.STATEMENT[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition EXECUTIVE = new ExcelPageDefinition
      ((short) 7, PageFormatFactory.EXECUTIVE[0], PageFormatFactory.EXECUTIVE[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition A3 = new ExcelPageDefinition
      ((short) 8, PageFormatFactory.A3[0], PageFormatFactory.A3[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition A4 = new ExcelPageDefinition
      ((short) 9, PageFormatFactory.A4[0], PageFormatFactory.A4[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition A4_SMALL = new ExcelPageDefinition
      ((short) 10, PageFormatFactory.A4_SMALL[0], PageFormatFactory.A4_SMALL[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition A5 = new ExcelPageDefinition
      ((short) 11, PageFormatFactory.A5[0], PageFormatFactory.A5[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition B4 = new ExcelPageDefinition
      ((short) 12, PageFormatFactory.B4[0], PageFormatFactory.B4[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition B5 = new ExcelPageDefinition
      ((short) 13, PageFormatFactory.B5[0], PageFormatFactory.B5[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition FOLIO = new ExcelPageDefinition
      ((short) 14, PageFormatFactory.FOLIO[0], PageFormatFactory.FOLIO[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition QUARTO = new ExcelPageDefinition
      ((short) 15, PageFormatFactory.QUARTO[0], PageFormatFactory.QUARTO[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition PAPER10X14 = new ExcelPageDefinition
      ((short) 16, PageFormatFactory.PAPER10X14[0], PageFormatFactory.PAPER10X14[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition PAPER11X17 = new ExcelPageDefinition
      ((short) 17, PageFormatFactory.PAPER11X17[0], PageFormatFactory.PAPER11X17[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition NOTE = new ExcelPageDefinition
      ((short) 18, PageFormatFactory.NOTE[0], PageFormatFactory.NOTE[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENV9 = new ExcelPageDefinition
      ((short) 19, PageFormatFactory.ENV9[0], PageFormatFactory.ENV9[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENV10 = new ExcelPageDefinition
      ((short) 20, PageFormatFactory.ENV10[0], PageFormatFactory.ENV10[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENV11 = new ExcelPageDefinition
      ((short) 21, PageFormatFactory.ENV11[0], PageFormatFactory.ENV11[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENV12 = new ExcelPageDefinition
      ((short) 22, PageFormatFactory.ENV12[0], PageFormatFactory.ENV12[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENV14 = new ExcelPageDefinition
      ((short) 23, PageFormatFactory.ENV14[0], PageFormatFactory.ENV14[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVDL = new ExcelPageDefinition
      ((short) 27, PageFormatFactory.ENVDL[0], PageFormatFactory.ENVDL[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVC5 = new ExcelPageDefinition
      ((short) 28, PageFormatFactory.ENVC5[0], PageFormatFactory.ENVC5[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVC3 = new ExcelPageDefinition
      ((short) 29, PageFormatFactory.ENVC3[0], PageFormatFactory.ENVC3[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVC4 = new ExcelPageDefinition
      ((short) 30, PageFormatFactory.ENVC4[0], PageFormatFactory.ENVC4[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVC6 = new ExcelPageDefinition
      ((short) 31, PageFormatFactory.ENVC6[0], PageFormatFactory.ENVC6[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVC65 = new ExcelPageDefinition
      ((short) 32, PageFormatFactory.ENVC65[0], PageFormatFactory.ENVC65[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVISOB4 = new ExcelPageDefinition
      ((short) 33, PageFormatFactory.ENVISOB4[0], PageFormatFactory.ENVISOB4[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVB5 = new ExcelPageDefinition
      ((short) 34, PageFormatFactory.ENVISOB5[0], PageFormatFactory.ENVISOB5[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVB6 = new ExcelPageDefinition
      ((short) 35, PageFormatFactory.ENVISOB6[0], PageFormatFactory.ENVISOB6[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVELOPE = new ExcelPageDefinition
      ((short) 36, PageFormatFactory.ENVELOPE[0], PageFormatFactory.ENVELOPE[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVMONARCH = new ExcelPageDefinition
      ((short) 37, PageFormatFactory.ENVMONARCH[0], PageFormatFactory.ENVMONARCH[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition ENVPERSONAL = new ExcelPageDefinition// envelope 6 3/4
      ((short) 38, PageFormatFactory.ENVPERSONAL[0], PageFormatFactory.ENVPERSONAL[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition FANFOLDUS = new ExcelPageDefinition
      ((short) 39, PageFormatFactory.FANFOLDUS[0], PageFormatFactory.FANFOLDUS[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition FANFOLDGERMAN = new ExcelPageDefinition
      ((short) 40, PageFormatFactory.FANFOLDGERMAN[0], PageFormatFactory.FANFOLDGERMAN[1]);

  /** A standard page format mapping for excel. */
  public static final ExcelPageDefinition FANFOLDGERMANLEGAL = new ExcelPageDefinition
      ((short) 41, PageFormatFactory.FANFOLDGERMANLEGAL[0], PageFormatFactory.FANFOLDGERMANLEGAL[1]);

  /**
   * A read only page format mapping definiton to map a page format to an
   * predefined excel constant.
   */
  public static class ExcelPageDefinition
  {
    /** The excel internal page format code referring to that page size. */
    private final short pageFormatCode;
    /** The width of the page format. */
    private final int width;
    /** The height of the page format. */
    private final int height;

    /**
     * Defines a new excel page format mapping.
     *
     * @param pageFormatCode the excel internal page format code.
     * @param width the width of the page.
     * @param height the height of the page.
     */
    public ExcelPageDefinition(final short pageFormatCode, final int width, final int height)
    {
      this.pageFormatCode = pageFormatCode;
      this.width = width;
      this.height = height;
    }

    /**
     * Return the excel page format code that describes that page size.
     *
     * @return the page format code as defined in the Excel File format.
     */
    public short getPageFormatCode()
    {
      return pageFormatCode;
    }

    /**
     * Returns the defined page width for that page definition.
     *
     * @return the page width;
     */
    public int getWidth()
    {
      return width;
    }

    /**
     * Returns the defined page height for that page definition.
     *
     * @return the page height;
     */
    public int getHeight()
    {
      return height;
    }
  }

  /**
   * Default Constructor.
   */
  private ExcelPrintSetupFactory()
  {
  }

  /**
   * Performs the page setup and searches a matching page format for the
   * report.
   *
   * @param printSetup the print setup object of the current sheet.
   * @param pageformat the pageformat defined for the report.
   * @param paperdef the excel paper size property (may be null).
   */
  public static void performPageSetup(final HSSFPrintSetup printSetup,
                                      final PageFormat pageformat,
                                      final String paperdef, final String paperOrientation)
  {
    short pageCode = parsePaperSizeProperty(paperdef);
    if (pageCode == -1)
    {
      pageCode = computePaperSize(pageformat);
    }
    if (pageCode != -1)
    {
      printSetup.setPaperSize(pageCode);
    }
    if (paperOrientation != null)
    {
      printSetup.setLandscape(paperOrientation.equalsIgnoreCase("Landscape"));
    }
    else
    {
      printSetup.setLandscape(pageformat.getOrientation() != PageFormat.PORTRAIT);
    }
  }

  /**
   * Searches all defined excel page formats to find a page format that
   * matches the given pageformat. If no matching format was found, the
   * next greater page format is used.
   * <p>
   * If no page format fits the definition, -1 is returned.
   *
   * @param format the page format
   * @return the computed paper size or -1 if no paper size matches the
   * requirements
   */
  private static short computePaperSize(final PageFormat format)
  {
    ExcelPageDefinition pageDef = null;
    final int width = (int) format.getPaper().getWidth();
    final int height = (int) format.getPaper().getHeight();
    int delta = -1;

    final Field[] fields = ExcelPrintSetupFactory.class.getDeclaredFields();
    for (int i = 0; i < fields.length; i++)
    {
      final Field field = fields[i];
      if (ExcelPageDefinition.class.isAssignableFrom(field.getType()) == false)
      {
        // Log.debug ("Is no valid pageformat definition");
        continue;
      }
      if (Modifier.isStatic(field.getModifiers()) == false)
      {
        // is no static field, who defined it here?
        continue;
      }
      try
      {
        final ExcelPageDefinition pageformat = (ExcelPageDefinition) field.get(null);
        if ((pageformat.getWidth() < width) || (pageformat.getHeight() < height))
        {
          // paper is too small, ignore it
          continue;
        }
        final int newDelta = (pageformat.getWidth() - width) + (pageformat.getHeight() - height);
        if ((delta == -1) || (newDelta < delta))
        {
          pageDef = pageformat;
          delta = newDelta;
        }
      }
      catch (IllegalAccessException iae)
      {
        // ignore ..
      }
    }
    if (pageDef == null)
    {
      return -1;
    }
    else
    {
      return pageDef.getPageFormatCode();
    }
  }

  /**
   * Parses the defined paper size for the excel sheets. The paper size can
   * be defined using the report configuration properties.
   *
   * @param paper the paper constant for the excel page size.
   * @return the parsed HSSF paper size constant or -1 if undefined.
   */
  private static short parsePaperSizeProperty(final String paper)
  {
    if (paper == null)
    {
      return -1;
    }
    try
    {
      final Field field = ExcelPrintSetupFactory.class.getDeclaredField(paper);
      if (ExcelPageDefinition.class.isAssignableFrom(field.getType()) == false)
      {
        // Log.debug ("Is no valid pageformat definition");
        return -1;
      }
      final Object o = field.get(null);
      final ExcelPageDefinition pageformat = (ExcelPageDefinition) o;
      return pageformat.getPageFormatCode();
    }
    catch (NoSuchFieldException nfe)
    {
      // Log.debug ("There is no pageformat " + name + " defined.");
      return -1;
    }
    catch (IllegalAccessException aie)
    {
      // Log.debug ("There is no pageformat " + name + " accessible.");
      return -1;
    }
  }

}

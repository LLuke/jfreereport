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
 * TextOperationModule.java
 * ------------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TextOperationModule.java,v 1.8 2003/09/13 15:14:40 taqua Exp $
 *
 * Changes
 * -------
 * 02-Dec-2002 : Initial version
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 * 07-Feb-2003 : ContentCreation extracted into separate package
 */

package org.jfree.report.modules.output.pageable.base.operations;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.content.Content;
import org.jfree.report.content.TextLine;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.Log;

/**
 * Creates the required operations to display/print text content in the output target.
 *
 * @see org.jfree.report.content.TextContent
 * @see TextLine
 * @author Thomas Morgner
 */
public strictfp class TextOperationModule extends OperationModule
{
  /**
   * Default constructor.
   */
  public TextOperationModule()
  {
    super("text/*");
  }

  /**
   * Creates a list of operations.
   *
   * @param col  the operations collector.
   * @param element  the element.
   * @param value  the content.
   * @param bounds  the bounds.
   */
  public void createOperations(final PhysicalOperationsCollector col, final Element element,
                               final Content value, final Rectangle2D bounds)
  {
    if (bounds == null)
    {
      throw new NullPointerException("Bounds is null");
    }
    if (element == null)
    {
      throw new NullPointerException("element is null");
    }
    if (value == null)
    {
      throw new NullPointerException("Value is null");
    }
    final Content c = value.getContentForBounds(bounds);
    if (c == null)
    {
      return;
    }
    // Font
    final FontDefinition font = element.getStyle().getFontDefinitionProperty();

    // Paint
    final Color paint = (Color) element.getStyle().getStyleProperty(ElementStyleSheet.PAINT);

    col.addOperation(new PhysicalOperation.SetFontOperation(font));
    col.addOperation(new PhysicalOperation.SetPaintOperation(paint));

    final ElementAlignment va
        = (ElementAlignment) element.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    final VerticalBoundsAlignment vba = getVerticalLayout(va, bounds);
    // calculate the horizontal shift ... is applied later

    final Rectangle2D cBounds = c.getMinimumContentSize();
    float vbaShift = (float) cBounds.getY();
    vbaShift = (float) vba.align(c.getMinimumContentSize()).getY() - vbaShift;

    final ElementAlignment ha
        = (ElementAlignment) element.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);

    final HorizontalBoundsAlignment hba = getHorizontalLayout(ha, bounds);
    addContent(c, col, hba, vbaShift);
    // bugfix here: Dont move the line content within the global content bounds.
  }

  /**
   * Add a single content junk (in most cases a single line or a line fragment) to
   * the list of PhysicalOperations. This method is called recursivly for all contentparts.
   *
   * @param c  the content.
   * @param col  the list where to collect the generated content
   * @param hba  the bounds.
   * @param vbaShift  the vertical bounds alignment shifting.
   */
  private void addContent(final Content c, final PhysicalOperationsCollector col,
                          final HorizontalBoundsAlignment hba,
                          final float vbaShift)
  {
    if (c instanceof TextLine)
    {
      final String value = ((TextLine) c).getContent();
      final Rectangle2D abounds = hba.align(c.getBounds());
      abounds.setRect(abounds.getX(), abounds.getY() + vbaShift,
          abounds.getWidth(), abounds.getHeight());
      col.addOperation(new PhysicalOperation.SetBoundsOperation(abounds));
      col.addOperation(new PhysicalOperation.PrintTextOperation(value));
    }
    else
    {
      for (int i = 0; i < c.getContentPartCount(); i++)
      {
        addContent(c.getContentPart(i), col, hba, vbaShift);
      }
    }
  }


  /**
   * Logs the content. This is a debug method.
   *
   * @param c  the content.
   */
  public static void print(final Content c)
  {
    if (c == null)
    {
      Log.debug("Content = " + c + "IsNull");
      return;
    }
    Log.debug("Content = " + c + " Bounds: " + c.getBounds());
    if (c instanceof TextLine)
    {
      Log.debug("Line: " + ((TextLine) c).getContent());
    }
    else
    {
      for (int i = 0; i < c.getContentPartCount(); i++)
      {
        print(c.getContentPart(i));
      }
    }
  }

}

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
 * $Id: TextOperationModule.java,v 1.5 2003/09/07 15:27:08 taqua Exp $
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
public class TextOperationModule extends OperationModule
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
   * @param e  the element.
   * @param value  the content.
   * @param bounds  the bounds.
   */
  public void createOperations(final PhysicalOperationsCollector col, final Element e,
                               final Content value, final Rectangle2D bounds)
  {
    if (bounds == null)
    {
      throw new NullPointerException("Bounds is null");
    }
    if (e == null)
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
    final FontDefinition font = e.getStyle().getFontDefinitionProperty();

    // Paint
    final Color paint = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);

    col.addOperation(new PhysicalOperation.SetFontOperation(font));
    col.addOperation(new PhysicalOperation.SetPaintOperation(paint));
    Rectangle2D cbounds = c.getMinimumContentSize();
    if (cbounds == null)
    {
      // if the content could not determine its minimum bounds, then skip ...
      cbounds = bounds.getBounds2D();
    }

    final ElementAlignment va
        = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    final VerticalBoundsAlignment vba = getVerticalLayout(va, bounds);
    // calculate the horizontal shift ... is applied later
    // vba.calculateShift(cbounds);

    final ElementAlignment ha
        = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);

    HorizontalBoundsAlignment hba = getHorizontalLayout(ha, bounds);
    addContent(c, col, hba, vba);
  }

  /**
   * Add a single content junk (in most cases a single line or a line fragment) to
   * the list of PhysicalOperations. This method is called recursivly for all contentparts.
   *
   * @param c  the content.
   * @param col  the list where to collect the generated content
   * @param hba  the bounds.
   * @param vba  the vertical bounds alignment.
   */
  private void addContent(final Content c, final PhysicalOperationsCollector col,
                          final HorizontalBoundsAlignment hba,
                          final VerticalBoundsAlignment vba)
  {
    if (c instanceof TextLine)
    {
      final String value = ((TextLine) c).getContent();
      final Rectangle2D abounds = vba.align(hba.align(c.getBounds()));
      col.addOperation(new PhysicalOperation.SetBoundsOperation(abounds));
      col.addOperation(new PhysicalOperation.PrintTextOperation(value));
    }
    else
    {
      for (int i = 0; i < c.getContentPartCount(); i++)
      {
        addContent(c.getContentPart(i), col, hba, vba);
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

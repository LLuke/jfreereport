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
 * TextOperationModule.java
 * ------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TextOperationModule.java,v 1.4 2003/01/21 17:11:39 taqua Exp $
 *
 * Changes
 * -------
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.ElementLayoutInformation;
import com.jrefinery.report.targets.pageable.contents.Content;
import com.jrefinery.report.targets.pageable.contents.TextContent;
import com.jrefinery.report.targets.pageable.contents.TextLine;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.FloatDimension;

import java.awt.Font;
import java.awt.Paint;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of operations that relate to text.
 *
 * @author Thomas Morgner
 */
public class TextOperationModule extends OperationModule
{
  /**
   * Default constructor.
   */
  public TextOperationModule ()
  {
    super ("text/*");
  }

  /**
   * Creates a list of operations.
   *
   * @param e  the element.
   * @param value  the content.
   * @param bounds  the bounds.
   *
   * @return a list of operations.
   */
  public List createOperations(Element e, Content value, Rectangle2D bounds)
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
    Content c = value.getContentForBounds(bounds);
    // Font
    Font font = e.getStyle().getFontStyleProperty();

    // Paint
    Color paint = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);

    ArrayList list = new ArrayList();
    list.add (new PhysicalOperation.SetFontOperation (font));
    list.add (new PhysicalOperation.SetPaintOperation(paint));
    Rectangle2D cbounds = c.getMinimumContentSize();
    if (cbounds == null)
    {
      // if the content could not determine its minimum bounds, then skip ...
      cbounds = bounds;
    }

    ElementAlignment va
        = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    VerticalBoundsAlignment vba = null;
    if (va.equals(ElementAlignment.TOP))
    {
      vba = new TopAlignment(bounds);
    }
    else if (va.equals(ElementAlignment.MIDDLE))
    {
      vba = new MiddleAlignment(bounds);
    }
    else
    {
      vba = new BottomAlignment(bounds);
    }
    // calculate the horizontal shift ... is applied later
    vba.calculateShift(cbounds);

    ElementAlignment ha
        = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);
    if (ha.equals(ElementAlignment.CENTER))
    {
      addContent(c, list, new CenterAlignment(bounds), vba);
    }
    else if (ha.equals(ElementAlignment.RIGHT))
    {
      addContent(c, list, new RightAlignment(bounds), vba);
    }
    else
    {
      addContent(c, list, new LeftAlignment(bounds), vba);
    }

    return list;
  }

  /**
   * Add a single content junk (in most cases a single line or a line fragment) to
   * the list of PhysicalOperations. This method is called recursivly for all contentparts.
   *
   * @param c  the content.
   * @param list  the list where to collect the generated content
   * @param bounds  the bounds.
   * @param vba  the vertical bounds alignment.
   */
  private void addContent (Content c, List list, HorizontalBoundsAlignment bounds,
                           VerticalBoundsAlignment vba)
  {
    if (c instanceof TextLine)
    {
      String value = ((TextLine) c).getContent();
      Rectangle2D abounds = vba.applyShift (bounds.align(c.getBounds()));
      list.add (new PhysicalOperation.SetBoundsOperation (abounds));
      list.add (new PhysicalOperation.PrintTextOperation(value));
    }
    else
    {
      for (int i = 0; i < c.getContentPartCount(); i++)
      {
        addContent(c.getContentPart(i), list, bounds, vba);
      }
    }
  }

  /**
   * Creates content.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public Content createContentForElement(Element e, ElementLayoutInformation bounds, OutputTarget ot)
    throws OutputTargetException
  {
    Point2D point = bounds.getAbsolutePosition();

    // TextElement has a defined width (Max(MinSize, PrefSize).
    // and a maximum height (Min(MaxSize, PrefSize).

    Dimension2D wDim = ElementLayoutInformation.unionMax(bounds.getMinimumSize(),
                                                           bounds.getPreferredSize());
    Dimension2D hDim = ElementLayoutInformation.unionMin(bounds.getMaximumSize(),
                                                           bounds.getPreferredSize());
    Dimension2D dim = new FloatDimension(wDim.getWidth(), hDim.getHeight());

    String text = (String) e.getValue();
    Font f = e.getStyle().getFontStyleProperty();
    Rectangle2D tBounds = new Rectangle2D.Double(point.getX(),
                                                  point.getY(),
                                                  dim.getWidth(),
                                                  dim.getHeight());
    TextContent tc = new TextContent(text, tBounds, ot.createTextSizeCalculator(f));
    return tc;
  }

  /**
   * Logs the content.
   *
   * @param c  the content.
   */
  public static void print (Content c)
  {
    Log.debug ("Content = " + c + ", " + c.getBounds());
    if (c == null)
    {
      Log.debug ("IsNull");
      return;
    }
    if (c instanceof TextLine)
    {
      Log.debug ("Line: " + ((TextLine) c).getContent());
    }
    else
    {
      for (int i = 0; i < c.getContentPartCount(); i++)
      {
        print (c.getContentPart(i));
      }
    }
  }

}

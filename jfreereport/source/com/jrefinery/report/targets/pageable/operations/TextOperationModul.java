/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * ----------------------------------
 * TextOperationModul.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: TextOperationModul.java,v 1.1 2002/12/02 17:56:58 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.contents.Content;
import com.jrefinery.report.targets.pageable.contents.TextContent;
import com.jrefinery.report.targets.pageable.contents.TextLine;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class TextOperationModul extends OperationModul
{
  public TextOperationModul ()
  {
    super ("text/*");
  }

  public List createOperations(Element e, Content value, Rectangle2D bounds)
  {
    if (bounds == null) throw new NullPointerException("Bounds is null");
    if (e == null) throw new NullPointerException("element is null");
    if (value == null) throw new NullPointerException("Value is null");
    Content c = value.getContentForBounds(bounds);
    print(c);
    // Font
    Font font = e.getStyle().getFontStyleProperty();

    // Paint
    Paint paint = (Paint) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);

    ArrayList list = new ArrayList();
    list.add (new PhysicalOperation.SetFontOperation (font));
    list.add (new PhysicalOperation.SetPaintOperation(paint));
    Rectangle2D cbounds = c.getMinimumContentSize();
    if (cbounds == null)
    {
      // if the content could not determine its minimum bounds, then skip ...
      cbounds = bounds;
    }

    ElementAlignment va = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
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


    ElementAlignment ha = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);
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

  private void addContent (Content c, List list, HorizontalBoundsAlignment bounds, VerticalBoundsAlignment vba)
  {
    if (c instanceof TextLine)
    {
      String value = ((TextLine) c).getContent();
      Log.debug ("Adding Content: " + value + " -> " + bounds.align(c.getBounds()));
      list.add (new PhysicalOperation.SetBoundsOperation (vba.applyShift (bounds.align(c.getBounds()))));
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

  public Content createContentForElement(Element e, Rectangle2D bounds, OutputTarget ot)
    throws OutputTargetException
  {
    String text = (String) e.getValue();
    Font f = e.getStyle().getFontStyleProperty();
    TextContent tc = new TextContent(text, bounds, ot.createTextSizeCalculator(f));
    return tc;
  }


  public static void print (Content c)
  {
    Log.debug ("c = " + c + ", " + c.getBounds());
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

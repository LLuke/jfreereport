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
 * ----------------
 * GeneralPathObjectDescription.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 20.03.2003 : Initial version
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.Shape;
import java.util.ArrayList;

import org.jfree.xml.factory.objects.AbstractObjectDescription;
import org.jfree.xml.factory.objects.ObjectFactoryException;

/**
 *
 */
public class GeneralPathObjectDescription extends AbstractObjectDescription
{
  public GeneralPathObjectDescription()
  {
    this(GeneralPath.class);
  }

  public GeneralPathObjectDescription(Class c)
  {
    super(c);
    if (Shape.class.isAssignableFrom(c) == false)
    {
      throw new IllegalArgumentException("Must be a shape instance");
    }
    // "even-odd" or "non-zero"
    setParameterDefinition("windingRule", String.class);
    setParameterDefinition("segments", PathIteratorSegment[].class);
  }

  /**
   * Creates an object based on this description.
   *
   * @return The object.
   */
  public Object createObject()
  {
    int wRule = parseWindingRule();
    if (wRule == -1)
    {
      return null;
    }

    PathIteratorSegment[] segments = (PathIteratorSegment[]) getParameter("segments");
    if (segments == null)
    {
      return null;
    }

    GeneralPath path = new GeneralPath();
    path.setWindingRule(wRule);
    for (int i = 0; i < segments.length; i++)
    {
      int segmentType = segments[i].getSegmentType();
      switch (segmentType)
      {
        case PathIterator.SEG_CLOSE:
          {
            path.closePath();
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            path.curveTo(segments[i].getX1(), segments[i].getY1(),
                segments[i].getX2(), segments[i].getY2(),
                segments[i].getX3(), segments[i].getY3());
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            path.lineTo(segments[i].getX1(), segments[i].getY1());
            break;
          }
        case PathIterator.SEG_MOVETO:
          {
            path.moveTo(segments[i].getX1(), segments[i].getY1());
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            path.quadTo(segments[i].getX1(), segments[i].getY1(),
                segments[i].getX2(), segments[i].getY2());
            break;
          }
      }
    }
    return path;
  }

  private int parseWindingRule()
  {
    String windingRule = (String) getParameter("windingRule");
    int wRule = -1;
    if (windingRule == null)
    {
      return wRule;
    }
    if (windingRule.equals("wind-even-odd"))
    {
      wRule = PathIterator.WIND_EVEN_ODD;
    }
    else if (windingRule.equals("wind-non-zero"))
    {
      wRule = PathIterator.WIND_NON_ZERO;
    }
    return wRule;
  }

  /**
   * Sets the parameters of this description object to match the supplied object.
   *
   * @param o  the object (should be an instance of <code>FontDefinition</code>).
   *
   * @throws ObjectFactoryException if the object is not an instance of <code>Float</code>.
   */
  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (getObjectClass().isAssignableFrom(o.getClass()) == false)
    {
      throw new ObjectFactoryException("Class is not assignable");
    }

    Shape s = (Shape) o;
    PathIterator pi = s.getPathIterator(AffineTransform.getTranslateInstance(0,0));
    if (pi.getWindingRule() == PathIterator.WIND_EVEN_ODD)
    {
      setParameter("windingRule", "wind-even-odd");
    }
    else
    {
      setParameter("windingRule", "wind-non-zero");
    }

    float[] points = new float[6];
    ArrayList segments = new ArrayList();
    while (pi.isDone() == false)
    {
      int type = pi.currentSegment(points);
      PathIteratorSegment seg = new PathIteratorSegment();
      switch (type)
      {
        case PathIterator.SEG_CLOSE:
          {
            seg.setSegmentType(PathIterator.SEG_CLOSE);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            seg.setSegmentType(PathIterator.SEG_CUBICTO);
            seg.setX1(points[0]);
            seg.setY1(points[1]);
            seg.setX2(points[2]);
            seg.setY2(points[3]);
            seg.setX3(points[4]);
            seg.setY3(points[5]);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            seg.setSegmentType(PathIterator.SEG_LINETO);
            seg.setX1(points[0]);
            seg.setY1(points[1]);
            break;
          }
        case PathIterator.SEG_MOVETO:
          {
            seg.setSegmentType(PathIterator.SEG_MOVETO);
            seg.setX1(points[0]);
            seg.setY1(points[1]);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            seg.setSegmentType(PathIterator.SEG_QUADTO);
            seg.setX1(points[0]);
            seg.setY1(points[1]);
            seg.setX2(points[2]);
            seg.setY2(points[3]);
            break;
          }
      }
      segments.add(seg);
      pi.next();
    }

    setParameter("segments", segments.toArray(new PathIteratorSegment[0]));
  }
}

/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * PathIteratorSegmentObjectDescription.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------------------------
 * 09-May-2003 : Initial version
 *
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.awt.geom.PathIterator;

import org.jfree.xml.factory.objects.AbstractObjectDescription;
import org.jfree.xml.factory.objects.ObjectFactoryException;

public class PathIteratorSegmentObjectDescription extends AbstractObjectDescription
{
  private static final String SEG_MOVE_TO = "move-to";
  private static final String SEG_LINE_TO = "line-to";
  private static final String SEG_CUBIC_TO = "cubic-to";
  private static final String SEG_QUAD_TO = "quad-to";
  private static final String SEG_CLOSE = "close";

  /**
   * Creates a new object description.
   */
  public PathIteratorSegmentObjectDescription()
  {
    super(PathIteratorSegment.class);
    setParameterDefinition("x1", Float.TYPE);
    setParameterDefinition("y1", Float.TYPE);
    setParameterDefinition("x2", Float.TYPE);
    setParameterDefinition("y2", Float.TYPE);
    setParameterDefinition("x3", Float.TYPE);
    setParameterDefinition("y3", Float.TYPE);
    setParameterDefinition("segmentType", String.class);
  }

  /**
   * Creates an object based on the description.
   *
   * @return The object.
   */
  public Object createObject()
  {
    PathIteratorSegment seg = new PathIteratorSegment();
    int segType = parseSegmentType((String) getParameter("segmentType"));
    if (segType == -1)
      return null;

    seg.setSegmentType(segType);
    seg.setX1(getFloatParameter("x1"));
    seg.setX2(getFloatParameter("x2"));
    seg.setX3(getFloatParameter("x3"));
    seg.setY1(getFloatParameter("y1"));
    seg.setY2(getFloatParameter("y2"));
    seg.setY3(getFloatParameter("y3"));
    return seg;
  }

  private int parseSegmentType (String segment)
  {
    if (segment == null)
    {
      return -1;
    }
    if (segment.equals(SEG_CLOSE))
    {
      return PathIterator.SEG_CLOSE;
    }
    if (segment.equals(SEG_CUBIC_TO))
    {
      return PathIterator.SEG_CUBICTO;
    }
    if (segment.equals(SEG_LINE_TO))
    {
      return PathIterator.SEG_LINETO;
    }
    if (segment.equals(SEG_MOVE_TO))
    {
      return PathIterator.SEG_MOVETO;
    }
    if (segment.equals(SEG_QUAD_TO))
    {
      return PathIterator.SEG_QUADTO;
    }
    return -1;
  }

  private String createSegmentType (int segment)
  {
    switch (segment)
    {
      case PathIterator.SEG_CLOSE: return SEG_CLOSE;
      case PathIterator.SEG_CUBICTO: return SEG_CUBIC_TO;
      case PathIterator.SEG_LINETO: return SEG_LINE_TO;
      case PathIterator.SEG_MOVETO: return SEG_MOVE_TO;
      case PathIterator.SEG_QUADTO: return SEG_QUAD_TO;
    }
    return null;
  }

  private float getFloatParameter (String name)
  {
    Float o = (Float) getParameter(name);
    if (o == null)
    {
       return 0;
    }
    return o.floatValue();
  }

  /**
   * Sets the parameters of this description object to match the supplied object.
   *
   * @param o  the object.
   *
   * @throws ObjectFactoryException if there is a problem while reading the
   * properties of the given object.
   */
  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if ((o instanceof PathIteratorSegment) == false)
    {
      throw new ObjectFactoryException(
          "The given object is no PathIteratorSegment.");
    }

    PathIteratorSegment seg = (PathIteratorSegment) o;
    setParameter("segmentType", createSegmentType(seg.getSegmentType()));
    setParameter("x1", String.valueOf(seg.getX1()));
    setParameter("x2", String.valueOf(seg.getX2()));
    setParameter("x3", String.valueOf(seg.getX3()));
    setParameter("y1", String.valueOf(seg.getY1()));
    setParameter("y2", String.valueOf(seg.getY2()));
    setParameter("y2", String.valueOf(seg.getY3()));
  }
}

/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * $Id: PathIteratorSegmentObjectDescription.java,v 1.4 2003/06/29 16:59:25 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09-May-2003 : Initial version
 *
 */
package org.jfree.report.modules.parser.ext.factory.objects;

import java.awt.geom.PathIterator;

import org.jfree.xml.factory.objects.AbstractObjectDescription;
import org.jfree.xml.factory.objects.ObjectFactoryException;

/**
 * Describes the PathIteratorSegment object for the object factories used
 * in the parser.
 *
 * @see org.jfree.xml.factory.objects.ClassFactory
 * @see org.jfree.xml.factory.objects.ObjectDescription
 * @author Thomas Morgner
 */
public class PathIteratorSegmentObjectDescription extends AbstractObjectDescription
{
  /**
   * A constant defining a possible string representation of a
   * PathIterator constant.
   */
  private static final String SEG_MOVE_TO = "move-to";
  /**
   * A constant defining a possible string representation of a
   * PathIterator constant.
   */
  private static final String SEG_LINE_TO = "line-to";
  /**
   * A constant defining a possible string representation of a
   * PathIterator constant.
   */
  private static final String SEG_CUBIC_TO = "cubic-to";
  /**
   * A constant defining a possible string representation of a
   * PathIterator constant.
   */
  private static final String SEG_QUAD_TO = "quad-to";
  /**
   * A constant defining a possible string representation of a
   * PathIterator constant.
   */
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
    final PathIteratorSegment seg = new PathIteratorSegment();
    final int segType = parseSegmentType((String) getParameter("segmentType"));
    if (segType == -1)
    {
      return null;
    }

    seg.setSegmentType(segType);
    seg.setX1(getFloatParameter("x1"));
    seg.setX2(getFloatParameter("x2"));
    seg.setX3(getFloatParameter("x3"));
    seg.setY1(getFloatParameter("y1"));
    seg.setY2(getFloatParameter("y2"));
    seg.setY3(getFloatParameter("y3"));
    return seg;
  }

  /**
   * Parses the given string representation and returns the path iterator
   * type or -1 if the string does not represent a path iterator value.
   *
   * @param segment the string that contains the PathIterator type.
   * @return the parsed PathIterator type or -1.
   */
  private int parseSegmentType(final String segment)
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

  /**
   * Creates a string representation of the given PathIterator segment type.
   *
   * @param segment the segment type
   * @return the segment type as string
   * @throws IllegalArgumentException if the segment type is none of the
   * predefined PathIterator types.
   */
  private String createSegmentType(final int segment)
    throws IllegalArgumentException
  {
    switch (segment)
    {
      case PathIterator.SEG_CLOSE:
        return SEG_CLOSE;
      case PathIterator.SEG_CUBICTO:
        return SEG_CUBIC_TO;
      case PathIterator.SEG_LINETO:
        return SEG_LINE_TO;
      case PathIterator.SEG_MOVETO:
        return SEG_MOVE_TO;
      case PathIterator.SEG_QUADTO:
        return SEG_QUAD_TO;
      default:
        throw new IllegalArgumentException("The segment type is invalid.");
    }
  }

  /**
   * Reads the given parameter as float or returns 0 if the parameter
   * is not specified.
   *
   * @param name the parameter name
   * @return the float value of the parameter or 0.
   */
  private float getFloatParameter(final String name)
  {
    final Float o = (Float) getParameter(name);
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
  public void setParameterFromObject(final Object o) throws ObjectFactoryException
  {
    if ((o instanceof PathIteratorSegment) == false)
    {
      throw new ObjectFactoryException(
          "The given object is no PathIteratorSegment.");
    }

    final PathIteratorSegment seg = (PathIteratorSegment) o;
    setParameter("segmentType", createSegmentType(seg.getSegmentType()));
    setParameter("x1", String.valueOf(seg.getX1()));
    setParameter("x2", String.valueOf(seg.getX2()));
    setParameter("x3", String.valueOf(seg.getX3()));
    setParameter("y1", String.valueOf(seg.getY1()));
    setParameter("y2", String.valueOf(seg.getY2()));
    setParameter("y2", String.valueOf(seg.getY3()));
  }
}

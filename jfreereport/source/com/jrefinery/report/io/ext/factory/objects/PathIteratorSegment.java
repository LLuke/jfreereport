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
 * PathIteratorSegment.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ObjectReferenceTableModel.java,v 1.6 2003/05/02 12:40:10 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09-May-2003 : Initial version
 *
 */
package com.jrefinery.report.io.ext.factory.objects;

public class PathIteratorSegment
{
  private int segmentType;
  private float x1;
  private float y1;
  private float x2;
  private float y2;
  private float x3;
  private float y3;

  public PathIteratorSegment()
  {
  }

  public int getSegmentType()
  {
    return segmentType;
  }

  public void setSegmentType(int segmentType)
  {
    this.segmentType = segmentType;
  }

  public float getX1()
  {
    return x1;
  }

  public void setX1(float x1)
  {
    this.x1 = x1;
  }

  public float getY1()
  {
    return y1;
  }

  public void setY1(float y1)
  {
    this.y1 = y1;
  }

  public float getX2()
  {
    return x2;
  }

  public void setX2(float x2)
  {
    this.x2 = x2;
  }

  public float getY2()
  {
    return y2;
  }

  public void setY2(float y2)
  {
    this.y2 = y2;
  }

  public float getX3()
  {
    return x3;
  }

  public void setX3(float x3)
  {
    this.x3 = x3;
  }

  public float getY3()
  {
    return y3;
  }

  public void setY3(float y3)
  {
    this.y3 = y3;
  }
}

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
 * ------------------------
 * DefaultClassFactory.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.FontDefinition;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A default implementation of the {@link ClassFactory} interface.
 * 
 * @author Thomas Morgner
 */
public class DefaultClassFactory extends ClassFactoryImpl
{
  /**
   * Creates a new factory.
   */
  public DefaultClassFactory()
  {
    registerClass(Dimension2D.class, new BeanObjectDescription(FloatDimension.class));
    registerClass(Date.class, new DateObjectDescription());
    registerClass(Boolean.TYPE, new BooleanObjectDescription());
    registerClass(Byte.TYPE, new ByteObjectDescription());
    registerClass(Double.TYPE, new DoubleObjectDescription());
    registerClass(Float.TYPE, new FloatObjectDescription());
    registerClass(Integer.TYPE, new IntegerObjectDescription());
    registerClass(Long.TYPE, new LongObjectDescription());
    registerClass(Short.TYPE, new ShortObjectDescription());
    registerClass(Character.TYPE, new CharacterObjectDescription());
    registerClass(Character.class, new CharacterObjectDescription());
    registerClass(Boolean.class, new BooleanObjectDescription());
    registerClass(Byte.class, new ByteObjectDescription());
    registerClass(Double.class, new DoubleObjectDescription());
    registerClass(Float.class, new FloatObjectDescription());
    registerClass(Integer.class, new IntegerObjectDescription());
    registerClass(Long.class, new LongObjectDescription());
    registerClass(Short.class, new ShortObjectDescription());
    registerClass(Line2D.class, new Line2DObjectDescription());
    registerClass(Point2D.class, new Point2DObjectDescription());
    registerClass(Rectangle2D.class, new Rectangle2DObjectDescription());
    registerClass(String.class, new StringObjectDescription());
    registerClass(ElementAlignment.class, new AlignmentObjectDescription());
    registerClass(Color.class, new ColorObjectDescription());
    registerClass(BasicStroke.class, new BasicStrokeObjectDescription());

    registerClass(Format.class, new ClassLoaderObjectDescription());
    registerClass(NumberFormat.class, new BeanObjectDescription(NumberFormat.class));
    registerClass(DecimalFormat.class, new DecimalFormatObjectDescription());
    registerClass(DecimalFormatSymbols.class, 
                  new BeanObjectDescription(DecimalFormatSymbols.class));
    registerClass(DateFormat.class, new ClassLoaderObjectDescription());
    registerClass(SimpleDateFormat.class, new BeanObjectDescription(DecimalFormatSymbols.class));
    registerClass(DateFormatSymbols.class, new ClassLoaderObjectDescription());

    registerClass(FontDefinition.class, new FontDefinitionObjectDescription());
  }
}

/**
 * Date: Jan 10, 2003
 * Time: 9:04:41 PM
 *
 * $Id: DefaultClassFactory.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FloatDimension;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;

public class DefaultClassFactory extends ClassFactoryImpl
{
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
  }
}

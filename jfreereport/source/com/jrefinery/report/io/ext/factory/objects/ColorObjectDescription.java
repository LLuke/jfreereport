/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: ColorObjectDescription.java,v 1.3 2003/01/23 18:07:45 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.util.Log;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ColorObjectDescription extends AbstractObjectDescription
{
  public ColorObjectDescription()
  {
    super(Color.class);
    setParameterDefinition("value", String.class);
  }

  public Object createObject()
  {
    String value = (String) getParameter("value");
    if (value == null) return Color.black;
    
    try
    {
      // get color by hex or octal value
      return Color.decode(value);
    }
    catch (NumberFormatException nfe)
    {
      // if we can't decode lets try to get it by name
      try
      {
        // try to get a color by name using reflection
        // black is used for an instance and not for the color itselfs
        Field f = Color.class.getField(value);

        return (Color) f.get(null);
      }
      catch (Exception ce)
      {
        Log.debug("No such Color : " + value);
        // if we can't get any color return black
        return Color.black;
      }
    }
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof Color == false)
    {
      throw new ObjectFactoryException("Is no instance of color");
    }
    Color c = (Color) o;

    try
    {
      Field[] fields = Color.class.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        Field f = fields[i];
        if (Modifier.isPublic(f.getModifiers()) &&
            Modifier.isFinal(f.getModifiers()) &&
            Modifier.isStatic(f.getModifiers()))
        {
          String name = f.getName();
          Object oColor = f.get(null);
          if (oColor instanceof Color)
          {
            if (c.equals(oColor))
            {
              setParameter("value", name);
              return;
            }
          }
        }
      }
    }
    catch (Exception e)
    {
    }

    // no defined constant color, so this must be a user defined color
    String color = Integer.toHexString(c.getRGB() & 0x00ffffff);
    StringBuffer retval = new StringBuffer(7);
    retval.append("#");

    int fillUp = 6 - color.length();
    for (int i = 0; i < fillUp; i++)
    {
      retval.append("0");
    }

    retval.append(color);
    setParameter("value", retval.toString());
  }
}

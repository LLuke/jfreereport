/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.util.Log;
import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

import java.awt.Color;
import java.lang.reflect.Field;

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
}

/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libformula/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 *
 * ------------
 * $Id: DefaultLocalizationContext.java,v 1.3 2007/04/01 13:51:52 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import org.jfree.formula.typing.Type;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 03.11.2006, 14:28:12
 * 
 * @author Thomas Morgner
 */
public class DefaultLocalizationContext implements LocalizationContext
{
  private static final String CONFIG_TIMEZONE_KEY = "org.jfree.formula.timezone";

  private static final String CONFIG_LOCALE_KEY = "org.jfree.formula.locale";

  private static final String CONFIG_DATEFORMAT_KEY = "org.jfree.formula.dateformat.";

  private List dateFormats;

  private List datetimeFormats;

  private List timeFormats;

  private Locale locale;
  
  private TimeZone timeZone;

  public DefaultLocalizationContext()
  {
    dateFormats = new ArrayList();
    datetimeFormats = new ArrayList();
    timeFormats = new ArrayList();
  }

  public Locale getLocale()
  {
    return locale;
  }

  public ResourceBundle getBundle(String id)
  {
    return ResourceBundle.getBundle(id, getLocale());
  }

  public TimeZone getTimeZone()
  {
    return timeZone;
  }

  public List getDateFormats(Type type)
  {
    if (type.isFlagSet(Type.DATE_TYPE))
    {
      return dateFormats;
    }
    else if (type.isFlagSet(Type.DATETIME_TYPE))
    {
      return datetimeFormats;
    }
    else if (type.isFlagSet(Type.TIME_TYPE))
    {
      return timeFormats;
    }
    return null;
  }
  
  private String[] createLocale(String locale)
  {
    final String[] split2 = locale.split("_");
    final String language = split2[0];
    final String country = split2.length >= 2 ? split2[1] : "";
    final String variant = split2.length >= 3 ? split2[2] : "";
    
    return new String[]{language, country, variant};
  }

  public void initialize(Configuration config)
  {
    // setting locale
    final String loc = config.getConfigProperty(CONFIG_LOCALE_KEY,
        Locale.getDefault().toString());
    final String[] loc2 = createLocale(loc);
    this.locale = new Locale(loc2[0], loc2[1], loc2[2]);

    //setting timezone
    final String timeZoneId = config.getConfigProperty(CONFIG_TIMEZONE_KEY,
        TimeZone.getDefault().getID());
    timeZone = TimeZone.getTimeZone(timeZoneId);
    
    // adding custom dateformats
    final Iterator properties = config.findPropertyKeys(CONFIG_DATEFORMAT_KEY);
    while (properties.hasNext())
    {
      String property = (String) properties.next();
      final String key = property.substring(CONFIG_DATEFORMAT_KEY.length(),
          property.length());
      final String[] split = key.split(".");
      final String format = config.getConfigProperty(key);
      if (split.length == 2)
      {
        final String type = split[0];
        final String[] locale = createLocale(split[1]);

        final SimpleDateFormat df = new SimpleDateFormat(format, new Locale(
            locale[0], locale[1], locale[2]));

        if (Type.TIME_TYPE.equals(type))
        {
          timeFormats.add(df);
        }
        else if (Type.DATE_TYPE.equals(type))
        {
          dateFormats.add(df);
        }
        else if (Type.DATETIME_TYPE.equals(type))
        {
          datetimeFormats.add(df);
        }
      }
    }

    // adding default dateformats using current local
    datetimeFormats.add(SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,
        SimpleDateFormat.SHORT, getLocale()));
    dateFormats.add(SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,
        getLocale()));
    timeFormats.add(SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT,
        getLocale()));

    // adding default ISO8 dateformats
    datetimeFormats.add(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US));
    dateFormats.add(new SimpleDateFormat("yyyy-MM-dd", Locale.US));
    timeFormats.add(new SimpleDateFormat("hh:mm:ss", Locale.US));
  }
}

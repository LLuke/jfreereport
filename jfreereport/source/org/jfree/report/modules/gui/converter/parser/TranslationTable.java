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
 * TranslationTable.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TranslationTable.java,v 1.1 2003/08/26 17:35:51 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 25.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.converter.parser;

import java.util.Properties;
import java.io.InputStream;

import org.jfree.report.util.Log;

public class TranslationTable
{
  private Properties translationMap;
  private String context;

  public TranslationTable (Properties translations, String context)
  {
    this.translationMap = translations;
    this.context = context;
  }

  public String translateAttribute (String localName, String orgValue)
  {
    if (orgValue == null)
    {
      return null;
    }
    if (localName == null)
    {
      throw new NullPointerException();
    }
    if (localName.length() == 0)
    {
      throw new IllegalArgumentException();
    }
    StringBuffer key = new StringBuffer();
    key.append(context);
    key.append("@");
    key.append(localName);
    key.append("@");
    key.append(orgValue);
//    Log.debug ("Attemp to translate: " + key);
    String o = translationMap.getProperty(key.toString(), orgValue);
//    Log.debug ("Result: " + o);

    return o;
  }

  public static void main (String[] args)
  {
    InputStream in = TranslationTable.class.getResourceAsStream("translations.properties");
    if (in == null)
    {
      return;
    }
    Properties translations = new Properties();
    try
    {
      translations.load(in);
    }
    catch (Exception e)
    {
      Log.debug ("Unable to load the translation set.");
    }
    TranslationTable table = new TranslationTable
      (translations, "report-definition.parser-config.object-factory");
    Log.debug (table.translateAttribute
      ("class", "com.jrefinery.report.io.ext.factory.objects.DefaultClassFactory"));
  }
}

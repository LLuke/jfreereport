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
 * $Id: TranslationTable.java,v 1.3 2003/09/10 18:20:25 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 25.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.converter.parser;

import java.util.Properties;

/**
 * A simple attribute translator. The translator searches a set of properties
 * for a specific value and replaces the attribute value with the updated
 * value.
 * <p>
 * A translation will be valid for a given context, which is build acording
 * to the rules specified in the contextmap.properties file.
 *   
 * @author Thomas Morgner
 */
public class TranslationTable
{
  /** The translation map contains all keys and their values. */
  private final Properties translationMap;
  /** the current context, where the map will be valid. */
  private final String context;

  /**
   * Creates a new translation table which will contain the given translations
   * and will be valid within the given context.
   * 
   * @param translations the translations
   * @param context the translation context.
   */
  public TranslationTable (Properties translations, String context)
  {
    this.translationMap = translations;
    this.context = context;
  }

  /**
   * Translates the value of the given attribute into a new value.
   * If no translation for the original value is known, then the value
   * is returned unchanged.
   *  
   * @param localName the name of the attribute
   * @param orgValue the untranslated value as read from the xml file 
   * @return the translated value if defined, else the untranslated value.
   */
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
    String o = translationMap.getProperty(key.toString(), orgValue);

    return o;
  }

//  public static void main (String[] args)
//  {
//    InputStream in = TranslationTable.class.getResourceAsStream("translations.properties");
//    if (in == null)
//    {
//      return;
//    }
//    Properties translations = new Properties();
//    try
//    {
//      translations.load(in);
//    }
//    catch (Exception e)
//    {
//      Log.debug ("Unable to load the translation set.");
//    }
//    TranslationTable table = new TranslationTable
//      (translations, "report-definition.parser-config.object-factory");
//    Log.debug (table.translateAttribute
//      ("class", "com.jrefinery.report.io.ext.factory.objects.DefaultClassFactory"));
//  }
}

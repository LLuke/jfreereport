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
 * --------------------------
 * EncodingComboBoxModel.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: EncodingComboBoxModel.java,v 1.15 2003/04/11 14:11:44 taqua Exp $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.preview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.Enumeration;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jrefinery.report.util.EncodingSupport;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.report.util.Log;

/**
 * A model for the 'encoding' combo box.
 * <p>
 * This model is used in the {@link CSVExportDialog} class (and possibly others).
 *
 * @author Thomas Morgner.
 */
public class EncodingComboBoxModel implements ComboBoxModel
{
  private static final String ENCODING_DEFAULT_DESCRIPTION = "[no description]";

  /**
   * An encoding comparator.
   */
  private static class EncodingCarrierComparator implements Comparator
  {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.
     *
     * @param o1  the first object to be compared.
     * @param o2  the second object to be compared.
     *
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     *
     * @throws ClassCastException if the arguments' types prevent them from
     *         being compared by this Comparator.
     */
    public int compare(Object o1, Object o2)
    {
      EncodingCarrier e1 = (EncodingCarrier) o1;
      EncodingCarrier e2 = (EncodingCarrier) o2;
      return e1.getName().compareTo(e2.getName());
    }

    /**
     * Returns <code>true</code> if this object is equal to <code>o</code>, and <code>false</code>
     * otherwise.
     *
     * @param o  the object.
     *
     * @return A boolean.
     */
    public boolean equals (Object o)
    {
      if (o == null)
      {
        return false;
      }
      return getClass().equals(o.getClass());
    }

    /**
     * All comparators of this type are equal.
     *
     * @return A hash code.
     */
    public int hashCode ()
    {
      return getClass().hashCode();
    }
  }

  /**
   * An encoding carrier.
   */
  private static class EncodingCarrier
  {
    /** The encoding name. */
    private String name;

    /** The encoding description. */
    private String description;

    /** The display name. */
    private String displayName;
    
    /**
     * Creates a new encoding.
     *
     * @param name  the name (<code>null</code> not permitted).
     * @param description  the description.
     */
    public EncodingCarrier(String name, String description)
    {
      if (name == null)
      {
        throw new NullPointerException();
      }
      this.name = name;
      this.description = description;
      StringBuffer dName = new StringBuffer();
      dName.append(name);
      dName.append(" (");
      dName.append(description);
      dName.append(")");
      this.displayName = dName.toString();
    }

    /**
     * Returns the name.
     *
     * @return The name.
     */
    public String getName()
    {
      return name;
    }

    /**
     * Returns the description.
     *
     * @return The description.
     */
    public String getDescription()
    {
      return description;
    }

    /**
     * Returns the display name (the regular name followed by the description in brackets).
     *
     * @return The display name.
     */
    public String getDisplayName ()
    {
      return displayName;
    }

    /**
     * Returns <code>true</code> if the objects are equal, and <code>false</code> otherwise.
     *
     * @param o  the object.
     *
     * @return A boolean.
     */
    public boolean equals(Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (!(o instanceof EncodingCarrier))
      {
        return false;
      }

      final EncodingCarrier carrier = (EncodingCarrier) o;

      if (!name.equals(carrier.name))
      {
        return false;
      }

      return true;
    }

    /**
     * Returns a hash code.
     *
     * @return The hash code.
     */
    public int hashCode()
    {
      return name.hashCode();
    }
  }

  /** Storage for the encodings. */
  private ArrayList encodings;

  /** Storage for registered listeners. */
  private ArrayList listDataListeners;

  /** The selected index. */
  private int selectedIndex;

  /** The selected object. */
  private Object selectedObject;

  /**
   * Creates a new model.
   */
  public EncodingComboBoxModel()
  {
    encodings = new ArrayList();
    listDataListeners = null;
    selectedIndex = -1;
  }

  /**
   * Adds an encoding.
   *
   * @param name  the name.
   * @param description  the description.
   *
   * @return <code>true</code> if the encoding is valid and added to the model, <code>false</code>
   *         otherwise.
   */
  public boolean addEncoding (String name, String description)
  {
    if (EncodingSupport.isSupportedEncoding(name))
    {
      encodings.add (new EncodingCarrier(name, description));
    }
    else
    {
      return false;
    }

    fireContentsChanged();
    return true;
  }

  /**
   * Adds an encoding to the model without checking its validity.
   *
   * @param name  the name.
   * @param description  the description.
   */
  public void addEncodingUnchecked (String name, String description)
  {
    encodings.add (new EncodingCarrier(name, description));
    fireContentsChanged();
  }

  /**
   * Make sure, that this encoding is defined and selectable in the combobox model.
   *
   * @param encoding the encoding that should be verified.
   */
  public void ensureEncodingAvailable (String encoding)
  {
    String desc = getDefaultEncodings().getProperty(encoding, ENCODING_DEFAULT_DESCRIPTION);
    EncodingCarrier ec = new EncodingCarrier(encoding, desc);
    if (encodings.contains(ec) == false)
    {
      encodings.add(ec);
      fireContentsChanged();
    }
  }

  /**
   * Sorts the encodings.
   */
  public void sort ()
  {
    Collections.sort(encodings, new EncodingCarrierComparator());
    fireContentsChanged();
  }

  /**
   * Notifies all registered listeners that the content of the model has changed.
   */
  protected void fireContentsChanged ()
  {
    if (listDataListeners == null)
    {
      return;
    }
    ListDataEvent evt = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
    for (int i = 0; i < listDataListeners.size(); i++)
    {
      ListDataListener l = (ListDataListener) listDataListeners.get(i);
      l.contentsChanged(evt);
    }
  }

  /**
   * Set the selected item. The implementation of this  method should notify
   * all registered <code>ListDataListener</code>s that the contents
   * have changed.
   *
   * @param anItem the list object to select or <code>null</code>
   *        to clear the selection
   */
  public void setSelectedItem(Object anItem)
  {
    selectedObject = anItem;
    if (anItem != null && anItem instanceof String)
    {
      selectedIndex = encodings.indexOf(new EncodingCarrier((String) anItem, null));
    }
  }

  /**
   * Returns the selected index.
   *
   * @return The index.
   */
  public int getSelectedIndex()
  {
    return selectedIndex;
  }

  /**
   * Returns the selected encoding.
   *
   * @return The encoding (name).
   */
  public String getSelectedEncoding ()
  {
    if (selectedIndex == -1)
    {
      return null;
    }
    EncodingCarrier ec = (EncodingCarrier) encodings.get(selectedIndex);
    return ec.getName();
  }

  /**
   * Returns the selected item.
   *
   * @return The selected item or <code>null</code> if there is no selection
   */
  public Object getSelectedItem()
  {
    return selectedObject;
  }

  /**
   * Returns the length of the list.
   *
   * @return the length of the list
   */
  public int getSize()
  {
    return encodings.size();
  }

  /**
   * Returns the value at the specified index.
   *
   * @param index the requested index
   *
   * @return the value at <code>index</code>
   */
  public Object getElementAt(int index)
  {
    EncodingCarrier ec = (EncodingCarrier) encodings.get(index);
    return ec.getDisplayName();
  }

  /**
   * Adds a listener to the list that's notified each time a change
   * to the data model occurs.
   *
   * @param l the <code>ListDataListener</code> to be added
   */
  public void addListDataListener(ListDataListener l)
  {
    if (listDataListeners == null)
    {
      listDataListeners = new ArrayList(5);
    }
    listDataListeners.add (l);
  }

  /**
   * Removes a listener from the list that's notified each time a
   * change to the data model occurs.
   *
   * @param l the <code>ListDataListener</code> to be removed
   */
  public void removeListDataListener(ListDataListener l)
  {
    if (listDataListeners == null)
    {
      return;
    }
    listDataListeners.remove(l);
  }

  private static Properties defaultEncodings;

  private static Properties getDefaultEncodings ()
  {
    if (defaultEncodings == null)
    {
      defaultEncodings = new Properties();
      // basic encoding set, base encodings
      defaultEncodings.put("ASCII", "American Standard Code for Information Interchange");
      defaultEncodings.put("Cp1252", "Windows Latin-1");
      defaultEncodings.put("ISO-8859-1", "Latin alphabet No. 1");
      defaultEncodings.put("ISO-8859-15", "Latin alphabet No. 9, 'Euro' enabled");
      defaultEncodings.put("UTF-8", "8 Bit UCS Transformation Format");
      defaultEncodings.put("UTF-16", "16 Bit UCS Transformation Format");
      // missing: UTF-16BE, UTF-16LE are no base need in EndUser environments

      //extended encoding set, contained in lib/charsets.jar
      defaultEncodings.put("Cp1250", "Windows Eastern Europe");
      defaultEncodings.put("Cp1253", "Windows Greek");
      defaultEncodings.put("Cp1254", "Windows Turkish");
      defaultEncodings.put("Cp1255", "Windows Hebrew");
      defaultEncodings.put("Cp1256", "Windows Arabic");
      defaultEncodings.put("Cp1257", "Windows Baltic");
      defaultEncodings.put("Cp1258", "Windows Vietnamese");
      defaultEncodings.put("ISO-8859-2", "Latin alphabet No. 2");
      defaultEncodings.put("ISO-8859-3", "Latin alphabet No. 3");
      defaultEncodings.put("ISO-8859-4", "Latin alphabet No. 4");
      defaultEncodings.put("ISO-8859-5", "Latin/Cyrillic Alphabet");
      defaultEncodings.put("ISO-8859-6", "Latin/Arabic Alphabet");
      defaultEncodings.put("ISO-8859-7", "Latin/Greek Alphabet");
      defaultEncodings.put("ISO-8859-8", "Latin/Hebrew Alphabet");
      defaultEncodings.put("ISO-8859-9", "Latin alphabet No. 5");
      defaultEncodings.put("ISO-8859-13", "Latin alphabet No. 7");
      defaultEncodings.put("MS932", "Windows Japanese");
      defaultEncodings.put("EUC-JP", "JISX 0201, 0208 and 0212, EUC encoding Japanese");
      defaultEncodings.put("EUC-JP-LINUX", "JISX 0201, 0208, EUC encoding Japanese");
      defaultEncodings.put("SJIS", "Shift-JIS, Japanese");
      defaultEncodings.put("ISO-2022-JP", "JIS X 0201, 0208, in ISO 2002 form, Japanese");
      defaultEncodings.put("MS936", "Windows Simplified Chinese");
      defaultEncodings.put("GB18030", "Simplified Chinese, PRC standard");
      defaultEncodings.put("EUC_CN", "GB2312, EUC encoding, Simplified Chinese");
      defaultEncodings.put("GB2312", "GB2312, EUC encoding, Simplified Chinese");
      defaultEncodings.put("GBK", "GBK, Simplified Chinese");
      defaultEncodings.put("ISCII91", "ISCII encoding of Indic scripts");
      defaultEncodings.put("ISO-2022-CN-GB", "GB2312 in ISO 2022 CN form, Simplified Chinese");
      defaultEncodings.put("MS949", "Windows Korean");
      defaultEncodings.put("EUC_KR", "KS C 5601, EUC encoding, Korean");
      defaultEncodings.put("ISO-2022-KR", "ISO 2022 KR, Korean");
      defaultEncodings.put("MS950", "Windows Traditional Chinese");
      defaultEncodings.put("EUC-TW", "CNS 11643 (Plane 1-3), EUC encoding, Traditional Chinese");
      defaultEncodings.put("ISO-2022-CN-CNS", "CNS 11643 in ISO 2022 CN form, Traditional Chinese");
      defaultEncodings.put("Big5", "Big5, Traditional Chinese");
      defaultEncodings.put("Big5-HKSCS", "Big5 with Hong Kong extensions, Traditional Chinese");
      defaultEncodings.put("TIS-620", "TIS 620, Thai");
      defaultEncodings.put("KOI8-R", "KOI8-R, Russian");

      //extended encoding set, contained in lib/charsets.jar
      defaultEncodings.put("Big5_Solaris",
                      "Big5 with seven additional Hanzi ideograph character mappings");
      defaultEncodings.put("Cp037",
                      "USA, Canada (Bilingual, French), Netherlands, Portugal, Brazil, Australia");
      defaultEncodings.put("Cp273", "IBM Austria, Germany");
      defaultEncodings.put("Cp277", "IBM Denmark, Norway");
      defaultEncodings.put("Cp278", "IBM Finland, Sweden");
      defaultEncodings.put("Cp280", "IBM Italy");
      defaultEncodings.put("Cp284", "IBM Catalan/Spain, Spanish Latin America");
      defaultEncodings.put("Cp285", "IBM United Kingdom, Ireland");
      defaultEncodings.put("Cp297", "IBM France");
      defaultEncodings.put("Cp420", "IBM Arabic");
      defaultEncodings.put("Cp424", "IBM Hebrew");
      defaultEncodings.put("Cp437", "MS-DOS United States, Australia, New Zealand, South Africa");
      defaultEncodings.put("Cp500", "EBCDIC 500V1");
      defaultEncodings.put("Cp737", "PC Greek");
      defaultEncodings.put("Cp775", "PC Baltik");
      defaultEncodings.put("Cp838", "IBM Thailand extended SBCS");
      defaultEncodings.put("Cp850", "MS-DOS Latin-1");
      defaultEncodings.put("Cp852", "MS-DOS Latin 2");
      defaultEncodings.put("Cp855", "IBM Cyrillic");
      defaultEncodings.put("Cp856", "IBM Hebrew");
      defaultEncodings.put("Cp857", "IBM Turkish");
      defaultEncodings.put("Cp858", "MS-DOS Latin-1 with Euro character");
      defaultEncodings.put("Cp860", "MS-DOS Portuguese");
      defaultEncodings.put("Cp861", "MS-DOS Icelandic");
      defaultEncodings.put("Cp862", "PC Hebrew");
      defaultEncodings.put("Cp863", "MS-DOS Canadian French");
      defaultEncodings.put("Cp864", "PC Arabic");
      defaultEncodings.put("Cp865", "MS-DOS Nordic");
      defaultEncodings.put("Cp866", "MS-DOS Russian");
      defaultEncodings.put("Cp868", "MS-DOS Pakistan");
      defaultEncodings.put("Cp869", "IBM Modern Greek");
      defaultEncodings.put("Cp870", "IBM Multilingual Latin-2");
      defaultEncodings.put("Cp871", "IBM Iceland");
      defaultEncodings.put("Cp874", "IBM Thai");
      defaultEncodings.put("Cp875", "IBM Greek");
      defaultEncodings.put("Cp918", "IBM Pakistan (Urdu)");
      defaultEncodings.put("Cp921", "IBM Lativa, Lithuania (AIX, DOS)");
      defaultEncodings.put("Cp922", "IBM Estonia (AIX, DOS)");
      defaultEncodings.put("Cp930", "Japanese Katakana-Kanji mixed with 4370 UDC, superset of 5026");
      defaultEncodings.put("Cp933", "Korean mixed with 1880 UDC, superset of 5029");
      defaultEncodings.put("Cp935", "Simplified Chinese mixed with 1880 UDC, superset of 5031");
      defaultEncodings.put("Cp937", "Traditional Chinsese Hostmixed with 6204 UDC, superset of 5033");
      defaultEncodings.put("Cp939", "Japanese Latin Kanji mixed with 4370 UDC, superset of 5035");
      defaultEncodings.put("Cp942", "IBM OS/2 Japanese, superset of Cp932");
      defaultEncodings.put("Cp942C", "Variant of Cp942: IBM OS/2 Japanese, superset of Cp932");
      defaultEncodings.put("Cp943", "IBM OS/2 Japanese, superset of Cp932 and Shift-JIS");
      defaultEncodings.put("Cp943C",
                      "Variant of Cp943: IBM OS/2 Japanese, superset of Cp932 and Shift-JIS");
      defaultEncodings.put("Cp948", "IBM OS/2 Chinese (Taiwan) superset of Cp938");
      defaultEncodings.put("Cp949", "PC Korean");
      defaultEncodings.put("Cp949C", "Variant of Cp949: PC Korean");
      defaultEncodings.put("Cp950", "PC Chinese (Hong Kong, Taiwan)");
      defaultEncodings.put("Cp964", "AIX Chinese (Taiwan)");
      defaultEncodings.put("Cp970", "AIX Korean");
      defaultEncodings.put("Cp1006", "IBM AIX Parkistan (Urdu)");
      defaultEncodings.put("Cp1025",
                      "IBM Multilingual Cyrillic: Bulgaria, Bosnia, Herzegovinia, Macedonia (FYR)");
      defaultEncodings.put("Cp1026", "IBM Latin-5, Turkey");
      defaultEncodings.put("Cp1046", "IBM Arabic Windows");
      defaultEncodings.put("Cp1097", "IBM Iran (Farsi)/Persian");
      defaultEncodings.put("Cp1098", "IBM Iran (Farsi)/Persian (PC)");
      defaultEncodings.put("Cp1112", "IBM Lativa, Lithuania");
      defaultEncodings.put("Cp1122", "IBM Estonia");
      defaultEncodings.put("Cp1123", "IBM Ukraine");
      defaultEncodings.put("Cp1124", "IBM AIX Ukraine");
      defaultEncodings.put("Cp1140",
          "USA, Canada (Bilingual, French), Netherlands, Portugal, Brazil, Australia (with Euro)");
      defaultEncodings.put("Cp1141", "IBM Austria, Germany (Euro enabled)");
      defaultEncodings.put("Cp1142", "IBM Denmark, Norway (Euro enabled)");
      defaultEncodings.put("Cp1143", "IBM Finland, Sweden (Euro enabled)");
      defaultEncodings.put("Cp1144", "IBM Italy (Euro enabled)");
      defaultEncodings.put("Cp1145", "IBM Catalan/Spain, Spanish Latin America (with Euro)");
      defaultEncodings.put("Cp1146", "IBM United Kingdom, Ireland (with Euro)");
      defaultEncodings.put("Cp1147", "IBM France (with Euro)");
      defaultEncodings.put("Cp1148", "IBM EBCDIC 500V1 (with Euro)");
      defaultEncodings.put("Cp1149", "IBM Iceland (with Euro)");
      defaultEncodings.put("Cp1381", "IBM OS/2, DOS People's Republic of Chine (PRC)");
      defaultEncodings.put("Cp1383", "IBM AIX People's Republic of Chine (PRC)");
      defaultEncodings.put("Cp33722", "IBM-eucJP - Japanese (superset of 5050)");
      defaultEncodings.put("MS874", "Windows Thai");
      defaultEncodings.put("MacArabic", "Macintosh Arabic");
      defaultEncodings.put("MacCentralEurope", "Macintosh Latin-2");
      defaultEncodings.put("MacCroatian", "Macintosh Croatian");
      defaultEncodings.put("MacCyrillic", "Macintosh Cyrillic");
      defaultEncodings.put("MacDingbat", "Macintosh Dingbat");
      defaultEncodings.put("MacGreek", "Macintosh Greek");
      defaultEncodings.put("MacHebrew", "Macintosh Hebrew");
      defaultEncodings.put("MacIceland", "Macintosh Iceland");
      defaultEncodings.put("MacRoman", "Macintosh Roman");
      defaultEncodings.put("MacRomania", "Macintosh Romania");
      defaultEncodings.put("MacSymbol", "Macintosh Symbol");
      defaultEncodings.put("MacThai", "Macintosh Thai");
      defaultEncodings.put("MacTurkish", "Macintosh Turkish");
      defaultEncodings.put("MacUkraine", "Macintosh Ukraine");
    }
    return defaultEncodings;
  }

  /**
   * Creates a default model containing a selection of encodings.
   *
   * @return The default model.
   */
  public static EncodingComboBoxModel createDefaultModel ()
  {
    EncodingComboBoxModel ecb = new EncodingComboBoxModel();

    String availEncs = ReportConfiguration.getGlobalConfig().getAvailableEncodings();

    if (availEncs.equalsIgnoreCase(ReportConfiguration.AVAILABLE_ENCODINGS_ALL))
    {
      Properties encodings = getDefaultEncodings();
      Enumeration enum = encodings.keys();
      while (enum.hasMoreElements())
      {
        String enc = (String) enum.nextElement();
        // if not set to "true"
        if (encodings.getProperty(enc, "false").equalsIgnoreCase("true"))
        {
          // if the encoding is disabled ...
          ecb.addEncoding(enc, defaultEncodings.getProperty(enc, ENCODING_DEFAULT_DESCRIPTION));
        }
      }
    }
    else if (availEncs.equals(ReportConfiguration.AVAILABLE_ENCODINGS_FILE))
    {
      String encFile = ReportConfiguration.getGlobalConfig().getEncodingsDefinitionFile();
      InputStream in = ecb.getClass().getResourceAsStream(encFile);
      if (in == null)
      {
        Log.warn (new com.jrefinery.util.Log.SimpleMessage
            ("The specified encodings definition file was not found: ", encFile));
      }
      else
      {
        try
        {
          Properties defaultEncodings = getDefaultEncodings();
          Properties encDef = new Properties();
          BufferedInputStream bin = new BufferedInputStream(in);
          encDef.load(bin);
          bin.close();
          Enumeration enum = encDef.keys();
          while (enum.hasMoreElements())
          {
            String enc = (String) enum.nextElement();
            // if not set to "true"
            if (encDef.getProperty(enc, "false").equalsIgnoreCase("true"))
            {
              // if the encoding is disabled ...
              ecb.addEncoding(enc, defaultEncodings.getProperty(enc, ENCODING_DEFAULT_DESCRIPTION));
            }
          }
        }
        catch (IOException e)
        {
          Log.warn (new com.jrefinery.util.Log.SimpleMessage
              ("There was an error while reading the encodings definition file: ", encFile), e);
        }
      }
    }
    return ecb;
  }

  /**
   * Returns the index of an encoding.
   *
   * @param encoding  the encoding (name).
   *
   * @return The index.
   */
  public int indexOf(String encoding)
  {
    return encodings.indexOf(new EncodingCarrier(encoding, null));
  }

  /**
   * Returns an encoding.
   *
   * @param index  the index.
   *
   * @return The index.
   */
  public String getEncoding (int index)
  {
    EncodingCarrier ec = (EncodingCarrier) encodings.get(index);
    return ec.getName();
  }

  /**
   * Returns a description.
   *
   * @param index  the index.
   *
   * @return The description.
   */
  public String getDescription (int index)
  {
    EncodingCarrier ec = (EncodingCarrier) encodings.get(index);
    return ec.getDescription();
  }
}

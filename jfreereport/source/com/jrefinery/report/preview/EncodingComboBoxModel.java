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
 * $Id: EncodingComboBoxModel.java,v 1.13 2003/04/05 18:57:12 taqua Exp $
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

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jrefinery.report.util.EncodingSupport;

/**
 * A model for the 'encoding' combo box.
 * <p>
 * This model is used in the {@link CSVExportDialog} class (and possibly others).
 *
 * @author Thomas Morgner.
 */
public class EncodingComboBoxModel implements ComboBoxModel
{
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

  /**
   * Creates a default model containing a selection of encodings.
   *
   * @return The default model.
   */
  public static EncodingComboBoxModel createDefaultModel ()
  {
    EncodingComboBoxModel ecb = new EncodingComboBoxModel();
    // basic encoding set, base encodings
    ecb.addEncoding("ASCII", "American Standard Code for Information Interchange");
    ecb.addEncoding("Cp1252", "Windows Latin-1");
    ecb.addEncoding("ISO-8859-1", "Latin alphabet No. 1");
    ecb.addEncoding("ISO-8859-15", "Latin alphabet No. 9, 'Euro' enabled");
    ecb.addEncoding("UTF-8", "8 Bit UCS Transformation Format");
    ecb.addEncoding("UTF-16", "16 Bit UCS Transformation Format");
    // missing: UTF-16BE, UTF-16LE are no base need in EndUser environments
    //extended encoding set, contained in lib/charsets.jar
    ecb.addEncoding("Cp1250", "Windows Eastern Europe");
    ecb.addEncoding("Cp1253", "Windows Greek");
    ecb.addEncoding("Cp1254", "Windows Turkish");
    ecb.addEncoding("Cp1255", "Windows Hebrew");
    ecb.addEncoding("Cp1256", "Windows Arabic");
    ecb.addEncoding("Cp1257", "Windows Baltic");
    ecb.addEncoding("Cp1258", "Windows Vietnamese");
    ecb.addEncoding("ISO-8859-2", "Latin alphabet No. 2");
    ecb.addEncoding("ISO-8859-3", "Latin alphabet No. 3");
    ecb.addEncoding("ISO-8859-4", "Latin alphabet No. 4");
    ecb.addEncoding("ISO-8859-5", "Latin/Cyrillic Alphabet");
    ecb.addEncoding("ISO-8859-6", "Latin/Arabic Alphabet");
    ecb.addEncoding("ISO-8859-7", "Latin/Greek Alphabet");
    ecb.addEncoding("ISO-8859-8", "Latin/Hebrew Alphabet");
    ecb.addEncoding("ISO-8859-9", "Latin alphabet No. 5");
    ecb.addEncoding("ISO-8859-13", "Latin alphabet No. 7");
    ecb.addEncoding("MS932", "Windows Japanese");
    ecb.addEncoding("EUC-JP", "JISX 0201, 0208 and 0212, EUC encoding Japanese");
    ecb.addEncoding("EUC-JP-LINUX", "JISX 0201, 0208, EUC encoding Japanese");
    ecb.addEncoding("SJIS", "Shift-JIS, Japanese");
    ecb.addEncoding("ISO-2022-JP", "JIS X 0201, 0208, in ISO 2002 form, Japanese");
    ecb.addEncoding("MS936", "Windows Simplified Chinese");
    ecb.addEncoding("GB18030", "Simplified Chinese, PRC standard");
    ecb.addEncoding("EUC_CN", "GB2312, EUC encoding, Simplified Chinese");
    ecb.addEncoding("GB2312", "GB2312, EUC encoding, Simplified Chinese");
    ecb.addEncoding("GBK", "GBK, Simplified Chinese");
    ecb.addEncoding("ISCII91", "ISCII encoding of Indic scripts");
    ecb.addEncoding("ISO-2022-CN-GB", "GB2312 in ISO 2022 CN form, Simplified Chinese");
    ecb.addEncoding("MS949", "Windows Korean");
    ecb.addEncoding("EUC_KR", "KS C 5601, EUC encoding, Korean");
    ecb.addEncoding("ISO-2022-KR", "ISO 2022 KR, Korean");
    ecb.addEncoding("MS950", "Windows Traditional Chinese");
    ecb.addEncoding("EUC-TW", "CNS 11643 (Plane 1-3), EUC encoding, Traditional Chinese");
    ecb.addEncoding("ISO-2022-CN-CNS", "CNS 11643 in ISO 2022 CN form, Traditional Chinese");
    ecb.addEncoding("Big5", "Big5, Traditional Chinese");
    ecb.addEncoding("Big5-HKSCS", "Big5 with Hong Kong extensions, Traditional Chinese");
    ecb.addEncoding("TIS-620", "TIS 620, Thai");
    ecb.addEncoding("KOI8-R", "KOI8-R, Russian");

    //extended encoding set, contained in lib/charsets.jar
    ecb.addEncoding("Big5_Solaris",
                    "Big5 with seven additional Hanzi ideograph character mappings");
    ecb.addEncoding("Cp037",
                    "USA, Canada (Bilingual, French), Netherlands, Portugal, Brazil, Australia");
    ecb.addEncoding("Cp273", "IBM Austria, Germany");
    ecb.addEncoding("Cp277", "IBM Denmark, Norway");
    ecb.addEncoding("Cp278", "IBM Finland, Sweden");
    ecb.addEncoding("Cp280", "IBM Italy");
    ecb.addEncoding("Cp284", "IBM Catalan/Spain, Spanish Latin America");
    ecb.addEncoding("Cp285", "IBM United Kingdom, Ireland");
    ecb.addEncoding("Cp297", "IBM France");
    ecb.addEncoding("Cp420", "IBM Arabic");
    ecb.addEncoding("Cp424", "IBM Hebrew");
    ecb.addEncoding("Cp437", "MS-DOS United States, Australia, New Zealand, South Africa");
    ecb.addEncoding("Cp500", "EBCDIC 500V1");
    ecb.addEncoding("Cp737", "PC Greek");
    ecb.addEncoding("Cp775", "PC Baltik");
    ecb.addEncoding("Cp838", "IBM Thailand extended SBCS");
    ecb.addEncoding("Cp850", "MS-DOS Latin-1");
    ecb.addEncoding("Cp852", "MS-DOS Latin 2");
    ecb.addEncoding("Cp855", "IBM Cyrillic");
    ecb.addEncoding("Cp856", "IBM Hebrew");
    ecb.addEncoding("Cp857", "IBM Turkish");
    ecb.addEncoding("Cp858", "MS-DOS Latin-1 with Euro character");
    ecb.addEncoding("Cp860", "MS-DOS Portuguese");
    ecb.addEncoding("Cp861", "MS-DOS Icelandic");
    ecb.addEncoding("Cp862", "PC Hebrew");
    ecb.addEncoding("Cp863", "MS-DOS Canadian French");
    ecb.addEncoding("Cp864", "PC Arabic");
    ecb.addEncoding("Cp865", "MS-DOS Nordic");
    ecb.addEncoding("Cp866", "MS-DOS Russian");
    ecb.addEncoding("Cp868", "MS-DOS Pakistan");
    ecb.addEncoding("Cp869", "IBM Modern Greek");
    ecb.addEncoding("Cp870", "IBM Multilingual Latin-2");
    ecb.addEncoding("Cp871", "IBM Iceland");
    ecb.addEncoding("Cp874", "IBM Thai");
    ecb.addEncoding("Cp875", "IBM Greek");
    ecb.addEncoding("Cp918", "IBM Pakistan (Urdu)");
    ecb.addEncoding("Cp921", "IBM Lativa, Lithuania (AIX, DOS)");
    ecb.addEncoding("Cp922", "IBM Estonia (AIX, DOS)");
    ecb.addEncoding("Cp930", "Japanese Katakana-Kanji mixed with 4370 UDC, superset of 5026");
    ecb.addEncoding("Cp933", "Korean mixed with 1880 UDC, superset of 5029");
    ecb.addEncoding("Cp935", "Simplified Chinese mixed with 1880 UDC, superset of 5031");
    ecb.addEncoding("Cp937", "Traditional Chinsese Hostmixed with 6204 UDC, superset of 5033");
    ecb.addEncoding("Cp939", "Japanese Latin Kanji mixed with 4370 UDC, superset of 5035");
    ecb.addEncoding("Cp942", "IBM OS/2 Japanese, superset of Cp932");
    ecb.addEncoding("Cp942C", "Variant of Cp942: IBM OS/2 Japanese, superset of Cp932");
    ecb.addEncoding("Cp943", "IBM OS/2 Japanese, superset of Cp932 and Shift-JIS");
    ecb.addEncoding("Cp943C",
                    "Variant of Cp943: IBM OS/2 Japanese, superset of Cp932 and Shift-JIS");
    ecb.addEncoding("Cp948", "IBM OS/2 Chinese (Taiwan) superset of Cp938");
    ecb.addEncoding("Cp949", "PC Korean");
    ecb.addEncoding("Cp949C", "Variant of Cp949: PC Korean");
    ecb.addEncoding("Cp950", "PC Chinese (Hong Kong, Taiwan)");
    ecb.addEncoding("Cp964", "AIX Chinese (Taiwan)");
    ecb.addEncoding("Cp970", "AIX Korean");
    ecb.addEncoding("Cp1006", "IBM AIX Parkistan (Urdu)");
    ecb.addEncoding("Cp1025",
                    "IBM Multilingual Cyrillic: Bulgaria, Bosnia, Herzegovinia, Macedonia (FYR)");
    ecb.addEncoding("Cp1026", "IBM Latin-5, Turkey");
    ecb.addEncoding("Cp1046", "IBM Arabic Windows");
    ecb.addEncoding("Cp1097", "IBM Iran (Farsi)/Persian");
    ecb.addEncoding("Cp1098", "IBM Iran (Farsi)/Persian (PC)");
    ecb.addEncoding("Cp1112", "IBM Lativa, Lithuania");
    ecb.addEncoding("Cp1122", "IBM Estonia");
    ecb.addEncoding("Cp1123", "IBM Ukraine");
    ecb.addEncoding("Cp1124", "IBM AIX Ukraine");
    ecb.addEncoding("Cp1140",
        "USA, Canada (Bilingual, French), Netherlands, Portugal, Brazil, Australia (with Euro)");
    ecb.addEncoding("Cp1141", "IBM Austria, Germany (Euro enabled)");
    ecb.addEncoding("Cp1142", "IBM Denmark, Norway (Euro enabled)");
    ecb.addEncoding("Cp1143", "IBM Finland, Sweden (Euro enabled)");
    ecb.addEncoding("Cp1144", "IBM Italy (Euro enabled)");
    ecb.addEncoding("Cp1145", "IBM Catalan/Spain, Spanish Latin America (with Euro)");
    ecb.addEncoding("Cp1146", "IBM United Kingdom, Ireland (with Euro)");
    ecb.addEncoding("Cp1147", "IBM France (with Euro)");
    ecb.addEncoding("Cp1148", "IBM EBCDIC 500V1 (with Euro)");
    ecb.addEncoding("Cp1149", "IBM Iceland (with Euro)");
    ecb.addEncoding("Cp1381", "IBM OS/2, DOS People's Republic of Chine (PRC)");
    ecb.addEncoding("Cp1383", "IBM AIX People's Republic of Chine (PRC)");
    ecb.addEncoding("Cp33722", "IBM-eucJP - Japanese (superset of 5050)");
    ecb.addEncoding("MS874", "Windows Thai");
    ecb.addEncoding("MacArabic", "Macintosh Arabic");
    ecb.addEncoding("MacCentralEurope", "Macintosh Latin-2");
    ecb.addEncoding("MacCroatian", "Macintosh Croatian");
    ecb.addEncoding("MacCyrillic", "Macintosh Cyrillic");
    ecb.addEncoding("MacDingbat", "Macintosh Dingbat");
    ecb.addEncoding("MacGreek", "Macintosh Greek");
    ecb.addEncoding("MacHebrew", "Macintosh Hebrew");
    ecb.addEncoding("MacIceland", "Macintosh Iceland");
    ecb.addEncoding("MacRoman", "Macintosh Roman");
    ecb.addEncoding("MacRomania", "Macintosh Romania");
    ecb.addEncoding("MacSymbol", "Macintosh Symbol");
    ecb.addEncoding("MacThai", "Macintosh Thai");
    ecb.addEncoding("MacTurkish", "Macintosh Turkish");
    ecb.addEncoding("MacUkraine", "Macintosh Ukraine");
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

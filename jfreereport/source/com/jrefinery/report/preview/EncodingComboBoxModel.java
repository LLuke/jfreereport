/**
 * Date: Jan 21, 2003
 * Time: 7:54:06 PM
 *
 * $Id: EncodingComboBoxModel.java,v 1.1 2003/01/22 19:45:28 taqua Exp $
 */
package com.jrefinery.report.preview;

import com.jrefinery.report.util.Log;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class EncodingComboBoxModel implements ComboBoxModel
{
  private static class EncodingCarrierComparator implements Comparator
  {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     *
     * The implementor must ensure that <tt>sgn(compare(x, y)) ==
     * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>compare(x, y)</tt> must throw an exception if and only
     * if <tt>compare(y, x)</tt> throws an exception.)<p>
     *
     * The implementor must also ensure that the relation is transitive:
     * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
     * <tt>compare(x, z)&gt;0</tt>.<p>
     *
     * Finally, the implementer must ensure that <tt>compare(x, y)==0</tt>
     * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
     * <tt>z</tt>.<p>
     *
     * It is generally the case, but <i>not</i> strictly required that
     * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
     * any comparator that violates this condition should clearly indicate
     * this fact.  The recommended language is "Note: this comparator
     * imposes orderings that are inconsistent with equals."
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * 	       first argument is less than, equal to, or greater than the
     *	       second.
     * @throws ClassCastException if the arguments' types prevent them from
     * 	       being compared by this Comparator.
     */
    public int compare(Object o1, Object o2)
    {
      EncodingCarrier e1 = (EncodingCarrier) o1;
      EncodingCarrier e2 = (EncodingCarrier) o2;
      return e1.getName().compareTo(e2.getName());
    }

    public boolean equals (Object o)
    {
      if (o == null) return false;
      return getClass().equals(o.getClass());
    }
  }

  private static class EncodingCarrier
  {
    private String name;
    private String description;

    public EncodingCarrier(String name, String description)
    {
      if (name == null) throw new NullPointerException();
      this.name = name;
      this.description = description;
    }

    public String getName()
    {
      return name;
    }

    public String getDescription()
    {
      return description;
    }

    public String getDisplayName ()
    {
      return name + " (" + description + ")";
    }

    public boolean equals(Object o)
    {
      if (this == o) return true;
      if (!(o instanceof EncodingCarrier)) return false;

      final EncodingCarrier carrier = (EncodingCarrier) o;

      if (!name.equals(carrier.name)) return false;

      return true;
    }

    public int hashCode()
    {
      return name.hashCode();
    }
  }

  private ArrayList encodings;
  private ArrayList listDataListeners;
  private int selectedIndex;
  private Object selectedObject;

  public EncodingComboBoxModel()
  {
    encodings = new ArrayList();
    listDataListeners = new ArrayList();
    selectedIndex = -1;
  }

  public boolean addEncoding (String name, String description)
  {
    try
    {
      new String (" ").getBytes(name);
      encodings.add (new EncodingCarrier(name, description));

      fireContentsChanged();
      return true;
    }
    catch (Exception e)
    {
      Log.debug ("Encoding " + name + " is not supported on this system.");
      return false;
    }
  }

  public void sort ()
  {
    Collections.sort(encodings, new EncodingCarrierComparator());
    fireContentsChanged();
  }

  protected void fireContentsChanged ()
  {
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

  public int getSelectedIndex()
  {
    return selectedIndex;
  }

  public String getSelectedEncoding ()
  {
    if (selectedIndex == -1)
      return null;

    EncodingCarrier ec = (EncodingCarrier) encodings.get(selectedIndex);
    return ec.getName();
  }

  /**
   * Returns the selected item
   * @return The selected item or <code>null</code> if there is no selection
   */
  public Object getSelectedItem()
  {
    return selectedObject;
  }

  /**
   * Returns the length of the list.
   * @return the length of the list
   */
  public int getSize()
  {
    return encodings.size();
  }

  /**
   * Returns the value at the specified index.
   * @param index the requested index
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
   * @param l the <code>ListDataListener</code> to be added
   */
  public void addListDataListener(ListDataListener l)
  {
    listDataListeners.add (l);
  }

  /**
   * Removes a listener from the list that's notified each time a
   * change to the data model occurs.
   * @param l the <code>ListDataListener</code> to be removed
   */
  public void removeListDataListener(ListDataListener l)
  {
    listDataListeners.remove(l);
  }

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
    ecb.addEncoding("Big5_Solaris", "Big5 with seven additional Hanzi ideograph character mappings");
    ecb.addEncoding("Cp037", "USA, Canada (Bilingual, French), Netherlands, Portugal, Brazil, Australia");
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
    ecb.addEncoding("Cp943C", "Variant of Cp943: IBM OS/2 Japanese, superset of Cp932 and Shift-JIS");
    ecb.addEncoding("Cp948", "IBM OS/2 Chinese (Taiwan) superset of Cp938");
    ecb.addEncoding("Cp949", "PC Korean");
    ecb.addEncoding("Cp949C", "Variant of Cp949: PC Korean");
    ecb.addEncoding("Cp950", "PC Chinese (Hong Kong, Taiwan)");
    ecb.addEncoding("Cp964", "AIX Chinese (Taiwan)");
    ecb.addEncoding("Cp970", "AIX Korean");
    ecb.addEncoding("Cp1006", "IBM AIX Parkistan (Urdu)");
    ecb.addEncoding("Cp1025", "IBM Multilingual Cyrillic: Bulgaria, Bosnia, Herzegovinia, Macedonia (FYR)");
    ecb.addEncoding("Cp1026", "IBM Latin-5, Turkey");
    ecb.addEncoding("Cp1046", "IBM Arabic Windows");
    ecb.addEncoding("Cp1097", "IBM Iran (Farsi)/Persian");
    ecb.addEncoding("Cp1098", "IBM Iran (Farsi)/Persian (PC)");
    ecb.addEncoding("Cp1112", "IBM Lativa, Lithuania");
    ecb.addEncoding("Cp1122", "IBM Estonia");
    ecb.addEncoding("Cp1123", "IBM Ukraine");
    ecb.addEncoding("Cp1124", "IBM AIX Ukraine");
    ecb.addEncoding("Cp1140", "USA, Canada (Bilingual, French), Netherlands, Portugal, Brazil, Australia (with Euro)");
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

  public int indexOf(String encoding)
  {
    return encodings.indexOf(new EncodingCarrier(encoding, null));
  }

  public String getEncoding (int index)
  {
    EncodingCarrier ec = (EncodingCarrier) encodings.get(index);
    return ec.getName();
  }

  public static void main (String [] args)
  {
    createDefaultModel();
  }
}

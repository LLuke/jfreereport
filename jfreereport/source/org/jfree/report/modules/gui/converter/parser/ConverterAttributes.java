/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ConverterAttributes.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ConverterAttributes.java,v 1.4 2004/05/07 14:29:46 mungady Exp $
 *
 * Changes
 * -------------------------
 * 25-Aug-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.converter.parser;

import org.xml.sax.Attributes;

/**
 * An Attribute implementation that uses a translation table to map given attribute values
 * into the new value-space.
 *
 * @author Thomas Morgner
 */
public class ConverterAttributes implements Attributes
{
  /**
   * The attributes from the XML parser are used as backend.
   */
  private Attributes base;
  /**
   * The translation table that translates the attribute values.
   */
  private TranslationTable translationTable;

  /**
   * Creates a new ConverterAttribute set for the given attributes and the specified
   * translation table.
   *
   * @param base             the attributes from the XML parser
   * @param translationTable the translation table.
   */
  public ConverterAttributes (final Attributes base,
                              final TranslationTable translationTable)
  {
    if (base == null)
    {
      throw new NullPointerException("Base attributes are null.");
    }
    if (translationTable == null)
    {
      throw new NullPointerException("TranslationTable is null.");
    }
    this.base = base;
    this.translationTable = translationTable;
  }


  /**
   * Look up an attribute's value by index.
   * <p/>
   * <p>If the attribute value is a list of tokens (IDREFS, ENTITIES, or NMTOKENS), the
   * tokens will be concatenated into a single string with each token separated by a
   * single space.</p>
   *
   * @param index The attribute index (zero-based).
   * @return The attribute's value as a string, or null if the index is out of range.
   *
   * @see #getLength
   */
  public String getValue (final int index)
  {
    final String localName = getLocalName(index);
    return translationTable.translateAttribute(localName, base.getValue(index));
  }


  /**
   * Look up an attribute's value by Namespace name.
   * <p/>
   * <p>See {@link #getValue(int) getValue(int)} for a description of the possible
   * values.</p>
   *
   * @param uri       The Namespace URI, or the empty String if the name has no Namespace
   *                  URI.
   * @param localName The local name of the attribute.
   * @return The attribute value as a string, or null if the attribute is not in the
   *         list.
   */
  public String getValue (final String uri, final String localName)
  {
    // local name already given ...
    return translationTable.translateAttribute(localName, base.getValue(uri, localName));
  }

  /**
   * Look up an attribute's value by XML 1.0 qualified name.
   * <p/>
   * <p>See {@link #getValue(int) getValue(int)} for a description of the possible
   * values.</p>
   *
   * @param qName The XML 1.0 qualified name.
   * @return The attribute value as a string, or null if the attribute is not in the list
   *         or if qualified names are not available.
   */
  public String getValue (final String qName)
  {
    return translationTable.translateAttribute(qName, base.getValue(qName));
  }

  /**
   * Return the number of attributes in the list.
   * <p/>
   * <p>Once you know the number of attributes, you can iterate through the list.</p>
   *
   * @return The number of attributes in the list.
   *
   * @see #getURI(int)
   * @see #getLocalName(int)
   * @see #getQName(int)
   * @see #getType(int)
   * @see #getValue(int)
   */
  public int getLength ()
  {
    return base.getLength();
  }

  /**
   * Look up an attribute's Namespace URI by index.
   *
   * @param index The attribute index (zero-based).
   * @return The Namespace URI, or the empty string if none is available, or null if the
   *         index is out of range.
   *
   * @see #getLength
   */
  public String getURI (final int index)
  {
    return base.getURI(index);
  }

  /**
   * Look up an attribute's local name by index.
   *
   * @param index The attribute index (zero-based).
   * @return The local name, or the empty string if Namespace processing is not being
   *         performed, or null if the index is out of range.
   *
   * @see #getLength
   */
  public String getLocalName (final int index)
  {
    return base.getLocalName(index);
  }

  /**
   * Look up an attribute's XML 1.0 qualified name by index.
   *
   * @param index The attribute index (zero-based).
   * @return The XML 1.0 qualified name, or the empty string if none is available, or null
   *         if the index is out of range.
   *
   * @see #getLength
   */
  public String getQName (final int index)
  {
    return base.getQName(index);
  }

  /**
   * Look up an attribute's type by index.
   * <p/>
   * <p>The attribute type is one of the strings "CDATA", "ID", "IDREF", "IDREFS",
   * "NMTOKEN", "NMTOKENS", "ENTITY", "ENTITIES", or "NOTATION" (always in upper
   * case).</p>
   * <p/>
   * <p>If the parser has not read a declaration for the attribute, or if the parser does
   * not report attribute types, then it must return the value "CDATA" as stated in the
   * XML 1.0 Recommentation (clause 3.3.3, "Attribute-Value Normalization").</p>
   * <p/>
   * <p>For an enumerated attribute that is not a notation, the parser will report the
   * type as "NMTOKEN".</p>
   *
   * @param index The attribute index (zero-based).
   * @return The attribute's type as a string, or null if the index is out of range.
   *
   * @see #getLength
   */
  public String getType (final int index)
  {
    return base.getType(index);
  }

  /**
   * Look up the index of an attribute by Namespace name.
   *
   * @param uri       The Namespace URI, or the empty string if the name has no Namespace
   *                  URI.
   * @param localName The attribute's local name.
   * @return The index of the attribute, or -1 if it does not appear in the list.
   */
  public int getIndex (final String uri, final String localName)
  {
    return base.getIndex(uri, localName);
  }

  /**
   * Look up the index of an attribute by XML 1.0 qualified name.
   *
   * @param qName The qualified (prefixed) name.
   * @return The index of the attribute, or -1 if it does not appear in the list.
   */
  public int getIndex (final String qName)
  {
    return base.getIndex(qName);
  }

  /**
   * Look up an attribute's type by Namespace name.
   * <p/>
   * <p>See {@link #getType(int) getType(int)} for a description of the possible
   * types.</p>
   *
   * @param uri       The Namespace URI, or the empty String if the name has no Namespace
   *                  URI.
   * @param localName The local name of the attribute.
   * @return The attribute type as a string, or null if the attribute is not in the list
   *         or if Namespace processing is not being performed.
   */
  public String getType (final String uri, final String localName)
  {
    return getType(uri, localName);
  }

  /**
   * Look up an attribute's type by XML 1.0 qualified name.
   * <p/>
   * <p>See {@link #getType(int) getType(int)} for a description of the possible
   * types.</p>
   *
   * @param qName The XML 1.0 qualified name.
   * @return The attribute type as a string, or null if the attribute is not in the list
   *         or if qualified names are not available.
   */
  public String getType (final String qName)
  {
    return getType(qName);
  }
}

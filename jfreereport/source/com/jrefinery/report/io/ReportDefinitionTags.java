/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------------
 * ReportDefinitionTags.java
 * -------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: ReportDefinitionTags.java,v 1.20 2002/12/08 23:29:48 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Extracted from ReportDefinitionContentHandler
 * 10-Jul-2002 : Added Image-field and image-function tags
 * 31-Aug-2002 : Added many tags in the meanwhile
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 */
package com.jrefinery.report.io;

/**
 * Defines the tags used in the parsing process.
 *
 * @author Thomas Morgner
 */
public interface ReportDefinitionTags
{
  /** Literal text for an XML report element. */
  public static final String REPORT_TAG = "report";

  /** Literal text for an XML report element. */
  public static final String REPORT_HEADER_TAG = "reportheader";

  /** Literal text for an XML report element. */
  public static final String REPORT_FOOTER_TAG = "reportfooter";

  /** Literal text for an XML report element. */
  public static final String PAGE_HEADER_TAG = "pageheader";

  /** Literal text for an XML report element. */
  public static final String PAGE_FOOTER_TAG = "pagefooter";

  /**
   * Group definition
   */

  /** Literal text for an XML report element. */
  public static final String GROUPS_TAG = "groups";

  /** Literal text for an XML report element. */
  public static final String GROUP_TAG = "group";

  /** Literal text for an XML report element. */
  public static final String GROUP_HEADER_TAG = "groupheader";

  /** Literal text for an XML report element. */
  public static final String GROUP_FOOTER_TAG = "groupfooter";

  /** Literal text for an XML report element. */
  public static final String FIELDS_TAG = "fields";

  /** Literal text for an XML report element. */
  public static final String FIELD_TAG = "field";

  /**
   * Items and data elements of the report
   */

  /** Literal text for an XML report element. */
  public static final String ITEMS_TAG = "items";

  /** Literal text for an XML report element. */
  public static final String LABEL_TAG = "label";

  /** Literal text for an XML report element. */
  public static final String IMAGEREF_TAG = "imageref";

  /** Literal text for an XML report element. */
  public static final String LINE_TAG = "line";

  /** Literal text for an XML report element. */
  public static final String RECTANGLE_TAG = "rectangle";

  /** Literal text for an XML report element. */
  public static final String GENERAL_FIELD_TAG = "general-field";

  /** Literal text for an XML report element. */
  public static final String STRING_FIELD_TAG = "string-field";

  /** Literal text for an XML report element. */
  public static final String NUMBER_FIELD_TAG = "number-field";

  /** Literal text for an XML report element. */
  public static final String DATE_FIELD_TAG = "date-field";

  /** Literal text for an XML report element. */
  public static final String MULTILINE_FIELD_TAG = "multiline-field";

  /** Literal text for an XML report element. */
  public static final String IMAGEFIELD_TAG = "image-field";

  /** Literal text for an XML report element. */
  public static final String IMAGEFUNCTION_TAG = "image-function";

  /** Literal text for an XML report element. */
  public static final String IMAGEURLFIELD_TAG = "imageurl-field";

  /** Literal text for an XML report element. */
  public static final String IMAGEURLFUNCTION_TAG = "imageurl-function";

  /** Literal text for an XML report element. */
  public static final String GENERAL_FUNCTION_TAG = "general-function";

  /** Literal text for an XML report element. */
  public static final String STRING_FUNCTION_TAG = "string-function";

  /** Literal text for an XML report element. */
  public static final String NUMBER_FUNCTION_TAG = "number-function";

  /** Literal text for an XML report element. */
  public static final String DATE_FUNCTION_TAG = "date-function";

  /** Literal text for an XML report element. */
  public static final String MULTILINE_FUNCTION_TAG = "multiline-function";

  /** Literal text for an XML attribute. */
  public static final String FUNCTIONS_TAG = "functions";

  /** Literal text for an XML attribute. */
  public static final String FUNCTION_TAG = "function";

  /** Literal text for an XML attribute. */
  public static final String EXPRESSION_TAG = "expression";

  /** Literal text for an XML attribute. */
  public static final String DATAREF_TAG = "data-ref";

  /** Literal text for an XML attribute. */
  public static final String FONT_NAME_ATT = "fontname";

  /** Literal text for an XML attribute. */
  public static final String FONT_STYLE_ATT = "fontstyle";

  /** Literal text for an XML attribute. */
  public static final String FONT_SIZE_ATT = "fontsize";


  /** Literal text for an XML attribute value. */
  public static final String FS_BOLD = "fsbold";

  /** Literal text for an XML attribute value. */
  public static final String FS_ITALIC = "fsitalic";

  /** Literal text for an XML attribute value. */
  public static final String FS_UNDERLINE = "fsunderline";

  /** Literal text for an XML attribute value. */
  public static final String FS_STRIKETHR = "fsstrikethr";


  /** Literal text for an XML report element. */
  public static final String PROPERTY_TAG = "property";

  /** Literal text for an XML report element. */
  public static final String PROPERTIES_TAG = "properties";


  /** Literal text for an XML attribute. */
  public static final String FORMAT_ATT = "format";

  /** Literal text for an XML attribute. */
  public static final String NAME_ATT = "name";

  /** Literal text for an XML attribute. */
  public static final String ALIGNMENT_ATT = "alignment";

  /** Literal text for an XML attribute. */
  public static final String VALIGNMENT_ATT = "vertical-alignment";

  /** Literal text for an XML attribute. */
  public static final String COLOR_ATT = "color";

  /** Literal text for an XML attribute. */
  public static final String FIELDNAME_ATT = "fieldname";

  /** Literal text for an XML attribute. */
  public static final String FUNCTIONNAME_ATT = "function";

  /** Literal text for an XML attribute. */
  public static final String NULLSTRING_ATT = "nullstring";

  /** Literal text for an XML attribute. */
  public static final String PAGEFORMAT_ATT = "pageformat";

  /** Literal text for an XML attribute. */
  public static final String LEFTMARGIN_ATT = "leftmargin";

  /** Literal text for an XML attribute. */
  public static final String RIGHTMARGIN_ATT = "rightmargin";

  /** Literal text for an XML attribute. */
  public static final String TOPMARGIN_ATT = "topmargin";

  /** Literal text for an XML attribute. */
  public static final String BOTTOMMARGIN_ATT = "bottommargin";

  /** Literal text for an XML attribute. */
  public static final String WIDTH_ATT = "width";

  /** Literal text for an XML attribute. */
  public static final String HEIGHT_ATT = "height";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_ATT = "orientation";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_PORTRAIT_VAL = "portrait";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_LANDSCAPE_VAL = "landscape";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_REVERSE_LANDSCAPE_VAL = "reverselandscape";

  /** The dependency level attribute. */
  public static final String DEPENCY_LEVEL_ATT = "deplevel";

  /** The property reference tag. */
  public static final String PROPERTY_REFERENCE_TAG = "property-ref";

  /** The property encoding attribute. */
  public static final String PROPERTY_ENCODING_ATT = "encoding";

  /** The property encoding text. */
  public static final String PROPERTY_ENCODING_TEXT = "text";

  /** The repeat header value. */
  public static final String REPEAT_HEADER = "repeat";

  /** The configuration tag. */
  public static final String CONFIGURATION_TAG = "configuration";
}

/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * ReportDefinitionTags.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 10-May-2002: Extracted from ReportDefinitionContentHandler
 */
package com.jrefinery.report.io;

public interface ReportDefinitionTags
{
  public static final String REPORT_TAG = "report";
  public static final String REPORT_HEADER_TAG = "reportheader";
  public static final String REPORT_FOOTER_TAG = "reportfooter";
  public static final String PAGE_HEADER_TAG = "pageheader";
  public static final String PAGE_FOOTER_TAG = "pagefooter";

  /**
   * Group definition
   */

  public static final String GROUPS_TAG = "groups";
  public static final String GROUP_TAG = "group";
  public static final String GROUP_HEADER_TAG = "groupheader";
  public static final String GROUP_FOOTER_TAG = "groupfooter";
  public static final String FIELDS_TAG = "fields";
  public static final String FIELD_TAG = "field";

  /**
   * Items and data elements of the report
   */

  public static final String ITEMS_TAG = "items";
  public static final String LABEL_TAG = "label";
  public static final String IMAGEREF_TAG = "imageref";
  public static final String LINE_TAG = "line";
  public static final String RECTANGLE_TAG = "rectangle";
  public static final String GENERAL_FIELD_TAG = "general-field";
  public static final String STRING_FIELD_TAG = "string-field";
  public static final String NUMBER_FIELD_TAG = "number-field";
  public static final String DATE_FIELD_TAG = "date-field";
  public static final String MULTILINE_FIELD_TAG = "multiline-field";
  public static final String GENERAL_FUNCTION_TAG = "general-function";
  public static final String STRING_FUNCTION_TAG = "string-function";
  public static final String NUMBER_FUNCTION_TAG = "number-function";
  public static final String DATE_FUNCTION_TAG = "date-function";
  public static final String MULTILINE_FUNCTION_TAG = "multiline-function";

  public static final String FUNCTIONS_TAG = "functions";
  public static final String FUNCTION_TAG = "function";
  public static final String DATAREF_TAG = "data-ref";
  public static final String FONT_NAME_ATT = "fontname";
  public static final String FONT_STYLE_ATT = "fontstyle";
  public static final String FONT_SIZE_ATT = "fontsize";

  public static final String FS_BOLD = "fsbold";
  public static final String FS_ITALIC = "fsitalic";
  public static final String FS_UNDERLINE = "fsunderline";
  public static final String FS_STRIKETHR = "fsstrikethr";

  public static final String PROPERTY_TAG = "property";
  public static final String PROPERTIES_TAG = "properties";

  public static final String NAME_ATT = "name";
  public static final String ALIGNMENT_ATT = "alignment";
  public static final String COLOR_ATT = "color";
  public static final String FIELDNAME_ATT = "fieldname";
  public static final String FUNCTIONNAME_ATT = "function";
  public static final String NULLSTRING_ATT = "nullstring";
}

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
 * ${name}
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultStyleKeys.java,v 1.1 2004/08/21 18:58:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14-May-2004 : Initial version
 *
 */

package org.jfree.layout.style;

public class DefaultStyleKeys
{
  private DefaultStyleKeys()
  {
  }

  /** A key for an element's 'visible' flag. */
  public static final StyleKey VISIBLE = StyleKey.getStyleKey("visible", Boolean.class);

  /** A key for the 'font name' used to draw element text. */
  public static final StyleKey FONT = StyleKey.getStyleKey("font", String.class);

  /** A key for the 'font size' used to draw element text. */
  public static final StyleKey FONTSIZE = StyleKey.getStyleKey("font-size", Number.class);

  /** A key for the 'font size' used to draw element text. */
  public static final StyleKey LINEHEIGHT = StyleKey.getStyleKey("line-height", Number.class);

  /** A key for an element's 'bold' flag. */
  public static final StyleKey BOLD = StyleKey.getStyleKey("font-bold", Boolean.class);

  /** A key for an element's 'italic' flag. */
  public static final StyleKey ITALIC = StyleKey.getStyleKey("font-italic", Boolean.class);

  /** A key for an element's 'underlined' flag. */
  public static final StyleKey UNDERLINED = StyleKey.getStyleKey("font-underline",
      Boolean.class);

  /** A key for an element's 'strikethrough' flag. */
  public static final StyleKey STRIKETHROUGH = StyleKey.getStyleKey("font-strikethrough",
      Boolean.class);

  /** A key for the horizontal alignment of an element. */
  public static final StyleKey ALIGNMENT = StyleKey.getStyleKey("text-align",
      TextAlignment.class);

  /** A key for the vertical alignment of an element. */
  public static final StyleKey VALIGNMENT = StyleKey.getStyleKey("vertical-align",
      VerticalAlignment.class);

  /** A key for the display style of an element. */
  public static final StyleKey DISPLAY_STYLE = StyleKey.getStyleKey("display-style",
      DisplayStyle.class);

  /** A key for the line wrapping of an text element. */
  public static final StyleKey LINE_WRAPPING = StyleKey.getStyleKey("line-wrapping",
      LineWrapping.class);

  /** A key for the indention of the first word of an text element. */
  public static final StyleKey TEXT_INDENT = StyleKey.getStyleKey("text-indent",
      Float.class);

  public static final StyleKey MARGIN_TOP = StyleKey.getStyleKey("margin-top", Number.class);
  public static final StyleKey MARGIN_LEFT = StyleKey.getStyleKey("margin-left", Number.class);
  public static final StyleKey MARGIN_BOTTOM = StyleKey.getStyleKey("margin-bottom", Number.class);
  public static final StyleKey MARGIN_RIGHT = StyleKey.getStyleKey("margin-right", Number.class);

  public static final StyleKey PADDING_TOP = StyleKey.getStyleKey("padding-top", Number.class);
  public static final StyleKey PADDING_LEFT = StyleKey.getStyleKey("padding-left", Number.class);
  public static final StyleKey PADDING_BOTTOM = StyleKey.getStyleKey("padding-bottom", Number.class);
  public static final StyleKey PADDING_RIGHT = StyleKey.getStyleKey("padding-right", Number.class);

  public static final StyleKey POSITION =
          StyleKey.getStyleKey("position", ElementPosition.class);

  public static final StyleKey TEXT_CLEAR =
          StyleKey.getStyleKey("clear", TextClear.class);

  public static final StyleKey TEXT_FLOATING =
          StyleKey.getStyleKey("float", TextFloating.class);

  public static final StyleKey PAGEBREAK_BEFORE =
          StyleKey.getStyleKey("pagebreak-before", PageBreakRule.class);

  public static final StyleKey PAGEBREAK_AFTER =
          StyleKey.getStyleKey("pagebreak-after", PageBreakRule.class);

  /** Value ALWAYS is ignored and counts as AUTO */
  public static final StyleKey PAGEBREAK_INSIDE =
          StyleKey.getStyleKey("pagebreak-after", PageBreakRule.class);

  public static final StyleKey ORPHANS = StyleKey.getStyleKey("orphans", Number.class);
  public static final StyleKey WIDOWS = StyleKey.getStyleKey("widows", Number.class);
}

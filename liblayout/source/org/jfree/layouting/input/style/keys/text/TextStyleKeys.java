package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * http://www.w3.org/TR/css3-text/<br/>
 * and</br>
 * http://www.w3.org/TR/2003/CR-css3-text-20030514/
 * <p/>
 * Text needs to be changed, as the Working-draft is more than just incomplete.
 * TextShadow is not supported yet.
 *
 * @see http://www.unicode.org/unicode/reports/tr9/tr9-11.html
 * @author Thomas Morgner
 */
public class TextStyleKeys
{
  /**
   * This property declares whether and how white space inside the element is
   * collapsed. Values have the following meanings:
   * <p/>
   * <ul>
   * <li>collapse
   * <p>This value directs user agents to collapse sequences of white
   * space into a single character (or in some cases, no character).
   * </p>
   * </li>
   * <li>preserve
   * <p>
   * This value prevents user agents from collapsing sequences of white space.
   * Line breaks are preserved.
   * </p>
   * </li>
   * <li>
   * preserve-breaks
   * <p>
   * This value collapses white space as for 'collapse', but preserves line breaks.
   * </p>
   * </li>
   * <li>
   * discard
   * <p>
   * This value directs user agents to discard all white space in the element.
   * </p>
   * </li>
   * </ul>
   *
   * @see http://www.w3.org/TR/css3-text/#white-space-rules
   */
  public static StyleKey WHITE_SPACE_COLLAPSE =
          StyleKeyRegistry.getRegistry().createKey
                  ("white-space-collapse", false, true, false);

  public static StyleKey WORD_BREAK =
          StyleKeyRegistry.getRegistry().createKey
                  ("word-break", false, true, false);

  public static StyleKey HYPHENATE =
          StyleKeyRegistry.getRegistry().createKey
                  ("hyphenate", false, true, false);

  public static StyleKey TEXT_WRAP =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-wrap", false, true, false);

  public static StyleKey WORD_WRAP =
          StyleKeyRegistry.getRegistry().createKey
                  ("word-wrap", false, true, false);

  public static StyleKey TEXT_ALIGN =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-align", false, true, false);

  public static StyleKey TEXT_ALIGN_LAST =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-align-last", false, true, false);

  public static StyleKey TEXT_JUSTIFY =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-justify", false, true, false);

  public static StyleKey WORD_SPACING =
          StyleKeyRegistry.getRegistry().createKey
                  ("word-spacing", false, true, false);

  public static StyleKey LETTER_SPACING =
          StyleKeyRegistry.getRegistry().createKey
                  ("letter-spacing", false, true, true);

  /** Arabic script specific */
  public static StyleKey TEXT_KASHIDA_SPACE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-kashida-space", false, true, false);

  public static StyleKey DIRECTION =
          StyleKeyRegistry.getRegistry().createKey
                  ("direction", false, true, false);

  public static StyleKey BLOCK_PROGRESSION =
          StyleKeyRegistry.getRegistry().createKey
                  ("block-progression", false, true, false);

  public static StyleKey GLYPH_ORIENTATION_HORIZONTAL =
          StyleKeyRegistry.getRegistry().createKey
                  ("glyph-orientation-horizontal", false, true, false);

  public static StyleKey GLYPH_ORIENTATION_VERTICAL =
          StyleKeyRegistry.getRegistry().createKey
                  ("glyph-orientation-vertical", false, true, false);

  public static StyleKey UNICODE_BIDI =
          StyleKeyRegistry.getRegistry().createKey
                  ("unicode-bidi", false, true, false);

  public static StyleKey TEXT_SCRIPT =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-script", false, true, false);

  /** todo: For asian scripts; not yet used. */
  public static StyleKey TEXT_JUSTIFY_TRIM =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-justify-trim", false, true, false);

  public static StyleKey TEXT_INDENT =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-indent", false, true, true);

  public static StyleKey TEXT_OVERFLOW_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overflow-mode", false, true, false);

  public static StyleKey TEXT_OVERFLOW_ELLIPSIS =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overflow-ellipsis", false, true, true);

  /** Asian fonts only */
  public static StyleKey PUNCTUATION_TRIM =
          StyleKeyRegistry.getRegistry().createKey
                  ("punctuation-trim", false, true, false);
  /** Asian fonts only */
  public static StyleKey TEXT_AUTO_SPACE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-autospace", false, true, true);

  public static StyleKey KERNING_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("kerning-mode", false, true, true);
  public static StyleKey KERNING_PAIR_THRESHOLD =
          StyleKeyRegistry.getRegistry().createKey
                  ("kerning-pair-threshold", false, true, false);
  public static StyleKey TEXT_UNDERLINE_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-underline-style", false, true, false);
  public static StyleKey TEXT_LINE_THROUGH_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-line-through-style", false, true, false);
  public static StyleKey TEXT_OVERLINE_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overline-style", false, true, false);
  public static StyleKey TEXT_UNDERLINE_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-underline-width", false, true, false);
  public static StyleKey TEXT_LINE_THROUGH_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-line-through-width", false, true, false);
  public static StyleKey TEXT_OVERLINE_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overline-width", false, true, false);
  public static StyleKey TEXT_UNDERLINE_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-underline-mode", false, true, false);
  public static StyleKey TEXT_LINE_THROUGH_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-line-through-mode", false, true, false);
  public static StyleKey TEXT_OVERLINE_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overline-mode", false, true, false);
  public static StyleKey TEXT_UNDERLINE_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-underline-color", false, true, false);
  public static StyleKey TEXT_LINE_THROUGH_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-line-through-color", false, true, false);
  public static StyleKey TEXT_OVERLINE_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overline-color", false, true, false);

  public static StyleKey TEXT_UNDERLINE_POSITION =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-underline-position", false, true, false);
  public static StyleKey TEXT_BLINK =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-blink", false, false, false);

  public static StyleKey LINE_GRID_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("line-grid-mode", false, true, false);
  public static StyleKey LINE_GRID_PROGRESSION =
          StyleKeyRegistry.getRegistry().createKey
                  ("line-grid-progression", false, true, false);
  public static StyleKey TEXT_TRANSFORM =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-transform", false, true, false);
  public static StyleKey HANGING_PUNCTUATION =
          StyleKeyRegistry.getRegistry().createKey
                  ("hanging-punctuation", false, true, false);
  public static StyleKey TEXT_COMBINE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-combine", false, true, false);

  private TextStyleKeys()
  {
  }
}
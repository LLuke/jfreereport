package org.jfree.layouting.input.swing;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;

/**
 * 
 */
public interface Convertor
{
  /**
   * Converts a style key and a style value to a CSS compatible style key and style value.
   * A conversion can result in more than one key and value.
   *
   * @param key The style key to convert.
   * @param value The style value to convert.
   * @param cssAttr The current converted CSS attributes for the current element.
   * @param context The current Element.
   * @return The conversion result or null if no converstion has been done.
   */
  public AttributeSet convertToCSS(Object key, Object value, AttributeSet cssAttr, Element context);
}

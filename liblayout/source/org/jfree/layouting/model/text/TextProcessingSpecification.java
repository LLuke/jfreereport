/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * TextProcessingSpecification.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.model.text;

import org.jfree.layouting.input.style.keys.text.TextTransform;
import org.jfree.layouting.input.style.keys.text.TextWrap;
import org.jfree.layouting.input.style.keys.text.WhitespaceCollapse;
import org.jfree.layouting.input.style.keys.text.WordBreak;

/**
 * Creation-Date: 21.12.2005, 13:51:02
 *
 * @author Thomas Morgner
 */
public class TextProcessingSpecification
{
  private WhitespaceCollapse whitespaceCollapse;
  private WordBreak wordBreak;
  private boolean hyphenate;
  private TextWrap textWrap;
  private boolean wordWrap;
  private TextTransform textTransform;

  public TextProcessingSpecification()
  {
  }

  public WhitespaceCollapse getWhitespaceCollapse()
  {
    return whitespaceCollapse;
  }

  public void setWhitespaceCollapse(final WhitespaceCollapse whitespaceCollapse)
  {
    this.whitespaceCollapse = whitespaceCollapse;
  }

  public WordBreak getWordBreak()
  {
    return wordBreak;
  }

  public void setWordBreak(final WordBreak wordBreak)
  {
    this.wordBreak = wordBreak;
  }

  public boolean isHyphenate()
  {
    return hyphenate;
  }

  public void setHyphenate(final boolean hyphenate)
  {
    this.hyphenate = hyphenate;
  }

  public TextWrap getTextWrap()
  {
    return textWrap;
  }

  public void setTextWrap(final TextWrap textWrap)
  {
    this.textWrap = textWrap;
  }

  public TextTransform getTextTransform()
  {
    return textTransform;
  }

  public void setTextTransform(final TextTransform textTransform)
  {
    this.textTransform = textTransform;
  }

  public boolean isWordWrap()
  {
    return wordWrap;
  }

  public void setWordWrap(final boolean wordWrap)
  {
    this.wordWrap = wordWrap;
  }
}

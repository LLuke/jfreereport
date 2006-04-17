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
 * TextSpacingSpecification.java
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

import org.jfree.layouting.input.style.keys.text.KerningMode;
import org.jfree.layouting.input.style.keys.text.PunctuationTrim;
import org.jfree.layouting.input.style.keys.text.TextAutoSpace;

/**
 * Creation-Date: 21.12.2005, 13:56:35
 *
 * @author Thomas Morgner
 */
public class TextSpacingSpecification
{
  private AbsoluteSpacingValue wordSpacing;
  private AbsoluteSpacingValue letterSpacing;
  private PunctuationTrim punctuationTrim;
  private TextAutoSpace textAutoSpace;
  private KerningMode kerningMode;
  private double kerningThreshold; // a font size in pt.

  public TextSpacingSpecification()
  {
  }

  public AbsoluteSpacingValue getWordSpacing()
  {
    return wordSpacing;
  }

  public void setWordSpacing(final AbsoluteSpacingValue wordSpacing)
  {
    this.wordSpacing = wordSpacing;
  }

  public AbsoluteSpacingValue getLetterSpacing()
  {
    return letterSpacing;
  }

  public void setLetterSpacing(final AbsoluteSpacingValue letterSpacing)
  {
    this.letterSpacing = letterSpacing;
  }

  public PunctuationTrim getPunctuationTrim()
  {
    return punctuationTrim;
  }

  public void setPunctuationTrim(final PunctuationTrim punctuationTrim)
  {
    this.punctuationTrim = punctuationTrim;
  }

  public TextAutoSpace getTextAutoSpace()
  {
    return textAutoSpace;
  }

  public void setTextAutoSpace(final TextAutoSpace textAutoSpace)
  {
    this.textAutoSpace = textAutoSpace;
  }

  public KerningMode getKerningMode()
  {
    return kerningMode;
  }

  public void setKerningMode(final KerningMode kerningMode)
  {
    this.kerningMode = kerningMode;
  }

  public double getKerningThreshold()
  {
    return kerningThreshold;
  }

  public void setKerningThreshold(final double kerningThreshold)
  {
    this.kerningThreshold = kerningThreshold;
  }
}

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
 * DummyOutputProcessor.java
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

package org.jfree.layouting.output.pageable.dummy;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.model.PageContext;
import org.jfree.layouting.normalizer.pagable.PageGeneratingNormalizer;
import org.jfree.layouting.normalizer.pagable.PagePreparationNormalizer;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.pageable.PageableOutputProcessor;

public class DummyOutputProcessor implements PageableOutputProcessor
{

  private FontStorage fontStorage;
  private OutputProcessorMetaData metaData;

  public DummyOutputProcessor ()
  {
    fontStorage = new DefaultFontStorage(new AWTFontRegistry());
    metaData = new DummyOutputProcessorMetaData(fontStorage);
  }

  public FontStorage getFontStorage ()
  {
    return fontStorage;
  }

  public OutputProcessorMetaData getMetaData ()
  {
    return metaData;
  }

  public PageContext createPageContext(int pageNumber)
  {
    return null;
  }

  public PageGeneratingNormalizer createGenerateNormalizer()
  {
    return null;
  }

  public PagePreparationNormalizer createPrepareNormalizer()
  {
    return null;
  }

}

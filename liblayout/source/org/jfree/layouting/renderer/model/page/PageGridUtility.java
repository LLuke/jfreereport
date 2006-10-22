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
 * PageGridFactory.java
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
package org.jfree.layouting.renderer.model.page;

import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;

/**
 * Creation-Date: 20.10.2006, 20:46:30
 *
 * @author Thomas Morgner
 */
public class PageGridUtility
{
  private PageGridUtility()
  {
  }

  public static PageSize lookupPageSize (CSSValue sizeVal,
                                         OutputProcessorMetaData metaData)
  {
    PageSize defaultVal = metaData.getDefaultPageSize();

    if (sizeVal instanceof CSSValuePair == false)
    {
      return defaultVal;
    }
    CSSValuePair valuePair = (CSSValuePair) sizeVal;
    final CSSValue firstValue = valuePair.getFirstValue();
    if (firstValue instanceof CSSNumericValue == false)
    {
      return defaultVal;
    }
    final CSSValue secondValue = valuePair.getSecondValue();
    if (secondValue instanceof CSSNumericValue == false)
    {
      return defaultVal;
    }

    CSSNumericValue widthVal = CSSValueResolverUtility.getLength
            (firstValue, CSSNumericValue.createPtValue(defaultVal.getWidth()));
    CSSNumericValue heightVal = CSSValueResolverUtility.getLength
            (secondValue, CSSNumericValue.createPtValue(defaultVal.getHeight()));
    double width = CSSValueResolverUtility.convertLengthToDouble
            (widthVal, null, metaData);
    double height = CSSValueResolverUtility.convertLengthToDouble
            (heightVal, null, metaData);
    if (width < 1 || height < 1)
    {
      return defaultVal;
    }
    return new PageSize((int) width, (int) height);
  }

}

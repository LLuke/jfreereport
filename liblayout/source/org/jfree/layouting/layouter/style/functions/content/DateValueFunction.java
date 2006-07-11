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
 * DateFunction.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DateFunction.java,v 1.1 2006/04/17 21:01:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.functions.content;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.statics.FormattedContentToken;
import org.jfree.layouting.layouter.i18n.LocalizationContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;

/**
 * Creation-Date: 15.04.2006, 18:33:56
 *
 * @author Thomas Morgner
 */
public class DateValueFunction implements ContentFunction
{
  public DateValueFunction()
  {
  }

  public ContentToken evaluate(final LayoutProcess layoutProcess,
                               final LayoutElement element,
                               final CSSFunctionValue function)
          throws FunctionEvaluationException
  {

    final Date date = DocumentContextUtility.getDate
            (layoutProcess.getDocumentContext());
    final CSSValue[] parameters = function.getParameters();
    final LocalizationContext localizationContext =
            DocumentContextUtility.getLocalizationContext
                    (layoutProcess.getDocumentContext());

    DateFormat format = getDateFormat
            (parameters, localizationContext,
                    element.getLayoutContext().getLanguage());
    return new FormattedContentToken(date, format, format.format(date));
  }

  private DateFormat getDateFormat(CSSValue[] parameters,
                                   LocalizationContext localizationContext,
                                   Locale locale)
  {
    if (parameters.length < 1)
    {
      return localizationContext.getDateFormat(locale);
    }

    final CSSValue formatValue = parameters[0];
    if (formatValue instanceof CSSStringValue == false)
    {
      return localizationContext.getDateFormat(locale);
    }

    CSSStringValue sval = (CSSStringValue) formatValue;
    DateFormat format = localizationContext.getDateFormat
            (sval.getValue(), locale);
    if (format != null)
    {
      return format;
    }
    return localizationContext.getDateFormat(locale);
  }


}

/**
 * Date: Jan 12, 2003
 * Time: 5:52:29 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.elements;

import com.jrefinery.report.Element;

public interface ElementFactory
{
  public Element getElementForType (String type);
}

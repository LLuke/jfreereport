/**
 * Date: Jan 10, 2003
 * Time: 4:54:52 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.targets.style.StyleKey;
import com.jrefinery.report.io.Parser;

public interface StyleKeyFactory
{
  public StyleKey getStyleKey (String name);
  public Object createBasicObject(StyleKey k, String value);

  public void init (Parser parser);
}

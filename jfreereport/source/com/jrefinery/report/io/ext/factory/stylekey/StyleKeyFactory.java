/**
 * Date: Jan 10, 2003
 * Time: 4:54:52 PM
 *
 * $Id: StyleKeyFactory.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.targets.style.StyleKey;
import com.jrefinery.report.io.Parser;

public interface StyleKeyFactory
{
  public StyleKey getStyleKey (String name);
  public Object createBasicObject(StyleKey k, String value, Class c);

  public void init (Parser parser);
}

/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

public class CharacterObjectDescription extends AbstractObjectDescription
{
  public CharacterObjectDescription()
  {
    super(Character.class);
    setParameterDefinition("value", String.class);
  }

  public Object createObject()
  {
    String o = (String) getParameter("value");
    if (o == null)
      return null;

    if (o.length() > 0)
      return new Character(o.charAt(0));
    else
      return null;
  }
}

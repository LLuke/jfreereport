/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: ByteObjectDescription.java,v 1.2 2003/01/13 19:00:51 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;



public class ByteObjectDescription extends AbstractObjectDescription
{
  public ByteObjectDescription()
  {
    super(Byte.class);
    setParameterDefinition("value", String.class);
  }

  public Object createObject()
  {
    String o = (String) getParameter("value");
    return Byte.valueOf(o);
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof Byte)
    {
      setParameter("value", String.valueOf(o));
    }
  }
}

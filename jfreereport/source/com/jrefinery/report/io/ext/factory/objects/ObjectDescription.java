/**
 * Date: Jan 10, 2003
 * Time: 8:15:33 PM
 *
 * $Id: ObjectDescription.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.util.Iterator;

public interface ObjectDescription
{
  public Class getParameterDefinition (String name);

  public void setParameter (String name, Object value);

  public Object getParameter (String name);

  public Iterator getParameterNames ();

  public Class getObjectClass ();

  public Object createObject ();

  public ObjectDescription getInstance();

  public void setParameterFromObject (Object o)  throws ObjectFactoryException;
}

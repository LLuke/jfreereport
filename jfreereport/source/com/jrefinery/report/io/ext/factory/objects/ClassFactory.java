/**
 * Date: Jan 22, 2003
 * Time: 7:59:56 PM
 *
 * $Id: ClassFactory.java,v 1.3 2003/01/23 18:07:45 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.util.Iterator;

public interface ClassFactory
{
  public ObjectDescription getDescriptionForClass (Class c);
  public ObjectDescription getSuperClassObjectDescription(Class c);
  public Iterator getRegisteredClasses();
}

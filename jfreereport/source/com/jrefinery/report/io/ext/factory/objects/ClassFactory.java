/**
 * Date: Jan 22, 2003
 * Time: 7:59:56 PM
 *
 * $Id: ClassFactory.java,v 1.2 2003/01/22 19:38:26 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

public interface ClassFactory
{
  public ObjectDescription getDescriptionForClass (Class c);
  public ObjectDescription getSuperClassObjectDescription(Class c);
}

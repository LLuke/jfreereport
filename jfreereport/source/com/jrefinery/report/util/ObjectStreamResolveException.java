/**
 * Date: Feb 5, 2003
 * Time: 4:01:02 PM
 *
 * $Id$
 */
package com.jrefinery.report.util;

import java.io.ObjectStreamException;

public class ObjectStreamResolveException extends ObjectStreamException
{
  /**
   * Create an ObjectStreamException with the specified argument.
   *
   * @param classname the detailed message for the exception
   */
  public ObjectStreamResolveException(String classname)
  {
    super(classname);
  }

  /**
   * Create an ObjectStreamException.
   */
  public ObjectStreamResolveException()
  {
  }
}

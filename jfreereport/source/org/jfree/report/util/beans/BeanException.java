package org.jfree.report.util.beans;

import org.jfree.util.StackableException;

public class BeanException extends StackableException
{
  public BeanException ()
  {
  }

  public BeanException (final String message, final Exception ex)
  {
    super(message, ex);
  }

  public BeanException (final String message)
  {
    super(message);
  }
}

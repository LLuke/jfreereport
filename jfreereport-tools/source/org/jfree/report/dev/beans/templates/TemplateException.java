package org.jfree.report.dev.beans.templates;

import org.jfree.util.StackableException;

public class TemplateException extends StackableException
{
  public TemplateException()
  {
  }

  public TemplateException(final String message, final Exception ex)
  {
    super(message, ex);
  }

  public TemplateException(final String message)
  {
    super(message);
  }
}

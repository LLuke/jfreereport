/**
 *
 *  Date: 23.06.2002
 *  Log4JLogTarget.java
 *  ------------------------------
 *  23.06.2002 : ...
 */
package com.jrefinery.report.ext.log;

import com.jrefinery.report.util.LogTarget;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;

public class Log4JLogTarget implements LogTarget
{
  private Category cat;

  public Log4JLogTarget ()
  {
    this (Category.getInstance("JFreeReport");
  }

  public Log4JLogTarget (Category cat)
  {
    if (cat == null) throw new NullPointerException("Given category is null");
    this.cat = cat;
  }

  public void debug (String message)
  {
    cat.debug(message);
  }

  public void debug (String message, Exception e)
  {
    cat.debug (message, e);
  }

  public void error (String message)
  {
    cat.error (message);
  }

  public void error (String message, Exception e)
  {
    cat.error (message, e);
  }

  public void info (String message)
  {
    cat.info(message);
  }

  public void info (String message, Exception e)
  {
    cat.info (message, e);
  }

  public void log (int level, String message)
  {
    Priority priority = Priority.toPriority(level);
    cat.log(priority, message);
  }

  public void log (int level, String message, Exception e)
  {
    Priority priority = Priority.toPriority(level);
    cat.log(priority, message, e);
  }

  public void warn (String message)
  {
    cat.warn(message);
  }

  public void warn (String message, Exception e)
  {
    cat.warn (message, e);
  }
}
package org.jfree.report.dev.beans.templates;

import java.util.HashMap;

public class Context
{
  private HashMap backend;
  private Context parent;

  public Context()
  {
    this.backend = new HashMap();
  }

  public Context create()
  {
    Context ctx = new Context();
    ctx.parent = this;
    return ctx;
  }

  public void put (String name, Object value)
  {
    backend.put (name, value);
  }

  public Object get (String name)
  {
    Object retval = backend.get(name);
    if (retval == null && parent != null)
    {
      return parent.get(name);
    }
    return retval;
  }
}

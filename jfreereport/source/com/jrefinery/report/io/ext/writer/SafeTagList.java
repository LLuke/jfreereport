/**
 * Date: Jan 22, 2003
 * Time: 6:35:47 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.writer;

import java.util.Hashtable;

public class SafeTagList
{
  private Hashtable safeTags;

  private static class SafeDescription
  {
    private boolean open;
    private boolean close;

    public SafeDescription(boolean open, boolean close)
    {
      this.open = open;
      this.close = close;
    }

    public boolean isOpen()
    {
      return open;
    }

    public boolean isClose()
    {
      return close;
    }
  }

  public SafeTagList()
  {
    safeTags = new Hashtable();
  }

  public void add (String tag)
  {
    safeTags.put(tag, new SafeDescription(true, true));
  }

  public void add (String tag, boolean open, boolean closed)
  {
    safeTags.put(tag, new SafeDescription(open, closed));
  }

  public boolean isSafeForOpen (String tag)
  {
    SafeDescription sd = (SafeDescription) safeTags.get(tag);
    if (sd == null)
      return false;

    return sd.isOpen();
  }

  public boolean isSafeForClose (String tag)
  {
    SafeDescription sd = (SafeDescription) safeTags.get(tag);
    if (sd == null)
      return false;

    return sd.isClose();
  }
}

/**
 * Date: Nov 30, 2002
 * Time: 8:23:19 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.output;

public class PDFFontRecordKey
{
  private String logicalName;
  private String encoding;

  public PDFFontRecordKey(String logicalName, String encoding)
  {
    this.logicalName = logicalName;
    this.encoding = encoding;
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof PDFFontRecordKey)) return false;

    final PDFFontRecordKey key = (PDFFontRecordKey) o;

    if (encoding != null)
    {
       if (!encoding.equals(key.encoding))
         return false;
       if (key.encoding == null)
         return false;
    }
    if (logicalName != null)
    {
      if (!logicalName.equals(key.logicalName))
        return false;
      if (key.logicalName == null)
        return false;
    }
    return true;
  }

  public int hashCode()
  {
    int result;
    result = (logicalName != null ? logicalName.hashCode() : 0);
    result = 29 * result + (encoding != null ? encoding.hashCode() : 0);
    return result;
  }
}

package org.jfree.report.data;

import org.jfree.report.structure.Element;

/**
 * Creation-Date: 24.11.2006, 13:51:02
 *
 * @author Thomas Morgner
 */
public interface PrecomputeNode
{
  public Element getElement();

  public String getId();

  public String getName();

  public String getTag();

  public String getNamespace();

  public PrecomputeNode getParent();

  public PrecomputeNode getNext();

  public PrecomputeNode getFirstChild();

  public PrecomputeNode getLastChild();

  public int getFunctionCount();

  public String getFunctionName(int idx);

  public Object getFunctionResult(int idx);
}

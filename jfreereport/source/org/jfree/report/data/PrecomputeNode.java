package org.jfree.report.data;

import org.jfree.report.structure.Element;

/**
 * Creation-Date: 24.11.2006, 13:51:02
 *
 * @author Thomas Morgner
 */
public interface PrecomputeNode
{
  Element getElement();

  String getId();

  String getName();

  String getTag();

  String getNamespace();

  PrecomputeNode getParent();

  PrecomputeNode getNext();

  PrecomputeNode getFirstChild();

  PrecomputeNode getLastChild();

  int getFunctionCount();

  String getFunctionName(int idx);

  Object getFunctionResult(int idx);
}

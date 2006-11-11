/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * Group.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: Group.java,v 1.2 2006/04/22 16:18:14 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.report.structure;

import java.io.Serializable;

import org.jfree.report.expressions.Expression;

/**
 * A report group.  Reports can contain any number of (nested) groups. The order
 * of the fields is not important.
 * <p/>
 * Whether a new group should be started is evaluated by the group's expression.
 * If that expression returns Boolean.TRUE, a new group is started. (That
 * expression answers the Questions: 'Does this group continue?'.
 * <p>
 * A group uses the same data set as the containing group/report.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class Group extends Section implements Serializable
{
  private Expression groupingExpression;

  /** Constructs a group with no fields, and an empty header and footer. */
  public Group()
  {
    setType("group");
    setRepeat(true);
  }

  /**
   * Returns a string representation of the group (useful for debugging).
   *
   * @return A string.
   */
  public String toString()
  {
    final StringBuffer b = new StringBuffer();
    b.append("Group={Name='");
    b.append(getName());
    b.append("} ");
    return b.toString();
  }

  public Expression getGroupingExpression()
  {
    return groupingExpression;
  }

  public void setGroupingExpression(final Expression groupingExpression)
  {
    this.groupingExpression = groupingExpression;
  }

  public Group getGroup()
  {
    return this;
  }
}

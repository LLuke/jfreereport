/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * AbstractSpacingProducer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.text;

import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.LayoutProcess;
import org.jfree.util.Log;

/**
 * Creation-Date: 11.06.2006, 17:02:27
 *
 * @author Thomas Morgner
 */
public class GraphemeClusterProducer implements ClassificationProducer
{
  private static class GraphemeClusterProducerState implements State
  {
    private int lastClassification;
    private int lastValue;

    public GraphemeClusterProducerState()
    {
    }

    public int getLastClassification()
    {
      return lastClassification;
    }

    public void setLastClassification(final int lastClassification)
    {
      this.lastClassification = lastClassification;
    }

    public int getLastValue()
    {
      return lastValue;
    }

    public void setLastValue(final int lastValue)
    {
      this.lastValue = lastValue;
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws StateException
     */
    public StatefullComponent restore(LayoutProcess layoutProcess)
            throws StateException
    {
      GraphemeClusterProducer prod = new GraphemeClusterProducer();
      prod.lastClassification = lastClassification;
      prod.lastValue = lastValue;
      return prod;
    }
  }

  private int lastClassification;
  private int lastValue;
  private GraphemeClassifier classifier;

  public GraphemeClusterProducer()
  {
    classifier = new GraphemeClassifier();
  }

  /**
   * Returns an alternating counter for the grapheme clusters. The value
   * returned can be tested for equality; if two subsequent calls return
   * the same value, the characters of these calls belong to the same cluster.
   *
   * @param codePoint
   * @return
   */
  public int createGraphemeCluster (int codePoint)
  {
    int classification = classifier.getGraphemeClassification(codePoint);
    if (codePoint == GraphemeClassifier.EXTEND)
    {
      lastClassification = classification;
      return lastValue;
    }

    if (lastClassification == GraphemeClassifier.LF &&
        classification == GraphemeClassifier.CR)
    {
      lastClassification = classification;
      return lastValue;
    }

    if (lastClassification == GraphemeClassifier.L)
    {
      if ((codePoint & GraphemeClassifier.ANY_HANGUL_MASK) ==
              GraphemeClassifier.ANY_HANGUL_MASK)
      {
        lastClassification = classification;
        return lastValue;
      }
    }

    final boolean oldLVorV =
            (lastClassification & GraphemeClassifier.V_OR_LV_MASK) ==
              GraphemeClassifier.V_OR_LV_MASK;
    final boolean newVorT =
            (codePoint & GraphemeClassifier.V_OR_T_MASK) ==
                    GraphemeClassifier.V_OR_T_MASK;
    if (oldLVorV && newVorT)
    {
      lastClassification = classification;
      return lastValue;
    }

    final boolean oldLVTorT =
            (lastClassification & GraphemeClassifier.LVT_OR_T_MASK) ==
              GraphemeClassifier.LVT_OR_T_MASK;
    if (oldLVTorT && codePoint == GraphemeClassifier.T)
    {
      lastClassification = classification;
      return lastValue;
    }

    lastValue += 1;
    lastClassification = classification;
    return lastValue;
  }


  public State saveState() throws StateException
  {
    GraphemeClusterProducerState state = new GraphemeClusterProducerState();
    state.setLastClassification(lastClassification);
    state.setLastValue(lastValue);
    return state;
  }
}

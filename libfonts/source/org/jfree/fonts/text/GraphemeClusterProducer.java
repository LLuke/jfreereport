/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * $Id: GraphemeClusterProducer.java,v 1.1 2007/04/03 14:13:56 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.fonts.text;

/**
 * Creation-Date: 11.06.2006, 17:02:27
 *
 * @author Thomas Morgner
 */
public class GraphemeClusterProducer implements ClassificationProducer
{
  private int lastClassification;
  private GraphemeClassifier classifier;

  public GraphemeClusterProducer()
  {
    classifier = GraphemeClassifier.getClassifier();
  }

  /**
   * Returns an alternating counter for the grapheme clusters. The value
   * returned can be tested for equality; if two subsequent calls return
   * the same value, the characters of these calls belong to the same cluster.
   *
   * @param codePoint
   * @return true, if a new cluster starts, false if the old cluster continues.
   */
  public boolean createGraphemeCluster (final int codePoint)
  {
    final int classification = classifier.getGraphemeClassification(codePoint);
    if (classification == GraphemeClassifier.EXTEND)
    {
      lastClassification = classification;
      return false;
    }

    if (lastClassification == GraphemeClassifier.LF &&
        classification == GraphemeClassifier.CR)
    {
      lastClassification = classification;
      return false;
    }

    if (lastClassification == GraphemeClassifier.L)
    {
      if ((classification & GraphemeClassifier.ANY_HANGUL_MASK) ==
              GraphemeClassifier.ANY_HANGUL_MASK)
      {
        lastClassification = classification;
        return false;
      }
    }

    final boolean oldLVorV =
            (lastClassification & GraphemeClassifier.V_OR_LV_MASK) ==
              GraphemeClassifier.V_OR_LV_MASK;
    final boolean newVorT =
            (classification & GraphemeClassifier.V_OR_T_MASK) ==
                    GraphemeClassifier.V_OR_T_MASK;
    if (oldLVorV && newVorT)
    {
      lastClassification = classification;
      return false;
    }

    final boolean oldLVTorT =
            (lastClassification & GraphemeClassifier.LVT_OR_T_MASK) ==
              GraphemeClassifier.LVT_OR_T_MASK;
    if (oldLVTorT && classification == GraphemeClassifier.T)
    {
      lastClassification = classification;
      return false;
    }

    lastClassification = classification;
    return true;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

}

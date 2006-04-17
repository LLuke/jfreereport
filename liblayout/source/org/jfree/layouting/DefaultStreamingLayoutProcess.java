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
 * DefaultStreamingLayoutProcess.java
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
package org.jfree.layouting;

import org.jfree.layouting.layouter.feed.DefaultInputFeed;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.normalizer.Normalizer;
import org.jfree.layouting.normalizer.streaming.StreamingNormalizer;
import org.jfree.layouting.output.streaming.StreamingOutputProcessor;

/**
 * Creation-Date: 02.01.2006, 19:33:35
 *
 * @author Thomas Morgner
 */
public class DefaultStreamingLayoutProcess extends AbstractLayoutProcess
        implements StreamingLayoutProcess
{
  private StreamingNormalizer streamingNormalizer;

  public DefaultStreamingLayoutProcess(StreamingOutputProcessor outputProcessor)
  {
    super(outputProcessor);
    streamingNormalizer = outputProcessor.createNormalizer();
  }

  protected InputFeed createInputFeed()
  {
    return new DefaultInputFeed(this);
  }

  public Normalizer getNormalizer()
  {
    return streamingNormalizer;
  }
}

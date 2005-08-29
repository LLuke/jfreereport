/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * -------------
 * CardDemo.java
 * -------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: CardDemo.java,v 1.5 2005/08/08 15:36:27 taqua Exp $
 *
 * Changes
 * -------
 * 29.03.2003 : Initial version
 */
package org.jfree.report.demo.cards;

import org.jfree.report.demo.helper.CompoundDemoFrame;
import org.jfree.report.demo.helper.DefaultDemoSelector;
import org.jfree.report.demo.helper.DemoSelector;
import org.jfree.ui.RefineryUtilities;

/**
 * A JFreeReport demo.
 *
 * @author Thomas Morgner.
 */
public class CardDemo extends CompoundDemoFrame
{
  /**
   * Default constructor.
   */
  public CardDemo (final DemoSelector selector)
  {
    super(selector);
    init();
  }

  /**
   * The starting point for the demo application.
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    final DefaultDemoSelector selector = createDemoInfo();

    final CardDemo frame = new CardDemo(selector);
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }

  public static DefaultDemoSelector createDemoInfo() {
    final DefaultDemoSelector selector = new DefaultDemoSelector("Card demos");
    selector.addDemo(new SimpleCardDemoHandler());
    selector.addDemo(new LeadingEmptyCardsDemoHandler());
    return selector;
  }
}

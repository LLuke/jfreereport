/**
 * Date: Mar 9, 2003
 * Time: 2:14:00 PM
 *
 * $Id$
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.Composite;

public class SetCompositeOperation implements G2Operation
{
  private Composite composite;

  public SetCompositeOperation(Composite composite)
  {
    this.composite = composite;
  }

  public void draw(Graphics2D g2)
  {
    g2.setComposite(composite);
  }
}

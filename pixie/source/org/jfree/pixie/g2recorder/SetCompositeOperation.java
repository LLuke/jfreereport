/**
 * Date: Mar 9, 2003
 * Time: 2:14:00 PM
 *
 * $Id: SetCompositeOperation.java,v 1.1 2003/03/09 20:38:13 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.Composite;

public class SetCompositeOperation implements G2Operation
{
  private Composite composite;

  public SetCompositeOperation(final Composite composite)
  {
    this.composite = composite;
  }

  public void draw(final Graphics2D g2)
  {
    g2.setComposite(composite);
  }
}

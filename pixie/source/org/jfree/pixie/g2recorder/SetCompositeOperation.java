/**
 * Date: Mar 9, 2003
 * Time: 2:14:00 PM
 *
 * $Id: SetCompositeOperation.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Composite;
import java.awt.Graphics2D;

public class SetCompositeOperation implements G2Operation
{
  private Composite composite;

  public SetCompositeOperation (final Composite composite)
  {
    this.composite = composite;
  }

  public void draw (final Graphics2D g2)
  {
    g2.setComposite(composite);
  }
}

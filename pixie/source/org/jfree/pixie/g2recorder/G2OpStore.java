/**
 * Date: Mar 9, 2003
 * Time: 1:43:29 PM
 *
 * $Id$
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;

import com.jrefinery.ui.Drawable;

public class G2OpStore implements Drawable
{
  private static class StoreCarrier
  {
    private G2Recorder source;
    private G2Operation op;

    public StoreCarrier(G2Recorder source, G2Operation op)
    {
      if (source == null) throw new NullPointerException();
      if (op == null) throw new NullPointerException();

      this.source = source;
      this.op = op;
    }

    public G2Recorder getSource()
    {
      return source;
    }

    public G2Operation getOp()
    {
      return op;
    }
  }

  private ArrayList store;

  public G2OpStore()
  {
    store = new ArrayList();
  }

  public void addOperation (G2Recorder source, G2Operation operation)
  {
    store.add (new StoreCarrier(source, operation));
  }

  public void draw(Graphics2D graphics, Rectangle2D bounds)
  {
    Hashtable usedGraphics = new Hashtable();

    StoreCarrier[] scs = (StoreCarrier[]) store.toArray(new StoreCarrier[store.size()]);

    // the bounds are ignored ... we assume, that clipping is enabled by the caller..
    for (int i = 0; i < scs.length; i++)
    {
      StoreCarrier sc = scs[i];
      G2Recorder g2r = sc.getSource();
      Graphics2D g2 = (Graphics2D) usedGraphics.get(g2r);
      if (g2 == null)
      {
        g2 = (Graphics2D) graphics.create();
        usedGraphics.put(g2r, g2);
      }
      sc.getOp().draw(g2);
    }
  }
}

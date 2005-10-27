package org.jfree.report.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import org.jfree.ui.ExtendedDrawable;
import org.jfree.util.Log;

/**
 * Creation-Date: 11.10.2005, 14:03:15
 *
 * @author Thomas Morgner
 */
public class ComponentDrawable implements ExtendedDrawable
{
  private class PainterRunnable implements Runnable
  {
    private Rectangle2D area;
    private Graphics2D graphics;

    public PainterRunnable()
    {
    }

    public Graphics2D getGraphics()
    {
      return graphics;
    }

    public void setGraphics(final Graphics2D graphics)
    {
      this.graphics = graphics;
    }

    public Rectangle2D getArea()
    {
      return area;
    }

    public void setArea(final Rectangle2D area)
    {
      this.area = area;
    }

    public void run()
    {
      if (component instanceof Window)
      {
        component.addNotify();
      }
      else
      {
        peerSupply.pack();
        contentPane.add(component);
      }

      component.setBounds
              ((int) area.getX(), (int) area.getY(),
               (int) area.getWidth(), (int) area.getHeight());
      component.validate();
      component.paint(graphics);

    }
  }

  private boolean preserveAspectRatio;
  private Component component;
  private JFrame peerSupply;
  private JPanel contentPane;
  private PainterRunnable runnable;
  private boolean paintSynchronously;

  public ComponentDrawable()
  {
    peerSupply = new JFrame();
    peerSupply.pack(); // add a peer ...
    contentPane = new JPanel();
    contentPane.setLayout(null);
    peerSupply.setContentPane(contentPane);
    runnable = new PainterRunnable();
  }

  public boolean isPaintSynchronously()
  {
    return paintSynchronously;
  }

  public void setPaintSynchronously(final boolean paintSynchronously)
  {
    this.paintSynchronously = paintSynchronously;
  }

  private void cleanUp ()
  {
    if (component instanceof JComponent)
    {
      JComponent jc = (JComponent) component;
      RepaintManager.currentManager(jc).removeInvalidComponent(jc);
      RepaintManager.currentManager(jc).markCompletelyClean(jc);
    }
    contentPane.removeAll();
    RepaintManager.currentManager(contentPane).removeInvalidComponent(contentPane);
    RepaintManager.currentManager(contentPane).markCompletelyClean(contentPane);
    peerSupply.dispose();
  }

  public Component getComponent()
  {
    return component;
  }

  public void setComponent(final Component component)
  {
    this.component = component;
    prepareComponent(component);
  }

  public synchronized Dimension getPreferredSize()
  {
    if (component == null)
    {
      return new Dimension(0,0);
    }
    peerSupply.pack();
    if (component instanceof Window == false)
    {
      contentPane.add(component);
      contentPane.validate();
    }
    else
    {
      component.validate();
    }
    component.validate();
    final Dimension retval = component.getPreferredSize();
    cleanUp();
    return retval;
  }

  public void setPreserveAspectRatio(final boolean preserveAspectRatio)
  {
    this.preserveAspectRatio = preserveAspectRatio;
  }

  public boolean isPreserveAspectRatio()
  {
    return preserveAspectRatio;
  }

  public synchronized void draw(Graphics2D g2, Rectangle2D area)
  {
    if (component == null)
    {
      return;
    }

    runnable.setArea(area);
    runnable.setGraphics(g2);

    if (SwingUtilities.isEventDispatchThread() || paintSynchronously == false)
    {
      runnable.run();
    }
    else
    {
      try
      {
        SwingUtilities.invokeAndWait(runnable);
      }
      catch (Exception e)
      {
        Log.warn ("Failed to redraw the component.");
      }
    }

    cleanUp();
  }

  private void prepareComponent (Component c)
  {
    if (c instanceof JComponent)
    {
      final JComponent jc = (JComponent) c;
      jc.setDoubleBuffered(false);
      final Component[] childs = jc.getComponents();
      for (int i = 0; i < childs.length; i++)
      {
        final Component child = childs[i];
        prepareComponent(child);
      }
    }
  }

  public static void main(String[] args)
  {
    ComponentDrawable drawa = new ComponentDrawable();
    drawa.setComponent(new JFileChooser());
    BufferedImage bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D graphics = bi.createGraphics();
    drawa.draw(graphics, new Rectangle2D.Double(0, 0, 500, 500));
    graphics.dispose();
  }
}

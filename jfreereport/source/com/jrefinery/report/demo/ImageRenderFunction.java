/*
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 14.08.2002
 * Time: 20:49:54
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.jrefinery.report.demo;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.AbstractFunction;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageRenderFunction extends AbstractFunction
{
  private ImageReference functionValue;

  /**
   * Create a image according to the current state, simple and silly ...
   */
  public void pageStarted(ReportEvent event)
  {
    BufferedImage image = new BufferedImage(150, 50, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = image.createGraphics();
    JButton bt = new JButton("A Button");
    bt.setSize(90,20);
    JRadioButton radio = new JRadioButton("A radio button");
    radio.setSize(100,20);

    g2.setColor(Color.darkGray);
    bt.paint(g2);
    g2.setColor(Color.blue);
    g2.setTransform(AffineTransform.getTranslateInstance(20, 20));
    radio.paint(g2);
    g2.setTransform(AffineTransform.getTranslateInstance(0, 0));
    g2.setPaint(Color.green);
    g2.setFont(new Font ("Serif", Font.PLAIN, 10));
    g2.drawString("You are viewing a graphics of JFreeReport On index " + event.getState().getCurrentDisplayItem(), 10, 10);

    functionValue = new ImageReference(image);
  }

  public Object getValue()
  {
    return functionValue;
  }
}

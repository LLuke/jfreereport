/**
 *
 *  Date: 23.06.2002
 *  CursorTest.java
 *  ------------------------------
 *  23.06.2002 : ...
 */
package com.jrefinery.report.ext.junit.base;

import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.demo.SampleData1;
import com.jrefinery.report.ext.junit.bugs.ReportPropertyLostBug;
import com.jrefinery.report.targets.G2OutputTarget;
import com.jrefinery.report.Cursor;

import java.net.URL;
import java.io.FileNotFoundException;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.print.Paper;
import java.awt.print.PageFormat;

import junit.framework.TestCase;

public class CursorTest extends TestCase
{
  private G2OutputTarget target;

  public CursorTest (String s)
  {
    super (s);
  }

  protected void setUp () throws Exception
  {

    // set LandScape
    Paper paper = new Paper ();
    paper.setSize (595.275590551181d, 419.5275590551181);
    paper.setImageableArea (70.86614173228338, 70.86614173228347, 453.54330708661416, 277.8236220472441);

    PageFormat format = new PageFormat ();
    format.setOrientation (PageFormat.LANDSCAPE);
    format.setPaper (paper);
    target = new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), format);
  }

  public void testCursor ()
  {
    Cursor c = new Cursor(target);
    try
    {
      c.advance(-1);
      fail("No expected Exception thrown");
    }
    catch (IllegalArgumentException ae)
    {
    }
    c.advance(1000000);
    assertTrue(c.isSpaceFor(1) == false);

    c = new Cursor(target);
    try
    {
      c.advanceTo(-1);
      fail("No expected Exception thrown");
    }
    catch (IllegalArgumentException ae)
    {
    }
    c.advanceTo(1000000);
    assertTrue(c.isSpaceFor(1) == false);

    c = new Cursor(target);
    try
    {
      c.reserveSpace(-1);
      fail("No expected Exception thrown");
    }
    catch (IllegalArgumentException ae)
    {
    }
    c.reserveSpace(1000000);
    assertTrue(c.isSpaceFor(1) == false);

  }

}
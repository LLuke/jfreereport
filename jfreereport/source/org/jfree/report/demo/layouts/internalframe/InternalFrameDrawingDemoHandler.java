/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * InternalFrameDrawingDemoHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: InternalFrameDrawingDemoHandler.java,v 1.1 2005/12/11 12:47:05 taqua Exp $
 *
 * Changes
 * -------------------------
 * 11.12.2005 : Initial version
 */
package org.jfree.report.demo.layouts.internalframe;

import org.jfree.report.demo.helper.DemoHandler;
import org.jfree.report.demo.helper.PreviewHandler;
import org.jfree.report.JFreeReportBoot;

/**
 * Creation-Date: 11.12.2005, 12:33:51
 *
 * @author Thomas Morgner
 */
public class InternalFrameDrawingDemoHandler implements DemoHandler
{
  public InternalFrameDrawingDemoHandler()
  {
  }

  /**
   * A helper class to make this demo accessible from the DemoFrontend.
   */
  private class InternalFrameDrawingPreviewHandler implements PreviewHandler
  {
    public InternalFrameDrawingPreviewHandler()
    {
    }

    public void attemptPreview()
    {
      InternalFrameDemoFrame.main(new String[]{});
    }
  }

  public String getDemoName()
  {
    return "InternalFrame drawing";
  }

  public PreviewHandler getPreviewHandler()
  {
    return new InternalFrameDrawingPreviewHandler();
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();
    new InternalFrameDrawingDemoHandler().getPreviewHandler().attemptPreview();
  }
}

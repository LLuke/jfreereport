/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * BrushConstants.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BrushConstants.java,v 1.1 2003/02/25 20:58:07 taqua Exp $
 *
 * Changes
 * -------
 * 09-Mar-2003 : Initial version.
 */
package org.jfree.pixie.wmf.records;

import java.util.Hashtable;

/**
 * Manages the available WmfCommands and allows a generic command instantiation.
 */
public class CommandFactory
{
  private static CommandFactory commandFactory;

  public static CommandFactory getInstance ()
  {
    if (commandFactory == null)
    {
      commandFactory = new CommandFactory();
    }
    return commandFactory;
  }

  public CommandFactory()
  {
  }

  /** A global collection of all known record types. */
  private Hashtable recordTypes;

  /**
   * Registers all known command types to the standard factory.
   */
  public void registerAllKnownTypes ()
  {
    if (recordTypes != null)
    {
      return;
    }

    recordTypes = new Hashtable ();

    registerCommand (new MfCmdAnimatePalette ());
    registerCommand (new MfCmdArc ());
    registerCommand (new MfCmdBitBlt ());
    registerCommand (new MfCmdChord ());
    registerCommand (new MfCmdCreateBrush ());
    registerCommand (new MfCmdCreateDibPatternBrush ());
    registerCommand (new MfCmdCreateFont ());
    registerCommand (new MfCmdCreatePen ());
    registerCommand (new MfCmdCreatePalette ());
    registerCommand (new MfCmdCreatePatternBrush ());
    registerCommand (new MfCmdCreateRegion ());
    registerCommand (new MfCmdDeleteObject ());
    registerCommand (new MfCmdEllipse ());
    registerCommand (new MfCmdEscape ());
    registerCommand (new MfCmdExcludeClipRect ());
    registerCommand (new MfCmdExtFloodFill ());
    registerCommand (new MfCmdExtTextOut ());
    registerCommand (new MfCmdFillRegion ());
    registerCommand (new MfCmdFrameRegion ());
    registerCommand (new MfCmdFloodFill ());
    registerCommand (new MfCmdInvertRegion ());
    registerCommand (new MfCmdIntersectClipRect ());
    registerCommand (new MfCmdLineTo ());
    registerCommand (new MfCmdMoveTo ());
    registerCommand (new MfCmdOffsetClipRgn ());
    registerCommand (new MfCmdOffsetViewportOrg ());
    registerCommand (new MfCmdOffsetWindowOrg ());
    registerCommand (new MfCmdOldBitBlt ());
    registerCommand (new MfCmdOldStrechBlt ());
    registerCommand (new MfCmdPatBlt ());
    registerCommand (new MfCmdPaintRgn ());
    registerCommand (new MfCmdPie ());
    registerCommand (new MfCmdPolyPolygon ());
    registerCommand (new MfCmdPolygon ());
    registerCommand (new MfCmdPolyline ());
    registerCommand (new MfCmdRealisePalette ());
    registerCommand (new MfCmdRectangle ());
    registerCommand (new MfCmdRestoreDc ());
    registerCommand (new MfCmdResizePalette ());
    registerCommand (new MfCmdRoundRect ());
    registerCommand (new MfCmdSaveDc ());
    registerCommand (new MfCmdScaleWindowExt ());
    registerCommand (new MfCmdScaleViewportExt ());
    registerCommand (new MfCmdSelectClipRegion ());
    registerCommand (new MfCmdSelectObject ());
    registerCommand (new MfCmdSelectPalette ());
    registerCommand (new MfCmdSetBkMode ());
    registerCommand (new MfCmdSetBkColor ());
    registerCommand (new MfCmdSetDibitsToDevice ());
    registerCommand (new MfCmdSetMapperFlags ());
    registerCommand (new MfCmdSetMapMode ());
    registerCommand (new MfCmdSetPaletteEntries ());
    registerCommand (new MfCmdSetPolyFillMode ());
    registerCommand (new MfCmdSetPixel ());
    registerCommand (new MfCmdSetRop2 ());
    registerCommand (new MfCmdSetStretchBltMode ());
    registerCommand (new MfCmdSetTextCharExtra ());
    registerCommand (new MfCmdSetTextAlign ());
    registerCommand (new MfCmdSetTextColor ());
    registerCommand (new MfCmdSetTextJustification ());
    registerCommand (new MfCmdSetViewPortExt ());
    registerCommand (new MfCmdSetViewPortOrg ());
    registerCommand (new MfCmdSetWindowExt ());
    registerCommand (new MfCmdSetWindowOrg ());
    registerCommand (new MfCmdStretchBlt ());
    registerCommand (new MfCmdStretchDibits ());
    registerCommand (new MfCmdTextOut ());
  }

  private void registerCommand (MfCmd command)
  {
    if (recordTypes.get (new Integer (command.getFunction ())) != null)
    {
      throw new IllegalArgumentException ("Already registered");
    }

    recordTypes.put (new Integer (command.getFunction ()), command);
  }

  public MfCmd getCommand (int function)
  {
    if (recordTypes == null)
    {
      registerAllKnownTypes();
    }

    MfCmd cmd = (MfCmd) recordTypes.get (new Integer (function));
    if (cmd == null)
    {
      MfCmdUnknownCommand ucmd = new MfCmdUnknownCommand ();
      ucmd.setFunction (function);
      return ucmd;
    }
    return cmd.getInstance ();
  }
}

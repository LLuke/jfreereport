/**
 * ========================================
 * Pixie : a free Java vector image library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/pixie/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * CommandFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
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

  public CommandFactory ()
  {
  }

  /**
   * A global collection of all known record types.
   */
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

    recordTypes = new Hashtable();

    registerCommand(new MfCmdAnimatePalette());
    registerCommand(new MfCmdArc());
    registerCommand(new MfCmdDibBitBlt());
    registerCommand(new MfCmdChord());
    registerCommand(new MfCmdCreateBrush());
    registerCommand(new MfCmdCreateDibPatternBrush());
    registerCommand(new MfCmdCreateFont());
    registerCommand(new MfCmdCreatePen());
    registerCommand(new MfCmdCreatePalette());
    registerCommand(new MfCmdCreatePatternBrush());
    registerCommand(new MfCmdCreateRegion());
    registerCommand(new MfCmdDeleteObject());
    registerCommand(new MfCmdEllipse());
    registerCommand(new MfCmdEscape());
    registerCommand(new MfCmdExcludeClipRect());
    registerCommand(new MfCmdExtFloodFill());
    registerCommand(new MfCmdExtTextOut());
    registerCommand(new MfCmdFillRegion());
    registerCommand(new MfCmdFrameRegion());
    registerCommand(new MfCmdFloodFill());
    registerCommand(new MfCmdInvertRegion());
    registerCommand(new MfCmdIntersectClipRect());
    registerCommand(new MfCmdLineTo());
    registerCommand(new MfCmdMoveTo());
    registerCommand(new MfCmdOffsetClipRgn());
    registerCommand(new MfCmdOffsetViewportOrg());
    registerCommand(new MfCmdOffsetWindowOrg());
    registerCommand(new MfCmdBitBlt());
    registerCommand(new MfCmdStretchBlt());
    registerCommand(new MfCmdPatBlt());
    registerCommand(new MfCmdPaintRgn());
    registerCommand(new MfCmdPie());
    registerCommand(new MfCmdPolyPolygon());
    registerCommand(new MfCmdPolygon());
    registerCommand(new MfCmdPolyline());
    registerCommand(new MfCmdRealisePalette());
    registerCommand(new MfCmdRectangle());
    registerCommand(new MfCmdRestoreDc());
    registerCommand(new MfCmdResizePalette());
    registerCommand(new MfCmdRoundRect());
    registerCommand(new MfCmdSaveDc());
    registerCommand(new MfCmdScaleWindowExt());
    registerCommand(new MfCmdScaleViewportExt());
    registerCommand(new MfCmdSelectClipRegion());
    registerCommand(new MfCmdSelectObject());
    registerCommand(new MfCmdSelectPalette());
    registerCommand(new MfCmdSetBkMode());
    registerCommand(new MfCmdSetBkColor());
    registerCommand(new MfCmdSetDibitsToDevice());
    registerCommand(new MfCmdSetMapperFlags());
    registerCommand(new MfCmdSetMapMode());
    registerCommand(new MfCmdSetPaletteEntries());
    registerCommand(new MfCmdSetPolyFillMode());
    registerCommand(new MfCmdSetPixel());
    registerCommand(new MfCmdSetRop2());
    registerCommand(new MfCmdSetStretchBltMode());
    registerCommand(new MfCmdSetTextCharExtra());
    registerCommand(new MfCmdSetTextAlign());
    registerCommand(new MfCmdSetTextColor());
    registerCommand(new MfCmdSetTextJustification());
    registerCommand(new MfCmdSetViewPortExt());
    registerCommand(new MfCmdSetViewPortOrg());
    registerCommand(new MfCmdSetWindowExt());
    registerCommand(new MfCmdSetWindowOrg());
    registerCommand(new MfCmdDibStretchBlt());
    registerCommand(new MfCmdStretchDibits());
    registerCommand(new MfCmdTextOut());
  }

  private void registerCommand (final MfCmd command)
  {
    if (recordTypes.get(new Integer(command.getFunction())) != null)
    {
      throw new IllegalArgumentException("Already registered");
    }

    recordTypes.put(new Integer(command.getFunction()), command);
  }

  public MfCmd getCommand (final int function)
  {
    if (recordTypes == null)
    {
      registerAllKnownTypes();
    }

    final MfCmd cmd = (MfCmd) recordTypes.get(new Integer(function));
    if (cmd == null)
    {
      final MfCmdUnknownCommand ucmd = new MfCmdUnknownCommand();
      ucmd.setFunction(function);
      return ucmd;
    }
    return cmd.getInstance();
  }
}

/* =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * PrintAction.java
 * ----------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.action;

import java.util.ResourceBundle;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.PreviewFrame;

/**
 * Print action for a print preview frame.
 */
public class PrintAction extends AbstractAction implements Runnable {

    /** The preview frame that this action is assigned to. */
    protected PreviewFrame frame;

    /**
     * Constructs a new action.
     */
    public PrintAction(PreviewFrame frame, ResourceBundle resources) {

        this.frame = frame;

        String name = resources.getString("action.print.name");
        this.putValue(Action.NAME, name);

        String description = resources.getString("action.print.description");
        this.putValue(Action.SHORT_DESCRIPTION, description);

        Integer mnemonic = (Integer)resources.getObject("action.print.mnemonic");
        this.putValue(Action.MNEMONIC_KEY, mnemonic);

        KeyStroke accelerator = (KeyStroke)resources.getObject("action.print.accelerator");
        this.putValue(Action.ACCELERATOR_KEY, accelerator);

        ImageIcon icon16 = new ImageIcon(PrintAction.class.getResource("Print16.gif"));
        this.putValue(Action.SMALL_ICON, icon16);

        ImageIcon icon24 = new ImageIcon(PrintAction.class.getResource("Print24.gif"));
        this.putValue("ICON24", icon24);

        this.putValue(Action.ACTION_COMMAND_KEY, JFreeReportConstants.PRINT_COMMAND);

    }

    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(this);
    }

    public void run() {
        frame.attemptPrint();
    }

}
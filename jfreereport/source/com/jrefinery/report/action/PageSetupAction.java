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
 * --------------------
 * PageSetupAction.java
 * --------------------
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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.PreviewFrame;

/**
 * Page setup action for a print preview frame.
 */
public class PageSetupAction extends AbstractAction implements Runnable {

    /** The frame that this action is assigned to. */
    protected PreviewFrame frame;

    /**
     * Constructs a new action.
     */
    public PageSetupAction(PreviewFrame frame, ResourceBundle resources) {

        this.frame = frame;

        String name = resources.getString("action.page-setup.name");
        this.putValue(Action.NAME, name);

        String description = resources.getString("action.page-setup.description");
        this.putValue(Action.SHORT_DESCRIPTION, description);

        Integer mnemonic = (Integer)resources.getObject("action.page-setup.mnemonic");
        this.putValue(Action.MNEMONIC_KEY, mnemonic);

        ImageIcon icon16 = new ImageIcon(PageSetupAction.class.getResource("PageSetup16.gif"));
        this.putValue(Action.SMALL_ICON, icon16);

        ImageIcon icon24 = new ImageIcon(PageSetupAction.class.getResource("PageSetup24.gif"));
        this.putValue("ICON24", icon24);

        this.putValue(Action.ACTION_COMMAND_KEY, JFreeReportConstants.PAGE_SETUP_COMMAND);

    }

    /**
     * Handles a page setup action.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(this);
    }

    /**
     * Triggers a page setup.
     */
    public void run() {

        if (frame!=null) {
            frame.attemptPageSetup();
        }
        else {
            System.out.println("PageSetupAction: trying to run against a null frame.");
        }

    }

}
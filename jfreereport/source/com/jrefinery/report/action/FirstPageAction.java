package com.jrefinery.report.action;

import com.jrefinery.report.JFreeReportConstants;

import javax.swing.AbstractAction;
import javax.swing.Action;

import java.util.ResourceBundle;

/**
 * @author js
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public abstract class FirstPageAction extends AbstractAction
{
 
  public FirstPageAction(ResourceBundle resources)
  {
    putValue(Action.NAME, resources.getString("action.firstpage.name"));
    putValue(Action.SHORT_DESCRIPTION, resources.getString("action.firstpage.description"));
    putValue(Action.MNEMONIC_KEY, resources.getObject("action.firstpage.mnemonic"));
    putValue(Action.ACCELERATOR_KEY, resources.getObject("action.firstpage.accelerator"));
    putValue(Action.SMALL_ICON, resources.getObject("action.firstpage.small-icon"));
    putValue("ICON24", resources.getObject("action.firstpage.icon"));

    putValue(Action.ACTION_COMMAND_KEY, JFreeReportConstants.FIRSTPAGE_COMMAND);
  }
}

/**
 *
 *  Date: 31.05.2002
 *  GotoPageAction.java
 *  ------------------------------
 *  31.05.2002 : ...
 */
package com.jrefinery.report.action;

import com.jrefinery.report.JFreeReportConstants;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.util.ResourceBundle;

public abstract class GotoPageAction extends AbstractAction
{
  /**
   * Constructs a new action.
   *
   * @param resources Localised resources for the action.
   */
  public GotoPageAction(ResourceBundle resources)
  {
    this.putValue(Action.NAME, resources.getString("action.gotopage.name"));
    this.putValue(Action.SHORT_DESCRIPTION, resources.getString("action.gotopage.description"));
    this.putValue(Action.MNEMONIC_KEY, resources.getObject("action.gotopage.mnemonic"));
    this.putValue(Action.ACCELERATOR_KEY, resources.getObject("action.gotopage.accelerator"));
    this.putValue(Action.ACTION_COMMAND_KEY, JFreeReportConstants.GOTO_COMMAND);
  }

}
/**
 * ----------------------
 * AbstractActionDowngrade.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.util;

import javax.swing.Action;
import javax.swing.AbstractAction;

/**
 * Defines the 2 new constants introduced by Sun in version 1.3 of the J2SDK.
 */
public abstract class AbstractActionDowngrade extends AbstractAction implements ActionDowngrade
{
  /**
   * The key used for storing a <code>KeyStroke</code> to be used as the
   * accelerator for the action.
   */
  public static final String ACCELERATOR_KEY="AcceleratorKey";

  /**
   * The key used for storing an int key code to be used as the mnemonic
   * for the action.
   */
  public static final String MNEMONIC_KEY="MnemonicKey";
}

package org.jfree.report.modules.gui.common;

import java.util.Locale;

import org.jfree.util.Configuration;

/**
 * Creation-Date: 16.11.2006, 17:06:38
 *
 * @author Thomas Morgner
 */
public interface GuiContext
{
  Locale getLocale();

  IconTheme getIconTheme();

  Configuration getConfiguration();
}

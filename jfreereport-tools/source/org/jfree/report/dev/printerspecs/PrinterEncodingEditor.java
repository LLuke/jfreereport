package org.jfree.report.dev.printerspecs;

import java.util.Properties;
import java.io.File;
import java.io.IOException;
import javax.swing.JMenu;

import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.ui.tabbedui.AbstractTabbedUI;
import org.jfree.ui.tabbedui.TabbedFrame;
import org.jfree.report.modules.misc.configstore.base.ConfigFactory;
import org.jfree.report.modules.misc.configstore.base.ConfigStorage;
import org.jfree.report.modules.misc.configstore.base.ConfigStoreException;
import org.jfree.report.JFreeReportBoot;
import org.jfree.util.Log;

public class PrinterEncodingEditor extends AbstractTabbedUI
{
  private JMenu[] prefixMenu;
  private static final String CONFIG_PATH = "printer_specification_editor";

  public PrinterEncodingEditor ()
  {
    JFreeReportBoot.getInstance().start();

    prefixMenu = new JMenu[1];
    prefixMenu[0] = new JMenu("File");
    prefixMenu[0].add(new ActionMenuItem(getCloseAction()));

    initializeModels();

    setGlobalMenu(true);
//    addRootEditor(new EpsonEncodingsRootEditor(true));
    addRootEditor(new EpsonEncodingsRootEditor(false));
  }

  protected void initializeModels ()
  {
    final EditablePrinterSpecificationLoader loader =
            new EditablePrinterSpecificationLoader();
    final ConfigStorage cs = ConfigFactory.getInstance().getUserStorage();
    final PrinterEditorData data = PrinterEditorData.getInstance();
    if (cs.existsProperties(CONFIG_PATH) == false)
    {
      return;
    }
    try
    {

      final Properties p = cs.loadProperties(CONFIG_PATH, new Properties());
      final String lru9Pin = p.getProperty("lru.9pin");
      if (lru9Pin != null)
      {
        loader.load(data.getNinePinPrinters(), new File(lru9Pin));
        data.setLruNinePinPrinterFile(lru9Pin);
      }
      final String lru24Pin = p.getProperty("lru.24pin");
      if (lru24Pin != null)
      {
        loader.load(data.getTwentyfourPinPrinters(), new File(lru24Pin));
        data.setLruTwentyfourPinPrinterFile(lru24Pin);
      }
    }
    catch (ConfigStoreException e)
    {
      Log.debug ("Failed to load configuration.", e);
    }
    catch (IOException e)
    {
      Log.debug ("Failed to load configuration.", e);
    }

  }

  /**
   * Attempts to exit.
   */
  protected void attempExit ()
  {
    final Properties p = new Properties ();
    final PrinterEditorData data = PrinterEditorData.getInstance();
    if (data.getLruNinePinPrinterFile() != null)
    {
      p.setProperty("lru.9pin", data.getLruNinePinPrinterFile());
    }
    if (data.getLruTwentyfourPinPrinterFile() != null)
    {
      p.setProperty("lru.24pin", data.getLruTwentyfourPinPrinterFile());
    }

    final ConfigStorage cs = ConfigFactory.getInstance().getUserStorage();
    try
    {
      cs.storeProperties(CONFIG_PATH, p);
    }
    catch (ConfigStoreException e)
    {
      Log.info ("Storing the configuration failed.");
    }
    System.exit(0);
  }

  /**
   * The postfix menus.
   *
   * @return The postfix menus.
   */
  protected JMenu[] getPostfixMenus ()
  {
    return new JMenu[0];
  }

  /**
   * Returns the prefix menus.
   *
   * @return The prefix menus.
   */
  protected JMenu[] getPrefixMenus ()
  {
    return prefixMenu;
  }

  public static void main (final String[] args)
  {
    final TabbedFrame frame = new TabbedFrame("Printer Encoding Editor");
    frame.init(new PrinterEncodingEditor());
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}

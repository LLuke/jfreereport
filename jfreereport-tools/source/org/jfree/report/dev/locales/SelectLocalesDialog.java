package org.jfree.report.dev.locales;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jfree.base.BaseBoot;
import org.jfree.ui.KeyedComboBoxModel;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.Log;

public class SelectLocalesDialog extends JDialog
{
  private class ConfirmAction extends AbstractAction implements ListDataListener
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public ConfirmAction ()
    {
      putValue(NAME, "OK");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      confirmed = true;
      setVisible(false);
    }

    /**
     * Sent when the contents of the list has changed in a way that's too complex to
     * characterize with the previous methods. For example, this is sent when an item has
     * been replaced. Index0 and index1 bracket the change.
     *
     * @param e a <code>ListDataEvent</code> encapsulating the event information
     */
    public void contentsChanged (final ListDataEvent e)
    {
      final Object selectedKey = languagesModel.getSelectedKey();
      final boolean noSelection = selectedKey == null ||
              selectedKey.equals("*");
      if (noSelection)
      {
        setEnabled(false);
        return;
      }

      final String locale = getSelectedLocaleName();
      if (invalidLocalesModel.contains(locale))
      {
        setEnabled(false);
      }
      else
      {
        setEnabled(true);
      }
    }

    /**
     * Sent after the indices in the index0,index1 interval have been inserted in the data
     * model. The new interval includes both index0 and index1.
     *
     * @param e a <code>ListDataEvent</code> encapsulating the event information
     */
    public void intervalAdded (final ListDataEvent e)
    {
    }

    /**
     * Sent after the indices in the index0,index1 interval have been removed from the data
     * model.  The interval includes both index0 and index1.
     *
     * @param e a <code>ListDataEvent</code> encapsulating the event information
     */
    public void intervalRemoved (final ListDataEvent e)
    {
    }
  }

  private class CancelAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public CancelAction ()
    {
      putValue(NAME, "Cancel");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      confirmed = false;
      setVisible(false);
    }
  }

  private static final String EMPTY_SELECTION_KEY = "*";

  private boolean confirmed;
  private DefaultListModel invalidLocalesModel;

  private ConfirmAction confirmAction;
  private CancelAction cancelAction;
  private KeyedComboBoxModel languagesModel;
  private KeyedComboBoxModel countryModel;

  public SelectLocalesDialog (final JFrame parent)
  {
    super(parent);
    init();
  }

  public SelectLocalesDialog ()
  {
    init();
  }

  private void init()
  {
    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    setTitle("Select Locales ..");

    confirmAction = new ConfirmAction();
    confirmAction.setEnabled(false);
    cancelAction = new CancelAction();

    countryModel = createCountryModel();
    countryModel.addListDataListener(confirmAction);
    languagesModel = createLanguageModel();
    languagesModel.addListDataListener(confirmAction);

    invalidLocalesModel = new DefaultListModel();

    final JLabel selectLocalesLabel = new JLabel
            ("Enter a new locale specification.");
    final JComboBox locales = new JComboBox(languagesModel);
    final JComboBox countries = new JComboBox(countryModel);
    final JList definedCountries = new JList(invalidLocalesModel);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 4;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets (5,5,5,5);
    contentPane.add (selectLocalesLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets (5,5,5,5);
    contentPane.add (new JLabel ("Language"), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets (5,5,5,5);
    contentPane.add (locales, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets (5,5,5,5);
    contentPane.add (new JLabel ("Country (optional)"), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets (5,5,5,5);
    contentPane.add (countries, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 4;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets (5,5,2,5);
    contentPane.add (new JLabel ("Existing locales"), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 4;
    gbc.weightx = 2;
    gbc.weighty = 2;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets (2,5,5,5);
    contentPane.add (new JScrollPane (definedCountries), gbc);

    final JPanel buttonPane = new JPanel();
    buttonPane.setLayout(new GridLayout(1,2, 5,5 ));
    buttonPane.add (new ActionButton (confirmAction));
    buttonPane.add (new ActionButton (cancelAction));

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 4;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets (5,5,5,5);
    contentPane.add (buttonPane, gbc);

    setContentPane(contentPane);
  }

  public String[] getInvalidLocales ()
  {
    final String[] retval = new String[invalidLocalesModel.size()];
    invalidLocalesModel.copyInto(retval);
    return retval;
  }

  public void setInvalidLocales (final String[] invalidLocales)
  {
    for (int i = 0; i < invalidLocales.length; i++)
    {
      invalidLocalesModel.addElement(invalidLocales[i]);
    }
  }

  public boolean isConfirmed ()
  {
    return confirmed;
  }

  public String getSelectedLocaleName ()
  {
    final String selectedLang = (String) languagesModel.getSelectedKey();
    if (selectedLang == null || EMPTY_SELECTION_KEY.equals(selectedLang))
    {
      return null;
    }
    final String selectedCountry = (String) countryModel.getSelectedKey();
    if (selectedCountry == null || EMPTY_SELECTION_KEY.equals(selectedCountry))
    {
      return selectedLang;
    }
    return selectedLang + "_" + selectedCountry;
  }

  public String performQuery()
  {
    setModal(true);
    setVisible(true);
    if (confirmed)
    {
      return getSelectedLocaleName();
    }
    return null;
  }

  private KeyedComboBoxModel createLanguageModel ()
  {
    final KeyedComboBoxModel retval = new KeyedComboBoxModel();
    retval.add(EMPTY_SELECTION_KEY, "");
    try
    {
      final LanguageCollection lc = new LanguageCollection();
      lc.load();
      final String[][] langs = lc.getLanguages();
      for (int i = 0; i < langs.length; i++)
      {
        retval.add(langs[i][0], langs[i][0] + " - " + langs[i][1]);
      }
    }
    catch(IOException ioe)
    {
      Log.warn ("Unable to load the languages.", ioe);
    }
    return retval;
  }

  private KeyedComboBoxModel createCountryModel ()
  {
    final KeyedComboBoxModel retval = new KeyedComboBoxModel();
    retval.add(EMPTY_SELECTION_KEY, "");
    try
    {
      final CountryCollection lc = new CountryCollection();
      lc.load();
      final String[][] langs = lc.getCountries();
      for (int i = 0; i < langs.length; i++)
      {
        retval.add(langs[i][0], langs[i][0] + " - " + langs[i][1]);
      }
    }
    catch(IOException ioe)
    {
      Log.warn ("Unable to load the languages.", ioe);
    }
    return retval;
  }

  public String selectLocale ()
  {
    confirmed = false;
    countryModel.setSelectedKey(null);
    languagesModel.setSelectedKey(null);
    setModal(true);
    setVisible(true);
    if (confirmed == false)
    {
      return null;
    }
    return getSelectedLocaleName();
  }

  public static void main (final String[] args)
  {
    BaseBoot.getInstance().start();
    final SelectLocalesDialog dialog = new SelectLocalesDialog(new JFrame());
    dialog.setModal(true);
    dialog.pack();
    Log.debug (dialog.selectLocale());
    System.exit(0);
  }
}

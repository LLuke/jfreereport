package org.jfree.report.modules.gui.plaintext;

import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jfree.report.modules.gui.base.components.EncodingComboBoxModel;
import org.jfree.report.modules.output.pageable.plaintext.PrinterEncoding;
import org.jfree.report.modules.output.pageable.plaintext.PrinterSpecification;
import org.jfree.report.util.EncodingSupport;

public class EncodingSelector extends JPanel
{
  public static class GenericPrinterSpecification
          implements PrinterSpecification
  {
    public GenericPrinterSpecification ()
    {
    }

    public String getDisplayName ()
    {
      return getName();
    }

    /**
     * Returns the encoding definition for the given java encoding.
     *
     * @param encoding the java encoding that should be mapped into a printer specific
     *                 encoding.
     * @return the printer specific encoding.
     *
     * @throws IllegalArgumentException if the given encoding is not supported.
     */
    public PrinterEncoding getEncoding (final String encoding)
    {
      if (isEncodingSupported(encoding) == false)
      {
        throw new IllegalArgumentException("Encoding is not supported.");
      }

      return new PrinterEncoding(encoding, encoding, encoding, new byte[0]);
    }

    /**
     * Returns the name of the encoding mapping. This is usually the same as the printer
     * model name.
     *
     * @return the printer model.
     */
    public String getName ()
    {
      return "Generic Printer";
    }

    /**
     * Checks, whether the given Java-encoding is supported.
     *
     * @param encoding the java encoding that should be mapped into a printer specific
     *                 encoding.
     * @return true, if there is a mapping, false otherwise.
     */
    public boolean isEncodingSupported (final String encoding)
    {
      if (EncodingSupport.isSupportedEncoding(encoding))
      {
        // if already checked there, then use it ...
        return true;
      }
      return false;
    }

    /**
     * Returns true, if a given operation is supported, false otherwise.
     *
     * @param operationName the operation, that should be performed
     * @return true, if the printer will be able to perform that operation, false
     *         otherwise.
     */
    public boolean isFeatureAvailable (final String operationName)
    {
      // we accept the first default that is offered, we do not even check the operation
      return true;
    }
  }

  private EncodingComboBoxModel encodingComboBoxModel;
  private JComboBox encodingComboBox;

  /**
   * Create a new JPanel with a double buffer and a flow layout
   */
  public EncodingSelector ()
  {
    setLayout(new BorderLayout());
    encodingComboBox = new JComboBox();
    add(encodingComboBox, BorderLayout.CENTER);
    setEncodings(new GenericPrinterSpecification());
  }

  public String getSelectedEncoding ()
  {
    return encodingComboBoxModel.getSelectedEncoding();
  }

  public void setSelectedEncoding (final String encoding)
  {
    this.encodingComboBoxModel.setSelectedEncoding(encoding);
  }

  public void setEncodings (final PrinterSpecification printerSpecification)
  {
    if (printerSpecification == null)
    {
      throw new NullPointerException("Specification must not be null.");
    }
    final EncodingComboBoxModel defaultEncodingModel = EncodingComboBoxModel.createDefaultModel();

    final EncodingComboBoxModel retval = new EncodingComboBoxModel();
    for (int i = 0; i < defaultEncodingModel.getSize(); i++)
    {
      final String encoding = defaultEncodingModel.getEncoding(i);
      if (printerSpecification.isEncodingSupported(encoding))
      {
        final String description = defaultEncodingModel.getDescription(i);
        retval.addEncoding(encoding, description);
      }
    }
    retval.sort();
    final Object oldSelectedValue = encodingComboBox.getSelectedItem();
    encodingComboBox.setModel(retval);
    encodingComboBoxModel = retval;
    encodingComboBoxModel.setSelectedItem(oldSelectedValue);
  }
}

package org.jfree.report.dev.printerspecs;

import org.jfree.report.modules.output.pageable.plaintext.DefaultPrinterSpecification;

public class ModifiablePrinterSpecification extends DefaultPrinterSpecification
{
  private String name;
  private String displayName;

  public ModifiablePrinterSpecification (final String name, final String displayName)
  {
    super(name, displayName);
    this.name = name;
    this.displayName = displayName;
  }

  public String getDisplayName ()
  {
    return displayName;
  }

  /**
   * Returns the name of the encoding mapping. This is usually the same as the printer
   * model name.
   *
   * @return the printer model.
   */
  public String getName ()
  {
    return name;
  }

  public void setDisplayName (final String displayName)
  {
    this.displayName = displayName;
  }

  public void setName (final String name)
  {
    this.name = name;
  }
}

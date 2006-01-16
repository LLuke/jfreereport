package org.jfree.report.ext.junit.bugs;

import java.text.MessageFormat;
import java.text.Format;

public class MessageFormatTest
{
  public static void main (String[] args)
  {
    MessageFormat format = new MessageFormat("{1} {0,number,integer}");
    System.out.println(format.format(new Object[]{new Integer (1), new Integer (1)}));

    Format[] fmt = format.getFormatsByArgumentIndex();

    for (int i = 0; i < fmt.length; i++)
    {
      Format format1 = fmt[i];
      System.out.println(format1);

    }

    format.setFormat(1, null);
    System.out.println(format.format(new Object[]{"-", "a"}));
  }
}

/**
 * Date: Feb 12, 2003
 * Time: 2:46:10 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyReferenceGenerator;
import com.jrefinery.report.io.ext.factory.objects.ObjectReferenceGenerator;

public class ReferenceDocGenerator
{
  public static void main (String [] args)
  {
    StyleKeyReferenceGenerator.main(args);
    ObjectReferenceGenerator.main(args);
  }
}

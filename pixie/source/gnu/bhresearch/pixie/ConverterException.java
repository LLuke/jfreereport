// ConverterException.java - Error reporting for Pixie converter.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

package gnu.bhresearch.pixie;

/**
 Error reporting for Pixie converter.
 */
public class ConverterException extends Exception
{
  public ConverterException (String mes)
  {
    super (mes);
  }
}

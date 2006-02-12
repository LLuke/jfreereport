package org.jfree.layouting.layouter.counters.numeric;

public class CambodianCounterStyle extends NumericCounterStyle
{
  public CambodianCounterStyle ()
  {
    super(10, ".");
    setReplacementChar('0', '\u17e0');
    setReplacementChar('1', '\u17e1');
    setReplacementChar('2', '\u17e2');
    setReplacementChar('3', '\u17e3');
    setReplacementChar('4', '\u17e4');
    setReplacementChar('5', '\u17e5');
    setReplacementChar('6', '\u17e6');
    setReplacementChar('7', '\u17e7');
    setReplacementChar('8', '\u17e8');
    setReplacementChar('9', '\u17e9');
  }


}

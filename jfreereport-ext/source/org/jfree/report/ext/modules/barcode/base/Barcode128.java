package org.jfree.report.ext.modules.barcode.base;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.jfree.report.ext.modules.barcode.Barcode;

/**
 * Created by IntelliJ IDEA. User: Administrateur Date: 7 avr. 2004 Time: 00:31:55 To
 * change this template use File | Settings | File Templates.
 */
public class Barcode128 extends Barcode
{

  private static final byte[] STOP = {2, 3, 3, 1, 1, 1, 2};

  private ArrayList codeTable;

  private static final byte[][] TABLE = {
    {2, 1, 2, 2, 2, 2},
    {2, 2, 2, 1, 2, 2},
    {2, 2, 2, 2, 2, 1},
    {1, 2, 1, 2, 2, 3},
    {1, 2, 1, 3, 2, 2},
    {1, 3, 1, 2, 2, 2},
    {1, 2, 2, 2, 1, 3},
    {1, 2, 2, 3, 1, 2},
    {1, 3, 2, 2, 1, 2},
    {2, 2, 1, 2, 1, 3},
    {2, 2, 1, 3, 1, 2},
    {2, 3, 1, 2, 1, 2},
    {1, 1, 2, 2, 3, 2},
    {1, 2, 2, 1, 3, 2},
    {1, 2, 2, 2, 3, 1},
    {1, 1, 3, 2, 2, 2},
    {1, 2, 3, 1, 2, 2},
    {1, 2, 3, 2, 2, 1},
    {2, 2, 3, 2, 1, 1},
    {2, 2, 1, 1, 3, 2},
    {2, 2, 1, 2, 3, 1},
    {2, 1, 3, 2, 1, 2},
    {2, 2, 3, 1, 1, 2},
    {3, 1, 2, 1, 3, 1},
    {3, 1, 1, 2, 2, 2},
    {3, 2, 1, 1, 2, 2},
    {3, 2, 1, 2, 2, 1},
    {3, 1, 2, 2, 1, 2},
    {3, 2, 2, 1, 1, 2},
    {3, 2, 2, 2, 1, 1},
    {2, 1, 2, 1, 2, 3},
    {2, 1, 2, 3, 2, 1},
    {2, 3, 2, 1, 2, 1},
    {1, 1, 1, 3, 2, 3},
    {1, 3, 1, 1, 2, 3},
    {1, 3, 1, 3, 2, 1},
    {1, 1, 2, 3, 1, 3},
    {1, 3, 2, 1, 1, 3},
    {1, 3, 2, 3, 1, 1},
    {2, 1, 1, 3, 1, 3},
    {2, 3, 1, 1, 1, 3},
    {2, 3, 1, 3, 1, 1},
    {1, 1, 2, 1, 3, 3},
    {1, 1, 2, 3, 3, 1},
    {1, 3, 2, 1, 3, 1},
    {1, 1, 3, 1, 2, 3},
    {1, 1, 3, 3, 2, 1},
    {1, 3, 3, 1, 2, 1},
    {3, 1, 3, 1, 2, 1},
    {2, 1, 1, 3, 3, 1},
    {2, 3, 1, 1, 3, 1},
    {2, 1, 3, 1, 1, 3},
    {2, 1, 3, 3, 1, 1},
    {2, 1, 3, 1, 3, 1},
    {3, 1, 1, 1, 2, 3},
    {3, 1, 1, 3, 2, 1},
    {3, 3, 1, 1, 2, 1},
    {3, 1, 2, 1, 1, 3},
    {3, 1, 2, 3, 1, 1},
    {3, 3, 2, 1, 1, 1},
    {3, 1, 4, 1, 1, 1},
    {2, 2, 1, 4, 1, 1},
    {4, 3, 1, 1, 1, 1},
    {1, 1, 1, 2, 2, 4},
    {1, 1, 1, 4, 2, 2},
    {1, 2, 1, 1, 2, 4},
    {1, 2, 1, 4, 2, 1},
    {1, 4, 1, 1, 2, 2},
    {1, 4, 1, 2, 2, 1},
    {1, 1, 2, 2, 1, 4},
    {1, 1, 2, 4, 1, 2},
    {1, 2, 2, 1, 1, 4},
    {1, 2, 2, 4, 1, 1},
    {1, 4, 2, 1, 1, 2},
    {1, 4, 2, 2, 1, 1},
    {2, 4, 1, 2, 1, 1},
    {2, 2, 1, 1, 1, 4},
    {4, 1, 3, 1, 1, 1},
    {2, 4, 1, 1, 1, 2},
    {1, 3, 4, 1, 1, 1},
    {1, 1, 1, 2, 4, 2},
    {1, 2, 1, 1, 4, 2},
    {1, 2, 1, 2, 4, 1},
    {1, 1, 4, 2, 1, 2},
    {1, 2, 4, 1, 1, 2},
    {1, 2, 4, 2, 1, 1},
    {4, 1, 1, 2, 1, 2},
    {4, 2, 1, 1, 1, 2},
    {4, 2, 1, 2, 1, 1},
    {2, 1, 2, 1, 4, 1},
    {2, 1, 4, 1, 2, 1},
    {4, 1, 2, 1, 2, 1},
    {1, 1, 1, 1, 4, 3},
    {1, 1, 1, 3, 4, 1},
    {1, 3, 1, 1, 4, 1},
    {1, 1, 4, 1, 1, 3},
    {1, 1, 4, 3, 1, 1},
    {4, 1, 1, 1, 1, 3},
    {4, 1, 1, 3, 1, 1},
    {1, 1, 3, 1, 4, 1},
    {1, 1, 4, 1, 3, 1},
    {3, 1, 1, 1, 4, 1},
    {4, 1, 1, 1, 3, 1},
    {2, 1, 1, 4, 1, 2},
    {2, 1, 1, 2, 1, 4},
    {2, 1, 1, 2, 3, 2}
  };

  private static final int CODESET_AB_TO_C = 99;
  private static final int CODESET_BC_TO_A = 101;
  private static final int CODESET_CA_TO_B = 100;
  private static final int SHIFT_AB = 98;
  private static final int START_A = 103;
  private static final int START_B = 104;
  private static final int START_C = 105;


  /**
   * @param code
   * @deprecated
   */
  public Barcode128 (final String code)
  {
    this.codeTable = new ArrayList();

    final int start = this.getFirstFourCharType(code);
    this.codeTable.add(TABLE[start]);   //the start character

    int currentSet;
    if (start == START_B)
    {
      currentSet = CODESET_CA_TO_B;
    }
    else if (start == START_C)
    {
      currentSet = CODESET_AB_TO_C;
    }
    else
    {    //START A
      currentSet = CODESET_BC_TO_A;
    }

    int count = start;  //the checksum

    final char[] codeChars = code.toCharArray();
    for (int i = 0, index = 1; i < codeChars.length; i++, index++)
    {
      final char tmpChar = codeChars[i];
      if (tmpChar >= 0 && tmpChar < 128)
      {

        final int tmpSet = this.getNextFourCharType(i, code, currentSet);
        final int tmpCharSet = this.needToShift(tmpChar, currentSet);

        if (tmpSet != currentSet)
        {  //need to change the codeset
          this.codeTable.add(TABLE[tmpSet]);
          count += index * tmpSet;
          index++;
        }
        currentSet = tmpSet;

        if (currentSet != CODESET_AB_TO_C)
        {
          if (tmpCharSet != currentSet)
          {  // add the shift if necessary
            this.codeTable.add(TABLE[tmpCharSet]);
            count += index * tmpCharSet;
            index++;
          }
          final int tmpCharInt = this.getCharIndex(tmpChar, tmpCharSet);
          this.codeTable.add(TABLE[tmpCharInt]);
          count += index * tmpCharInt;

        }
        else
        { //codeset C
          final int tmpCharInt = this.getNumberIndex(code, i);
          this.codeTable.add(TABLE[tmpCharInt]);
          count += index * tmpCharInt;
          index++;
          i += 1;

          /*tmpCharInt = this.getNumberIndex(code, i);
          this.codeTable.add(TABLE[tmpCharInt]);
          count += index*tmpCharInt;
          i+=1;*/
        }
        /* } else { //add the char with the current codeset
        if(tmpCharSet != currentSet) {
        this.codeTable.add(TABLE[tmpCharSet]);
        }else {

        }
        }   */


      }
      else
      {
        //Log.warn("The character '" + code.charAt(i) + "' is illegal in code 128.");
        throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code 128.");
      }
    }

    this.codeTable.add(TABLE[count % 103]);
    this.codeTable.add(STOP);
    super.setCode(code);
  }

  public void draw (final Graphics2D graphics2D, final Rectangle2D rectangle2D)
  {
    init(graphics2D, rectangle2D);

    //(nbchar-1)*11
    //13 for the stop
    final int nbChars = codeTable.size();
    //System.out.println(nbChars + "++++\n");
    final float barcodeWidth = this.getMinWidth() * ((nbChars - 1) * 11 + 13);

    final Rectangle2D barcodeArea = this.computeBarcodeArea(rectangle2D, barcodeWidth);
    final Rectangle2D codeArea = this.computeCodeArea(rectangle2D, (float) barcodeArea.getMaxY());
    final Rectangle2D fullArea = this.createFullArea(rectangle2D, codeArea, barcodeArea);


    this.printFullArea(graphics2D, fullArea);

    if (this.isShowCode())
    {
      this.printCode(graphics2D, fullArea, codeArea);
    }

    //now lets draw the barcode
    graphics2D.setColor(this.getBarcodeColor());

    final Point2D p = this.getBarcodePoint(fullArea, barcodeArea);
    float xPointer = (float) p.getX();
    final float yPointer = (float) p.getY();

    //LETS DRAW NOW I SAID !
    for (int i = 0; i < nbChars; i++)
    {
      final byte[] currentByte = (byte[]) codeTable.get(i);
      for (int j = 0; j < currentByte.length; j++)
      {
        if (j % 2 == 0)
        { //even == bar
          graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth() * currentByte[j], this.getMinHeight()));

          xPointer += this.getMinWidth() * currentByte[j];
        }
        else
        {  //odd == space
          xPointer += this.getMinWidth() * currentByte[j];
        }
      }
    }
  }

  private int getNumberIndex (final String str, final int pointer)
  {
    return Integer.parseInt(str.substring(pointer, pointer + 2));
  }

  private int getCharIndex (final char currentChar, final int currentCodeSet)
  {
    if (currentCodeSet == CODESET_CA_TO_B)
    {
      return currentChar + 32;
    }

    if (currentCodeSet == CODESET_BC_TO_A && currentChar < 33)
    {
      return currentChar + 64;
    }
    else
    {
      return currentChar + 32;
    }
  }

  private int needToShift (final char currentChar, final int currentCodeSet)
  {
    if (currentCodeSet != CODESET_AB_TO_C)
    { //shift is not avalaible to CODESET C
      if (currentChar < 32 && currentCodeSet == CODESET_CA_TO_B)
      { //then shift
        return SHIFT_AB;
      }
      else if (currentChar < 32 && currentCodeSet == CODESET_BC_TO_A)
      {
        return currentCodeSet;
      }
      else if (currentChar >= 32 && currentCodeSet == CODESET_CA_TO_B)
      {
        return currentCodeSet;
      }
      else if (currentChar >= 32 && currentCodeSet == CODESET_BC_TO_A)
      {
        return SHIFT_AB;
      }

    }/*else {
       return CODESET_AB_TO_C;
       } */

    return currentCodeSet; //CODE C
  }

  private int getNextFourCharType (final int startIndex, final String str,
                                   final int currentSet)
  {
    //String exp = str.substring(startIndex, startIndex+4);
    final String exp = str.substring(startIndex);

    if (Pattern.matches("^\\d{4,}", exp))
    {
      return CODESET_AB_TO_C;
    }

    if (Pattern.matches("^\\W{4,}", exp))
    {
      return CODESET_BC_TO_A;
    }

    //return CODESET_CA_TO_B;
    return currentSet;

  }

  private int getFirstFourCharType (final String str)
  {
    //String exp = str.substring(0, 4);

    if (Pattern.matches("^\\d{4,}", str))
    {
      //System.out.println("------STARTC");
      return START_C;
    }

    if (Pattern.matches("^\\W{4,}", str))
    {
      //System.out.println("------STARTA");
      return START_A;
    }


    //System.out.println("------STARTB");

    return START_B;
  }


}

/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * -----------------------
 * PageFormatFactory.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 27-Jul-2002 : Initial version, empty class :(
 * 26-Aug-2002 : Coded all defined Postscript-PaperFormats as defined by Adobe.
 */
package com.jrefinery.report.util;

import javax.swing.JOptionPane;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.awt.print.Paper;

/**
 * Page sizes defined by ADOBE:
 *
 * <p>
 * <a href="http://partners.adobe.com/asn/developer/pdfs/tn/5003.PPD_Spec_v4.3.pdf">Postscript Specifications</a>
 * <p>
 * Usage for creating an printjob on A4 paper with 2.5 cm border:
 * <code>
 * Paper paper = PageFormatFactory.createPaper (PageFormatFactory.A4);
 * paper.setBordersMm (25, 25, 25, 25);
 * PageFormat format = PageFormatFactory.createPageFormat (paper, PageFormat.PORTRAIT);
 * </code>
 */
public class PageFormatFactory
{
  public static final int DOTS_PER_INCH = 72;

  public static final int[] PAPER10X11 = { 720, 792 };
  public static final int[] PAPER10X13 = { 720, 936 };
  public static final int[] PAPER10X14 = { 720, 1008 };
  public static final int[] PAPER12X11 = { 864, 792 };
  public static final int[] PAPER15X11 = { 1080, 792 };
  public static final int[] PAPER7X9 =   { 504, 648 };
  public static final int[] PAPER8X10 = { 576, 720 };
  public static final int[] PAPER9X11 = { 648, 792 };
  public static final int[] PAPER9X12 = { 648, 864 };

  public static final int[] A0 = { 2384, 3370 };
  public static final int[] A1 = { 1684, 2384 };
  public static final int[] A2 = { 1191, 1684 };

  public static final int[] A3 = { 842, 1191 };
  public static final int[] A3_TRANSVERSE = { 842, 1191 };
  public static final int[] A3_EXTRA = { 913, 1262 };
  public static final int[] A3_EXTRATRANSVERSE = { 913, 1262 };
  public static final int[] A3_ROTATED = { 1191, 842 };

  public static final int[] A4 = { 595, 842 };
  public static final int[] A4_TRANSVERSE = { 595, 842 };
  public static final int[] A4_EXTRA = { 667, 914 };
  public static final int[] A4_PLUS = { 595, 936 };
  public static final int[] A4_ROTATED = { 842, 595 };
  public static final int[] A4_SMALL = { 595, 842 };

  public static final int[] A5 = { 420, 595 };
  public static final int[] A5_TRANSVERSE = { 420, 595 };
  public static final int[] A5_EXTRA = { 492, 668 };
  public static final int[] A5_ROTATED = { 595, 420 };

  public static final int[] A6 = { 297, 420 };
  public static final int[] A6_ROTATED = { 420, 297 };

  public static final int[] A7 = { 210, 297 };
  public static final int[] A8 = { 148, 210 };
  public static final int[] A9 = { 105, 148 };
  public static final int[] A10 = { 73, 105 };

  public static final int[] ANSIC = { 1224, 1584 };
  public static final int[] ANSID = { 1584, 2448 };
  public static final int[] ANSIE = { 2448, 3168 };

  public static final int[] ARCHA = { 648, 864 };
  public static final int[] ARCHB = { 864, 1296 };
  public static final int[] ARCHC = { 1296, 1728 };
  public static final int[] ARCHD = { 1728, 2592 };
  public static final int[] ARCHE = { 2592, 3456 };

  public static final int[] B0 = { 2920, 4127 };
  public static final int[] B1 = { 2064, 2920 };
  public static final int[] B2 = { 1460, 2064 };
  public static final int[] B3 = { 1032, 1460 };
  public static final int[] B4 = { 729, 1032 };
  public static final int[] B4_ROTATED = { 1032, 729 };
  public static final int[] B5 = { 516, 729 };
  public static final int[] B5_TRANSVERSE = { 516, 729 };
  public static final int[] B5_ROTATED = { 729, 516 };
  public static final int[] B6 = { 363, 516 };
  public static final int[] B6_ROTATED = { 516, 363 };
  public static final int[] B7 = { 258, 363 };
  public static final int[] B8 = { 181, 258 };
  public static final int[] B9 = { 127, 181 };
  public static final int[] B10 = { 91, 127 };

  public static final int[] C4 = { 649, 918 };
  public static final int[] C5 = { 459, 649 };
  public static final int[] C6 = { 323, 459 };

  public static final int[] COMM10 = { 297, 684 };
  public static final int[] DL = { 312, 624 };
  public static final int[] DOUBLEPOSTCARD = { 567, 419 };  // should be 419.5, but I ignore that ..
  public static final int[] DOUBLEPOSTCARD_ROTATED = { 419, 567 };

  public static final int[] ENV9 = { 279, 639 };
  public static final int[] ENV10 = { 297, 684 };
  public static final int[] ENV11 = { 324, 747 };
  public static final int[] ENV12 = { 342, 792 };
  public static final int[] ENV14 = { 360, 828 };

  public static final int[] ENVC0 = { 2599, 3676 };
  public static final int[] ENVC1 = { 1837, 2599 };
  public static final int[] ENVC2 = { 1298, 1837 };
  public static final int[] ENVC3 = { 918, 1296 };
  public static final int[] ENVC4 = { 649, 918 };
  public static final int[] ENVC5 = { 459, 649 };
  public static final int[] ENVC6 = { 323, 459 };
  public static final int[] ENVC65 = { 324, 648 };
  public static final int[] ENVC7 = { 230, 323 };

  public static final int[] ENVCHOU3 = { 340, 666 };
  public static final int[] ENVCHOU3_ROTATED = { 666, 340 };
  public static final int[] ENVCHOU4 = { 255, 581 };
  public static final int[] ENVCHOU4_ROTATED = { 581, 255 };

  public static final int[] ENVDL = { 312, 624 };
  public static final int[] ENVINVITE = { 624, 624 };
  public static final int[] ENVISOB4 = { 708, 1001 };
  public static final int[] ENVISOB5 = { 499, 709 };
  public static final int[] ENVISOB6 = { 499, 354 };
  public static final int[] ENVITALIAN = { 312, 652 };
  public static final int[] ENVKAKU2 = { 680, 941 };
  public static final int[] ENVKAKU2_ROTATED = { 941, 680 };
  public static final int[] ENVKAKU3 = { 612, 785 };
  public static final int[] ENVKAKU3_ROTATED = { 785, 612 };

  public static final int[] ENVMONARCH = { 279, 540 };
  public static final int[] ENVPERSONAL = { 261, 468 };
  public static final int[] ENVPRC1 = { 289, 468 };
  public static final int[] ENVPRC1_ROTATED = { 468, 289 };
  public static final int[] ENVPRC2 = { 289, 499 };
  public static final int[] ENVPRC2_ROTATED = { 499, 289 };
  public static final int[] ENVPRC3 = { 354, 499 };
  public static final int[] ENVPRC3_ROTATED = { 499, 354 };
  public static final int[] ENVPRC4 = { 312, 590 };
  public static final int[] ENVPRC4_ROTATED = { 590, 312 };
  public static final int[] ENVPRC5 = { 312, 624 };
  public static final int[] ENVPRC5_ROTATED = { 624, 312 };
  public static final int[] ENVPRC6 = { 340, 652 };
  public static final int[] ENVPRC6_ROTATED = { 652, 340 };
  public static final int[] ENVPRC7 = { 454, 652 };
  public static final int[] ENVPRC7_ROTATED = { 652, 454 };
  public static final int[] ENVPRC8 = { 340, 876 };
  public static final int[] ENVPRC8_ROTATED = { 876, 340 };
  public static final int[] ENVPRC9 = { 649, 918 };
  public static final int[] ENVPRC9_ROTATED = { 918, 649 };
  public static final int[] ENVPRC10 = { 918, 1298 };
  public static final int[] ENVPRC10_ROTATED = { 1298, 918 };

  public static final int[] ENVYOU4 = { 298, 666 };
  public static final int[] ENVYOU4_ROTATED = { 666, 298 };

  public static final int[] EXECUTIVE = { 522, 756 };
  public static final int[] FANFOLDUS = { 1071, 792 };
  public static final int[] FANFOLDGERMAN = { 612, 864 };
  public static final int[] FANFOLDGERMANLEGAL = { 612, 936 };
  public static final int[] FOLIO = { 595, 935 };

  public static final int[] ISOB0 = { 2835, 4008 };
  public static final int[] ISOB1 = { 2004, 2835 };
  public static final int[] ISOB2 = { 1417, 2004 };
  public static final int[] ISOB3 = { 1001, 1417 };
  public static final int[] ISOB4 = { 709, 1001 };
  public static final int[] ISOB5 = { 499, 709 };
  public static final int[] ISOB5_EXTRA = { 570, 782 };
  public static final int[] ISOB6 = { 354, 499 };
  public static final int[] ISOB7 = { 249, 354 };
  public static final int[] ISOB8 = { 176, 249 };
  public static final int[] ISOB9 = { 125, 176 };
  public static final int[] ISOB10 = { 88, 125 };

  public static final int[] LEDGER = { 1224, 792 };
  public static final int[] LEGAL = { 612, 1008 };
  public static final int[] LEGAL_EXTRA = { 684, 1080 };
  public static final int[] LETTER = { 612, 792 };
  public static final int[] LETTER_TRANSVERSE = { 612, 792 };
  public static final int[] LETTER_EXTRA = { 684, 864 };
  public static final int[] LETTER_EXTRATRANSVERSE = { 684, 864 };
  public static final int[] LETTER_PLUS = { 612, 914 };
  public static final int[] LETTER_ROTATED = { 792, 612 };
  public static final int[] LETTER_SMALL = { 612, 792 };

  public static final int[] MONARCH = ENVMONARCH;

  public static final int[] NOTE = { 612, 792 };
  public static final int[] POSTCARD = { 284, 419 };
  public static final int[] POSTCARD_ROTATED = { 419, 284 };
  public static final int[] PRC16K = { 414, 610 };
  public static final int[] PRC16K_ROTATED = { 610, 414 };
  public static final int[] PRC32K = { 275, 428 };
  public static final int[] PRC32K_ROTATED = { 428, 275 };
  public static final int[] PRC32K_BIG = { 275, 428 };
  public static final int[] PRC32K_BIGROTATED = { 428, 275 };

  public static final int[] QUARTO = { 610, 780 };
  public static final int[] STATEMENT = { 396, 612 };
  public static final int[] SUPERA = { 643, 1009 };
  public static final int[] SUPERB = { 864, 1380 };
  public static final int[] TABLOID = { 792, 1224 };
  public static final int[] TABLOIDEXTRA = { 864, 1296 };

  private static PageFormatFactory singleton;

  public static PageFormatFactory getInstance ()
  {
    if (singleton == null)
    {
      singleton = new PageFormatFactory();
    }
    return singleton;
  }

  public static Paper createPaper (int[] papersize)
  {
    if (papersize.length != 2) throw new IllegalArgumentException("Paper must have a width and a height");

    return createPaper(papersize[0], papersize[1]);
  }

  public static Paper createPaper (int width, int heigth)
  {
    Paper p = new Paper();
    p.setSize(width, heigth);
    setBorders(p, 0,0,0,0);
    return p;
  }

  public static void setBorders (Paper paper, double top, double left, double bottom, double right)
  {
    paper.setImageableArea(top, left, paper.getWidth() - right, paper.getHeight() - bottom);
  }

  public static void setBordersInch (Paper paper, double top, double left, double bottom, double right)
  {
    setBorders(paper, convertInchToPoints(top), convertInchToPoints(left), convertInchToPoints(bottom), convertInchToPoints(right));
  }

  public static void setBordersMm (Paper paper, double top, double left, double bottom, double right)
  {
    setBorders(paper, convertMmToPoints(top), convertMmToPoints(left), convertMmToPoints(bottom), convertMmToPoints(right));
  }

  public static double convertInchToPoints (double points)
  {
    return points * 72;
  }

  public static double convertMmToPoints (double points)
  {
    return points * (72d/254d);
  }

  public static PageFormat createPageFormat (Paper paper, int orientation)
  {
    PageFormat pf = new PageFormat();
    pf.setPaper(paper);
    pf.setOrientation(orientation);
    return pf;
  }


}
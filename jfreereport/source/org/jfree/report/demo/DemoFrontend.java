/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * DemoFrontend.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DemoFrontend.java,v 1.11 2005/10/06 00:50:25 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05.04.2004 : Initial version
 *  
 */

package org.jfree.report.demo;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.bookstore.BookstoreDemo;
import org.jfree.report.demo.cards.CardDemo;
import org.jfree.report.demo.conditionalgroup.ConditionalGroupDemo;
import org.jfree.report.demo.fonts.FontDemo;
import org.jfree.report.demo.form.SimplePatientFormDemo;
import org.jfree.report.demo.functions.FunctionsDemo;
import org.jfree.report.demo.functions.PercentageDemo;
import org.jfree.report.demo.groups.GroupsDemo;
import org.jfree.report.demo.helper.CompoundDemoFrame;
import org.jfree.report.demo.helper.DefaultDemoSelector;
import org.jfree.report.demo.helper.DemoSelector;
import org.jfree.report.demo.huge.VeryLargeReportDemo;
import org.jfree.report.demo.internationalisation.I18nDemo;
import org.jfree.report.demo.invoice.InvoiceDemo;
import org.jfree.report.demo.largetext.LGPLTextDemo;
import org.jfree.report.demo.layouts.LayoutDemo;
import org.jfree.report.demo.multireport.MultiReportDemo;
import org.jfree.report.demo.onetomany.PeopleReportDemo;
import org.jfree.report.demo.opensource.OpenSourceDemo;
import org.jfree.report.demo.sportscouncil.SportsCouncilDemo;
import org.jfree.report.demo.surveyscale.SurveyScaleDemo;
import org.jfree.report.demo.swingicons.SwingIconsDemo;
import org.jfree.report.demo.world.WorldDemo;
import org.jfree.report.demo.stylesheets.StyleSheetDemoHandler;
import org.jfree.ui.RefineryUtilities;

public class DemoFrontend extends CompoundDemoFrame
{
  public DemoFrontend(final DemoSelector demoSelector)
  {
    super(demoSelector);
    setIgnoreEmbeddedConfig(true);
    final ModifiableConfiguration editableConfig =
            JFreeReportBoot.getInstance().getEditableConfig();
    editableConfig.setConfigProperty(EMBEDDED_KEY, "true");
    init();
  }
  public static DemoSelector createDemoInfo ()
  {
    final DefaultDemoSelector rootSelector = new DefaultDemoSelector
            ("All JFreeReport Demos");

    rootSelector.addChild(CardDemo.createDemoInfo());
    rootSelector.addChild(InvoiceDemo.createDemoInfo());
    rootSelector.addChild(PeopleReportDemo.createDemoInfo());
    rootSelector.addChild(SurveyScaleDemo.createDemoInfo());
    rootSelector.addChild(WorldDemo.createDemoInfo());
    rootSelector.addChild(OpenSourceDemo.createDemoInfo());
    rootSelector.addChild(FunctionsDemo.createDemoInfo());
    rootSelector.addChild(LayoutDemo.createDemoInfo());
    // report footer
    rootSelector.addDemo(new ConditionalGroupDemo());
    //rootSelector.addDemo(new CSVReaderDemo());
    rootSelector.addDemo(new SimplePatientFormDemo());
    rootSelector.addDemo(new MultiReportDemo());
    rootSelector.addDemo(new SportsCouncilDemo());
    rootSelector.addDemo(new SwingIconsDemo());
    rootSelector.addDemo(new HelloWorld());
    rootSelector.addDemo(new LGPLTextDemo());
    rootSelector.addDemo(new I18nDemo());
    rootSelector.addDemo(new PercentageDemo());
    rootSelector.addDemo(new GroupsDemo());
    rootSelector.addDemo(new FontDemo());
    rootSelector.addDemo(new VeryLargeReportDemo());
    rootSelector.addDemo(new BookstoreDemo());
    rootSelector.addDemo(new StyleSheetDemoHandler());

    return rootSelector;
  }

  public static void main (final String[] args)
  {

    final DemoFrontend frontend = new DemoFrontend(createDemoInfo());
    frontend.pack();
    RefineryUtilities.centerFrameOnScreen(frontend);
    frontend.setVisible(true);
  }
}

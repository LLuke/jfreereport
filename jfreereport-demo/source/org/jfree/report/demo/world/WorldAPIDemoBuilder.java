/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * WorldAPIDemoBuilder.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.demo.world;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import org.jfree.report.JFreeReport;
import org.jfree.report.expressions.sys.GroupByExpression;
import org.jfree.report.expressions.sys.GetValueExpression;
import org.jfree.report.expressions.FormulaFunction;
import org.jfree.report.structure.Group;
import org.jfree.report.structure.Section;
import org.jfree.report.structure.Node;
import org.jfree.report.structure.DetailSection;
import org.jfree.report.structure.StaticText;
import org.jfree.report.structure.ContentElement;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 02.12.2006, 20:41:17
 *
 * @author Thomas Morgner
 */
public class WorldAPIDemoBuilder
{
  public WorldAPIDemoBuilder()
  {
  }

  public JFreeReport createReport()
  {
    final JFreeReport report = new JFreeReport();
    report.setQuery("default");
    report.addNode(createReportHeader());
    report.addNode(createGroup());
    report.addNode(createReportFooter());

    return report;
  }

  private Group createGroup()
  {
    final GroupByExpression groupByExpression = new GroupByExpression();
    groupByExpression.setField(0, "Continent");

    final FormulaFunction function = new FormulaFunction();
    function.setName("PopulationSum");
    function.setFormula("jfreereport:[Population] + [PopulationSum]");
    function.setInitial("jfreereport:[Population]");
    function.setPrecompute(true);

    final Group group = new Group();
    group.setGroupingExpression(groupByExpression);
    group.addExpression(function);
    group.addNode(createGroupHeader());
    group.addNode(createDetailSection());
    group.addNode(createGroupFooter());
    return group;
  }

  private Node createDetailSection()
  {
    final Section tableDataCountry = new Section();
    tableDataCountry.setType("td");
    tableDataCountry.setNamespace(Namespaces.XHTML_NAMESPACE);
    tableDataCountry.addNode(createContentElement("Country"));

    final Section tableDataIso = new Section();
    tableDataIso.setType("td");
    tableDataIso.setNamespace(Namespaces.XHTML_NAMESPACE);
    tableDataIso.addNode(createContentElement("ISO Code"));

    final Section tableDataPopulation = new Section();
    tableDataPopulation.setType("td");
    tableDataPopulation.setNamespace(Namespaces.XHTML_NAMESPACE);
    tableDataPopulation.addNode(createContentElement("Population"));

    final Section tableRow = new Section();
    tableRow.setType("tr");
    tableRow.setNamespace(Namespaces.XHTML_NAMESPACE);
    tableRow.addNode(tableDataCountry);
    tableRow.addNode(tableDataIso);
    tableRow.addNode(tableDataPopulation);

    final DetailSection section = new DetailSection();
    section.setVirtual(true);
    section.addNode(tableRow);

    final Section footerLabel = new Section();
    footerLabel.setType("td");
    footerLabel.setNamespace(Namespaces.XHTML_NAMESPACE);
    footerLabel.setAttribute(Namespaces.XHTML_NAMESPACE, "colspan", "2");
    footerLabel.addNode(new StaticText("Total Population:"));

    final Section footerSum = new Section();
    footerSum.setType("td");
    footerSum.setNamespace(Namespaces.XHTML_NAMESPACE);
    footerSum.addNode(createContentElement("PopulationSum"));

    final Section footerRow = new Section();
    footerRow.setType("tr");
    footerRow.setNamespace(Namespaces.XHTML_NAMESPACE);
    footerRow.addNode(footerLabel);
    footerRow.addNode(footerSum);

    final Section table = new Section();
    table.setType("table");
    table.setNamespace(Namespaces.XHTML_NAMESPACE);
    table.addNode(createColumnDefinition("55%"));
    table.addNode(createColumnDefinition("10%"));
    table.addNode(createColumnDefinition("35%"));
    table.addNode(section);
    table.addNode(footerRow);
    return table;
  }

  private Node createColumnDefinition (String width)
  {
    final Section column = new Section();
    column.setType("col");
    column.setNamespace(Namespaces.XHTML_NAMESPACE);
    column.setAttribute(Namespaces.XHTML_NAMESPACE, "style", "width: " + width);
    return column;
  }

  private Node createGroupFooter()
  {
    final Section section = new Section();

    final Section h1 = new Section();
    h1.setType("hr");
    h1.setNamespace(Namespaces.XHTML_NAMESPACE);
    section.addNode(h1);

    return section;
  }

  private Node createGroupHeader()
  {
    final Section section = new Section();

    final ContentElement continentElement = new ContentElement();
    continentElement.setValueExpression(new GetValueExpression("Continent"));

    final Section h1 = new Section();
    h1.setType("h2");
    h1.setNamespace(Namespaces.XHTML_NAMESPACE);
    h1.setAttribute(Namespaces.XHTML_NAMESPACE, "style", "background-color: #C8FAF4; font-size: 14pt");
    h1.addNode(new StaticText("Continent: "));
    h1.addNode(createContentElement("Continent"));
    section.addNode(h1);

    final Section p = new Section();
    p.setType("p");
    p.setNamespace(Namespaces.XHTML_NAMESPACE);
    p.addNode(new StaticText("The precomputed result of the sum-function is "));
    p.addNode(createContentElement("PopulationSum"));
    section.addNode(p);

    return section;
  }

  private ContentElement createContentElement(String field)
  {
    final ContentElement populationElement = new ContentElement();
    populationElement.setValueExpression(new GetValueExpression(field));
    return populationElement;
  }

  public Section createReportHeader()
  {
    final Section section = new Section();

    final Section h1 = new Section();
    h1.setType("h1");
    h1.setNamespace(Namespaces.XHTML_NAMESPACE);
    h1.setAttribute(Namespaces.XHTML_NAMESPACE, "style", "background-color: #afafaf; text-align: center");
    h1.addNode(new StaticText("List of Countries by Continent"));
    section.addNode(h1);

    final Section p = new Section();
    p.setType("p");
    p.setNamespace(Namespaces.XHTML_NAMESPACE);
    p.addNode(new StaticText("This report lists a selected set of countries and produces a running\n" +
        "    total from the given population numbers. The original JFreeReport\n" +
        "    demo report supported percentages as well. Percentages need a\n" +
        "    precomputation step to compute the grand total, before the current\n" +
        "    value can be set in relation to the total population."));
    section.addNode(p);

//
//    final Section img = new Section();
//    img.setType("img");
//    img.setNamespace(Namespaces.XHTML_NAMESPACE);
//    img.setAttribute(Namespaces.XHTML_NAMESPACE, "src", "gorilla.jpg");
//    img.setAttribute(Namespaces.XHTML_NAMESPACE, "alt", "A gorilla");

    final URL imageURL = ObjectUtilities.getResourceRelative
            ("gorilla.jpg", WorldAPIDemoBuilder.class);
    final Image image = Toolkit.getDefaultToolkit().createImage(imageURL);

    final ContentElement imgManual = new ContentElement();
    imgManual.setType("img");
    imgManual.setNamespace(Namespaces.XHTML_NAMESPACE);
    imgManual.setAttribute(Namespaces.XHTML_NAMESPACE, "content", image);
    imgManual.setAttribute(Namespaces.XHTML_NAMESPACE, "style", "content: attr(content)");
    section.addNode(imgManual);

    return section;
  }

  public Section createReportFooter()
  {
    final Section section = new Section();

    final Section h1 = new Section();
    h1.setType("h1");
    h1.addNode(new StaticText("End Of Report"));
    section.addNode(h1);

    return section;
  }
}

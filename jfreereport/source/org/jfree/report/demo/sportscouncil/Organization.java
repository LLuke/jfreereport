package org.jfree.report.demo.sportscouncil;

import java.util.ArrayList;

public class Organization
{
  private CouncilRecord council;
  private ArrayList leaders;
  private ArrayList subOrganizations;

  public Organization (final CouncilRecord council)
  {
    this.council = council;
    this.leaders = new ArrayList();
    this.subOrganizations = new ArrayList();
  }

  public CouncilRecord getCouncil ()
  {
    return council;
  }

  public int getLeaderCount ()
  {
    return leaders.size();
  }

  public int getSubOrganzationsCount ()
  {
    return subOrganizations.size();
  }

  public LeaderRecord getLeader (final int i)
  {
    return (LeaderRecord) leaders.get(i);
  }

  public SubOrganizationRecord getSubOrganization (final int i)
  {
    return (SubOrganizationRecord) subOrganizations.get(i);
  }

  public void addLeader (final LeaderRecord record)
  {
    leaders.add(record);
  }

  public void addSubOrganization (final SubOrganizationRecord record)
  {
    subOrganizations.add(record);
  }
}

package org.jfree.report.demo.sportscouncil;

public class Record
{
  private String recordType;
  private String orgID;

  public Record (final String recordType, final String orgID)
  {
    this.recordType = recordType;
    this.orgID = orgID;
  }

  public String getOrgID ()
  {
    return orgID;
  }

  public String getRecordType ()
  {
    return recordType;
  }
}

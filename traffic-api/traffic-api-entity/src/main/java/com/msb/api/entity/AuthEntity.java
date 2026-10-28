package com.msb.api.entity;

import java.io.Serializable;
import java.util.HashMap;

public class AuthEntity implements Serializable {

  private long aid;
  private String aname;
  private String apath;
  private long atype;
  private long pid;

  public long getAid() {
    return aid;
  }

  public void setAid(long aid) {
    this.aid = aid;
  }


  public String getAname() {
    return aname;
  }

  public void setAname(String aname) {
    this.aname = aname;
  }


  public String getApath() {
    return apath;
  }

  public void setApath(String apath) {
    this.apath = apath;
  }


  public long getAtype() {
    return atype;
  }

  public void setAtype(long atype) {
    this.atype = atype;
  }


  public long getPid() {
    return pid;
  }

  public void setPid(long pid) {
    this.pid = pid;
  }

}

package com.msb.system.entity;

import java.util.List;

public class RoleEntity {
    private long rid;
    private String rname;
    private int rtype;
    private String redesc;

    //用户信息
    private List<UserEntity> users;

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public int getRtype() {
        return rtype;
    }

    public void setRtype(int rtype) {
        this.rtype = rtype;
    }

    public String getRedesc() {
        return redesc;
    }

    public void setRedesc(String redesc) {
        this.redesc = redesc;
    }
}

/**
 * Copyright (C) 2013 Loophole, LLC
 * <p>
 * Licensed under The Prosperity Public License 3.0.0
 */
package io.bastillion.manage.db;

import io.bastillion.manage.model.HostSystem;
import io.bastillion.manage.util.DBUtils;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO to manage profile information
 */
public class ProfileSystemsDB {

    private ProfileSystemsDB() {
    }

    /**
     * sets host systems for profile
     *
     * @param profileId    profile id
     * @param systemIdList list of host system ids
     */
    public static void setSystemsForProfile(Long profileId, List<Long> systemIdList) throws SQLException, GeneralSecurityException {


        Connection con = DBUtils.getConn();
        PreparedStatement stmt = con.prepareStatement("delete from system_map where profile_id=?");
        stmt.setLong(1, profileId);
        stmt.execute();
        DBUtils.closeStmt(stmt);

        for (Long systemId : systemIdList) {
            stmt = con.prepareStatement("insert into system_map (profile_id, system_id) values (?,?)");
            stmt.setLong(1, profileId);
            stmt.setLong(2, systemId);
            stmt.execute();
            DBUtils.closeStmt(stmt);
        }

        DBUtils.closeConn(con);
    }

    /**
     * returns a list of systems for a given profile
     *
     * @param con       DB connection
     * @param profileId profile id
     * @return list of host systems
     */
    public static List<HostSystem> getSystemsByProfile(Connection con, Long profileId) throws SQLException {

        List<HostSystem> hostSystemList = new ArrayList<>();

        PreparedStatement stmt = con.prepareStatement("select * from  system s, system_map m where s.id=m.system_id and m.profile_id=? order by display_nm asc");
        stmt.setLong(1, profileId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            HostSystem hostSystem = new HostSystem();
            hostSystem.setId(rs.getLong("id"));
            hostSystem.setDisplayNm(rs.getString("display_nm"));
            hostSystem.setUser(rs.getString("username"));
            hostSystem.setHost(rs.getString("host"));
            hostSystem.setPort(rs.getInt("port"));
            hostSystem.setAuthorizedKeys(rs.getString("authorized_keys"));
            hostSystemList.add(hostSystem);
        }
        DBUtils.closeRs(rs);
        DBUtils.closeStmt(stmt);


        return hostSystemList;
    }

    /**
     * returns a list of systems for a given profile
     *
     * @param profileId profile id
     * @return list of host systems
     */
    public static List<HostSystem> getSystemsByProfile(Long profileId) throws SQLException, GeneralSecurityException {

        Connection con = DBUtils.getConn();
        List<HostSystem> hostSystemList = getSystemsByProfile(con, profileId);
        DBUtils.closeConn(con);

        return hostSystemList;
    }

    /**
     * returns a list of system ids for a given profile
     *
     * @param con       DB con
     * @param profileId profile id
     * @return list of host systems
     */
    public static List<Long> getSystemIdsByProfile(Connection con, Long profileId) throws SQLException {

        List<Long> systemIdList = new ArrayList<>();

        PreparedStatement stmt = con.prepareStatement("select * from  system s, system_map m where s.id=m.system_id and m.profile_id=? order by display_nm asc");
        stmt.setLong(1, profileId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            systemIdList.add(rs.getLong("id"));
        }
        DBUtils.closeRs(rs);
        DBUtils.closeStmt(stmt);

        return systemIdList;
    }

    /**
     * returns a list of system ids for a given profile
     *
     * @param profileId profile id
     * @return list of host systems
     */
    public static List<Long> getSystemIdsByProfile(Long profileId) throws SQLException, GeneralSecurityException {

        Connection con = DBUtils.getConn();
        List<Long> systemIdList = getSystemIdsByProfile(con, profileId);
        DBUtils.closeConn(con);

        return systemIdList;
    }

    /**
     * returns a list of system ids for a given profile
     *
     * @param con       DB con
     * @param profileId profile id
     * @param userId    user id
     * @return list of host systems
     */
    public static List<Long> getSystemIdsByProfile(Connection con, Long profileId, Long userId) throws SQLException {

        List<Long> systemIdList = new ArrayList<>();

        PreparedStatement stmt = con.prepareStatement("select sm.system_id from  system_map sm, user_map um where um.profile_id=sm.profile_id and sm.profile_id=? and um.user_id=?");
        stmt.setLong(1, profileId);
        stmt.setLong(2, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            systemIdList.add(rs.getLong("system_id"));
        }
        DBUtils.closeRs(rs);
        DBUtils.closeStmt(stmt);

        return systemIdList;
    }

    /**
     * returns a list of system ids for a given profile
     *
     * @param profileId profile id
     * @param userId    user id
     * @return list of host systems
     */
    public static List<Long> getSystemIdsByProfile(Long profileId, Long userId) throws SQLException, GeneralSecurityException {

        Connection con = DBUtils.getConn();
        List<Long> systemIdList = getSystemIdsByProfile(con, profileId, userId);
        DBUtils.closeConn(con);

        return systemIdList;
    }
}

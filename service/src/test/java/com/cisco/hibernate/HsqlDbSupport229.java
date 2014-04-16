package com.cisco.hibernate;

import org.unitils.core.UnitilsException;
import org.unitils.core.dbsupport.HsqldbDbSupport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;

import static org.unitils.thirdparty.org.apache.commons.dbutils.DbUtils.closeQuietly;

/**
 * User: Rost
 * Date: 17.04.2014
 * Time: 0:57
 */
public class HsqlDbSupport229 extends HsqldbDbSupport {

    @Override
    public void disableReferentialConstraints() {
        Connection connection = null;
        Statement queryStatement = null;
        Statement alterStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getSQLHandler().getDataSource().getConnection();
            queryStatement = connection.createStatement();
            alterStatement = connection.createStatement();

            resultSet = queryStatement.executeQuery("select TABLE_NAME, CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where CONSTRAINT_TYPE = 'FOREIGN KEY' AND CONSTRAINT_SCHEMA = '" + getSchemaName() + "'");
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String constraintName = resultSet.getString("CONSTRAINT_NAME");
                alterStatement.executeUpdate("alter table " + qualified(tableName) + " drop constraint " + quoted(constraintName));
            }
        } catch (Exception e) {
            throw new UnitilsException("Error while disabling not referential constraints on schema " + getSchemaName(), e);
        } finally {
            closeQuietly(queryStatement);
            closeQuietly(connection, alterStatement, resultSet);
        }
    }

    @Override
    public void disableCheckAndUniqueConstraints() {
        Connection connection = null;
        Statement queryStatement = null;
        Statement alterStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getSQLHandler().getDataSource().getConnection();
            queryStatement = connection.createStatement();
            alterStatement = connection.createStatement();

            resultSet = queryStatement.executeQuery("select TABLE_NAME, CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where CONSTRAINT_TYPE IN ('CHECK', 'UNIQUE') AND CONSTRAINT_SCHEMA = '" + getSchemaName() + "'");
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String constraintName = resultSet.getString("CONSTRAINT_NAME");
                alterStatement.executeUpdate("alter table " + qualified(tableName) + " drop constraint " + quoted(constraintName));
            }
        } catch (Exception e) {
            throw new UnitilsException("Error while disabling check and unique constraints on schema " + getSchemaName(), e);
        } finally {
            closeQuietly(queryStatement);
            closeQuietly(connection, alterStatement, resultSet);
        }
    }

    @Override
    public Set<String> getTriggerNames() {
        return getSQLHandler().getItemsAsStringSet("select TRIGGER_NAME from INFORMATION_SCHEMA.TRIGGERS where TRIGGER_SCHEMA = '" + getSchemaName() + "'");
    }
}

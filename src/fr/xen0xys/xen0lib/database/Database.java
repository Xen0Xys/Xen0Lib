package fr.xen0xys.xen0lib.database;

import fr.xen0xys.xen0lib.utils.Status;

import java.sql.*;
import java.util.HashMap;

/**
 * Database object, contain connection to database
 */
public class Database {
    private Connection connection;
    private final String ip;
    private final int port;
    private final String user;
    private final String password;
    private final String database;
    private final HashMap<String, Table> tables;

    public Database(String ip, int port, String user, String password, String database){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        this.tables = new HashMap<>();
    }

    /**
     * Need to execute connect method to connect Database Object to Remote Database.
     * @return Xen0Lib Status
     */
    public Status connect(){
        try{
            this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s",
                    this.ip,
                    this.port,
                    this.database),
                    this.user,
                    this.password);
            if(this.connection != null){
                return Status.Success;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return Status.SQLError;
    }

    /**
     * Try to reconnect to database
     * @return Xen0Lib Status
     */
    public Status reconnect(){
        if(this.connection != null){
            try{
                this.connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return this.connect();
    }

    // TABLE MANAGEMENT
    /**
     * Check if a table is locally open
     * @param tableName Table name
     * @return True if table is open
     */
    public boolean isTableOpen(String tableName){
        return this.tables.get(tableName) != null;
    }

    /**
     * Open table
     * @param tableName Table name
     * @return Xen0Lib Status
     */
    public Status openTable(String tableName){
        if(this.tables.get(tableName) != null){
            this.tables.put(tableName, new Table(tableName, this));
            return Status.Success;
        }
        return Status.SQLAlreadyOpenError;
    }

    /**
     * Open table and create it if not exist
     * @param tableName Table name
     * @return Xen0Lib Status
     */
    public Status openTableAndCreateINE(String tableName, String initTableString){
        if(this.tables.get(tableName) != null){
            Table table = new Table(tableName, this);
            return this.openTableAndCreateINE(table, initTableString);
        }
        return Status.SQLAlreadyOpenError;
    }

    public Status openTableAndCreateINE(Table table, String initTableString){
        if(table.getTableName() != null){
            Status status = table.create(initTableString);
            if(status == Status.Success){
                this.tables.put(table.getTableName(), table);
                return Status.Success;
            }else if(status == Status.SQLTableAlreadyExist){
                return status;
            }else{
                return Status.SQLError;
            }
        }
        return Status.SQLAlreadyOpenError;
    }

    /**
     * Open Table from custom Table Object and initialize it
     * @param table Custom Table Object
     * @param initTableString Table initialization string
     * @return Xen0Lib Status
     */
    public Status openTable(Table table, String initTableString){
        if(this.tables.get(table.getTableName()) != null){
            if(table.create(initTableString) != Status.Success){
                return Status.SQLError;
            }
            this.tables.put(table.getTableName(), table);
            return Status.Success;
        }
        return Status.SQLAlreadyOpenError;
    }

    /**
     * Open Table from custom Table Object
     * @param table Custom Table Object
     * @return Xen0Lib Status
     */
    public Status openTable(Table table){
        if(this.tables.get(table.getTableName()) != null){
            this.tables.put(table.getTableName(), table);
            return Status.Success;
        }
        return Status.SQLAlreadyOpenError;
    }

    /**
     * Return table
     * @param tableName Table Name
     * @return Xen0Lib Table
     */
    public Table getTable(String tableName){
        return this.tables.get(tableName);
    }

    // QUERY MANAGEMENT
    private Statement getStatement(){
        try {
            return this.connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            this.reconnect();
        }
        return null;
    }

    /**
     * Execute and update given SQL query
     * @param query SQL query
     * @return Xen0Lib Status
     */
    public Status executeUpdateQuery(String query){
        try {
            Statement statement = this.getStatement();
            if(statement != null) {
                statement.executeUpdate(query);
                return Status.Success;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            this.reconnect();
        }
        return Status.SQLError;
    }

    /**
     * Execute given SQL query without update
     * @param query SQL query
     * @return SQL ResultSet
     */
    public ResultSet executeQuery(String query){
        try{
            Statement statement = this.getStatement();
            if(statement != null){
                return statement.executeQuery(query);
            }
        } catch(SQLException e){
            e.printStackTrace();
            this.reconnect();
        }
        return null;
    }

    // PREPARED STATEMENTS MANAGEMENT

    /**
     * Return Prepared Statement with loaded query
     * @param query SQL query
     * @return Prepared Statement
     */
    public PreparedStatement getPreparedStatement(String query){
        try {
            return this.connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Execute given Prepared Statement without update
     * @param preparedStatement SQL Prepared Statement
     * @return Xen0Lib Status
     */
    public Status executeUpdatePreparedStatement(PreparedStatement preparedStatement){
        try {
            preparedStatement.executeUpdate();
            return Status.Success;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Status.SQLError;
    }

    /**
     * Execute given Prepared Statement without update
     * @param preparedStatement SQL Prepared Statement
     * @return SQL ResultSet
     */
    public ResultSet executePreparedStatement(PreparedStatement preparedStatement){
        try{
            return preparedStatement.executeQuery();
        } catch(SQLException e){
            e.printStackTrace();
            this.reconnect();
        }
        return null;
    }

    public Status isTableExist(String tableName){
        String query = String.format("SELECT * FROM information_schema.TABLES WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s'", this.database, tableName);
        ResultSet rs = this.executeQuery(query);
        try {
            if(rs.next()){
                return Status.SQLTableAlreadyExist;
            }else{
                return Status.SQLTableNotExist;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Status.SQLError;
    }

}

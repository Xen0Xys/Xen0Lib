package fr.xen0xys.xen0lib.database;

import fr.xen0xys.xen0lib.utils.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class Database {


    private Connection connection;
    private final String ip;
    private final int port;
    private final String user;
    private final String password;
    private final String databaseName;
    private final String databasePath;
    private final boolean isMySQL;
    private final HashMap<String, Table> tables;
    private final Logger logger;

    /**
     * Constructor for MySQL database only!
     * @param ip
     * @param port
     * @param user
     * @param password
     * @param database
     */
    public Database(String ip, int port, String user, String password, String database, Logger logger){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
        this.databaseName = database;
        this.tables = new HashMap<>();
        this.databasePath = null;
        this.isMySQL = true;
        this.logger = logger;
    }

    /**
     * Constructor for SQLite database only!
     * @param folderPath Path for folder which database go in
     * @param fileName Database file name
     */
    public Database(String folderPath, String fileName, Logger logger){
        this.ip = "";
        this.port = 0;
        this.user = "";
        this.password = "";
        this.databaseName = "";
        this.tables = new HashMap<>();
        this.databasePath = folderPath + "/" + fileName + ".db";
        this.isMySQL = false;
        this.logger = logger;
    }

    /**
     * Need to execute connect method to connect Database Object to Remote Database.
     * @return Xen0Lib Status: Success, SQLError
     */
    public Status connect(){
        DriverManager.setLoginTimeout(2);
        try{
            if(this.isMySQL){
                this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s?autoReconnect=true",
                        this.ip,
                        this.port,
                        this.databaseName),
                        this.user,
                        this.password);
            }else{
                this.connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", this.databasePath));
            }
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
     * @return Xen0Lib Status: Success, SQLError
     */
    public Status reconnect(){
        if(this.connection != null){
            this.disconnect();
        }
        return this.connect();
    }

    /**
     * Disconnect from database
     * @return Xen0Lib Status: Success, SQLError
     */
    public Status disconnect(){
        try {
            this.connection.close();
            return Status.Success;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Status.SQLError;
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
     * @return Xen0Lib Status: Success, SQLAlreadyOpenError
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
     * @param initTableString Table init string
     * @return Xen0Lib Status: Success, Exist, SQLError, SQLAlreadyOpenError
     */
    public Status openTableAndCreateINE(String tableName, String initTableString){
        initTableString = changeQuerySyntax(initTableString);
        if(this.tables.get(tableName) != null){
            Table table = new Table(tableName, this);
            return this.openTableAndCreateINE(table, initTableString);
        }
        return Status.SQLAlreadyOpenError;
    }

    /**
     * Open table and create it if not exist
     * @param table Xen0Lib Table Object
     * @param initTableString Table init string
     * @return Xen0Lib Status: Success, Exist, SQLError, SQLAlreadyOpenError
     */
    public Status openTableAndCreateINE(Table table, String initTableString){
        initTableString = changeQuerySyntax(initTableString);
        if(table.getTableName() != null){
            Status status = table.create(initTableString);
            if(status == Status.Success){
                this.tables.put(table.getTableName(), table);
                return Status.Success;
            }else if(status == Status.Exist){
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
     * @return Xen0Lib Status: Success, SQLError, SQLAlreadyOpenError
     */
    public Status openTable(Table table, String initTableString){
        initTableString = changeQuerySyntax(initTableString);
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
     * @return Xen0Lib Status, Success, SQLError, SQLAlreadyOpenError
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
     * @return Xen0Lib Status: Success, SQLError
     */
    public Status executeUpdateQuery(String query){
        query = changeQuerySyntax(query);
        try {
            Statement statement = this.getStatement();
            if(statement != null) {
                statement.executeUpdate(query);
                return Status.Success;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.warning("Can't send request, retrying...");
            this.reconnect();
            try {
                Statement statement = this.getStatement();
                if(statement != null) {
                    statement.executeUpdate(query);
                    return Status.Success;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                logger.severe("Request can't be sent, aborting!");
            }
        }
        return Status.SQLError;
    }

    /**
     * Execute given SQL query without update
     * @param query SQL query
     * @return SQL ResultSet
     */
    public ResultSet executeQuery(String query){
        query = changeQuerySyntax(query);
        try{
            Statement statement = this.getStatement();
            if(statement != null){
                return statement.executeQuery(query);
            }
        } catch(SQLException e){
            e.printStackTrace();
            logger.warning("Can't send request, retrying...");
            this.reconnect();
            try{
                Statement statement = this.getStatement();
                if(statement != null){
                    return statement.executeQuery(query);
                }
            } catch(SQLException ex){
                ex.printStackTrace();
                logger.severe("Request can't be sent, aborting!");
            }
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
        query = changeQuerySyntax(query);
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
     * @return Xen0Lib Status: Success, SQLError
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
            logger.warning("Can't send request, retrying...");
            this.reconnect();
            try{
                return preparedStatement.executeQuery();
            } catch(SQLException ex){
                ex.printStackTrace();
                logger.severe("Request can't be sent, aborting!");
            }
        }
        return null;
    }

    /**
     * Check if table exist
     * @param tableName Table name
     * @return Xen0Lib Status: Exist, NotExist, SQLError
     */
    public Status isTableExist(String tableName){
        String query;
        if(this.isMySQL){
            query = String.format("SELECT * FROM information_schema.TABLES WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s'", this.databaseName, tableName);
        }else{
            query = String.format("SELECT name FROM sqlite_master WHERE type='table' AND name='%s';", tableName);
        }

        ResultSet rs = this.executeQuery(query);
        try {
            if(rs.next()){
                return Status.Exist;
            }else{
                return Status.NotExist;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Status.SQLError;
    }

    /**
     * Check if rs is empty or not
     * @param rs SQL ResultSet
     * @return Xen0Lib Status: Exist, NotExist, SQLError
     */
    public Status isDataExist(ResultSet rs){
        try {
            if(rs.next()){
                rs.beforeFirst();
                return Status.Exist;
            }else{
                return Status.NotExist;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Status.SQLError;
    }

    private String changeQuerySyntax(String inputString){
        if(!this.isMySQL){
            List<String> fieldNames = new ArrayList<>();
            String outputString = inputString;
            outputString = outputString.replace(" INT ", " INTEGER ");
            outputString = outputString.replace(" INT,", " INTEGER,");
            outputString = outputString.replace("UNSIGNED", "");
            outputString = outputString.replace("  ", " ");
            for(String temp: inputString.split("\\(")[0].split(",")){
                if(temp.contains("AUTO_INCREMENT")){
                    String fieldName = temp.split(" ")[0];
                    fieldNames.add(fieldName);
                }
            }
            if(fieldNames.size() != 0){
                outputString = outputString.replace("PRIMARY KEY", "");
                outputString = outputString.replace("AUTO_INCREMENT", "");
                outputString = outputString.replace("  ", " ");
                for(String fieldName: fieldNames){
                    outputString += String.format(",PRIMARY KEY(\"%s\" AUTOINCREMENT)", fieldName);
                }
            }
            return outputString;
        }
        return inputString;
    }

}

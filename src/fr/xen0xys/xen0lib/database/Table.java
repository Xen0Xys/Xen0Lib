package fr.xen0xys.xen0lib.database;

import fr.xen0xys.xen0lib.utils.Status;

public class Table {

    private final String tableName;
    private final Database database;

    public Table(String tableName, Database database){
        this.tableName = tableName;
        this.database = database;
    }

    /**
     * Create table
     * @param initTableString
     * @return Xen0Lib Status: Success, SQLError, Exist
     */
    public Status create(String initTableString){
        if(database.isTableExist(this.tableName) == Status.NotExist){
            String query = String.format("CREATE TABLE %s (%s)", this.tableName, initTableString);
            return this.database.executeUpdateQuery(query);
        }else{
            return Status.Exist;
        }
    }

    public Status drop(){
        String query = String.format("DROP %s;", this.tableName);
        return this.database.executeUpdateQuery(query);
    }

    public Status deleteData(String selector){
        String query = String.format("DELETE FROM %s WHERE %s;", this.tableName, selector);
        return this.database.executeUpdateQuery(query);
    }

    public String getTableName() {
        return tableName;
    }

    public Database getDatabase() {
        return database;
    }
}

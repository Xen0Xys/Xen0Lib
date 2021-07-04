package fr.xen0xys.xen0lib.database;

import fr.xen0xys.xen0lib.utils.Status;

public class Table {

    private final String tableName;
    private final Database database;

    public Table(String tableName, Database database){
        this.tableName = tableName;
        this.database = database;
    }

    public Status create(String tableInitString){
        String query = String.format("CREATE TABLE %s (%s)", this.tableName, tableInitString);
        return this.database.executeUpdateQuery(query);
    }

}

package data.metadata;

import java.util.ArrayList;

public class Table {
    private final String name;

    private ArrayList<Column> columns;

    public Table(String name, ArrayList<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<Column> columns) {
        this.columns = columns;
    }
}

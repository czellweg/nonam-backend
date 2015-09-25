package ch.nonam.worldcat.xml;

import java.util.List;

import com.google.common.collect.Lists;

public class ResultSet {

    private List<Row> rows;

    public ResultSet() {
        this.rows = Lists.newArrayList();
    }

    public ResultSet(List<Row> rows) {
        this.rows = rows;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public void addRow(Row row) {
        this.rows.add(row);
    }

}

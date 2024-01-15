package BD;

import Model.Article;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DataBean {
    private static DatabaseConnection beanGenerique;

    public DataBean() {
        beanGenerique = new DatabaseConnection();
    }


    public void close() {
        try {
            beanGenerique.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

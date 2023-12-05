package BD;


import Protocol.FactureBill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBean {
    private static DatabaseConnection DatabaseConnectionBean;

    static {
        DatabaseConnectionBean = new DatabaseConnection();
    }

    public static int SelectLogin(String login, String pass) {
        try {
            int id = 0;
            String requette = "SELECT id FROM clients WHERE login = '" + login + "' AND password = '" + pass + "'";

            ResultSet executed = DatabaseConnectionBean.executeQuery(requette);

            if (executed.next()) {
                id = executed.getInt("id");
            }
            return id;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FactureBill> getALLFactures(int idClient, int check) {
        try {
            List<FactureBill> factures = new ArrayList<>();
            String requete = "SELECT * FROM factures WHERE paye = " + check + " AND idClient = " + idClient;
            System.out.println("montant " + check);
            System.out.println("idClient " + idClient);

            ResultSet executed = DatabaseConnectionBean.executeQuery(requete);

            while (executed.next()) {
                int idFacture = executed.getInt("idFacture");
                String date = executed.getString("dateFacture");
                float montant = executed.getFloat("montant");
                System.out.println("IdFacture " + idFacture);
                System.out.println("date " + date);
                System.out.println("montant " + montant);
                FactureBill facture = new FactureBill(idFacture, date, montant);
                factures.add(facture);
            }

            return factures;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String getPassword(String login) throws SQLException {
        String req = " ";
        String requette = "SELECT id FROM clients WHERE login = '" + login;
        ResultSet executed = DatabaseConnectionBean.executeQuery(requette);

        if(executed.next())
        {
            req = executed.getString("password");
        }

        return  req;
    }
}

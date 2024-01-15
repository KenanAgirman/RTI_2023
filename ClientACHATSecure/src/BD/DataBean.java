package BD;


import Protocol.FactureBill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
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

    public static LinkedList<FactureBill> getALLFactures(boolean paye, int idClient){
        try {
            int p;

            if(paye) p = 1;
            else p = 0;

            String query = "SELECT * FROM factures WHERE paye = " + 1 + " AND idClient = " + idClient + " AND dateFacture IS NOT NULL";

            ResultSet resultSet = DatabaseConnectionBean.executeQuery(query);

            LinkedList<FactureBill> factures = new LinkedList<>();

            while (resultSet.next()){
                int idFacture = resultSet.getInt("idFacture");
                String dateFacture = resultSet.getString("dateFacture");
                Float montant = resultSet.getFloat("montant");

                FactureBill facture = new FactureBill(idFacture, dateFacture, montant);

                factures.add(facture);
            }

            return factures;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    public static List<FactureBill> getALLFactures(int idClient, int check) {
        try {
            List<FactureBill> factures = new ArrayList<>();
            String requete = "SELECT * FROM factures WHERE paye = " + check + " AND idClient = " + idClient;
            System.out.println("idClient " + idClient);
            System.out.println("check " + check);

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

     */
    public boolean PayemenetFactureVisa(int idFacture) throws SQLException {
        String requete = "UPDATE factures SET paye = '1' WHERE idFacture = " + idFacture;
        int executed = DatabaseConnectionBean.executeUpdate(requete);

        if(executed > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public String getPassword(String login) throws SQLException {
        String req = " ";
        String requette = "SELECT * FROM clients WHERE login = '" + login + "'";
        ResultSet executed = DatabaseConnectionBean.executeQuery(requette);

        if (executed.next()) {
            req = executed.getString("password");
        }

        System.out.println("PASSWORD " + req);
        return req;
    }


}

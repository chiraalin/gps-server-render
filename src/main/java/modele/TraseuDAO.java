package aplicatie.modele;




import aplicatie.modele.Traseu;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TraseuDAO {
    private Connection conexiune;

    public TraseuDAO(Connection conexiune) {
        this.conexiune = conexiune;
    }

    public void adaugaTraseu(Traseu traseu) throws SQLException {
        String sql = "INSERT INTO Trasee (id_masina, dataOra, latitudine, longitudine) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexiune.prepareStatement(sql)) {
            stmt.setInt(1, traseu.getIdMasina());
            stmt.setTimestamp(2, Timestamp.valueOf(traseu.getDataOra()));
            stmt.setDouble(3, traseu.getLatitudine());
            stmt.setDouble(4, traseu.getLongitudine());
            stmt.executeUpdate();
        }
    }

    public List<Traseu> getTraseePentruMasinaInInterval(int idMasina, LocalDateTime deLa, LocalDateTime panaLa) throws SQLException {
        List<Traseu> trasee = new ArrayList<>();
        String sql = "SELECT * FROM Trasee WHERE id_masina = ? AND dataOra BETWEEN ? AND ? ORDER BY dataOra";

        try (PreparedStatement stmt = conexiune.prepareStatement(sql)) {
            stmt.setInt(1, idMasina);
            stmt.setTimestamp(2, Timestamp.valueOf(deLa));
            stmt.setTimestamp(3, Timestamp.valueOf(panaLa));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Traseu t = new Traseu(
                            rs.getInt("id"),
                            rs.getInt("id_masina"),
                            rs.getTimestamp("dataOra").toLocalDateTime(),
                            rs.getDouble("latitudine"),
                            rs.getDouble("longitudine")
                    );
                    trasee.add(t);
                }
            }
        }

        return trasee;
    }

    public Traseu getUltimaLocatiePentruMasina(int idMasina) throws SQLException {
        String sql = "SELECT TOP 1 * FROM Trasee WHERE id_masina = ? ORDER BY dataOra DESC";
        try (PreparedStatement stmt = conexiune.prepareStatement(sql)) {
            stmt.setInt(1, idMasina);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Traseu(
                            rs.getInt("id"),
                            rs.getInt("id_masina"),
                            rs.getTimestamp("dataOra").toLocalDateTime(),
                            rs.getDouble("latitudine"),
                            rs.getDouble("longitudine")
                    );
                }
            }
        }
        return null;
    }

    public void stergeTraseePentruMasina(int idMasina) throws SQLException {
        String sql = "DELETE FROM Trasee WHERE id_masina = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(sql)) {
            stmt.setInt(1, idMasina);
            stmt.executeUpdate();
        }
    }


    public List<Traseu> getTraseePentruMasina(int idMasina) throws SQLException {
        List<Traseu> trasee = new ArrayList<>();
        String sql = "SELECT * FROM Trasee WHERE id_masina = ? ORDER BY dataOra";
        try (PreparedStatement stmt = conexiune.prepareStatement(sql)) {
            stmt.setInt(1, idMasina);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Traseu t = new Traseu(
                            rs.getInt("id"),
                            rs.getInt("id_masina"),
                            rs.getTimestamp("dataOra").toLocalDateTime(),
                            rs.getDouble("latitudine"),
                            rs.getDouble("longitudine")
                    );
                    trasee.add(t);
                }
            }
        }
        return trasee;
    }
}

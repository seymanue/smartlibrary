package repo;

import db.Database;
import model.Loan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository {

    // Kitap ödünç ver
    public void add(Loan loan) {
        String sql = "INSERT INTO loans(bookId, studentId, dateBorrowed, dateReturned) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, loan.getBookId());
            ps.setInt(2, loan.getStudentId());
            ps.setString(3, loan.getDateBorrowed());
            ps.setString(4, loan.getDateReturned());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Add loan error: " + e.getMessage());
        }
    }

    // Tüm ödünçleri listele
    public List<Loan> getAll() {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loans";

        try (Connection conn = Database.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Loan l = new Loan(
                        rs.getInt("id"),
                        rs.getInt("bookId"),
                        rs.getInt("studentId"),
                        rs.getString("dateBorrowed"),
                        rs.getString("dateReturned")
                );
                list.add(l);
            }

        } catch (SQLException e) {
            System.out.println("Get all loans error: " + e.getMessage());
        }
        return list;
    }

    // Kitabı geri teslim al
    public void returnBook(int loanId, String returnDate) {
        String sql = "UPDATE loans SET dateReturned=? WHERE id=?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, returnDate);
            ps.setInt(2, loanId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Return book error: " + e.getMessage());
        }
    }

    // Kitap şu an ödünçte mi?
    public boolean isBookBorrowed(int bookId) {
        String sql = "SELECT COUNT(*) FROM loans WHERE bookId=? AND dateReturned IS NULL";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Check borrowed error: " + e.getMessage());
        }
        return false;
    }
}

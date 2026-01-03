package repo;

import db.Database;
import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    public void add(Book book) {
        String sql = "INSERT INTO books(title, author, year) VALUES (?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getYear());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Add book error: " + e.getMessage());
        }
    }

    public List<Book> getAll() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = Database.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Book b = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year")
                );
                list.add(b);
            }

        } catch (SQLException e) {
            System.out.println("Get all books error: " + e.getMessage());
        }
        return list;
    }

    public Book getById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year")
                );
            }

        } catch (SQLException e) {
            System.out.println("Get book by id error: " + e.getMessage());
        }
        return null;
    }

    public void update(Book book) {
        String sql = "UPDATE books SET title=?, author=?, year=? WHERE id=?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getYear());
            ps.setInt(4, book.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Update book error: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM books WHERE id=?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Delete book error: " + e.getMessage());
        }
    }
}

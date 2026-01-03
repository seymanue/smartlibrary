package repo;

import db.Database;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    public void add(Student student) {
        String sql = "INSERT INTO students(name, department) VALUES (?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getDepartment());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Add student error: " + e.getMessage());
        }
    }

    public List<Student> getAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = Database.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department")
                );
                list.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Get all students error: " + e.getMessage());
        }
        return list;
    }

    public Student getById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department")
                );
            }

        } catch (SQLException e) {
            System.out.println("Get student by id error: " + e.getMessage());
        }
        return null;
    }

    public void update(Student student) {
        String sql = "UPDATE students SET name=?, department=? WHERE id=?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getDepartment());
            ps.setInt(3, student.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Update student error: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM students WHERE id=?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Delete student error: " + e.getMessage());
        }
    }
}

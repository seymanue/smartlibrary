package app;

import db.Database;
import model.Book;
import model.Student;
import model.Loan;
import repo.BookRepository;
import repo.StudentRepository;
import repo.LoanRepository;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final BookRepository bookRepo = new BookRepository();
    private static final StudentRepository studentRepo = new StudentRepository();
    private static final LoanRepository loanRepo = new LoanRepository();

    public static void main(String[] args) {
        Database.init();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== SMARTLIBRARY MENU ===");
            System.out.println("1) Kitap Ekle");
            System.out.println("2) Kitapları Listele");
            System.out.println("3) Öğrenci Ekle");
            System.out.println("4) Öğrencileri Listele");
            System.out.println("5) Kitap Ödünç Ver");
            System.out.println("6) Ödünç Listesini Görüntüle");
            System.out.println("7) Kitap Geri Teslim Al");
            System.out.println("0) Çıkış");
            System.out.print("Seçim: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> addBook(sc);
                case "2" -> listBooks();
                case "3" -> addStudent(sc);
                case "4" -> listStudents();
                case "5" -> borrowBook(sc);
                case "6" -> listLoans();
                case "7" -> returnBook(sc);
                case "0" -> {
                    System.out.println("Çıkış yapıldı.");
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private static void addBook(Scanner sc) {
        System.out.print("Kitap adı: ");
        String title = sc.nextLine();

        System.out.print("Yazar: ");
        String author = sc.nextLine();

        System.out.print("Yıl: ");
        int year = readInt(sc);

        bookRepo.add(new Book(title, author, year));
        System.out.println("✅ Kitap eklendi.");
    }

    private static void listBooks() {
        List<Book> books = bookRepo.getAll();
        System.out.println("\n--- KİTAPLAR ---");
        if (books.isEmpty()) {
            System.out.println("Kayıt yok.");
            return;
        }
        books.forEach(System.out::println);
    }

    private static void addStudent(Scanner sc) {
        System.out.print("Öğrenci adı: ");
        String name = sc.nextLine();

        System.out.print("Bölüm: ");
        String department = sc.nextLine();

        studentRepo.add(new Student(name, department));
        System.out.println("✅ Öğrenci eklendi.");
    }

    private static void listStudents() {
        List<Student> students = studentRepo.getAll();
        System.out.println("\n--- ÖĞRENCİLER ---");
        if (students.isEmpty()) {
            System.out.println("Kayıt yok.");
            return;
        }
        students.forEach(System.out::println);
    }

    private static void borrowBook(Scanner sc) {
        System.out.print("Kitap ID: ");
        int bookId = readInt(sc);

        System.out.print("Öğrenci ID: ");
        int studentId = readInt(sc);

        System.out.print("Tarih (YYYY-MM-DD): ");
        String dateBorrowed = sc.nextLine().trim();

        if (loanRepo.isBookBorrowed(bookId)) {
            System.out.println("⚠️ Bu kitap şu an ödünçte. Önce iade edilmesi gerekiyor.");
            return;
        }

        if (bookRepo.getById(bookId) == null) {
            System.out.println("⚠️ Böyle bir kitap ID yok.");
            return;
        }
        if (studentRepo.getById(studentId) == null) {
            System.out.println("⚠️ Böyle bir öğrenci ID yok.");
            return;
        }

        loanRepo.add(new Loan(bookId, studentId, dateBorrowed));
        System.out.println("✅ Ödünç verildi.");
    }

    private static void listLoans() {
        List<Loan> loans = loanRepo.getAll();
        System.out.println("\n--- ÖDÜNÇLER ---");
        if (loans.isEmpty()) {
            System.out.println("Kayıt yok.");
            return;
        }
        loans.forEach(System.out::println);
    }

    private static void returnBook(Scanner sc) {
        System.out.print("Ödünç ID (loan id): ");
        int loanId = readInt(sc);

        System.out.print("İade tarihi (YYYY-MM-DD): ");
        String returnDate = sc.nextLine().trim();

        loanRepo.returnBook(loanId, returnDate);
        System.out.println("✅ İade işlemi yapıldı.");
    }

    private static int readInt(Scanner sc) {
        while (true) {
            try {
                String s = sc.nextLine().trim();
                return Integer.parseInt(s);
            } catch (Exception e) {
                System.out.print("Sayı gir: ");
            }
        }
    }
}

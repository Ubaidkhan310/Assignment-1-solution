package sdaLabTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// DOMAIN LAYER (Entity)
class Book {
    private String title;
    private String author;
    private String details;
// CONTRUCTOR 
    public Book(String title, String author, String details) {
        this.title = title;
        this.author = author;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author;
    }
}

// REPOSITORY LAYER (Data Access)
interface BookRepository {
    List<Book> getBooks();

    Book getBookDetails(String title);
}

class InMemoryBookRepository implements BookRepository {
    private List<Book> books;

    public InMemoryBookRepository() {
        books = new ArrayList<>();
        books.add(new Book("Java Programming", "John Doe", "An introductory book on Java."));
        books.add(new Book("Data Structures", "Jane Doe", "An in-depth guide to data structures."));
        books.add(new Book("Web Development", "Alice Smith", "Basics of web development with HTML, CSS, and JS."));
    }

    @Override
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public Book getBookDetails(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }
}

// FACTORY (for creating repositories)
class BookRepositoryFactory {
    public static BookRepository createBookRepository(String type) {
        if (type.equalsIgnoreCase("inMemory")) {
            return new InMemoryBookRepository();
        }
        throw new IllegalArgumentException("Unsupported repository type");
    }
}

// SINGLETON (Application Layer Entry Point)
class WebApplication {
    private static WebApplication instance;
    private BookRepository bookRepository;

    private WebApplication(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public static WebApplication getInstance(BookRepository bookRepository) {
        if (instance == null) {
            instance = new WebApplication(bookRepository);
        }
        return instance;
    }

    public List<Book> browseBooks() {
        return bookRepository.getBooks();
    }

    public Book selectBook(String title) {
        return bookRepository.getBookDetails(title);
    }
}

// SERVICE LAYER (Facade Pattern for business logic)
class BookServiceFacade {
    private WebApplication webApp;

    public BookServiceFacade(WebApplication webApp) {
        this.webApp = webApp;
    }

    public List<Book> getAvailableBooks() {
        return webApp.browseBooks();
    }

    public Book getBookDetails(String title) {
        return webApp.selectBook(title);
    }
}

// PRESENTATION LAYER (Handles user interaction)
class UserInterface {
    private BookServiceFacade bookService;
    private Scanner scanner;

    public UserInterface(BookServiceFacade bookService) {
        this.bookService = bookService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Book Browsing Application!");
        displayBookList();
        selectAndDisplayBook();
        scanner.close();
    }

    private void displayBookList() {
        System.out.println("Fetching list of available books...");
        List<Book> books = bookService.getAvailableBooks();
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private void selectAndDisplayBook() {
        System.out.print("\nEnter the title of the book to view details: ");
        String title = scanner.nextLine();

        Book selectedBook = bookService.getBookDetails(title);
        if (selectedBook != null) {
            System.out.println("\nBook Details:");
            System.out.println("Title: " + selectedBook.getTitle());
            System.out.println("Details: " + selectedBook.getDetails());
        } else {
            System.out.println("Book not found!");
        }
    }
}

// APPLICATION LAYER (Entry Point)
public class useCase {
    public static void main(String[] args) {
        // Repository Factory to choose data source
        BookRepository bookRepository = BookRepositoryFactory.createBookRepository("inMemory");

        // Singleton for WebApplication
        WebApplication webApp = WebApplication.getInstance(bookRepository);

        // Facade for Service Layer
        BookServiceFacade bookService = new BookServiceFacade(webApp);

        // Presentation Layer for User Interaction
        UserInterface userInterface = new UserInterface(bookService);

        // Start the application
        userInterface.start();
    }
}

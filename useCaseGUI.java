package sdaLabTask;

import javax.swing.SwingUtilities;

public class useCaseGUI {
    public static void main(String[] args) {
        // Repository Factory to choose data source
        BookRepository bookRepository = BookRepositoryFactory.createBookRepository("inMemory");

        // Singleton for WebApplication
        WebApplication webApp = WebApplication.getInstance(bookRepository);

        // Facade for Service Layer
        BookServiceFacade bookService = new BookServiceFacade(webApp);

        // Initialize and show the GUI
        SwingUtilities.invokeLater(() -> {
            BookBrowserGUI gui = new BookBrowserGUI(bookService);
            gui.setVisible(true);
        });
    }
}

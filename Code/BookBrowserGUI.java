package sdaLabTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// BookBrowserGUI: GUI class for browsing books
public class BookBrowserGUI extends JFrame {
    private BookServiceFacade bookService;
    private JList<String> bookList;
    private JTextArea bookDetailsArea;

    // Constructor
    public BookBrowserGUI(BookServiceFacade bookService) {
        this.bookService = bookService;
        
        setTitle("Book Browsing Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Book List Panel
        JPanel listPanel = new JPanel(new BorderLayout());
        JLabel listLabel = new JLabel("Available Books:");
        listPanel.add(listLabel, BorderLayout.NORTH);

        // Fetch book titles for the list
        List<Book> books = bookService.getAvailableBooks();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Book book : books) {
            listModel.addElement(book.getTitle());
        }
        bookList = new JList<>(listModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(bookList);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        // Book Details Panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        JLabel detailsLabel = new JLabel("Book Details:");
        detailsPanel.add(detailsLabel, BorderLayout.NORTH);

        // Text area for displaying selected book details
        bookDetailsArea = new JTextArea();
        bookDetailsArea.setEditable(false);
        bookDetailsArea.setLineWrap(true);
        bookDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScrollPane = new JScrollPane(bookDetailsArea);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);

        // Button to view details of the selected book
        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displaySelectedBookDetails();
            }
        });

        // Add components to main frame
        add(listPanel, BorderLayout.WEST);
        add(detailsPanel, BorderLayout.CENTER);
        add(viewDetailsButton, BorderLayout.SOUTH);
    }

    // Display details of the selected book in the text area
    private void displaySelectedBookDetails() {
        String selectedTitle = bookList.getSelectedValue();
        if (selectedTitle != null) {
            Book selectedBook = bookService.getBookDetails(selectedTitle);
            if (selectedBook != null) {
                bookDetailsArea.setText("Title: " + selectedBook.getTitle() + "\n"
                        + "Details: " + selectedBook.getDetails());
            } else {
                bookDetailsArea.setText("Book details not found.");
            }
        } else {
            bookDetailsArea.setText("Please select a book from the list.");
        }
    }
}




package Problem3;
import java.util.ArrayList;

class Library {
    ArrayList<String> books = new ArrayList<>();


void addBook(String title) {
    try {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Book title cannot be null or empty."
            );
        }

        if (books.contains(title)) {
            System.out.println("Book already exists!");
            return;
        }

        books.add(title);
        System.out.println(title + " added successfully.");

    } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
    }
}

void insertBook(int index, String title) {
    try {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Book title cannot be null or empty."
            );
        }

        if (index < 0 || index > books.size()) {
            throw new IndexOutOfBoundsException("Invalid index!");
        }

        if (books.contains(title)) {
            System.out.println("Book already exists!");
            return;
        }

        books.add(index, title);
        System.out.println(title + " inserted at index " + index + " successfully.");

    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
        System.out.println(e.getMessage());
    }
}
    void updateBook(String oldTitle, String newTitle) {
       int index = -1;
        try {
            if (newTitle == null || newTitle.trim().isEmpty()) {
                throw new IllegalArgumentException("New book title cannot be null or empty.");
            }
            index = books.indexOf(oldTitle);
            if (index<0 || index >= books.size()) {
                throw new IllegalArgumentException("Book not found!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        books.set(index, newTitle);
        System.out.println(oldTitle + " updated to " + newTitle + " successfully.");
    }

    void removeBook(String title) {
        if (books.remove(title)) {
            System.out.println(title + " removed successfully.");
        } else {
            System.out.println("Book not found!");
        }
    }

    void searchBook(String title) {
       try {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Book title cannot be null or empty.");
            }
            if (books.contains(title)) {
                System.out.println(title + " is available in the library.");
            } else {
                System.out.println(title + " is not available in the library.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    void displayBooks() {
        System.out.println("-----All Books-----");
        System.out.println("Books in the library: " + books);
    }

    int getTotalBooks() {
        return books.size();
    }
}

public class Statement1 {
    public static void main(String[] args) {
        Library library = new Library();

        library.addBook("Java");
        library.addBook("Python");
        library.addBook("Data Structures");
        library.displayBooks();
        
        System.out.println("==================================================================");
        library.insertBook(1, "Database Management");
        library.displayBooks();
        System.out.println("==================================================================");
        library.updateBook("Python", "Advanced Python");
        library.displayBooks();
        System.out.println("==================================================================");
        library.searchBook("Java");
        library.searchBook("C++");
        System.out.println("==================================================================");
        library.removeBook("Data Structures");
        library.displayBooks();
        System.out.println("==================================================================");
        System.out.println("Total books: " + library.getTotalBooks());
    }
}

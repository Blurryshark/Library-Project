import org.junit.jupiter.api.*;

import javax.swing.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private String testISBN = "1337";
    private String testTitle = "Cold Days";
    private String testSubject = "Fiction";
    private int testPageCount = 1337;
    private String testAuthor = "Jim Butcher";
    private LocalDate testDueDate = LocalDate.now();

    private Book testBook = null;

    @BeforeAll
    static void mainSetup(){
        System.out.println("Main setup...");
    }

    @AfterAll
    static void mainTearDown(){
        System.out.println("...Main teardown");
    }
    @BeforeEach
    void setUp() {
        testBook = new Book(testISBN, testTitle, testSubject, testPageCount, testAuthor, testDueDate);
    }

    @AfterEach
    void tearDown() {
        testBook = null;
    }

    @Test
    void testConstructor(){
        Book testBook2 = null;
        assertNull(testBook2);

        testBook2 = testBook;
        assertNotNull(testBook2);
        assertEquals(testBook2, testBook);
    }

    @Test
    void testToString() {
        System.out.println("*testing 'toString'*");
        assertEquals(testBook.toString(), "Cold Days by Jim Butcher ISBN: 1337");
    }

    @Test
    void getIsbn() {
        System.out.println("*testing 'getIsbn'*");
        assertEquals(testBook.getIsbn(), 1337);
    }

    @Test
    void setIsbn() {
        System.out.println("*testing 'setIsbn'*");
        String newISBN = "42";
        testBook.setIsbn(newISBN);
        assertEquals(testBook.getIsbn(), newISBN);
    }

    @Test
    void getTitle() {
        System.out.println("*testing 'getTitle'*");
        assertEquals(testBook.getTitle(), "Cold Days");
    }

    @Test
    void setTitle() {
        System.out.println("*testing 'setTitle'*");
        String newTitle = "Snow Crash";
        testBook.setTitle(newTitle);
        assertEquals(testBook.getTitle(), newTitle);
    }

    @Test
    void getSubject() {
        System.out.println("*testing 'getSubject'*");
        assertEquals(testBook.getSubject(), "Fiction");
    }

    @Test
    void setSubject() {
        System.out.println("*testing 'setSubject'*");
        String newSubject = "Science Fiction";
        testBook.setSubject(newSubject);
        assertEquals(testBook.getSubject(), newSubject);
    }

    @Test
    void getPageCount() {
        System.out.println("*testing 'getPageCount'*");
        assertEquals(testBook.getPageCount(), 1337);
    }

    @Test
    void setPageCount() {
        System.out.println("*testing 'setPageCount'*");
        int newPageCount = 42;
        testBook.setPageCount(newPageCount);
        assertEquals(testBook.getPageCount(), newPageCount);
    }

    @Test
    void getAuthor() {
        System.out.println("*testing 'getAuthor'*");
        assertEquals(testBook.getAuthor(), "Jim Butcher");
    }

    @Test
    void setAuthor() {
        System.out.println("*testing 'setAuthor'*");
        String newAuthor = "Neal Stephenson";
        testBook.setAuthor(newAuthor);
        assertEquals(testBook.getAuthor(), newAuthor);
    }

    @Test
    void getDueDate() {
        System.out.println("*testing 'getDueDate'*");
        assertEquals(testBook.getDueDate(), LocalDate.now());
    }

    @Test
    void setDueDate() {
        System.out.println("*testing 'setDeuDate'*");
        assertEquals(testBook.getDueDate(), LocalDate.now());
        testBook.setDueDate(LocalDate.MIN);
        assertEquals(testBook.getDueDate(), LocalDate.MIN);
    }

    @Test
    void testEquals() {
        System.out.println("*testing 'equals'*");
        Book testBook2 = new Book("42", "Neuromancer", "Science Fiction", 42, "William Gibson", LocalDate.now());
        assertNotEquals(testBook, testBook2);

        Book testBook3 = new Book(testISBN, testTitle, testSubject, testPageCount, testAuthor, testDueDate);
        assertEquals(testBook3, testBook);
    }

}
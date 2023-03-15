/*
* Liam Cristescu
* 12 Feb 2023
* Test class for the Reader class in the Library
*/
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    private String testISBN = "1337";
    private String testTitle = "Fool Moon";
    private String testSubject = "Fiction";
    private int testPageCount = 1337;
    private String testAuthor = "Jim Butcher";
    private LocalDate testDueDate = LocalDate.now();
    private String testName = "James Marsters";
    private String testPhone = "123-456-6789";
    private int testCardNumber = 12345;
    private List<Book> testBooks = new ArrayList<>();

    private Book testBook = null;
    private Reader testReader = null;

    @BeforeAll
    static void mainSetup(){
        System.out.println("Main setup...");
    }

    @AfterAll
    static void mainTearDown(){
        System.out.println("...Main teardown");
    }

    @AfterEach
    void tearDown() {
        testBook = null;
        testReader = null;
    }

    @BeforeEach
    void setUp() {
        testBook = new Book (testISBN, testTitle, testSubject,testPageCount,testAuthor, testDueDate);
        testReader = new Reader(testCardNumber,testName,testPhone);
    }

    @Test
    void addBook() {
        assertEquals(testReader.addBook(testBook), Code.SUCCESS);
        assertEquals(testReader.addBook(testBook), Code.BOOK_ALREADY_CHECKED_OUT_ERROR);
    }

    @Test
    void removeBook() {
        assertEquals(testReader.removeBook(testBook), Code.READER_DOESNT_HAVE_BOOK_ERROR);
        assertEquals(testReader.addBook(testBook), Code.SUCCESS);
    }

    @Test
    void hasBook() {
        assertFalse(testReader.hasBook(testBook), String.valueOf(false));
        testReader.addBook(testBook);
        assertTrue(testReader.hasBook(testBook), String.valueOf(true));
    }

    @Test
    void getBookCount() {
        assertEquals(testReader.getBookCount(), 0);
        testReader.addBook(testBook);
        assertEquals(testReader.getBookCount(), 1);
        testReader.removeBook(testBook);
        assertEquals(testReader.getBookCount(), 0);
    }

    @Test
    void getBooks() {
        List<Book> testList = new ArrayList<>();
        testList.add(testBook);
        testReader.addBook(testBook);
        assertEquals(testReader.getBooks(), testList);
    }

    @Test
    void setBooks() {
        List<Book> testList = new ArrayList<>();
        testList.add(testBook);
        testReader.setBooks(testList);
        assertEquals(testReader.getBooks(), testList);
    }

    @Test
    void getCardNumber() {
        assertEquals(testReader.getCardNumber(), testCardNumber);
    }

    @Test
    void setCardNumber() {
        testReader.setCardNumber(12346);
        assertNotEquals(testReader.getCardNumber(), testCardNumber);
    }

    @Test
    void getName() {
        assertEquals(testReader.getName(), testName);
    }
    @Test
    void setName() {
        testReader.setName("Harry Dresden");
        assertNotEquals(testReader.getName(), testName);
    }

    @Test
    void getPhone() {
        assertEquals(testReader.getPhone(), testPhone);
    }

    @Test
    void setPhone() {
        testReader.setPhone("987-654-3210");
        assertNotEquals(testReader.getPhone(), testPhone);
    }
}
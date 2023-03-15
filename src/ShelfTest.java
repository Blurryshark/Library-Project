/*
* Liam Cristescu
* 03/05/2023
* Test class for Shelf
*/
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {

    private int testShelfNumber = 1;
    private String testISBN = "1337";
    private String testTitle = "Fool Moon";
    private String testSubject = "Fiction";
    private int testPageCount = 1337;
    private String testAuthor = "Jim Butcher";
    private LocalDate testDueDate = LocalDate.now();

    private Book testBook = null;
    private Shelf testShelf = null;

    @BeforeAll
    static void mainSetup() {
        System.out.println("main setup...");
    }

    @AfterAll
    static void mainTakedown() {
        System.out.println("...main takedown");
    }

    @BeforeEach
    void setUp(){
        testShelf = new Shelf(testShelfNumber, testSubject);
        testBook = new Book(testISBN, testTitle, testSubject, testPageCount,testAuthor,testDueDate);
    }

    @AfterEach
    void takeDown(){
        testBook = null;
        testShelf = null;
    }

    @Test
    void testConstructor(){
        Shelf testShelf2 = null;
        assertNull(testShelf2);

        testShelf2 = testShelf;
        assertNotNull(testShelf2);
        assertEquals(testShelf2, testShelf);
    }

    @Test
    void addBook() {
        String testSubject2 = "Mystery";
        Book testBook2 = new Book(testISBN, testTitle, testSubject2, testPageCount, testAuthor, testDueDate);

        assertEquals(Code.SUCCESS, testShelf.addBook(testBook));
        assertEquals(1, testShelf.getBooks().get(testBook));

        assertEquals(Code.SUCCESS, testShelf.addBook(testBook));
        assertEquals(2, testShelf.getBooks().get(testBook));

        assertEquals(Code.SHELF_SUBJECT_MISMATCH_ERROR, testShelf.addBook(testBook2));
        assertEquals(2, testShelf.getBooks().get(testBook));
    }

    @Test
    void getBookCount() {
        assertEquals(-1, testShelf.getBookCount(testBook));
        double rand = Math.random()*100;

        for(int i = 0; i < (int)rand; i++){
            testShelf.addBook(testBook);
        }
        assertEquals((int)rand, testShelf.getBookCount(testBook));
    }

    @Test
    void getBooks() {
        HashMap<Book, Integer> testMap = new HashMap<>();
        assertEquals(testMap, testShelf.getBooks());
    }

    @Test
    void getShelfNumber() {
        assertEquals(testShelfNumber, testShelf.getShelfNumber());
    }

    @Test
    void getSubject() {
        assertEquals(testSubject, testShelf.getSubject());
    }

    @Test
    void listBooks() {
        String expected = "1 books on shelf: 1 : Fiction\nFool Moon by Jim Butcher ISBN:1337 1\n";
        System.out.println("EXPECTED: " + expected);

        testShelf.addBook(testBook);

        System.out.println("ACTUAL: " + testShelf.listBooks());
        assertEquals(expected, testShelf.listBooks());

        Book testBook2 = new Book("42","Cold Days", "Fiction",100, "Jim Butcher", LocalDate.now());
        testShelf.addBook(testBook2);
        expected = "2 books on shelf: 1 : Fiction\nFool Moon by Jim Butcher ISBN:1337 1\nCold Days by Jim Butcher ISBN:42 1\n";
        System.out.println("EXPECTED: " + expected);
        System.out.println("ACTUAL: " + testShelf.listBooks());

        assertEquals(expected, testShelf.listBooks());
    }

    @Test
    void removeBook() {
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, testShelf.removeBook(testBook));
        assertEquals(Code.SUCCESS, testShelf.addBook(testBook));
        assertEquals(1, testShelf.getBookCount(testBook));
        assertEquals(Code.SUCCESS, testShelf.removeBook(testBook));
        assertEquals(0, testShelf.getBookCount(testBook));

    }

    @Test
    void setBooks() {
        HashMap<Book, Integer> testMap = new HashMap<>();
        testShelf.addBook(testBook);
        assertNotEquals(testMap, testShelf.getBooks());
        testShelf.setBooks(testMap);
        assertEquals(testMap, testShelf.getBooks());
    }

    @Test
    void setShelfNumber() {
        int num = 2;
        assertNotEquals(num, testShelf.getShelfNumber());
        testShelf.setShelfNumber(num);
        assertEquals(num, testShelf.getShelfNumber());
    }

    @Test
    void setSubject() {
        String sub = "Fantasy";
        assertNotEquals(sub, testShelf.getSubject());
        testShelf.setSubject(sub);
        assertEquals(sub, testShelf.getSubject());
    }

}
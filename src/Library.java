/*
*Liam Cristescu
* 03.11.2023
* This class defines the titular library in the Library project
*/
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Library {
    public static final int LENDING_LIMIT = 5;
    private String name;
    private static int libraryCard;
    private List<Reader> readers;
    private HashMap<String, Shelf> shelves;
    private HashMap<Book, Integer> books;

    public Library (String name){
        this.name = name;
        books = new HashMap<>();
        shelves = new HashMap<>();
        readers = new ArrayList<>();
        libraryCard = 0;
    }
    public Code init(String filename){
        /*Create a Scanner object and attempt to open the file in a try block. If it can't be found, return an error in
        * catch block*/
        Scanner s;
        FileReader f;
        try{
            f = new FileReader(filename);
            s = new Scanner(f);
        }catch (FileNotFoundException e) {
            return Code.FILE_NOT_FOUND_ERROR;
        }
        /*This chunk of 'init()' initalizes and lists all the books in the given file*/
        int count = convertInt(s.nextLine(), Code.BOOK_COUNT_ERROR);

        if(count < 0){
            return errorCode(count);
        }
        initBooks(count, s);
        listBooks();

        /*This chunk of 'init()' initializes and lists all the shelves in the given file*/
        count = convertInt(s.nextLine());

        if(count < 0){
            return errorCode(count);
        }
        initShelves(count, s);
        listShelves(true);      //TEMPORARY VALUE

        /*This chunk of 'init()' initializes and lists all the readers in the given file*/
        count = convertInt(s.nextLine());

        if(count < 0){
            return errorCode(count);
        }
        initReaders(count, s);
        listReaders();
        return Code.SUCCESS;
    }
    /*This probably does not need to be it's own function given that it's only one line, but I am putting it here for
    * posterity, in case additional changes are needed in the future.*/
    public static int convertInt(String input){
        return Integer.parseInt(input);
    }
    /*The original prompt named this functon 'parseInt,' but after learning the function of this function i have renamed it
    * to processCode in order to make the code more understandable to me. */
    public static int convertInt(String recordCountString, Code code){
        int errorCode;
        try{
            errorCode = Integer.parseInt(recordCountString);
        } catch (NumberFormatException e){
            System.out.println("Value which caused error: " + recordCountString + "\nError Message: " + code.getMessage());
            /*This is rather ineligent from my perspective, but it seemed the easiest way to get the job done. In essence,
             * in order to facilitate the printing of a certain message depending on the code object supplied, we are going to
             * use a switch statement to compare the code value of the supplied object with the values of the code objects we
             * are checking for by hard-coding the values into the switch*/
            switch (code.getCode()) {
                case -2:
                    System.out.println("Error: Could not read number of books");
                case -8:
                    System.out.println("Error: could not parse page count");
                case -101:
                    System.out.println("Error: Could not parse date component");
                default:
                    System.out.println("Error: Unknown conversion error");

                    return code.getCode();
            }
        }
        return errorCode;
    }
    /*Nothing fancy here. just a bunch of if statements that go along how the prompt instructs to design this method*/
    public static LocalDate convertDate(String recordCountDate, Code code){
        if(recordCountDate.equals("0000")){
            return LocalDate.of(1970,1,1);
        }
        String[] dateCount = recordCountDate.split("-");
        if (dateCount.length != 3){
            System.out.println("ERROR: date conversion error, could not parse [date]\nUsing default date (1970-01-01)");
            return LocalDate.of(1970,1,1);
        }
        if(convertInt(dateCount[0]) < 0){
            System.out.println("Error converting date: Year [" + convertInt(dateCount[0], Code.DATE_CONVERSION_ERROR) + "]");
            System.out.println("Using default date (1970-01-01)");
            return LocalDate.of(1970,1,1);
        } else if (convertInt(dateCount[1]) < 0){
            System.out.println("Error converting date: Month [" + convertInt(dateCount[1], Code.DATE_CONVERSION_ERROR) + "]");
            System.out.println("Using default date (1970-01-01)");
            return LocalDate.of(1970,1,1);
        } else if (convertInt(dateCount[2]) < 0){
            System.out.println("Error converting date: Day [" + convertInt(dateCount[2], Code.DATE_CONVERSION_ERROR) + "]");
            System.out.println("Using default date (1970-01-01)");
            return LocalDate.of(1970,1,1);
        }
        return LocalDate.of(convertInt(dateCount[0], Code.DATE_CONVERSION_ERROR), convertInt(dateCount[1], Code.DATE_CONVERSION_ERROR),
                convertInt(dateCount[2], Code.DATE_CONVERSION_ERROR));

    }
    /*Parses through the section of the csv file that lists all the books present in the library. bookCount is read from the
    * first line of the file*/
    public Code initBooks(int bookCount, Scanner scan){
        /*Error checking, likely unecessary, but there's no accounting for the intelligence of the people documenting anything
        * nowadays.*/
        if (bookCount < 1){
            return Code.LIBRARY_ERROR;
        }
        /*create an array to store lines from the file*/
        ArrayList<String> bookLines = new ArrayList<>();
        /*scans ONLY THE LINES LISTING THE BOOKS into the previously created array. this will make them easier to parse*/
        for(int i = 0; i < bookCount; i++){
            bookLines.add(scan.nextLine());
        }
        /*enhanced for loop to iterate through the list containing all the book lines*/
        for (String s : bookLines){
            /*split each individual book line into an array to parse them based on how the books are entered into the csv
            * we then check to make sure the the page count isn't less than zero*/
            String[] stringArray = s.split(",");
            if (convertInt(stringArray[Book.PAGE_COUNT_], Code.PAGE_COUNT_ERROR) < 1){
                return Code.PAGE_COUNT_ERROR;
            }
            /*IntelliJ says this is always false, but no reason not to include it. Paranoid? Maybe. But just because you're
            * paranoid doesn't mean there isn't an invisible demon about to eat your face */
            if (convertDate(stringArray[Book.DUE_DATE_], Code.SUCCESS).equals(null)){
                return Code.DATE_CONVERSION_ERROR;
            }
            /*use the addBook function, defined later. Use the static final int values defined in book to pull the elements
            * we need from the stringArray storing the book in order to use them as arguments for the new book object*/
            addBook(new Book(stringArray[Book.ISBN_], stringArray[Book.TITLE_], stringArray[Book.SUBJECT_],
                    convertInt(stringArray[Book.PAGE_COUNT_]), stringArray[Book.AUTHOR_],
                    convertDate(stringArray[Book.DUE_DATE_], Code.SUCCESS)));
        }
        return Code.SUCCESS;
    }
    /*Lists all the books in the 'books' hashmap*/
    public int listBooks(){
        int count = 0;
        for(Book book : books.keySet()){
            count += books.get(book);
            System.out.println(books.get(book) + " copies of " + book.getTitle() + " ISBN:" + book.getIsbn());
        }
        return count;
    }
    /*Initialize all the shelves from the csv file. shelfCount provided from a line parsed in the csv file in the init()
    * method. */
    public Code initShelves(int shelfCount, Scanner scan){
        if (shelfCount < 1){
            return Code.SHELF_COUNT_ERROR;
        }
        /*This behaves largely the same way as addBook(), we parse the lines into an array, and then use the split() function
        * to parse said lines into their own individual arrays. we then use the static final int values defined in Shelf to pull
        * the values required for the constructor from the array*/
        ArrayList<String> shelfLines = new ArrayList<>();
        for (int i = 0; i < shelfCount; i++){
            shelfLines.add(scan.nextLine());
        }
        for (String s : shelfLines) {
            String[] stringArray = s.split(",");
            addShelf(new Shelf(convertInt(stringArray[Shelf.SHELF_NUMBER_]), stringArray[Shelf.SUBJECT_]));
        }
        return Code.SUCCESS;
    }
    /*Lists all the shelves, im unsure of what element of the program decides where showBooks wll be true or false, so
    * as of now, it is permanently set to true in the function call above. */
    public Code listShelves(boolean showBooks){
        if(showBooks){
            for (String name : shelves.keySet()){
                shelves.get(name).listBooks();
            }
        }else {
            for (String name : shelves.keySet()){
                System.out.println(shelves.get(name).toString());
            }
        }
        return Code.SUCCESS;
    }
    /*This functions the same way was initShelves() and initBooks(), but it has a hitch or two that are explained within the function*/
    public Code initReaders(int readerCount, Scanner scan){
        if (readerCount < 0){
            return Code.READER_COUNT_ERROR;
        }
        /*Same deal as before, push all readers into and array, split individual lines into individual arrays for further processing*/
        ArrayList<String> readerLines = new ArrayList<>();
        for (int i = 0; i < readerCount; i++){
            readerLines.add(scan.nextLine());
        }
        for (String s : readerLines){
            String [] stringArray = s.split(",");
            addReader(new Reader(convertInt(stringArray[Reader.CARD_NUMBER_]), stringArray[Reader.NAME_],
                    stringArray[Reader.PHONE_]));
            /*After each reader is created, we need to ensure that they have all the books listed within the csv file.
            * We use a for loop where 'i' is initally set to BOOK_START_, which is an array we use for when, in each given line
            * that represents a reader, the listing of books actually starts. That is to say, much like how we use the static
            * final int values to pull string elements from an array of strings that represents a reader, we are using the static
            * final int value BOOK_START_ to know where in the line books begin to be listed. the for loop is incremented by 2
            * every time to account for the fact that the books are listed by their isbn AND their due date*/
            for (int i = Reader.BOOK_START_; i < stringArray.length - 1; i += 2) {
                /*the first step in this process is to call the checkOutBook function supplying the current reader and the
                * given book (found by isbn) as parameters. */
                Reader r = getReaderByCard(convertInt((stringArray[Reader.CARD_NUMBER_])));
                Book b = getBookByISBN(stringArray[i]);
                checkOutBook(r, b);
                /*the second step is to parse the date into an array of strings and use the static method Integer.parseInt()
                * to change the due date of the book and match it to what is provided in the csv file. */
                String [] dateArray = stringArray[i+1].split("-");
                int year = Integer.parseInt(dateArray[0]);
                int month = Integer.parseInt(dateArray[1]);
                int day = Integer.parseInt(dateArray[2]);
                r.getBooks().get(i - Reader.BOOK_START_).setDueDate(LocalDate.of(year, month, day));
            }
            System.out.println(stringArray[Reader.NAME_] + " added to the library!");
        }
        return Code.SUCCESS;
    }
    /*Checks and makes sure the supplied reader doesn't already exist within the library before adding them*/
    public Code addReader(Reader r){
        if(readers.contains(r)){
            System.out.println(r.getName() + " already has an account!");
            return Code.READER_ALREADY_EXISTS_ERROR;
        }
        for(Reader reader : readers){
            if(r.getCardNumber() == reader.getCardNumber()){
                System.out.println(reader.getName() + " and " + r.getName() + " have the same card number!");
                return Code.READER_CARD_NUMBER_ERROR;
            }
        }
        readers.add(r);
        if(r.getCardNumber() > libraryCard){
            libraryCard = r.getCardNumber();
        }
        return Code.SUCCESS;
    }
    /*This code handles what will happen if the returned book count returned is less than zero, which corresponds to an
     * error code. Conveniently, 'enum' classes have a builtin 'values()' function that allows us to iterate through all
     * the static values */
    public Code errorCode(int bookCount){
        for (Code code : Code.values()){
            if (code.getCode() == bookCount){
                return code;
            }
        }
        /*if we cannot find a static matching the count of books, we simply return an unknown error*/
        return Code.UNKNOWN_ERROR;
    }
    /*Checks whether the supplied book exists within the library before adding it to the stacks. If the given book does
    * exist, ensure that the keyValue tracking the supply of the given book is properly incremented.*/
    public Code addBook(Book newBook){
        if(books.containsKey(newBook)){
            Integer newCount = books.get(newBook);
            newCount += 1;
            books.put(newBook, newCount);
            System.out.println(newCount + " copies of " + newBook.getTitle() + " in the stacks");
        } else {
            books.put(newBook, 1);
            System.out.println(newBook.getTitle() + " added to the stacks");
        }
        for (String subject : shelves.keySet()){
            if(subject.equals(newBook.getSubject())){
                shelves.get(subject).addBook(newBook);
                return Code.SUCCESS;
            }
        }
        System.out.println("No shelf for " + newBook.getSubject() + " books");
        return Code.SHELF_EXISTS_ERROR;
    }
    /*Removes a book from the reader, and adds it back on to the shelf. Checks for various conditions like if the library
    * never had the book in the first place or if the reader doesn't have the book they are trying to return. */
    public Code returnBook(Reader reader, Book book){
        if(!reader.getBooks().contains(book)){
            System.out.println(reader.getName() + " does not have " + book.getTitle() + " checked out...");
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }
        if(!books.containsKey(book)){
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        System.out.println(reader.getName() + " is returning " + book.getTitle());
        Code codeCheck = reader.removeBook(book);
        if(codeCheck.equals(Code.SUCCESS)){
            codeCheck = returnBook(book);
            return codeCheck;
        } else {
            System.out.println("Could not return " + book.getTitle());
            return codeCheck;
        }
    }
    /*This does the same as return book above, but, despite being public, it's more for use within this class and the methods
    * therein.*/
    public Code returnBook(Book book){
        if(!shelves.containsKey(book.getSubject())){
            System.out.println("No shelf for " + book.getTitle());
            return Code.SHELF_EXISTS_ERROR;
        } else {
            shelves.get(book.getSubject()).addBook(book);
            return Code.SUCCESS;
        }
    }
    /*Adds the supplied book to the given shelf. checks for subject mismatches and whether the supplied shelf even exists
    * within the library */
    private Code addBookToShelf(Book book, Shelf shelf){
        Code codeCheck = returnBook(book);
        if (codeCheck.equals(Code.SUCCESS)){
            return codeCheck;
        }
        if(!book.getSubject().equals(shelf.getSubject())){
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
        codeCheck = shelf.addBook(book);
        if (codeCheck.equals(Code.SUCCESS)){
            System.out.println(book.getTitle() + " added to shelf");
            return codeCheck;
        } else {
            System.out.println("Could not add " + book.getTitle() + " to the shelf");
            return codeCheck;
        }
    }
    /*Very straightforward. Checks whether a reader is capable of checking out a book before cheking it out. if the reader is unable
    * to check out a book then the killbots are activates in order to make an example of them*/
    public Code checkOutBook(Reader reader, Book book){
        if(!readers.contains(reader)){
            System.out.println(reader.getName() + " does not have an account here [kill bots activated]");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        if(reader.getBooks().size() > LENDING_LIMIT){
            System.out.println(reader.getName() + " has reached the lending limit (" + LENDING_LIMIT + "), kill bots activated");
            return Code.BOOK_LIMIT_REACHED_ERROR;
        }
        if(!shelves.containsKey(book.getSubject())){
            System.out.println("no shelf for " + book.getSubject() + " books!");
            return Code.SHELF_EXISTS_ERROR;
        }
        if(!books.containsKey(book)){
            System.out.println("Could not find " + book.getTitle());
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        Code codeCheck = reader.addBook(book);
        if(!codeCheck.equals(Code.SUCCESS)){
            System.out.println(book.getTitle() + " could not be check out!");
            return codeCheck;
        }
        codeCheck = shelves.get(book.getSubject()).removeBook(book);
        if(codeCheck.equals(Code.SUCCESS)){
            System.out.println(book.getTitle() + " checked out successfully");
            return codeCheck;
        }
        return codeCheck;
    }
    /*Given an isbn, searches the entire keyset and compares the given string against the books in the library*/
    public Book getBookByISBN(String isbn){
        for (Book book : books.keySet()){
            if(book.getIsbn().equals(isbn)){
                return book;
            }
        }
        System.out.println("ERROR: Could not find a book with isbn:" + isbn);
        return null;
    }
    /*Given a subject, increments the amount of shelves and calls the overloaded function in order to check whether the
    * shelf already exists and then add it to the library*/
    public Code addShelf(String subject){
        int shelfNumber = 1 + shelves.size();
        Shelf newShelf = new Shelf(shelfNumber, subject);
        Code codeCheck = addShelf(newShelf);
        return codeCheck;
    }
    /*overloaded function. checks if the supplied shelf already exists, adds it to the library*/
    public Code addShelf(Shelf newShelf){
        if(shelves.containsKey(newShelf.getSubject())){
            System.out.println("ERROR: Shelf already exists " + newShelf.getSubject());
            return Code.SHELF_EXISTS_ERROR;
        }
        shelves.put(newShelf.getSubject(), newShelf);
        /*parses through every book in the library and checks if it needs to be added to the shelf. */
        for (Book book: books.keySet()){
            if(book.getSubject().equals(newShelf.getSubject())){
                for(int i = 0; i < books.get(book); i++) {
                    addBookToShelf(book, newShelf);
                }
            }
        }
        return Code.SUCCESS;
    }
    /*basic getter given the shelf number*/
    public Shelf getShelf(Integer shelfNum){
        for (String subject : shelves.keySet()){
            if (shelves.get(subject).getShelfNumber() == shelfNum){
                return shelves.get(subject);
            }
        }
        System.out.println("No shelf number " + shelfNum + " found");
        return null;
    }
    /*basic getter given the subject of the shelf*/
    public Shelf getShelf(String subject){
        for (String subjectKey : shelves.keySet()){
            if (subjectKey.equals(subject)){
                return shelves.get(subject);
            }
        }
        System.out.println("No shelf for " + subject + " books");
        return null;
    }
    /*prints every reader's toString() and then returns the amount of readers in the library*/
    public int listReaders(){
        int readerCount = 0;
        for (Reader reader : readers){
            readerCount++;
            System.out.println(reader.toString());
        }
        return readerCount;
    }
    /*Overloaded method, if true it lists the readers AND all the books that they have checked out. */
    public int listReaders(boolean showBooks){
        int readerCount = 0;
        if (!showBooks){
            readerCount = listReaders();
        } else {
            for (Reader reader : readers){
                System.out.print(reader.getName() + "(#" + (readers.indexOf(reader) + 1) + ") has the following books:\n[");
                for (Book book : reader.getBooks()){
                    System.out.print(book.toString());
                }
                System.out.println("]");
                readerCount++;
            }
        }
        return readerCount;
    }
    /*simple getter given a card number*/
    public Reader getReaderByCard(int cardNum){
        for(Reader reader : readers){
            if(reader.getCardNumber() == cardNum){
                return reader;
            }
        }
        System.out.println("Could not find reader with card number #" + cardNum);
        return null;
    }
    /*removes a reader from the library. does some miscellaneous error checking*/
    public Code removeReader(Reader reader){
        if(readers.contains(reader)){
            if(!reader.getBooks().isEmpty()){
                return Code.READER_STILL_HAS_BOOKS_ERROR;
            }
            readers.remove(reader);
            return Code.SUCCESS;
        } else {
            System.out.println(reader.getName() + " is not part of this Library...");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
    }
    public static int getLibraryCardNumber(){
        return libraryCard + 1;
    }
}

/*
* Liam Cristescu
* 3/5/2023
* Shelf class for the library project
*/

import java.util.HashMap;
import java.util.Objects;

public class Shelf {
    public static final int SHELF_NUMBER_ = 0;
    public static final int SUBJECT_ = 1;
    private int shelfNumber;
    private String subject;
    private HashMap<Book, Integer> books;

    /*Constructor*/
    public Shelf(int shelfNumber, String subject){
        this.shelfNumber = shelfNumber;
        this.subject = subject;
        books = new HashMap<>();
    }

    /*Adds a book to the shelf. If the book exists already, simply increment the value
    * associated with the key value. If not, then a new key value pair will be generated.
    * All this is dependent on whether the book's subject matches that of the shelf. A
    * NOT_IMPLEMENTED error is returned outside the ifelse statement so that JDK wont yell
    * at me...*/
    public Code addBook(Book newBook){
        if(books.containsKey(newBook)){
            Integer val = books.get(newBook) + 1;
            books.put(newBook, val);
            System.out.println("Book added to shelf!");
            return Code.SUCCESS;
        } else if (!books.containsKey(newBook) && this.getSubject().equals(newBook.getSubject())){
            books.put(newBook, 1);
            System.out.println("Book added to shelf!");
            return Code.SUCCESS;
        } else if (!this.getSubject().equals(newBook.getSubject())){
            System.out.println("Subject does not match!");
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
        return Code.NOT_IMPLEMENTED_ERROR;
    }
    /*Automatically generated .equals() method*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return shelfNumber == shelf.shelfNumber && Objects.equals(subject, shelf.subject);
    }

    /*Takes a book as input and returns however many copies of the given book exist on the shelf*/
    public int getBookCount(Book book){
        if(!books.containsKey(book)){
            return -1;
        }else {
            return books.get(book);
        }
    }
    /*Standard Getters, HashCode*/
    public HashMap<Book, Integer> getBooks(){
        return books;
    }
    public int getShelfNumber(){
        return shelfNumber;
    }
    public String getSubject(){
        return subject;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shelfNumber, subject);
    }
    /*Iterates through the shelf once to see how many books are on the shelf, and then iterates again to list those
    * books out in a String. Used a StringBuilder object to manage memory a little. Is there a way to do this without
    * iterating through the shelf 2 whole times? possibly, but I would imagine that those methods would make use of
    * variables who's existence would offset the value of removing an iteration from the algorithm*/
    public String listBooks(){
        int bookCount = 0;
        StringBuilder output = new StringBuilder();
//        for (Book currentBook : books.keySet()){
//            bookCount += books.get(currentBook);
//        }
//        output.append(bookCount + " books on shelf: " + this.getShelfNumber()
//         + " : " + this.getSubject() + "\n");
        for (Book currentBook : books.keySet()){
            output.append(currentBook.getTitle() + " by " + currentBook.getAuthor() + " ISBN:" + currentBook.getIsbn()
            + " " + books.get(currentBook) + "\n");
            bookCount += books.get(currentBook);
        }
        return bookCount + " books on shelf: " + this.getShelfNumber()
           + " : " + this.getSubject() + "\n" + output.toString();
    }
    /*Removes a book from the shelf if the given book exists on the shelf. A NOT IMPLEMENTED code method was called
    * to account for areas outside the if statment that would never trigger, but that the JDK would yell at me for not
    * taking into account nonetheless*/
    public Code removeBook(Book book){
        if(!books.containsKey(book)){
            System.out.println(book.getTitle() + " is not kept on this shelf (" + getSubject() + ")...");
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else if (books.containsKey(book) && books.get(book) < 1){
            System.out.println("There aren't any copies of " + book.getTitle() + " on this shelf " + getSubject() + ") at the moment...");
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else if (books.containsKey(book) && books.get(book) >= 1){
            int newCount = books.get(book);
            newCount --;
            books.put(book, newCount);
            System.out.println(book.getTitle() + " successfully removed from shelf...");
            return Code.SUCCESS;
        }
        return Code.NOT_IMPLEMENTED_ERROR;
    }
    /*Standard setters, easy toString() that simply lists the shelf number and the Subject of the shelf*/
    public void setBooks(HashMap<Book, Integer> map){
        this.books = map;
    }
    public void setShelfNumber(int num){
        this.shelfNumber = num;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }
    @Override
    public String toString(){
        return getShelfNumber() + " : " + getSubject();
    }
}

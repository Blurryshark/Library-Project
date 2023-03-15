/*
* Liam Cristescu
* 12 Feb 2023
* Class that represents a reader capable of checking books in and out of the library
*/
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reader {
    public static final int CARD_NUMBER_ = 0;
    public static final int NAME_ = 1;
    public static final int PHONE_ = 2;
    public static final int BOOK_COUNT_ = 3;
    public static final int BOOK_START_ = 4;
    private int cardNumber;
    private String name;
    private String phone;
    private List<Book> books;

    public Reader(int cardNumber, String name, String phone){
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;

        books = new ArrayList<>();
    }

    public Code addBook(Book book){
        if (books.contains(book)){
            return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
        }

        books.add(book);
        return Code.SUCCESS;
    }
    public Code removeBook(Book book){
        if(!books.contains(book)){
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        } else if (books.contains(book)){
            books.remove(book);
            return Code.SUCCESS;
        }
        return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR;
    }
    public boolean hasBook(Book book) {
        return books.contains(book);
    }
    public int getBookCount() {
        return books.size();
    }
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> newBooks) {
        books.clear();
        for (int i = 0; i < newBooks.size(); i ++){
            books.add(newBooks.get(i));
        }
    }
    public int getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(int newCardNumber) {
        cardNumber = newCardNumber;
    }
    public String getName() {
        return name;
    }
    public void setName(String newName) {
        name = newName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String newPhone) {
        phone = newPhone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return cardNumber == reader.cardNumber && Objects.equals(name, reader.name) && Objects.equals(phone, reader.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, name, phone);
    }

    @Override
    public String toString(){
        String out = name + " (#" + cardNumber + ") has checked out {";
        for (int i = 0; i < books.size(); i++){
            out = out + books.get(i) + ", ";
        }
        out = out + "}";
        return out;
    }
}

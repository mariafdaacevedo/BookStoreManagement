package co.book_store.service;

import co.book_store.modelo.Book;

import java.util.List;

public interface IBookService {

    public List<Book> findAllBooks();

    public Book findBookById(Integer idBook);

    public void saveBook(Book book);

    public void deleteBook(Book book);

}

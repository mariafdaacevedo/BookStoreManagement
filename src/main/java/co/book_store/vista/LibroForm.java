package co.book_store.vista;

import co.book_store.modelo.Book;
import co.book_store.service.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LibroForm extends JFrame {

    BookServiceImpl bookService;
    private JPanel panel;
    private JTable tabla;

    private JTextField idBookTexto;
    private JTextField libroTexto;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField stockTexto;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private DefaultTableModel tablaModelo;


    @Autowired
    public LibroForm(BookServiceImpl bookService) {
        this.bookService = bookService;
        iniciarForma();
        // agregar un libro nuevo
        addButton.addActionListener(e -> addBook());
        // editar un libro existente
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                loadBookSelected();
            }
        });
        editButton.addActionListener(e -> editBook());
        deleteButton.addActionListener(e -> deleteBook());
    }

    private void iniciarForma(){
        setContentPane(panel); // es la inicializacion de la caja que tiene el formulario visual
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // para cerrar la aplicacion
        setVisible(true); // para que se visualice el form
        setSize(900,700); // son pixeles
        // centrar el formulario en la pantalla
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width -getWidth() /2);
        int y = (screenSize.height -getHeight() /2);
        setLocation(x,y);
    }

    private void createUIComponents() {
        // creamos el idBook oculto
        idBookTexto = new JTextField("");
        idBookTexto.setVisible(false);
        // creamos la tabla
        this.tablaModelo = new DefaultTableModel(0,5);
        String[] cabeceros = {"Id", "Book name" , "Autor", "Price", "Stock"};
        this.tablaModelo.setColumnIdentifiers(cabeceros);
        // instanciar objeto JTable
        this.tabla = new JTable(tablaModelo);
        listarBooks();
    }

    private void listarBooks(){
        // limpiar tabla
        tablaModelo.setRowCount(0);
        // Obtener los libros
        var libros = bookService.findAllBooks();
        libros.forEach((book) -> {
            Object[] rowBook = {
                    book.getIdBook(),
                    book.getBookName(),
                    book.getAutor(),
                    book.getPrice(),
                    book.getStock()
            };
            this.tablaModelo.addRow(rowBook);
        } );
    }

    // metodos del crud
    private void addBook(){
        // Leemos los datos del formulario
        if(libroTexto.getText().equals("")){
            showMessage("The book field must be completed.");
            libroTexto.requestFocusInWindow();
            return;
        }
        var bookName = libroTexto.getText();
        var autor = autorTexto.getText();
        var price = Double.parseDouble(precioTexto.getText());
        var stock = Integer.parseInt(stockTexto.getText());
        // crear objeto libro
        var book = new Book(null, bookName, autor, price, stock);
        // guardamos el libro
        this.bookService.saveBook(book);
        showMessage("The book was added correctly!");
        // reiniciamos el formulario
        cleanForm();
        // actualizamos la tabla
        listarBooks();
    }

    private void loadBookSelected(){
        // column indexes start on 0
        var renglon = tabla.getSelectedRow();
        if(renglon != -1){
            String idBook = tabla.getModel().getValueAt(renglon, 0).toString();
            idBookTexto.setText(idBook);
            String bookName= tabla.getModel().getValueAt(renglon, 1).toString();
            libroTexto.setText(bookName);
            String autor= tabla.getModel().getValueAt(renglon, 2).toString();
            autorTexto.setText(autor);
            String price= tabla.getModel().getValueAt(renglon, 3).toString();
            precioTexto.setText(price);
            String stock= tabla.getModel().getValueAt(renglon, 4).toString();
            stockTexto.setText(stock);
        }
    }

    private void editBook(){
        // Leemos los datos del formulario
        if(this.idBookTexto.getText().equals("")){
            showMessage("Please select a book.");
        }else{
            if(libroTexto.getText().equals("")){
                showMessage("The book field must be completed.");
                libroTexto.requestFocusInWindow();
                return;
            }
        }
        // Llenamos el objeto libro a actualizar
        int idBook = Integer.parseInt(idBookTexto.getText());
        var bookName = libroTexto.getText();
        var autor = autorTexto.getText();
        var price = Double.parseDouble(precioTexto.getText());
        var stock = Integer.parseInt(stockTexto.getText());

        var book = new Book(idBook, bookName, autor, price, stock);
        bookService.saveBook(book);
        showMessage("The book information was successfully updated.");
        cleanForm();
        listarBooks();
    }

    private void deleteBook(){
        var renglon = tabla.getSelectedRow();
        if(renglon != -1){
            String idBook =
                    tabla.getModel().getValueAt(renglon, 0).toString();
            var book = new Book();
            book.setIdBook(Integer.parseInt(idBook));
            bookService.deleteBook(book);
            showMessage("The book " + idBook + " information was successfully deleted");
            cleanForm();
            listarBooks();
        }
        else{
            showMessage("Please select a book....");
        }
    }


    private void showMessage(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    private void cleanForm(){
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        stockTexto.setText("");

    }
}

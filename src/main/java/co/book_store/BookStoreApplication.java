package co.book_store;

import co.book_store.vista.LibroForm;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class BookStoreApplication {

	public static void main(String[] args) {
		// corremos la aplicacion NO web
		ConfigurableApplicationContext contextoSpring =
				new SpringApplicationBuilder(BookStoreApplication.class)
						.headless(false)
						.web(WebApplicationType.NONE)
						.run(args);
		// ejecutamos el codigo que carga el formulario
		EventQueue.invokeLater(() -> {
			LibroForm libroFomr = contextoSpring.getBean(LibroForm.class);
			libroFomr.setVisible(true);

		});
	}


}

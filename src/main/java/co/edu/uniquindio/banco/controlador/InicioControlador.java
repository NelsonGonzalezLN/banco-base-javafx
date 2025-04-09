package co.edu.uniquindio.banco.controlador;

import co.edu.uniquindio.banco.modelo.entidades.Banco;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase que representa el controlador de la vista Inicio
 * @author caflorezvi
 */
public class InicioControlador {
    private final Banco banco = Banco.getInstancia();

    /**
     * Método que permite ir a la vista de Iniciar Sesión
     * @param actionEvent Evento que representa el clic del botón
     */
    public void irIniciarSesion(ActionEvent actionEvent) {
        navegarVentana("/login.fxml", "Banco - Iniciar Sesión");
    }

    /**
     * Método que permite ir a la vista de Registro de Cliente
     * @param actionEvent Evento que representa el clic del botón
     */
    public void irRegistroCliente(ActionEvent actionEvent) {
        navegarVentana("/registro.fxml", "Banco - Registro de Cliente");
    }

    /**
     * Método que permite ir a la venana indicada por el nombre del archivo FXML
     * @param nombreArchivoFxml Nombre del archivo FXML
     * @param tituloVentana Título de la ventana
     */
    public void navegarVentana(String nombreArchivoFxml, String tituloVentana) {
        try {

            // Cargar la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombreArchivoFxml));
            Parent root = loader.load();

            // Crear la escena
            Scene scene = new Scene(root);

            // Crear un nuevo escenario (ventana)
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(tituloVentana);

            // Mostrar la nueva ventana
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

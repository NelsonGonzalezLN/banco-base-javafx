package co.edu.uniquindio.banco.controlador;

import co.edu.uniquindio.banco.modelo.entidades.Banco;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Clase que representa el controlador de la ventana de registro de usuario
 * @author caflorezvi
 */
public class RegistroControlador {

    @FXML
    private TextField txtIdentificacion;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtDireccion;
    @FXML
    private PasswordField txtPassword;

    private final Banco banco = Banco.getInstancia();

    /**
     * Método que se ejecuta al presionar el botón de registrarse
     * @param actionEvent evento de acción
     */
    public void registrarse(ActionEvent actionEvent) {

        try {
            // Se intenta agregar el usuario al banco
            banco.registrarUsuario(
                    txtIdentificacion.getText(),
                    txtNombre.getText(),
                    txtDireccion.getText(),
                    txtCorreo.getText(),
                    txtPassword.getText() );

            // Se muestra un mensaje de éxito y se cierra la ventana
            crearAlerta("Usuario registrado correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();

        }catch (Exception e){
            crearAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    /**
     * Método que se encarga de mostrar una alerta en pantalla
     * @param mensaje mensaje a mostrar
     * @param tipo tipo de alerta
     */
    public void crearAlerta(String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setTitle("Alerta");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Método que se encarga de cerrar la ventana actual
     */
    public void cerrarVentana(){
        Stage stage = (Stage) txtIdentificacion.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}

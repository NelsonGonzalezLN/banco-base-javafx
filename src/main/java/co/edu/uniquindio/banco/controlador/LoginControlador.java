package co.edu.uniquindio.banco.controlador;

import co.edu.uniquindio.banco.modelo.entidades.Banco;
import co.edu.uniquindio.banco.modelo.entidades.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Clase que representa el controlador de la vista de login
 * @author caflorezvi
 */
public class LoginControlador {

    @FXML
    private Button btnIniciarSecion;

    @FXML
    private PasswordField pswContrasena;

    @FXML
    private TextField txtID;

    private final Banco banco = Banco.getInstancia();
    private Usuario usuario;

    public void actIniciarSecion(ActionEvent actionEvent) {
        try {
            if (txtID.getText().isEmpty()) throw new Exception("No se ha ingresado un ID");
            Usuario usuario = banco.buscarUsuario(txtID.getText());
            if(usuario == null) throw  new Exception("No hay un usuario registrado con ese ID");
            if (pswContrasena.getText().isEmpty()) throw new Exception("No se ha ingresado una contraseña");
            if (!usuario.getPassword().equals(pswContrasena.getText())) throw  new Exception("El usuario y la contraseña no coinciden");
            this.usuario = usuario;
            navegarVentana("/panelCliente.fxml","Banco - Panel de usuario");
            cerrarVentana();
        } catch (Exception e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    public void navegarVentana(String nombreArchivoFxml, String tituloVentana) {
        try {

            // Cargar la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombreArchivoFxml));
            Parent root = loader.load();
            PanelClienteControlador controller = loader.getController();
            controller.inicializarValores(usuario);

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

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    public void cerrarVentana(){
        Stage stage = (Stage) txtID.getScene().getWindow();
        stage.close();
    }
}

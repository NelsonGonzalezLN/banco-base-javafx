package co.edu.uniquindio.banco.controlador;

import co.edu.uniquindio.banco.modelo.entidades.Banco;
import co.edu.uniquindio.banco.modelo.entidades.BilleteraVirtual;
import co.edu.uniquindio.banco.modelo.entidades.Usuario;
import co.edu.uniquindio.banco.modelo.enums.Categoria;
import co.edu.uniquindio.banco.config.Constantes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Clase que se encarga de controlar la creación de transferencias entre billeteras
 * @author caflorezvi
 */
public class TransferenciaControlador implements Initializable {

    @FXML
    private Button btnTransferir;

    @FXML
    private ComboBox<Categoria> cmbCategoria;

    @FXML
    private TextField txtMontoTransferir;

    @FXML
    private TextField txtNumCuenta;

    private final Banco banco = Banco.getInstancia();
    private Usuario usuario;
    private BilleteraVirtual billetera;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbCategoria.getItems().setAll(Categoria.values());
    }


    public void transferir(ActionEvent actionEvent) {
        Float monto = 0.0F;
        try {
            if (txtNumCuenta.getText().isEmpty()) throw new Exception("No se ha ingresado un numero de cuenta");
            if (billetera.getNumero().equals(txtNumCuenta.getText())) throw new Exception("No puedes transferir dinero a tu propia cuenta");
            if (cmbCategoria.getValue() == null) throw new Exception("No se ha seleccionado una categoria");
            if(!banco.comprobarExistenciaUsuario(txtNumCuenta.getText()))throw new Exception("No hay un usuario con dicho numero de billetera");
            if (txtMontoTransferir.getText().isEmpty()) throw new Exception("No se ha ingresado un monto");
            float numero = Float.parseFloat(txtMontoTransferir.getText());
            monto = numero;
            float total = Constantes.COMISION + monto;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Confirmación");
            alert.setContentText("Monto a transferir: $" + monto
                    +"\nComición: $" + Constantes.COMISION
                    +"\nCosto total: $" + total
                    +"\nSe le transferira a: " + banco.buscarBilletera(txtNumCuenta.getText()).getUsuario().getNombre()
                    +"\n\n¿Esta seguro de querer realizar la transferencia?");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                try {
                    banco.realizarTransferencia(billetera.getNumero(), txtNumCuenta.getText(), monto, cmbCategoria.getValue());
                    cerrarVentana();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    public void inicializarValores(Usuario usuario) {
        this.usuario = usuario;
        billetera = banco.buscarBilleteraUsuario(usuario.getId());
    }

    public void cerrarVentana(){
        Stage stage = (Stage) txtNumCuenta.getScene().getWindow();
        stage.close();
    }
}

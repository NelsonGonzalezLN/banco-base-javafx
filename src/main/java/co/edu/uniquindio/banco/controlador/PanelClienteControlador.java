package co.edu.uniquindio.banco.controlador;

import co.edu.uniquindio.banco.modelo.entidades.Banco;
import co.edu.uniquindio.banco.modelo.entidades.BilleteraVirtual;
import co.edu.uniquindio.banco.modelo.entidades.Transaccion;
import co.edu.uniquindio.banco.modelo.entidades.Usuario;
import co.edu.uniquindio.banco.modelo.enums.Categoria;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clase que se encarga de gestionar las acciones de la interfaz gráfica del panel del cliente.
 * @author caflorezvi
 */
public class PanelClienteControlador implements Initializable {

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnConsultar;

    @FXML
    private Button btnTransferir;

    @FXML
    private TableColumn<Transaccion, String> clmCategoria;

    @FXML
    private TableColumn<Transaccion, String> clmFecha;

    @FXML
    private TableColumn<Transaccion, String> clmTipo;

    @FXML
    private TableColumn<Transaccion, String> clmUsuario;

    @FXML
    private TableColumn<Transaccion, String> clmValor;

    @FXML
    private Label lblCodigo;

    @FXML
    private Label lblInfo;

    @FXML
    private TableView<Transaccion> tblTransacciones;

    private final Banco banco = Banco.getInstancia();
    private Usuario usuario;
    private BilleteraVirtual billetera;
    private ObservableList<Transaccion> transaccionesObserbables;


    @FXML
    void actCerrarSesion(ActionEvent event) {
        cerrarVentana();
        navegarVentana("/login.fxml","Banco - Panel de usuario");
    }

    @FXML
    void actConsultar(ActionEvent event) {
        mostrarAlerta("Su saldo actual es de: " + billetera.consultarSaldo(), Alert.AlertType.INFORMATION);
    }

    @FXML
    void actTransferir(ActionEvent event) {
        navegarVentana("/transferencia.fxml","Banco - Panel de usuario");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clmTipo.setCellValueFactory(celldata -> new SimpleStringProperty(encontrarTipo(celldata.getValue())));
        clmFecha.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getFecha().toString()));
        clmCategoria.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getTipo().toString()));
        clmUsuario.setCellValueFactory(celldata -> new SimpleStringProperty(encontrarFuenteTipo(celldata.getValue())));
        clmValor.setCellValueFactory(celldata -> new SimpleStringProperty(String.valueOf(celldata.getValue().getMonto())));

    }

    public void inicializarValores(Usuario usuario){
        try {
            if(usuario != null){
                this.usuario = usuario;
                lblInfo.setText(usuario.getNombre()+" bienvenido a su banco, aquí podra ver sus transacciones");
                lblCodigo.setText("Numero de Cuenta: " + banco.buscarBilleteraUsuario(usuario.getId()).getNumero());
                billetera = banco.buscarBilleteraUsuario(usuario.getId());

                transaccionesObserbables = FXCollections.observableArrayList();
                actualizarTabla();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String encontrarTipo(Transaccion transaccion){
        if (transaccion.getTipo().toString().equals(Categoria.RECARGA.toString()))return "Deposito";
        if (billetera.getNumero().equals(transaccion.getBilleteraOrigen().getNumero())) return "Retiro";
        else return "Deposito";

    }

    private String encontrarFuenteTipo(Transaccion transaccion){
        if (billetera.getNumero().equals(transaccion.getBilleteraOrigen().getNumero())) return transaccion.getBilleteraDestino().getUsuario().getNombre();
        else return transaccion.getBilleteraOrigen().getUsuario().getNombre();
    }

    public void actualizarTabla() {
        transaccionesObserbables.setAll(banco.obtenerTransacciones(billetera.getNumero()));
        tblTransacciones.setItems(transaccionesObserbables);
    }

    public void navegarVentana(String nombreArchivoFxml, String tituloVentana) {
        try {

            // Cargar la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombreArchivoFxml));
            Parent root = loader.load();
            if (nombreArchivoFxml.equals("/transferencia.fxml")) {
                TransferenciaControlador controller = loader.getController();
                controller.inicializarValores(usuario);
            }


            // Crear la escena
            Scene scene = new Scene(root);

            // Crear un nuevo escenario (ventana)
            Stage stage = new Stage();
            stage.setOnHiding(event -> actualizarTabla());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(tituloVentana);

            // Mostrar la nueva ventana
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cerrarVentana(){
        Stage stage = (Stage) tblTransacciones.getScene().getWindow();
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
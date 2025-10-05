package com.empresa.compras.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainController {

    @FXML private Button btnUsuarios;
    @FXML private Button btnOrdenes;
    @FXML private Button btnPagos;
    @FXML private Button btnRecepciones;
    @FXML private Button btnInventarios;
    @FXML private Button btnLogout;

    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {
        // Acciones de los botones
        btnUsuarios.setOnAction(e -> mostrarVista("usuarios"));
        btnOrdenes.setOnAction(e -> mostrarVista("ordenes"));
        // btnPagos.setOnAction(e -> mostrarVista("pagos"));
        // btnRecepciones.setOnAction(e -> mostrarVista("recepciones"));
        // btnInventarios.setOnAction(e -> mostrarVista("inventarios"));

        // ✅ Acción del botón de cerrar sesión
        btnLogout.setOnAction(e -> cerrarSesion());
    }

    private void mostrarVista(String nombreVista) {
        try {
            contentArea.getChildren().clear();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + nombreVista + ".fxml"));
            contentArea.getChildren().add(loader.load());

        } catch (Exception e) {
            e.printStackTrace();
            Label error = new Label("⚠ Error al cargar la vista: " + nombreVista);
            error.setStyle("-fx-text-fill: red;");
            contentArea.getChildren().add(error);
        }
    }

    // ✅ Método corregido para cerrar sesión correctamente
    private void cerrarSesion() {
        try {
            // Limpia los datos del usuario logueado
            ComprasApp.usuarioLogueadoId = null;
            ComprasApp.usuarioLogueadoNombre = null;
            ComprasApp.usuarioLogueadoRol = null;

            // Carga la escena del login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Obtiene la ventana actual
            Stage stage = (Stage) btnLogout.getScene().getWindow();

            // Cambia la escena
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Compras - Login");
            stage.show();

            System.out.println("✅ Sesión cerrada. Volviendo al login...");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error al cerrar sesión y volver al login.");
        }
    }
}

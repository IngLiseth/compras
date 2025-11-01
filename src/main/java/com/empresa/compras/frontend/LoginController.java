package com.empresa.compras.frontend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblMensaje;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public void login() {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        String json = String.format("{\"username\":\"%s\",\"passwordHash\":\"%s\"}", usuario, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Apiconfig.BASE_URL + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parseamos la respuesta y guardamos la sesión en ComprasApp
                Map<String, Object> data = mapper.readValue(response.body(), new TypeReference<>() {});
                Long idUsuario = ((Number) data.get("idUsuario")).longValue();
                String username = (String) data.get("username");
                String rol = (String) data.get("rol");

                ComprasApp.usuarioLogueadoId = idUsuario;
                ComprasApp.usuarioLogueadoNombre = username;
                ComprasApp.usuarioLogueadoRol = rol;

                lblMensaje.setText("✅ Login exitoso!");
                lblMensaje.setStyle("-fx-text-fill: green;");
                System.out.println("Respuesta del servidor: " + response.body());

                abrirMenuPrincipal();
            } else {
                lblMensaje.setText("❌ Credenciales inválidas (" + response.statusCode() + ")");
                lblMensaje.setStyle("-fx-text-fill: red;");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lblMensaje.setText("⚠ No se pudo contactar al servidor");
        }
    }

    private void abrirMenuPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Sistema de Compras - Menú Principal");
            Scene scene = new Scene(root, 1200, 700);
            stage.setScene(scene);

            // 🔹 Si quieres que se abra en modo maximizado:
            stage.setMaximized(true);
            stage.show();

            // cerrar la ventana de login
            ((Stage) txtUsuario.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("⚠ No se pudo cargar la vista principal (main.fxml)");
        }
    }
}

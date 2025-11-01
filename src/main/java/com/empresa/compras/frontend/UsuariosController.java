package com.empresa.compras.frontend;

import com.empresa.compras.dtos.RolResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class UsuariosController {

    @FXML private TextField txtNombre, txtEmail, txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbRol;
    @FXML private Label lblMensaje;

    @FXML private TableView<UsuarioResponseDTO> tablaUsuarios;
    @FXML private TableColumn<UsuarioResponseDTO, Long> colId;
    @FXML private TableColumn<UsuarioResponseDTO, String> colNombre, colEmail, colUsername, colRol;
    @FXML private TableColumn<UsuarioResponseDTO, Boolean> colActivo;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // Guardar usuario seleccionado
    private UsuarioResponseDTO usuarioSeleccionado;

    @FXML
    public void initialize() {
        // Configuración de columnas
        cargarRoles();
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("nombreRol"));

        cargarUsuarios();

        // Esto luego debería venir desde el backend
        cargarRoles();

        // Escuchar selección en la tabla
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                usuarioSeleccionado = newSel;
                llenarCampos(newSel);
            }
        });
    }

    private void cargarUsuarios() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/usuarios"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<UsuarioResponseDTO> usuarios = mapper.readValue(response.body(),
                        new TypeReference<>() {});
                tablaUsuarios.setItems(FXCollections.observableArrayList(usuarios));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void llenarCampos(UsuarioResponseDTO usuario) {
        txtNombre.setText(usuario.getNombre());
        txtEmail.setText(usuario.getEmail());
        txtUsername.setText(usuario.getUsername());
        txtPassword.clear(); // no mostramos el hash, pedimos nueva si quiere
        cmbRol.setValue(usuario.getNombreRol());
    }

    @FXML
    private void crearUsuario() {
        try {
            String json = """
                {
                  "nombre": "%s",
                  "email": "%s",
                  "username": "%s",
                  "passwordHash": "%s",
                  "activo": true,
                  "idRol": 1
                }
                """.formatted(txtNombre.getText(), txtEmail.getText(), txtUsername.getText(), txtPassword.getText());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/usuarios"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201) {
                lblMensaje.setText("✅ Usuario registrado con éxito");
                cargarUsuarios();
                limpiarCampos();
            } else {
                lblMensaje.setText("❌ Error al registrar usuario");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void actualizarUsuario() {
        if (usuarioSeleccionado == null) {
            lblMensaje.setText("⚠ Selecciona un usuario para actualizar");
            return;
        }

        try {
            String json = """
                {
                  "nombre": "%s",
                  "email": "%s",
                  "username": "%s",
                  "passwordHash": "%s",
                  "activo": true,
                  "idRol": 1
                }
                """.formatted(txtNombre.getText(), txtEmail.getText(), txtUsername.getText(),
                    txtPassword.getText().isEmpty() ? usuarioSeleccionado.getUsername() : txtPassword.getText());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/usuarios/" + usuarioSeleccionado.getIdUsuario()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                lblMensaje.setText("✅ Usuario actualizado con éxito");
                cargarUsuarios();
                limpiarCampos();
            } else {
                lblMensaje.setText("❌ Error al actualizar usuario");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("⚠ Error de conexión al actualizar");
        }
    }

    @FXML
    private void eliminarUsuario() {
        UsuarioResponseDTO usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario == null) {
            lblMensaje.setText("⚠ Selecciona un usuario para eliminar");
            return;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/usuarios/" + usuario.getIdUsuario()))

                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 204) {
                lblMensaje.setText("🗑 Usuario eliminado");
                cargarUsuarios();
                limpiarCampos();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void limpiarCampos() {
        txtNombre.clear();
        txtEmail.clear();
        txtUsername.clear();
        txtPassword.clear();
        cmbRol.getSelectionModel().clearSelection();
        usuarioSeleccionado = null;
    }
    @FXML
    private void volverAlMenu() {
        try {
            // Obtener la ventana actual
            javafx.stage.Stage stage = (javafx.stage.Stage) txtNombre.getScene().getWindow();

            // Cargar el menú principal
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/main.fxml") // 👈 usa el nombre real de tu FXML
            );

            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Sistema de Compras - Menú Principal");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("⚠ Error al volver al menú");
        }
    }
    private void cargarRoles() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/roles"))
                    .GET()
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                List<RolResponseDTO> roles = mapper.readValue(res.body(), new TypeReference<>() {});
                cmbRol.setItems(FXCollections.observableArrayList(
                        roles.stream().map(r -> r.getIdRol() + " - " + r.getNombreRol()).toList()
                ));
            } else {
                lblMensaje.setText("⚠ Error al cargar roles: " + res.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("⚠ No se pudieron cargar los roles");
        }
    }


}

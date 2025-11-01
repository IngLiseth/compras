package com.empresa.compras.frontend;

import com.empresa.compras.dtos.EstadoOrdenResponseDTO;
import com.empresa.compras.dtos.InsumoResponseDTO;
import com.empresa.compras.dtos.ProveedorResponseDTO;
import com.empresa.compras.dtos.UsuarioResponseDTO;
import com.empresa.compras.frontend.fxmodels.DetalleFX;
import com.empresa.compras.frontend.fxmodels.OrdenFX;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import com.empresa.compras.frontend.Apiconfig;

public class OrdenesController {

    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cmbUsuario, cmbProveedor, cmbEstado, cmbInsumo;
    @FXML private TextField txtObservaciones, txtCantidad, txtPrecio;
    @FXML private TableView<DetalleFX> tablaDetalles;
    @FXML private TableView<OrdenFX> tablaOrdenes;
    @FXML private TableColumn<DetalleFX, String> colInsumo;
    @FXML private TableColumn<DetalleFX, Integer> colCantidad;
    @FXML private TableColumn<DetalleFX, Double> colPrecio, colSubtotal;
    @FXML private TableColumn<OrdenFX, Long> colIdOrden;
    @FXML private TableColumn<OrdenFX, String> colUsuario, colProveedor, colEstado, colObs;
    @FXML private TableColumn<OrdenFX, Double> colTotal;
    @FXML private Label lblMensaje;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private final List<DetalleFX> detallesTemp = new ArrayList<>();
    private Long ordenSeleccionadaId = null;

    @FXML
    public void initialize() {
        // columnas de detalles
        colInsumo.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getNombreInsumo()));
        colCantidad.setCellValueFactory(d -> d.getValue().cantidadProperty().asObject());
        colPrecio.setCellValueFactory(d -> d.getValue().precioUnitarioProperty().asObject());
        colSubtotal.setCellValueFactory(d -> d.getValue().subtotalProperty().asObject());

        // columnas de órdenes
        colIdOrden.setCellValueFactory(d -> d.getValue().idOrdenProperty().asObject());
        colUsuario.setCellValueFactory(d -> d.getValue().usuarioProperty());
        colProveedor.setCellValueFactory(d -> d.getValue().proveedorProperty());
        colEstado.setCellValueFactory(d -> d.getValue().estadoProperty());
        colObs.setCellValueFactory(d -> d.getValue().observacionesProperty());
        colTotal.setCellValueFactory(d -> d.getValue().totalProperty().asObject());

        tablaOrdenes.getSelectionModel().selectedItemProperty().addListener((obs, old, nueva) -> {
            if (nueva != null) cargarOrdenEnFormulario(nueva.getIdOrden());
        });

        //cargarUsuarios();
        cargarProveedores();
        cargarEstados();
        cargarInsumos();
        cargarOrdenes();
        if (ComprasApp.usuarioLogueadoNombre != null) {
            cmbUsuario.setValue(ComprasApp.usuarioLogueadoId + " - " + ComprasApp.usuarioLogueadoNombre);
            cmbUsuario.setDisable(true); // desactiva el combo para que no se pueda cambiar
        }
    }

    @FXML
    private void agregarDetalle() {
        try {
            String insumoSel = cmbInsumo.getValue();
            if (insumoSel == null || insumoSel.isBlank()) {
                lblMensaje.setText("⚠ Selecciona un insumo");
                return;
            }
            if (txtCantidad.getText().isBlank() || txtPrecio.getText().isBlank()) {
                lblMensaje.setText("⚠ Ingresa cantidad y precio");
                return;
            }
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            DetalleFX det = new DetalleFX(
                    Long.parseLong(insumoSel.split(" - ")[0].trim()),           // idInsumo
                    insumoSel.split(" - ")[1].trim(),                           // nombreInsumo
                    cantidad, precio, cantidad * precio
            );


            detallesTemp.add(det);
            tablaDetalles.setItems(FXCollections.observableArrayList(detallesTemp));
            tablaDetalles.refresh();

            txtCantidad.clear();
            txtPrecio.clear();
            lblMensaje.setText("✅ Detalle agregado");
        } catch (NumberFormatException nfe) {
            lblMensaje.setText("⚠ Cantidad y precio deben ser numéricos");
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("⚠ Error al agregar detalle");
        }
    }


    @FXML
    private void crearOrden() {

        enviarOrden("POST", Apiconfig.BASE_URL + "/ordenes");
    }

    @FXML
    private void actualizarOrden() {
        if (ordenSeleccionadaId == null) {
            lblMensaje.setText("⚠ Selecciona una orden para actualizar");
            return;
        }
        enviarOrden("PUT", Apiconfig.BASE_URL + "/ordenes/" + ordenSeleccionadaId);
    }

    @FXML
    private void eliminarOrden() {
        if (ordenSeleccionadaId == null) {
            lblMensaje.setText("⚠ Selecciona una orden para eliminar");
            return;
        }
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/ordenes/" + ordenSeleccionadaId))
                    .DELETE().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 204) {
                lblMensaje.setText("🗑 Orden eliminada");
                cargarOrdenes();
                limpiarCampos();
            } else {
                lblMensaje.setText("❌ No se pudo eliminar (HTTP " + res.statusCode() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("⚠ Error al eliminar");
        }
    }

    private void enviarOrden(String metodo, String url) {
        try {
            String idUsuario = String.valueOf(ComprasApp.usuarioLogueadoId);
            String idProveedor = extraerId(cmbProveedor);
            String idEstado = extraerId(cmbEstado);

            if (idUsuario == null || idProveedor == null || idEstado == null) {
                lblMensaje.setText("⚠ Usuario, proveedor y estado son obligatorios");
                return;
            }
            if (detallesTemp.isEmpty()) {
                lblMensaje.setText("⚠ Debe agregar al menos un detalle");
                return;
            }

            Locale prev = Locale.getDefault();
            Locale.setDefault(Locale.US);
            StringBuilder detallesJson = new StringBuilder("[");
            for (int i = 0; i < detallesTemp.size(); i++) {
                DetalleFX d = detallesTemp.get(i);

                // aseguramos que idInsumo no es nulo
                Long idInsumo = d.getIdInsumo() != null ? d.getIdInsumo() : 0L;

                detallesJson.append(String.format(
                        "{\"idInsumo\":%d,\"cantidad\":%d,\"precioUnitario\":%.2f}",
                        idInsumo, d.getCantidad(), d.getPrecioUnitario()
                ));

                if (i < detallesTemp.size() - 1) detallesJson.append(",");
            }
            detallesJson.append("]");

            Locale.setDefault(prev);

            String json = String.format(
                    "{ \"fecha\":\"%s\", \"observaciones\":\"%s\", \"idUsuario\":%s, \"idProveedor\":%s, \"idEstado\":%s, \"detalles\":%s }",
                    (dpFecha.getValue() != null ? dpFecha.getValue() : LocalDate.now()),
                    (txtObservaciones.getText() == null ? "" : txtObservaciones.getText()),
                    idUsuario, idProveedor, idEstado, detallesJson.toString()
            );

            HttpRequest.Builder b = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json");

            if ("POST".equals(metodo)) {
                b.POST(HttpRequest.BodyPublishers.ofString(json));
            } else {
                // 🔧 Adjuntamos idOrden al cuerpo del PUT correctamente
                String jsonConId = json.substring(0, json.length() - 1)
                        + String.format(", \"idOrden\": %d }", ordenSeleccionadaId);
                b.PUT(HttpRequest.BodyPublishers.ofString(jsonConId));
            }



            HttpResponse<String> res = client.send(b.build(), HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() == 201 || res.statusCode() == 200) {
                lblMensaje.setText("✅ Orden guardada");
                cargarOrdenes();
                limpiarCampos();
            } else {
                lblMensaje.setText("❌ Error al guardar orden: " + res.statusCode());
                System.out.println("⚠ JSON enviado: " + json);
                System.out.println("⚠ Respuesta backend: " + res.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("⚠ Error al procesar la orden");
        }
    }

    private void cargarOrdenes() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/ordenes"))
                    .GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                List<OrdenFX> ordenes = mapper.readValue(res.body(), new TypeReference<>() {});
                tablaOrdenes.setItems(FXCollections.observableArrayList(ordenes));
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("⚠ Error al cargar órdenes");
        }
    }

    private void cargarOrdenEnFormulario(Long idOrden) {
        try {
            ordenSeleccionadaId = idOrden;
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/ordenes/" + idOrden))
                    .GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) {
                lblMensaje.setText("⚠ No se pudo cargar la orden seleccionada");
                return;
            }

            OrdenFX o = mapper.readValue(res.body(), OrdenFX.class);
            dpFecha.setValue(LocalDate.now());
            txtObservaciones.setText(o.getObservaciones() == null ? "" : o.getObservaciones());
            seleccionarOpcionPorNombre(cmbUsuario, o.getUsuario());
            seleccionarOpcionPorNombre(cmbProveedor, o.getProveedor());
            seleccionarOpcionPorNombre(cmbEstado, o.getEstado());

            detallesTemp.clear();
            if (o.getDetalles() != null) {
                for (DetalleFX d : o.getDetalles()) {
                    detallesTemp.add(new DetalleFX(
                            d.getIdInsumo(),
                            d.getNombreInsumo(),
                            d.getCantidad(),
                            d.getPrecioUnitario(),
                            d.getSubtotal()
                    ));
                }


            }
            tablaDetalles.setItems(FXCollections.observableArrayList(detallesTemp));
            tablaDetalles.refresh();
            lblMensaje.setText("ℹ Orden cargada");
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("⚠ Error al cargar la orden");
        }
    }

    @FXML
    private void limpiarCampos() {
        dpFecha.setValue(null);
        txtObservaciones.clear();
        cmbUsuario.getSelectionModel().clearSelection();
        cmbProveedor.getSelectionModel().clearSelection();
        cmbEstado.getSelectionModel().clearSelection();
        cmbInsumo.getSelectionModel().clearSelection();
        detallesTemp.clear();
        tablaDetalles.getItems().clear();
        ordenSeleccionadaId = null;
    }

    @FXML
    private void volverAlMenu() {
        try {
            var stage = (javafx.stage.Stage) dpFecha.getScene().getWindow();
            var loader = new javafx.fxml.FXMLLoader(getClass().getResource("/main.fxml"));
            var scene = new javafx.scene.Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Sistema de Compras - Menú Principal");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarUsuarios() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/usuarios"))
                    .GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                List<UsuarioResponseDTO> usuarios = mapper.readValue(res.body(), new TypeReference<>() {});
                cmbUsuario.setItems(FXCollections.observableArrayList(
                        usuarios.stream().map(u -> u.getIdUsuario() + " - " + u.getNombre()).toList()
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void cargarProveedores() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/proveedores"))
                    .GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                List<ProveedorResponseDTO> proveedores = mapper.readValue(res.body(), new TypeReference<>() {});
                cmbProveedor.setItems(FXCollections.observableArrayList(
                        proveedores.stream().map(p -> p.getIdProveedor() + " - " + p.getNombreProveedor()).toList()
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void cargarEstados() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/estados-orden"))
                    .GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                List<EstadoOrdenResponseDTO> estados = mapper.readValue(res.body(), new TypeReference<>() {});
                cmbEstado.setItems(FXCollections.observableArrayList(
                        estados.stream().map(e -> e.getIdEstado() + " - " + e.getNombreEstado()).toList()
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void cargarInsumos() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/insumos"))
                    .GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                List<InsumoResponseDTO> insumos = mapper.readValue(res.body(), new TypeReference<>() {});
                cmbInsumo.setItems(FXCollections.observableArrayList(
                        insumos.stream().map(i -> i.getIdInsumo() + " - " + i.getNombreInsumo()).toList()
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private String extraerId(ComboBox<String> combo) {
        String v = combo.getValue();
        if (v == null) return null;
        return v.split(" - ")[0].trim();
    }

    private void seleccionarOpcionPorNombre(ComboBox<String> combo, String nombre) {
        if (nombre == null) { combo.getSelectionModel().clearSelection(); return; }
        Optional<String> match = combo.getItems().stream()
                .filter(s -> s.endsWith(" - " + nombre) || s.contains(" - " + nombre))
                .findFirst();
        match.ifPresent(combo.getSelectionModel()::select);
    }
}

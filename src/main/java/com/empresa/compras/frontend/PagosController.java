package com.empresa.compras.frontend;
import com.empresa.compras.frontend.fxmodels.PagoFX;


import com.empresa.compras.frontend.fxmodels.DetallePagoFX;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PagosController {

    // ==================== FXML ====================
    @FXML private ComboBox<Long> cmbOrden;
    @FXML private ComboBox<String> cmbMetodo;
    @FXML private TextField txtMontoParcial;
    @FXML private Button btnAgregarDetalle;

    @FXML private TableView<DetallePagoFX> tblDetalles;
    @FXML private TableColumn<DetallePagoFX, String> colMetodo;
    @FXML private TableColumn<DetallePagoFX, Double> colMonto;

    @FXML private TextField txtTotal;
    @FXML private Button btnRegistrarPago;
    @FXML private Label lblMensaje;

    @FXML private TableView<PagoFX> tblPagos;
    @FXML private TableColumn<PagoFX, Number> colPagoId;
    @FXML private TableColumn<PagoFX, String> colFecha;
    @FXML private TableColumn<PagoFX, Number> colOrden;
    @FXML private TableColumn<PagoFX, Number> colTotal;

    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnVerDetalles;
    @FXML private Button btnLimpiar;
    @FXML private Button btnAtras;

    // ==================== Infra ====================
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    //private static final String BASE = "http://localhost:8080/api";

    // Detalles que se muestran/arman en la tabla superior
    private final ObservableList<DetallePagoFX> detalles = FXCollections.observableArrayList();

    // Cache: nombre del método -> id
    private final Map<String, Long> metodosNombreAId = new HashMap<>();

    // Estado
    private Long pagoSeleccionadoId = null;
    private Long detalleSeleccionadoId = null;

    // ==================== Init ====================
    @FXML
    public void initialize() {
        // columnas tabla detalles (usar properties de DetallePagoFX)
        colMetodo.setCellValueFactory(d -> d.getValue().metodoPagoProperty());
        colMonto.setCellValueFactory(d -> d.getValue().montoParcialProperty().asObject());
        tblDetalles.setItems(detalles);

        // columnas tabla pagos (usar properties de PagoFX)
        colPagoId.setCellValueFactory(p -> p.getValue().idPagoProperty());
        colFecha.setCellValueFactory(p -> p.getValue().fechaProperty());
        colOrden.setCellValueFactory(p -> p.getValue().idOrdenProperty());
        colTotal.setCellValueFactory(p -> p.getValue().montoTotalProperty());




        txtTotal.setEditable(false);
        lblMensaje.setText("");

        // acciones
        btnAgregarDetalle.setOnAction(e -> onAgregarDetalle());
        btnRegistrarPago.setOnAction(e -> onRegistrarPago());
        btnLimpiar.setOnAction(e -> limpiarFormulario());
        btnActualizar.setOnAction(e -> onActualizarDetalle());
        btnVerDetalles.setOnAction(e -> onVerDetalles());
        btnEliminar.setOnAction(e -> onEliminarPago());
        btnAtras.setOnAction(e -> onAtras());


        // selección de detalle (arriba): rellena los campos para editar
        tblDetalles.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                detalleSeleccionadoId = newSel.getIdDetalle();
                cmbMetodo.setValue(newSel.getMetodoPago());
                txtMontoParcial.setText(String.valueOf(newSel.getMontoParcial()));
                lblMensaje.setText("Detalle seleccionado: " + detalleSeleccionadoId);
            }
        });

        // datos iniciales
        cargarOrdenes();
        cargarMetodos();
        cargarPagos();           // al seleccionar un pago se cargan sus detalles
        configurarSeleccionPago();
    }

    // ==================== Cargas iniciales ====================
    private void cargarOrdenes() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/ordenes"))
                    .GET().build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            List<Map<String, Object>> data = mapper.readValue(res.body(), new TypeReference<>() {});
            List<Long> ids = data.stream()
                    .map(m -> toLong(m.get("idOrden")))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            cmbOrden.getItems().setAll(ids);
        } catch (Exception ex) {
            lblMensaje.setText("Error cargando órdenes");
            ex.printStackTrace();
        }
    }

    private void cargarMetodos() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/metodos-pago"))
                    .GET().build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            List<Map<String, Object>> data = mapper.readValue(res.body(), new TypeReference<>() {});

            cmbMetodo.getItems().clear();
            metodosNombreAId.clear();

            for (Map<String, Object> m : data) {
                Long id = toLong(m.get("idMetodoPago"));
                String nombre = Objects.toString(m.get("nombreMetodo"), "");
                if (id != null && !nombre.isBlank()) {
                    metodosNombreAId.put(nombre, id);
                    cmbMetodo.getItems().add(nombre);
                }
            }
        } catch (Exception ex) {
            lblMensaje.setText("Error cargando métodos");
            ex.printStackTrace();
        }
    }

    private void cargarPagos() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/pagos"))
                    .GET().build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            List<Map<String, Object>> data = mapper.readValue(res.body(), new TypeReference<>() {});

            ObservableList<PagoFX> items = FXCollections.observableArrayList();
            for (Map<String, Object> p : data) {
                items.add(new PagoFX(
                        toLong(p.get("idPago")),
                        Objects.toString(p.get("fecha"), ""),
                        toLong(p.get("idOrden")),
                        toDouble(p.get("montoTotal"))
                ));
            }
            tblPagos.setItems(items);
        } catch (Exception ex) {
            lblMensaje.setText("Error cargando pagos");
            ex.printStackTrace();
        }
    }

    // ==================== Detalles (UI) ====================
    private void onAgregarDetalle() {
        lblMensaje.setText("");
        String metodo = cmbMetodo.getValue();
        String montoTxt = txtMontoParcial.getText();

        if (metodo == null || metodo.isBlank()) {
            lblMensaje.setText("Seleccione un método");
            return;
        }
        if (montoTxt == null || montoTxt.isBlank()) {
            lblMensaje.setText("Digite un monto");
            return;
        }

        try {
            double monto = Double.parseDouble(montoTxt);
            if (monto <= 0) {
                lblMensaje.setText("El monto debe ser mayor que 0");
                return;
            }
            detalles.add(new DetallePagoFX(null, metodo, monto));
            tblDetalles.refresh();
            txtMontoParcial.clear();
            recalcularTotal();
        } catch (NumberFormatException nfe) {
            lblMensaje.setText("Monto inválido");
        }
    }

    private void onActualizarDetalle() {
        if (detalleSeleccionadoId == null) {
            lblMensaje.setText("Seleccione un detalle para actualizar");
            return;
        }
        if (pagoSeleccionadoId == null) {
            lblMensaje.setText("Seleccione un pago en la tabla inferior");
            return;
        }

        String metodo = cmbMetodo.getValue();
        String montoTxt = txtMontoParcial.getText();

        if (metodo == null || metodo.isBlank() || montoTxt == null || montoTxt.isBlank()) {
            lblMensaje.setText("Complete método y monto");
            return;
        }

        Long idMetodo = metodosNombreAId.get(metodo);
        if (idMetodo == null) {
            lblMensaje.setText("Método no reconocido");
            return;
        }

        Double monto;
        try {
            monto = Double.valueOf(montoTxt);
        } catch (NumberFormatException ex) {
            lblMensaje.setText("Monto inválido");
            return;
        }

        try {
            // 🔧 Endpoint correcto del backend:
            String url = Apiconfig.BASE_URL + "/pagos-detalles/detalle/" + detalleSeleccionadoId
                    + "?montoParcial=" + monto
                    + "&idMetodoPago=" + idMetodo;

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            client.send(req, HttpResponse.BodyHandlers.ofString());

            lblMensaje.setText("✅ Detalle actualizado");

            // Recargar detalles y totales
            cargarDetallesPago(pagoSeleccionadoId);
            cargarPagos();

        } catch (Exception e) {
            lblMensaje.setText("❌ Error actualizando detalle");
            e.printStackTrace();
        }
    }

    private void recalcularTotal() {
        double total = detalles.stream()
                .mapToDouble(DetallePagoFX::getMontoParcial)
                .sum();
        txtTotal.setText(String.valueOf(total));
    }

    private void limpiarFormulario() {
        cmbOrden.getSelectionModel().clearSelection();
        cmbMetodo.getSelectionModel().clearSelection();
        txtMontoParcial.clear();
        detalles.clear();
        txtTotal.clear();
        lblMensaje.setText("");
        tblDetalles.refresh();
        detalleSeleccionadoId = null;
    }

    // ==================== Registrar (POST) ====================
    private void onRegistrarPago() {
        try {
            lblMensaje.setText("");

            Long idOrden = cmbOrden.getValue();
            if (idOrden == null) {
                lblMensaje.setText("Seleccione una orden");
                return;
            }
            if (detalles.isEmpty()) {
                lblMensaje.setText("Agregue al menos un detalle");
                return;
            }

            double total = detalles.stream().mapToDouble(DetallePagoFX::getMontoParcial).sum();

            // JSON esperado por el backend
            List<Map<String, Object>> detallesDTO = new ArrayList<>();
            for (DetallePagoFX d : detalles) {
                Long idMetodo = metodosNombreAId.get(d.getMetodoPago());
                if (idMetodo == null) {
                    lblMensaje.setText("Método no reconocido: " + d.getMetodoPago());
                    return;
                }
                Map<String, Object> det = new HashMap<>();
                det.put("idMetodoPago", idMetodo);
                det.put("montoParcial", d.getMontoParcial());
                detallesDTO.add(det);
            }

            Map<String, Object> body = new HashMap<>();
            body.put("fecha", LocalDate.now().toString());
            body.put("montoTotal", total);
            body.put("idOrden", idOrden);
            body.put("detalles", detallesDTO);

            String json = mapper.writeValueAsString(body);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/pagos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.send(req, HttpResponse.BodyHandlers.ofString());

            cargarPagos();
            limpiarFormulario();
            lblMensaje.setText("✅ Pago registrado");
        } catch (Exception ex) {
            lblMensaje.setText("❌ Error registrando pago");
            ex.printStackTrace();
        }
    }

    // ==================== Ver / Eliminar ====================
    private void onVerDetalles() {
        PagoFX sel = tblPagos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            lblMensaje.setText("Seleccione un pago");
            return;
        }
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/pagos/" + sel.getIdPago()))
                    .GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            Map<String, Object> pago = mapper.readValue(res.body(), new TypeReference<>() {});
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dets = (List<Map<String, Object>>) pago.get("detalles");

            String texto = (dets == null || dets.isEmpty())
                    ? "Sin detalles"
                    : dets.stream()
                    .map(m -> Objects.toString(m.get("metodoPago"), "")
                            + " : " + Objects.toString(m.get("montoParcial"), "0"))
                    .collect(Collectors.joining("\n"));

            Alert a = new Alert(Alert.AlertType.INFORMATION, texto, ButtonType.OK);
            a.setHeaderText("Detalles del pago " + sel.getIdPago());
            a.showAndWait();
        } catch (Exception e) {
            lblMensaje.setText("Error consultando detalles");
            e.printStackTrace();
        }
    }

    private void onEliminarPago() {
        PagoFX sel = tblPagos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            lblMensaje.setText("Seleccione un pago");
            return;
        }
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/pagos/" + sel.getIdPago()))
                    .DELETE().build();
            client.send(req, HttpResponse.BodyHandlers.ofString());
            cargarPagos();
            lblMensaje.setText("Pago eliminado");
        } catch (Exception ex) {
            lblMensaje.setText("Error eliminando pago");
            ex.printStackTrace();
        }
    }

    // ==================== Selección y carga de detalles ====================
    private void configurarSeleccionPago() {
        tblPagos.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    if (newSel != null) {
                        pagoSeleccionadoId = newSel.getIdPago();
                        detalleSeleccionadoId = null; // limpiar selección de detalle
                        cargarDetallesPago(pagoSeleccionadoId);
                        lblMensaje.setText("Pago seleccionado: " + pagoSeleccionadoId);
                    }
                });
    }

    private void cargarDetallesPago(Long idPago) {
        if (idPago == null) return; // protección
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Apiconfig.BASE_URL + "/pagos/" + idPago))
                    .GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            mapper.findAndRegisterModules(); // por si el backend envía LocalDate

            Map<String, Object> pago = mapper.readValue(response.body(), new TypeReference<>() {});
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dets = (List<Map<String, Object>>) pago.get("detalles");

            detalles.clear();
            if (dets != null) {
                for (Map<String, Object> d : dets) {
                    Long idDet = toLong(d.get("idDetalle"));
                    String metodo = Objects.toString(d.get("metodoPago"), "");
                    Double monto = toDouble(d.get("montoParcial"));
                    detalles.add(new DetallePagoFX(idDet, metodo, monto));
                }
            }
            tblDetalles.refresh();
            recalcularTotal();
            lblMensaje.setText("Detalles cargados");
        } catch (Exception e) {
            lblMensaje.setText("Error cargando detalles del pago");
            e.printStackTrace();
        }
    }


    // ==================== Util (parse seguro) ====================
    private static Long toLong(Object n) {
        if (n == null) return null;
        if (n instanceof Number) return ((Number) n).longValue();
        try { return Long.valueOf(n.toString()); } catch (Exception e) { return null; }
    }

    private static Double toDouble(Object n) {
        if (n == null) return 0.0;
        if (n instanceof Number) return ((Number) n).doubleValue();
        try { return Double.valueOf(n.toString()); } catch (Exception e) { return 0.0; }
    }
    private void onAtras() {
        try {
            var stage = (javafx.stage.Stage) txtMontoParcial.getScene().getWindow();
            var loader = new javafx.fxml.FXMLLoader(getClass().getResource("/main.fxml"));
            var scene = new javafx.scene.Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Sistema de Compras - Menú Principal");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.example.ppa.CrearEditar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppa.Controllers.ActivityController;
import com.example.ppa.Entidades.Actividad;
import com.example.ppa.R;

public class CrearEditarActividadActivity extends AppCompatActivity {

    private EditText editTextNombreActividad;
    private EditText editTextDescripcionActividad;
    private EditText editTextFechaInicioActividad;
    private EditText editTextFechaFinActividad;
    private Spinner spinnerEstadoActividad;
    private Button buttonGuardarActividad;
    private Button buttonEliminarActividad;
    private ActivityController activityController;
    private int proyectoId;
    private Integer actividadId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_editar_actividad);

        editTextNombreActividad = findViewById(R.id.editTextNombreActividad);
        editTextDescripcionActividad = findViewById(R.id.editTextDescripcionActividad);
        editTextFechaInicioActividad = findViewById(R.id.editTextFechaInicioActividad);
        editTextFechaFinActividad = findViewById(R.id.editTextFechaFinActividad);
        spinnerEstadoActividad = findViewById(R.id.spinnerEstadoActividad);
        buttonGuardarActividad = findViewById(R.id.buttonGuardarActividad);
        buttonEliminarActividad = findViewById(R.id.buttonEliminarActividad);

        activityController = new ActivityController(this);

        // Obtener el ID del proyecto
        proyectoId = getIntent().getIntExtra("proyectoId", -1);
        if (proyectoId == -1) {
            Toast.makeText(this, "Error: No se proporcionó el ID del proyecto", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configurar el Spinner de estados
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.estados_actividad, // Define esto en strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstadoActividad.setAdapter(adapter);

        // Verificar si se está editando una actividad
        if (getIntent().hasExtra("actividadId")) {
            actividadId = getIntent().getIntExtra("actividadId", -1);
            cargarDatosActividad(actividadId);
            buttonEliminarActividad.setVisibility(View.VISIBLE);
        }

        buttonGuardarActividad.setOnClickListener(v -> guardarActividad());
        buttonEliminarActividad.setOnClickListener(v -> eliminarActividad());
    }

    private void cargarDatosActividad(int id) {
        Actividad actividad = activityController.obtenerActividad(id);
        if (actividad != null) {
            editTextNombreActividad.setText(actividad.getNombre());
            editTextDescripcionActividad.setText(actividad.getDescripcion());
            editTextFechaInicioActividad.setText(actividad.getFechaInicio());
            editTextFechaFinActividad.setText(actividad.getFechaFin());
            // Seleccionar el estado en el Spinner
            String estado = actividad.getEstado();
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerEstadoActividad.getAdapter();
            if (adapter != null) {
                int position = adapter.getPosition(estado);
                spinnerEstadoActividad.setSelection(position);
            }
        } else {
            Toast.makeText(this, "Error al cargar la actividad", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void guardarActividad() {
        String nombre = editTextNombreActividad.getText().toString().trim();
        String descripcion = editTextDescripcionActividad.getText().toString().trim();
        String fechaInicio = editTextFechaInicioActividad.getText().toString().trim();
        String fechaFin = editTextFechaFinActividad.getText().toString().trim();
        String estado = spinnerEstadoActividad.getSelectedItem().toString();

        if (!nombre.isEmpty() && !fechaInicio.isEmpty() && !fechaFin.isEmpty()) {
            if (actividadId == null) {
                // Crear nueva actividad
                long resultado = activityController.crearActividad(nombre, descripcion, fechaInicio, fechaFin, estado, proyectoId);
                if (resultado > 0) {
                    Toast.makeText(this, "Actividad creada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al crear la actividad", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Editar actividad existente
                Actividad actividadActualizada = new Actividad(actividadId, nombre, descripcion, fechaInicio, fechaFin, estado, proyectoId);
                int filasAfectadas = activityController.actualizarActividad(actividadActualizada);
                if (filasAfectadas > 0) {
                    Toast.makeText(this, "Actividad actualizada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al actualizar la actividad", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Por favor, complete los campos obligatorios", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarActividad() {
        if (actividadId != null) {
            int filasAfectadas = activityController.eliminarActividad(actividadId);
            if (filasAfectadas > 0) {
                Toast.makeText(this, "Actividad eliminada", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al eliminar la actividad", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se puede eliminar una actividad que no existe", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityController.close();
    }
}
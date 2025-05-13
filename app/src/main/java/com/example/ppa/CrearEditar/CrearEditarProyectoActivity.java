package com.example.ppa.CrearEditar; // Asegúrate de que este sea el paquete correcto

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppa.Controllers.ActivityController;
import com.example.ppa.Controllers.ProjectController;
import com.example.ppa.Entidades.Actividad;
import com.example.ppa.Entidades.Proyecto;
import com.example.ppa.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CrearEditarProyectoActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextDescripcion;
    private EditText editTextFechaInicio;
    private EditText editTextFechaFin;
    private Button buttonGuardar;
    private Button buttonEliminar;
    private ProjectController projectController;
    private ActivityController activityController;
    private int usuarioId;
    private Integer proyectoId = null;
    private ListView listViewActividadesProyecto;
    private TextView textViewAvanceProyecto;
    private FloatingActionButton fabNuevaActividad;
    private List<Actividad> listaDeActividades;
    private ArrayAdapter<String> adapterActividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_editar_proyecto);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        editTextFechaInicio = findViewById(R.id.editTextFechaInicio);
        editTextFechaFin = findViewById(R.id.editTextFechaFin);
        buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonEliminar = findViewById(R.id.buttonEliminar);
        listViewActividadesProyecto = findViewById(R.id.listViewActividadesProyecto);
        textViewAvanceProyecto = findViewById(R.id.textViewAvanceProyecto);
        fabNuevaActividad = findViewById(R.id.fabNuevaActividad);

        projectController = new ProjectController(this);
        activityController = new ActivityController(this);

        usuarioId = getIntent().getIntExtra("usuarioId", -1);

        if (getIntent().hasExtra("proyectoId")) {
            proyectoId = getIntent().getIntExtra("proyectoId", -1);
            cargarDatosProyecto(proyectoId);
            cargarActividadesProyecto(proyectoId);
            buttonEliminar.setVisibility(View.VISIBLE);
        } else {
            buttonEliminar.setVisibility(View.GONE);
        }

        buttonGuardar.setOnClickListener(v -> guardarProyecto());
        buttonEliminar.setOnClickListener(v -> eliminarProyecto());

        fabNuevaActividad.setOnClickListener(v -> {
            Intent intent = new Intent(CrearEditarProyectoActivity.this, CrearEditarActividadActivity.class);
            intent.putExtra("proyectoId", proyectoId);
            startActivity(intent);
        });

        listViewActividadesProyecto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Actividad actividadSeleccionada = listaDeActividades.get(position);
                Intent intent = new Intent(CrearEditarProyectoActivity.this, CrearEditarActividadActivity.class);
                intent.putExtra("proyectoId", proyectoId);
                intent.putExtra("actividadId", actividadSeleccionada.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (proyectoId != null) {
            cargarActividadesProyecto(proyectoId);
        }
    }

    private void cargarDatosProyecto(int id) {
        Proyecto proyecto = projectController.obtenerProyecto(id);
        if (proyecto != null) {
            editTextNombre.setText(proyecto.getNombre());
            editTextDescripcion.setText(proyecto.getDescripcion());
            editTextFechaInicio.setText(proyecto.getFechaInicio());
            editTextFechaFin.setText(proyecto.getFechaFin());
        } else {
            Toast.makeText(this, "Error al cargar el proyecto", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void guardarProyecto() {
        String nombre = editTextNombre.getText().toString().trim();
        String descripcion = editTextDescripcion.getText().toString().trim();
        String fechaInicio = editTextFechaInicio.getText().toString().trim();
        String fechaFin = editTextFechaFin.getText().toString().trim();

        if (!nombre.isEmpty() && !fechaInicio.isEmpty() && !fechaFin.isEmpty()) {
            if (proyectoId == null) {
                long resultado = projectController.crearProyecto(nombre, descripcion, fechaInicio, fechaFin, usuarioId);
                if (resultado > 0) {
                    proyectoId = (int) resultado; // Obtener el ID del proyecto recién creado
                    cargarActividadesProyecto(proyectoId); // Cargar actividades del nuevo proyecto
                    Toast.makeText(this, "Proyecto creado", Toast.LENGTH_SHORT).show();
                    // No finish() aquí para mostrar la sección de actividades
                } else {
                    Toast.makeText(this, "Error al crear el proyecto", Toast.LENGTH_SHORT).show();
                }
            } else {
                Proyecto proyectoActualizado = new Proyecto(proyectoId, nombre, descripcion, fechaInicio, fechaFin, usuarioId);
                int filasAfectadas = projectController.actualizarProyecto(proyectoActualizado);
                if (filasAfectadas > 0) {
                    Toast.makeText(this, "Proyecto actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al actualizar el proyecto", Toast.LENGTH_SHORT).show();
                }
                cargarActividadesProyecto(proyectoId); // Recargar actividades después de actualizar
            }
        } else {
            Toast.makeText(this, "Por favor, complete los campos obligatorios del proyecto", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarProyecto() {
        if (proyectoId != null) {
            int filasAfectadas = projectController.eliminarProyecto(proyectoId);
            if (filasAfectadas > 0) {
                Toast.makeText(this, "Proyecto eliminado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al eliminar el proyecto", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se puede eliminar un proyecto que no existe", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarActividadesProyecto(int proyectoId) {
        listaDeActividades = activityController.listarActividadesPorProyecto(proyectoId);
        if (listaDeActividades != null) {
            String[] nombresYEstados = new String[listaDeActividades.size()];
            for (int i = 0; i < listaDeActividades.size(); i++) {
                nombresYEstados[i] = listaDeActividades.get(i).getNombre() + " (" + listaDeActividades.get(i).getEstado() + ")";
            }
            adapterActividades = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombresYEstados);
            listViewActividadesProyecto.setAdapter(adapterActividades);
            calcularYMostrarAvance(listaDeActividades);
        } else {
            String[] mensaje = {"No hay actividades para este proyecto."};
            adapterActividades = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mensaje);
            listViewActividadesProyecto.setAdapter(adapterActividades);
            textViewAvanceProyecto.setText("Avance del Proyecto: 0%");
        }
    }

    private void calcularYMostrarAvance(List<Actividad> actividades) {
        if (actividades.isEmpty()) {
            textViewAvanceProyecto.setText("Avance del Proyecto: 0%");
            return;
        }

        int totalActividades = actividades.size();
        int actividadesRealizadas = 0;
        for (Actividad actividad : actividades) {
            if (actividad.getEstado().equals("Realizado")) {
                actividadesRealizadas++;
            }
        }

        double porcentaje = (double) actividadesRealizadas / totalActividades * 100;
        textViewAvanceProyecto.setText(String.format("Avance del Proyecto: %.2f%%", porcentaje));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        projectController.close();
        activityController.close();
    }
}
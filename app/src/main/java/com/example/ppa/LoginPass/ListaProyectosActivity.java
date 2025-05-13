package com.example.ppa.LoginPass;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppa.Controllers.ProjectController;
import com.example.ppa.CrearEditar.CrearEditarProyectoActivity;
import com.example.ppa.Entidades.Proyecto;
import com.example.ppa.LoginActivity;
import com.example.ppa.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
public class ListaProyectosActivity extends AppCompatActivity {

    private TextView textViewUsuarioId;
    private ListView listViewProyectos;
    private FloatingActionButton fabNuevoProyecto;
    private ProjectController projectController;
    private int usuarioId;
    private List<Proyecto> listaDeProyectos;
    private ArrayAdapter<String> adapter; // Un ArrayAdapter simple para mostrar solo el nombre

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_proyectos);

        textViewUsuarioId = findViewById(R.id.textViewUsuarioId);
        listViewProyectos = findViewById(R.id.listViewProyectos);
        fabNuevoProyecto = findViewById(R.id.fabNuevoProyecto);

        projectController = new ProjectController(this);

        // Obtener el ID del usuario que inició sesión
        usuarioId = getIntent().getIntExtra("usuarioId", -1);
        if (usuarioId != -1) {
            textViewUsuarioId.setText("Proyectos del Usuario ID: " + usuarioId);
            cargarProyectos();
        } else {
            textViewUsuarioId.setText("Error al obtener el ID del usuario.");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Listener para el FloatingActionButton (crear nuevo proyecto)
        fabNuevoProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaProyectosActivity.this, CrearEditarProyectoActivity.class);
                intent.putExtra("usuarioId", usuarioId);
                startActivity(intent);
            }
        });

        // Listener para cuando se hace clic en un elemento de la lista (editar proyecto)
        listViewProyectos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Proyecto proyectoSeleccionado = listaDeProyectos.get(position);
                Intent intent = new Intent(ListaProyectosActivity.this, CrearEditarProyectoActivity.class);
                intent.putExtra("proyectoId", proyectoSeleccionado.getId()); // Pasar el ID del proyecto a editar
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProyectos(); // Recargar la lista cuando volvemos a esta Activity
    }

    private void cargarProyectos() {
        listaDeProyectos = projectController.listarProyectos(usuarioId);
        List<String> nombresDeProyectos = new ArrayList<>();
        for (Proyecto proyecto : listaDeProyectos) {
            nombresDeProyectos.add(proyecto.getNombre());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombresDeProyectos);
        listViewProyectos.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        projectController.close();
    }
}
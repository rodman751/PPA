package com.example.ppa.LoginPass;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppa.Controllers.ActivityController;
import com.example.ppa.Entidades.Actividad;
import com.example.ppa.LoginActivity;
import com.example.ppa.R;

import java.util.List;
public class ListaActividadesActivity extends AppCompatActivity {

    private TextView textViewUsuarioId;
    private ListView listViewActividades;
    private ActivityController activityController;
    private int usuarioId; // Aunque aquí podríamos listar todas las actividades del usuario,
    // quizás quieras filtrar por proyecto en el futuro.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividades); // Asegúrate de tener este layout

        textViewUsuarioId = findViewById(R.id.textViewUsuarioId); // Asegúrate de tener este TextView en tu layout
        listViewActividades = findViewById(R.id.listViewActividades); // Asegúrate de tener este ListView en tu layout

        activityController = new ActivityController(this);

        // Obtener el ID del usuario que inició sesión
        usuarioId = getIntent().getIntExtra("usuarioId", -1);
        if (usuarioId != -1) {
            textViewUsuarioId.setText("Actividades del Usuario ID: " + usuarioId);
            cargarActividades();
        } else {
            textViewUsuarioId.setText("Error al obtener el ID del usuario.");
            // Volver a la pantalla de login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Cerrar MainActivity
        }
    }

    private void cargarActividades() {
        // Por ahora, vamos a cargar todas las actividades sin filtrar por usuario.
        // En una implementación real, podrías querer filtrar por usuario o mostrar
        // las actividades asociadas a un proyecto específico.
        List<Actividad> actividades = activityController.listarTodasLasActividades(); // Necesitas implementar este método en ActivityController

        if (actividades != null && !actividades.isEmpty()) {
            // Aquí necesitas un adaptador para mostrar las actividades en el ListView
            // Por ahora, vamos a crear un ArrayAdapter básico para mostrar solo los nombres
            String[] nombresActividades = new String[actividades.size()];
            for (int i = 0; i < actividades.size(); i++) {
                nombresActividades[i] = actividades.get(i).getNombre() + " (" + actividades.get(i).getEstado() + ")";
            }

            android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    nombresActividades
            );
            listViewActividades.setAdapter(adapter);

            // Puedes agregar un OnItemClickListener al ListView para ver los detalles de la actividad
            // listViewActividades.setOnItemClickListener(...);

        } else {
            // Mostrar un mensaje si no hay actividades
            String[] mensaje = {"No hay actividades registradas."};
            android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    mensaje
            );
            listViewActividades.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityController.close();
    }
}
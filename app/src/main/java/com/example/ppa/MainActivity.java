package com.example.ppa;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ppa.LoginPass.ListaActividadesActivity;
import com.example.ppa.LoginPass.ListaProyectosActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textViewUsuarioIdLogin;
    private Button btnIrAProyectos;
    private Button btnIrAActividades;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewUsuarioIdLogin = findViewById(R.id.textViewUsuarioIdLogin); // Asegúrate de tener este TextView en tu activity_main.xml
        btnIrAProyectos = findViewById(R.id.btnIrAProyectos); // Asegúrate de tener este Button en tu activity_main.xml
        btnIrAActividades = findViewById(R.id.btnIrAActividades); // Asegúrate de tener este Button en tu activity_main.xml
        btnLogout = findViewById(R.id.btnLogout); // Asegúrate de tener este Button en tu activity_main.xml

        // Obtener el ID del usuario que inició sesión (si se envió)
        int usuarioId = getIntent().getIntExtra("usuarioId", -1);
        if (usuarioId != -1) {
            textViewUsuarioIdLogin.setText("Usuario logueado con ID: " + usuarioId);
        } else {
            textViewUsuarioIdLogin.setText("Bienvenido a la aplicación.");
        }

        btnIrAProyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaProyectosActivity.class);
                intent.putExtra("usuarioId", usuarioId); // Pasar el ID del usuario a la lista de proyectos
                startActivity(intent);
            }
        });

        btnIrAActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí podrías navegar a una actividad que liste todas las actividades del usuario
                // O quizás mostrar un calendario de actividades, etc.
                // Por ahora, podemos dejar un Toast o un Log
                // Toast.makeText(MainActivity.this, "Módulo de Actividades en desarrollo", Toast.LENGTH_SHORT).show();
                // Log.i("MainActivity", "Ir a Actividades");
                Intent intent = new Intent(MainActivity.this, ListaActividadesActivity.class);
                intent.putExtra("usuarioId", usuarioId); // Pasar el ID del usuario a la lista de actividades (si la creas)
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volver a la pantalla de login
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Cerrar MainActivity
            }
        });
    }
}
package com.example.ppa;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppa.Controllers.UserController;

public class RegistroUsuarioActivity extends AppCompatActivity {
    private EditText editTextRegistroUsername;
    private EditText editTextRegistroPassword;
    private Button buttonRegistrar;
    private UserController userController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextRegistroUsername = findViewById(R.id.editTextRegistroUsername);
        editTextRegistroPassword = findViewById(R.id.editTextRegistroPassword);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);

        userController = new UserController(this);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextRegistroUsername.getText().toString();
                String password = editTextRegistroPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegistroUsuarioActivity.this, "Por favor, ingresa usuario y contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                long resultado = userController.registrarUsuario(username, password);

                if (resultado > 0) {
                    Toast.makeText(RegistroUsuarioActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    finish(); // Volver a la pantalla de login
                } else {
                    Toast.makeText(RegistroUsuarioActivity.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userController.close();
    }

}
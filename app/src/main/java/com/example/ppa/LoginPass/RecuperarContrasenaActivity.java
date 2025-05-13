package com.example.ppa.LoginPass;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppa.Controllers.UserController;
import com.example.ppa.R;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    private EditText editTextRecuperarUsername;
    private Button buttonRecuperar;
    private TextView textViewContrasenaRecuperada;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        editTextRecuperarUsername = findViewById(R.id.editTextRecuperarUsername);
        buttonRecuperar = findViewById(R.id.buttonRecuperar);
        textViewContrasenaRecuperada = findViewById(R.id.textViewContrasenaRecuperada);
        userController = new UserController(this);

        buttonRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextRecuperarUsername.getText().toString().trim();
                if (!username.isEmpty()) {
                    String password = userController.recuperarContrasena(username);
                    if (password != null) {
                        textViewContrasenaRecuperada.setText("La contraseña es: " + password);
                    } else {
                        textViewContrasenaRecuperada.setText("");
                        Toast.makeText(RecuperarContrasenaActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    textViewContrasenaRecuperada.setText("");
                    Toast.makeText(RecuperarContrasenaActivity.this, "Por favor, ingrese el nombre de usuario", Toast.LENGTH_SHORT).show();
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
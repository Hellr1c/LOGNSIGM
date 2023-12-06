package santos.hmarco.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecordDatabase db;

    EditText un, pw;
    Button lgn, reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new RecordDatabase(this);
        un = findViewById(R.id.edtUsername);
        pw = findViewById(R.id.edtPassword);
        lgn = findViewById(R.id.btnLogin);
        reg = findViewById(R.id.btnRegister);

        lgn.setOnClickListener(this);
        reg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnRegister) {
            Intent intent = new Intent(".RegisterActivity");
            startActivity(intent);
            finish();
        } else if (id == R.id.btnLogin) {
            String username = un.getText().toString();
            String password = pw.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Fill all fields", Toast.LENGTH_LONG).show();
            } else {

                //password = db.hashPassword(password);
                Log.d("Test", username + " " + password);
                int idl = db.login(username, password);
                if (idl != -1) {
                    if (db.isAdmin(idl)) {
                        Toast.makeText(MainActivity.this, "Admin Login Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(".AdminActivity");
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(".GuestActivity");
                        intent.putExtra("id", idl);
                        intent.putExtra("from", "Main");
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed Login", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
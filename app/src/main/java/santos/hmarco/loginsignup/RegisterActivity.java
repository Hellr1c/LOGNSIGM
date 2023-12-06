package santos.hmarco.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText un, pw, pw2;
    Button lgn, reg;

    RecordDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new RecordDatabase(this);

        un = findViewById(R.id.edtUsername);
        pw = findViewById(R.id.edtPassword);
        pw2 = findViewById(R.id.edtPassword2);
        lgn = findViewById(R.id.btnLogin);
        reg = findViewById(R.id.btnRegister);
        lgn.setOnClickListener(this);
        reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLogin) {
            Intent intent = new Intent(".MainActivity");
            startActivity(intent);
            finish();
        } else if (id == R.id.btnRegister) {
            String username = un.getText().toString();
            String password = pw.getText().toString();
            String password2 = pw2.getText().toString();

            if (username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_LONG).show();
            } else {
                if (password.equals(password2)) {
                    long idnn = db.addRecord("", "", "", username, password, "", "", "", "", "false", "false");
                    Toast.makeText(RegisterActivity.this, "Guest registered", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(".MainActivity");
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords don't match", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
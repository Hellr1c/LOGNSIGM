package santos.hmarco.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GuestActivity extends AppCompatActivity {

    EditText fn, mn, ln, un, addr, cont, email, oldpw, newpw, newpw2;
    Spinner gender;
    Button edit;

    int id;
    RecordDatabase db;
    ArrayList<String> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("Male");
        arr.add("Female");
        arr.add("Prefer not to say");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gender = findViewById(R.id.spinGender);
        gender.setAdapter(adapter);

        id = getIntent().getIntExtra("id", -1);
        db = new RecordDatabase(this);
        info = db.getRecord(id);

        fn = findViewById(R.id.edtfn);
        mn = findViewById(R.id.edtmn);
        ln = findViewById(R.id.edtln);

        un = findViewById(R.id.edtUsername);

        addr = findViewById(R.id.edtAddress);
        cont = findViewById(R.id.edtContact);
        email = findViewById(R.id.edtEmail);
//        oldpw = findViewById(R.id.edtPassword);
//        newpw = findViewById(R.id.edtPassword2);
//        newpw2 = findViewById(R.id.edtPassword3);

        edit = findViewById(R.id.btnEdit);

        fn.setText(info.get(0));
        mn.setText(info.get(1));
        ln.setText(info.get(2));
        un.setText(info.get(3));

        addr.setText(info.get(5));
        cont.setText(info.get(6));
        email.setText(info.get(8));

        if (info.get(7).isEmpty()) {
            gender.setSelection(0);
        } else {
            gender.setSelection(Integer.parseInt(info.get(7)));
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                String fname = !fn.getText().toString().isEmpty() ? fn.getText().toString() : info.get(0);
                String mname = !mn.getText().toString().isEmpty() ? mn.getText().toString() : info.get(1);
                String lname = !ln.getText().toString().isEmpty() ? ln.getText().toString() : info.get(2);
                String uname = !un.getText().toString().isEmpty() ? un.getText().toString() : info.get(3);
                String address = !addr.getText().toString().isEmpty() ? addr.getText().toString() : info.get(5);
                String contact = !cont.getText().toString().isEmpty() ? cont.getText().toString() : info.get(6);
                String emailaddr = !email.getText().toString().isEmpty() ? email.getText().toString() : info.get(8);
                String gend = String.valueOf(gender.getSelectedItemPosition());
                String password = info.get(4);
                /**
                if (!oldpw.getText().toString().isEmpty()) {
                    if (newpw.getText().toString().isEmpty() || newpw2.getText().toString().isEmpty()) {
                        Toast.makeText(GuestActivity.this, "Please complete all password fields to continue.", Toast.LENGTH_LONG).show();
                    }
                } else if (!newpw.getText().toString().isEmpty()) {
                    if (oldpw.getText().toString().isEmpty() || newpw2.getText().toString().isEmpty()) {
                        Toast.makeText(GuestActivity.this, "Please complete all password fields to continue.", Toast.LENGTH_LONG).show();
                    }
                } else if (!newpw2.getText().toString().isEmpty()) {
                    if (oldpw.getText().toString().isEmpty() || newpw.getText().toString().isEmpty()) {
                        Toast.makeText(GuestActivity.this, "Please complete all password fields to continue.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    String oldpass = oldpw.getText().toString();
                    String newpass = newpw.getText().toString();
                    String newpass2 = newpw2.getText().toString();

                    if (!oldpass.isEmpty() && !newpass.isEmpty() && !newpass2.isEmpty()) {
                        if (!oldpass.equals(info.get(4))) {
                            Toast.makeText(GuestActivity.this, "Old password is wrong.", Toast.LENGTH_LONG).show();
                        } else if (!newpass.equals(info.get(4))) {
                            Toast.makeText(GuestActivity.this, "New password is the same as old password.", Toast.LENGTH_LONG).show();
                        } else if (!newpass2.equals(newpass)) {
                            Toast.makeText(GuestActivity.this, "New passwords must match.", Toast.LENGTH_LONG).show();
                        } else {
                            password = newpass;
                        }
                    }
                } */

                int cnt = db.editRecord(id, fname, mname, lname, uname, password, address, contact, gend, emailaddr, info.get(9), info.get(10));
                Toast.makeText(GuestActivity.this, "Edit record success " + String.valueOf(cnt), Toast.LENGTH_LONG).show();
                String from = getIntent().getStringExtra("from");
                if (!from.equals("Main")) {
                    Intent intent = new Intent(".AdminActivity");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
package santos.hmarco.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminActivity extends ListActivity {

    RecordDatabase db;
    ListView lv;
    //Button btnlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_admin);

        //btnlogout = (Button) findViewById(R.id.btnlogout);

        db = new RecordDatabase(this);
        ArrayList<String> guests = db.getStrings();

        if (guests.size() > 0) {
            setListAdapter(new ArrayAdapter<String>(AdminActivity.this, android.R.layout.simple_list_item_1, guests));

        } else {
            Toast.makeText(AdminActivity.this, "No records.", Toast.LENGTH_LONG).show();
        }

//        btnlogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(".MainActivity");
//                startActivity(intent);
//                finish();
//            }
//        });

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String selectedItemText = (String) getListAdapter().getItem(position);
        int idn = Integer.parseInt(selectedItemText.split(" ")[0]);

        if (db.isConfirm(idn)) {
            // dialog ng info
            ArrayList<String> info = db.getRecord(idn);
            Log.d("Test", info.toString());
            String gender = info.get(7) == "0" ? "Male" : info.get(7) == "1" ? "Female" : "Prefer not to say";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(info.get(3))
                    .setMessage("Full name: " + info.get(0) + " " + info.get(1) + " " + info.get(2) +
                            "\n Address: " + info.get(5) +
                            "\n Contact: " + info.get(6) +
                            "\n Gender: " + gender +
                            "\n Email: " + info.get(8));

            builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(".GuestActivity");
                    intent.putExtra("from", "admin");
                    intent.putExtra("id", idn);
                    startActivity(intent);
                    finish();
                }
            });

            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Guest account").setMessage("This is a guest account. Please approve or deny.");
            builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.approve(idn);
                }
            });
            builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        //Toast.makeText(AdminActivity.this, "Clicked: " + String.valueOf(idn), Toast.LENGTH_LONG).show();
    }
}
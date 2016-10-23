package com.edwinacubillos.sesion9firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText eId, eNombre, eTelefono, eCorreo;

    String nombre, correo,telefono,codigo;
    Button bInsertar, bActualizar, bBorrar, bBuscar,bLimpiar;
    Integer id=0;

    private String FIREBASE_URL="https://agenda-28d7f.firebaseio.com/";
    private Firebase firebasedatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        firebasedatos = new Firebase(FIREBASE_URL);

        eId = (EditText) findViewById(R.id.eId);
        eNombre = (EditText) findViewById(R.id.eNombre);
        eTelefono = (EditText) findViewById(R.id.eTelefono);
        eCorreo = (EditText) findViewById(R.id.eMail);
        bInsertar = (Button) findViewById(R.id.bInsertar);
        bActualizar = (Button) findViewById(R.id.bActualizar);
        bBorrar = (Button) findViewById(R.id.bBorrar);
        bBuscar = (Button) findViewById(R.id.bBuscar);
        bLimpiar = (Button) findViewById(R.id.bLimpiar);

        bInsertar.setOnClickListener(this);
        bActualizar.setOnClickListener(this);
        bBorrar.setOnClickListener(this);
        bBuscar.setOnClickListener(this);
        bLimpiar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        nombre = eNombre.getText().toString();
        correo = eCorreo.getText().toString();
        telefono = eTelefono.getText().toString();
        codigo = eId.getText().toString();
        Firebase firebd;
        switch (v.getId()) {
            case R.id.bLimpiar:
                eNombre.setText("");
                eTelefono.setText("");
                eCorreo.setText("");
                eId.setText("");
                break;
            case R.id.bInsertar:
                Contacto contacto = new Contacto(nombre,telefono,correo,String.valueOf(id));
                firebd = firebasedatos.child("contacto "+id);
                firebd.setValue(contacto);
                id++;
                break;
            case R.id.bActualizar:
                firebd = firebasedatos.child("contacto "+codigo);

                Map<String,Object> nuevonombre = new HashMap<>();
                nuevonombre.put("nombre",nombre);
                firebd.updateChildren(nuevonombre);

                Map<String,Object> nuevotelefono = new HashMap<>();
                nuevonombre.put("telefono",telefono);
                firebd.updateChildren(nuevotelefono);

                Map<String,Object> nuevocorreo = new HashMap<>();
                nuevonombre.put("mail",correo);
                firebd.updateChildren(nuevocorreo);


                break;
            case R.id.bBorrar:
                firebd = firebasedatos.child("contacto "+codigo);
                firebd.removeValue();
                break;
            case R.id.bBuscar:
                final String code = "contacto "+codigo;
                firebasedatos.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(code).exists()){
                            Log.d("data",dataSnapshot.child(code).getValue().toString());
                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                break;

        }

    }
}

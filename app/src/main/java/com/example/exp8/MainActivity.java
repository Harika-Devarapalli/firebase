package com.example.exp8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

class details{
    String vendor,pid,pname,price,itemsavailable,type;
    details(){

    }
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setItemsavailable(String itemsavailable) {
        this.itemsavailable = itemsavailable;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVendor() {
        return vendor;
    }

    public String getPid() {
        return pid;
    }

    public String getPname() {
        return pname;
    }

    public String getPrice() {
        return price;
    }

    public String getItemsavailable() {
        return itemsavailable;
    }

    public String getType() {
        return type;
    }

    details(String vendor, String pid, String pname, String price, String itemsavailable, String type){
        this.vendor =vendor;
        this.pid=pid;
        this.pname=pname;
        this.price=price;
        this.itemsavailable=itemsavailable;
        this.type=type;
    }
    boolean check(){
        if(vendor.length()>0 && pid.length()>0 && pname.length()>0 && itemsavailable.length()>0 && type.length()>0 && price.length()>0){
            return true;
        }
        return false;
    }

    Map<String,String> makemap(){
        Map<String,String> var=new HashMap<>();
        var.put("vendor",vendor);
        var.put("pid",pid);
        var.put("pname",pname);
        var.put("price",price);
        var.put("itemsavailable",itemsavailable);
        var.put("type",type);
        return var;
    }
}
public class MainActivity extends AppCompatActivity {
    boolean flag=false;
    ProgressDialog progressDialog,progressDialog2;
    EditText vendor ,pid,pname,price,itemsavailable,type;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Button add,table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vendor=findViewById(R.id.vendorid);
        pid=findViewById(R.id.productid);
        pname=findViewById(R.id.productname);
        itemsavailable=findViewById(R.id.itemsavailable);
        type=findViewById(R.id.type);
        price=findViewById(R.id.price);
        table=findViewById(R.id.button2);
        add=findViewById(R.id.add);
        pid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {


                String text=pid.getText().toString();

                DatabaseReference getvendor = database.getReference("vendor/"+vendor.getText()+"/"+text);
                getvendor.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            progressDialog2= ProgressDialog.show(MainActivity.this, "Please Wait",
                                    "Data is fetching", false);
                            details a=task.getResult().getValue(details.class);
                            Log.d("mydataget", task.getResult().toString());
                            if(a!=null){
                                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT ).show();
                                pname.setText(a.pname);
                                pname.setFocusable(false);
                                type.setText(a.type);
                                type.setFocusable(false);
                            }
                            else{
                                pname.setText("");
                                pname.setFocusable(true);
                                type.setText("");
                                type.setFocusable(true);
                            }
                            progressDialog2.hide();

                        }
                    }
                });
            }
        });
        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                details data=new details(vendor.getText().toString(),pid.getText().toString(),pname.getText().toString(),price.getText() .toString()
                        , itemsavailable.getText().toString(), type.getText().toString());
                if(data.check()) {
                    progressDialog= ProgressDialog.show(MainActivity.this, "Please Wait",
                            "Data is adding", false);
                    checkvendorExists(data);

                }
                else{
                    Toast.makeText(MainActivity.this, "Fill all the Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        table.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,table_activity.class);
                startActivity(i);
            }
        });
    }
    void checkvendorExists(details data){

        DatabaseReference getvendor = database.getReference("vendor/"+data.vendor);
        getvendor.get().addOnCompleteListener(new  OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                   if(task.getResult().getValue()==null){
                       database.getReference("vendor").child(data.vendor).child(data.pid).setValue(data.makemap());
                       progressDialog.dismiss();
                       Toast.makeText(MainActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                       Log.d("test","vendornull");
                   }
                   else{
                       DatabaseReference item = database.getReference("vendor/"+data.vendor+"/"+data.pid);
                       item.get().addOnCompleteListener(new  OnCompleteListener<DataSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<DataSnapshot> task) {
                               if (!task.isSuccessful()) {
                                   Log.e("firebase", "Error getting data", task.getException());
                               }
                               else {
                                   if(task.getResult().getValue()==null){
                                       database.getReference("vendor").child(data.vendor).child(data.pid).setValue(data.makemap());
                                       progressDialog.dismiss();
                                       Toast.makeText(MainActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                       Log.d("test","itemnull");
                                   }
                                   else{
                                      details data2= task.getResult().getValue(details.class);
                                      data2.itemsavailable=String.valueOf(Integer.parseInt(data.itemsavailable)+(Integer.parseInt(data2.itemsavailable)));
                                       data2.price=data.price;
                                       database.getReference("vendor").child(data.vendor).child(data.pid).setValue(data2.makemap());
                                       progressDialog.dismiss();
                                       Toast.makeText(MainActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                       Log.d("test","nonull");
                                   }
                               }
                           }
                       });
                   }
                }
            }
        });
    }
}
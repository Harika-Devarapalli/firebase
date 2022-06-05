package com.example.exp8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.exp8.details;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class table_activity extends AppCompatActivity {
    Button b;
    EditText filter;
    ProgressDialog progressDialog;
    String Selectedvalue;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    List<details> var;
    void fetchData(){
        var =new ArrayList<details>();
        progressDialog= ProgressDialog.show(table_activity.this, "Please Wait",
                "Fetching data", false);
        DatabaseReference ref=database.getReference("vendor");
        ValueEventListener myref=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    for(DataSnapshot p:s.getChildren()){

                        details obj=p.getValue(details.class);
                        var.add(obj);

                    }
                }
                Log.d("mysize",String.valueOf(var.size()));
                addalldata();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addValueEventListener(myref);


    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        filter=findViewById(R.id.filter);
        Selectedvalue="select";
        AutoCompleteTextView dropdown = findViewById(R.id.autoCompleteTextView);
        String values[]=getResources().getStringArray(R.array.values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.values));
        dropdown.setAdapter(adapter);
        dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                       Selectedvalue=values[i];
                        Toast.makeText(table_activity.this, values[i], Toast.LENGTH_SHORT).show();
            }
        });
        fetchData();

        b=findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                removerows();
                if(Selectedvalue.equals("select")|| Selectedvalue.equals("All")){
                    addalldata();
                }
                else{
                    String valuefilter=filter.getText().toString();
                    if(Selectedvalue.equals("Product_id")){
                        int n=var.size();
                        int i;
                        for(i=0;i<n;i++){
                            if(var.get(i).pid.contains(valuefilter))
                                addrow(var.get(i).vendor,var.get(i).pid,var.get(i).pname,var.get(i).price,var.get(i).itemsavailable,var.get(i).type);
                        }
                    }
                    if(Selectedvalue.equals("Product_Name")){
                        int n=var.size();
                        int i;
                        for(i=0;i<n;i++){
                            if(var.get(i).pname.contains(valuefilter))
                                addrow(var.get(i).vendor,var.get(i).pid,var.get(i).pname,var.get(i).price,var.get(i).itemsavailable,var.get(i).type);
                        }
                    }
                    if(Selectedvalue.equals("Type")){
                        int n=var.size();
                        int i;
                        for(i=0;i<n;i++){
                            if(var.get(i).type.contains(valuefilter))
                                addrow(var.get(i).vendor,var.get(i).pid,var.get(i).pname,var.get(i).price,var.get(i).itemsavailable,var.get(i).type);
                        }
                    }
                    if(Selectedvalue.equals("Vendor")){
                        int n=var.size();
                        int i;
                        for(i=0;i<n;i++){
                            if(var.get(i).vendor.contains(valuefilter))
                                addrow(var.get(i).vendor,var.get(i).pid,var.get(i).pname,var.get(i).price,var.get(i).itemsavailable,var.get(i).type);
                        }

                    }
                    if(Selectedvalue.equals("Available_Items")) {
                        int n = var.size();
                        int i;
                        for (i = 0; i < n; i++) {
                            try {
                                if (Integer.parseInt(var.get(i).itemsavailable) > Integer.parseInt(valuefilter))
                                    addrow(var.get(i).vendor, var.get(i).pid, var.get(i).pname, var.get(i).price, var.get(i).itemsavailable, var.get(i).type);
                            } catch (Exception e) {
                                    Toast.makeText(table_activity.this, "Enter number", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
            }
        });
    }
    void addalldata(){
        int size=var.size();
        int i;
        for(i=0;i<size;i++){
            addrow(var.get(i).vendor,var.get(i).pid,var.get(i).pname,var.get(i).price,var.get(i).itemsavailable,var.get(i).type);
        }
    }
    void removerows(){
        TableLayout mytable=findViewById(R.id.mytable);
        int count=mytable.getChildCount();
        int i;
        Log.d("count",String.valueOf(count));
        if(count>1)
        for(i=1;i<count;i++){
            mytable.removeViewAt(1);
        }
    }
    void addrow(String vendor,String pid,String pname,String price,String itemsavailable,String type){
        TableLayout mytable=findViewById(R.id.mytable);
        TableRow myrow=new TableRow(this);
        TableLayout.LayoutParams rowparams=new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        myrow.setLayoutParams(rowparams);
        for(int i=0;i<6;i++){
            TextView mytext=new TextView(this);
            TableRow.LayoutParams mytextparams=new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            mytextparams.setMargins((int)dpToPx(this, 10), 0,(int)dpToPx(this, 10), 0);
            mytextparams.weight=1;
            mytext.setTextColor(Color.BLACK);
            mytext.setTextSize(16);
            mytext.setLayoutParams(mytextparams);
            if(i==0)
                mytext.setText(vendor);
            if(i==1)
                mytext.setText(pid);
            if(i==2)
                mytext.setText(pname);
            if(i==3)
                mytext.setText(price);
            if(i==4)
                mytext.setText(itemsavailable);
            if(i==5)
                mytext.setText(type);
            myrow.addView(mytext);
        }
        mytable.addView(myrow);
    }
    public static float dpToPx(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
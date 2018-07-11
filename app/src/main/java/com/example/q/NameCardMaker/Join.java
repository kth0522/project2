package com.example.q.NameCardMaker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static android.widget.Toast.LENGTH_SHORT;


public class Join extends AppCompatActivity {
    EditText et_id, et_pw, et_pw_chk;
    String sId, sPw, sPw_chk;
    Button btn_join;
    private final String BASE_URL = "http://52.231.71.86:8080";
    private Retrofit retrofit;

    public interface AddUserService{
        @GET("/api/NewUser/{email}/{password}")
        Call<JsonObject> addUser(
                @Path("email") String email,
                @Path("password") String password

        );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        et_id = (EditText) findViewById(R.id.joinEmail);
        et_pw = (EditText) findViewById(R.id.joinPassword);
        et_pw_chk = (EditText) findViewById(R.id.checkPassword);

        sId = et_id.getText().toString();
        sPw = et_pw.getText().toString();
        sPw_chk = et_pw_chk.getText().toString();

        btn_join = (Button) findViewById(R.id.jBtnJoin);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_id.getText().toString().equals("") || et_pw.getText().toString().equals("")|| et_pw_chk.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"빈 칸을 모두 입력해 주세요.",LENGTH_SHORT).show();
                }
                else{
                    sId = et_id.getText().toString();
                    sPw = et_pw.getText().toString();
                    sPw_chk = et_pw_chk.getText().toString();

                    if (sPw.equals(sPw_chk)) {
                        init();
                        AddUserService addUserService = retrofit.create(AddUserService.class);
                        Call<JsonObject> call = addUserService.addUser(sId, sPw);
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                Toast.makeText(Join.this, "성공적으로 가입되었습니다", Toast.LENGTH_SHORT).show();
                                Intent myIntent = new Intent(getApplicationContext(), Login.class);
                                startActivity(myIntent);
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Toast.makeText(Join.this, "가입 실패", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(),"비밀번호를 일치하지 않습니다.",LENGTH_SHORT).show();


                    }
                }

            }
        });
    }

    private void init(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }





}


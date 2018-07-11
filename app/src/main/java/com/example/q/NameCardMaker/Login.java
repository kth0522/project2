package com.example.q.NameCardMaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.q.NameCardMaker.models.Password;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static android.widget.Toast.LENGTH_SHORT;

public class Login extends AppCompatActivity {
    CallbackManager callbackManager = CallbackManager.Factory.create();

    EditText et_id, et_pw;
    String sId, sPw;
    Button btn_join,btn_login;

    private Retrofit retrofit;
    private final String BASE_URL = "http://52.231.71.86:8080";

    public interface  checkPassword{
        @GET("/api/{email}/password")
        Call<List<Password>> getPassword(
                @Path("email") String email
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        et_id = (EditText) findViewById(R.id.email);
        et_pw = (EditText) findViewById(R.id.password);

        sId = et_id.getText().toString();
        sPw = et_pw.getText().toString();

        btn_login = (Button) findViewById(R.id.btnLogin);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_id.getText().toString().equals("") || et_pw.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"빈 칸을 모두 입력해 주세요.",LENGTH_SHORT).show();
                }
                else{
                    sId = et_id.getText().toString();
                    sPw = et_pw.getText().toString();
                    init();
                    checkPassword checkpassword = retrofit.create(checkPassword.class);
                    Call<List<Password>> call = checkpassword.getPassword(sId);
                    call.enqueue(new Callback<List<Password>>() {
                        @Override
                        public void onResponse(Call<List<Password>> call, Response<List<Password>> response) {
                            List<Password> passwords = response.body();
                            String server_password = null;
                            for (Password componet : passwords) {
                                server_password = componet.password;
                            }
                            Log.d("password",server_password);
                            if (sPw.equals(server_password)) {
                                //Toast.makeText(Login.this, "맞았습니다", Toast.LENGTH_SHORT).show();
                                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(myIntent);
                            } else {
                                Toast.makeText(Login.this, "패스워드가 틀렸습니다", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<List<Password>> call, Throwable t) {
                            Toast.makeText(Login.this, "서버 접속 에러", Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }
        });

        btn_join =(Button)findViewById(R.id.btnJoin);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), Join.class);
                startActivity(myIntent);
            }
        });

        //facebook login
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("user_status"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

}

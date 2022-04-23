package hk.hku.cs.picshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import hk.hku.cs.picshare.account.AccountManager;
import hk.hku.cs.picshare.account.LoginRsp;
import hk.hku.cs.picshare.account.User;
import hk.hku.cs.picshare.lib.NetworkManager;
import hk.hku.cs.picshare.lib.PicImageView;
import hk.hku.cs.picshare.lib.ThreadManager;

public class LoginActivity extends Activity implements View.OnClickListener {
    private static final String Tag = "LoginActivity";
    EditText InputEmail, InputPassword;
    Button LoginBtn;
    TextView TextView_forgetpsw, TextView_Sign;
    String inputEmail, inputusername_psw;
    PicImageView Logo;
    NetworkManager network;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InputEmail = (EditText) findViewById(R.id.editText_email);
        InputPassword = (EditText) findViewById(R.id.editText_password);
        LoginBtn = (Button) findViewById(R.id.btn_login);
        TextView_forgetpsw = (TextView) findViewById(R.id.textview_forgetpsw);
        TextView_Sign = (TextView) findViewById(R.id.textview_sign);
        network = NetworkManager.getInstance();
        Logo=findViewById(R.id.picImageView);

        LoginBtn.setOnClickListener(this);
        TextView_forgetpsw.setOnClickListener(this);
        TextView_Sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            inputEmail = InputEmail.getText().toString();
            inputusername_psw = InputPassword.getText().toString();
            AlertDialog waitDialog = new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Login")
                    .setCancelable(false)
                    .setMessage("Please wait...")
                    .show();
            Log.i(Tag, "login start");
            network.login(inputEmail, inputusername_psw, new NetworkManager.PicCallback<LoginRsp>() {
                @Override
                public void onSuccess(LoginRsp data) {
                    Log.i(Tag, "login success:" + data);
                    AccountManager.getInstance().setLocalUserInfo(data.user.uid, data.user.name, data.user.email);
                    ThreadManager.getInstance().runOnUiThread(() -> {
                        waitDialog.dismiss();
                        finish();
                    });
                }

                @Override
                public void onFail(String msg) {
                    ThreadManager.getInstance().runOnUiThread(() -> {
                        waitDialog.dismiss();
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Error")
                                .setMessage(msg)
                                .show();
                    });
                }
            });
        } else if (view.getId() == R.id.textview_forgetpsw) {
            //TBD
        } else if (view.getId() == R.id.textview_sign) {
            Intent JumpToSign = new Intent(this, SigninActivity.class);
            startActivity(JumpToSign);
        }
    }

    @Override
    public void onBackPressed() {
    }
}

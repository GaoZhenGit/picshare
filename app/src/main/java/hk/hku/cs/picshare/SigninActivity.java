package hk.hku.cs.picshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import hk.hku.cs.picshare.account.User;
import hk.hku.cs.picshare.lib.NetworkManager;
import hk.hku.cs.picshare.lib.ThreadManager;

public class SigninActivity extends Activity implements View.OnClickListener {
    EditText SignName,SignNumber,SignPsw,SignPsw2;
    Button SignConfirm;
    ImageView SignImage;
    NetworkManager network;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        SignName=findViewById(R.id.edit_sign_name);
        SignNumber=findViewById(R.id.edit_sign_phone);
        SignPsw=findViewById(R.id.edit_sign_psw);
        SignPsw2=findViewById(R.id.edit_sign_psw2);
        SignConfirm=findViewById(R.id.btn_sign_confirm);
        SignImage=findViewById(R.id.img_sign_userpic);
        network = NetworkManager.getInstance();

        SignConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_sign_confirm)
        {
            String strName,strPhone,strPsw,strPsw2;
            strName=SignName.getText().toString();
            strPhone=SignNumber.getText().toString();
            strPsw=SignPsw.getText().toString();
            strPsw2=SignPsw2.getText().toString();
            if(strPsw.equals(strPsw2))
            {
                network.signin(strName,strPhone,strPsw,new NetworkManager.PicCallback<User>(){
                    @Override
                    public void onSuccess(User data) {
                        ThreadManager.getInstance().runOnUiThread(() -> {
                            AlertDialog waitDialog = new AlertDialog.Builder(SigninActivity.this)
                                    .setTitle("Success")
                                    .setMessage("Thank you for sign up")
                                    .show();
                        });

                    }

                    @Override
                    public void onFail(String msg) {
                        ThreadManager.getInstance().runOnUiThread(() -> {
                            AlertDialog waitDialog = new AlertDialog.Builder(SigninActivity.this)
                                    .setTitle("Error")
                                    .setMessage(msg)
                                    .show();
                        });
                    }
                });
            }
            else
            {
                AlertDialog waitDialog = new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Please check again your password.")
                        .show();
            }
        }

    }
}

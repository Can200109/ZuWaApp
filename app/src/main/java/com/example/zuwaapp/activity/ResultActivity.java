package com.example.zuwaapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zuwaapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultActivity extends AppCompatActivity {
    private TextView tvResult,tv;
    private final String QQ_URL="http://txz.qq.com/p?k=";
    private final String WX_URL="http://weixin.qq.com/x/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        tvResult = findViewById(R.id.tv_result);
        tv = findViewById(R.id.tv);
        Log.e("TAG", result );
        if (isHttpUrl(result)){
            if (result.startsWith(QQ_URL)){
                goqqScan(this);
            }else if(result.startsWith(WX_URL)){
                gowxScan(this);
            }
            else{
                Uri uri = Uri.parse(result);
                Intent httpIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(httpIntent);
            }

        }else {
            tv.setText("二维码结果为：");
            tvResult.setText(result);
        }

    }

    public static boolean isHttpUrl(String urls) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//比对
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }

    private void gowxScan(Context context){
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            context.startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(context,"没有安装微信",Toast.LENGTH_LONG);
        }
    }
    private void goqqScan(Context context){
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
        context.startActivity(intent);
    }
}
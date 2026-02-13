package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AesEncryptDecryptActivity extends AppCompatActivity {

    private EditText etInputContent; // 待加密/解密内容
    private EditText etKey; // 密钥
    private EditText etIv; // 初始向量
    private TextView tvResult; // 结果展示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes_encrypt_decrypt);

        // 绑定控件
        etInputContent = findViewById(R.id.et_input_content);
        etKey = findViewById(R.id.et_key);
        etIv = findViewById(R.id.et_iv);
        tvResult = findViewById(R.id.tv_result);
        Button btnEncrypt = findViewById(R.id.btn_encrypt);
        Button btnDecrypt = findViewById(R.id.btn_decrypt);

        // 加密按钮点击事件
        btnEncrypt.setOnClickListener(v -> doAesEncrypt());

        // 解密按钮点击事件
        btnDecrypt.setOnClickListener(v -> doAesDecrypt());

        // ========== 新增：点击结果复制 ==========
        tvResult.setOnClickListener(v -> {
            String resultText = tvResult.getText().toString().trim();
            if (resultText.isEmpty() || resultText.startsWith("加密失败") || resultText.startsWith("解密失败")) {
                Toast.makeText(this, "无有效内容可复制", Toast.LENGTH_SHORT).show();
                return;
            }

            // 复制到剪贴板
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    getSystemService(CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("AES结果", resultText);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * 执行AES加密
     */
    private void doAesEncrypt() {
        // 获取输入参数
        String plainText = etInputContent.getText().toString().trim();
        String key = etKey.getText().toString().trim();
        String iv = etIv.getText().toString().trim();

        // 空值校验
        if (plainText.isEmpty()) {
            Toast.makeText(this, "请输入待加密的明文", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 调用加密工具类
            String cipherText = AesEncryptUtil.encrypt(plainText, key, iv);
            // 展示结果
            tvResult.setText(cipherText);
            Toast.makeText(this, "加密成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // 捕获异常并提示
            tvResult.setText("加密失败：" + e.getMessage());
            Toast.makeText(this, "加密失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 执行AES解密
     */
    private void doAesDecrypt() {
        // 获取输入参数
        String cipherText = etInputContent.getText().toString().trim();
        String key = etKey.getText().toString().trim();
        String iv = etIv.getText().toString().trim();

        // 空值校验
        if (cipherText.isEmpty()) {
            Toast.makeText(this, "请输入待解密的Base64密文", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 调用解密工具类
            String plainText = AesEncryptUtil.decrypt(cipherText, key, iv);
            // 展示结果
            tvResult.setText(plainText);
            Toast.makeText(this, "解密成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // 捕获异常并提示
            tvResult.setText("解密失败：" + e.getMessage());
            Toast.makeText(this, "解密失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
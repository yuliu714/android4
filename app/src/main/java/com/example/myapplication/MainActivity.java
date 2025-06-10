package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.model.Msg;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 1. 成员变量
    private EditText editText1,editText2;
    private Button button1;
    private ListView listView;
    private List<Msg> msgs = new ArrayList<>();
    private DatabaseHelper  db= new DatabaseHelper(this);

    // 2. onCreate 方法（入口）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 调用父类的创建方法
        setContentView(R.layout.activity_main); // ✅ 加载你定义的 XML 布局文件（activity_main.xml）

        // 2.1 绑定控件
        editText1=findViewById(R.id.edittext1);
        editText2=findViewById(R.id.edittext2);
        button1= findViewById(R.id.button1);
        listView=findViewById(R.id.ListView);

        // 2.2 设置按钮点击事件（添加）
        button1.setOnClickListener(v -> {
            String msg=editText2.getText().toString().trim();
            //.getText()获取文本，，，.toString()转换String对象,,,.trim()把首尾的空字符去掉
            if(!msg.isEmpty()){//如果文本不为空
                boolean success = db.addMsg(msg);//插入数据
                if(success){//如果插入数据成功
                    Toast.makeText(MainActivity.this, "添加成功",//弹出消息
                            Toast.LENGTH_SHORT).show();
                }else{//如果插入数据不成功
                    Toast.makeText(MainActivity.this, "添加失败",//弹出消息
                            Toast.LENGTH_SHORT).show();
                }

            }else{//如果文本为空
                Toast.makeText(MainActivity.this, "文本为空",//弹出消息
                        Toast.LENGTH_SHORT).show();
            }
            loadMsg();
        });


        // 2.3 设置文本监听器（可选）
        editText1.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文本变化前
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文本变化时
                filterMsg(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 文本变化后            ‘
            }
        });
        editText2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文本变化前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文本变化时
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文本变化后
            }

        });

        // 2.4 设置ListView长按事件（删除）
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            //parent:父组件ListView，，view:当前组件列表项，，position:位置，类型是int，，id:id
            Msg msg = msgs.get(position);//List.get()
            new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)//也是AlertDialog弹窗
            .setTitle("确认删除")//下面4个setXXXXX是设置弹窗的标题、内容、按钮、取消按钮
            .setMessage("你确定要删除这条记录吗？")
            .setPositiveButton("确定", (dialog, which) -> {
                boolean success = db.deleteMsg(msg.id);
                if (success) {
                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    loadMsg();
                } else {
                    Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("取消", null)
            .show();
            return true;
        });

        // 2.5 设置ListView点击事件（修改）
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            //parent:父组件ListView，，view:当前组件列表项，，position:位置，类型是int，，id:id
            Msg msg = msgs.get(position);//List.get()
            boolean success = db.deleteMsg(msg.id);
            if (success) {
                Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                loadMsg();
            } else {
                Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
            } return true;
        });

        // 2.6 加载数据
        loadMsg();//调用函数

    }

    
    // 3. 加载数据到ListView
    private void loadMsg() {//加载数据
        msgs = db.getAllMsg();
        ArrayAdapter<Msg> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, msgs);
        listView.setAdapter(adapter);
    }

    // 4. 可选：搜索/过滤功能
    private void filterMsg(String query) {
        List<Msg> allMsgs = db.getAllMsg();
        List<String> filteredBooks = new ArrayList<>();
        for (Msg msg : allMsgs) {
            if (msg.message.toLowerCase().contains(query.toLowerCase())) {
                filteredBooks.add(msg.message);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredBooks);
        listView.setAdapter(adapter);
    }




}

package com.tc.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloWorldActivity extends AppCompatActivity {
    //Type logt then tab will help create a tag.
    private static final String TAG = "HelloWorldActivity";

     void linkMySql(){
        try {
            //Toast.makeText(HelloWorldActivity.this, "try to connect db.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "linkMySql: begin to link sql.");

            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection cn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/","root","DB123456");

            Statement st=cn.createStatement();

            /*
            String sql="use student";
            boolean hasResult = st.execute(sql);
            ResultSet rs = null;
            if (hasResult) {

                rs = st.getResultSet();
                if (rs != null)
                {

                    Log.d(TAG, "use student return: " );
                }
            }
            */


            String sql="select * from tb_class";
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                String name=rs.getString("name");
                Log.d(TAG, "linkMySql: " + name);
                //Toast.makeText(HelloWorldActivity.this, "from DB " + name, Toast.LENGTH_SHORT).show();
            }

            cn.close();
            st.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);
        //Use logd, logi then tab will help create log.
        Log.d("HelloWorldActivity", "onCreate execute");
        Log.i(TAG, "onCreate: this is log.i().");


        Button btnQuery = findViewById(R.id.btnQueryDB);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        linkMySql();

                    }
                }).start();
            }
        });


    }
}

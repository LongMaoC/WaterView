package cxy.com.waterviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cxy.com.waterviewlib.WaterView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WaterView waterView;
    private Button btn_start;
    private Button btn_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViw();

        btn_reset.setClickable(false);
        btn_start.setClickable(true);

        waterView.setListener(new WaterView.Listener() {
            @Override
            public void finish() {
                Toast.makeText(MainActivity.this, "已经满了！！！", Toast.LENGTH_SHORT).show();
                btn_reset.setClickable(true);
            }
        });
    }

    private void initViw() {
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_reset.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        waterView = (WaterView) findViewById(R.id.waterview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                btn_reset.setClickable(false);
                btn_start.setClickable(false);
                waterView.start();
                break;
            case R.id.btn_reset:
                btn_reset.setClickable(false);
                btn_start.setClickable(true);
                waterView.reset();
                break;
        }
    }
}

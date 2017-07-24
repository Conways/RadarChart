package com.conways.radarchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RadarChart radarChart;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radarChart=(RadarChart)this.findViewById(R.id.radarChart);
        List<RadarData> radarDatas=new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            RadarData radarData = new RadarData();
            radarData.setAbliity(10 + (int) (Math.random() * 50));
            radarData.setItemName(i+"能力");
            radarDatas.add(radarData);
        }
        radarChart.setList(radarDatas);
        seekBar=(SeekBar)this.findViewById(R.id.seekBar);
        seekBar.setMax(1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radarChart.setDeflectionAngle(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


}

package com.mar.yourfootfall;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //define variables
    TextView text1, text2, text3, ing, text4;
    SensorManager manager;
    SensorEventListener listener;
    Button startbtn;

    long startTime, nowTime;
    long times;

    float currentAcc, lastAcc, distance, effectiveAcc;
    float totaldistance= 0;

    float totalX, totalY, totalXs, totalYs, totalNot;
    float totalZ, totalZs;
    String num, rZ, rZs;

    float num2;
    int btnnum=0;
    boolean accel = false;
    boolean rotate = false;
    boolean pre = false;
    final static int FREQ = 1;
    int mOrientCount;
    float azims;

    float totalheight, totalheight2 = 0;
    float th, ths;

    float[] Accel = new float[3];
    float[] Rotate = new float[3];
    float[] Press = new float[3];
    float [] values1 = new float[3];
    int T;
    float x, y, z, pz;
    float height, nowheight, disheight, changeheight, changeheight2, nowheight2, disheight2;
    float azim;

    float NS2S = 1.0f/1000000000.0f;

    float [] Rs = new float[9];
    float [] Is = new float[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect to xml
        text2 = findViewById(R.id.textView2);
        text3 = findViewById(R.id.textView3);
        ing = findViewById(R.id.ing);
        text4 = findViewById(R.id.textView4);

        startbtn = findViewById(R.id.start);

        startTime = 0;

        manager = (SensorManager) getSystemService(SENSOR_SERVICE); //센서관리객체설정
        Sensor accelrometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor Rotation = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor Pressure = manager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        listener = new SensorEventListener() {

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor == accelrometer) {
                    System.arraycopy(event.values, 0, Accel, 0, event.values.length);//가속도센서 라면
                    accel = true;
                }

                else if (event.sensor == Rotation){
                    System.arraycopy(event.values, 0, Rotate, 0, event.values.length);
                    rotate = true;

                }
                else if(event.sensor == Pressure){
                    System.arraycopy(event.values, 0, Press, 0, event.values.length);
                    pre = true;
                }
                if((accel && rotate) && pre ){
                    ///////////////이동거리////////////
                    x = Accel[0];
                    y = Accel[1];
                    z = Accel[2];
                    startTime = 0;
                    nowTime = System.currentTimeMillis();
                    times = (long) ((nowTime - startTime)*NS2S);
                    startTime = nowTime;

                    currentAcc = Math.round(Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(y,2)));

                    effectiveAcc = currentAcc - lastAcc;
                    distance = Math.abs(effectiveAcc)*0.5f*times*times;

                    lastAcc = currentAcc;
                    totaldistance += distance;
                    num = String.format("%.2f", totaldistance/100000000);

                    text2.setText("총 이동 거리는"+num +"m입니다. \n");

                    num2 = totalZ + totalZs;

                    if(num2 <0){
                        text2.append("총 이동 고도는 0m입니다. \n");
                    }else{
                        text2.append("총 이동 고도는"+String.format("%.2f", num2/2)+"m입니다. \n");

                    }

                    /////////////////방향/////////////////

                    SensorManager.getRotationMatrix(Rs,Is,Accel,Rotate);
                    SensorManager.getOrientation(Rs, values1);

                    //방위값(라디언단위) -> 각도단위로 변경
                    azim = (float) Math.toDegrees(values1[0]);
                    if(mOrientCount++ % FREQ != 0) return;
                    T = mOrientCount / FREQ ;

                    if (T == 1){
                        azims = azim;
                    }

                    float ro = azim-azims;
                    text3.setText("방향 측정 중.. "+ro+"\n");
                    if((0>=ro && ro>-45) || (0<=ro && ro<45)) {
                        text3.setText("당신은 앞을 향했습니다." + "\n\n");
                        totalY = 0;
                        totalY += totaldistance - totalX - totalXs - totalYs;

                    } else if((ro>=45 && ro<135)||(ro<=-225 && ro>-315)) {
                        text3.setText("당신은 오른쪽을 향했습니다." + "\n\n");
                        totalX = 0;
                        totalX += totaldistance - totalY - totalXs - totalYs;

                    } else if((ro<=-135 && ro>-225)||(ro>=135 && ro<225)){
                        text3.setText("당신은 뒤로 향했습니다."+"\n\n");
                        totalYs = 0;
                        totalYs += totaldistance - totalX - totalXs - totalY;

                    } else if((ro<=-45 && ro>-135)||(ro>=225 && ro<315) ){
                        text3.setText("당신은 왼쪽을 향했습니다."+"\n\n");
                        totalXs = 0;
                        totalXs += totaldistance - totalX - totalYs - totalY;

                    }


                    String TY = String.format("%.2f", totalY/100000000);
                    text3.append("앞쪽으로"+TY+"m를 갔습니다."+"\n");
                    String TX = String.format("%.2f", totalX/100000000);
                    text3.append("오른쪽으로"+TX+"m를 갔습니다."+"\n");
                    String TYs = String.format("%.2f", totalYs/100000000);
                    text3.append("뒤쪽으로"+TYs+"m를 갔습니다."+"\n");
                    String TXs = String.format("%.2f", totalXs/100000000);
                    text3.append("왼쪽으로"+TXs+"m를 갔습니다."+"\n\n");
//////////////////고도////////////////////
                    pz = Press[0];

                    pz = (float) (Math.round(pz*100)/100.0);
                    height = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pz);
                    height = (float) (Math.round(height*100)/100.0);


                    changeheight = height - nowheight;

                    disheight = changeheight;
                    nowheight = height;

                    totalheight += disheight;
                    th = (float) (Math.round(totalheight*100)/100.0);


                    //총 거리
                    changeheight2 = height - nowheight2;
                    disheight2 = Math.abs(changeheight2);
                    nowheight2 = height;

                    totalheight2 += disheight2;
                    ths = (float) (Math.round(totalheight2*100)/100.0);



                    text4.setText("고도 측정 중... \n\n");
                    if(th>=0.25){
                        text4.setText("위쪽을 향했습니다. \n\n");
                        totalZ = 0;
                        totalZ += totalheight2 - totalZs;

                    }else if(th<=-0.25){
                        text4.setText("아래쪽을 향했습니다. \n\n");
                        totalZs = 0;
                        totalZs += totalheight2 - totalZ;

                    }else{
                        text4.setText("위쪽과 아래쪽으로는 이동하지 않았습니다. \n\n");
                        totalNot = 0;
                        totalNot += totalheight2 - totalZs - totalZ;
                    }
                    totalNot = (float) Math.round(totalNot*100/100.0);
                    rZ = String.format("%.2f", totalZ/2);
                    rZs= String.format("%.2f", totalZs/2);
                    text4.append("위쪽으로"+rZ+"m를 갔습니다.\n");
                    text4.append("아래쪽으로"+rZs+"m를 갔습니다.\n");

                }
        }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };


        startbtn.setOnClickListener(v -> {
            //아래 만들었던 리스너 연결
            ++btnnum;
            if(btnnum == 2){
                ing.setText("세팅 완료");
                startbtn.setText("측정하기");
                manager.unregisterListener(listener, accelrometer);
                manager.unregisterListener(listener, Rotation);
                manager.unregisterListener(listener, Pressure);


            }else if(btnnum == 1){
                boolean chk = manager.registerListener(listener, accelrometer, SensorManager.SENSOR_DELAY_UI);
                boolean chk2 = manager.registerListener(listener, Rotation, SensorManager.SENSOR_DELAY_UI);
                boolean chk3 = manager.registerListener(listener, Pressure, SensorManager.SENSOR_DELAY_UI);

                if (!chk) {
                    text1.setText("가속도 센서 지원하지 않습니다.");
                } else if (!chk2) {
                    text1.setText("방향 센서 지원하지 않습니다.");
                } else if(!chk3){
                    text1.setText("기압 센서 지원하지 않습니다.");
                }
                mOrientCount = 0;
                totaldistance=0;
                totalX = 0;
                totalXs = 0;
                totalY = 0;
                totalYs = 0;
                totalheight = 0;
                totalheight2 = 0;
                totalZ = 0;
                totalZs = 0;
                totalNot = 0;
                ing.setText("세팅 중...");
                startbtn.setText("세팅 끝내기");

            }else if(btnnum %2 == 0){
                ing.setText("측정 완료");
                startbtn.setText("다시 측정하기");
                manager.unregisterListener(listener, accelrometer);
                manager.unregisterListener(listener, Rotation);
                manager.unregisterListener(listener, Pressure);

            }else{
                boolean chk = manager.registerListener(listener, accelrometer, SensorManager.SENSOR_DELAY_UI);
                boolean chk2 = manager.registerListener(listener, Rotation, SensorManager.SENSOR_DELAY_UI);
                boolean chk3 = manager.registerListener(listener, Pressure, SensorManager.SENSOR_DELAY_UI);

                if (!chk) {
                    text1.setText("가속도 센서 지원하지 않습니다.");
                } else if (!chk2) {
                    text1.setText("방향 센서 지원하지 않습니다.");
                } else if(!chk3){
                    text1.setText("기압 센서 지원하지 않습니다.");
                }
                mOrientCount = 0;
                totaldistance=0;
                totalX = 0;
                totalXs = 0;
                totalY = 0;
                totalYs = 0;
                totalheight = 0;
                totalheight2 = 0;
                totalZ = 0;
                totalZs = 0;
                totalNot = 0;

                ing.setText("측정 중...");
                startbtn.setText("측정 끝내기");
            }
        });
    }
};
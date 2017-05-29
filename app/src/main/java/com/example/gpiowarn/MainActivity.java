package com.example.gpiowarn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //输入和输出GPIO引脚名称
    private static final String GPIO_IN_NAME_A = "BCM21";
    private static final String GPIO_IN_NAME_B = "BCM22";
    private static final String GPIO_IN_NAME_C = "BCM16";
    private static final String GPIO_OUT_NAME_A = "BCM5";
    private static final String GPIO_OUT_NAME_B = "BCM6";
    private static final String GPIO_OUT_NAME_C = "BCM19";

    //输入和输出Gpio
    private Gpio mGpioIn;
    private Gpio nGpioin;
    private Gpio lGpioin;
    private Gpio mGpioOut;
    private Gpio nGpioOut;
    private Gpio lGpioOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PeripheralManagerService manager = new PeripheralManagerService();
        try {
            //打开并设置输入Gpio，监听输入信号变化
            mGpioIn = manager.openGpio(GPIO_IN_NAME_A);
            mGpioIn.setDirection(Gpio.DIRECTION_IN);
            mGpioIn.setEdgeTriggerType(Gpio.EDGE_FALLING);
            mGpioIn.setActiveType(Gpio.ACTIVE_HIGH);
            mGpioIn.registerGpioCallback(mGpioCallback);

            nGpioin = manager.openGpio(GPIO_IN_NAME_B);
            nGpioin.setDirection(Gpio.DIRECTION_IN);
            nGpioin.setEdgeTriggerType(Gpio.EDGE_FALLING);
            nGpioin.setActiveType(Gpio.ACTIVE_HIGH);
            nGpioin.registerGpioCallback(mGpioCallback);

            lGpioin = manager.openGpio(GPIO_IN_NAME_C);
            lGpioin.setDirection(Gpio.DIRECTION_IN);
            lGpioin.setEdgeTriggerType(Gpio.EDGE_FALLING);
            lGpioin.setActiveType(Gpio.ACTIVE_HIGH);
            lGpioin.registerGpioCallback(mGpioCallback);

            //打开并设置输出Gpio
            mGpioOut = manager.openGpio(GPIO_OUT_NAME_A);
            mGpioOut.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);

            nGpioOut = manager.openGpio(GPIO_OUT_NAME_B);
            nGpioOut.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);

            lGpioOut = manager.openGpio(GPIO_OUT_NAME_C);
            lGpioOut.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private GpioCallback mGpioCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            try {
                if (gpio == mGpioIn) {
                    mGpioOut.setValue(true);
                }
                if (gpio == nGpioin) {
                    nGpioOut.setValue(true);
                }
                if (nGpioOut.getValue() & mGpioOut.getValue()) {
                    lGpioOut.setValue(true);
                }
                if (gpio == lGpioin) {
                    mGpioOut.setValue(false);
                    nGpioOut.setValue(false);
                    lGpioOut.setValue(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public void onGpioError(Gpio gpio, int error) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭Gpio
        if (mGpioIn != null) {
            try {
                mGpioIn.unregisterGpioCallback(mGpioCallback);
                mGpioIn.close();
                mGpioIn = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (nGpioin != null) {
            try {
                nGpioin.unregisterGpioCallback(mGpioCallback);
                nGpioin.close();
                nGpioin = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (lGpioin != null) {
            try {
                lGpioin.unregisterGpioCallback(mGpioCallback);
                lGpioin.close();
                lGpioin = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mGpioOut != null) {
            try {
                mGpioOut.close();
                mGpioOut = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (nGpioOut != null) {
            try {
                nGpioOut.close();
                nGpioOut = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (lGpioOut != null) {
            try {
                lGpioOut.close();
                lGpioOut = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


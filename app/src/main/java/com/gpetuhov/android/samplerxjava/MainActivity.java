package com.gpetuhov.android.samplerxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;

    private CompositeSubscription subscription;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text_view);
        updateText();

        button = (Button) findViewById(R.id.button_start);
        button.setOnClickListener(v -> {
            toggleCounter();
        });
    }

    private void toggleCounter() {

        if (null == subscription) {
            counter = 0;
            updateText();

            subscription = new CompositeSubscription();
            subscription.add(
                    Observable
                            .interval(1, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(number -> {
                                counter++;
                                updateText();
                            })
            );

        } else {
            if (subscription.hasSubscriptions()) {
                subscription.unsubscribe();
            }
            subscription = null;
        }
    }

    private void updateText() {
        textView.setText("" + counter);
    }
}

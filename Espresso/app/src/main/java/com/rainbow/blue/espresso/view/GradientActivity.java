/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rainbow.blue.espresso.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rainbow.blue.espresso.R;
import com.rainbow.blue.espresso.widget.GradientView;

public class GradientActivity extends AppCompatActivity {

    private GradientView gradientView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        gradientView = (GradientView) findViewById(R.id.gradient_view);
        gradientView.initData(0, 1.9f, 2.5f, 5f);

    }

    public void setValue(View view) {
        gradientView.setValue((float) (Math.random() * 5));
    }


}

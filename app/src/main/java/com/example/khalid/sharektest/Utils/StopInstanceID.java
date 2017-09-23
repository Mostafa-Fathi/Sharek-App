package com.example.khalid.sharektest.Utils;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

/**
 * Created by Abdelrahman on 6/29/2017.
 */

public class StopInstanceID extends Thread
    {
        //Initially setting the flag as true

        private volatile boolean flag = true;

        //This method will set flag as false

    public void stopRunning()
    {
        flag = false;
    }

    @Override
    public void run()
    {
        //Keep the task in while loop

        //This will make thread continue to run until flag becomes false

        while (flag)
        {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}


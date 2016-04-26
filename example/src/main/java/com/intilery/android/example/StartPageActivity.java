package com.intilery.android.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.intilery.android.sdk.Intilery;
import com.intilery.android.sdk.IntileryConfig;
import com.intilery.android.sdk.io.IntileryIO;
import com.intilery.android.sdk.obj.EventData;
import com.intilery.android.sdk.obj.IntileryEvent;
import com.intilery.android.sdk.obj.Properties;
import com.intilery.android.sdk.obj.PropertyUpdate;
import com.intilery.android.sdk.obj.RequestResult;
import com.intilery.android.sdk.util.MapBuilder;

public class StartPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final StartPageActivity me = this;
        setContentView(R.layout.activity_startpage);
        GCMManager pushClientManager = new GCMManager(this, "GOOGLE_API_PROJECT_ID");
        pushClientManager.registerIfNeeded(new GCMManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(final String registrationId, boolean isNewRegistration) {

                Log.d("Registration id", registrationId);
                new Intilery(IntileryConfig.builder()
                    .userAgent("SOME USER AGENT GOES HERE")
                    .appName("YOUR APP NAME GOES HERE")
                    .gcmToken(registrationId) // SOME GCM TOKEN
                    .intileryToken("YOUR INTILERY TOKEN GOES")
                    .url("https://www.intilery-analytics.com/api")
                    .rootContext(me)
                    .build());
                try {
                    Intilery.i().getIo().track(
                        IntileryEvent.builder().eventData(
                            EventData.builder("Sign In")
                                 .data("Customer", new MapBuilder<String, Object>()
                                      .put("Email", "bob@intilery.com").build())
                            .build())
                        .build(),
                        new IntileryIO.RequestResultReceiver() {
                            @Override
                            public void receive(RequestResult requestResult) {
                                Intilery.i().getIo().setVisitorProperties(
                                    PropertyUpdate.builder()
                                        .property("First Name", "Bob")
                                        .property("Last Name", "Todd")
                                        .property("Sex","Male")
                                    .build(),
                                    new IntileryIO.RequestResultReceiver() {
                                        @Override
                                        public void receive(RequestResult requestResult) {
                                            Intilery.i().getIo().getVisitorProperties(
                                                new IntileryIO.PropertyReceiver() {
                                                    @Override
                                                    public void receive(Properties properties) {
                                                         System.out.println(properties.get("First Name"));
                                                    }
                                                },
                                                "First Name", "Last Name");
                                        } // end of receive method for setting visitor properties
                                    } // end of receiver class for setting visitor properties
                                ); // end of setVisitorProperties
                            } // end of receive method for sign in event
                        } //end of receiver class for sign in event
                    ); // end of .track()
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // End of success for GCM get token
            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package platform.contextawarenesscore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import platform.contextawarenesscore.common.Constants;
import platform.contextawarenesscore.common.Time;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * FitnessContextDemo - Demonstration Application for Context Awareness Platform
 */
public class FitnessContextDemo extends ContextClientActivity implements View.OnClickListener
{

    //Local variable to setlayouts if user info not received
    private boolean gotInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //Carry out window building activity stuff
        super.onCreate(savedInstanceState);

        //Set contexts update refresh time
        contextRefreshTime = 1000;

        setContentView(R.layout.activity_main);

        //Stop service button
        Button stopService = (Button) findViewById(R.id.button2);
        stopService.setOnClickListener(this);

        //Button to submit user info
        Button buttonDone = (Button) findViewById(R.id.btnDone);
        buttonDone.setOnClickListener(this);

        //Set listeners for layouts
        //Heart Rate Layout
        LinearLayout heartRateLayout = (LinearLayout) findViewById(R.id.layoutHeartRate);
        heartRateLayout.setOnClickListener(this);

        //vehicle Layout
        LinearLayout vehicleLayout = (LinearLayout) findViewById(R.id.layoutVehicle);
        vehicleLayout.setOnClickListener(this);

        //running Layout
        LinearLayout runningLayout = (LinearLayout) findViewById(R.id.layoutRunning);
        runningLayout.setOnClickListener(this);

        //Cycling Layout
        LinearLayout cyclingLayout = (LinearLayout) findViewById(R.id.layoutBike);
        cyclingLayout.setOnClickListener(this);

        //Walk Layout
        LinearLayout walkingLayout = (LinearLayout) findViewById(R.id.layoutWalking);
        walkingLayout.setOnClickListener(this);

        //Sitting Layout
        LinearLayout sittingLayout = (LinearLayout) findViewById(R.id.layoutSitting);
        sittingLayout.setOnClickListener(this);

    }


    /**
     * Sets layouts at startup
     */
    public void setLayouts()
    {
        //Set layouts visbility
        //Set user info layout to gone
        LinearLayout userInfo = (LinearLayout) findViewById(R.id.layoutUserInfo);
        userInfo.setVisibility(View.GONE);
        //Set contexts layout and button layout visible
        LinearLayout contexts = (LinearLayout) findViewById(R.id.layoutContexts);
        contexts.setVisibility(View.VISIBLE);
        LinearLayout button = (LinearLayout) findViewById(R.id.layoutButton);
        button.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v)
    {
        try {
            switch (v.getId()) {
                case R.id.button2:
                    stopContextService();
                    System.exit(0);
                    break;
                case R.id.btnDone:
                    //Set user info
                    EditText ageBox = (EditText) findViewById(R.id.txtAge);
                    contextService.getFitnessContext().getIdentityContext().setUserAge(Integer.parseInt(ageBox.getText().toString()));
                    EditText heightBox = (EditText) findViewById(R.id.txtHeight);
                    contextService.getFitnessContext().getIdentityContext().setUserHeightCM(Integer.parseInt(heightBox.getText().toString()));
                    String sex = "";
                    RadioButton rbM = (RadioButton) findViewById(R.id.radioM);
                    if (rbM.isChecked()) sex = "male";
                    else sex = "female";
                    contextService.getFitnessContext().getIdentityContext().setUserSex(sex);
                    EditText weightBox = (EditText) findViewById(R.id.txtWeight);
                    contextService.getFitnessContext().getIdentityContext().setUserWeightKG(Integer.parseInt(weightBox.getText().toString()));
                    gotInfo = true;
                    Toast.makeText(contextService, "Information set, Fitness Service Started!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.layoutBike:
                    Toast.makeText(contextService, "Time spent Cycling.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.layoutHeartRate:
                    Toast.makeText(contextService, "Average HeartRate from Smartwatch.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.layoutRunning:
                    Toast.makeText(contextService, "Time spent Running.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.layoutSitting:
                    Toast.makeText(contextService, "Time spent Sitting or Little Activity.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.layoutVehicle:
                    Toast.makeText(contextService, "Time spent in a Vehicle: Train, Car or Bus.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.layoutWalking:
                    Toast.makeText(contextService, "Time spent Walking.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }catch (Exception ex)
        {
            Toast.makeText(contextService, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public void updateContexts()
    {
        try {
            if (gotInfo) {
                setLayouts();
            }

            if (contextService.getFitnessContext().getIdentityContext().getUserWeightKG() > 0) {

                int heartRate = contextService.getFitnessContext().getAverageHeartRate();
                TextView txtHeartRate = (TextView) findViewById(R.id.txtAvgHRValue);
                txtHeartRate.setText(Integer.toString(heartRate));

                Time invehicle = (Time) contextService.getFitnessContext().getUserActivityContext().getActivityTimeSpent(Constants.Vehicle);
                TextView vehicle = (TextView) findViewById(R.id.txtInVehicleValue);
                vehicle.setText(invehicle.getHours() + " H " + invehicle.getMinutes() + " M");

                Time running = (Time) contextService.getFitnessContext().getUserActivityContext().getActivityTimeSpent(Constants.Running);
                TextView txtRunning = (TextView) findViewById(R.id.txtRunValue);
                txtRunning.setText(running.getHours() + " H " + running.getMinutes() + " M");

                Time cycling = (Time) contextService.getFitnessContext().getUserActivityContext().getActivityTimeSpent(Constants.Cycling);
                TextView txtCycling = (TextView) findViewById(R.id.txtInBikeValue);
                txtCycling.setText(cycling.getHours() + " H " + cycling.getMinutes() + " M");

                Time walking = (Time) contextService.getFitnessContext().getUserActivityContext().getActivityTimeSpent(Constants.Walking);
                TextView txtWalking = (TextView) findViewById(R.id.txtWalkValue);
                txtWalking.setText(walking.getHours() + " H " + walking.getMinutes() + " M");

                Time still = (Time) contextService.getFitnessContext().getUserActivityContext().getActivityTimeSpent(Constants.Still);
                TextView txtStill = (TextView) findViewById(R.id.txtSedValue);
                txtStill.setText(still.getHours() + " H " + still.getMinutes() + " M");

                String dayAndMonth = contextService.getFitnessContext().getTimeContext().DayOfTheMonth() + "/" +
                        contextService.getFitnessContext().getTimeContext().MonthAsInteger() + "/" +
                        contextService.getFitnessContext().getTimeContext().Year();
                TextView txtday = (TextView) findViewById(R.id.txtDayandDate);
                txtday.setText(dayAndMonth);

                String cal = "" + contextService.getFitnessContext().getCaloriesBurned();
                EditText txtcal = (EditText) findViewById(R.id.txtCalBurned);
                txtcal.setText(cal);

                String step = "" + contextService.getFitnessContext().getUserActivityContext().getStepsTaken();
                EditText steps = (EditText) findViewById(R.id.txtStepsTaken);
                steps.setText(step);

                gotInfo = true;
            }

            if (gotInfo) {
                setLayouts();
            }
        }catch(Exception ex)
        {
            Toast.makeText(contextService, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

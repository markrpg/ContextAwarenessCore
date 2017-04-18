package platform.contextawarenesscore.common;

/**
 * Created by Mark on 25/02/2017.
 */

public class Constants
{

    //Intent signiture for activity recognition
    public static final String ACTIVITY_RECOGNITION_INTENT = "custom.intent.action.Activity_Recognition";

    //Definition of custom command used in broadcast sending / receiving
    public static final String CUSTOM_BROADCAST_COMMAND = "Custom_Command";

    //Notification ID
    public static final int NOTIFICATION_ID = 1;

    //Activity Recognition Constants
    public static final String Vehicle = "IN_VEHICLE";
    public static final String Walking = "WALKING";
    public static final String Running = "RUNNING";
    public static final String Cycling = "CYCLING";
    public static final String Standing = "ON_FOOT";
    public static final String Tilting = "TILTING";
    public static final String Unknown = "UNKNOWN";
    public static final String Still = "STILL";

    //Heart Rate constants
    public static final String Heartrate = "HEARTRATE";

    //Update time for updating heartrate default 10 minutes (600,000ms)
    public static final int HeartRateUpdateTime = 600000;

    //Message thats only received by smartwatch for heartrate
    public static final String HeartRateMessage = "GETRT";

    //Attribute to define heart rate key for datamap heart rate retrieval in data layer
    public static final String HEARTRATE_KEY = "platform.contextawarenesscore.HeartRate";

    //Heartrate path used for data layer transfer
    public static final String HEARTRATE_PATH = "/HeartRate";

    //When calculating time for activity a value is used to return the last timestamp
    public static final String TimeStampValue = "TSV";
}

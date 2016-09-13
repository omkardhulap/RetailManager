package com.company.retail.config;
/**
 * @author omkar
 */

//TODO: Configure application.properties
public class ConfigConstants {
    public static String geoApiKey = "AIzaSyCuJmJrMJvMzu_4ZgMpK7dJiV_26sBwBJ0";
    public static String distMatrixApiKey = "AIzaSyDbPwMDCH72N8Qf_qsQW9WggQj4tTc-IVw";

    public static String distMatrixAPI_Base = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&";
    public static String distMatrixAPI_Origin = "origins=";
    public static String distMatrixAPI_Destination = "&destinations=";
    public static String distMatrixAPI_Key = "&key=";
}

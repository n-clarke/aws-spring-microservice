package com.s3bucket.file.manager.global;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class UserGroupData
{
    public UserGroupData(listOfGroups group)
    {
        setInitialisation(group);
    }

    private static String userGroup, accessKey, secretKey;
    public enum listOfGroups { admin, developer, devop, tester, businessanalyst }
    public static String getUserGroup() { return userGroup; }
    public static String getAccessKey() { return accessKey; }
    public static String getSecretKey() { return secretKey; }

    private static void setInitialisation(listOfGroups group){
        try (FileReader reader = new FileReader("com/s3bucket/file/manager/global/config.json"))
        {
            JSONParser jsonParser = new JSONParser();

            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray config = (JSONArray) obj;

            config.forEach(input -> setUserGroup((JSONObject) input, group));
            config.forEach(input -> setUserAccessKey((JSONObject) input, group));
            config.forEach(input -> setUserSecretKey((JSONObject) input, group));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void setUserGroup(JSONObject inObj, listOfGroups group)
    {
        JSONObject object = (JSONObject) inObj.get("user-group");
        userGroup = (String) object.get(group.toString());
    }

    private static void setUserAccessKey(JSONObject inObj, listOfGroups group)
    {
        JSONObject object = (JSONObject) inObj.get("user-group");
        accessKey = (String) object.get(group.toString() + "/" + "access-key");
    }

    private static void setUserSecretKey(JSONObject inObj, listOfGroups group)
    {
        JSONObject object = (JSONObject) inObj.get("user-group");
        secretKey = (String) object.get(group.toString() + "/" + "secret-key");
    }
}

package com.s3bucket.file.manager.global;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class S3BucketData {

    public S3BucketData()
    {
        setInitialisation();
    }

    private static String name, path, templateFile;

    public static String getName() { return name; }
    public static String getPath() { return path; }
    public static String getTemplateFile() { return templateFile; }

    private static void setName(){}

    private static void setInitialisation()
    {
        try (FileReader reader = new FileReader("com/s3bucket/file/manager/global/config.json"))
        {
            JSONParser jsonParser = new JSONParser();

            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray config = (JSONArray) obj;

            config.forEach(input -> setName((JSONObject) input));
            config.forEach(input -> setPath((JSONObject) input));
            config.forEach(input -> setTemplateFile((JSONObject) input));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void setName(JSONObject inObj)
    {
        JSONObject object = (JSONObject) inObj.get("s3bucket");
        name = (String) object.get("name");
    }

    private static void setPath(JSONObject inObj)
    {
        JSONObject object = (JSONObject) inObj.get("s3bucket");
        path = (String) object.get("data" + "/" + "path");
    }

    private static void setTemplateFile(JSONObject inObj)
    {
        JSONObject object = (JSONObject) inObj.get("s3bucket");
        templateFile = (String) object.get("data" + "/" + "template-file-url");
    }
}

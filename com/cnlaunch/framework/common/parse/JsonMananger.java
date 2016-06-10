package com.cnlaunch.framework.common.parse;

import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.framework.utils.NLog;
import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.xmlpull.v1.XmlPullParser;

public class JsonMananger {
    private static JsonMananger instance;
    private static ObjectMapper jsonMapper;
    private final String tag;

    private JsonMananger() {
        this.tag = JsonMananger.class.getSimpleName();
        if (jsonMapper == null) {
            jsonMapper = new ObjectMapper();
            jsonMapper.getSerializationConfig().setSerializationInclusion(Inclusion.ALWAYS);
            jsonMapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
    }

    public static JsonMananger getInstance() {
        if (instance == null) {
            synchronized (JsonMananger.class) {
                if (instance == null) {
                    instance = new JsonMananger();
                }
            }
        }
        return instance;
    }

    public <T> T jsonToBean(String json, Class<T> cls) throws HttpException {
        try {
            return jsonMapper.readValue(json, (Class) cls);
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new HttpException(e.getMessage());
        } catch (JsonMappingException e2) {
            if (json.contains("10003") || json.contains("1022")) {
                throw new HttpException(String.valueOf(AsyncTaskManager.JSONMAPPING_ERROR_CODE));
            }
            e2.printStackTrace();
            throw new HttpException(e2.getMessage());
        } catch (IOException e3) {
            e3.printStackTrace();
            throw new HttpException(e3.getMessage());
        }
    }

    public String beanToJson(Object obj) throws HttpException {
        String result = XmlPullParser.NO_NAMESPACE;
        try {
            result = jsonMapper.writeValueAsString(obj);
            NLog.m917e(this.tag, "beanToJson: " + result);
            return result;
        } catch (JsonGenerationException e) {
            e.printStackTrace();
            throw new HttpException(e.getMessage());
        } catch (JsonMappingException e2) {
            e2.printStackTrace();
            throw new HttpException(e2.getMessage());
        } catch (IOException e3) {
            e3.printStackTrace();
            throw new HttpException(e3.getMessage());
        }
    }

    public ObjectMapper getJsonMapper() {
        return jsonMapper;
    }
}

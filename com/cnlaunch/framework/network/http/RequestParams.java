package com.cnlaunch.framework.network.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

public class RequestParams {
    protected ConcurrentHashMap<String, FileWrapper> fileParams;
    protected boolean isRepeatable;
    protected ConcurrentHashMap<String, StreamWrapper> streamParams;
    protected ConcurrentHashMap<String, String> urlParams;
    protected ConcurrentHashMap<String, Object> urlParamsWithObjects;

    /* renamed from: com.cnlaunch.framework.network.http.RequestParams.1 */
    class C01281 extends HashMap<String, String> {
        C01281(String str, String str2) {
            put(str, str2);
        }
    }

    private static class FileWrapper {
        public String contentType;
        public File file;

        public FileWrapper(File file, String contentType) {
            this.file = file;
            this.contentType = contentType;
        }
    }

    private static class StreamWrapper {
        public String contentType;
        public InputStream inputStream;
        public String name;

        public StreamWrapper(InputStream inputStream, String name, String contentType) {
            this.inputStream = inputStream;
            this.name = name;
            this.contentType = contentType;
        }
    }

    public RequestParams() {
        this(null);
    }

    public RequestParams(Map<String, String> source) {
        this.isRepeatable = false;
        init();
        if (source != null) {
            for (Entry<String, String> entry : source.entrySet()) {
                put((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    public RequestParams(String key, String value) {
        this(new C01281(key, value));
    }

    public RequestParams(Object... keysAndValues) {
        this.isRepeatable = false;
        init();
        int len = keysAndValues.length;
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Supplied arguments must be even");
        }
        for (int i = 0; i < len; i += 2) {
            put(String.valueOf(keysAndValues[i]), String.valueOf(keysAndValues[i + 1]));
        }
    }

    public void put(String key, String value) {
        if (key != null && value != null && !XmlPullParser.NO_NAMESPACE.equals(value)) {
            this.urlParams.put(key, value);
        }
    }

    public void put(String key, File file) throws FileNotFoundException {
        put(key, file, null);
    }

    public void put(String key, File file, String contentType) throws FileNotFoundException {
        if (key != null && file != null) {
            this.fileParams.put(key, new FileWrapper(file, contentType));
        }
    }

    public void put(String key, InputStream stream) {
        put(key, stream, null);
    }

    public void put(String key, InputStream stream, String name) {
        put(key, stream, name, null);
    }

    public void put(String key, InputStream stream, String name, String contentType) {
        if (key != null && stream != null) {
            this.streamParams.put(key, new StreamWrapper(stream, name, contentType));
        }
    }

    public void put(String key, Object value) {
        if (key != null && value != null) {
            this.urlParamsWithObjects.put(key, value);
        }
    }

    public void add(String key, String value) {
        if (key != null && value != null) {
            Object obj = this.urlParamsWithObjects.get(key);
            if (obj == null) {
                obj = new HashSet();
                put(key, obj);
            }
            if (obj instanceof List) {
                ((List) obj).add(value);
            } else if (obj instanceof Set) {
                ((Set) obj).add(value);
            }
        }
    }

    public void remove(String key) {
        this.urlParams.remove(key);
        this.streamParams.remove(key);
        this.fileParams.remove(key);
        this.urlParamsWithObjects.remove(key);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Entry<String, String> entry : this.urlParams.entrySet()) {
            if (result.length() > 0) {
                result.append(AlixDefine.split);
            }
            result.append((String) entry.getKey());
            result.append("=");
            result.append((String) entry.getValue());
        }
        for (Entry<String, StreamWrapper> entry2 : this.streamParams.entrySet()) {
            if (result.length() > 0) {
                result.append(AlixDefine.split);
            }
            result.append((String) entry2.getKey());
            result.append("=");
            result.append("STREAM");
        }
        for (Entry<String, FileWrapper> entry3 : this.fileParams.entrySet()) {
            if (result.length() > 0) {
                result.append(AlixDefine.split);
            }
            result.append((String) entry3.getKey());
            result.append("=");
            result.append("FILE");
        }
        for (BasicNameValuePair kv : getParamsList(null, this.urlParamsWithObjects)) {
            if (result.length() > 0) {
                result.append(AlixDefine.split);
            }
            result.append(kv.getName());
            result.append("=");
            result.append(kv.getValue());
        }
        return result.toString();
    }

    public void setHttpEntityIsRepeatable(boolean isRepeatable) {
        this.isRepeatable = isRepeatable;
    }

    public HttpEntity getEntity(ResponseHandlerInterface progressHandler) throws IOException {
        if (this.streamParams.isEmpty() && this.fileParams.isEmpty()) {
            return createFormEntity();
        }
        return createMultipartEntity(progressHandler);
    }

    public HttpEntity createFormEntity() {
        try {
            return new UrlEncodedFormEntity(getParamsList(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private HttpEntity createMultipartEntity(ResponseHandlerInterface progressHandler) throws IOException {
        SimpleMultipartEntity entity = new SimpleMultipartEntity(progressHandler);
        entity.setIsRepeatable(this.isRepeatable);
        for (Entry<String, String> entry : this.urlParams.entrySet()) {
            entity.addPart((String) entry.getKey(), (String) entry.getValue());
        }
        for (BasicNameValuePair kv : getParamsList(null, this.urlParamsWithObjects)) {
            entity.addPart(kv.getName(), kv.getValue());
        }
        for (Entry<String, StreamWrapper> entry2 : this.streamParams.entrySet()) {
            StreamWrapper stream = (StreamWrapper) entry2.getValue();
            if (stream.inputStream != null) {
                entity.addPart((String) entry2.getKey(), stream.name, stream.inputStream, stream.contentType);
            }
        }
        for (Entry<String, FileWrapper> entry3 : this.fileParams.entrySet()) {
            FileWrapper fileWrapper = (FileWrapper) entry3.getValue();
            entity.addPart((String) entry3.getKey(), fileWrapper.file, fileWrapper.contentType);
        }
        return entity;
    }

    private void init() {
        this.urlParams = new ConcurrentHashMap();
        this.streamParams = new ConcurrentHashMap();
        this.fileParams = new ConcurrentHashMap();
        this.urlParamsWithObjects = new ConcurrentHashMap();
    }

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = new LinkedList();
        List<String> keyList = new ArrayList();
        for (Entry<String, String> entry : this.urlParams.entrySet()) {
            keyList.add((String) entry.getKey());
        }
        Collections.sort(keyList);
        for (String key : keyList) {
            for (Entry<String, String> entry2 : this.urlParams.entrySet()) {
                if (key.equals(entry2.getKey())) {
                    lparams.add(new BasicNameValuePair((String) entry2.getKey(), (String) entry2.getValue()));
                }
            }
        }
        lparams.addAll(getParamsList(null, this.urlParamsWithObjects));
        return lparams;
    }

    private List<BasicNameValuePair> getParamsList(String key, Object value) {
        List<BasicNameValuePair> params = new LinkedList();
        Object nestedValue;
        if (value instanceof Map) {
            Map<String, Object> map = (Map) value;
            List<String> list = new ArrayList(map.keySet());
            Collections.sort(list);
            for (String nestedKey : list) {
                String nestedKey2;
                nestedValue = map.get(nestedKey2);
                if (nestedValue != null) {
                    if (key != null) {
                        nestedKey2 = String.format("%s[%s]", new Object[]{key, nestedKey2});
                    }
                    params.addAll(getParamsList(nestedKey2, nestedValue));
                }
            }
        } else if (value instanceof List) {
            for (Object nestedValue2 : (List) value) {
                params.addAll(getParamsList(String.format("%s[]", new Object[]{key}), nestedValue2));
            }
        } else if (value instanceof Object[]) {
            for (Object nestedValue22 : (Object[]) value) {
                params.addAll(getParamsList(String.format("%s[]", new Object[]{key}), nestedValue22));
            }
        } else if (value instanceof Set) {
            for (Object nestedValue222 : (Set) value) {
                params.addAll(getParamsList(key, nestedValue222));
            }
        } else if (value instanceof String) {
            params.add(new BasicNameValuePair(key, (String) value));
        }
        return params;
    }

    public String getParamString() {
        StringBuilder result = new StringBuilder();
        for (NameValuePair parameter : getParamsList()) {
            if (result.length() > 0) {
                result.append(AlixDefine.split);
            }
            result.append(parameter.getName());
            result.append("=");
            result.append(parameter.getValue());
        }
        return result.toString();
    }

    public String getParamValueString() {
        StringBuilder lparams = new StringBuilder();
        for (Entry<String, String> entry : this.urlParams.entrySet()) {
            lparams.append((String) entry.getValue());
        }
        return lparams.toString();
    }
}

package com.soho.spring.extend;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.soho.spring.model.RetCode;
import com.soho.spring.model.RetData;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * @author shadow
 */
public class FastjsonMessageConver extends AbstractHttpMessageConverter<Object> {

    public static final Charset UTF8 = Charset.forName("UTF-8");
    private Charset charset;
    private SerializerFeature[] features;

    public FastjsonMessageConver() {
        super(new MediaType[]{new MediaType("application", "json", UTF8), new MediaType("application", "*+json", UTF8)});
        this.charset = UTF8;
        this.features = new SerializerFeature[0];
    }

    protected boolean supports(Class<?> clazz) {
        return true;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public SerializerFeature[] getFeatures() {
        return this.features;
    }

    public void setFeatures(SerializerFeature... features) {
        this.features = features;
    }

    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream in = inputMessage.getBody();
        byte[] buf = new byte[1024];
        while (true) {
            int len = in.read(buf);
            if (len == -1) {
                byte[] bytes = baos.toByteArray();
                return JSON.parseObject(bytes, 0, bytes.length, this.charset.newDecoder(), clazz, new Feature[0]);
            }
            if (len > 0) {
                baos.write(buf, 0, len);
            }
        }
    }

    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        OutputStream out = null;
        try {
            out = outputMessage.getBody();
            String text = JSON.toJSONString(obj, this.features);
            Object data = null;
            if (text == null || "".equals(text)) {
                data = new HashMap();
            } else {
                data = JSON.parse(text);
            }
            RetData<Object> retData = new RetData<>(RetCode.OK_STATUS, RetCode.OK_MESSAGE, data);
            text = JSON.toJSONString(retData);
            byte[] bytes = text.getBytes(this.charset);
            out.write(bytes);
        } catch (Exception e) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + e.getMessage(), e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}

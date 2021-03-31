package com.kevin.https;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/***
 * 利用HttpClient进行https请求的工具类
 */
public class HttpsClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpsClientUtil.class);
    static CloseableHttpClient httpClient;
    static CloseableHttpResponse httpResponse;

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();

    }

    /**
     * 发送https get请求
     * @param params 这个参数将会自动追加到url后面
     * @param url
     * @return
     */
    public static String sendByHttpsGet(Map<String,String>params,String url) {

        if(params ==null || params.size()==0) {
            //return "参数不合法";
        } else {
            Assert.hasText(url, "参数不合法");

            StringBuffer param = new StringBuffer("?");

            Set<String> keys = params.keySet();

            int i = 0 ;
            int max = keys.size();

            for (String key : keys) {
                param.append(key+"="+params.get(key));
                if(i<max-1) {
                    param.append("&");
                }
                i++;

            }

            url += param.toString();
        }


        logger.info("创建请求httpGet-URL={}", url);

        try {

            HttpGet httpGet = new HttpGet(url);
            httpClient = HttpsClientUtil.createSSLClientDefault();
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                return jsObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }finally {
            try {
                httpResponse.close();
                httpClient.close();
                logger.info("请求流关闭完成");
            } catch (IOException e) {
                logger.info("请求流关闭出错");
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 发送https post请求
     *
     * @param jsonPara
     * @throws Exception
     */
    public static String sendByHttpsPost(Map<String, Object> params, String url) {
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> listNVP = new ArrayList<NameValuePair>();
            if (params != null) {
                for (String key : params.keySet()) {
                    listNVP.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listNVP, "UTF-8");
            logger.info("创建请求httpPost-URL={},params={}", url, listNVP);
            httpPost.setEntity(entity);
            httpClient = HttpsClientUtil.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                return jsObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                httpResponse.close();
                httpClient.close();
                logger.info("请求流关闭完成");
            } catch (IOException e) {
                logger.info("请求流关闭出错");
                e.printStackTrace();
            }
        }
    }

    public static String sendByHttpsPost(String msg, String url) {
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(msg,"UTF-8");
            logger.info("创建请求httpPost-URL={},params={}", url, stringEntity);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-Type", "text/plain;charset=utf8");
            httpClient = HttpsClientUtil.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                return jsObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                httpResponse.close();
                httpClient.close();
                logger.info("请求流关闭完成");
            } catch (IOException e) {
                logger.info("请求流关闭出错");
                e.printStackTrace();
            }
        }
    }

    public static String sendByPost(String msg,String url) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(msg,"UTF-8");
        httpPost.setEntity(stringEntity);
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            } else {
                return "发送失败";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "发送失败";
    }

    public static void main(String[] args) {
//        String result = HttpsClientUtil.sendByHttpsPost("", "https://test-api.htlic.com/access/o/pay/bjsPay/resultNotify");
//        String result = HttpsClientUtil.sendByHttpsPost("", "https://www.baidu.com");
        String result = HttpsClientUtil.sendByPost("{\"用户名\":\"username\"}", "http://localhost:8080/test/test?params=测试1");
        System.out.println(result);
    }

}

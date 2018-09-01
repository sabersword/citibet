package org.ypq.newcitibet.task;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ypq.newcitibet.ui.Recognition;
import org.ypq.newcitibet.utils.CitibetUtils;

import net.sourceforge.tess4j.TesseractException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 
* @ClassName: Login 
* @Description: 用来登录账号的类,获取到的jsessionid是其他http任务的基础
* @author god
* @date 2017年2月22日 下午12:38:00
 */
public class Login {
//    @Autowired
    private OkHttpClient client;
    @Autowired
//    private JFrame frame;
    private static final Logger logger = LoggerFactory.getLogger(Login.class.getName());
    private String location = null, valid = null, code = null;   //几个重要的登陆变量,location用于找到主页,JSESSIONID用于header的标识,valid用于账号密码登陆加密,code用于验证码
    private String jsessionid = null;
    private Map<String, String> cookies = new HashMap<String, String>(); 
    private String uid = null;      //登陆的用户名
    private String pass = null;     //登陆密码
    private String pinCode = null;  //PIN码
    private String r1 = null, r2 = null;    //PIN步骤需要的r1,r2
    public final String codePath = "code.jpg";
    Login() {
        client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private HashMap<String, Cookie> cookieStore = new HashMap<String, Cookie>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                        for (Cookie cookie : cookies) {
//                            if (!cookie.name().equals("JSESSIONID"))
//                                cookieStore.put(cookie.name(), cookie);
//                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        if (cookieStore.isEmpty())
                            return  new ArrayList<Cookie>();
                        return new ArrayList<Cookie>(cookieStore.values());
                    }
                })
                .build();
    }
    Login(OkHttpClient client, JFrame frame) {

    }
    
    public void testImg() {
        Builder builder = new Request.Builder().url("https://www.ctb988.net/img.jpg?0.16911522029540038");
        builder.header("Accept", "text/html, application/xhtml+xml, */*");
        builder.header("Accept-Encoding", "gzip, deflate");
        builder.header("Accept-Language", "zh-CN");
        builder.header("Connection", "keep-alive");
        builder.header("Host", "www.ctb988.net");
        builder.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
        builder.header("Cookie", "JSESSIONID=" + jsessionid);
//        builder.header("Upgrade-Insecure-Requests", "1");
//        builder.header("Cache-Control", "max-age=0, no-cache");
//        builder.header("Pragma", "no-cache");
        
        
        Request request = builder.build();
        Call callCode = client.newCall(request);
        logger.info("getcode before()))");
        callCode.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
//                    String body = response.body().string();
                    String contentType = response.header("Content-Type");
                    logger.info("contentLength" + response.body().contentLength());
                System.out.println(response.body().contentLength());
                
                List<String> cookieHeaders =  response.headers("Set-Cookie");
                if (jsessionid == null) {
//                  根据cookie找到JSESSIONID,作为客户端的标识
                  
                  if(cookieHeaders == null) {
                      return;
                  }
                  for (String setCookie : cookieHeaders) {
                      Matcher m1 = Pattern.compile("(?<=JSESSIONID=).+?(?=;)").matcher(setCookie);
                      if(m1.find()) {
                          jsessionid = m1.group();
                      }
                  }
                  if (jsessionid == null)
                      return;
                }
                  System.out.println("jsessionid=" + jsessionid);
                
                
                testImg();
                BufferedImage bi = ImageIO.read(response.body().byteStream());
                ImageIO.write(bi, "jpg", new File(codePath));
                logger.info("getCode success");
                
                    code = Recognition.recognize(codePath);
                } catch (Throwable e) {
                    logger.info(e.getMessage());
                }
                System.out.println(codePath + " code = " + code);
                
                System.out.println("getCode success!");
                logger.info("getCode success2");
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("getCode fail!!!");
                logger.info("getCode fail");
            }
        });
        
    }
    
    public void beforeGetJsession() {
        
        
        Request request = new Request.Builder().url("https://www.ctb988.net/").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                
//                List<String> cookieHeaders =  response.headers("Set-Cookie");
//                if(cookieHeaders == null) {
//                    return;
//                }
//                for (String setCookie : cookieHeaders) {
//                    Matcher key = Pattern.compile(".+?(?==)").matcher(setCookie);
//                    Matcher value = Pattern.compile("(?<==).+?(?=;)").matcher(setCookie);
//                    if(key.find() && value.find()) {
//                        cookies.put(key.group(), value.group());
//                    }
//                }
                location = body.substring(body.indexOf('?') + 1, body.indexOf('\'', body.indexOf('?') + 1));
                getHome(location);
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                
            }
        });
    }
    
    /**
     * 登陆1.获取JSESSIONID
     * @param button
     * @param contentPane
     * @throws Exception
     */
    public void getJession() {
        Builder builder = new Request.Builder().url("https://www.ctb988.net/?r=1");
//        for (Map.Entry<String, String> entry : cookies.entrySet()) {
//            builder.addHeader("Cookie", entry.getKey() + "=" + entry.getValue());
//        }
        Request request = builder.build();
//        Request request = new Request.Builder().url("https://www.ctb988.net/?r=1").build();
//        client.setFollowRedirects(false);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //根据cookie找到JSESSIONID,作为客户端的标识
//                List<String> cookieHeaders =  response.headers("Set-Cookie");
//                if(cookieHeaders == null) {
//                    return;
//                }
//                for (String setCookie : cookieHeaders) {
//                    Matcher m1 = Pattern.compile("(?<=JSESSIONID=).+?(?=;)").matcher(setCookie);
//                    if(m1.find()) {
//                        jsessionid = m1.group();
//                    }
//                }
//                if (jsessionid == null)
//                    return;
//                
//                System.out.println("jsessionid=" + jsessionid);
                location = body.substring(body.indexOf('?') + 1, body.indexOf('\'', body.indexOf('?') + 1));
//                System.out.println("location= " + location);
//                cookies.put("JSESSIONID", jsessionid);
                getHome(location);
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("requestInit fail!!!");
            }
        });
        
    }
    
    /**
     * 登陆2.获取location
     * @param location
     */
    public void getHome(String location) {
        String url = "https://www.ctb988.net/?" + location;
        System.out.println(url);
//        Request request = new Request.Builder().url(url).addHeader("Cookie", "JSESSIONID=" + jsessionid).build();
        
        Builder builder = new Request.Builder().url(url);
//        for (Map.Entry<String, String> entry : cookies.entrySet()) {
//            builder.addHeader("Cookie", entry.getKey() + "=" + entry.getValue());
//        }
        Request request = builder.build();
        Call callCode = client.newCall(request);
        logger.info("getHome()))");
        callCode.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                System.out.println("getHome success!!!");
                logger.info("getHome success");
                getCode();
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("getHome fail!!!");
            }
        });
    }
    
    /**
     * 登陆3.获取验证码
     */
    public void getCode() {
//        Request request = new Request.Builder().url("http://www.ctb988.net/img.jpg?0.16911522029540038").addHeader("Cookie", "JSESSIONID=" + jsessionid).build();
        Builder builder = new Request.Builder().url("https://www.ctb988.net/img.jpg?0.16911522029540038");
//        for (Map.Entry<String, String> entry : cookies.entrySet()) {
//            builder.addHeader("Cookie", entry.getKey() + "=" + entry.getValue());
//        }
        builder.header("Accept", "image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5");
        builder.header("Accept-Language", "zh-CN");
        builder.header("Host", "www.ctb988.net");
//        builder.header("Referer", "https://www.ctb988.net/_index_ctb.jsp");
        builder.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
        
        Request request = builder.build();
        Call callCode = client.newCall(request);
        logger.info("getcode before()))");
        callCode.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String body = response.body().string();
                    logger.info("contentLength" + response.body().contentLength());
                    
                System.out.println(response.body().contentLength());
                getCode();
                BufferedImage bi = ImageIO.read(response.body().byteStream());
                ImageIO.write(bi, "jpg", new File(codePath));
                logger.info("getCode success");
                
                    code = Recognition.recognize(codePath);
                } catch (Throwable e) {
                    logger.info(e.getMessage());
                }
                System.out.println(codePath + " code = " + code);
                
                System.out.println("getCode success!");
                logger.info("getCode success2");
//                getValid();
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("getCode fail!!!");
                logger.info("getCode fail");
            }
        });
    }
    /**
     * 登陆4.获取用户名密码登陆所需的valid
     */
    
    public void getValid() {
        Request request = new Request.Builder().url("https://www.ctb988.net/_login.jsp")
                .addHeader("Cookie", "JSESSIONID=" + jsessionid)
                .addHeader("Host", "www.ctb988.net")
                .addHeader("Referer", "https://www.ctb988.net/login.jsp?" + location)
                .build();
        Call callCode = client.newCall(request);
        callCode.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Matcher matcher = Pattern.compile("(?<=id=\"valid\" value=\").+(?=\"/>)").matcher(body);
                if(!matcher.find())
                    return;
                valid = matcher.group();
                System.out.println("valid =" + valid);
                System.out.println("getValid success!");    
                
                firstLogin();
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("getValid fail!!!");
            }
        });
    }
    
    /**
     * 登陆5.根据获取的valid加密,开始第一次登陆
     */
    public void firstLogin() {
        pass = SHA1(valid + code + SHA1("voodoo_people_" + uid + SHA1(pass)));      //获取加密后的pass
        RequestBody requestBody = new FormBody.Builder()
                .add("code", code)
                .add("lang", "EN")
                .add("pass", pass)
                .add("ssl", "http:")
                .add("uid", uid)
                .add("valid", valid)
                .build();
//        FormEncodingBuilder feBuilder = new FormEncodingBuilder();
        Request request = new Request.Builder().url("https://secure.ctb988.net/login").post(requestBody).addHeader("Cookie", "JSESSIONID=" + jsessionid).build();
        Call callCode = client.newCall(request);
        callCode.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("firstLogin success!!!");
                getPin();
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("firstLogin fail!!!");
            }
        });
    }
    
    /**
     * 登陆6.获取PIN需要的r1,r2
     */
    public void getPin() {
        
        Request request = new Request.Builder().url("https://secure.ctb988.net/validate_pin.jsp?sml=m").addHeader("Cookie", "JSESSIONID=" + jsessionid).build();
        Call callCode = client.newCall(request);
        callCode.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Matcher matcher = Pattern.compile("(?<=var r\\d=').+(?=';)").matcher(body);     //分别提取r1,r2的正则表达式
                if(!matcher.find())
                    return;
                r1 = matcher.group();
                if(!matcher.find())
                    return;
                r2 = matcher.group();
                System.out.println("r1=" + r1);
                System.out.println("r2=" + r2);
                System.out.println("getPin success!!!");
                secondLogin();
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("getPin fail!!!");
            }
        });
        
    }
    
    /**
     * 登陆7.最后PIN的登陆
     */
    public void secondLogin() {
        pinCode = SHA1(r1 + r2 + SHA1("pin_" + uid + pinCode));
        RequestBody requestBody = new FormBody.Builder().add("code", pinCode).build();
        Request request = new Request.Builder().url("https://secure.ctb988.net/verifypin").post(requestBody).addHeader("Cookie", "JSESSIONID=" + jsessionid).build();
        Call callCode = client.newCall(request);
        callCode.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                if (body.contains("dispatch")) {
                    logger.info("登录成功");
                    ((JLabel) CitibetUtils.getComponentByName("test")).setText("登录成功");
                    getOrder();
                }
                else
                    logger.error("最终登录失败!!");
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("secondLogin失败!!");
            }
        });
    }
    
    /**
     * 登陆8.测试获取订单
     */
    public void getOrder() {
        Request request = new Request.Builder().url("http://web.ctb988.net/datastore?q=n&l=x&tnum=1&x=0.5897412358741256").addHeader("Cookie", "JSESSIONID=" + jsessionid).build();
        Call callCode = client.newCall(request);
        callCode.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                logger.debug("测试获取订单 完成!!!");
                BaseCitibet.setJsessionid(jsessionid);  //将登陆所确认的JSESSIONID送给所有http定时任务
                CitibetUtils.setJsessionid(jsessionid); //将登陆所确认的JSESSIONID工具类,因为里面有手动赌吃的功能
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("getOrder 失败!!!");
            }
        });
    }
    
    /**
     * JAVA库自带的SHA1加密
     * @param decript
     * @return
     */
    private String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public void testCode()
    {
        try {
            code = Recognition.recognize(codePath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TesseractException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("code=" + code);
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
    public void setClient(OkHttpClient client) {
        this.client = client;
    }
    public OkHttpClient getClient() {
        return client;
    }
    public String getJsessionid() {
        return jsessionid;
    }
}

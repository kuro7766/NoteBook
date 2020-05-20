package com.example.notebook.Util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTML最终版本,最完美测试版
 */
public class HTMLCrawler {
    static int lastLength = 0;

    public static void main(String[] a) throws InterruptedException {

        while (true) {
            new HTMLCrawler().url("https://newapi.ppkao.com/api/ExamApi/GetHotExamId?UserID=14990023&ksid=104583&UserToken=wHIVHWw35D4Sd4Tqap29ON3N%2f5pC%2b0Q%2bWFYg9jFPHIVKVqHSXW0TFg%3d%3d&source=Android&sign=a088d41e95a4526c47c5a009866c0e46")
                    .doGet(new GetListener() {
                        @Override
                        public void onSuccess(String s, List<String> strings) {
                            System.out.println(s);
                        }

                        @Override
                        public void onFail(String s) {

                        }
                    });
        }
    }


    public static final String UTF8 = "UTF-8";
    public static final String GBK = "GBK";
    public static final String GB2312 = "GB2312";

    private HashMap<String, String> parmas;
    private String url;
    private HashMap<String, String> requestProperties;
    private String charset;
    private String regex = null;

    public HTMLCrawler() {
        requestProperties = new HashMap<>();
        parmas = new HashMap<>();
    }

    public HTMLCrawler Charset(String charset) {
        this.charset = charset;
        return this;
    }

    public HTMLCrawler url(String url) {
        this.url = url;
        return this;
    }

    public HTMLCrawler Regex(String regex) {
        this.regex = regex;
        return this;
    }

    public HTMLCrawler Header(String key, String value) {
        requestProperties.put(key, value);
        return this;
    }

    /**
     * Accept-Encoding: gzip, deflate
     * Accept-Language: en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7
     *
     * @param raw
     * @return
     */
    public HTMLCrawler RawHeaders(String raw) {
        raw = raw.replaceAll("(\n$|^\n)", "");
        String[] o = raw.split("\n");
        HashMap<String, String> hashMap = new HashMap();
        for (String s : o) {
            if (s.matches("\\s*")) {
                continue;
            }
            try {
                String key = s.split(": ")[0];
                String value = s.split(": ")[1];
                hashMap.put(key, value);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("格式错误");
                return this;
            }
        }
        for (Map.Entry<String, String> e : hashMap.entrySet()) {
            Header(e.getKey(), e.getValue());
        }
        return this;
    }

    public HTMLCrawler Param(String key, String value) {
        parmas.put(key, value);
        return this;
    }

    public void doGet(GetListener listener) {
        URL urlmy = null;
        StringBuilder sbb = new StringBuilder();
        if (parmas.size() != 0) {
            if (!this.url.matches(".*\\?$"))
                sbb.append("?");
            for (Map.Entry entry : this.parmas.entrySet()) {
                sbb.append(entry.getKey() + "=" + entry.getValue());
                sbb.append("&");
            }
        }
        try {
            urlmy = new URL(url + sbb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFail(e.toString());
            return;
        }

        HttpURLConnection con = null;
        BufferedReader br = null;
        try {
            con = (HttpURLConnection) urlmy.openConnection();
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }

            HttpURLConnection.setFollowRedirects(true);
            con.setInstanceFollowRedirects(false);
            con.connect();
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), charset == null ? "GBK" : charset));
        } catch (IOException e) {
            e.printStackTrace();
            listener.onFail(e.toString());
            return;
        }

        String s = "";

        StringBuffer sb = new StringBuffer();

        while (true) {
            try {
                if ((s = br.readLine()) == null) break;
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                listener.onFail(e.toString());
                return;
            }
            sb.append(s);
        }
        listener.onSuccess(sb.toString(), regex == null ? null : Regex.findAll(sb.toString(), regex));

    }

    public void doPost(GetListener listener) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //1.获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
//            2.中文有乱码的需要将PrintWriter改为如下
//            out= new PrintWriter(conn.getOutputStream(),"unicode");
            // 发送请求参数
            StringBuilder sb = new StringBuilder();
            if (parmas.size() != 0) {
                for (Map.Entry entry : this.parmas.entrySet()) {
                    sb.append(entry.getKey() + "=" + entry.getValue());
                    sb.append("&");
                }
                out.print(sb.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset == null ? "GBK" : charset));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            listener.onFail(e.toString());
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                listener.onFail(ex.toString());
                ex.printStackTrace();
            }
        }
        listener.onSuccess(result, regex == null ? null : Regex.findAll(result, regex));
    }

    public interface GetListener {
        void onSuccess(String s, List<String> strings);

        void onFail(String s);
    }
}

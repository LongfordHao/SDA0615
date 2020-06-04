package com.tfjy.sda.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

/**
 * @description jsopu工具类
 * @Version:V1.0
 * @params
 * @return
 * @auther: 张兴军
 * @date: 2020/4/27 9:56
 */
public class JsoupUtil {
    public static String getHtmlPageResponse(String url) throws Exception {

        List<String> srcList = new ArrayList<String>();
        /** 创建模拟指定浏览器的客户端对象 */
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        /** JS执行出错不抛出异常 */
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        /** HTTP状态不是200时不抛出异常 */
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        /** 不启用CSS */
        webClient.getOptions().setCssEnabled(false);
        /** 启用JS(非常重要) */
        webClient.getOptions().setJavaScriptEnabled(true);
        /** 支持AJAX(非常重要) */
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        /** JS执行需要一定时间，设置等待时间(非常重要) */
        webClient.waitForBackgroundJavaScript(10000);
        // webClient.getOptions().setActiveXNative(false);
        // webClient.getOptions().setTimeout(10000);
        /** 加载网页 */
        HtmlPage page = webClient.getPage(url);
        // Thread.sleep(3000);
        /** 将加载的网页转换成XML形式 */
        String pageXml = page.asXml();

        webClient.close();
        return pageXml;
    }
}

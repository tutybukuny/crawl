package crawl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tutyb
 */
public class CrawlQuestions {

    public static void main(String[] args) {
        try {
            PrintWriter print = new PrintWriter("question.xml", "UTF-8");
            print.write("<root>\r\n");

            Document doc = Jsoup.parse(getHTML("http://vnexpress.net/interactive/2016/thi-sat-hach-lai-xe"));
            Elements elements = doc.select(".top_dethi span a");

            for (Element element : elements) {
                String link = "http://vnexpress.net" + element.attr("href");
                doc = Jsoup.parse(getHTML(link));
                Elements els = doc.select(".detail_cauhoi");

                for (Element el : els) {
                    print.write("\t<test>\r\n");
                    print.write("\t\t<question>" + el.getElementsByClass("noidung_cauhoi").first().text() + "</question>\r\n");
                    print.write("\t\t<answer>\r\n");
                    Elements lis = el.getElementsByTag("li");
                    for (Element li : lis) {
                        print.write("\t\t\t" + li.text() + "\r\n");
                    }
                    print.write("\t\t</answer>\r\n");
                    print.write("\t</test>\r\n");
                }
            }
            
            print.write("</root>");
            print.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(CrawlQuestions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getHTML(String linkForumThis) {
        String line;
        String html = "";
        try {

            URL url = new URL(linkForumThis);
            InputStream is = url.openStream();

            BufferedReader read = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            while ((line = read.readLine()) != null) {
                html += line + "\r\n";
            }

            read.close();
            is.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(CrawlQuestions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CrawlQuestions.class.getName()).log(Level.SEVERE, null, ex);
        }

        return html;
    }
}

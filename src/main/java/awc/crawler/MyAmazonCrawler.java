package awc.crawler;

import java.util.Optional;

import org.apache.http.client.fluent.Request;

public class MyAmazonCrawler
{
    public Optional<String> crawl (String url)
    {
        try {
            return Optional.ofNullable(
                    Request.Get(url)
                           .addHeader("accept-language", "en-US,en;q=0.9,zh-TW;q=0.8,zh;q=0.7")
                           .addHeader("user-agent",
                                      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36")
                           .execute().returnContent().asString()
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

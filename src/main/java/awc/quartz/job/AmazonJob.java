package awc.quartz.job;

import java.util.List;
import java.util.Optional;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import awc.crawler.MyAmazonCrawler;
import awc.dataparser.AmazonDataParser;
import awc.jobrepo.BasicAmazonJobRepo;
import awc.jobrepo.JobRepo;

public class AmazonJob implements Job
{
    private static final Logger LOG = LoggerFactory.getLogger(AmazonJob.class);
    private static final String baseURL = "https://nlp.netbase.com/sentiment?languageTag=en&mode=index&syntax=twitter&text=";

    public void execute (JobExecutionContext context)
    {
        JobRepo jobRepo = BasicAmazonJobRepo.getInstance();

        String url = jobRepo.getNextLink();
        while (url == null) {
            try {
                Thread.sleep(2000L);
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            url = jobRepo.getNextLink();
        }

        LOG.info(String.format("Received URL: %s", url));

        jobRepo.visited(url);

        // read in the files (from amazon, myself?)
        MyAmazonCrawler mac = new MyAmazonCrawler();
        Optional<String> optionalHtml = mac.crawl(url);

        if (optionalHtml.isPresent()) {
            AmazonDataParser adp = new AmazonDataParser(optionalHtml.get());
            List<String> outgoingLinks = adp.extractOutgoingLinks();
            LOG.info(String.format("Outgoing links: %s", outgoingLinks.toString()));
            jobRepo.addLinks(outgoingLinks);
        }
        else {
            LOG.info(String.format("Can't crawl %s", url));
        }
    }

    /*

        Entry pageEntry = new Entry();
        pageEntry.set("RECORD_ID", "page");
        pageEntry.set("RECORD_URL", url);
        pageEntry.set("RECORD_TITLE", parser.getName());
        pageEntry.set("META_TAGS2", "AmazonReview");

        // get product id
        StringBuffer productIDBuffer = new StringBuffer();
        int index = url.indexOf("/dp/") + 4;
        System.out.println(index);
        while (index < url.length() && url.charAt(index) != '/' && url.charAt(index) != '?') {
            productIDBuffer.append(url.charAt(index));
            index++;
        }

        pageEntry.set("META_TAGS", productIDBuffer.toString());
        System.out.println("META TAG: " + productIDBuffer.toString());
        AmazonScheduler.writer.println(pageEntry.toString());

        System.out.println("-------");
        System.out.println("title: " + parser.getName());
        System.out.println("-------");
        System.out.println("price: " + parser.getPrice());
        System.out.println("-------");



        System.out.println("alternate images: ");
        for (String s : parser.getAlternateImages()) {
            System.out.println(s);
        }

        System.out.println("reviews: ");

        int count = 0;
        for (Review r : parser.getReviews()) {
            String reviewText;
//            r.setRecordID("page" + seenPages + "-review" + count++);
            r.setProductID(productIDBuffer.toString());
            if (r.getReviewText().length() > 1000) {
                reviewText = r.getReviewText().substring(0, 1000); // truncate to 1000 characters
            } else {
                reviewText = r.getReviewText();
            }
            r.setReviewText(reviewText);
            String urlEncodedReview = ""; // encodeValue(reviewText);
            String requestURL = baseURL + urlEncodedReview;
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Entry reviewEntry = ReviewProcessor.getSentiment(r, requestURL);
            reviewEntry.set("META_TAGS2", "AmazonReview");
            System.out.println("---------");
            System.out.println(reviewEntry);
            System.out.println("---------");

            AmazonScheduler.writer.println(reviewEntry);
            AmazonScheduler.writer.flush();
        }
     */
}



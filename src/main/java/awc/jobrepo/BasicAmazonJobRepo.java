package awc.jobrepo;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BasicAmazonJobRepo implements JobRepo
{
    private static Queue<String> jobQueue = new ConcurrentLinkedQueue<>();

    private static Set<String> visited = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private static BasicAmazonJobRepo jobRepo = new BasicAmazonJobRepo("https://www.amazon.com/Echo-Dot/dp/B07FZ8S74R/");

    public static BasicAmazonJobRepo getInstance ()
    {
        return jobRepo;
    }

    private BasicAmazonJobRepo (String seedUrl)
    {
        addLink(seedUrl);
    }

    public String getNextLink ()
    {
        return jobQueue.poll();
    }

    public void visited (String link)
    {
        visited.add(link);
    }

    public void addLinks (List<String> links)
    {
        links.forEach(link -> this.addLink(link));
    }

    public void addLink (String link)
    {
        if (!visited.contains(link)) {
            jobQueue.offer(link);
        }
    }
}

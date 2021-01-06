package kz.malimov;

import java.time.LocalDateTime;
import java.util.Date;

public class Report {
    String name, id;
    int numOfArticles;
    long avg_content_length;
    long content_length;
    LocalDateTime published_from, published_to;

    public Report(String name, String id, int content_length, LocalDateTime published_from, LocalDateTime published_to) {
        this.name = name;
        this.id = id;
        this.content_length = content_length;
        this.published_from = published_from;
        this.published_to = published_to;
        numOfArticles = 1;
    }

    public void setAvgContentLength() {
        avg_content_length = content_length/numOfArticles;
    }

    @Override
    public String toString() {
        return "Report{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", avg_content_length=" + avg_content_length +
                ", published_from=" + published_from +
                ", published_to=" + published_to +
                '}';
    }
}

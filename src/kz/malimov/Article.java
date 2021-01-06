package kz.malimov;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class Article {
    int id;
    String source_id;
    String source_name, title, content;
    LocalDateTime published_at;

    public Article(int id, String source_id, String source_name, String title, String content, LocalDateTime published_at) {
        this.id = id;
        this.source_id = source_id;
        this.source_name = source_name;
        this.title = title;
        this.content = content;
        this.published_at = published_at;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", source_id='" + source_id + '\'' +
                ", source_name='" + source_name + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", published_at=" + published_at +
                '}';
    }
}

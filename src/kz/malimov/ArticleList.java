package kz.malimov;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ArticleList extends Thread {
    private ArrayList<Article> articles;
    ArrayList<Report> reports;
    private int articleNum;
    private String fileName;

    public ArticleList(String fileName) {
        this.fileName = fileName;
        articleNum = 0;
        reports = new ArrayList<>();
    }

    @Override
    public void run() {
        int countLine = 0;
        articles = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.UTF_8)) {
            String line = br.readLine();
            while (line != null){
                countLine++;
                if (countLine == 1){
                    line = br.readLine();
                }
                String[] attributes = line.split(",");
                if (attributes.length != 6) {
                    line = br.readLine();
                    continue;
                }
                Article article = createArticle(attributes);
                articles.add(article);
                articleNum++;
                line = br.readLine();
            }
            //getAll();
            produceReport();
            /*for (Report r: reports) {
                System.out.println(r);
            }*/
            //System.out.println(articleNum);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private Article createArticle(String[] attributes) {
        int id = 0;
        String source_id = "";
        String source_name = "";
        String title, content = "";
        LocalDateTime published_at;
        id = Integer.parseInt(attributes[0]);
        source_id = attributes[1];
        source_name = attributes[2];
        title = attributes[3];
        content = attributes[4];
        if (attributes[5].contains(".")){
            attributes[5] = attributes[5].split("\\.")[0];
        }else if (attributes[5].contains("+")){
            attributes[5] = attributes[5].split("\\+")[0];
        }
        else {
            attributes[5] = attributes[5].split("Z")[0];
        }
        attributes[5] = attributes[5].replaceAll("T", " ");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S");
        //DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
//        if (attributes[5].length() > 24){
//            published_at = LocalDateTime.parse(attributes[5], formatter3);
//        }
//        else
//        if(attributes[5].length() > 20){
//            published_at = LocalDateTime.parse(attributes[5], formatter2);
//        }else
        published_at = LocalDateTime.parse(attributes[5], formatter);
//        System.out.println(published_at);
        return new Article(id,source_id,source_name,title,content,published_at);
    }
    public void getAll(){
        for (Article a:
             articles) {
            System.out.println(a);
        }
    }
    private void produceReport() {
        boolean found;
        for (Article a: articles) {
            found = false;
            for (Report r: reports) {
                if (a.source_id.equals(r.id)) {
                    found = true;
                    r.numOfArticles++;
                    if (a.published_at.isAfter(r.published_to)) {
                        r.published_to = a.published_at;
                    }
                    if (a.published_at.isBefore(r.published_from)) {
                        r.published_from = a.published_at;
                    }
                    r.content_length += calculateContentLength(a.content);
                }
            }
            if (!found) {
                reports.add(new Report(a.source_name, a.source_id, calculateContentLength(a.content), a.published_at, a.published_at));
            }
        }
        for (Report r: reports) {
            r.setAvgContentLength();
        }
    }

    public int calculateContentLength(String content) {
        int len = 0;
        len += content.length();
        if (content.contains("chars]")) {
            String temp = content.split("\\[\\+")[1];
            temp = temp.split(" chars]")[0];
            len += Integer.parseInt(temp);
            len -= (9 + temp.length());
        }
        return len;
    }

    public synchronized void produceFinalReport(ArrayList<Report> finalReport) {
        boolean found;
        for (Report r: reports) {
            found = false;
            for (Report fr: finalReport) {
                if (r.id.equals(fr.id)) {
                    found = true;
                    fr.numOfArticles += r.numOfArticles;
                    if (r.published_to.isAfter(fr.published_to)) {
                        fr.published_to = r.published_to;
                    }
                    if (r.published_from.isBefore(fr.published_from)) {
                        fr.published_from = r.published_from;
                    }
                    fr.content_length += r.content_length;
                }
            }
            if (!found) {
                finalReport.add(r);
            }
        }
        for (Report r: finalReport) {
            r.setAvgContentLength();
        }
    }
}

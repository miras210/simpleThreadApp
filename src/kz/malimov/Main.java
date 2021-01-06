package kz.malimov;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        File[] csv = Objects.requireNonNull(new File("C:\\Users\\Miras\\Desktop\\java\\csv").listFiles());
        ArrayList<ArticleList> csvThreads = new ArrayList<>();
        for (File f: csv) {
            csvThreads.add(new ArticleList(f.getPath()));
        }
        for (ArticleList t: csvThreads) {
            t.start();
        }
        for (ArticleList t: csvThreads) {
            t.join();
        }
        ArrayList<Report> finalReport = new ArrayList<>();
        for (ArticleList al: csvThreads) {
            al.produceFinalReport(finalReport);
        }

        try {
            boolean needDataTitles = false;
            File myReport = new File("C:\\Users\\Miras\\Desktop\\java\\report.csv");
            if (myReport.createNewFile()) {
                needDataTitles = true;
            }
            PrintWriter writer = new PrintWriter(myReport);
            if (needDataTitles) {
                writer.write("name,ID,published_from,published_to,avg_content_length\n");
            }
            for (Report r: finalReport) {
                StringBuilder sb = new StringBuilder();
                sb.append(r.name);
                sb.append(",");
                sb.append(r.id);
                sb.append(",");
                sb.append(r.published_from);
                sb.append(",");
                sb.append(r.published_to);
                sb.append(",");
                sb.append(r.avg_content_length);
                sb.append("\n");
                writer.write(sb.toString());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

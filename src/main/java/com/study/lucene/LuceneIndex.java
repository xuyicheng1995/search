package com.study.lucene;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Optional;

public class LuceneIndex {

    public static void createIndex(String sourceFileDirectory, String indexDirectory, boolean userCompundFile) throws IOException {

        // 创建 索引存放路径
        Directory directory = FSDirectory.open(Paths.get(indexDirectory));
        // 创建 标准分析器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 是否使用符合索引格式
        config.setUseCompoundFile(userCompundFile);
        try(IndexWriter writer = new IndexWriter(directory, config)) {
            File sourceFilePath = new File(sourceFileDirectory);
            Optional.ofNullable(sourceFilePath.listFiles()).ifPresent(files -> {
                for (File file : files) {
                    if (file.isFile()) {
                        // 创建Document
                        Document document = new Document();
                        // 文档 名称
                        document.add(new TextField("fileName", file.getName(), Field.Store.YES));
                        // 文档 内容
                        document.add(new TextField("content", readFile(file), Field.Store.YES));
                        // 文档 路径
                        document.add(new StoredField("path", file.getPath()));
                        // 文档 文件大小
                        document.add(new NumericDocValuesField("size", getFileSize(file)));
                        // 写入索引
                        try {
                            writer.addDocument(document);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public static String readFile(File file) {
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long getFileSize(File file) {
        return file.length();
    }
}

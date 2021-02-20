package com.study.lucene.testcase;

import com.study.lucene.LuceneIndex;

import java.io.IOException;

public class TestCreateIndex {

    public static void main(String[] args) throws IOException {
        String sourceFileDirectory = "D:\\develop\\code\\search\\lucene\\data";
        String indexDirectory = "D:\\develop\\code\\search\\lucene\\index";
        boolean userCompundFile = false;
        LuceneIndex.createIndex(sourceFileDirectory, indexDirectory, userCompundFile);
    }
}

package com.zongwu33.test;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zongwu
 * on 26/03/2018.
 * 将人民日报 带标注语料转换为 仅分词语料
 */
public class CreateSimpleCorpus {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("args length <2!");
            return;
        }
        String originFile = args[0];
        String resultFile = args[1];
        if (StringUtils.equals(originFile, resultFile)) {
            System.out.println("args can not be the same!!");
            return;
        }
        File outFile = new File(resultFile);
        if (outFile.exists()) {
            System.out.println("warning! out file exist!");
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(originFile)));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                List<String> wordList = new LinkedList<String>();
                Pattern pattern = Pattern.compile("(\\[(([^\\s]+/[0-9a-zA-Z]+)\\s+)+?([^\\s]+/[0-9a-zA-Z]+)]/?[0-9a-zA-Z]+)|([^\\s]+/[0-9a-zA-Z]+)");
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String single = matcher.group();
                    if (single.startsWith("[") && !single.startsWith("[/")) {
                        List<String> words = createComb(single);
                        wordList.addAll(words);
                    } else {
                        String word = createSingle(single);
                        wordList.add(word);
                    }
                }

                save(wordList, writer);
            }
            writer.flush();
            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //[中央/n	人民/n	广播/vn	电台/n]/nt
    private static List<String> createComb(String param) {
        if (param == null) return null;
        int cutIndex = param.lastIndexOf(']');
        if (cutIndex <= 2 || cutIndex == param.length() - 1) return null;
        String wordParam = param.substring(1, cutIndex);
        List<String> wordList = new LinkedList<>();
        for (String single : wordParam.split("\\s+")) {
            if (single.length() == 0) continue;
            wordList.add(createSingle(single));
        }
        return wordList;
    }

    //	朋友/n	们/k	，/w	致以/v
    private static String createSingle(String rawWord) {
        int cutIndex = rawWord.lastIndexOf('/');
        return rawWord.substring(0, cutIndex);
    }

    private static void save(List<String> segments, BufferedWriter writer) throws IOException {
        String content = StringUtils.join(segments, " ") + '\n';
        System.out.println(content);
        writer.append(content);
    }
}

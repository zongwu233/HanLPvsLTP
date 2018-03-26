package com.zongwu33.test;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.perceptron.PerceptronSegmenter;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;

/**
 * Created by zongwu
 * on 23/03/2018.
 */
public class TestForSIGHan2005 {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("args length <2!");
            return;
        }
        String testFile = args[0];
        String resultFile = args[1];
        File outFile = new File(resultFile);
        if (outFile.exists()) {
            System.out.println("warning! out file exist!");
            return;
        }
        try {
            PerceptronSegmenter segmenter = new PerceptronSegmenter(
                    HanLP.Config.PerceptronCWSModelPath);
            BufferedReader reader = new BufferedReader(new FileReader(new File(testFile)));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                if (StringUtils.isBlank(line)) continue;
                List<String> segments = segmenter.segment(line);
                save(segments, writer);
            }
            writer.flush();
            writer.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save(List<String> segments, BufferedWriter writer) throws IOException {
        String content = StringUtils.join(segments, " ") + '\n';
        writer.append(content);
    }
}

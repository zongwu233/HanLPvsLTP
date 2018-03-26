package com.zongwu33.test;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.perceptron.PerceptronSegmenter;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by zongwu
 * on 26/03/2018.
 */
public class PerformanceTest {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("args length <1!");
            return;
        }
        String testFile = args[0];
        try {
            long stt = System.currentTimeMillis();
            PerceptronSegmenter segmenter = new PerceptronSegmenter(
                    HanLP.Config.PerceptronCWSModelPath);

            //触发模型的懒加载
            segmenter.segment("商品和服务");
            long edd = System.currentTimeMillis();
            System.out.println("init model: " + (edd - stt));

            BufferedReader reader = new BufferedReader(new FileReader(new File(testFile)));
            String line = null;
            long totalTime = 0L;
            long num = 0;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isBlank(line)) continue;
                long start = System.currentTimeMillis();
                segmenter.segment(line);
                long end = System.currentTimeMillis();
                ++num;
                totalTime += (end - start);
            }
            reader.close();
            System.out.println("total time:" + totalTime);
            System.out.println("total num:" + num);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

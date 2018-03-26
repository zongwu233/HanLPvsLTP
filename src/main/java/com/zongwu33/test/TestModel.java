package com.zongwu33.test;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.perceptron.PerceptronSegmenter;

/**
 * Created by zongwu
 * on 23/03/2018.
 */
public class TestModel {
    public static void main(String[] args) {
        try {
            PerceptronSegmenter segmenter = new PerceptronSegmenter(
                    HanLP.Config.PerceptronCWSModelPath);
            System.out.println(segmenter.segment("商品和服务"));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

    }
}

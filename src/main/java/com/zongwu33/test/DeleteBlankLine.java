package com.zongwu33.test;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * Created by zongwu
 * on 23/03/2018.
 *
 * 删除小说里的空白行，存在多个空行的话，LTP会终止处理
 */
public class DeleteBlankLine {

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
            BufferedReader reader = new BufferedReader(new FileReader(new File(testFile)));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isBlank(line)) continue;
                save(line, writer);
            }
            writer.flush();
            writer.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save(String content, BufferedWriter writer) throws IOException {
        writer.append(content+'\n');
    }
}

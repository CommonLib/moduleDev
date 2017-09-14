package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author:dongpo 创建时间: 8/29/2016
 * 描述:
 * 修改:
 */
public class DimenUtil {

    public static void gen() {

        /**
         * Androidstudio： ./app/src/main/res/values/dimens.xml
         * Eclipse： ./res/values/dimens.xml
         *
         * /F:/work/MyWorkSpace/TestYouku/
         * */
        //获取当前Classes 的绝对路径
        String filePath = Class.class.getClass().getResource("/").getPath();
        System.out.println(filePath);

        System.out.println(System.getProperty("user.dir"));
        File file = new File("C:\\Users\\byang059\\AndroidStudioProjects\\eApplication\\app\\src\\main\\res\\values\\dimens.xml");//这里如果找不到,使用绝对路径即可
        BufferedReader reader = null;
        //添加分辨率
        StringBuilder sw480 = new StringBuilder();
        /*StringBuilder sw600 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();
        StringBuilder sw800 = new StringBuilder();
        StringBuilder  w820 = new StringBuilder();*/

        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束

            while ((tempString = reader.readLine()) != null) {

                if (tempString.contains("</dimen>")) {
                    //tempString = tempString.replaceAll(" ", "");
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    //<dimen name = "temotew">25dp</dimen>
                    int num = Integer.valueOf(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));
                    String des = tempString.substring(tempString.indexOf("\"") + 1, tempString.lastIndexOf("\""));
                    if(des.contains("width")){
                        sw480.append(start).append((int) Math.round(num * 1.11)).append(end).append("\n");
                    }else{
                        sw480.append(start).append((int) Math.round(num * 1.11)).append(end).append("\n");
                    }
                    //主要核心就再这里了，如下3种分辨率分别缩小 0.6、0.75、0.9倍(根据实际情况自己随意DIY)
                    /*sw600.append(start).append((int) Math.round(num * 0.75)).append(end).append("\n");
                    sw720.append(start).append((int) Math.round(num * 0.9)).append(end).append("\n");
                    sw800.append(tempString).append("\n");
                    w820.append(tempString).append("\n");*/

                } else {
                    sw480.append(tempString).append("\n");
                    /*sw600.append(tempString).append("\n");
                    sw720.append(tempString).append("\n");
                    sw800.append(tempString).append("\n");
                    w820.append(tempString).append("\n");*/
                }
                line++;
            }
            reader.close();
            System.out.println("<!--  1200 x 1920 -->");
            System.out.println(sw480);
            /*System.out.println("<!--  sw600 -->");
            System.out.println(sw600);

            System.out.println("<!--  sw720 -->");
            System.out.println(sw720);
            System.out.println("<!--  sw800 -->");
            System.out.println(sw800);
            System.out.println("<!--  sw820 -->");
            System.out.println(w820);*/

            String sw480file = "C:\\Users\\byang059\\AndroidStudioProjects\\eApplication\\app\\src\\main\\res\\values-2048x1536\\dimens.xml";
            /*String sw600file = "./app/src/main/res/values-sw600dp-land/dimens.xml";
            String sw720file = "./app/src/main/res/values-sw720dp-land/dimens.xml";
            String sw800file = "./app/src/main/res/values-sw800dp-land/dimens.xml";
            String w820file = "./app/src/main/res/values-w820dp/dimens.xml";*/
            writeFile(sw480file, sw480.toString());
           /* writeFile(sw600file, sw600.toString());
            writeFile(sw720file, sw720.toString());
            writeFile(sw800file, sw800.toString());
            writeFile(w820file, w820.toString());*/
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
            File target = new File(file);
            if(!target.getParentFile().exists()){
                target.getParentFile().mkdirs();
            }
            if (!target.exists()) {
                target.createNewFile();
            }
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }

    public static void main(String[] args) {
        gen();
    }
}

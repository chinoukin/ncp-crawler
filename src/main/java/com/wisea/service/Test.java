package com.wisea.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
//
//    final static String text = "<span>hello world</span></span>JAVA REGEX TEST<span>chinoukin!!!</span>";
//    final static String regex = "<span>.*?</span>";
//
//    public static void main(String[] args) {
//        Pattern r = Pattern.compile(regex);
//        Matcher m = r.matcher(text);
//        while (m.find()) {
//            String res = m.group();
//            System.out.println(res);
//        }
//    }

    final static String text = "<b>999</b>www.chinoukin.com&<b>888</b>";
    final static String regex = "<b>\\d*</b>";

    public static void main(String[] args) {
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(text);
        while (m.find()) {
            String res = m.group();
            System.out.println(res);
        }
    }


}

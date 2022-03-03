package com.hzf.study.program.nowcoder;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author zhuofan.han
 * @date 2022/3/3
 */
public class Array3MinNum {

    // 给定一个int数组 请从改数组中选择3个最小数字并输出
    // 输入 21,30,62,5,31
    // 输出 21305
    // 5,21
    // 215
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] split = scanner.nextLine().split(",");
            Object objects =
                Arrays.stream(split).mapToInt(Integer::parseInt).sorted().limit(3).mapToObj(String::valueOf)
                    .sorted((a, b) -> (a + b).compareTo(b + a)).reduce("", (a, b) -> a + b);
            System.out.println(objects);
        }
    }
}

package com.hzf.study.program.huawei;

/**
 * @author zhuofan.han
 * @date 2022/1/28
 */
public class LongestCommonStr {
    public static void main(String[] args) {
        LongestCommonStr commonStr = new LongestCommonStr();
        System.out.println(commonStr.LCS("2LQ74WK8Ld0x7d8FP8l61pD7Wsz1E9xOMp920hM948eGjL9Kb5KJt80","U08U29zzuodz16CBZ8xfpmmn5SKD80smJbK83F2T37JRqYfE76vh6hrE451uFQ100ye9hog1Y52LDk0L52SuD948eGjLz0htzd5YF9J1Y6oI7562z4T2"));
    }

    /**
     * longest common substring
     * @param str1 string字符串 the string
     * @param str2 string字符串 the string
     * @return string字符串
     */
    public String LCS (String str1, String str2) {
        // write code here
        int max = 0, maxIndex = 0, tmp = 0, c1Off = 0, c2Off = str2.length() - 1, range = 1;
        for (int i = 1; i < str1.length() + str2.length(); i++) {
            for (int j = 0; j < range; j++) {
                if (str1.charAt(j + c1Off) == str2.charAt(c2Off + j)) {
                    tmp++;
                } else {
                    if (tmp > max) {
                        max = tmp;
                        maxIndex = j + c1Off;
                    }
                    tmp = 0;
                }
                if (j == range - 1 && tmp > max) {
                    max = tmp;
                    maxIndex = j + c1Off + 1;
                }
            }
            tmp = 0;
            if (c2Off > 0) {
                c2Off--;
                if (range < str1.length()) range++;
            } else {
                c1Off++;
                range--;
            }
        }
        return str1.substring(maxIndex - max, maxIndex);
    }

    public String LCS2(String str1, String str2) {
        int start = 0;
        int end = 1; //初始条件：subString(0, 1)，左闭右开，即只取第一个第0个字符
        String result = "";
        while(end <= str2.length()){ //subString左闭右开，所以可以取到等于
            String subStr = str2.substring(start, end);
            if(str1.contains(subStr)){
                result = subStr; // str1包含str2的该子串，例如:ab，继续右移看是否继续包含abc，所以end右移
            }else{
                start++; //str1不包含str2的该子串，例如:13，不必再找以13开头的子串了，因为不包含13，肯定也不包含134，所以start右移；因为需要找最长子串，start右移后，end也需要右移，保证长度不会缩短
            }
            end++; //上述两种情况，均需要end右移
        }
        return result;
    }
}
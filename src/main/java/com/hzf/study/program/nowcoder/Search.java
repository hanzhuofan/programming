package com.hzf.study.program.nowcoder;

/**
 * @author zhuofan.han
 * @date 2022/3/2
 */
public class Search {

    /**
     * 有一个长度为 n 的按严格升序排列的整数数组 nums ，在实行 search 函数之前，在某个下标 k 上进行旋转，使数组变为[nums[k],nums[k+1],.....,nums[nums.length-1],nums[0],nums[1],.......,nums[k-1]]。
     * 给定旋转后的数组 nums 和一个整型 target ，请你查找 target 是否存在于 nums 数组中并返回其下标（从0开始计数），如果不存在请返回-1。
     *
     *
     * @param nums int整型一维数组
     * @param target int整型
     * @return int整型
     */
    public static int search (int[] nums, int target) {
        // write code here
        if (nums.length < 5) {
            for (int i = 0; i < nums.length; i++) {
                if (target == nums[i]) return i;
            }
        } else {
            int left = 0;
            int right = nums.length - 1;
            while (left < right) {
                if (nums[left] == target) return left;
                if (nums[right] == target) return right;
                int mid = (right - left) / 2 + left;
                if (nums[mid] == target) return mid;

                if (nums[left] < nums[mid]) {
                    if (nums[left] < target && nums[mid] > target) {
                        right = mid - 1;
                    } else {
                        left = mid + 1;
                    }
                } else {
                    if (nums[right] > target && nums[mid] < target) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
            }
            if (nums[left] == target) return left;
            if (nums[right] == target) return right;
        }

        return -1;
    }

    /**
     * 请实现有重复数字的升序数组的二分查找
     * 给定一个 元素有序的（升序）长度为n的整型数组 nums 和一个目标值 target  ，写一个函数搜索 nums 中的第一个出现的target，如果目标值存在返回下标，否则返回 -1
     *
     * 如果目标值存在返回下标，否则返回 -1
     * @param nums int整型一维数组
     * @param target int整型
     * @return int整型
     */
    public static int search2(int[] nums, int target) {
        // write code here
        if (nums.length == 0) return -1;
        if (nums[0] == target) return 0;
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                if (nums[mid - 1] != target) {
                    return mid;
                } else {
                    right = mid - 1;
                }
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(search(new int[]{6,8,10,1,2,4}, 1));
    }
}

package dev.ravikamble.arrays.twosum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HashMapSolution implements TwoSumProblem {
    @Override
    public int[] findIndices(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        int[] output = new int[2];

        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                output[0] = map.get(nums[i]);
                output[1] = i;
                break;
            } else {
                map.put(target - nums[i], i);
            }
        }
        return output;
    }

    public static void main(String[] args) {
        TwoSumProblem twoSum = new HashMapSolution();
        int[] nums = {3, 2, 4};
        int target = 10;
        System.out.println(Arrays.toString(twoSum.findIndices(nums, target)));
    }
}

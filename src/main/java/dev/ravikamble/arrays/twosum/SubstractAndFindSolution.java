package dev.ravikamble.arrays.twosum;

import java.util.Arrays;

public class SubstractAndFindSolution implements TwoSumProblem {
    @Override
    public int[] findIndices(int[] nums, int target) {
        int[] output = new int[2];
        boolean found = false;
        for(int i = 0; i < nums.length - 1; i++){
            for(int j  = i + 1; j < nums.length; j++){
                if(nums[i] + nums[j] == target){
                    output[0] = i;
                    output[1] = j;
                    found = true;
                    break;
                }
            }
            if(found){
               break;
            }
        }
        return output;
    }

    public static void main(String[] args) {
        TwoSumProblem twoSum = new SubstractAndFindSolution();
        int[] nums = {3, 2, 4};
        int target = 6;
        System.out.println(Arrays.toString(twoSum.findIndices(nums, target)));
    }
}

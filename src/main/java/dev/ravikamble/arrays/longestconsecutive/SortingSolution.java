package dev.ravikamble.arrays.longestconsecutive;

import java.util.Arrays;

public class SortingSolution implements LongestConsecutiveSequence{
    public static void main(String[] args) {
        LongestConsecutiveSequence l = new  SortingSolution();
        System.out.println(l.longestConsecutive(new int[]{1,2,3,4,5,6,7,8,9}));
        System.out.println(l.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
        System.out.println(l.longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}));
        System.out.println(l.longestConsecutive(new int[]{-1, -2, 0, 1, 9}));
    }

    @Override
    public int longestConsecutive(int[] input) {
        Arrays.sort(input);
        int longestSeq = 0;
        int temp = 1;
        for(int i = 0; i < input.length - 1; i++){
            if((input[i] + 1) == input[i + 1]){
                temp++;
                longestSeq = Math.max(temp, longestSeq);
            }else{
                temp = 1;
            }
        }
        return longestSeq;
    }
}

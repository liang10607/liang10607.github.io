package com.liang.review;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Author:bernie-mac
 * Data:2021/4/21 17:44
 * Description: com.liang.review
 */
public class StuctTest {

    @Test
    public void testArrayList(){
        int[] aryy= new int[]{39,237,13,98,623,1,45,21,6,9};
                //,3,2,72,29,14,146,32,46,135,224,767,92,12};
        int[] sortArry = shellSort(aryy);
        for (int i = 0; i < aryy.length; i++) {
            System.out.print(sortArry[i]+"，");
        }
    }


    private int[] testBubleMaxSortTest(int[] arr){
        int count =0;
        for (int i = 0; i < arr.length - 1; i++) {
            boolean flag =false;
            for (int j = 0; j < arr.length - 1; j++) {
                if (arr[j] > arr[j+1]){
                    flag =true;
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] =temp;
                }
                count ++;
            }
            if (!flag){
                break;
            }
        }
        return arr;
    }


    private int[] testChooseMaxSortTest(int[] arr) {
        int curMaxIndex = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            curMaxIndex = i;
            for (int j = i + 1; j < arr.length ; j++) {
                if (arr[j] > arr[curMaxIndex]) {
                    curMaxIndex = j;
                }
            }
            int temp = arr[curMaxIndex];
            arr[curMaxIndex] = arr[i];
            arr[i] = temp;

        }
        return arr;
    }


    private int[] testInsertMaxSortTest(int[] arr) {
        int insertIndex;
        for (int i = 1; i < arr.length ; i++) {
            insertIndex =i-1;
            int tempValue = arr[i];
            while (insertIndex>=0&&arr[insertIndex]<tempValue){
                arr[insertIndex+1] = arr[insertIndex];
                insertIndex--;
            }
            arr[insertIndex+1] = tempValue;
        }
        return arr;
    }



    // 修改于 2019-03-06
    private int[] shellSort(int[] arr) {
        int len = arr.length;
        int count=0;
        for(int gap = Math.abs(len / 2); gap > 0; gap = Math.abs(gap / 2)) {
            // 注意：这里和动图演示的不一样，动图是分组执行，实际操作是多个分组交替执行
//        int gap =1;
            System.out.print("分组Gap:"+gap +"\n");

            for(int i = gap; i < len; i++) {
                int j = i;
                int current = arr[i];
                while(j - gap >= 0 && current < arr[j - gap]) {
                    arr[j] = arr[j - gap];
                    j = j - gap;
                    count ++ ;
                }
                arr[j] = current;
            }
        }
        System.out.println("交换次数：--"+count);
        return arr;
    }

}

[回目录页](..)

# 排序算法
 
  常见的排序算法主要包括：
  
  * 冒泡排序
  
  * 选择排序
  
  * 插入排序
  
  * 希尔排序
  
  * 快速排序
  
  * 归并排序
  
# 1. 冒泡排序
   
   两个for循环；
   
   内层实现相邻元素的循环交换， 内层每次循环完成都会把某一个最大的值移动到前面。
   
   外层循环没执行一次，则让内层完成完成一个数的排序，所有外层循环完成，则排序完成。
   
   **具体代码：**
   
```
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
                //最后一次内循环，已经无需交换，则停止排序
                break;
            }
        }
        return arr;
    }

```

# 2. 选择排序

  每次内循环把最值与前面的值调换位置，外循环每次找出一个最值直到所有值都被查过一次放到相应的位置。
  
```
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
```  

# 3. 插入排序

   先把前面的排序后， 后面的一个个插入到前面排序号的位置中
   
```
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
```   
   
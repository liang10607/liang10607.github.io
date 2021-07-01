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

# 希尔排序

  思路：在插入排序基础上，包一层for循环，通过这个for循环得gap=length/2逐步递减的步长来堆数据进行分组插入排序。

  当步长gap=1的时候，整个希尔排序就相当于一个插入排序。前面的分组排序过程中，大部分数据组已经有了一定顺序，最后一轮就是微调顺序，


```
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
```

# 堆排序算法

堆排序是一种树形选择排序，是对直接选择排序的有效改进。

**堆的定义下:** 具有n个元素的序列 （h1,h2,...,hn),当且仅当满足（h(i)>=h(2i+1),hi>=h(2i+2)）或（hi<=h(2i+1),h(i)<=h(2i+2)） (i=1,2,...,n/2)时称之为堆。  
在这里只讨论满足前者条件的堆。由堆的定义可以看出，堆顶元素（即第一个元素）必为最大项（大顶堆）。完全二叉树可以很直观地表示堆的结构。堆顶为根，其它为左子树、右子树。

**思想:**
* 初始时把要排序的数的序列看作是一棵顺序存储的二叉树，调整它们的存储序，使之成为一个 堆，这时堆的根节点的数最大。
* 然后将根节点与堆的最后一个节点交换。然后对前面(n-1)个数重新调整使之成为堆。
* 依此类推，直到只有两个节点的堆，并对 它们作交换，最后得到有n个节点的有序序列。
* 从算法描述来看，堆排序需要两个过程，一是建立堆，二是堆顶与堆的最后一个元素交换位置。所以堆排序有两个函数组成。一是建堆的渗透函数，二是反复调用渗透函数实现排序的函数。

**堆排序的时间复杂度是O(nLog(n))

```
public static void heapSort(int[] a){
    int arrayLength = a.length;
    // 循环建堆
    for (int i = 0; i < arrayLength - 1; i++) {
        // 建堆
        buildMaxHeap(a, arrayLength - 1 - i);
        // 交换堆顶和最后一个元素
        swap(a, 0, arrayLength - 1 - i);
        System.out.println(Arrays.toString(a));
    }
}
// 对data数组从0到lastIndex建大顶堆
public static void buildMaxHeap(int[] data, int lastIndex) {
    // 从lastIndex处节点（最后一个节点）的父节点开始
    for (int i = (lastIndex - 1) / 2; i >= 0; i--) {
        // k保存正在判断的节点
        int k = i;
        // 如果当前k节点的子节点存在
        while (k * 2 + 1 <= lastIndex) {
            // k节点的左子节点的索引
            int biggerIndex = 2 * k + 1;
            // 如果biggerIndex小于lastIndex，即biggerIndex+1代表的k节点的右子节点存在
            if (biggerIndex < lastIndex) {
                // 若果右子节点的值较大
                if (data[biggerIndex] < data[biggerIndex + 1]) {
                    // biggerIndex总是记录较大子节点的索引
                    biggerIndex++;
                }
            }
            // 如果k节点的值小于其较大的子节点的值
            if (data[k] < data[biggerIndex]) {
                // 交换他们
                swap(data, k, biggerIndex);
                // 将biggerIndex赋予k，开始while循环的下一次循环，重新保证k节点的值大于其左右子节点的值
                k = biggerIndex;
            } else {
                break;
            }
        }
    }
}
// 交换
private static void swap(int[] data, int i, int j) {
    int tmp = data[i];
    data[i] = data[j];
    data[j] = tmp;
}
```

# 快速排序

  通过一趟排序将待排序记录分割成独立的两部分，其中一部分记录的关键字均比另一部分关键字小，则分别对这两部分继续进行排序，直到整个序列有序。
  
```
/**
 * 快速排序
 * 
 * @param numbers
 *            带排序数组
 */
public static void quick(int[] numbers) {
    if (numbers.length > 0) // 查看数组是否为空
    {
        quickSort(numbers, 0, numbers.length - 1);
    }
}
/**
 * 
 * @param numbers
 *            带排序数组
 * @param low
 *            开始位置
 * @param high
 *            结束位置
 */
public static void quickSort(int[] numbers, int low, int high) {
    if (low >= high) {
        return;
    }
    int middle = getMiddle(numbers, low, high); // 将numbers数组进行一分为二
    quickSort(numbers, low, middle - 1); // 对低字段表进行递归排序
    quickSort(numbers, middle + 1, high); // 对高字段表进行递归排序
}
/**
 * 查找出中轴（默认是最低位low）的在numbers数组排序后所在位置
 * 
 * @param numbers
 *            带查找数组
 * @param low
 *            开始位置
 * @param high
 *            结束位置
 * @return 中轴所在位置
 */
public static int getMiddle(int[] numbers, int low, int high) {
    int temp = numbers[low]; // 数组的第一个作为中轴
    while (low < high) {
        while (low < high && numbers[high] > temp) {
            high--;
        }
        numbers[low] = numbers[high];// 比中轴小的记录移到低端
        while (low < high && numbers[low] < temp) {
            low++;
        }
        numbers[high] = numbers[low]; // 比中轴大的记录移到高端
    }
    numbers[low] = temp; // 中轴记录到尾
    return low; // 返回中轴的位置
}
```  

# 归并排序

  归并（Merge）排序法是将两个（或两个以上）有序表合并成一个新的有序表，即把待排序序列分为若干个子序列，每个子序列是有序的。然后再把有序子序列合并为整体有序序列。
  
```
/**
 * 归并排序
 * 简介:将两个（或两个以上）有序表合并成一个新的有序表 即把待排序序列分为若干个子序列，每个子序列是有序的。然后再把有序子序列合并为整体有序序列
 * 时间复杂度为O(nlogn)
 * 稳定排序方式
 * @param nums 待排序数组
 * @return 输出有序数组
 */
public static int[] sort(int[] nums, int low, int high) {
    int mid = (low + high) / 2;
    if (low < high) {
        // 左边
        sort(nums, low, mid);
        // 右边
        sort(nums, mid + 1, high);
        // 左右归并
        merge(nums, low, mid, high);
    }
    return nums;
}
/**
 * 将数组中low到high位置的数进行排序
 * @param nums 待排序数组
 * @param low 待排的开始位置
 * @param mid 待排中间位置
 * @param high 待排结束位置
 */
public static void merge(int[] nums, int low, int mid, int high) {
    int[] temp = new int[high - low + 1];
    int i = low;// 左指针
    int j = mid + 1;// 右指针
    int k = 0;
    // 把较小的数先移到新数组中
    while (i <= mid && j <= high) {
        if (nums[i] < nums[j]) {
            temp[k++] = nums[i++];
        } else {
            temp[k++] = nums[j++];
        }
    }
    // 把左边剩余的数移入数组
    while (i <= mid) {
        temp[k++] = nums[i++];
    }
    // 把右边边剩余的数移入数组
    while (j <= high) {
        temp[k++] = nums[j++];
    }
    // 把新数组中的数覆盖nums数组
    for (int k2 = 0; k2 < temp.length; k2++) {
        nums[k2 + low] = temp[k2];
    }
}
```

# 各个排序比较

![avatar](/image/sort_ratio_compare.png)
  


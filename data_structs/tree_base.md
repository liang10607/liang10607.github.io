[回目录页](..)

# 1. 树的基础

### 1.1 树的基本术语

* **结点的度**：结点拥有的子树的数目。二叉树节点的度最多是2

* **叶子**：度为零的结点。 树尾部的节点

* **分支结点**：度不为零的结点。

* **树的度**：树中结点的最大的度。

* **层次**：根结点的层次为1，其余结点的层次等于该结点的双亲结点的层次加1。若一个结点有子树，那么该结点称为子树根的"双亲"

* **树的高度**：树中结点的最大层次。

* **无序树**：如果树中结点的各子树之间的次序是不重要的，可以交换位置。

* **有序树**：如果树中结点的各子树之间的次序是重要的, 不可以交换位置。

* **森林**：0个或多个不相交的树组成。对森林加上一个根，森林即成为树；删去根，树即成为森林。

### 2 二叉树

#### 2.1 简介

   二叉树是每个节点最多有两个子树的树结构。它有五种基本形态：二叉树可以是空集；根可以有空的左子树或右子树；或者左、右子树皆为空。

![avatar](/image/binary_tree_base.jpg)

二叉树有以下几个性质：

**性质1：** 二叉树第i层上的结点数目最多为$2^{i-1}$ (i≥1)。

**性质2：** 深度为k的二叉树至多有$2^k - 1$个结点(k≥1)。

**性质3：** 包含n个结点的二叉树的高度至少为$log_2(n+1)$。

**性质4：** 在任意一棵二叉树中，若叶子结点的个数为n0，度为2的结点数为n2，则n0=n2+1。

#### 2.2 满二叉树

**定义：** 高度为h，并且由$2^{h}-1$个结点的二叉树，被称为满二叉树。

#### 2.3 完全二叉树

**定义：**  一颗二叉树众，只有最下两层的度小于2，且最下层的叶子结点集中再树的左部。

![avatar](/image/all_binary_tee.jpg)

#### 2.4 二叉查找树

**定义**：

```
二叉查找树(Binary Search Tree)，又被称为二叉搜索树。设x为二叉查找树中的一个结点，x节点包含关键字key，节点x的key值记为key[x]。如果y是x的左子树中的一个结点，则key[y] <= key[x]；如果y是x的右子树的一个结点，则key[y] >= key[x]。
```

(1) 若任意节点的左子树不空，则左子树上所有结点的值均小于它的根结点的值；

(2) 任意节点的右子树不空，则右子树上所有结点的值均大于它的根结点的值；

(3) 任意节点的左、右子树也分别为二叉查找树。

(4) 没有键值相等的节点（no duplicate nodes）。

总结起来就是：若子树不为空，则父节点一定大于左子树，并小于右子树； 并且不存在健值相等的节点。

![avatar](/image/binary_search_tree.jpg)

# 3. 二叉查找树

### 3.1 二叉查找树的java类

```
public class BSTree<T extends Comparable<T>> {

    private BSTNode<T> mRoot;    // 根结点

    public class BSTNode<T extends Comparable<T>> {
        T key;                // 关键字(键值)
        BSTNode<T> left;      // 左孩子
        BSTNode<T> right;     // 右孩子
        BSTNode<T> parent;    // 父结点

        public BSTNode(T key, BSTNode<T> parent, BSTNode<T> left, BSTNode<T> right) {
            this.key = key;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
    }

        ......
}
```

### 3.2 二叉查找树的深度遍历

 根据根节点的访问时机，分为前序，中序，后序遍历三种形态：
 
**前序遍历** 
```
/**
(01) 访问根结点；
  
  (02) 先序遍历左子树；
  
  (03) 先序遍历右子树。
**/
private void preOrder(BSTNode<T> tree) {
    if(tree != null) {
        System.out.print(tree.key+" "); //先访问根节点
        preOrder(tree.left);
        preOrder(tree.right);
    }
}

public void preOrder() {
    preOrder(mRoot);
} 
```

**中序遍历** 

 按我自己理解的中序就是：先左到底，左到不能在左了就停下来并打印该节点，然后返回到该节点的上一节点，并打印该节点，然后再访问该节点的右子树，再左到不能再左了就停下来

```
/**
(01) 中序遍历左子树；

(02) 访问根结点；

(03) 中序遍历右子树。
**/
private void inOrder(BSTNode<T> tree) {
    if(tree != null) {
        inOrder(tree.left);
        System.out.print(tree.key+" ");  //根节点在中间被访问
        inOrder(tree.right);
    }
}

public void inOrder() {
    inOrder(mRoot);
} 
```


**后序序遍历** 

```
/**
(01) 后序遍历左子树；

(02) 后序遍历右子树；

(03) 访问根结点。
**/
private void postOrder(BSTNode<T> tree) {
    if(tree != null)
    {
        postOrder(tree.left);
        postOrder(tree.right);
        System.out.print(tree.key+" ");  //根节点在最后才会被访问
    }
}

public void postOrder() {
    postOrder(mRoot);
} 
```


### 3.3 二叉查找树的Key查找

**递归版本的代码**

```
/*
 * (递归实现)查找"二叉树x"中键值为key的节点
 */
private BSTNode<T> search(BSTNode<T> x, T key) {
    if (x==null)
        return x;

    int cmp = key.compareTo(x.key);
    if (cmp < 0)
        return search(x.left, key);
    else if (cmp > 0)
        return search(x.right, key);
    else
        return x;
}

public BSTNode<T> search(T key) {
    return search(mRoot, key);
}

```

**非递归版本**

```
/*
 * (非递归实现)查找"二叉树x"中键值为key的节点
 */
private BSTNode<T> iterativeSearch(BSTNode<T> x, T key) {
    while (x!=null) {
        int cmp = key.compareTo(x.key);

        if (cmp < 0) 
            x = x.left;
        else if (cmp > 0) 
            x = x.right;
        else
            return x;
    }

    return x;
}

public BSTNode<T> iterativeSearch(T key) {
    return iterativeSearch(mRoot, key);
}
```

### 3.4 二叉查找树的最大值和最小值

* 最大值：一直循环右子数，直到叶子节点
* 最大值：一直循环左子数，直到叶子节点

### 3.5 二叉查找树的插入

```
/* 
 * 将结点插入到二叉树中
 *
 * 参数说明：
 *     tree 二叉树的
 *     z 插入的结点
 */
private void insert(BSTree<T> bst, BSTNode<T> z) {
    int cmp;
    BSTNode<T> y = null;
    BSTNode<T> x = bst.mRoot;

    // 查找z的插入位置
    while (x != null) {
        y = x;
        cmp = z.key.compareTo(x.key);
        if (cmp < 0)
            x = x.left;
        else
            x = x.right;
    }

    z.parent = y;
    if (y==null)
        bst.mRoot = z;
    else {
        cmp = z.key.compareTo(y.key);
        if (cmp < 0)
            y.left = z;
        else
            y.right = z;
    }
}

/* 
 * 新建结点(key)，并将其插入到二叉树中
 *
 * 参数说明：
 *     tree 二叉树的根结点
 *     key 插入结点的键值
 */
public void insert(T key) {
    BSTNode<T> z=new BSTNode<T>(key,null,null,null);

    // 如果新建结点失败，则返回。
    if (z != null)
        insert(this, z);
}
```

### 3.6 二叉查找树的删除

```
/* 
 * 删除结点(z)，并返回被删除的结点
 *
 * 参数说明：
 *     bst 二叉树
 *     z 删除的结点
 */
private BSTNode<T> remove(BSTree<T> bst, BSTNode<T> z) {
    BSTNode<T> x=null;
    BSTNode<T> y=null;

    if ((z.left == null) || (z.right == null) )
        y = z;
    else
        y = successor(z);

    if (y.left != null)
        x = y.left;
    else
        x = y.right;

    if (x != null)
        x.parent = y.parent;

    if (y.parent == null)
        bst.mRoot = x;
    else if (y == y.parent.left)
        y.parent.left = x;
    else
        y.parent.right = x;

    if (y != z) 
        z.key = y.key;

    return y;
}

/* 
 * 删除结点(z)，并返回被删除的结点
 *
 * 参数说明：
 *     tree 二叉树的根结点
 *     z 删除的结点
 */
public void remove(T key) {
    BSTNode<T> z, node; 

    if ((z = search(mRoot, key)) != null)
        if ( (node = remove(this, z)) != null)
            node = null;
}
```

### 3.7 二叉树的广度优先遍历和深度优先遍历

```
import java.util.ArrayDeque;

public class BinaryTree {
    static class TreeNode{
        int value;
        TreeNode left;
        TreeNode right;

        public TreeNode(int value){
            this.value=value;
        }
    }

    TreeNode root;

    public BinaryTree(int[] array){
        root=makeBinaryTreeByArray(array,1);
    }

    /**
     * 采用递归的方式创建一颗二叉树
     * 传入的是二叉树的数组表示法
     * 构造后是二叉树的二叉链表表示法
     */
    public static TreeNode makeBinaryTreeByArray(int[] array,int index){
        if(index<array.length){
            int value=array[index];
            if(value!=0){
                TreeNode t=new TreeNode(value);
                array[index]=0;
                t.left=makeBinaryTreeByArray(array,index*2);
                t.right=makeBinaryTreeByArray(array,index*2+1);
                return t;
            }
        }
        return null;
    }

    /**
     * 深度优先遍历，相当于先根遍历
     * 采用非递归实现
     * 需要辅助数据结构：栈
     */
    public void depthOrderTraversal(){
        if(root==null){
            System.out.println("empty tree");
            return;
        }       
        ArrayDeque<TreeNode> stack=new ArrayDeque<TreeNode>();
        stack.push(root);       
        while(stack.isEmpty()==false){
            TreeNode node=stack.pop();
            System.out.print(node.value+"    ");
            if(node.right!=null){
                stack.push(node.right);
            }
            if(node.left!=null){
                stack.push(node.left);
            }           
        }
        System.out.print("\n");
    }

    /**
     * 广度优先遍历
     * 采用非递归实现
     * 需要辅助数据结构：队列
     */
    public void levelOrderTraversal(){
        if(root==null){
            System.out.println("empty tree");
            return;
        }
        ArrayDeque<TreeNode> queue=new ArrayDeque<TreeNode>();
        queue.add(root);
        while(queue.isEmpty()==false){
            TreeNode node=queue.remove();
            System.out.print(node.value+"    ");
            if(node.left!=null){
                queue.add(node.left);
            }
            if(node.right!=null){
                queue.add(node.right);
            }
        }
        System.out.print("\n");
    }

    /** 
     *                  13
     *                 /  \
     *               65    5
     *              /  \    \
     *             97  25   37
     *            /    /\   /
     *           22   4 28 32
     */
    public static void main(String[] args) {
        int[] arr={0,13,65,5,97,25,0,37,22,0,4,28,0,0,32,0};
        BinaryTree tree=new BinaryTree(arr);
        tree.depthOrderTraversal();
        tree.levelOrderTraversal();
    }
}
```





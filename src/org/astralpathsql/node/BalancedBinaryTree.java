package org.astralpathsql.node;

import org.astralpathsql.been.COREINFORMATION;
import org.astralpathsql.server.MainServer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.astralpathsql.server.MainServer.Mtree;

public class BalancedBinaryTree<T> {
    private int size;
    private Node root;
    @SuppressWarnings("rawtypes")
    private Comparator comparator;

    public BalancedBinaryTree() {

    }
    @SuppressWarnings("rawtypes")
    public BalancedBinaryTree(Comparator comparator) {
        this.comparator = comparator;
    }
    public void add(T data) {
        Node node = new Node(data);
        Node p = findNode(node);
        if (p == null) {
            this.root = node;
        } else if (p.compare(node) > 0) {
            p.left = node;
            node.parent = p;
        } else if (p.compare(node) < 0) {
            p.right = node;
            node.parent = p;
        } else {
            p.data = data;
            return;
        }
        size++;
        handleImBalance4Add(node);
    }

    public boolean remove(T data) {
        if (root == null) {
            return false;
        }
        Node node = new Node(data);
        Node res = findNode(node);
        if (node.compare(res) != 0) {
            throw new NoSuchElementException();
        }
        Node checkPoint;
        if (res.left == null && res.right == null) {
            checkPoint = removeLeafNode(res);
        } else if (res.left == null || res.right == null) {
            checkPoint = removeNodeWithOneChild(res);
        } else {
            checkPoint = removeNodeWithTwoChild(res);
        }
        if (checkPoint != null) {
            handleImbalance4Remove(checkPoint);
        }
        size--;
        return true;
    }

    public int size() {
        return size;
    }

    private void handleImbalance4Remove(Node checkPoint) {
        Node tmp = checkPoint;
        boolean leftRemoved = false;
        if (tmp.left == null && tmp.right == null) {
            tmp.factor = 0;
            if (tmp.parent != null) {
                leftRemoved = tmp.parent.left == tmp;
            }
            tmp = tmp.parent;
        } else if (tmp.factor == 0) {
            tmp.factor = tmp.left == null ? 1 : -1;
            return;
        } else {
            leftRemoved = tmp.left == null;
        }
        int f;
        while (tmp != null) {
            f = tmp.factor;

            if (leftRemoved) {
                f = f + 1;
            } else {
                f = f - 1;
            }
            tmp.factor = f;
            if (f == -2 || f == 2) {
                tmp = handleImbalance(tmp);
            } else if (f == 1 || f == -1) {
                break;
            }
            leftRemoved = tmp.parent != null && tmp.parent.left == tmp;
            tmp = tmp.parent;
        }
    }

    private Node removeNodeWithTwoChild(Node node) {
        if (node.left == null || node.right == null) {
            throw new IllegalStateException("only use of two child node removing");
        }
        Node target = findDeepestAndClosestNodeOfCurrentNode(node);
        node.data = target.data;
        Node p = null;
        if (target.left == null && target.right == null) {
            p = removeLeafNode(target);
        } else if (target.left == null || target.right == null) {
            p = removeNodeWithOneChild(target);
        } else {
            //impossible
            throw new IllegalStateException("method of findDeepestAndClosestNodeOfCurrentNode work not right!");
        }
        return p;
    }

    private Node removeNodeWithOneChild(Node node) {
        Node parent = node.parent;
        Node child;
        if (node.left == null && node.right != null) {
            child = node.right;
        } else if (node.right == null && node.left != null) {
            child = node.left;
        } else {
            throw new IllegalStateException("only use of one child node removing");
        }
        child.parent = parent;
        if (parent == null) {
            root = child;
        } else if (parent.left == node) {
            parent.left = child;
        } else {
            parent.right = child;
        }
        return child;
    }

    private Node removeLeafNode(Node node) {
        if (node.left != null || node.right != null) {
            throw new IllegalStateException("only use of leaf child node removing");
        }
        Node parent = node.parent;
        if (parent != null) {
            if (parent.left == node) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        } else {
            root = null;
        }
        return parent;
    }

    private Node findDeepestAndClosestNodeOfCurrentNode(Node node) {
        Node left = node.left;
        Node right = node.right;
        int i = 0, j = 0;
        Node targetLeft = left, targetRight = right;
        boolean bottom = false;
        for (; ; i++) {
            if (left.right != null) {
                left = left.right;
            } else {
                if (!bottom) {
                    targetLeft = left;
                    bottom = true;
                }
                if (left.left != null) {
                    left = left.left;
                } else {
                    break;
                }
            }
        }
        bottom = false;
        for (; ; j++) {
            if (right.left != null) {
                right = right.left;
            } else {
                if (!bottom) {
                    targetRight = right;
                    bottom = true;
                }
                if (right.right != null) {
                    right = right.right;
                } else {
                    break;
                }
            }
        }
        if (i >= j) {
            return targetLeft;
        } else {
            return targetRight;
        }
    }

    private void handleImBalance4Add(Node checkPoint) {
        Node tmp = checkPoint;
        Node p = tmp.parent;
        boolean tmpLeftFlag;
        int k;
        while (p != null) {
            tmpLeftFlag = p.left == tmp;
            if (tmpLeftFlag) {
                k = p.factor - 1;
            } else {
                k = p.factor + 1;
            }
            p.factor = k;
            if (k == 2 || k == -2) {
                handleImbalance(p);
                break;
            } else if (k == 0) {
                break;
            }
            tmp = p;
            p = p.parent;
        }
    }

    private Node handleImbalance(Node node) {
        Node res;
        int rotateType;
        int k = 0;
        if (node.factor == -2 && node.left.factor == -1) {
            res = RRR(node);
            rotateType = 1;
        } else if (node.factor == 2 && node.right.factor == 1) {
            res = LLR(node);
            rotateType = 2;
        } else if (node.factor == -2 && node.left.factor == 1) {
            k = node.left.right.factor;
            res = LRR(node);
            rotateType = 3;
        } else if (node.factor == 2 && node.right.factor == -1) {
            k = node.right.left.factor;
            res = RLR(node);
            rotateType = 4;
        } else {
            throw new RuntimeException("平衡因子计算错误");
        }
        handleFactorAfterReBalance(res, rotateType, k);
        return res;
    }

    private void handleFactorAfterReBalance(Node node, int rotateType, int originFactor) {
        node.factor = 0;
        if (rotateType == 1) {
            node.right.factor = 0;
        } else if (rotateType == 2) {
            node.left.factor = 0;
        } else if (rotateType == 3 || rotateType == 4) {
            if (originFactor == 1) {
                node.left.factor = -1;
                node.right.factor = 0;
            } else if (originFactor == -1) {
                node.left.factor = 0;
                node.right.factor = 1;
            } else {
                node.left.factor = 0;
                node.right.factor = 0;
            }
        }
    }

    private Node RRR(Node node) {
        Node parent = node.parent;
        Node child = node.left;
        child.parent = parent;
        node.parent = child;
        node.left = child.right;
        if (node.left != null) {
            node.left.parent = node;
        }
        child.right = node;
        if (parent == null) {
            root = child;
        } else if (parent.right == node) {
            parent.right = child;
        } else {
            parent.left = child;
        }
        return child;
    }

    private Node LLR(Node node) {
        Node parent = node.parent;
        Node child = node.right;
        child.parent = parent;
        node.parent = child;
        node.right = child.left;
        if (node.right != null) {
            node.right.parent = node;
        }
        child.left = node;
        if (parent == null) {
            root = child;
        } else if (parent.left == node) {
            parent.left = child;
        } else {
            parent.right = child;
        }
        return child;
    }

    private Node LRR(Node node) {
        LLR(node.left);
        return RRR(node);
    }

    private Node RLR(Node node) {
        RRR(node.right);
        return LLR(node);
    }

    private Node findNode(Node node) {
        Node p = null;
        Node tmp = root;
        while (tmp != null) {
            p = tmp;
            if (tmp.compare(node) > 0) {
                tmp = tmp.left;
            } else if (tmp.compare(node) < 0) {
                tmp = tmp.right;
            } else {
                break;
            }
        }
        return p;
    }

    private void traverse(Node node, Consumer<Node> consumer) {
        if (node == null) {
            return;
        }
        Deque<Node> nodeDeque = new ArrayDeque<>();
        node.height = 1;
        nodeDeque.add(node);
        while (nodeDeque.size() > 0) {
            Node first = nodeDeque.pollFirst();
            consumer.accept(first);
            Node right = first.right;
            if (right != null) {
                right.height = first.height + 1;
                nodeDeque.addFirst(right);
            }
            Node left = first.left;
            if (left != null) {
                left.height = first.height + 1;
                nodeDeque.addFirst(left);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        traverse(root, n -> {
            for (int i = 0; i < n.height; i++) {
                res.append("----------");
            }
            res.append(':');
            res.append(n.data);
            res.append('(');
            res.append(n.factor);
            res.append(')');
            res.append('\n');
        });
        return res.toString();
    }

    public String forW() {
        StringBuilder res = new StringBuilder();
        traverse(root, n -> {
            res.append(n.data);
            res.append("§");
        });
        return res.toString();
    }
    public Integer num(String t) {
        AtomicInteger in = new AtomicInteger();
        traverse(root, n -> {
            COREINFORMATION a = (COREINFORMATION) n.data;
            if (a.getTable().equals(t)) {
                in.getAndIncrement();
            }
        });
        return in.get();
    }
    public String forT(String table) {
        StringBuilder res = new StringBuilder();
        traverse(root, n -> {
            COREINFORMATION a = (COREINFORMATION) n.data;
            if (a.getTable().equals(table)) {
                res.append(a.getINFO());
                res.append("§");
            }
        });
        return res.toString();
    }
    public String forT(String table,String info) {
        StringBuilder res = new StringBuilder();
        traverse(root, n -> {
            COREINFORMATION a = (COREINFORMATION) n.data;
            if (a.getTable().equals(table)) {

                if (a.getINFO().contains(info)) {
                    res.append(a);
                    res.append("§");
                }
            }
        });
        return res.toString();
    }
    public String forI(String info) {
        StringBuilder res = new StringBuilder();
        traverse(root, n -> {
            COREINFORMATION a = (COREINFORMATION) n.data;
                if (a.getINFO().contains(info)) {
                    res.append(a);
                    res.append("§");
                }
        });
        return res.toString();
    }
    public String forTa(String table,String info) {
        StringBuilder res = new StringBuilder();
        traverse(root, n -> {
            COREINFORMATION a = (COREINFORMATION) n.data;
            if (a.getTable().equals(table)) {
                if (a.getINFO().contains(info)) {
                    res.append(a.getINFO());
                    res.append("§");
                }
            }
        });
        return res.toString();
    }
    public void deleteBy(String table,String info) {
        List<Integer> res = new ArrayList<>();
        traverse(root, n -> {
            COREINFORMATION a = (COREINFORMATION) n.data;
            if (a.getTable().equals(table)) {
                if (a.getINFO().contains(info)) {
                    res.add(a.getId());
                }
            }
        });
        for (int i = 0; i < res.size(); i++) {
            if (Mtree.containsKey(table)) {
                Mtree.get(table).remove(new COREINFORMATION(res.get(i)));
            } else {
                MainServer.tree.remove(new COREINFORMATION(res.get(i)));
            }

        }
    }
    public String select(String v,String t) {
        StringBuilder res = new StringBuilder();
        traverse(root, n -> {
            COREINFORMATION a = (COREINFORMATION) n.data;
            String r[] = a.getINFO().split(";");
            String ta[] = a.getTable().split(":");
            for (int x = 0; x < r.length; x ++) {
                if (r[x].contains(v)) {
                    if (ta[0].equals(t)) {
                        String ch[] = r[x].split("'");
                        res.append(ch[1]);
                        res.append("§");
                    }
                }
            }

        });
        return res.toString();
    }
    public String select(String v,String t,String info) {
        StringBuilder res = new StringBuilder();
        traverse(root, n -> {
            COREINFORMATION a = (COREINFORMATION) n.data;
            String r[] = a.getINFO().split(";");
            String ta[] = a.getTable().split(":");
            for (int x = 0; x < r.length; x ++) {
                if (r[x].contains(v)) {
                    if (ta[0].equals(t)) {
                        if (a.getINFO().contains(info)) {
                            System.out.println(1);
                            String ch[] = r[x].split("'");
                            res.append(ch[1]);
                            res.append("§");
                        }
                    }
                }
            }

        });
        return res.toString();
    }
    private int getMaxHeight(Node node) {
        int[] height = new int[1];
        traverse(node, n -> {
            if (n.left == null && n.right == null && n.height > height[0]) {
                height[0] = n.height;
            }
        });
        return height[0];
    }

    //查询
    public Object getId(Comparable<T> data) {
        if (this.size == 0) {                                    // 没有数据
            return false;                                        // 结束查询
        }
        return this.root.getId(data);                    // Node类查询
    }


    class Node {
        private Node parent;
        private Node left;
        private Node right;
        private int factor;
        private int height;
        private T data;

        public Node(T data) {
            Objects.requireNonNull(data);
            this.data = data;
        }

        public int compare(Node target) {
            if (comparator != null) {
                return comparator.compare(this.data, target.data);
            } else {
                return ((Comparable) this.data).compareTo(target.data);
            }
        }

        @Override
        public String toString() {
            return data.toString();
        }

        public Object getId(Comparable<T> data) {
            if (data.compareTo((T) this.data) == 0) {            // 数据匹配
                return this.data;                                    // 查找到了
            } else if (data.compareTo((T) this.data) < 0) {    // 左子节点查询
                if (this.left != null) {                        // 左子节点存在
                    return this.left.getId(data);        // 递归调用
                } else {                                        // 没有左子节点
                    return false;                                // 无法找到
                }
            } else {                                            // 右子节点查询
                if (this.right != null) {                        // 右子节点存在
                    return this.right.getId(data);        // 递归调用
                } else {                                        // 没有右子节点
                    return false;                                // 无法找到
                }
            }
        }
    }

    static BalancedBinaryTree<Integer> buildTestTree() {
        BalancedBinaryTree<Integer> binaryTree = new BalancedBinaryTree();
        binaryTree.add(8);
        binaryTree.add(3);
        binaryTree.add(13);
        binaryTree.add(1);
        binaryTree.add(5);
        binaryTree.add(10);
        binaryTree.add(16);
        binaryTree.add(2);
        binaryTree.add(4);
        binaryTree.add(7);
        binaryTree.add(9);
        binaryTree.add(12);
        binaryTree.add(15);
        binaryTree.add(18);
        binaryTree.add(6);
        binaryTree.add(11);
        binaryTree.add(14);
        binaryTree.add(17);
        binaryTree.add(19);
        binaryTree.add(20);
        return binaryTree;
    }
}
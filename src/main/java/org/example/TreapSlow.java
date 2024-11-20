package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import treePrinter.TreePrinter;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Теория<br/>
 * <a href="http://e-maxx.ru/algo/treap">http://e-maxx.ru/algo/treap</a><br/>
 * <a href="https://www.geeksforgeeks.org/treap-a-randomized-binary-search-tree/">https://www.geeksforgeeks.org/treap-a-randomized-binary-search-tree/</a><br/>
 * <a href="https://www.geeksforgeeks.org/implementation-of-search-insert-and-delete-in-treap/">https://www.geeksforgeeks.org/implementation-of-search-insert-and-delete-in-treap/</a><br/>
 * <a href="http://faculty.washington.edu/aragon/pubs/rst89.pdf">http://faculty.washington.edu/aragon/pubs/rst89.pdf</a><br/>
 * <a href="https://habr.com/ru/articles/101818/">https://habr.com/ru/articles/101818/</a><br/>
 * <a href="https://habr.com/ru/articles/102006/">https://habr.com/ru/articles/102006/</a><br/>
 * Примеение в linux kernel<br/>
 * <a href="https://www.kernel.org/doc/mirror/ols2005v2.pdf">https://www.kernel.org/doc/mirror/ols2005v2.pdf</a>
 */
public class TreapSlow {

    Node root;

    TreePrinter<Node> printer = new TreePrinter<>(Node::getLabel, Node::getLeft, Node::getRight);

    {
        printer.setHspace(1);
        printer.setHspace(1);
        printer.setSquareBranches(true);
    }

    public void add(Integer value) {
        root = merge(root, new Node(value));
    }

    public void add(int pos, Integer value) {
        Node[] split = root.split(pos);
        Node tmp = new Node(value);
        root = merge(merge(split[0], tmp), split[1]);
    }


    public Node getStats(int a, int b) {
        // [0 < a) [a < N)
        Node[] low = root.split(a);
        // [a < b) [b < N)
        Node[] high = low[1].split(b - a);
        return high[0];
    }


    public Node merge(Node leftTree, Node rightTree) {
        push(leftTree);
        push(rightTree);
        if (leftTree == null) {
            return rightTree;
        }
        if (rightTree == null) {
            return leftTree;
        }
        if (leftTree.priority < rightTree.priority) {
            Node newRight = merge(leftTree.right, rightTree);
            return new Node(leftTree.value, leftTree.priority, leftTree.left, newRight);
        } else {
            Node newLeft = merge(leftTree, rightTree.left);
            return new Node(rightTree.value, rightTree.priority, newLeft, rightTree.right);
        }
    }

    private static void push(Node tree) {
        if (tree != null) {
            tree.pushPromise();
        }
    }

    private static int sizeOf(Node node) {
        return node != null ? node.size : 0;
    }

    private void inorder(Node cur, List<String> res) {
        if (cur == null) {
            return;
        }
        cur.pushPromise();
        inorder(cur.left, res);
        res.add(cur.toString());
        inorder(cur.right, res);
    }

    public void addProgression(Integer a, Integer b, Integer x) {
        // [0 < a) [a < N)
        Node[] low = root.split(a);
        // [a < b) [b < N)
        Node[] high = low[1].split(b - a);

        high[0].setProgression(x);

        root = merge(merge(low[0], high[0]), high[1]);
    }

    public void setAll(Integer a, Integer b, Integer x) {
        // [0 < a) [a < N)
        Node[] low = root.split(a);
        // [a < b) [b < N)
        Node[] high = low[1].split(b - a);

        high[0].setPromise(x);

        root = merge(merge(low[0], high[0]), high[1]);
    }


    public static class Statistic {
        int minValue;
        long sumValue;
        int maxValue;

        @Override
        public String toString() {
            return "Statistic{" +
                    "minValue=" + minValue +
                    ", sumValue=" + sumValue +
                    ", maxValue=" + maxValue +
                    '}';
        }
    }

    @Getter
    public static class Node {
        static Random RND = new Random(1);
        int priority;
        Node left;
        Node right;
        int size;
        long value;

        Statistic statistic = new Statistic();

        private Integer promise;
        private Integer progression;
        private int progressionOffset;


        private boolean isQueued = false;

        private List<Promise> promiseQueue;


        public Node(long value) {
            this(value, RND.nextInt());
        }

        public Node(long value, int priority) {
            this(value, priority, null, null);
        }

        public Node(long value, int priority, Node left, Node right) {
            this.priority = priority;
            this.left = left;
            this.right = right;
            this.value = value;
            if (value < 0) {
                throw new IllegalArgumentException();
            }
            recalculate();
        }

        public void recalculate() {
            size = sizeOf(left) + sizeOf(right) + 1;
            if (isQueued) {
                recalculateQueued();
            } else {
                recalculatePush();
            }


        }

        private void recalculateQueued() {
            if (promiseQueue != null && !promiseQueue.isEmpty()) {
                PromiseContext ctx = new PromiseContext(size, value, sumValueOf(left) + sumValueOf(right) + value, this);
                promiseQueue.forEach(p -> p.apply(ctx));
                setSumValue(ctx.sum);
            } else {
                setSumValue(sumValueOf(left) + sumValueOf(right) + value);
            }
        }

        private void recalculatePush() {
            if (promise == null) {
                setSumValue(sumValueOf(left) + sumValueOf(right) + value + promiseProgressionSum());
            } else {
                setSumValue(size * promise);
            }
        }

        private Long promiseProgressionSum() {
            if (progression == null) {
                return 0L;
            }
            return (sumProgression(progressionOffset + size) - sumProgression(progressionOffset)) * progression;

        }


        static long sumProgression(long n) {
            if (n < 1) {
                return 0;
            }
            return ((1L + n) * n / 2);
        }


        private static Long sumValueOf(Node node) {
            if( node == null){
                return 0L;
            }
            return node != null ? node.statistic.sumValue : 0;
        }


        public void setLeft(Node left) {
            this.left = left;
            recalculate();
        }

        public void setRight(Node right) {
            this.right = right;

            recalculate();
        }

        public void setSumValue(long sumValue) {
            if (sumValue < 0) {
                throw new IllegalStateException();
            }
            statistic.sumValue = sumValue;
        }

        public Node[] split(int pos) {
            push(this);
            Node tmp = null;
            Node[] res = (Node[]) Array.newInstance(this.getClass(), 2);
            int curIndex = sizeOf(left) + 1;
            if (curIndex <= pos) {
                if (right != null) {
                    Node[] split = right.split(pos - curIndex);
                    tmp = split[0];
                    res[1] = split[1];
                }
                res[0] = new Node(value, priority, left, tmp);
            } else {
                if (left != null) {
                    Node[] split = left.split(pos);
                    tmp = split[1];
                    res[0] = split[0];
                }
                res[1] = new Node(value, priority, tmp, right);
            }
            return res;
        }


        public void setPromise(int promise) {
            if (isQueued) {
                setPromiseQueued(promise);
            } else {
                setPromisePush(promise);
            }
        }

        private void setPromiseQueued(int promise) {
            addToPromiseWithClear(new SetPromise(promise));
            recalculate();
        }

        private void addToPromiseWithClear(Promise promise) {
            if (promiseQueue != null) {
                promiseQueue.clear();
            }
            addToPromise(promise);

        }

        private void setPromisePush(int promise) {
            if (progression != null) {
                pushPromise();
            }
            this.promise = promise;
            recalculate();
        }

        public void pushPromise() {
            if (isQueued) {
                pushQueued();
            } else {
                pushBasic();
            }
        }

        private void pushQueued() {
            if (promiseQueue == null || promiseQueue.isEmpty()) {
                return;
            }
            PromiseContext ctx = new PromiseContext(size, value, sumValueOf(left) + sumValueOf(right) + value, this);
            promiseQueue.forEach(p -> p.pushPromise(ctx));
            promiseQueue.clear();
            recalculate();
        }

        private void pushBasic() {
            if (left != null) {
                if (promise != null) {
                    left.setPromise(promise);
                }
                if (progression != null) {
                    left.setProgression(progression, progressionOffset);
                }
            }
            if (right != null) {
                if (promise != null) {
                    right.setPromise(promise);
                }
                if (progression != null) {
                    right.setProgression(progression, sizeOf(left) + 1 + progressionOffset);
                }
            }

            if (promise != null) {
                value = promise;
                promise = null;
            }
            if (progression != null) {
                int progressionCount = sizeOf(left) + 1 + progressionOffset;
                value += progressionCount * progression;
                if (value < 0) {
                    throw new IllegalStateException();
                }
                progression = null;
            }
            recalculate();
        }


        public void setProgression(Integer x) {
            this.setProgression(x, 0);

        }

        public void setProgression(Integer x, int offset) {
            if (isQueued) {
                setProgressionQueued(x, offset);
            } else {
                setProgressionWithPush(x, offset);
            }
        }

        private void setProgressionQueued(Integer x, int offset) {
            addToPromise(new Progression(x, offset));
        }

        public void addToPromise(Promise promise) {
            if (promiseQueue == null) {
                promiseQueue = new LinkedList<>();
            }
            if (promise instanceof Progression && !promiseQueue.isEmpty()) {
                Progression pNew = (Progression) promise;
                Promise lastPromise = promiseQueue.get(promiseQueue.size() - 1);
                if (lastPromise != null && lastPromise instanceof Progression) {
                    Progression pOld = (Progression) lastPromise;
                    if (pOld.offset == pNew.offset) {
                        pOld.merge(pNew);
                        recalculate();
                        return;
                    }
                }
            }

            promiseQueue.add(promise);
            recalculate();
        }

        private void setProgressionWithPush(Integer x, int offset) {
            if (promise != null) {
                pushPromise();
            }
            if (progression != null) {
                if (progressionOffset != offset) {
                    pushPromise();
                } else {
                    this.progression = this.progression + x;
                    recalculate();
                    return;
                }
            }
            this.progression = x;
            this.progressionOffset = offset;


            recalculate();
        }

        private Node search(int x) {

            if (value == x) {
                return this;
            }
            if (value < x) {
                return left.search(x);
            }
            return right.search(x);
        }

        public String getLabel() {
            if(isQueued){
                return  String.format("v=%d, s=%d sum=%d %s", value, size, statistic.sumValue, promiseQueue);
            }
            return String.format("v=%d, s=%d (prog=%d_%d, prom=%d) sum=%d", value, size, progression, progressionOffset, promise, statistic.sumValue);
        }
    }

    @AllArgsConstructor
    static class PromiseContext {
        final int size;
        long value;

        long sum;
        final Node node;
    }

    @AllArgsConstructor
    static class Progression implements Promise {
        long n;

        final long offset;

        @Override
        public void apply(PromiseContext context) {
            if (offset > 0) {
                context.sum = context.sum;
            }
            context.sum += (Node.sumProgression(context.size + offset) - Node.sumProgression(offset)) * n;
        }

        @Override
        public void pushPromise(PromiseContext context) {
            int sizeLeft = sizeOf(context.node.left);
            long curPos = offset + sizeLeft + 1;
            context.node.value += curPos * n;
            pushToNode(context.node.left, this);
            Progression forRight = new Progression(n, curPos);
            pushToNode(context.node.right, forRight);
        }

        public void merge(Progression pNew) {
            n += pNew.n;
        }

        @Override
        public String toString() {
            return "n=" + n + ",o=" + offset ;
        }
    }

    @AllArgsConstructor
    static class SetPromise implements Promise {
        long value;

        @Override
        public void apply(PromiseContext context) {
            context.value = value;
            context.sum = context.size * value;
        }

        @Override
        public void pushPromise(PromiseContext context) {
            context.node.value = value;
            pushToNodeWithClear(context.node.left, this);
            pushToNodeWithClear(context.node.right, this);
        }

        @Override
        public String toString() {
            return "set=" + value;
        }
    }

    interface Promise {
        void apply(PromiseContext context);

        void pushPromise(PromiseContext context);

        default void pushToNode(Node node, Promise promise) {
            if (node != null) {
                node.addToPromise(promise);
            }
        }

        default void pushToNodeWithClear(Node node, Promise promise) {
            if (node != null) {
                node.addToPromiseWithClear(promise);
            }
        }
    }
}

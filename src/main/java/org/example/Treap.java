package org.example;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Random;

public class Treap {

        Node root;

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
            if (leftTree == null) {
                return rightTree;
            }
            if (rightTree == null) {
                return leftTree;
            }


            if (leftTree.priority < rightTree.priority) {
                push(leftTree);
                Node newRight = merge(leftTree.right, rightTree);
                return new Node(leftTree.value, leftTree.priority, leftTree.left, newRight);
            } else {
                push(rightTree);
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

        public void addProgression(Integer a, Integer b, Integer x) {
            // [0 < a) [a < N)
            Node[] low = root.split(a);
            // [a < b) [b < N)
            Node[] high = low[1].split(b - a);

            high[0].setProgression(x);

            root = merge(merge(low[0], high[0]), high[1]);
        }

        public void case1(Integer a, Integer b, Integer x) {
            // [0 < a) [a < N)
            Node[] low = root.split(a);
            // [a < b) [b < N)
            Node[] high = low[1].split(b - a);

            high[0].setPromise(x);

            root = merge(merge(low[0], high[0]), high[1]);
        }

    public void case2(int a, int b, int x) {
       addProgression(a ,b, x);
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
            static Random RND = new Random();
            int priority;
            Node left;
            Node right;
            int size;
            long value;

            Statistic statistic = new Statistic();

            private boolean hasPromise;
            private int promise;

            private boolean hasProgression;
            private int progression;
            private int progressionOffset;

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
                recalculate();
            }

            public void recalculate() {
                size = sizeOf(left) + sizeOf(right) + 1;
                setSumValue(
                        (hasPromise ? (promise * (long) size) : (sumValueOf(left) + sumValueOf(right) + value)) + (hasProgression ? promiseProgressionSum() : 0)
                );
            }


            private Long promiseProgressionSum() {
                return (sumProgression(progressionOffset + size) - sumProgression(progressionOffset)) * progression;

            }

            static long sumProgression(long n) {
                if (n < 1) {
                    return 0;
                }
                return ((1L + n) * n / 2);
            }


            private static Long sumValueOf(Node node) {
                if (node == null) {
                    return 0L;
                }
                return node.statistic.sumValue;
            }


            public void setSumValue(long sumValue) {
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
                hasProgression = false;
                progression = progressionOffset = 0;

                hasPromise = true;
                this.promise = promise;
                recalculate();
            }


            public void pushPromise() {
                if (left != null) {
                    if (hasPromise) {
                        left.setPromise(promise);
                    }
                    if (hasProgression) {
                        left.setProgression(progression, progressionOffset);
                    }
                    if (hasProgression || hasPromise) {
                        left.recalculate();
                    }
                }
                if (right != null) {
                    if (hasPromise) {
                        right.setPromise(promise);
                    }
                    if (hasProgression) {
                        right.setProgression(progression, sizeOf(left) + 1 + progressionOffset);
                    }
                    if (hasProgression || hasPromise) {
                        right.recalculate();
                    }
                }

                if (hasPromise) {
                    value = promise;
                    promise = 0;
                    hasPromise = false;
                }
                if (hasProgression) {

                    value += (long) (sizeOf(left) + 1 + progressionOffset) * progression;
                    hasProgression = false;
                    progression = 0;
                }
                recalculate();
            }


            public void setProgression(int x) {
                this.setProgression(x, 0);

            }

            public void setProgression(int x, int offset) {
                if (hasProgression) {
                    if (this.progressionOffset != offset) {
                        pushPromise();
                    } else {
                        progression += x;
                        recalculate();
                        return;
                    }
                }

                hasProgression = true;
                progression = x;
                progressionOffset = offset;
                recalculate();

            }

        }

    }


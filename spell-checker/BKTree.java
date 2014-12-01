/**
 * 
 */

/**
 * @author amal
 *
 */
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BKTree {
        public static class BKNode {
                final String name;
                long freq;
                final Map<Integer, BKNode> children = new HashMap<Integer, BKNode>();

                public BKNode(String name,long freq) {
                        this.name = name;
                        this.freq=freq;
                }

                protected BKNode childAtDistance(int pos) {
                        return children.get(pos);
                }

                private void addChild(int pos, BKNode child) {
                        children.put(pos, child);
                }

                public Map<String,Long> search(String node, int maxDistance) {
                        int distance = distance(this.name, node);
                       // System.out.println("word"+this.name);
                        Map<String,Long> matches = new HashMap<String,Long>();
                        if (distance <= maxDistance)
                                matches.put(this.name,this.freq);
                        if (children.size() == 0)
                                return matches;
                        int i = max(1, distance - maxDistance);
                        for (; i <= distance + maxDistance; i++) {
                                BKNode child = children.get(i);
                                if (child == null)
                                        continue;
                                matches.putAll(child.search(node, maxDistance));
                        }
                        return matches;
                }
        }

        private BKNode root;

        
        public Map<String,Long> search(String q, int maxDist) {
                return root.search(q, maxDist);
        }
       
       
        public Map<String,Long> search(String q) {
                Map<String,Long> list = root.search(q, 1);
                return list;
        }
       

        public void add(String node,long freq) {
                if(node == null || node.isEmpty()) throw new IllegalArgumentException("word can't be null or empty.");
                BKNode newNode = new BKNode(node,freq);
                if (root == null) {
                        root = newNode;
                }
                addInternal(root, newNode);
        }

        private void addInternal(BKNode src, BKNode newNode) {
                if (src.equals(newNode))
                        return;
                int distance = distance(src.name, newNode.name);
                BKNode bkNode = src.childAtDistance(distance);
                if (bkNode == null) {
                        src.addChild(distance, newNode);
                } else
                        addInternal(bkNode, newNode);
        }

        
        public static int distance(String src, String tgt) {
                int d[][];
                if (src.length() == 0) {
                        return tgt.length();
                }
                if (tgt.length() == 0) {
                        return src.length();
                }
                d = new int[src.length() + 1][tgt.length() + 1];
                for (int i = 0; i <= src.length(); i++) {
                        d[i][0] = i;
                }
                for (int j = 0; j <= tgt.length(); j++) {
                        d[0][j] = j;
                }
                for (int i = 1; i <= src.length(); i++) {
                        char sch = src.charAt(i - 1);
                        for (int j = 1; j <= tgt.length(); j++) {
                                char tch = tgt.charAt(j - 1);
                                int cost = sch == tch ? 0 : 1;
                                d[i][j] = minimum(d[i - 1][j] + 1,         //deletion
                                                              d[i][j - 1] + 1,         //insertion
                                                              d[i - 1][j - 1] + cost); //substitution
                        }
                }
                return d[src.length()][tgt.length()];

        }

        private static int minimum(int a, int b, int c) {
                return min(min(a, b), c);
        }
}


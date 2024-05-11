package org.example;

import java.util.ArrayList;
import java.util.List;

class BStarNode {
    List<Integer> keys;
    List<BStarNode> children;
    boolean leaf;

    BStarNode(boolean leaf) {
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
        this.leaf = leaf;
    }
}
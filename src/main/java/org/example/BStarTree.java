package org.example;

public class BStarTree {
    private BStarNode root;
    private final int t; // минимальная степень дерева

    protected static int insertOpCount = 0;
    protected static int searchOpCount = 0;
    protected static int deleteOpCount = 0;

    BStarTree(int t) {
        this.root = null;
        this.t = t;
    }

    public void insert(int key) {
        if (root == null) {
            root = new BStarNode(true);
            root.keys.add(key);
            insertOpCount++;
        } else {
            if (root.keys.size() == (2 * t) - 1) {
                BStarNode newRoot = new BStarNode(false);
                newRoot.children.add(root);
                insertOpCount++;
                splitChild(newRoot, 0);
                insertOpCount++;
                root = newRoot;
            }
            insertNonFull(root, key);
            insertOpCount++;
        }
    }

    private void insertNonFull(BStarNode node, int key) {
        int i = node.keys.size() - 1;
        if (node.leaf) {
            while (i >= 0 && key < node.keys.get(i)) {
                i--;
            }
            node.keys.add(i + 1, key);
            insertOpCount++;
        } else {
            while (i >= 0 && key < node.keys.get(i)) {
                i--;
            }
            i++;
            if (node.children.get(i).keys.size() == (2 * t) - 1) {
                splitChild(node, i);
                insertOpCount++;
                if (key > node.keys.get(i)) {
                    i++;
                }
            }
            insertNonFull(node.children.get(i), key);
            insertOpCount++;
        }
    }

    private void splitChild(BStarNode parentNode, int childIndex) {
        BStarNode child = parentNode.children.get(childIndex);
        BStarNode newChild = new BStarNode(child.leaf);
        parentNode.keys.add(childIndex, child.keys.get(t - 1));

        for (int j = 0; j < t - 1; j++) {
            newChild.keys.add(child.keys.get(j + t));
        }
        child.keys.subList(t - 1, 2 * t - 1).clear();

        if (!child.leaf) {
            for (int j = 0; j < t; j++) {
                newChild.children.add(child.children.get(j + t));
            }
            child.children.subList(t, 2 * t).clear();
        }
        parentNode.children.add(childIndex + 1, newChild);
    }

    public void delete(int key) {
        if (root == null) {
            System.out.println("Дерево пустое");
            return;
        }
        deleteOpCount++;
        delete(root, key);
    }

    private void delete(BStarNode node, int key) {
        int i = 0;
        while (i < node.keys.size() && key > node.keys.get(i)) {
            i++;
        }
        if (i < node.keys.size() && key == node.keys.get(i)) {
            if (node.leaf) {
                node.keys.remove(i);
                deleteOpCount++;
            } else {
                if (node.children.get(i).keys.size() >= t) {
                    int predecessor = getPredecessor(node, i);
                    deleteOpCount++;
                    node.keys.set(i, predecessor);
                    deleteOpCount++;
                    delete(node.children.get(i), predecessor);
                    deleteOpCount++;
                }
                else if (node.children.get(i + 1).keys.size() >= t) {
                    int successor = getSuccessor(node, i);
                    deleteOpCount++;
                    node.keys.set(i, successor);
                    deleteOpCount++;
                    delete(node.children.get(i + 1), successor);
                    deleteOpCount++;
                }
                else {
                    merge(node, i);
                    deleteOpCount++;
                    delete(node.children.get(i), key);
                    deleteOpCount++;
                }
            }
        } else {
            if (node.leaf) {
                System.out.println("Ключ не найден");
                return;
            }
            boolean lastChild = (i == node.keys.size());
            if (node.children.get(i).keys.size() < t) {
                deleteOpCount++;
                fill(node, i);
            }
            if (lastChild && i > node.keys.size()) {
                deleteOpCount++;
                delete(node.children.get(i - 1), key);
            } else {
                deleteOpCount++;
                delete(node.children.get(i), key);
            }
        }
    }

    private int getPredecessor(BStarNode node, int index) {
        BStarNode current = node.children.get(index);
        while (!current.leaf) {
            current = current.children.get(current.keys.size());
        }
        return current.keys.get(current.keys.size() - 1);
    }

    private int getSuccessor(BStarNode node, int index) {
        BStarNode current = node.children.get(index + 1);
        while (!current.leaf) {
            current = current.children.get(0);
        }
        return current.keys.get(0);
    }

    private void merge(BStarNode node, int index) {
        BStarNode child = node.children.get(index);
        BStarNode sibling = node.children.get(index + 1);
        child.keys.add(node.keys.get(index));
        for (int i = 0; i < sibling.keys.size(); i++) {
            child.keys.add(sibling.keys.get(i));
        }
        if (!sibling.leaf) {
            for (int i = 0; i < sibling.children.size(); i++) {
                child.children.add(sibling.children.get(i));
            }
        }
        node.keys.remove(index);
        node.children.remove(index + 1);
    }

    private void fill(BStarNode node, int index) {
        if (index != 0 && node.children.get(index - 1).keys.size() >= t) {
            borrowFromPrev(node, index);
        }
        else if (index != node.keys.size() && node.children.get(index + 1).keys.size() >= t) {
            borrowFromNext(node, index);
        }
        else {
            if (index != node.keys.size()) {
                merge(node, index);
            } else {
                merge(node, index - 1);
            }
        }
    }

    private void borrowFromPrev(BStarNode node, int index) {
        BStarNode child = node.children.get(index);
        BStarNode sibling = node.children.get(index - 1);
        child.keys.add(0, node.keys.get(index - 1));
        if (!sibling.leaf) {
            child.children.add(0, sibling.children.get(sibling.children.size() - 1));
            sibling.children.remove(sibling.children.size() - 1);
        }
        node.keys.set(index - 1, sibling.keys.get(sibling.keys.size() - 1));
        sibling.keys.remove(sibling.keys.size() - 1);
    }

    private void borrowFromNext(BStarNode node, int index) {
        BStarNode child = node.children.get(index);
        BStarNode sibling = node.children.get(index + 1);
        child.keys.add(node.keys.get(index));
        if (!sibling.leaf) {
            child.children.add(sibling.children.get(0));
            sibling.children.remove(0);
        }
        node.keys.set(index, sibling.keys.get(0));
        sibling.keys.remove(0);
    }

    public boolean search(int key) {
        searchOpCount++;
        return search(root, key);
    }

    private boolean search(BStarNode node, int key) {
        int i = 0;
        while (i < node.keys.size() && key > node.keys.get(i)) {
            i++;
        }
        if (i < node.keys.size() && key == node.keys.get(i)) {
            return true;
        }
        if (node.leaf) {
            return false;
        }
        searchOpCount++;
        return search(node.children.get(i), key);

    }

    public void print() {
        print(root);
    }

    private void print(BStarNode node) {
        if (node != null) {
            for (int i = 0; i < node.keys.size(); i++) {
                System.out.print(node.keys.get(i) + " ");
            }
            System.out.println();
            if (!node.leaf) {
                for (int i = 0; i < node.children.size(); i++) {
                    print(node.children.get(i));
                }
            }
        }
    }
    static public void nullStats(){
        insertOpCount = 0;
        deleteOpCount = 0;
        searchOpCount = 0;
    }
}
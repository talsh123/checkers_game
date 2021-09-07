/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic.Board;

/**
 *
 * @author Administrator
 */
public class BinaryTree {

    private BinaryTree right;
    private BinaryTree left;
    private BinaryTree parent;
    private int info;

    public BinaryTree(BinaryTree right, BinaryTree left, BinaryTree parent, int info) {
        this.right = right;
        this.left = left;
        this.parent = parent;
        this.info = info;
    }

    public void setRight(BinaryTree right) {
        this.right = right;
    }

    public void setLeft(BinaryTree left) {
        this.left = left;
    }

    public void setParent(BinaryTree parent) {
        this.parent = parent;
    }

    public BinaryTree getRight() {
        return this.right;
    }

    public BinaryTree getLeft() {
        return this.left;
    }

    public BinaryTree getParent() {
        return this.parent;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public int getInfo() {
        return this.info;
    }

}

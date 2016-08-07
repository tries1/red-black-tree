import lombok.Data;

import java.awt.*;

/**
 * Created by jsh on 2016-08-07.
 *
 * 1. ���� ���� Ȥ�� �� ���� �ϳ��̴�.
 * 2. ��Ʈ ���� ���̴�.
 * 3. ��� ���� ���� ���̴�.
 * 4. ���� ����� �ڽĳ�� ������ ������ ��� ���̴�. (��, ���� ���� ���޾� ��Ÿ�� �� ������, �� ��常�� ���� ����� �θ� ��尡 �� �� �ִ�)
 * 5. � ���κ��� ���۵Ǿ� ���� ��忡 �����ϴ� ��� ��ο��� ���� ��带 �����ϸ� ��� ���� ������ �� ��尡 �ִ�.
 */
@Data
public class RedBlackTreeNode {
    private int value;
    private RedBlackTreeNode left;
    private RedBlackTreeNode right;
    private RedBlackTreeNode parent;
    private Color color;

    //�Ҿƹ���
    public RedBlackTreeNode grandParent(){
        if(parent != null){
            return parent.getParent();
        }else{
            return null;
        }
    }

    //�������
    public RedBlackTreeNode sibling(){
        if(getParent() != null){
            if(this == getParent().getLeft()){
                return getParent().getRight();

            }else{
                return getParent().getLeft();
            }
        }else{
            return null;
        }
    }

    //����
    public RedBlackTreeNode uncle(){
        return parent.sibling();
    }

    //Root ���� ������ ��� ����� Color�� Black���� �ٲ��ش�
    //2�� �Ӽ��� ���� �����ֱ� ���� �Լ��̴�.
    //���� 5�� �Ӽ��� �����Ѵ�.
    public void insertCase1(RedBlackTreeNode node) {
        if (node.getParent() == null) // root �λ���
            node.setColor(Color.BLACK); // 2�� �Ӽ�
            //5�� ��ȿ
        else
            insertCase2(node);
    }

    //�θ����� Color�� Black �� ��� Ʈ���� ��ȿ�ϴ�
    public void insertCase2(RedBlackTreeNode node) {
        if (node.getParent().getColor() == Color.BLACK) // �θ��� Color�� Black �� ��� 4�� �Ӽ� ����
            return; // Ʈ���� ��ȿ��
            //5�� ��ȿ
        else{
            insertCase3(node); // �θ��� Color�� Red �� ���
        }
    }

    public void insertCase3(RedBlackTreeNode node) {
        RedBlackTreeNode uncle = node.uncle();
        RedBlackTreeNode grandParent;

        if ((uncle != null) && (uncle.getColor() == Color.RED)) { // �θ����� Color�� Red �� ��� && ���̳���� Color�� Red �� ���
            node.getParent().setColor(Color.BLACK);
            uncle.setColor(Color.BLACK); // �θ���� ���̳��� Black
            grandParent = node.grandParent();
            grandParent.setColor(Color.RED); // �Ҿƹ��� ���� Red

            insertCase1(grandParent);  // �Ҿƹ�����尡 Root �����  2�� �Ӽ��� ���� ���� �� �ִ�.
        } else {
            insertCase4(node); // �θ����� Color�� Red �� ��� && ���̳���� Color�� Black �� ���
        }
    }

    public void insertCase4(RedBlackTreeNode node) {
        RedBlackTreeNode grandParent = node.grandParent();

        if ((node == node.getParent().getRight()) // ������ �ڽ��̰�, �θ�� ���� �ڽ��� ��
                && (node.getParent() == grandParent.getLeft())) {
            rotateLeft(node.getParent()); // ����ȸ��
            node = node.getLeft(); // ���� ����� �θ��忴�� �ڽĳ�忡 ���� ó���� �ϱ� ���ؼ� �θ���� �ּ� ����
        } else if ((node == node.getParent().getLeft()) // ���� �ڽ��̰�, �θ�� ������ �ڽ��� ��
                && (node.getParent() == grandParent.getRight())) {
            rotateRight(node.getParent());
            node = node.getRight(); //  ���� ����� �θ��忴�� �ڽĳ�忡 ���� ó���� �ϱ� ���ؼ� �θ���� �ּ� ����
        }
        insertCase5(node); // �θ��忡 ���ѿ��� ����� �θ��忴�� �ڽĳ�忡 ���� ó��, 4�� �Ӽ��� ���ѹٰ� �ִ�.
    }

    public void insertCase5(RedBlackTreeNode node) { // �θ���� Color �� Red �̰�, ���̳���� Color�� Black �� ���
        RedBlackTreeNode grandParent = node.grandParent();
        node.getParent().setColor(Color.BLACK);
        grandParent.setColor(Color.RED);
        if (node == node.getParent().getLeft()) {
            rotateRight(grandParent);
        } else {
            rotateLeft(grandParent);
        } //���ο� ��嵵 �θ��� �����ڽ�, �θ��嵵 �Ҿƹ�������� �����ڽ��̸� ������ ȸ���� �Ѵ�.
    }

    public void rotateLeft(RedBlackTreeNode node) {//���� ȸ��
        RedBlackTreeNode child = node.getRight();
        RedBlackTreeNode parent = node.getParent();

        if (child.getLeft() != null)
            child.getLeft().setParent(node);

        node.setRight(child.getLeft());
        node.setParent(child);
        child.setLeft(node);
        child.setParent(parent);

        if (parent != null) {
            if (parent.getLeft() == node)
                parent.setLeft(child);
            else
                parent.setRight(child);
        }
    }

    public void rotateRight(RedBlackTreeNode node) { // ������ ȸ��
        RedBlackTreeNode child = node.getLeft();
        RedBlackTreeNode parent = node.getParent();

        if (child.getRight() != null)
            child.getRight().setParent(node);

        node.setLeft(child.getRight());
        node.setParent(child);
        child.setRight(node);
        child.setParent(parent);

        if (parent != null) {
            if (parent.getRight() == node)
                parent.setRight(child);
            else
                parent.setLeft(child);
        }
    }

    private RedBlackTreeNode searchNode(int value) {// value �� ���� ��带 ã�´�
        RedBlackTreeNode node = root;
        while (node != null) {
            if (node.getValue() == value) {
                return node;
            } else if (node.getValue() < value) {
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
        }
        return node;
    }

    private RedBlackTreeNode leftMaximumNode(RedBlackTreeNode node) {//���ʳ�� �� ���������� ��� ã�Ƽ� ���� ū ��� , ������ȸ�� �ٷ��� ��
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }

    private void replaceNode(RedBlackTreeNode oldNode, RedBlackTreeNode newNode) { // ��ü
        if (oldNode.getParent() == null) {
            root = newNode;
        } else {
            if (oldNode == oldNode.getParent().getLeft())
                oldNode.getParent().setLeft(newNode);
            else
                oldNode.getParent().setRight(newNode);
        }
        if (newNode != null) {
            newNode.setParent(oldNode.getParent());
        }
    }

    public void delete(int value) { // ����
        RedBlackTreeNode node = searchNode(value);
        if (node == null)
            return; // ����
        if (node.getLeft() != null && node.getRight() != null) {
            RedBlackTreeNode leftMaxNode = leftMaximumNode(node.getLeft());
            node = leftMaxNode;
        }

        RedBlackTreeNode child = (node.getRight() == null) ? node.getLeft() : node.getRight();
        if (node.getColor() == Color.BLACK) { // ��尡 ���̶��
            node.setColor(child.getColor());
            deleteCase1(node);
        }
        replaceNode(node, child);
    }

    public void deleteCase1(RedBlackTreeNode node) {
        if (node.getParent() != null)
            deleteCase2(node);
    }

    public void deleteCase2(RedBlackTreeNode node) {
        RedBlackTreeNode sibling = node.sibling();
        if (sibling.getColor() == Color.RED) { // ������ color�� �������̸�
            node.getParent().setColor(Color.RED);
            sibling.setColor(Color.BLACK); // color�� �ٲ��ְ�
            if(node == node.getParent().getLeft()){
                rotateLeft(node.getParent()); // ���� �ڽ��̸� �������� �����ְ�
            }else{
                rotateRight(node.getParent());// ������ �ڽ��̸� �������� �����ְ�
            }
        }
        deleteCase3(node);
    }

    public void deleteCase3(RedBlackTreeNode node) {
        RedBlackTreeNode sibling = node.sibling();
        if ((node.getParent().getColor() == Color.BLACK)
                && (sibling.getColor() == Color.BLACK)
                && (sibling.getLeft().getColor() == Color.BLACK)
                && (sibling.getRight().getColor() == Color.BLACK)) {
            sibling.setColor(Color.RED);
            deleteCase1(node.getParent()); // �Ӽ� 5 ����, �ٽ� ���ư���
        } else
            deleteCase4(node);
    }

    public void deleteCase4(RedBlackTreeNode node) {
        RedBlackTreeNode sibling = node.sibling();
        if ((node.getParent().getColor() == Color.RED)
                && (sibling.getColor() == Color.BLACK)
                && (sibling.getLeft().getColor() == Color.BLACK)
                && (sibling.getRight().getColor() == Color.BLACK)) {
            sibling.setColor(Color.RED);
            node.getParent().setColor(Color.BLACK);
        } else
            deleteCase5(node);
    }

    public void deleteCase5(RedBlackTreeNode node) {
        RedBlackTreeNode sibling = node.sibling();
        if (sibling.getColor() == Color.BLACK) {
            if ((node == node.getParent().getLeft())
                    && (sibling.getRight().getColor() == Color.BLACK)
                    && (sibling.getLeft().getColor() == Color.RED)) {
                sibling.setColor(Color.RED);
                sibling.getLeft().setColor(Color.BLACK);
                rotateRight(sibling);
            } else if ((node == node.getParent().getRight())
                    && (sibling.getLeft().getColor() == Color.BLACK)
                    && (sibling.getRight().getColor() == Color.RED)) {
                sibling.setColor(Color.RED);
                sibling.getRight().setColor(Color.BLACK);
                rotateLeft(sibling);
            }
        }
        deleteCase6(node);
    }

    public void deleteCase6(RedBlackTreeNode node) {
        RedBlackTreeNode sibling = node.sibling();
        sibling.setColor(node.getParent().getColor());
        node.getParent().setColor(Color.BLACK);

        if(node == node.getParent().getLeft()){
            sibling.getRight().setColor(Color.BLACK);
            rotateLeft(node.getParent());
        }else{
            sibling.getLeft().setColor(Color.BLACK);
            rotateRight(node.getParent());
        }
    }

}

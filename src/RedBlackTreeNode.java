import lombok.Data;

import java.awt.*;

/**
 * Created by jsh on 2016-08-07.
 *
 * 1. 노드는 레드 혹은 블랙 중의 하나이다.
 * 2. 루트 노드는 블랙이다.
 * 3. 모든 리프 노드는 블랙이다.
 * 4. 레드 노드의 자식노드 양쪽은 언제나 모두 블랙이다. (즉, 레드 노드는 연달아 나타날 수 없으며, 블랙 노드만이 레드 노드의 부모 노드가 될 수 있다)
 * 5. 어떤 노드로부터 시작되어 리프 노드에 도달하는 모든 경로에는 리프 노드를 제외하면 모두 같은 개수의 블랙 노드가 있다.
 */
@Data
public class RedBlackTreeNode {
    private int value;
    private RedBlackTreeNode left;
    private RedBlackTreeNode right;
    private RedBlackTreeNode parent;
    private Color color;

    //할아버지
    public RedBlackTreeNode grandParent(){
        if(parent != null){
            return parent.getParent();
        }else{
            return null;
        }
    }

    //형제노드
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

    //삼촌
    public RedBlackTreeNode uncle(){
        return parent.sibling();
    }

    //Root 노드로 시작한 경우 노드의 Color를 Black으로 바꿔준다
    //2번 속성을 만족 시켜주기 위한 함수이다.
    //또한 5번 속성을 만족한다.
    public void insertCase1(RedBlackTreeNode node) {
        if (node.getParent() == null) // root 로삽입
            node.setColor(Color.BLACK); // 2번 속성
            //5번 유효
        else
            insertCase2(node);
    }

    //부모노드의 Color가 Black 인 경우 트리는 유효하다
    public void insertCase2(RedBlackTreeNode node) {
        if (node.getParent().getColor() == Color.BLACK) // 부모의 Color가 Black 인 경우 4번 속성 만족
            return; // 트리가 유효함
            //5번 유효
        else{
            insertCase3(node); // 부모의 Color가 Red 인 경우
        }
    }

    public void insertCase3(RedBlackTreeNode node) {
        RedBlackTreeNode uncle = node.uncle();
        RedBlackTreeNode grandParent;

        if ((uncle != null) && (uncle.getColor() == Color.RED)) { // 부모노드의 Color가 Red 인 경우 && 삼촌노드의 Color가 Red 인 경우
            node.getParent().setColor(Color.BLACK);
            uncle.setColor(Color.BLACK); // 부모노드와 삼촌노드는 Black
            grandParent = node.grandParent();
            grandParent.setColor(Color.RED); // 할아버지 노드는 Red

            insertCase1(grandParent);  // 할아버지노드가 Root 노드라면  2번 속성을 만족 안할 수 있다.
        } else {
            insertCase4(node); // 부모노드의 Color가 Red 인 경우 && 삼촌노드의 Color가 Black 인 경우
        }
    }

    public void insertCase4(RedBlackTreeNode node) {
        RedBlackTreeNode grandParent = node.grandParent();

        if ((node == node.getParent().getRight()) // 오른쪽 자식이고, 부모는 왼쪽 자식일 때
                && (node.getParent() == grandParent.getLeft())) {
            rotateLeft(node.getParent()); // 왼쪽회전
            node = node.getLeft(); // 원래 노드의 부모노드였던 자식노드에 대한 처리를 하기 위해서 부모노드로 주소 변경
        } else if ((node == node.getParent().getLeft()) // 왼쪽 자식이고, 부모는 오른쪽 자식일 때
                && (node.getParent() == grandParent.getRight())) {
            rotateRight(node.getParent());
            node = node.getRight(); //  원래 노드의 부모노드였던 자식노드에 대한 처리를 하기 위해서 부모노드로 주소 변경
        }
        insertCase5(node); // 부모노드에 대한원래 노드의 부모노드였던 자식노드에 대한 처리, 4번 속성을 위한바고 있다.
    }

    public void insertCase5(RedBlackTreeNode node) { // 부모노드는 Color 가 Red 이고, 삼촌노드의 Color가 Black 인 경우
        RedBlackTreeNode grandParent = node.grandParent();
        node.getParent().setColor(Color.BLACK);
        grandParent.setColor(Color.RED);
        if (node == node.getParent().getLeft()) {
            rotateRight(grandParent);
        } else {
            rotateLeft(grandParent);
        } //새로운 노드도 부모의 왼쪽자식, 부모노드도 할아버지노드의 왼쪽자식이면 오른쪽 회전을 한다.
    }

    public void rotateLeft(RedBlackTreeNode node) {//왼쪽 회전
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

    public void rotateRight(RedBlackTreeNode node) { // 오른쪽 회전
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

    private RedBlackTreeNode searchNode(int value) {// value 에 따른 노드를 찾는다
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

    private RedBlackTreeNode leftMaximumNode(RedBlackTreeNode node) {//왼쪽노드 중 오른쪽으로 계속 찾아서 가장 큰 노드 , 중위순회시 바로전 값
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }

    private void replaceNode(RedBlackTreeNode oldNode, RedBlackTreeNode newNode) { // 교체
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

    public void delete(int value) { // 삭제
        RedBlackTreeNode node = searchNode(value);
        if (node == null)
            return; // 없음
        if (node.getLeft() != null && node.getRight() != null) {
            RedBlackTreeNode leftMaxNode = leftMaximumNode(node.getLeft());
            node = leftMaxNode;
        }

        RedBlackTreeNode child = (node.getRight() == null) ? node.getLeft() : node.getRight();
        if (node.getColor() == Color.BLACK) { // 노드가 블랙이라면
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
        if (sibling.getColor() == Color.RED) { // 형제의 color가 빨간색이면
            node.getParent().setColor(Color.RED);
            sibling.setColor(Color.BLACK); // color를 바꿔주고
            if(node == node.getParent().getLeft()){
                rotateLeft(node.getParent()); // 왼쪽 자식이면 왼쪽으로 돌려주고
            }else{
                rotateRight(node.getParent());// 오른쪽 자식이면 왼쪽으로 돌려주고
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
            deleteCase1(node.getParent()); // 속성 5 위반, 다시 돌아간다
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

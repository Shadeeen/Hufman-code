package application;

public class Node<T> implements Comparable<Node<T>> {

	private int freq;
	private Node<T> left;
	private Node<T> right;
	private T data;

	public Node(T data, int freq) {
		super();
		this.freq = freq;
		this.data = data;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Node<T> getLeft() {
		return left;
	}

	public void setLeft(Node<T> left) {
		this.left = left;
	}

	public Node<T> getRight() {
		return right;
	}

	public void setRight(Node<T> right) {
		this.right = right;
	}

	public boolean hasRight() {
		return this.right != null;
	}

	public boolean hasLeft() {
		return this.left != null;
	}

	public boolean isLeaf() {
		return this.right == null && this.left == null;
	}

	@Override
	public int compareTo(Node<T> o) {
		if (this.freq > o.getFreq())
			return 1;
		else if (this.freq < o.getFreq())
			return -1;
		return 0;
	}

	@Override
	public String toString() {
		return "TNode [freq=" + freq + ", data=" + data + "]";
	}
}

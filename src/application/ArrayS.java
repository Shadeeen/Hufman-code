package application;

public class ArrayS<T extends Comparable<T>> {

	private T[] arr;
	private int top = -1;

	public ArrayS(int capacity) {
		arr = (T[]) new Comparable[capacity];
	}

	public boolean isEmpty() {
		return top == -1;
	}

	public void push(T data) {
		if (top + 1 >= arr.length)
			resize();

		arr[++top] = data;
	}

	public T pop() {
		if (!isEmpty()) {
			T peek = arr[top];
			top--;
			return peek;
		}
		return null;
	}

	public T peek() {
		if (isEmpty())
			return null;
		return arr[top];
	}

	public void clear() {
		top = -1;
	}

	public String toString() {
		String res = "Top--> ";
		for (int i = top; i >= 0; i--)
			res += "[" + arr[i] + "]--> ";
		return res + "Null";
	}

	private void resize() {
		T[] newArr = (T[]) new Comparable[arr.length * 2];
		for (int i = 0; i < arr.length; i++)
			newArr[i] = arr[i];
		this.arr = newArr;
	}

	private void shrink() {
		T[] newArr = (T[]) new Comparable[arr.length / 2];
		for (int i = 0; i < newArr.length; i++)
			newArr[i] = arr[i];
		this.arr = newArr;
	}
}

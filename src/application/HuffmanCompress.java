package application;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HuffmanCompress {

	private Node<Character> root;
	private int[] frequencies;
	private int numberOfChars;
	private int[] lengths;
	private String[] huff;
	private byte[] buffer; // data is stored here before being written to the compressed file
	private File originalFile;

	public HuffmanCompress(String pathOfOriginalFile, CompressScreen c) {

		originalFile = new File(pathOfOriginalFile);
		frequencies = new int[256];
		lengths = new int[256];
		huff = new String[256];
		if (originalFile.exists() && !getFileExtension(originalFile).equals(".huf"))
			compress(c);
	}

	private void compress(CompressScreen c) {

		try {
			BufferedInputStream inStream1 = new BufferedInputStream(new FileInputStream(originalFile));
			int val1;
			while ((val1 = inStream1.read()) != -1) {
				frequencies[val1]++;
			}

			inStream1.close();
			buildHuffmanTree();
			encode();

			String parentDirectory = originalFile.getParent();
			String compressedFileName = originalFile.getName().replace(getFileExtension(originalFile), ".huf");
			File compressedFile = new File(parentDirectory, compressedFileName);
			FileOutputStream fileOutputStream = new FileOutputStream(compressedFile, false);

			// Wrap the FileOutputStream in a BufferedOutputStream
			BufferedOutputStream outStream = new BufferedOutputStream(fileOutputStream);

			writeHeaderToCompressedFile(outStream);
			buffer = new byte[8];
			refillBufferWithZeros();
			int pointer = 0;

			BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(originalFile));
			int val;
			while ((val = inStream.read()) != -1) {
				int byteNumber = pointer / 8;
				int bitNumber = pointer % 8;
				String huffCode = huff[val];
				int length = this.lengths[val];

				for (int i = 0; i < length; i++) {
					byte bitValue = (byte) Integer.parseInt(huffCode.substring(i, i + 1));
					if (pointer == buffer.length * 8) {
						outStream.write(buffer);
						refillBufferWithZeros();
						pointer = 0;
						byteNumber = 0;
						bitNumber = 0;
					}
					bitValue = (byte) (bitValue << (7 - bitNumber));
					buffer[byteNumber] = (byte) (buffer[byteNumber] | bitValue);
					pointer++;
					byteNumber = pointer / 8;
					bitNumber = pointer % 8;
				}

				if (pointer == buffer.length * 8) {
					outStream.write(buffer);
					refillBufferWithZeros();
					pointer = 0;

				}

			}
			if (pointer != 0)
				outStream.write(buffer, 0, ((pointer / 8) + 1));

			inStream.close();
			outStream.close();

			c.getS().getFileSize().setText(originalFile.length() + "");
			c.getS().getNewFileSize().setText(compressedFile.length() + "");

			if (originalFile.length() == 0) {
				c.getS().getCompressionRatio().setText("N/A");
			} else {
				double compressRatio = (1 - (double) compressedFile.length() / originalFile.length()) * 100;
				c.getS().getCompressionRatio().setText(String.format("%.2f", compressRatio) + "%");
			}
			huffmanInformatonButton(c);
			headerInformationButton(compressedFile, c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void headerInformationButton(File compressedFile, CompressScreen c) {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px;");

		try (DataInputStream inStream = new DataInputStream(
				new BufferedInputStream(new FileInputStream(compressedFile)))) {
			// Read original file extension
			String fileExtension = inStream.readUTF();
			// Read size of the original file
			int originalFileSize = inStream.readInt();
			// Read size of the header
			int headerSize = inStream.readInt();
			// Read and parse the Huffman tree structure
			byte[] headerBuffer = new byte[headerSize];
			inStream.read(headerBuffer);

			Label extensionLabel = new Label("File Extension-File size-header Size:" + fileExtension + " - "
					+ originalFileSize + " - " + headerSize);

			extensionLabel.setStyle("-fx-font-weight: bold;");
			grid.add(extensionLabel, 0, 0);

			StringBuilder huffmanTreeStructure = new StringBuilder();
			Label headerLabel = new Label();
			headerLabel.setStyle("-fx-font-weight: bold;");
			for (byte b : headerBuffer) {
				for (int i = 7; i >= 0; i--) {
					huffmanTreeStructure.append((b >> i) & 1);
				}
			}

			headerLabel.setText("" + huffmanTreeStructure);

			grid.add(headerLabel, 0, 1);
			c.getHeader().setOnAction(e -> {
				ScrollPane scrollPane = new ScrollPane();
				scrollPane.setContent(grid);
				Scene s = new Scene(scrollPane, 800, 200);
				Stage st = new Stage();
				st.setScene(s);
				st.show();
			});
		} catch (IOException e) {
			System.err.println("Error reading the header of the compressed file: " + e.getMessage());
		}
	}

	private Button doneHuffmanButton;

	public void huffmanInformatonButton(CompressScreen c) {
		GridPane gp = new GridPane(10, 10);

		gp.add(new Text("Char"), 0, 0);
		gp.add(new Text("Freq"), 1, 0);
		gp.add(new Text("Code"), 2, 0);
		gp.add(new Text("Bits"), 3, 0);

		for (int i = 0, row = 1; i < 256; i++) {
			if (huff[i] != null && frequencies[i] > 0) {
				gp.add(new Text(" " + (char) i), 0, row);
				gp.add(new Text(" " + frequencies[i]), 1, row);
				gp.add(new Text(" " + huff[i]), 2, row);
				gp.add(new Text(" " + lengths[i]), 3, row);
				row++;
			}
		}

		gp.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';");
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(gp);
		scrollPane.setPrefSize(500, 300);
		scrollPane.setStyle("-fx-background: #fffee0;");
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setFitToWidth(true);

		doneHuffmanButton = new Button("Done");
		c.scale(doneHuffmanButton);
		doneHuffmanButton.setStyle(
				"-fx-background-color: #007BFF;-fx-text-fill: white;-fx-font-size: 15px;-fx-background-radius: 30px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';");

		Label huffmanDetails = new Label("Huffman Details:");
		huffmanDetails.setStyle(
				"-fx-font-size: 30px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';-fx-text-fill: white;-fx-background-color: #007BFF;-fx-background-radius: 20px;");
		Stage stage = new Stage();
		c.getHuffman().setOnAction(e -> {
			BorderPane bp = new BorderPane();
			bp.setCenter(scrollPane);
			bp.setRight(doneHuffmanButton);
			bp.setAlignment(scrollPane, Pos.CENTER);
			bp.setMargin(scrollPane, new Insets(50));
			bp.setAlignment(doneHuffmanButton, Pos.BOTTOM_RIGHT);
			bp.setMargin(doneHuffmanButton, new Insets(0, 10, 10, 0));
			bp.setTop(huffmanDetails);
			bp.setAlignment(huffmanDetails, Pos.TOP_CENTER);
			bp.setStyle("-fx-background-color:#fffee0");
			Scene scene = new Scene(bp, 700, 360);
			stage.setScene(scene);
			c.getStage().close();
			stage.show();

		});

		doneHuffmanButton.setOnAction(e -> {
			stage.close();
			c.getStage().show();

		});
	}

	private void buildHuffmanTree() {

		numberOfChars = 0;
		for (int i = 0; i < 256; i++)
			if (frequencies[i] != 0)
				numberOfChars += 1;

		MinHeap<Node<Character>> heap = new MinHeap<>(numberOfChars);

		for (int i = 0; i < frequencies.length; i++) { // fill the heap with characters with their frequencies
			if (frequencies[i] != 0) {
				Node<Character> node = new Node<>((char) i, frequencies[i]);
				heap.add(node);
			}
		}

		while (heap.getSize() > 1) {
			Node<Character> x = heap.deleteMin();
			Node<Character> y = heap.deleteMin();
			Node<Character> newNode = new Node<>(null, (x.getFreq() + y.getFreq()));
			newNode.setLeft(x);
			newNode.setRight(y);
			heap.add(newNode);
		}

		root = heap.deleteMin(); // after exiting the while loop, the heap has only 1 node, which is the root
	}

	private void encode() {
		if (root != null)
			encode(root, "", 0);
	}

	private void encode(Node<Character> node, String encoding, int depth) {
		if (node.isLeaf()) {
			huff[node.getData()] = encoding;
			lengths[node.getData()] = (depth);
			return;
		}

		if (node.hasLeft()) {
			encode(node.getLeft(), encoding + 0, (depth + 1));
		}

		if (node.hasRight()) {
			encode(node.getRight(), encoding + 1, (depth + 1));
		}
	}

	// write header to compressed file
	private void writeHeaderToCompressedFile(BufferedOutputStream outputStream) {
		try {
			buffer = new byte[getHeaderSize()];
			refillBufferWithZeros();
			DataOutputStream outStream = new DataOutputStream(outputStream);
			outStream.writeUTF(getFileExtension(originalFile)); // write original file extension
			outStream.writeInt(root.getFreq()); // write size of original file
			outStream.writeInt(getHeaderSize()); // write size of the header
			String huffmanCodingTree = CreateHuffmanCodingTree(); // get huffman coding tree
			int pointer = 0;
			int byteNumber;
			int bitNumber;
			for (int i = 0; i < huffmanCodingTree.length(); i++) {
				String nextChar = huffmanCodingTree.substring(i, i + 1); // get character at index i
				if (Integer.parseInt(nextChar) == 0) {
					pointer++; // buffer initially stores 0 values, so go to next position
				} else if (Integer.parseInt(nextChar) == 1) {
					byteNumber = pointer / 8;
					bitNumber = pointer % 8;
					buffer[byteNumber] = (byte) (buffer[byteNumber] | (1 << (7 - bitNumber)));
					pointer++;
				}
			}
			outStream.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// refill the buffer array with zeros for reusing
	private void refillBufferWithZeros() {
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = 0;
	}

	// count the leaves of Huffman tree
	private int countLeaves() {
		return countLeaves(root);
	}

	// helper recursive method for counting the leaves of Huffman tree
	private int countLeaves(Node<Character> curr) {
		if (curr == null)
			return 0;
		if (curr.isLeaf())
			return 1;
		return countLeaves(curr.getLeft()) + countLeaves(curr.getRight());
	}

	// count the parents (non-leafs) in Huffman tree
	private int countParents() {
		return countParents(root);
	}

	// helper recursive method for counting the parents in Huffman tree
	private int countParents(Node<Character> curr) {
		if (curr == null)
			return 0;
		if (curr.isLeaf())
			return 0;
		return 1 + countParents(curr.getLeft()) + countParents(curr.getRight());
	}

	// get file extension
	private String getFileExtension(File file) {
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index == -1) {
			return ""; // empty extension
		}
		return name.substring(index);
	}

	// this method creates the huffman coding tree to be written in the header in
	// the compressed file
	public String CreateHuffmanCodingTree() {
		return traversePostOrder(root) + 0;
	}

	// traverse Huffman tree to get the header
	private String traversePostOrder(Node<Character> curr) {
		if (curr == null)
			return "";
		String header = traversePostOrder(curr.getLeft()) + traversePostOrder(curr.getRight());
		if (curr.isLeaf()) {
			int data = curr.getData();
			header += 1 + "" + getBinaryRepresnetation(data);
		} else
			header += 0;
		return header;
	}

	// this method returns the size of header in bytes
	public int getHeaderSize() {
		return (int) (countLeaves() + Math.ceil((countLeaves() + countParents() + 1) / 8.0));
	}

	private String getBinaryRepresnetation(int val) {
		String binaryString = Integer.toBinaryString(val); // Convert the value to a binary string
		return String.format("%8s", binaryString).replace(' ', '0');
	}

}
package application;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class HuffmanDecompress {

	private Node<Integer> root;
	private byte[] buffer; // data is stored here before being written to the compressed file
	private int sizeOfOriginalFile;
	private int sizeOfHeader;
	private File compressedFile;
	private String originalFileExtension;

	public HuffmanDecompress(String pathOfCompressedFile) {
		super();

		this.compressedFile = new File(pathOfCompressedFile);
		if (compressedFile.exists() && getFileExtension(compressedFile).equals(".huf"))
			readCompressedFile();
	}

	private void readCompressedFile() {
		try {
			DataInputStream inStream = new DataInputStream(
					new BufferedInputStream(new FileInputStream(compressedFile)));
			originalFileExtension = inStream.readUTF(); // read original File extension
			sizeOfOriginalFile = inStream.readInt(); // read size of original file
			sizeOfHeader = inStream.readInt(); // read size of the header
			buildHuffmanTree(inStream); // build huffman tree by reading the header information
			readDecompressedFile(inStream);
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// read the header information to build huffman tree
	private void buildHuffmanTree(DataInputStream inStream) {
		ArrayS<Node<Integer>> stack = new ArrayS<Node<Integer>>(256);
		buffer = new byte[sizeOfHeader];
		try {
			inStream.read(buffer);
			int pointer = 0;
			while (pointer < (((sizeOfHeader * 8) / 10) * 10) - 1) {
				int byteNumber = pointer / 8;
				int bitNumber = pointer % 8;
				byte temp = (byte) (buffer[byteNumber] << bitNumber);
				temp = (byte) (temp >> 7); // we need only the bit to which the pointer points (0 or 1)
				temp = (byte) (temp & 1);

				if (temp == 0) { // non-leaf node
					Node<Integer> right = stack.pop();
					Node<Integer> left = stack.pop();
					Node<Integer> newNode = new Node<>(null, 0);
					newNode.setRight(right);
					newNode.setLeft(left);
					stack.push(newNode);
					pointer++;

				}

				else if (temp == 1) { // leaf node
					pointer++; // to start reading the next character
					byteNumber = pointer / 8;
					bitNumber = pointer % 8;
					int firstVal = (buffer[byteNumber] << bitNumber);
					int numberOfRemainingBits = bitNumber;
					if (numberOfRemainingBits > 0) {
						byteNumber += 1;
						pointer = byteNumber * 8;
						bitNumber = 0; // will be zero
						int secondVal = (buffer[byteNumber] >> (8 - numberOfRemainingBits)); // shift right with sign
																								// extension
						byte val2 = 0;
						for (int j = 0; j < numberOfRemainingBits; j++) {
							val2 = (byte) (val2 << 1); // shift left
							val2 = (byte) (val2 | 1); // make first digit = 1
						}
						secondVal = (byte) (secondVal & val2);
						byte asciiCode = (byte) (firstVal | secondVal); // get ASCII code for the character
						Node<Integer> newNode = new Node<>((int) asciiCode, 0);
						stack.push(newNode);
						pointer += numberOfRemainingBits;
					} else {
						byte asciiCode = (byte) firstVal;
						Node<Integer> newNode = new Node<>((int) asciiCode, 0);
						stack.push(newNode);
						pointer += 8;
					}
				}
			}
			root = stack.pop(); // tree is created

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// refill the buffer array with zeros for reusing
	private void zerosBuffer() {
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = 0;
	}

	// Process the compressed file and create the decompressed output
	private void readDecompressedFile(DataInputStream inputStream) {
		Node<Integer> currentNode = root; // Start from the root of the Huffman tree
		try (BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(new File(compressedFile.getParent(), compressedFile.getName()
						.replace(getFileExtension(compressedFile), "_Decompressed" + originalFileExtension))))) {

			// Initialize a buffer to store the bytes being processed
			buffer = new byte[8];
			zerosBuffer(); // Clear the buffer initially
			inputStream.read(buffer); // Read the first set of bytes from the compressed file

			int processedCharacters = 0; // Keep track of how many characters have been decompressed
			while (processedCharacters < sizeOfOriginalFile) {
				int bitIndex = -1; // Start see bits from the beginning of the buffer
				while (bitIndex < (buffer.length * 8) - 1) {
					bitIndex++;

					// Check if the current node is a leaf node
					if (currentNode.isLeaf()) {
						processedCharacters++;
						outputStream.write(currentNode.getData()); // Write the decompressed character to the output
																	// file
						currentNode = root; // Reset to the root of the Huffman tree for the next character
						if (processedCharacters == sizeOfOriginalFile) // Stop if all characters are decompressed
							break;
					}

					// Determine the current bits value (0 or 1)
					int byteIndex = bitIndex / 8; // Determine which byte the current bit belongs to
					int bitPosition = bitIndex % 8; // Determine the position of the bit within the byte
					byte currentBit = (byte) (buffer[byteIndex] << bitPosition); // Shift the bit to the MSB
					currentBit = (byte) ((currentBit >> 7) & 1); // Extract the bit value (0 or 1)

					// Traverse the Huffman tree based on the bit value
					if (currentBit == 0) {
						currentNode = currentNode.getLeft(); // Move to the left child for 0
					} else if (currentBit == 1) {
						currentNode = currentNode.getRight(); // Move to the right child for 1
					}
				}

				// Refill the buffer for the next set of bytes
				buffer = new byte[8];
				zerosBuffer();
				inputStream.read(buffer);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// get file extension
	private String getFileExtension(File file) {
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index == -1) {
			return "";
		}
		return name.substring(index);
	}
}

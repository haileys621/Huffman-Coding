import java.io.*;
import java.util.*;

public class Huffman{
    static PriorityQueue<HuffmanNode> queue;
    static HuffmanNode root;
    static HashMap<Character, String> codeTable;
    
    public static void createHuffman(ArrayList characters, ArrayList frequencies){
        queue = new PriorityQueue<HuffmanNode>();
        codeTable = new HashMap<Character, String>();
        for(int i = 0; i < characters.size(); i++){
            HuffmanNode node = new HuffmanNode((char)characters.get(i), (int)frequencies.get(i));
            queue.add(node);
        }
        
        while(queue.size() > 1){
            HuffmanNode n1 = queue.remove();
            HuffmanNode n2 = queue.remove();
            HuffmanNode newNode = new HuffmanNode('\0', (n1.freq + n2.freq));
            newNode.left = n2;
            newNode.right = n1;
            queue.add(newNode);
            root = newNode;
        }
    }
    
    public static void printCodes(HuffmanNode n, String code){
        if(n.left == null && n.right == null){
            System.out.println(n.ch + " : " + code);
            codeTable.put(n.ch, code);
            return;
        }
        String leftCode = code + "0";
        String rightCode = code + "1";
        printCodes(n.left, leftCode);
        printCodes(n.right, rightCode);
    } 
    
    public static String decode(String encodedStr) throws Exception{
        String decodedStr = "";
        HuffmanNode node = root;
        for(int i = 0; i < encodedStr.length(); i++){
            if(encodedStr.charAt(i) == '0'){
                node = node.left;
                if(node.left == null && node.right == null){
                    decodedStr += node.ch;
                    node = root;
                }
                else if(i == encodedStr.length()-1){
                    throw new Exception("ERROR: entered code contains erroneous values");
                }
            }
            else if(encodedStr.charAt(i) == '1'){
                node = node.right;
                if(node.left == null && node.right == null){
                    decodedStr += node.ch;
                    node = root;
                }
                else if(i == encodedStr.length()-1){
                    throw new Exception("ERROR: entered code contains erroneous values");
                }
            }
        }
        return decodedStr;
    }
    
    public static String encode(String decodedStr) throws Exception{
        String encodedStr = "";
        for(int i = 0; i < decodedStr.length(); i++){
            if(codeTable.containsKey(decodedStr.charAt(i))){
                encodedStr += codeTable.get(decodedStr.charAt(i));
            }
           
            else{
                throw new Exception("ERROR: Characters are not valid for encoding");
            }
        }
        return encodedStr;
    }

    public static void main(String args[]) throws Exception{
        File file = new File(args[0]);
        ArrayList<Character> characters = new ArrayList<Character>();
        ArrayList<Integer> frequencies = new ArrayList<Integer>();
        int pastLine = 1;
        int currentLine = 0;
        try{
            Scanner fileReader = new Scanner(file);
            while(fileReader.hasNextLine()){
                String str = fileReader.nextLine();
                currentLine++;
                if(currentLine != pastLine){
                    if (characters.contains('\n')){
                        int idx = characters.indexOf('\n');
                        frequencies.set(idx, frequencies.get(idx) + 1);
                    }
                    else{
                        characters.add('\n');
                        frequencies.add(1);
                    }
                    pastLine++;
                }
                for(int i = 0; i < str.length(); i++){
                    if (characters.contains(str.charAt(i))){
                        int idx = characters.indexOf(str.charAt(i));
                        frequencies.set(idx, frequencies.get(idx) + 1);
                    }
                    else{
                        characters.add(str.charAt(i));
                        frequencies.add(1);
                    }
                }
                
            } 
        }
        catch(FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        createHuffman(characters, frequencies);
        printCodes(root, "");
        System.out.println("Please enter a series of zeroes and ones to be decoded: ");
        String encodedStr = System.console().readLine();
        System.out.println(decode(encodedStr));
        System.out.println("Please enter a series of characters to be encoded: ");
        String decodedStr = System.console().readLine();
        System.out.println(encode(decodedStr));
    }
}

class HuffmanNode implements Comparable<HuffmanNode>{
    int freq;
    char ch;
    HuffmanNode left;
    HuffmanNode right;
    public HuffmanNode(char c, int f){
        ch = c;
        freq = f;
        left = null;
        right = null;
    }
    public int compareTo(HuffmanNode other) {
        if(this.freq < other.freq){
            return -1;
        }
        else if(this.freq > other.freq){
            return 1;
        }
        else{
            return 0;
        }
    }
}
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/* authour Ciara Lynch
* Implementation of Algorithm based on TST algorithm found in the Algorithms 4th Edition Textbook by Robert Sedgewick and Kevin Wayne. 
* Copyright © 2000–2019
* https://algs4.cs.princeton.edu/13stacks/Bag.java.html
*/

public class TST<Value> {
    private int n;              // size
    private Node<Value> root;   // root of TST

    private static class Node<Value> {
        private char c;                        // character
        private Node<Value> left, mid, right;  // left, middle, and right subtries
        private Value val;                     // value associated with string
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TST(String file) {
    }


    public static void main(String[] args) throws IOException{
        Scanner wordScan = new Scanner(System.in);
        /*String stopNames = "stops.txt";
        int sizeStopNames = arraySize(stopNames);
        System.out.println("Size: "+sizeStopNames);*/

        /*String answer = "";
        System.out.println("Please enter the Bus Stop you require: ");
        answer = wordScan.nextLine();
        String[] validChecker = answer.split(",");*/

        String filename = "stops.txt";
        if (filename != null) {
            try {
                BufferedReader readIn = new BufferedReader(new FileReader(filename));
                String str;
                List<String> list = new ArrayList<String>();
                while((str = readIn.readLine()) != null){
                    list.add(str);
                }
                String[] stringArr = list.toArray(new String[0]);
                //System.out.print(stringArr[1]);
                for (int count =1; count < stringArr.length; count++)
                {
                    String indexNode = stringArr[count];
                    String[] addressSplit = indexNode.split(",",4);
                    String addressSpecific = addressSplit[2];
                    String[] keyWordSplit = addressSpecific.split(" ",2);
                    addressSpecific = keyWordSplit[1] + " " + keyWordSplit[0];
                   addressSplit[2] = addressSpecific;
                   String addressHold = "";
                   
                   for (int i =0; i < addressSplit.length; i++){
                    addressHold = addressHold + addressSplit[i] +  ",";
                       //addressHold = addressHold.concat(addressSplit[i]);
                   }
                   stringArr[count] = addressHold;
                }
                TST<Integer> tst = new TST<Integer>(str);
                for (int count = 1; count < stringArr.length; count++){
                    tst.put(stringArr[count], count-1);
                }
                readIn.close();
                //Test get method on split string from tree
               /* int test = tst.get("1477,51465,HASTINGS ST FS WILLINGDON AVE WB,HASTINGS ST @ WILLINGDON AVE,49.281124,-123.003973,ZN 99, ,0,,");
                System.out.println(test);*/
            
                System.out.println(interfaceCall("stops.txt", "HASTINGS ST"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Method to be called for Part 4 User Interface
    * Takes in the file and user input and returns the 
    * requested bus stop strings
    */

    public static String interfaceCall(String file, String userInputString) {
        Scanner wordScan = new Scanner(System.in);
        String fileName = file;
       // String interfaceString ="";
        String answer = "";
        if (fileName != null) {
            try {
                BufferedReader readIn = new BufferedReader(new FileReader(fileName));
                String str;
                List<String> list = new ArrayList<String>();
                while((str = readIn.readLine()) != null){
                    list.add(str);
                }
                String[] stringArr = list.toArray(new String[0]);
                //System.out.print(stringArr[1]);
                for (int count =1; count < stringArr.length; count++)
                {
                    String indexNode = stringArr[count];
                    String[] addressSplit = indexNode.split(",",4);
                    String addressSpecific = addressSplit[2];
                    String[] keyWordSplit = addressSpecific.split(" ",2);
                    addressSpecific = keyWordSplit[1] + " " + keyWordSplit[0];
                   addressSplit[2] = addressSpecific;
                   String addressHold = "";
                   
                   for (int i =0; i < addressSplit.length; i++){
                    addressHold = addressHold + addressSplit[i] +  ",";
                       //addressHold = addressHold.concat(addressSplit[i]);
                   }
                   stringArr[count] = addressHold;
                }
                TST<Integer> tst = new TST<Integer>(str);
                for (int count = 1; count < stringArr.length; count++){
                    tst.put(stringArr[count], count-1);
                }
                readIn.close();

                
                for (int i =1; i < stringArr.length; i++){
                    String[] indexArr;
                    indexArr = stringArr[i].split(",");
                    if (indexArr !=null && indexArr[2].contains(userInputString))
                    {
                        answer = answer + "\n" + stringArr[i];
                    }
                }
                return answer;

                /*int userReturn = tst.get(userInputString);
                *interfaceString = String.valueOf(userReturn);
                return interfaceString;*/
            
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return answer;
        
    }

    
    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    // return subtrie corresponding to given key
    private Node<Value> get(Node<Value> x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(key)) n++;
        else if(val == null) n--;       // delete existing key
        root = put(root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<Value>();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> keys() {
    	//Queue<String> queue = new Queue<String>();
    	Queue<String>queue = new LinkedList<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        //Queue<String> queue = new Queue<String>();
        Queue<String>queue = new LinkedList<String>();
        Node<Value> x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.add(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.val != null) queue.add(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }


    


    
}
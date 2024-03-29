//Minimum Priority Queue
//Code based from Algorithms 4th Edition by Robert Sedgewick and Kevin Wayne
//https://algs4.cs.princeton.edu/24pq/IndexMinPQ.java.html

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {

	private int maxN;        // maximum number of elements on PQ
	private int n;           // number of elements on PQ
	private int[] pq;        // binary heap using 1-based indexing
	private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
	private Key[] keys;      // keys[i] = priority of i

	@SuppressWarnings("unchecked")
	public IndexMinPQ(int maxN) {
		if (maxN < 0) throw new IllegalArgumentException();
		this.maxN = maxN;
		n = 0;
		keys = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
		pq   = new int[maxN + 1];
		qp   = new int[maxN + 1];                   // make this of length maxN??
		for (int i = 0; i <= maxN; i++)
			qp[i] = -1;
	}

	public void insert(int i, Key key) {
		validateIndex(i);
		if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
		n++;
		qp[i] = n;
		pq[n] = i;
		keys[i] = key;
		swim(n);
	}


	public int delMin() {
		if (n == 0) throw new NoSuchElementException("Priority queue underflow");
		int min = pq[1];
		exch(1, n--);
		sink(1);
		assert min == pq[n+1];
		qp[min] = -1;        // delete
		keys[min] = null;    // to help with garbage collection
		pq[n+1] = -1;        // not needed
		return min;
	}

	private void sink(int k) {
		while (2*k <= n) {
			int j = 2*k;
			if (j < n && greater(j, j+1)) j++;
			if (!greater(k, j)) break;
			exch(k, j);
			k = j;
		}
	}

	public void decreaseKey(int i, Key key) {
		validateIndex(i);
		if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
		if (keys[i].compareTo(key) == 0)
			throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
		if (keys[i].compareTo(key) < 0)
			throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");
		keys[i] = key;
		swim(qp[i]);
	}


	public boolean isEmpty() {
		return n == 0;
	}

	private boolean greater(int i, int j) {
		return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
	}

	private void exch(int i, int j) {
		int swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
		qp[pq[i]] = i;
		qp[pq[j]] = j;
	}


	private void swim(int k) {
		while (k > 1 && greater(k/2, k)) {
			exch(k, k/2);
			k = k/2;
		}
	}

	public boolean contains(int i) {
		validateIndex(i);
		return qp[i] != -1;
	}

	private void validateIndex(int i) {
		if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
		if (i >= maxN) throw new IllegalArgumentException("index >= capacity: " + i);
	}


	public Iterator<Integer> iterator() { return new HeapIterator(); }

	private class HeapIterator implements Iterator<Integer> {
		// create a new pq
		private IndexMinPQ<Key> copy;

		// add all elements to copy of heap
		// takes linear time since already in heap order so no keys move
		public HeapIterator() {
			copy = new IndexMinPQ<Key>(pq.length - 1);
			for (int i = 1; i <= n; i++)
				copy.insert(pq[i], keys[pq[i]]);
		}

		public boolean hasNext()  { return !copy.isEmpty();                     }
		public void remove()      { throw new UnsupportedOperationException();  }

		public Integer next() {
			if (!hasNext()) throw new NoSuchElementException();
			return copy.delMin();
		}
	}
}

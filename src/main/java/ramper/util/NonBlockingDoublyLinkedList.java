package ramper.util;

/**
 * A doubly linked list implementation that exposes delete and add in O(1).
 * Note that this implementation is not synchronized, but exposes certain
 * methods that are thread-safe and non-blocking as described in
 *
 * Simple, Fast, and Practical Non-Blocking and Blocking Concurrent Queue Algorithms
 * by Maged M. Michael and Michael L. Scott.
 *
 * These operations are {@link #add(Node node)} and {@link #pop()}.
 * They correspond to <i>enqueue</i> and <i>dequeue</i> in the above paper.
 * Large parts of this code are copied from {@link java.util.concurrent.ConcurrentLinkedQueue}.
 *
 * Importantly this implementation exposes {@link #add(Node node)} and {@link #remove(Node node)}
 * with the nodes explicitly, i.e. the Node objects need to be created by the user. The reason
 * for this decision is that the Node removal might be managed togehter with some other data
 * structure.
 */
public class NonBlockingDoublyLinkedList<E>
{
    public static class Node<E> {
        volatile E item;
        volatile Node<E> next, prev = null;


        /**
         * Constructs a new node.  Uses relaxed write because item can
         * only be seen after publication via casNext.
         */
        Node(E item) {
            UNSAFE.putObject(this, itemOffset, item);
        }

        boolean casItem(E cmp, E val) {
            return UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
        }

        boolean casPrev(Node<E> cmp, Node<E> val) {
            return UNSAFE.compareAndSwapObject(this, prevOffset, cmp, val);
        }

        boolean casNext(Node<E> cmp, Node<E> val) {
            return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
        }


        public E getItem() {
            return this.item;
        }

        public void setItem(E item) {
            this.item = item;
        }

        protected void addBefore(Node<E> next) {
            if (next.prev != null) {
                next.prev.next = this;
            }
            this.prev = next.prev;
            next.prev = this;
            this.next = next;
        }

        protected void unlink() {
            if (this.prev != null) {
                this.prev.next = this.next;
            }
            if (this.next != null) {
                this.next.prev = this.prev;
            }
        }

        // Unsafe mechanics

        private static final sun.misc.Unsafe UNSAFE;
        private static final long itemOffset;
        private static final long prevOffset;
        private static final long nextOffset;

        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = Node.class;
                itemOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("item"));
                prevOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("prev"));
                nextOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("next"));
            }catch (Exception e) {
                throw new Error(e);
            }
        }
    };

    private transient volatile Node<E> head, tail;
    long size = 0;


    /**
     * Creates a {@code ConcurrentLinkedQueue} that is initially empty.
     */
    public NonBlockingDoublyLinkedList() {
        head = tail = new Node<E>(null);
    }

    public Node<E> getHead() {
        return this.head;
    }

    public Node<E> getTail() {
        return this.tail;
    }

    public long getSize() {
        return size;
    }

    /**
     * Thread-safe
     */
    public void add(Node<E> node) {
        // while (true) {
        //     Node<E> tail = this.tail;
        //     Node<E> next = this.tail.next;
        // }
        node.next = this.head;
        if (this.head != null) this.head.prev = node;
        if (this.tail == null) this.tail = node;
        this.head = node;
        this.size++;
    }


    /**
     * Thread-safe
     */
    public Node pop() {
        Node res = this.tail;
        if (this.tail != null) {
            this.tail = this.tail.prev;
            // only one item in the list
            if (this.tail == null) {
                this.head = null;
            } else {
                this.tail.next = null;
            }
            this.size--;
        }
        return res;
    }

    /**
     * Thread-safe
     */
    public void pop(int n) {
        while (n-- != 0 && this.tail != null) {
            this.pop();
        }
    }

    public void remove (Node<E> node) {
        if (this.head == node) {
            this.head = node.next;
        }
        if (this.tail == node) {
            this.tail = node.prev;
        }
        node.unlink();
        this.size--;
    }

    public void touch (Node<E> node) {
        this.remove(node);
        this.add(node);
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello Doubly Linked List" );
    }

    /**
     * Throws NullPointerException if argument is null.
     *
     * @param v the element
     */
    private static void checkNotNull(Object v) {
        if (v == null)
            throw new NullPointerException();
    }

    private boolean casTail(Node<E> cmp, Node<E> val) {
        return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
    }

    private boolean casHead(Node<E> cmp, Node<E> val) {
        return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
    }

    // Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;
    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = NonBlockingDoublyLinkedList.class;
            headOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("head"));
            tailOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("tail"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}

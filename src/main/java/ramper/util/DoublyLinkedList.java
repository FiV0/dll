package ramper.util;

/**
 * A doubly linked list implementation that exposes delete and add in O(1).
 * Note that this implementation is not synchronized.
 */
public class DoublyLinkedList<E>
{
    public class Node {
        E item;
        Node next, prev = null;

        public Node() {
            this.item = null;
        }

        public Node(E item) {
            this.item = item;
        }

        E getItem() {
            return this.item;
        }

        protected void addBefore(Node next) {
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
    };

    Node head, tail = null;
    int size = 0;

    public Node getHead() {
        return this.head;
    }

    public Node getTail() {
        return this.tail;
    }

    public int getSize() {
        return size;
    }

    public void add(Node node) {
        node.next = this.head;
        if (this.head != null) this.head.prev = node;
        if (this.tail == null) this.tail = node;
        this.head = node;
        this.size++;
    }

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

    public void pop(int n) {
        while (n-- != 0 && this.tail != null) {
            this.tail = this.tail.prev;
            this.size--;
        }
        if (this.tail == null) {
            this.head = null;
        } else {
            this.tail.next = null;
        }
    }

    public void remove (Node node) {
        if (this.head == node) {
            this.head = node.next;
        }
        if (this.tail == node) {
            this.tail = node.prev;
        }
        node.unlink();
        this.size--;
    }

    public void touch (Node node) {
        this.remove(node);
        this.add(node);
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello Doubly Linked List" );
    }
}

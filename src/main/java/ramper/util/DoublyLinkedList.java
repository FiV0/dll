package ramper.util;

/**
 * A doubly linked list implementation that exposes delete and add in O(1).
 * Note that this implementation is not synchronized.
 *
 */
public class DoublyLinkedList<E>
{
    public static class Node<E> {
        E item;
        Node<E> next, prev = null;

        public Node() {
            this.item = null;
        }

        public Node(E item) {
            this.item = item;
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
    };

    Node<E> head, tail = null;
    int size = 0;

    public Node<E> getHead() {
        return this.head;
    }

    public Node<E> getTail() {
        return this.tail;
    }

    public int getSize() {
        return size;
    }

    public void add(Node<E> node) {
        node.next = this.head;
        if (this.head != null) this.head.prev = node;
        if (this.tail == null) this.tail = node;
        this.head = node;
        this.size++;
    }

    public Node<E> pop() {
        Node<E> res = this.tail;
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
}

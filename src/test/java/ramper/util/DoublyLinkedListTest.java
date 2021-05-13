package ramper.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Unit test for DoublyLinkedList.
 */
public class DoublyLinkedListTest
{
    public <E> ArrayList<E> doublyLinkedListAsArray(DoublyLinkedList<E> dll) {
        ArrayList<E> res = new ArrayList<E>(dll.getSize());
        while (dll.getSize() > 0) {
            res.add(dll.pop().getItem());
        }
        return res;
    }

    @Test
    public void simpleAdds()
    {
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
        dll.add(new DoublyLinkedList.Node<Integer>(1));
        dll.add(new DoublyLinkedList.Node<Integer>(2));
        dll.add(new DoublyLinkedList.Node<Integer>(3));
        ArrayList<Integer> res = doublyLinkedListAsArray(dll);
        assertArrayEquals(new Integer [] {1,2,3}, res.toArray());
    }

    @Test
    public void addsAndPops()
    {
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
        dll.add(new DoublyLinkedList.Node<Integer>(1));
        dll.add(new DoublyLinkedList.Node<Integer>(2));
        dll.add(new DoublyLinkedList.Node<Integer>(3));
        dll.pop();
        dll.pop();
        dll.pop();
        assertEquals(0, dll.getSize());
    }


    @Test
    public void addAndPopInterleave()
    {
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
        dll.add(new DoublyLinkedList.Node<Integer>(1));
        dll.pop();
        dll.add(new DoublyLinkedList.Node<Integer>(2));
        dll.add(new DoublyLinkedList.Node<Integer>(3));
        dll.pop();
        dll.pop();
        assertEquals(0, dll.getSize());
    }

    @Test
    public void remove()
    {
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
        dll.add(new DoublyLinkedList.Node<Integer>(1));
        DoublyLinkedList.Node<Integer> n2 = new DoublyLinkedList.Node<Integer>(2);
        dll.add(n2);
        dll.add(new DoublyLinkedList.Node<Integer>(3));
        dll.remove(n2);
        DoublyLinkedList.Node<Integer> n4 = new DoublyLinkedList.Node<Integer>(4);
        dll.add(n4);
        dll.add(new DoublyLinkedList.Node<Integer>(5));
        dll.remove(n4);
        ArrayList<Integer> res = doublyLinkedListAsArray(dll);
        assertArrayEquals(new Integer [] {1,3,5}, res.toArray());
    }

    // corner cases

    @Test
    public void emptyPop()
    {
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
        dll.add(new DoublyLinkedList.Node<Integer>(1));
        dll.pop();
        assertEquals(null, dll.pop());
        assertEquals(0, dll.getSize());
        ArrayList<Integer> res = doublyLinkedListAsArray(dll);
        assertArrayEquals(new Integer [] {}, res.toArray());
    }


    @Test
    public void frontRemove()
    {
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
        DoublyLinkedList.Node<Integer> n1 = new DoublyLinkedList.Node<Integer>(1);
        dll.add(n1);
        dll.add(new DoublyLinkedList.Node<Integer>(2));
        assertEquals(2, dll.getSize());
        dll.remove(n1);
        assertEquals(1, dll.getSize());
        ArrayList<Integer> res = doublyLinkedListAsArray(dll);
        assertArrayEquals(new Integer [] {2}, res.toArray());
    }

    @Test
    public void backRemove()
    {
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
        dll.add(new DoublyLinkedList.Node<Integer>(1));
        DoublyLinkedList.Node<Integer> n2 = new DoublyLinkedList.Node<Integer>(2);
        dll.add(n2);
        assertEquals(2, dll.getSize());
        dll.remove(n2);
        assertEquals(1, dll.getSize());
        ArrayList<Integer> res = doublyLinkedListAsArray(dll);
        assertArrayEquals(new Integer [] {1}, res.toArray());
    }
}

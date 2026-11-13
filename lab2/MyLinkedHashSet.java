public class MyLinkedHashSet {
    private static class Entry {
        Object key;
        Entry next;        // для розв'язання колізій у бакеті хеш-таблиці
        Entry before, after; // для збереження порядку вставки (Linked-структура)

        Entry(Object key, Entry next) {
            this.key = key;
            this.next = next;
        }
    }

    private Entry[] table;
    private int size;
    private static final int CAPACITY = 16;

    private Entry head; // перший доданий елемент
    private Entry tail; // останній доданий елемент

    public MyLinkedHashSet() {
        table = new Entry[CAPACITY];
        size = 0;
    }

    private int hash(Object key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % CAPACITY;
    }

    public boolean add(Object key) {
        int bucket = hash(key);
        Entry current = table[bucket];

        // Перевіряємо дублікати
        while (current != null) {
            if (key == null && current.key == null) return false;
            if (key != null && key.equals(current.key)) return false;
            current = current.next;
        }

        // Створюємо новий вузол
        Entry newEntry = new Entry(key, table[bucket]);
        table[bucket] = newEntry;

        // Пов'язуємо в загальний ланцюжок ітерації (Linked)
        if (head == null) {
            head = newEntry;
            tail = newEntry;
        } else {
            tail.after = newEntry;
            newEntry.before = tail;
            tail = newEntry;
        }

        size++;
        return true;
    }

    public boolean contains(Object key) {
        int bucket = hash(key);
        Entry current = table[bucket];
        while (current != null) {
            if (key == null && current.key == null) return true;
            if (key != null && key.equals(current.key)) return true;
            current = current.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    // Метод для демонстрації збереження порядку
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        Entry current = head;
        while (current != null) {
            result[i++] = current.key;
            current = current.after;
        }
        return result;
    }
}

public class MyCache {
    private static class CacheNode {
        Object key;
        Object value;
        CacheNode next; // для колізій в бакеті
        CacheNode prev, nextOrder; // для черги LRU (молодий/старий)

        CacheNode(Object key, Object value, CacheNode next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private CacheNode[] table;
    private final int capacity;
    private int size;
    private static final int TABLE_CAPACITY = 32;

    private CacheNode head; // Найстаріші елементи (кандидати на видалення)
    private CacheNode tail; // Найновіші елементи

    public MyCache(int capacity) {
        this.capacity = capacity;
        this.table = new CacheNode[TABLE_CAPACITY];
        this.size = 0;
    }

    private int hash(Object key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % TABLE_CAPACITY;
    }

    public Object get(Object key) {
        int bucket = hash(key);
        CacheNode node = table[bucket];
        while (node != null) {
            if ((key == null && node.key == null) || (key != null && key.equals(node.key))) {
                moveToTail(node); // Оновили статус використання
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    public void put(Object key, Object value) {
        int bucket = hash(key);
        CacheNode node = table[bucket];

        // 1. Оновлення існуючого ключа
        while (node != null) {
            if ((key == null && node.key == null) || (key != null && key.equals(node.key))) {
                node.value = value;
                moveToTail(node);
                return;
            }
            node = node.next;
        }

        // 2. Якщо ліміт кешу вичерпано, видаляємо найстаріший елемент з голови (head)
        if (size >= capacity) {
            removeOldest();
        }

        // 3. Додаємо новий елемент
        CacheNode newNode = new CacheNode(key, value, table[bucket]);
        table[bucket] = newNode;
        addToTail(newNode);
        size++;
    }

    private void addToTail(CacheNode node) {
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.nextOrder = node;
            node.prev = tail;
            tail = node;
        }
    }

    private void moveToTail(CacheNode node) {
        if (node == tail) return;

        if (node == head) {
            head = node.nextOrder;
            if (head != null) head.prev = null;
        } else {
            node.prev.nextOrder = node.nextOrder;
            node.nextOrder.prev = node.prev;
        }

        node.nextOrder = null;
        node.prev = tail;
        tail.nextOrder = node;
        tail = node;
    }

    private void removeOldest() {
        if (head == null) return;
        CacheNode oldest = head;

        // Видаляємо з черги LRU
        head = head.nextOrder;
        if (head != null) head.prev = null;
        else tail = null;

        // Видаляємо з бакета хеш-таблиці
        int bucket = hash(oldest.key);
        CacheNode current = table[bucket];
        CacheNode prevNode = null;

        while (current != null) {
            if (current == oldest) {
                if (prevNode == null) table[bucket] = current.next;
                else prevNode.next = current.next;
                size--;
                break;
            }
            prevNode = current;
            current = current.next;
        }
    }

    public int size() {
        return size;
    }
}

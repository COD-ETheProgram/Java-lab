import java.util.Arrays;

public class TestApp {
    public static void main(String[] args) {
        System.out.println("1. Тестування списків (MyArrayList та MyLinkedList)");
        testList(new MyArrayList(), "MyArrayList");
        testList(new MyLinkedList(), "MyLinkedList");

        System.out.println("\n2. Тестування LinkedHashSet");
        testLinkedHashSet();

        System.out.println("\n3. Тестування кешу");
        testCache();
    }

    private static void testList(MyList list, String listType) {
        System.out.println("\nТест для " + listType);
        
        // Перевірка базового додавання в кінець
        list.add("Альфа");
        list.add("Бета");
        list.add("Гамма");
        System.out.println("Після add(): " + Arrays.toString(list.toArray()) + " | Розмір: " + list.size());

        // Перевірка додавання за індексом всередину
        list.add(1, "Вставлено_В_Центр");
        System.out.println("Після add(index): " + Arrays.toString(list.toArray()));

        // Перевірка масового додавання в кінець
        Object[] batch1 = {"Дельта", "Епсилон"};
        list.addAll(batch1);
        System.out.println("Після addAll(batch): " + Arrays.toString(list.toArray()));

        // Перевірка масового додавання за індексом
        Object[] batch2 = {"111", "222"};
        list.addAll(2, batch2);
        System.out.println("Після addAll(index, batch): " + Arrays.toString(list.toArray()));

        // Перевірка отримання та оновлення значення
        System.out.println("Елемент за індексом 4: " + list.get(4));
        list.set(4, "Оновлена_Гамма");
        System.out.println("Після set(4): " + list.get(4));

        // Перевірка пошуку індексу
        System.out.println("Індекс елемента 'Дельта': " + list.indexOf("Дельта"));
        System.out.println("Індекс неіснуючого: " + list.indexOf("НемаєМеня"));

        // Перевірка видалення
        Object removed = list.remove(2);
        System.out.println("Видалено елемент: " + removed);
        System.out.println("Фінальний стан списку: " + Arrays.toString(list.toArray()) + " | Розмір: " + list.size());
    }

    private static void testLinkedHashSet() {
        MyLinkedHashSet set = new MyLinkedHashSet();

        // Додаємо унікальні елементи в певному порядку
        System.out.println("Додаємо Київ: " + set.add("Київ"));
        System.out.println("Додаємо Львів: " + set.add("Львів"));
        System.out.println("Додаємо Одеса: " + set.add("Одеса"));

        // Спроба додати дублікат (має повернути false і не змінити структуру)
        System.out.println("Додаємо дублікат Львів: " + set.add("Львів"));

        // Перевірка наявності елементів
        System.out.println("Містить 'Київ'?: " + set.contains("Київ"));
        System.out.println("Містить 'Харків'?: " + set.contains("Харків"));

        // Перевірка збереження порядку вставки при виведенні
        System.out.println("Вміст сету (має зберегти порядок вставки):");
        System.out.println(Arrays.toString(set.toArray()) + " | Розмір: " + set.size());
    }

    private static void testCache() {
        // Створюємо кеш із лімітом (Capacity) у 3 елементи
        MyCache cache = new MyCache(3);

        System.out.println("Заповнюємо кеш до ліміту (A, B, C)...");
        cache.put("A", "Значення_A");
        cache.put("B", "Значення_B");
        cache.put("C", "Значення_C");
        System.out.println("Розмір кешу: " + cache.size());

        // Читаємо елемент 'A' — тепер він стає найновішим, а 'B' — найстарішим кандидатом на видалення
        System.out.println("Читаємо 'A': " + cache.get("A"));

        System.out.println("Додаємо новий елемент 'D' (кеш заповнений, 'B' має видалитися)...");
        cache.put("D", "Значення_D");

        // Перевіряємо, хто залишився в пам'яті
        System.out.println("Значення 'B' (має бути null, бо його витіснив LRU алгоритм): " + cache.get("B"));
        System.out.println("Значення 'A' (має залишитися): " + cache.get("A"));
        System.out.println("Значення 'C' (має залишитися): " + cache.get("C"));
        System.out.println("Значення 'D' (має залишитися): " + cache.get("D"));
        System.out.println("Фінальний розмір кешу (не перевищує 3): " + cache.size());
    }
}

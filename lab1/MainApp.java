import java.io.File;
import java.lang.reflect.Constructor;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class MainApp {
    private static final String JAVA_FILE_PATH = "TestModule.java";
    private static final String CLASS_FILE_PATH = "TestModule.class";

    public static void main(String[] args) throws Exception {
        System.out.println("Програма запущена. Змініть вміст TestModule.java для перевантаження");

        long lastModified = 0;

        // Програма постійно «висить» у пам'яті завдяки нескінченному циклу
        while (true) {
            File javaFile = new File(JAVA_FILE_PATH);

            if (javaFile.exists() && javaFile.lastModified() > lastModified) {
                lastModified = javaFile.lastModified();
                System.out.println("\nВиявлено зміни у файлі TestModule.java. Починаємо компіляцію...");

                // 1. Автоматична компиляція за допомогою JavaCompiler API
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                if (compiler == null) {
                    System.err.println("Помилка: Не знайдено JDK компілятор. Переконайтеся, що ви запускаєте програму через JDK.");
                    Thread.sleep(5000);
                    continue;
                }

                // Запуск компілятора (null параметри для використання стандартних потоків вводу/виводу)
                int compilationResult = compiler.run(null, null, null, javaFile.getPath());

                if (compilationResult == 0) {
                    System.out.println("Клас успішно скомпільовано.");

                    // 2. Створення нового лоадеру для кожної зміни
                    DynamicClassLoader loader = new DynamicClassLoader(CLASS_FILE_PATH);

                    // 3. Завантаження свіжої версії класу
                    Class<?> testModuleClass = loader.loadClass("TestModule");

                    // 4. Створення екземпляру за допомогою рефлексії
                    Constructor<?> constructor = testModuleClass.getConstructor();
                    Object testModuleInstance = constructor.newInstance();

                    // 5. Виклик System.out.println(t)
                    System.out.println("Результат виконання: " + testModuleInstance);
                } else {
                    System.err.println("Помилка: Не вдалося скомпілювати файл. Перевірте синтаксис.");
                }
            }

            // Пауза в 1 секунду, щоб не перевантажувати процесор постійними перевірками
            Thread.sleep(1000);
        }
    }
}

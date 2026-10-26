import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DynamicClassLoader extends ClassLoader {
    private final String classPath;

    public DynamicClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // Самостійне завантаження модулю із ігноруванням батьківського лоадеру
        if ("TestModule".equals(name)) {
            try {
                byte[] classBytes = Files.readAllBytes(Paths.get(classPath));
                return defineClass(name, classBytes, 0, classBytes.length);
            } catch (IOException e) {
                throw new ClassNotFoundException("Не вдалося прочитати байт-код класу", e);
            }
        }
        // Усі інші системні класи (як String, Object тощо) завантажуються стандартно
        return super.loadClass(name);
    }
}

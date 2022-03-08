import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * \* Created with IntelliJ IDEA.
 * \* Author: Prekrasnov Sergei
 * \* Date: 03.03.2022
 * \*  ----- group JAVA-27 -----
 * \* Description: Потоки ввода-вывода. Работа с файлами
 * \                 Задача 2: Сохранение
 * \
 */
public class Main {
    static File saveGames = new File("C:\\Games\\savegames");

    public static void main(String[] args) throws IOException {

        GameProgress savedGames1 = new GameProgress(99, 2, 2, 1.232);
        GameProgress savedGames2 = new GameProgress(89, 1, 5, 12.43);
        GameProgress savedGames3 = new GameProgress(55, 3, 7, 19.99);
        saveGame("C:\\Games\\savegames\\SaveGame-1.dat", savedGames1);
        saveGame("C:\\Games\\savegames\\SaveGame-2.dat", savedGames2);
        saveGame("C:\\Games\\savegames\\SaveGame-3.dat", savedGames3);

        List<String> zipData = Files.walk(Paths.get(String.valueOf(saveGames)))
                .filter(Files::isRegularFile)
                .filter(x->x.getFileName().toString().startsWith("SaveGame-"))
                .filter(x->x.getFileName().toString().endsWith("dat"))
                .map(x -> x.toAbsolutePath().toString())
                .collect(Collectors.toList());

        zipFiles("C:\\Games\\savegames\\GameSaved.zip", zipData);
        zipData.forEach(x->{
            new File(x).delete();
        });
    }

    public static void saveGame(String path, GameProgress data) {
        if (!saveGames.exists()) saveGames.mkdirs();
        try (ObjectOutputStream saveData = new ObjectOutputStream(new FileOutputStream(path))) {
            saveData.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void zipFiles(String path, List<String> zipData) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
            for (String arr : zipData) {
                try (FileInputStream fis = new FileInputStream(arr)) {
                    ZipEntry entry = new ZipEntry(Paths.get(arr).getFileName().toString());
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[16384];
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zout.write(buffer, 0, len);
                    }
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

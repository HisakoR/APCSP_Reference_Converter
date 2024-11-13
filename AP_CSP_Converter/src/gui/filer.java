package gui;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class filer {
    public static String userPath = "src/output";

    public ArrayList<String> readFile(String route){
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(route))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("目标文件不可用");
        }
        return lines;
    }

    public void saveData(ArrayList<String> lines) throws IOException {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String infoNam = time.format(formatter);
        String directoryPath = userPath;
        String filePath = userPath + "\\" + infoNam + ".txt";

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdir()) {
                System.out.println("文件夹创建成功");
            }
            else {
                System.out.println("文件夹创建失败");
            }
        }
        else {
            System.out.println("文件夹已存在");
        }
        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                System.out.println("文件创建成功");
            }
            else {
                System.out.println("文件已存在");
            }
        }
        catch (IOException e) {
            System.out.println("文件创建失败");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getUserPath(){
        return userPath;
    }
    public void setUserPath(String newPath){
        userPath = newPath;
    }
}

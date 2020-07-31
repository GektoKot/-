package com.javarush.task.task31.task3101;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
Проход по дереву файлов
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        File resultFileAbsolutePath = new File(args[1]);
        File allFilesContent = new File(resultFileAbsolutePath.getParent() + "\\allFilesContent.txt");

        if (FileUtils.isExist(resultFileAbsolutePath)) {
            FileUtils.renameFile(resultFileAbsolutePath, allFilesContent);
        }

        ArrayList<Path> files = new ArrayList<>();

        Files.walkFileTree(Paths.get(args[0]), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toFile().length() <= 50
                        && !file.toFile().isDirectory()
                        && !file.equals(allFilesContent.toPath())) {
                    files.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        Collections.sort(files, new Comparator<Path>() {
            @Override
            public int compare(Path o1, Path o2) {
                return o1.getFileName().compareTo(o2.getFileName());
            }
        });


//        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(allFilesContent, true))) {
//            for (Path path : files) {
//                try (BufferedReader fileReader = new BufferedReader(new FileReader(path.toFile()))){
//                    while (fileReader.ready()) {
//                        fileWriter.write(fileReader.read());
//                    }
//                    fileWriter.write("\n");
//                }
//            }
//        }
        try (FileOutputStream fos = new FileOutputStream(allFilesContent, true)) {
            for (Path path : files) {
                try (FileInputStream fis = new FileInputStream(path.toFile())) {
                    while (fis.available() > 0) {
                        fos.write(fis.read());
                    }
                    fos.write(System.lineSeparator().getBytes());
                    fos.flush();
                }
            }
        }
    }
}

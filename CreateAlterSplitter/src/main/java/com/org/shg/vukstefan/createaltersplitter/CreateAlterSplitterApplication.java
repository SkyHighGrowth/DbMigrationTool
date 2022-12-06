package com.org.shg.vukstefan.createaltersplitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class CreateAlterSplitterApplication {


    public static final String MYSQL_FOLDER_PATH = "C:\\Users\\DELL\\Desktop\\MYSQL\\";

    public static void main(String[] args) {
        SpringApplication.run(CreateAlterSplitterApplication.class, args);
        File rootFile = new File(MYSQL_FOLDER_PATH);
        File[] files = rootFile.listFiles();
        for (File file : files) {
            String newFileName = file.getName().replace("__create", "__alter");
            writeToFileInDirectory(removeAllDigit(newFileName), file);
        }

    }

    private static void writeToFileInDirectory(String newFileName, File originalFile) {
        FileReader fr;
        try {
            fr = new FileReader(originalFile);


            String ls = System.getProperty("line.separator");
            BufferedReader mainReader = new BufferedReader(fr);


            StringBuilder fullScriptNoComments = new StringBuilder();


            boolean currentlyInComment = false;

            String line = mainReader.readLine();


            while (line != null) {


                if (isLineAMultiLineCommentStart(line)) {
                    currentlyInComment = true;
                    if (isLineAMultiLineCommentStart(line) && !isLineAMultiLineCommentEnd(line)) {
                        removeLine(line, originalFile);
                    }
                }

                if (!currentlyInComment && !isLineAComment(line)) {
                    if (line.equals("USE `anon`;")) {
                        removeLine(line, originalFile);
                    } else {
                        fullScriptNoComments.append(line);
                        fullScriptNoComments.append(ls);
                    }

                } else if (!currentlyInComment && isLineAComment(line)) {
                    removeLine(line, originalFile);
                } else if (currentlyInComment && (!isLineAComment(line) && !isLineAMultiLineCommentStart(line) && !isLineAMultiLineCommentEnd(line))) {
                    removeLine(line, originalFile);
                }


                if (isLineAMultiLineCommentEnd(line) && currentlyInComment) {
                    currentlyInComment = false;
                    removeLine(line, originalFile);
                }

                line = mainReader.readLine();
            }
            String alterScriptString = fullScriptNoComments.substring(0);


            Reader inputString = new StringReader(alterScriptString);
            BufferedReader alterScriptBufferedReader = new BufferedReader(inputString);
            FileWriter alterWriter;

            if (alterScriptString.contains("ALTER TABLE")) {
                File myObj = new File(MYSQL_FOLDER_PATH + newFileName);
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                } else {
                    System.out.println("File already exists.");
                }
                alterWriter = new FileWriter(myObj);
            } else {
                mainReader.close();
                alterScriptBufferedReader.close();
                return;
            }


            BufferedWriter bufferedAlterWriter = new BufferedWriter(alterWriter);
            BufferedWriter bufferedOriginalWriter = new BufferedWriter(new FileWriter(originalFile));

            boolean inAlter = false;
            while ((line = alterScriptBufferedReader.readLine()) != null) {
                if (!inAlter && isLineStartOfAlter(line)) {
                    inAlter = true;
                    bufferedAlterWriter.write(line);
                    bufferedAlterWriter.write(ls);
                    removeLine(line, originalFile);
                } else if (inAlter) {
                    bufferedAlterWriter.write(line);
                    bufferedAlterWriter.write(ls);

                } else {
                    bufferedOriginalWriter.write(line);
                    bufferedOriginalWriter.write(ls);
                }
            }

            bufferedOriginalWriter.close();
            bufferedAlterWriter.close();
            mainReader.close();
            alterScriptBufferedReader.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void removeLine(String lineContent, File file) throws IOException {
        List<String> out = Files.lines(file.toPath()).filter(line -> !line.contains(lineContent)).collect(Collectors.toList());
        Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static boolean isLineAComment(String line) {
        if (line.contains("--")) return true;

        return false;
    }

    private static boolean isLineStartOfAlter(String line) {
        if (line.contains("ALTER TABLE")) return true;

        return false;
    }


    private static boolean isLineAMultiLineCommentEnd(String line) {
        if (line.contains("*/")) return true;

        return false;
    }

    private static boolean isLineAMultiLineCommentStart(String line) {
        if (line.contains("/*")) return true;

        return false;

    }

    private static String removeAllDigit(String str)
    {
        // Converting the given string
        // into a character array
        char[] charArray = str.toCharArray();
        String result = "";

        boolean firstUnderscore=false;
        // Traverse the character array
        for (int i = 0; i < charArray.length; i++) {
            if(charArray[i]=='_') {
                firstUnderscore = true;
            }

            // Check if the specified character is not digit
            // then add this character into result variable
            if (!Character.isDigit(charArray[i])) {
                result = result + charArray[i];
            }
            else if(firstUnderscore){
                result=result+charArray[i];
            }
        }

        // Return result
        return result;
    }


}



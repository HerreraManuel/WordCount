/* Manuel Herrera

Counting and file reading methods obtained from:
www.vogella.com/tutorials/JavaIO/article.html
https://stackoverflow.com/questions/4094119/counting-number-of-words-in-a-file
https://stackoverflow.com/questions/16802147/java-i-want-to-read-a-file-name-from-command-line-then-use-a-bufferedreader-to

Counting comments and source line of code implementation by
https://gist.github.com/shiva27/1432290
Some minor adjustments made to make it for this sprint.

Source Lines of Code determined by
"A SLOC Counting Standard." (2007)
by Nguyen, Vu, Sophia Deeds-Rubin, Thomas Tan and B. Bohm
 */

import picocli.*;
import picocli.CommandLine.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


class SourceFileCounts extends Metrics{
    StreamTokenizer inTokenizer;
    FileReader fileReader;
    BufferedReader buffReader;

        int noCmtSize;

        public void inSourceCounter(String fileName) throws Exception {
        fileReader = new FileReader(fileName);
        buffReader = new BufferedReader(fileReader);
        inTokenizer = new StreamTokenizer(buffReader);
        inTokenizer.slashStarComments(true);
        inTokenizer.slashSlashComments(true);
        int word;
        int currentLine = 0;
        noCmtSize = 0;

        while ((word = inTokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
            if (currentLine != inTokenizer.lineno()) {
                currentLine = inTokenizer.lineno();
                noCmtSize++;
            }
        }
    }

}

class FileCounts {
    String fileName;

}


@Command(name = "Metrics", footer = "\nCSC131: Individual Project - Sprint 2. Design document available.", description =
        "\nIf no option is declared, prints lines, words, character, comment lines, source line counts for each " +
                "FILE, and a total line if more than one FILE is specified. Otherwise, prints specified request for FILE(s).\n" +
                "SLOC is determined by: Nguyen, Vu et al. \"A SLOC Counting Standard.\" (2007).\n",
        sortOptions = false)

class Metrics {
    public long numLines;
    public long numWords;
    public long numChars;
    public long totalLines;
    public long totalWords;
    public long totalChars;
    public String fileExt;

    public Metrics() {}

    @Parameters(paramLabel = "Stat Requested", description = "Contents to display") String[] files;
    @Option(names = {"-l", "--lines"}, description = "Print the number of lines") boolean lineStat;
    @Option(names = {"-w", "--words"}, description = "Print the number of words") boolean wordStat;
    @Option(names = {"-c", "--characters"}, description = "Print the number of characters") boolean charStat;
    @Option(names = {"-s", "--sourceLines"}, description = "Print the number of source lines") boolean srcLnStat;
    @Option(names = {"-C", "--commentlines"}, description = "Print the number of comment lines") boolean cmtStat;
    @Option(names = {"-h", "--help"}, usageHelp = true,  description = "Display this help and exit") boolean help;

    public void run(String[] inFiles) throws Exception {
        SourceFileCounts in = new SourceFileCounts();
        try {
            boolean headerTrigger = true;
            for (String temp : inFiles) {
                lineCount(temp);
                wordAndCharCount(temp);
                printStats(temp, headerTrigger);
                in.inSourceCounter(temp);
                numLines = numWords = numChars = 0;
                headerTrigger = false;
            }
            if(inFiles.length > 1) printTotals();
        }catch (IOException e) { e.printStackTrace(); }
    }

    public void lineCount(String currFile) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(currFile));
        numLines = reader.lines().count();
        totalLines += numLines;
    }

    public void wordAndCharCount(String currFile) throws Exception{
        BufferedReader wordReader = new BufferedReader(new FileReader(currFile));
        String lineHolder;
        while ((lineHolder = wordReader.readLine()) != null){
            numChars += lineHolder.length();
            if (!lineHolder.isEmpty()){
                String[] lineOfWords = lineHolder.trim().split("\\s+");
                numWords += lineOfWords.length;
            }
        }
        totalChars += numChars;
        totalWords += numWords;
    }

    public boolean getExtension(File tempFile){
        fileExt = tempFile.getName().substring(tempFile.getName().indexOf("."));
        if (fileExt.equals(".java") || fileExt.equals(".c") || fileExt.equals(".h") ||
            fileExt.equals(".cpp") || fileExt.equals(".hpp")) return true;
        return false;
    }

    public void printTotals() {
        if (!lineStat && !wordStat && !charStat && !cmtStat && !srcLnStat)
            System.out.printf("%-5d %-5d %-10d %-4s", totalLines, totalWords, totalChars, "total\n");
            else{
            if (lineStat) System.out.printf("%-10d", totalLines);
            if (wordStat) System.out.printf("%-10d", totalWords);
            if (charStat) System.out.printf("%-10d", totalChars);
            System.out.printf("%-20s", "total\n");
        }
            System.out.println();
    }

    public void printStats(String temp, boolean trigger){
        if (!lineStat && !wordStat && !charStat && !cmtStat && !srcLnStat)
            System.out.printf("%-5d %-5d %-10d %-4s\n", numLines, numWords, numChars, temp);
        else {
            if ((cmtStat || srcLnStat) && trigger) {
                if (lineStat) System.out.printf("%-10s", "Lines");
                if (wordStat) System.out.printf("%-10s", "Words");
                if (charStat) System.out.printf("%-10s", "Chars");
                if (cmtStat) System.out.printf("%-10s", "Cmmts");
                if (srcLnStat) System.out.printf("%-10s", "SrcLines");
                System.out.printf("%4s", "File\n");
            }
            if (lineStat) System.out.printf("%-10d", numLines);
            if (wordStat) System.out.printf("%-10d", numWords);
            if (charStat) System.out.printf("%-10d", numChars);
            System.out.printf("%8s\n", temp);
        }
    }

    public static void main(String[] args){
        Metrics m = CommandLine.populateCommand(new Metrics(), args);
        if (args.length < 1 || m.help) CommandLine.usage(new Metrics(), System.out);
        else {
            try {
                m.run(m.files);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
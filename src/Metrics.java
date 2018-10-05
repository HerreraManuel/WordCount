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


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

class CodeReader{
    public long numOfCmts;
    public long numOfSrcLns;
    public long totalCmts;
    public long totalSrcLns;

    //Separate readLine function for Comments and Source Lines
    public void readLines(File inFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        boolean commentStart = true;
        String currLine = null;
        while ((currLine = reader.readLine()) != null) {
            currLine = currLine.trim();
            if (currLine.startsWith("//")) numOfCmts++;
            if (commentStart) {
                if (commentEnd(currLine)) {
                    currLine = currLine.substring(currLine.indexOf("*/") + 2).trim();
                    commentStart = false;
                }
            }
            if (commentStart(currLine)) { commentStart = true;            }
            if (isSourceCodeLine(currLine)) numOfSrcLns++;
        }
        totalCmts += numOfCmts; totalSrcLns += numOfSrcLns;
    }

    public boolean commentStart(String line){
        int index = line.indexOf("/*");
        if (index < 0) return false;
        return !commentEnd(line.substring((index + 2)));
    }

    public boolean commentEnd(String line){
        int index = line.indexOf("*/");
        if (index < 0) return false;
        else{
            String subString = line.substring(index).trim();
            if (subString.startsWith("//")) {numOfCmts++; return true;}
            if (commentStart(subString)) return false;
            else {numOfCmts++; return true;}
        }
    }

    public boolean isSourceCodeLine(String lineOfCode){
        boolean isSourceCode = false;
        if ("".equals(lineOfCode) || lineOfCode.startsWith("//")) return isSourceCode;
        int index = lineOfCode.indexOf("/*");
        if (index != 0) return true;
        else {
            while (lineOfCode.length() > 0) {
                lineOfCode = lineOfCode.substring(index + 2);
                int endOfComment = lineOfCode.indexOf("*/");
                        if (endOfComment < 0) return false;
                        if (endOfComment == lineOfCode.length() - 2) return false;
                        else {
                            String substring = lineOfCode.substring(endOfComment + 2).trim();
                            if ("".equals(substring) || substring.indexOf("//") == 0) return false;
                            else {
                                if (substring.startsWith("/*")) { lineOfCode = substring; continue;}
                                return true;
                            }
                        }
            }
        }
        return isSourceCode;
    }

    public void setNumOfCmts(long newNum) { this.numOfCmts = newNum;}
    public void setNumOfSrcLngs(long newNum) { this.numOfSrcLns = newNum;}
    public long getNumOfCmts() { return numOfCmts;}
    public long getNumOfSrcLns() { return numOfSrcLns;}
    public long getTotalCmts() { return totalCmts; }
    public long getTotalSrcLns() { return totalSrcLns; }
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

    @Parameters(paramLabel = "Stat Requested", description = "Contents to display") List<File> files;
    @Option(names = {"-l", "--lines"}, description = "Print the number of lines") boolean lineStat;
    @Option(names = {"-w", "--words"}, description = "Print the number of words") boolean wordStat;
    @Option(names = {"-c", "--characters"}, description = "Print the number of characters") boolean charStat;
    @Option(names = {"-s", "--sourceLines"}, description = "Print the number of source lines") boolean srcLnStat;
    @Option(names = {"-C", "--commentlines"}, description = "Print the number of comment lines") boolean cmtStat;
    @Option(names = {"-h", "--help"}, usageHelp = true,  description = "Display this help and exit") boolean help;

    public void run(List<File> inFiles) throws Exception {
        try {
            CodeReader codeIn = new CodeReader();
            boolean headerTrigger = true;
            for (File temp : inFiles) {
                lineCount(temp);
                wordAndCharCount(temp);
                if (getExtension(temp)) codeIn.readLines(temp);
                printStats(temp, codeIn, headerTrigger);
                numLines = numWords = numChars = 0;
                codeIn.setNumOfCmts(0); codeIn.setNumOfSrcLngs(0);
                headerTrigger = false;
            }
            if(inFiles.size() > 1) printTotals(codeIn);
        }catch (IOException e) { e.printStackTrace(); }
    }

    public void lineCount(File currFile) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(currFile));
        numLines = reader.lines().count();
        totalLines += numLines;
    }

    public void wordAndCharCount(File currFile) throws Exception{
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

    public void printTotals(CodeReader in) {
        if (lineStat) System.out.printf("%-10d", totalLines);
        if (wordStat) System.out.printf("%-10d", totalWords);
        if (charStat) System.out.printf("%-10d", totalChars);
        if (cmtStat) System.out.printf("%-10d", in.getTotalCmts());
        if (srcLnStat) System.out.printf("%-10d", in.getTotalSrcLns());
        System.out.printf("%-10s", "total\n");
    }

    public void printStats(File temp, CodeReader in, boolean trigger){
        if (!lineStat && !wordStat && !charStat && !cmtStat && !srcLnStat)
            System.out.printf("%-5d %-5d %-8d %10s\n", numLines, numWords, numChars, temp.getName());
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
            if (cmtStat) System.out.printf("%-10d", in.getNumOfCmts());
            if (srcLnStat) System.out.printf("%-10d", in.getNumOfSrcLns());
            System.out.printf("%8s\n", temp.getName());
        }
    }

    public static void main(String[] args){
        if (args.length < 1) CommandLine.usage(new Metrics(), System.out);
        else {
            try {
                Metrics m = CommandLine.populateCommand(new Metrics(), args);
                m.run(m.files);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
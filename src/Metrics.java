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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DistinctCode{
    public String[] javaOperators = {"import", "[]", "()", ".", "", "++", "--", "+", "-", "!", "~", "(type)",
            "*", "/", "%", "<<", ">>", ">>>", "<", "<=", ">", ">=", "instanceof", "==", "!=", "&",
            "^", "|", "&&", "||", "?:", "=", "+=", "-=", "*=", "/=", "%=", "{", "}", "{}", "(", ")",
            "&=", ">>>=", "#", "##", "^=", "?"};

    public String[] javaOperands = {"bool", "byte", "int", "float", "char", "double", "long",
            "short", "signed", "unsigned", "void", "break", "case", "catch", "try",
            "const", "null", "while", "public", "static", "main", "else", "for", "while"};

    public String[] cOperators = {"[]", "()", "->", ".", "!", "~", "+", "-", "++", "--", "&", "*"
            , "sizeof", "(type)", "/", "%", "<<", ">>", "<", "<=", ">", ">=", "==", "!=", "&&",
            "||", "|", "^", "?:", "=", "*=", "/=", "%=", "+=", "-=", "&=", "^=", "|=", "<<=",
            ">>=", ","};

    public String[] cOperands = {"int", "float", "char", "double", "long", "short", "signed", "unsigned",
            "void"};
}

 class HalsteadCounts extends DistinctCode{
     public int numOfDistinctOperators;
     public int numOfDistinctOperands;
     public ArrayList<String> operatorHolder;
     public ArrayList<String> operandHolder;
     public int totalOperators;
     public int totalOperands;
     public int progVocab;
     public int progLen;
     public double calProgLen;
     public double progVol;
     public double progDiff;
     public double progEffort;
     public double progTimeReq;
     public double progDelivBugs;
     public File fileName;

    void runHal(File inFile, int distOperators, int distOperands, int tOperators, int tOperands) throws Exception{
        numOfDistinctOperands = distOperands;
        numOfDistinctOperators = distOperators;
        totalOperators = tOperators;
        totalOperands = tOperands;
        progVocab = 0;
        progLen = 0;
        calProgLen = 0;
        progVol = 0;
        progDiff =0;
        progEffort = 0;
        progTimeReq = 0;
        progDelivBugs = 0;
        operatorHolder = new ArrayList<String>();
        operandHolder = new ArrayList<String>();
        getDistinctOperators(inFile);
        getDistinctOperands(inFile);
        vocab(); length(); calLen(); vol(); diff();
        effort(); timeReq(); bugs();
    }

    void setFileName(File fName) {fileName = fName;}

    void vocab() { progVocab = numOfDistinctOperators + numOfDistinctOperands;}

    void length() { progLen = totalOperators + totalOperands;}

    void calLen() { calProgLen = numOfDistinctOperators * (Math.log(numOfDistinctOperators) / Math.log(2))
            + numOfDistinctOperands * (Math.log(numOfDistinctOperands) / Math.log(2));}

    void vol() { progVol = calProgLen * (Math.log(progVocab) / Math.log(2));}

    void diff() {progDiff = (numOfDistinctOperators / 2) * (totalOperands / 1);}

    void effort() { progEffort = progDiff * progVol;}

    void timeReq() { progTimeReq = progEffort / 18;}

    void bugs() { progDelivBugs = Math.pow(progTimeReq, (2/3)) / 3000;}

    public int getDistinctOperators(File inFile) throws Exception{
        numOfDistinctOperators = 0;
        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        StreamTokenizer inToken = new StreamTokenizer(reader);
        int word;
        while ((word = inToken.nextToken()) != StreamTokenizer.TT_EOF){
            String currWord = inToken.sval;
            if (!isOperator(currWord)) {
                totalOperators++;
                if (!inOperatorList(currWord)) {
                    numOfDistinctOperators++;
                    operatorHolder.add(currWord);
                }
            }
       }
       return numOfDistinctOperators;
    }

    public int getDistinctOperands(File inFile) throws Exception{
        numOfDistinctOperands = 0;
        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        StreamTokenizer inToken = new StreamTokenizer(reader);
        int word;
        while ((word = inToken.nextToken()) != StreamTokenizer.TT_EOF){
            String currWord = inToken.sval;
            if (isOperand(currWord)) {
                totalOperands++;
                if (!inOperandList(currWord)) {
                    numOfDistinctOperands++;
                    operandHolder.add(currWord);
                }
            }
        }
        return numOfDistinctOperands;
    }

    int getTotalOperators() { return totalOperators; }

    int getTotalOperands() { return totalOperands; }

    boolean isOperator(String word) { return Arrays.asList(javaOperators).contains(word); }

    boolean inOperatorList(String word) { return operatorHolder.contains(word);} //operatorHolder.stream().anyMatch(word::equals); }

    boolean isOperand(String word) { return Arrays.asList(javaOperands).contains(word); }

    boolean inOperandList(String word) { return operandHolder.contains(word);} //operandHolder.stream().anyMatch(word::equals); }

}

class CodeReader {
    public int numOfCmts;
    public int numOfSrcLns;
    public int totalCmts;
    public int totalSrcLns;
    String fileExt;
    String currentFile;

    //Separate readLine function for Comments and Source Lines
    public int readSrcLines(File inFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        boolean commentStart = true;
        String currLine = null;
        while ((currLine = reader.readLine()) != null) {
            currLine = currLine.trim();
            if (isSourceCodeLine(currLine)) numOfSrcLns++;
        }
        return numOfSrcLns;
        //totalCmts += numOfCmts; totalSrcLns += numOfSrcLns;
    }

    public int readCmtLines(File inFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        boolean commentStart = true;
        String currLine = null;
        while ((currLine = reader.readLine()) != null) {
            currLine = currLine.trim();
            if (currLine.startsWith("//")) numOfCmts++;
            if (commentStart) { if (commentEnd(currLine)) {
            currLine = currLine.substring(currLine.indexOf("*/") + 2).trim();
             commentStart = false; }
            }
             if (commentStart(currLine)) { commentStart = true; }
        }
        return numOfCmts;
        //totalCmts += numOfCmts; totalSrcLns += numOfSrcLns;
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
        return false;
    }

   // public void setNumOfCmts(long newNum) { this.numOfCmts = newNum;}
   // public void setNumOfSrcLngs(long newNum) { this.numOfSrcLns = newNum;}
    public long getNumOfCmts() { return numOfCmts;}
    public long getNumOfSrcLns() { return numOfSrcLns;}
    public long getTotalCmts() { return totalCmts; }
    public long getTotalSrcLns() { return totalSrcLns; }


    public boolean isJava(File tempFile){
        fileExt = tempFile.getName().substring(tempFile.getName().indexOf("."));
        return fileExt.equals(".java");
    }

    public boolean isC(File tempFile){
        fileExt = tempFile.getName().substring(tempFile.getName().indexOf("."));
        if (fileExt.equals(".h") || fileExt.equals(".cpp") || fileExt.equals(".hpp")) return true;
        return false;
    }

    public boolean isCPP(File tempFile){
        fileExt = tempFile.getName().substring(tempFile.getName().indexOf("."));
        if (fileExt.equals(".c") || fileExt.equals(".h")) return true;
        return false;
    }
}

@Command(name = "Metrics", footer = "\nCSC131: Individual Project - Sprint 2. Design document available.", description =
        "\nIf no option is declared, prints lines, words, character, comment lines, source line counts for each " +
                "FILE, and a total line if more than one FILE is specified. Otherwise, prints specified request for FILE(s).\n" +
                "SLOC is determined by: Nguyen, Vu et al. \"A SLOC Counting Standard.\" (2007).\n",
        sortOptions = false)

class Metrics implements IMetrics{
    public int numLines;
    public int numWords;
    public int numChars;
    public int numSrcLines;
    public int numOfCmts;
    public int n1;
    public int n2;
    public int N1;
    public int N2;
    public int halVocab;
    public int halProgLen;
    public int halCalProgLen;
    public int halVol;
    public int halDiff;
    public int halEffort;
    public int halTime;
    public int halBugs;
    public int totalLines;
    public int totalWords;
    public int totalChars;
    public String currentFile;
    public File currFile;
    public HalsteadCounts in = new HalsteadCounts();

    public Metrics() {}

    @Parameters(paramLabel = "Stat Requested", description = "Contents to display") List<File> files;
    @Option(names = {"-l", "--lines"}, description = "Print the number of lines") boolean lineStat;
    @Option(names = {"-w", "--words"}, description = "Print the number of words") boolean wordStat;
    @Option(names = {"-c", "--characters"}, description = "Print the number of characters") boolean charStat;
    @Option(names = {"-s", "--sourceLines"}, description = "Print the number of source lines") boolean srcLnStat;
    @Option(names = {"-C", "--commentlines"}, description = "Print the number of comment lines") boolean cmtStat;
    @Option(names = "-H", description = "Print Halstead metrics") boolean halStat;
    @Option(names = {"-h", "--help"}, usageHelp = true,  description = "Display this help and exit") boolean help;

    public void run(List<File> inFiles) {
        try {
            CodeReader codeIn = new CodeReader();
            boolean headerTrigger = true;
            for (File temp : inFiles) {
                if(setPath(temp.getName())) { currentFile = temp.getName(); }
                in.setFileName(temp);
                numLines = getLineCount();
                numWords = getWordCount();
                numChars = getCharacterCount();
                numSrcLines = getSourceLineCount();
                numOfCmts = getCommentLineCount();
                n1 = getHalsteadn1();
                n2 = getHalsteadn2();
                N1 = getHalsteadN1();
                N2 = getHalsteadN2();
                in.runHal(temp, n1, n2, N1, N2);
                halVocab = getHalsteadVocabulary();
                halProgLen = getHalsteadProgramLength();
                halCalProgLen = getHalsteadCalculatedProgramLenght();
                halVol = getHalsteadVolume();
                halDiff = getHalsteadDifficulty();
                halEffort = getHalsteadEffort();
                halTime = getHalsteadTime();
                halBugs = getHalsteadBugs();
                printStats(temp, codeIn, headerTrigger);
                numLines = numWords = numChars = 0;
                headerTrigger = false;
            }
            if(inFiles.size() > 1) printTotals(codeIn);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public boolean setPath(String path) {
        currFile = new File(path);
        if (currFile.exists()) return true;
        else return false;
    }

    @Override
    public int getLineCount() {
        try {
            Long numOfLines;
            BufferedReader reader = new BufferedReader(new FileReader(currentFile));
            numOfLines = reader.lines().count();
            totalLines += numLines;
            return numOfLines.intValue();
        } catch (IOException e) {e.printStackTrace();}
        return 0;
    }

    @Override
    public int getWordCount() {
        try {
            BufferedReader wordReader = new BufferedReader(new FileReader(currentFile));
            String lineHolder;
            int numOfWords = 0;
            while ((lineHolder = wordReader.readLine()) != null) {
                if (!lineHolder.isEmpty()) {
                    String[] lineOfWords = lineHolder.trim().split("\\s+");
                    numOfWords += lineOfWords.length;
                }
            }
            return numOfWords;
        } catch(Exception e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int getCharacterCount() {
        try {
            BufferedReader wordReader = new BufferedReader(new FileReader(currentFile));
            String lineHolder;
            int charCount = 0;
            while ((lineHolder = wordReader.readLine()) != null) {
                charCount += lineHolder.length();
            }
            return charCount;
        } catch(Exception e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int getSourceLineCount() {
        try {
            CodeReader codeIn = new CodeReader();
            return codeIn.readSrcLines(currFile);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int getCommentLineCount() {
        try {
            CodeReader codeIn = new CodeReader();
            return codeIn.readCmtLines(currFile);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int getHalsteadn1() {
        try {
        return in.numOfDistinctOperators;
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int getHalsteadn2() {
        try {
            return in.numOfDistinctOperands;
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int getHalsteadN1(){
        try {
            return in.getTotalOperators();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int getHalsteadN2(){
        try {
            return in.getTotalOperands();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int getHalsteadVocabulary() {
        return in.progVocab;
    }

    @Override
    public int getHalsteadProgramLength() {
        return in.progLen;
    }

    @Override
    public int getHalsteadCalculatedProgramLenght() {
        return (int)in.calProgLen;
    }

    @Override
    public int getHalsteadVolume() {
        return (int)in.progVol;
    }

    @Override
    public int getHalsteadDifficulty() {
        return (int)in.progDiff;
    }

    @Override
    public int getHalsteadEffort() {
        return (int)in.progEffort;
    }

    @Override
    public int getHalsteadTime() {
        return (int)in.progTimeReq;
    }

    @Override
    public int getHalsteadBugs() {
        return (int)in.progDelivBugs;
    }

    @Override
    public boolean isSource(){
        currentFile = currentFile.substring(currentFile.lastIndexOf("."));
        if (currentFile.equals(".java") || currentFile.equals(".c") || currentFile.equals(".h") ||
                currentFile.equals(".cpp") || currentFile.equals(".hpp")) return true;
        return false;
    }

    void printHalsteadMetrics() {
        System.out.printf("%-10d %-10d %-10d %-10d %-10d %-10d %-10d %-10d",
                halVocab, halProgLen, halCalProgLen, halVol, halDiff,
                halEffort, halTime, halBugs);
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
            if ((cmtStat || srcLnStat || halStat) && trigger) {
                if (lineStat) System.out.printf("%-10s", "Lines");
                if (wordStat) System.out.printf("%-10s", "Words");
                if (charStat) System.out.printf("%-10s", "Chars");
                if (cmtStat) System.out.printf("%-10s", "Cmmts");
                if (srcLnStat) System.out.printf("%-10s", "SrcLines");
                if (halStat) { System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s",
                        "Vocab", "Lgth", "CalLen", "Vol", "Diff", "Effort", "T Req", "Bugs"); }
                System.out.printf("%8s", "File\n");
            }
            if (lineStat) System.out.printf("%-10d", numLines);
            if (wordStat) System.out.printf("%-10d", numWords);
            if (charStat) System.out.printf("%-10d", numChars);
            if (cmtStat) System.out.printf("%-10d", numOfCmts); //in.getNumOfCmts());
            if (srcLnStat) System.out.printf("%-10d", numSrcLines); //in.getNumOfSrcLns());
            if (halStat) printHalsteadMetrics();
            System.out.printf("%9s\n", temp.getName());
        }
    }
/*
    public static void main(String[] args){
        if (args.length < 1) CommandLine.usage(new Metrics(), System.out);
        else {
            try {
                Metrics m = CommandLine.populateCommand(new Metrics(), args);
                m.run(m.files);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    */
}
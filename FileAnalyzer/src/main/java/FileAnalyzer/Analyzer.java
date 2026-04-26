package FileAnalyzer;

// Apache PDFBox library - used for reading and extracting text from PDF files
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


// Apache POI library - used for reading and extracting text from Microsoft Word (.docx) files
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;



import java.io.*; // Java I/O classes - used for file input/output operations (reading/writing files)
import java.nio.file.*; // Java NIO (New I/O) - used for modern file handling (Paths, Files, Path)
import java.util.*; // Utility classes - collections, maps, lists, etc.
import java.util.regex.*; // Regular expressions - used for pattern matching in file analysis
import java.time.LocalDate; // Date handling - used to get current date for report naming

/**
 * Class responsible for analyzing file lines that are filtered
 * by a pattern that checks everything after a keyword or keyphrase
 * and printing the results as well as generating a new file with the results.
 */
public class Analyzer {

    // Map to store the file match counts
    private Map<String, Integer> fileMatchCounts = new HashMap<>();
    // Builder that stores the matched phrases along with the total lines of the given file
    StringBuilder matchString = new StringBuilder();
    private int countTotal = 0; // count for the total lines in the given file
    private int countMatch = 0; // count for the total matches in the given file

    // Method that analyzes all the lines in the given file using a keyword/keyphrase then finds the matches via a regex
    public void analyzeFile(String filePath, String key) throws IOException {

        List<String> lines; // List that contains all the lines in the file
        try {
            if (filePath.endsWith(".docx")) { // If check for the .docx (Word) extension
                XWPFDocument docWord = new XWPFDocument(new FileInputStream(filePath)); // Opens the Word file
                XWPFWordExtractor extractor = new XWPFWordExtractor(docWord); // Extracts text from the open Word file
                String text = extractor.getText(); // Variable that contains the extracted text
                lines = Arrays.asList(text.split("\\R")); // Splits the extracted text into individual lines
                docWord.close(); // Closes the Word file
            } else if (filePath.endsWith(".pdf")) { // If check for the .pdf extension
                PDDocument docPDF = PDDocument.load(new File(filePath)); // Opens the PDF file
                PDFTextStripper stripper = new PDFTextStripper(); // Extracts text from the open PDF file
                String text = stripper.getText(docPDF); // Variable that contains the extracted text
                lines = Arrays.asList(text.split("\\R")); // Splits the extracted text into individual lines
                docPDF.close(); // Closes the PDF file
            } else { // Else for any other file type
                lines = Files.readAllLines(Paths.get(filePath)); // Reads the plain text file line by line
            }
        }
        catch (IOException e) // Exception handling for IOException
        {
            // Message that gives a little insight to the user about the error and suggests to try again
            System.err.println("The following Exception occurred: " + e.getMessage() + ". Please try again.\n");
            throw e; // Throwing the exception to the main method
        }
        // Commented pattern filters for anything having zero or more spaces after the keyword/keyphrase
        //String patternString = "(?i)" + Pattern.quote(key) + "[\\s,]*(.*?)(?=(?:\\.\\.\\.|[.!?])\\s|\\R|$)";

        // Pattern below filters for anything after having one or more spaces or a comma after the keyword/keyphrase
        String patternString =
                "(?i)" + Pattern.quote(key) + "(?:\\s|,)\\s*(.*?)(?=(?:\\.\\.\\.|[.!?])\\s|\\R|$)";
        Pattern pattern = Pattern.compile(patternString); // Compiles the regular expression pattern (patternString) into a Pattern object

        for (String line : lines) { // For loop of all the lines in the file
            countTotal++; // Increasing the line total with each increment
            Matcher matcher = pattern.matcher(line); // Matcher object that applies the pattern to the line
            while (matcher.find()) { // Check for one or multiple matches in a line
                String match = matcher.group(); // String that takes the matching line
                countMatch++; // Increasing the match total when a match is found
                // The matching sentences and a count of the matching sentences is added to the fileMatchCounts Map
                fileMatchCounts.put(match, fileMatchCounts.getOrDefault(match, 0) + 1);
            }
        }

    }

    // Method that prints out the file match statistics
    public void printFileStats() {

        matchString.append("File Level Statistics:\n"); // Append to the matchString variable

        for (Map.Entry<String, Integer> entry : fileMatchCounts.entrySet()) { // for loop of all the keys and value in the fileMatchCounts Map
            // Append of the match text and the individual match count
            matchString.append("Occurrences: " + entry.getValue() + "  |  " + entry.getKey() + "\n");
        }

        matchString.append("<" + "-".repeat(20) + ">\n"); // Append of a separator to provide better result visibility
        matchString.append("Total lines: " + countTotal + "\n"); // Append of the total lines in the given file
        matchString.append("Total matches: " + countMatch); // Append of the total matches in the given file
        String output = matchString.toString(); // Converts the StringBuilder with the above appends to String
        System.out.println("\n" + output); // Print of the output string

    }

    // Method that generates the report file containing everything that was printed
    public void generateMatchFile() throws IOException {

        try {
            int fileNumber = 1; // initialization of a variable used to check for the existence of the output file
            String currentDate = LocalDate.now().toString(); // Current date used for the output file name
            Path outputPath = Paths.get("matchReport_" + currentDate + ".txt"); // Name of the .txt output file

            while (Files.exists(outputPath)) {
                outputPath = Paths.get("matchReport_" + currentDate + "_" + fileNumber + ".txt"); // name of the .txt output file if the while statement is still True
                fileNumber++; // incrementation for the name check
            }

            Files.writeString(outputPath, matchString); // Writes the content of the matchString to the output file
            System.out.println("The " + outputPath + " file was generated successfully"); // Print to keep the user informed
        }
        catch (IOException e) // Exception handling for IOException
        {
            // Message that gives a little insight to the user about the error and suggests to try again
            System.err.println("The following Exception occurred: " + e.getMessage() + ". Please try again.\n");
            throw e; // Throwing the exception to the main method
        }

    }

    // Main method to run the file analyzer
    public static void main(String[] args) {

        // Creates a new instance of the Analyzer class
        Analyzer analyzer = new Analyzer();

        try {
            // Analyze a file containing plain text, as well as PDF or Word files.
            analyzer.analyzeFile("FULL_PATH_TO_FILE\\test_log.log"
                    , "Error");
            // Print statistics
            analyzer.printFileStats();
            // Generates the matches report file
            analyzer.generateMatchFile();
        } catch (IOException e) { // Exception handling for IOException
            // Message that gives a little insight to the user about the error and suggests to try again
            System.err.println("File processing error: " + e.getMessage());
        }

    }
}
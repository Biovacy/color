import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;


public class color {

    // Default files
	private static final File WEB_ROOT = new File("./html");
	private static final String DEFAULT_FILE = "index.html";
	private static final String FILE_NOT_FOUND = "404.html";

    // port to listen connection
	private static final int PORT = 8080;

    // verbose mode (Output information in terminal)
	private static final boolean verbose = false;

	// Automaticly create a new generation between n amount of inputs
	private static final boolean automaticNewGeneration    = true;
	private static final int automaticNewGenerationAmount  = 3;
	private static 		 int automaticNewGenerationCounter = 0;

    // network connection
    private static networkInterface NI = new networkInterface();

    /*
        FUNCTIONS
    */

    public static void main(String[] args) {

		// HTTP server functionality
		try {
			ServerSocket serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nhttp://localhost:" + PORT);

			// Keep server alive
			while (true) {
				HTTPServer(serverConnect.accept());

				if (verbose == true) {
					System.out.println("Connecton opened. (" + new Date() + ")");
				}

			}

		} // end try
        catch (IOException e) {
            // Error management
			System.err.println("Server Connection error : " + e.getMessage());
		} // end catch
	} // end main

    private static void HTTPServer(Socket connect) {
        // A single thread that manages a request

        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;
		String data = null;
		byte[] fileData = null;
		int fileLength = 0;
		String content = null;

        try {
            // we read characters from the client via input stream on the socket
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));

            // we get character output stream to client (for headers)
            out = new PrintWriter(connect.getOutputStream());

            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // get first line of the request from the client
            String input = in.readLine();

            // we parse the request with a string tokenizer
            StringTokenizer parse = new StringTokenizer(input);

            // we get the HTTP method of the client
            String method = parse.nextToken().toUpperCase();

            // Gather the requested file
            fileRequested = parse.nextToken().toLowerCase();

            // Return default file
            if (fileRequested.endsWith("/")) {
                fileRequested += DEFAULT_FILE;
            }

			String[] fileAndData = splitFileAndData(fileRequested);

			data = fileAndData[1];
			fileRequested = fileAndData[0];

			if (verbose == true){
				System.out.println("File: " + fileRequested);
				System.out.println("Data: " + data);
			}

			if (fileRequested.contains("api") == true) {
				// The request was for an api call

				if (verbose == true) {
					System.out.println("API CALL:");
				}

				// Format the data for transmission
				content = "text/plain";
				fileData = apiManager(fileRequested, data);
				fileLength = fileData.length;

			}
			else {
				// The request was for a page

				// Gather the content on the webpage
				File file = new File(WEB_ROOT, fileRequested);
				fileLength = (int) file.length();
				content = getContentType(fileRequested);

				fileData = readFileData(file, fileLength);
			}

            // send HTTP Headers
            out.println("HTTP/1.1 200 OK");
            out.println("Server: Project Web Interface");
            out.println("Date: " + new Date());
            out.println("Content-type: " + content);
            out.println("Content-length: " + fileLength);

            // Allow cross requests
            // OBS. This should only be used with localhost, otherwise it is a security risk.
            out.println("Access-Control-Allow-Origin: *");
            out.println("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, Authorization");
            out.println("Access-Control-Allow-Credentials: true");
            out.println("Access-Control-Allow-Methods: GET,POST,PUT,DELETE,OPTIONS,HEAD");

            out.println(); // blank line between headers and content, very important !
            out.flush(); // flush character output stream buffer

            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();

            if (verbose == true) {
                System.out.println("File " + fileRequested + " of type " + content + " returned");
            }

        } // end try

        // Error management
        catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            }
            catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }

        }
        catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        }
        finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close(); // we close socket connection
            }
            catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }

    } // end run

    private static byte[] readFileData(File file, int fileLength) throws IOException {
        // open a file and read the contents.

		FileInputStream fileIn = null;
		byte[] fileData = new byte[fileLength];

		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null)
				fileIn.close();
		}

		return fileData;
	}

    private static String getContentType(String fileRequested) {
        // return supported MIME Types

        if (fileRequested.endsWith(".html"))
            return "text/html";
        else if (fileRequested.endsWith(".css")) {
            return "text/css";
        }
        else if (fileRequested.endsWith(".js")) {
            return "application/javascript";
        }
        else if (fileRequested.endsWith(".ico")) {
            return "image/x-icon";
        }
        else
            return "text/plain";
    }

    private static void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: Java HTTP Server from SSaurel : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbose == true) {
            System.out.println("File " + fileRequested + " not found");
        }
    }

    private static String[] splitFileAndData(String input){
        // Splits the request to a file and sent data.

        String data = null;
        String file = null;

		if (input.contains("?") == true) {

			String[] fileAndData = input.split("\\?");

			file = fileAndData[0];
			if (fileAndData.length != 1){
				data = fileAndData[1];
			}

		}
		else { // The request does not contain data
			file = input;
			data = null;
		}

        // TODO: CREATE THIS FUNCTION

		String[] returnArray = {file, data};

        return returnArray;
    }

	private static byte[] apiManager(String fileRequested, String data){
		// Manages the api
		// Input: fileRequested is the file which is being requested
		// Input: data is the data from the client side
		// Output: is the data being sent to the client

		String returnValue = "";

		if (fileRequested.equals("/api/getcolor") == true) {

			// Get new color
			int[] newColor = NI.generateColor();

			// Convert from int to hexadecimal
			String R = String.format("%X", newColor[0]);
			String G = String.format("%X", newColor[1]);
			String B = String.format("%X", newColor[2]);

			if(R.length() == 1) { R = "0" + R; }
			if(G.length() == 1) { G = "0" + G; }
			if(B.length() == 1) { B = "0" + B; }

			// Convert to a single RGB # color code
			returnValue = " #" + R + G + B;
		}

		else if (fileRequested.equals("/api/submitcolor") == true) {

			boolean answer = true;

			String[] colorAndAnswer = data.split("\\&");

			if (colorAndAnswer[1].equals("true") == true) {
				answer = true;
			}
			else {
				answer = false;
			}

			String rHex = Character.toString(colorAndAnswer[0].charAt(0)) + Character.toString(colorAndAnswer[0].charAt(1));
			String gHex = Character.toString(colorAndAnswer[0].charAt(2)) + Character.toString(colorAndAnswer[0].charAt(3));
			String bHex = Character.toString(colorAndAnswer[0].charAt(4)) + Character.toString(colorAndAnswer[0].charAt(5));

			int R = Integer.parseInt(rHex, 16);
			int G = Integer.parseInt(gHex, 16);
			int B = Integer.parseInt(bHex, 16);

			NI.colorDataInsert(R, G, B, answer);

			if (automaticNewGeneration == true){
				// Automaticly make a new generation

				automaticNewGenerationCounter++;

				if (automaticNewGenerationCounter % automaticNewGenerationAmount == 0) {
					NI.nextGeneration();
					System.out.println("Automatic next generation");
				}
			}

		}

		else if (fileRequested.equals("/api/evaluate")) {
			String rHex = Character.toString(data.charAt(0)) + Character.toString(data.charAt(1));
			String gHex = Character.toString(data.charAt(2)) + Character.toString(data.charAt(3));
			String bHex = Character.toString(data.charAt(4)) + Character.toString(data.charAt(5));

			int R = Integer.parseInt(rHex, 16);
			int G = Integer.parseInt(gHex, 16);
			int B = Integer.parseInt(bHex, 16);

			double likeProbability = NI.evaluate(R, G, B);

			returnValue = "" + likeProbability; // convert double to string
		}

		else if (fileRequested.equals("/api/newgeneration")) {

			NI.nextGeneration();

			returnValue = "";
		}

		byte[] returnValueBytes = returnValue.getBytes();

		return returnValueBytes;
	}




}

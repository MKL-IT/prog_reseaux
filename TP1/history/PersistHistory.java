
package history;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.StandardOpenOption;


public class PersistHistory {
	
	
	protected File file;
	protected String filename;
	

	// constructor PersistHistory
	public PersistHistory(String filename) {
		this.filename = filename;
	}
	
	public boolean createHistoryFile () {
        
        file = new File(filename);
        boolean success = false;

        try {
			if(file.createNewFile()){
				success = true;
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return success;
	}
	
	public boolean appendToHistoryFile (String line){

		Path filePath = Paths.get(filename);
		boolean success = false;

		try {
			Files.write(filePath, (line + System.lineSeparator()).getBytes(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	public List<String> readFromHistoryFile() {

		Path filePath = Paths.get(filename);
		List<String> historyList = new ArrayList<String>();
		try {
			historyList.addAll(Files.readAllLines(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return historyList;
	}
}

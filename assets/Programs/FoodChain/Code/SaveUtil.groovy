import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class SaveUtil {

	def high_score = 0;	
	SaveUtil(){
		
	}
	
	def loadScore(){
		Properties prop = new Properties();
		try(InputStream stream = new FileInputStream(new File("Programs/FoodChain/save.properties"))) {
		    prop.load(stream);
		    high_score = (prop.getProperty("high_score") != null) ? Integer.parseInt(prop.getProperty("high_score")) : 0;

		} catch (IOException | NumberFormatException ex) {
		    System.out.println(ex.getMessage());
		}
		return high_score
	}
	
	
	void saveScore(score){
		if(score > high_score){
			high_score = score
			Properties prop = new Properties();
			try(FileOutputStream stream = new FileOutputStream(new File("Programs/FoodChain/save.properties"))){
				prop.setProperty("high_score",high_score.toString())

				prop.store(stream, null)
				println "::: High score saved: $high_score :::"
			} catch (IOException | NumberFormatException ex) {
				System.out.println(ex.getMessage());
				println "::: High score save failure :::"
			}	
		}	
	}
}

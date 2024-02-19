import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
        String window = "";
        char chr;
        In in = new In(fileName);
        for (int i = 0; i < windowLength; i++)
        {
            chr = in.readChar();
            window += chr;
        }
        while (!in.isEmpty()) 
        {
            chr  = in.readChar();
            window += chr;
            if (CharDataMap.containsKey(window))
            {
                CharDataMap.get(window).update(chr);
            }
            else
            {
                List probs = new List();
                probs.addFirst(chr);
                CharDataMap.put(window, probs);
            }
            window = window.substring(1) + chr;
            
        for (List probs : CharDataMap.values())
            calculateProbabilities(probs);

        }

	}

           
    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {
        int countCharList = 0;
        Node currentnode = probs.getFirstNode();
        while (currentnode != null)
        {
            countCharList += currentnode.cp.count;
            currentnode = currentnode.next;
        }
        double currentcp = 0;
        currentnode = probs.getFirstNode();
        while (currentnode != null)
        {
            currentnode.cp.p = (double) currentnode.cp.count / countCharList;
            currentcp += (double) currentnode.cp.count / countCharList;;
            currentnode.cp.cp = currentcp;
            currentnode = currentnode.next;
        }
	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
        Node currentnode = probs.getFirstNode();
        double randomNumber = randomGenerator.nextDouble();
        while (randomNumber > currentnode.cp.cp)
        {
            currentnode = currentnode.next;
        }
        return currentnode.cp.chr;
	}

	

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText;
        }
        StringBuilder generatedText = new StringBuilder(initialText);
        String window = initialText.substring(initialText.length() - windowLength);
        while (generatedText.length() < textLength) {
            List charDataList = CharDataMap.get(window);
            if (charDataList == null) {
                break;
            }
            char nextChar = getRandomChar(charDataList);
            generatedText.append(nextChar);
            window = generatedText.substring(generatedText.length() - windowLength);
            }
        return generatedText.toString();      
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        //int windowLength = Integer.parseInt(args[0]);
        //String initialText = args[1];
       // int generatedTextLength = Integer.parseInt(args[2]);
       // Boolean randomGeneration = args[3].equals("random");
       // String fileName = args[4];
       // Create the LanguageModel object
     //  LanguageModel lm;
      // if (randomGeneration)
         //  lm = new LanguageModel(windowLength);
     //  else
       //    lm = new LanguageModel(windowLength, 20);
       //lm.train(fileName);
      // System.out.println(lm.generate(initialText, generatedTextLength));
 }
}

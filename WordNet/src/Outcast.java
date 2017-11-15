public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordnet) {
        wordNet = wordnet;
    }

    public String outcast(String[] nouns) {
        String outcast = "";
        int len = 0;

        for (String contender : nouns) {
            int distance = 0;
            for (String noun : nouns) {
                distance += wordNet.distance(contender, noun);
            }
            if (distance > len) {
                len = distance;
                outcast = contender;
            }
        }
        return outcast;
    }
}

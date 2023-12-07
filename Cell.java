public class Cell {
    private String word;
    private String type;

    public Cell(String word, String type) {
        this.word = word;
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getType() {
        return type;
    }

    public String getWord() {
        return word;
    }
}


package org.example.firstboot.model;

public class KeywordWeight {

    private String keyword;

    private double weight;

    public KeywordWeight() {
    }

    public KeywordWeight(
            String keyword,
            double weight) {

        this.keyword = keyword;
        this.weight = weight;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

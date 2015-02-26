package uk.ac.ncl.cs.team16.lloydsbankingapp.Models;

/**
 * Date: 29/01/2015
 * @author James Mountain
 * @version 1.0
 *
 */
public class DictionaryEntry {
    private String entryName;
    private String entryDescription;

    public DictionaryEntry(String name, String desc) {
        this.entryName = name;
        this.entryDescription = desc;
    }

    public String getEntryName() {
        return entryName;
    }

    public String getEntryDescription() {
        return entryDescription;
    }
}

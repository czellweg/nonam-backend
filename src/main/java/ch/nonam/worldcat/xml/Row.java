package ch.nonam.worldcat.xml;

import java.util.Arrays;
import java.util.List;

import ch.nonam.worldcat.domain.WorldcatResult;

/**
 * A {@link Row} instance corresponds to 1 {@link WorldcatResult} object, i.e.
 * the fully populated {@link WorldcatResult} has all the information needed to
 * produce the corresponding XML for importing.
 * 
 */
public class Row {

    private Col[] cols;

    public Row() {
        // initialises the list with the correct number of col entries
        this.cols = new Col[FileMakerUtility.getColumnNumber()];
    }

    public Row(List<Col> cols) {
        this.cols = cols.toArray(new Col[cols.size()]);
    }

    public Row(WorldcatResult wr) {
        // initialises the list with the correct number of col entries
        this.cols = new Col[FileMakerUtility.getColumnNumber()];
        addCol(new Col(wr.getN()), "nordAmerikaN");
        addCol(new Col(wr.getS()), "sonstigeLaenderS");
        addCol(new Col(wr.getMu()), "museologieMU");
        addCol(new Col(wr.getBereich()), "bereich");
        addCol(new Col(wr.getStandort()), "standort");
        addCol(new Col(wr.getLaufnummer()), "laufnummer");
        addCol(new Col(wr.getMedienart()), "medienart");
        addCol(new Col(wr.getTitle()), "titel");
        addCol(new Col(wr.getReihe()), "reihe");
        addCol(new Col(wr.getBand()), "band");
        addCol(new Col(wr.getAuthor()), "verfasser1");
        addCol(new Col("Autor"), "nebeneintrag1");
        addCol(new Col(wr.getOrt()), "ort");
        addCol(new Col(wr.getVerlag()), "verlag");
        addCol(new Col(wr.getJahr()), "jahr");
        addCol(new Col(wr.getIsbn()), "isbn");
        addCol(new Col(wr.getSprache()), "sprache");
        addCol(new Col(wr.getSeitenzahl()), "seitenzahl");
        addCol(new Col(wr.getSchlagwoerter()), "schlagwoerter");
    }

    public List<Col> getCols() {
        return Arrays.asList(this.cols);
    }

    public void setCols(List<Col> cols) {
        this.cols = cols.toArray(new Col[cols.size()]);
    }

    public void addCol(Col col, String uiName) {
        this.cols[FileMakerUtility.getColumnIndex(uiName)] = col;
    }

    public void addCol(Col col, int index) {
        this.cols[index] = col;
    }

}

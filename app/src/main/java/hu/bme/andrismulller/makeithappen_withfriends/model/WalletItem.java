package hu.bme.andrismulller.makeithappen_withfriends.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by Muller Andras on 2/22/2018.
 */

public class WalletItem extends SugarRecord {
    @Ignore
    int myId;
    String description;
    boolean bevetel;
    int ertek;
    String valuta;
    String category;
    long dateTime;

    public WalletItem() {
    }

    public WalletItem(String description, boolean bevetel, int ertek, String valuta, String category, long dateTime) {
        this.description = description;
        this.bevetel = bevetel;
        this.ertek = ertek;
        this.valuta = valuta;
        this.category = category;
        this.dateTime = dateTime;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBevetel() {
        return bevetel;
    }

    public void setBevetel(boolean bevetel) {
        this.bevetel = bevetel;
    }

    public int getErtek() {
        return ertek;
    }

    public void setErtek(int ertek) {
        this.ertek = ertek;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}

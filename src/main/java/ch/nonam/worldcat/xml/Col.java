package ch.nonam.worldcat.xml;

import java.util.List;

import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;

public class Col {

    public static final String N_DEFAULT = "WP Wenger-Peek Circumpolar Collection";
    public static final String S_DEFAULT = "01 Arktis";
    public static final String STANDORT_DEFAULT = "Bibliothek";
    public static final String MEDIENART_DEFAULT = "Buch";

    private List<Data> dataList = Lists.newArrayList();
    private String data;

    public Col(String data) {
        // this little hack was needed so that empty <DATA/> elements are in the
        // resulting XML
        if (StringUtils.isEmpty(data)) {
            // force empty DATA XML node
            this.dataList.add(new Data());
        } else {
            this.data = data;
        }
    }

    public String getData() {
        return this.data;
    }

}

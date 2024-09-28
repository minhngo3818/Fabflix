package XMLParser;

import java.util.ArrayList;
import java.util.HashMap;

public class GenreMap {
    private final HashMap<String, String> genreCategoryMap;

    public GenreMap() {
        // Initialize the map and populate it with genre mappings
        genreCategoryMap = new HashMap<>();
        genreCategoryMap.put("actn", "Violence");
        genreCategoryMap.put("advt", "Adventure");
        genreCategoryMap.put("avga", "Avant Garde");
        genreCategoryMap.put("biop", "Biopic");
        genreCategoryMap.put("camp", "Now - Camp");
        genreCategoryMap.put("cart", "Cartoon");
        genreCategoryMap.put("comd", "Comedy");
        genreCategoryMap.put("cnr", "Cops and Robbers");
        genreCategoryMap.put("ctxx", "Uncategorized");
        genreCategoryMap.put("disa", "Disaster");
        genreCategoryMap.put("docu", "Documentary");
        genreCategoryMap.put("dram", "Drama");
        genreCategoryMap.put("epic", "Epic");
        genreCategoryMap.put("faml", "Family");
        genreCategoryMap.put("fant", "Fantasy");
        genreCategoryMap.put("hist", "History");
        genreCategoryMap.put("horr", "Horror");
        genreCategoryMap.put("musc", "Musical");
        genreCategoryMap.put("myst", "Mystery");
        genreCategoryMap.put("noir", "Black");
        genreCategoryMap.put("porn", "Pornography");
        genreCategoryMap.put("psyc", "Psychology");
        genreCategoryMap.put("s.f.", "Sci-Fi");
        genreCategoryMap.put("susp", "Thriller");
        genreCategoryMap.put("surl", "Surreal");
        genreCategoryMap.put("romt", "Romantic");
        genreCategoryMap.put("tv", "TV show");
        genreCategoryMap.put("tvs", "TV series");
        genreCategoryMap.put("tvm", "TV miniseries");
        genreCategoryMap.put("west", "Western");

        // Consider them as inconsistency
        addMisspellings("Sci-Fi", "scfi", "S.F.");
//        addMisspellings("Adventure",  "Adctx", "Adct");
//        addMisspellings("Avant Garde", "Avant Garde");
//        addMisspellings("Biopic", "BioB", "BioPP", "BioPx", "BioG", "Bio");
//        addMisspellings("Now - Camp", "Camp", "CmR");
//        addMisspellings("Cartoon", "Cart", "Cart ");
//        addMisspellings("Comedy",  "Comdx");
//        addMisspellings("Cops and Robbers", "CnRb", "CnRb ", "CnRbb");
//        addMisspellings("Disaster", "Dist");
//        addMisspellings("Document",  "Duco", "Ducu", "Dramd");
//        addMisspellings("Drama", "Drama", "Dramd", "ram", "Draam");
//        addMisspellings("Fantasy", "FantH*");
//        addMisspellings("History", "H", "H**", "H*");
//        addMisspellings("Horror", "Hor", "H0");
//        addMisspellings("Musical", "Musical", "muusc", "Muscl", "Musicl", "stage musical");
//        addMisspellings("Mystery", "Mystp");
//        addMisspellings("Black",  "Nor");
//        addMisspellings("Pornography", "Porr");
//        addMisspellings("Sport", "sports");
//        addMisspellings("Surreal", "Surr", "surreal");
//        addMisspellings("Romantic",  "Romtx", "Ront");
//        addMisspellings("TV miniseries", "TVmini");
//        addMisspellings("Verite", "verite");
//        addMisspellings("Violence",  "Axtn", "Sctn", "Act");
//        addMisspellings("Western", "West1", "Weird");
//
//        // Exceptions
//        addMisspellings("Allegory", "Allegory");
//        addMisspellings("Road",  "Road");
//        addMisspellings("Anti-Drama", "anti-Dram");
//        addMisspellings("Wierd", "Weird");
    }


    private void addMisspellings(String correctValue, String... misspellings) {
        for (String misspelling : misspellings) {
            genreCategoryMap.put(misspelling.toLowerCase(), correctValue);
        }
    }

    public String getGenre(String categoryCode) {
        String acquiredGenre = null;

        acquiredGenre = genreCategoryMap.get(categoryCode.toLowerCase());

        // if (acquiredGenre == null) return "Uncategorized";

        return acquiredGenre;
    }

    public ArrayList<String> getTokenizedGenres(String categoryString) {
        ArrayList<String> results = new ArrayList<>();

        String firstResult = getGenre(categoryString);

        if (firstResult == null) return results;

        if (!firstResult.equals("Uncategorized")) {
            results.add(firstResult);
            return results;
        }

        String[] categories = categoryString.split("[\\s\\p{Punct}]+");
        if (categories.length > 1) {
            for (String cat : categories) {
                results.add(getGenre(cat));
            }
        } else {
            results.add(getGenre(categories[0]));
        }

        return results;
    }
}

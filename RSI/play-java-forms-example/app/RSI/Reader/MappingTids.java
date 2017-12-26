package RSI.Reader;

import RSI.Structure.CID;
import RSI.Structure.TID;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Created by Pedro Matos
 *
 * Class responsible for mapping the json files with the information
 * about the TIDs and CIDs to theirs respectively classes.
 *
 */
public class MappingTids {
    private HashMap<String, TID> TIDs = new HashMap<>();
    private HashMap<String, CID> CIDs = new HashMap<>();

    // this linked list contains all the json files that will need to be mapped into classes.
    private LinkedList<String> files_names;
    public MappingTids(LinkedList<String> files_names) {
        this.files_names = files_names;
    }

    /**
     * Name: StartMapping
     * The function that can be called by the user to start the mapping.
     * It will go by each of the names in the String Array and will call
     * another function according to the type of the json, if it's
     * a CID or a TID.
     *
     * To map we use the Gson tools.
     */
    public void StartMapping(){
        for(int i=0; i<files_names.size();i++){
            String[] split = files_names.get(i).split("/");
            String name = split[split.length-1];
            //remove .json
            String[] tmp_name = name.split(Pattern.quote("."));
            if(name.charAt(0) == 'T'){
                TIDs.put(tmp_name[0],MappingTID(files_names.get(i),name));
            }
            else{
                CIDs.put(tmp_name[0],MappingCID(files_names.get(i),name));
            }
        }
    }

    private TID MappingTID(String file_name,String name){
        Gson gson = new Gson();
        TID obj = null;
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(file_name));

            obj = gson.fromJson(br, TID.class);
            //remove .json
            String[] tmp_name = name.split(Pattern.quote("."));
            obj.setName(tmp_name[0]);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("aol");
        }
        return obj;
    }

    private CID MappingCID(String file_name,String name){
        Gson gson = new Gson();
        CID obj = null;
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(file_name));


            obj = gson.fromJson(br, CID.class);
            //remove .json
            String[] tmp_name = name.split(Pattern.quote("."));
            obj.setName(tmp_name[0]);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public HashMap<String, TID> getTIDs() {
        return TIDs;
    }

    public void setTIDs(HashMap<String, TID> TIDs) {
        this.TIDs = TIDs;
    }

    public HashMap<String, CID> getCIDs() {
        return CIDs;
    }

    public void setCIDs(HashMap<String, CID> CIDs) {
        this.CIDs = CIDs;
    }
}

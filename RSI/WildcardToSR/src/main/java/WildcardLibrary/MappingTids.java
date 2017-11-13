package WildcardLibrary;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
    private String mainFolder;
    public MappingTids(LinkedList<String> files_names, String mainFolder) {
        this.files_names = files_names;
        this.mainFolder = mainFolder;
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
            String[] split = files_names.get(i).split("\\.");
            String name = split[0];
            if(files_names.get(i).charAt(0) == 'T')
                TIDs.put(name,MappingTID(files_names.get(i),name));
            else
                CIDs.put(name,MappingCID(files_names.get(i),name));


        }
    }

    private TID MappingTID(String file_name,String name){
        Gson gson = new Gson();
        TID obj = null;
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(mainFolder+"/"+file_name));


            obj = gson.fromJson(br, TID.class);
            obj.setName(name);
            //System.out.println(obj.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private CID MappingCID(String file_name,String name){
        Gson gson = new Gson();
        CID obj = null;
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(mainFolder+"/"+file_name));


            obj = gson.fromJson(br, CID.class);
            obj.setName(name);
            //System.out.println(obj.getName());

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

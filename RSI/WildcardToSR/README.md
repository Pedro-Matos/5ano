# wildcard2dcm-sr, the tool!

Like has been stated in the README of the project, the goal of this application is to create a **DICOM SR** file after getting some csv and jsons as inputs. 
The application needs two csv files and both have a different purposes. Yet, both are necessary for the application to work.

####Ideal csv
The first one is called *idealCSV.csv* and contains the personal information to be coded in the DICOM SR. This one has the information of each DICOM in separated lines and each field is separated by **"," or "/"**. The first line will always contain the name that your doctor/instituation has given to each field. 
For example:

        Patient_Name,Patient_Age
        Pedro Matos/21
        Pedro Ferreira/27

As you can see, the first line, which will be global for the csv contains the *"tags"* and they are separated by a coma(,), while in the others line each information is separated by a slash(/). 

Yet, this is not enough for our application to code it into DICOM because it's tags are static and each user can create a csv easily by exporting the information from their website ou xlsx. This means that the *"tags"* in this csv are not the same used in DICOM SR terminology.
This means that it is necessary to have another csv where we specificate to which DICOM tag our tag matches.


####Mapping csv
This csv will create a brigde that allow the application to understand which user tag matches the DICOM SR tag. For that reason, this csv header tags always need to be in agreement with the tags used in the *ideal.csv*.
For example:

        Patient_Name,Patient_Age
        PatientName,PatientAge
        
In this csv the tags are always separated by coma and it is necessary to have just this two lines. The first one with the user tags and the second with the DICOM SR tags.


####Jsons
There are a lot of available templates to DICOM SR related to different medical areas, but our application is focused on mammography. For this reason, our template, which is coded in the jsons, will only work for mammography reports. 
All the information about this template can be found [here](http://dicom.nema.org/MEDICAL/dicom/current/output/chtml/part16/sect_BreastImagingReportTemplates.html).

The two big components of each template are the TIDs and CIDs. 
Both can be seen as nodes in a tree, but while the TIDs contain both information and a list of it's childs, the CIDs only contain information(they can be seen as terminal nodes).

In each Json file will be a TID or a CID, which is indicated by its name. For example, if the name is T202.json the file is coding a TID with the name of 202. 
Since each TID/CID will be one class object in our application, it is very important that the json follows a pre-defined structure. Otherwise, it won't be possible for the app to read it.

######TID format
Like has been said the TID contains both information and the indication for the next child node. In that case it is necessary to separate both information.
The area to contain the information of each triplet is nominated *"Triplet"* and the area to indicate the next child nodes is *"Childs"*.

        "Triplet":{
                "1":{
                        "CodeMeaning":"Observer Type",
                        "CodeValue": "121005",
                        "CodingSchemeDesignator": "DCM",
                        "CodingSchemeVersion": "",
                        "Mapping":"",
                        "Rel_With_Parent":"HAS OBS CONTEXT",
                        "VT":"CODE",
                        "NL":"h0",
                        "VM":"1",
                        "Req_Type":"MC",
                        "Condition":"IF Observer type is device",
                        "Constraint":"CID 270-“ObserverType”",
                        "TID_order":"1"
                        }
                },
        "Childs":{
                "2":{
                        "NL":"h1",
                        "Rel_With_Parent":"HAS OBS CONTEXT",
                        "VT":"INCLUDE",
                        "VM":"1",
                        "Req_Type":"MC",
                        "Condition":"IFF Row 1 value = (121006, DCM, “Person”) or Row 1 is absent",
                        "Constraint":"",
                        "Name":"T1003",
                        "Type":"TID",
                        "TID_order":"2",
                        "File_Name":"T1003.json"
                }
        }
                

As you can see there are two major blocks of information. Every tag of each group is coded by the information in the [NEMA](http://dicom.nema.org/MEDICAL/dicom/current/output/chtml/part16/sect_BreastImagingReportTemplates.html) like has been stated before. The changes made by our team are the *"TID_order"*. This new tag/field is very important because inside each node the information needs to be shown by a proper order, which is established with this tag. The program will see which is the order inside each node and will parse the information accordingly to it. 

######CID format
CIDs also contain information, but they don't have any information related to it's childs. This is because CIDs are alike terminal nodes. They only contain information and it's presented by order in the json file, which means that it isn't necessary to have the *TID_order* tag or another similar. All they code is the triplet information, the most important information in the DICOM SR terminology. 

        {
                "Triplet": {
                        "1":{
                                "CodeMeaning": "Volume",
                                "CodeValue": "G-D705",
                                "CodingSchemeDesignator": "SRT",
                                "CodingSchemeVersion": "",
                                "Mapping": ""
                        },
                        "2":{
                                "CodeMeaning": "Volume estimated from single 2D region",
                                "CodeValue": "121216",
                                "CodingSchemeDesignator": "DCM",
                                "CodingSchemeVersion": "",
                                "Mapping": ""
                        }
                }
        }

This is a short example from one of ours CIDs and you can see that the formatting of this json is simpler than the TID's one. All we have is Triplet information vs the triplet and childs information.

##Classes

**CsvParser**: Class responsible to map the information in the csv files. It will read both csv and store its information in two hash maps. This way the programm can know make the relation between the user tags and the correct tags. It contains a very simple *BufferedReader* and each combo of header/tag is put in the map.

**MappingTids**: Class responsible for mapping the json files containing the TID and CID information for their classes. Each TID and CID will be an object of it's respective class. This class is very simple because the the conversion between the information gathered by the *Reader* and the respective class is handled by [Gson library](http://dicom.nema.org/MEDICAL/dicom/current/output/chtml/part16/sect_BreastImagingReportTemplates.html) (Google library to handle jsons).

**DicomCreator**: This class is the most important of the whole project because it's responsible for the physical creation of the DICOM SR with all the information gathered by the other classes. The tree structure is already pre-defined and coded in the childs inside each TID, so it's only necessary to indicate by hard-code which is the first TID and the recursive function(**Writing_TID_Content**) will do the rest.
This actions will be done for each line of the csv creating the same number of SR's.

Inside this class theres another important function(**Write_Patient_Data**). This one will be responsible by the insertion of the personal user, doctor and facilities presented in the csv but dont have a direct relation to the tree model of the jsons.

This means that we are dealing with two types of information. One that is all structured in the json files and its related with the template we are using. The second is personal information and its not template related, which means that cannot be handled by the recursive function that creates the tree structure in the DICOM SR file.

##Use in command line:

java -jar W-V2-1.0-SNAPSHOT-jar-with-dependencies.jar inputs/ideal.csv inputs/mapping.csv mamo_tids/ outputs


        1º argument - Principal csv (idealCSV.csv)
        2º argument - csv mapping(mappingCSV.csv)
        3º argument - directory with all tids in json format(mamo_tids)
        4ª argument - directory to store all dicom sr. Just need to tell the name and it will be created
        
## Use with API:
To generate DICOM SR files with the API, the user must use the following class: Generator();

This class has methods that will receive as input the csv files with the information and mapping, and the directory with the jsons(template, in this case: mammography).

        Example of usage:
        Generator gn = new Generator();
        gn.configureMapping("inputs/idealCSV.csv","inputs/mappingCSV.csv","mamo_tids/");
        gn.generateSR("outputs/");


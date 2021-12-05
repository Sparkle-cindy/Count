package com.jnu.count.data;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBank<Count> {
    public static final String DATA_FILE_NAME = "data";
    private final Context context;
    List<Count> countItemList;

    public DataBank(Context context) {
        this.context=context;
    }

    @SuppressWarnings("unchecked")
    public List<Count> loadData() {
        countItemList=new ArrayList<>();
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(context.openFileInput(DATA_FILE_NAME));
            countItemList = (ArrayList<Count>) objectInputStream.readObject();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return  countItemList;
    }

    public void saveData() {
        ObjectOutputStream objectOutputStream=null;
        try{
            objectOutputStream = new ObjectOutputStream(context.openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE));
            objectOutputStream.writeObject(countItemList);
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

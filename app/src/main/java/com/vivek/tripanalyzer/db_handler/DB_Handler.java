package com.vivek.tripanalyzer.db_handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vivek.tripanalyzer.models.Trips;
import com.vivek.tripanalyzer.params.Params;

import java.util.ArrayList;
import java.util.List;

public class DB_Handler extends SQLiteOpenHelper {



    public DB_Handler(Context context) {
        super(context, Params.DB_NAME,null,Params.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + Params.TABLE_NAME +"(" +
                Params.KEY_ID +" INTEGER PRIMARY KEY," +
                Params.KEY_TRIP_NAME + " TEXT,"+
                Params.KEY_TRIP_KEY + " TEXT" +
                Params.KEY_MEMBER_NAME + "TEXT" +
                Params.KEY_MEMBER_ID + "TEXT" +  ")" ;
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       String DATABASE_ADD_MEMBER_NAME = "ALTER TABLE " + Params.TABLE_NAME + " ADD COLUMN " + Params.KEY_MEMBER_NAME + " TEXT;";
       String DATABASE_ADD_MEMBER_ID = "ALTER TABLE " + Params.TABLE_NAME + " ADD COLUMN " + Params.KEY_MEMBER_ID + " TEXT;";
        Log.d("tttttt"," before column ADDED");

        if(newVersion>oldVersion){
            db.execSQL(DATABASE_ADD_MEMBER_NAME);
            db.execSQL(DATABASE_ADD_MEMBER_ID);
            Log.d("tttttt","column ADDED");
        }

    }

    public void addTrip(Trips trips){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Params.KEY_TRIP_NAME,trips.getTrip_name());
        values.put(Params.KEY_TRIP_KEY,trips.getTrip_key());
        values.put(Params.KEY_MEMBER_NAME,trips.getMemberName());
        values.put(Params.KEY_MEMBER_ID,trips.getMemberId());
        db.insert(Params.TABLE_NAME,null,values);
        Log.d("dbvivek","trip has been successfully added");
        db.close();
    }

    public List<Trips> getAllTrips(){
        List<Trips> tripsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + Params.TABLE_NAME;
        Cursor cursor = db.rawQuery(select,null);


        if(cursor.moveToFirst()){
            do{
                Trips trips = new Trips();
                trips.setId(Integer.parseInt(cursor.getString(0)));
                trips.setTrip_name(cursor.getString(1));
                trips.setTrip_key(cursor.getString(2));
                trips.setMemberName(cursor.getString(3));
                trips.setMemberId(cursor.getInt(4));
                tripsList.add(trips);

            }while (cursor.moveToNext());
        }
        return tripsList;
    }

    public void deleteTrip(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Params.TABLE_NAME, Params.KEY_ID + "=?",
                new String[]{String.valueOf(id)});
    }
}

package com.tristar.utils;

import android.util.Log;

import com.tristar.object.AddressForServer;
import com.tristar.object.CourtAddressForServer;
import com.tristar.object.ProcessAddressForServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Sorting {

    public static Comparator<CourtAddressForServer> DateAsc_CourtAddressForServer = new Comparator<CourtAddressForServer>() {

        private String[] Time_separated;
        private int i = 0;

        @Override
        public int compare(CourtAddressForServer t1, CourtAddressForServer t2) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            String date_one = t1.getDateReceived();
            String date_two = t2.getDateReceived();

            try {

                Date one = dateFormat.parse(date_one);
                Date two = dateFormat.parse(date_two);
                i = one.compareTo(two);
            } catch (ParseException e) {
                e.printStackTrace();

                Log.d("sort_dueDate", e.getMessage() + "");
            }

            return i;

        }
    };




    public static Comparator<AddressForServer> DateAsc_AddressForServer = new Comparator<AddressForServer>() {

        private String[] Time_separated;
        private int i = 0;

        @Override
        public int compare(AddressForServer t1, AddressForServer t2) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            String date_one = t1.getDateReceived();
            String date_two = t2.getDateReceived();

            try {

                Date one = dateFormat.parse(date_one);
                Date two = dateFormat.parse(date_two);
                i = one.compareTo(two);
            } catch (ParseException e) {
                e.printStackTrace();

                Log.d("sort_dueDate", e.getMessage() + "");
            }

            return i;

        }
    };


    public static Comparator<CourtAddressForServer> DateDes_CourtAddressForServer = new Comparator<CourtAddressForServer>() {

        private String[] Time_separated;
        private int i = 0;

        @Override
        public int compare(CourtAddressForServer t1, CourtAddressForServer t2) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            String date_one = t1.getDateReceived();
            String date_two = t2.getDateReceived();

            try {
                Date one = dateFormat.parse(date_one);
                Date two = dateFormat.parse(date_two);
                i = two.compareTo(one);
            } catch (ParseException e) {
                e.printStackTrace();

                Log.d("sort_dueDate", e.getMessage() + "");
            }

            return i;

        }
    };


    public static Comparator<AddressForServer> DateDes_AddressForServer = new Comparator<AddressForServer>() {

        private String[] Time_separated;
        private int i = 0;

        @Override
        public int compare(AddressForServer t1, AddressForServer t2) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            String date_one = t1.getDateReceived();
            String date_two = t2.getDateReceived();

            try {

                Date one = dateFormat.parse(date_one);
                Date two = dateFormat.parse(date_two);
                i = two.compareTo(one);
            } catch (ParseException e) {
                e.printStackTrace();

                Log.d("sort_dueDate", e.getMessage() + "");
            }




            return i;

        }
    };




}
